package uk.org.ogsadai.dqp.lqp.optimiser.decorrelate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.CommonPredicate;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.AbstractJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.AntiSemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * This optimiser tries to remove correlations as much as possible. For now 
 * it deals with one particular scenario which is explained in detail below.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class DecorrelationOptimiser implements Optimiser 
{

    @Override
    public Operator optimise(Operator lqpRoot,
            RequestDQPFederation requestFederation,
            CompilerConfiguration compilerConfiguration,
            RequestDetails requestDetails) throws LQPException {
        
        decorrelateSelectPullUp(lqpRoot);
        return lqpRoot;
    }

    /**
     * 
     * This method pulls the predicate of a select operator up to the apply
     * operator above it. The select operator and any project operators in 
     * between are then removed.
     * 
     * Graphically, it starts with this structure,
     * 
     *                 Apply
     *               Semi-join (Condition1)
     *                   |
     *          ------------------
     *          |                 |
     *    Some operator 1 (ProjectNoD|Rename|Select)*
     *          |                 |  
     *         ...              Select (Condition2 containing correlated attributes)
     *                            |
     *                      Some operator 2
     *                            |
     *                           ...
     *                          
     * and reduces it to this,
     * 
     *               Semi-join (Condition1 AND Condition2)
     *                   |
     *          ------------------
     *          |                 |
     *    Some operator 1       Rename*
     *          |                 |  
     *         ...          Some operator 2
     *                            |
     *                           ...
     * 
     * ProjectNoD stands for a project operator with no derived attributes.
     * 
     * Note that there are three changes, the select operator and any project 
     * operators are removed, and the predicate of the select operator is 
     * appended to the predicate of the semi-join operator.
     * 
     * Removing selects with correlated attributes in them does not necessarily 
     * decorrelate a query because there may be other places where correlation 
     * occurs.
     * 
     * @param root
     *            root of the query plan to process
     * @throws LQPException
     *             if update after inserting the APPLY operator fails
     */
    public void decorrelateSelectPullUp(Operator root) throws LQPException 
    {
        List<Operator> allSelects = 
            OptimiserUtils.findOccurrences(root, OperatorID.SELECT);

        // Mapping from SELECT operator to the APPLY operator to which it
        // should be pulled up
        Map<Operator, ApplyOperator> selectToApplyMap = 
            new HashMap<Operator, ApplyOperator>();
        
        // Find the Selects that can be moved up to the Join
        ArrayList<SelectOperator> correlatedSelects = 
            new ArrayList<SelectOperator>();
        for (Operator select:allSelects) 
        {
            // Does this Select have any correlated attributes?
            Set<Attribute> attributes = select.getUsedAttributes();
            Set<Attribute> correlatedAttributes = new HashSet<Attribute>();
            for (Attribute attr:attributes) 
            {
                if (attr.isCorrelated()) 
                {
                    correlatedAttributes.add(attr);
                }
            }
            
            if (correlatedAttributes.size() == 0)
            {
                // No correlated attributes on this select so nothing to do 
                continue;
            }
            
            boolean acceptableParents = true;
            Operator child = select;
            Operator parent = select.getParent();
            // Project with no derived column, rename, or select parents are ok
            while (parent.getID() != OperatorID.NIL) {
                if (parent.getID() == OperatorID.PROJECT &&
                    !((ProjectOperator)parent).hasDerivedAttributes()) {
                    // Fine
                }else if (parent.getID() == OperatorID.RENAME) {
                    // Fine
                }else if(parent.getID() == OperatorID.SELECT) {
                    // Fine
                }else if (parent.getID() == OperatorID.APPLY) {
                    // If APPLY uses one of the correlated attributes then
                    // stop here, else keep trying to go further up.
                    ApplyOperator applyOp = (ApplyOperator) parent;
                    boolean stopHere = false;
                    Set<Attribute> attributesToBind = 
                        applyOp.getAttributesToBind();
                    for (Attribute correlatedAttribute : correlatedAttributes)
                    {
                        if (attributesToBind.contains(correlatedAttribute))
                        {
                            stopHere = true;
                            selectToApplyMap.put(select, applyOp);
                            break;
                        }
                    }
                    if (stopHere)
                    {
                        break;
                    }
                }else 
                {
                    acceptableParents = false;
                    break;
                }
                child = parent;
                parent = parent.getParent();
            }
            
            if (!acceptableParents) 
            {
                continue;
            }
            
            // Is parent now an Apply operator?
            if (parent.getID() == OperatorID.APPLY) {
                // Is the child at the right of the apply?
                if (parent.getChild(1) == child) {
                    // Is the join type semi/anti join?
                    Operator join = ((ApplyOperator)parent).getOperator();
                    if (join.getID() == OperatorID.SEMI_JOIN || 
                        join.getID() == OperatorID.ANTI_SEMI_JOIN) {
                    // All fine. We can take the predicate of this select 
                    // operator up to the join and remove the operator 
                    // from the tree.
                        correlatedSelects.add((SelectOperator)select);
                    }
                }
            }
        }

        // Remove Selects 
        for (SelectOperator select:correlatedSelects) {
            Operator parent = select.getParent();
            Operator correlatedApply = selectToApplyMap.get(select);
            
            while (parent != correlatedApply) {
                
                if (parent.getID() == OperatorID.APPLY)
                {
                    // Need to pull up past an uncorrelated APPLY
                    OptimiserUtils.pullPastNoChange(select);
                }
                else if (parent.getID() == OperatorID.PROJECT) 
                {
                    pullSelectPastProject(select);
                }
                else if (parent.getID() == OperatorID.RENAME) 
                {
                    RenameMap map = ((RenameOperator)parent).getRenameMap();
                    select.renameUsedAttributes(map);
                    OptimiserUtils.pullPastNoChange(select);                   
                } 
                else if (parent.getID() == OperatorID.SELECT) 
                {
                    OptimiserUtils.pullPastNoChange(select);
                } 
                else 
                {
                    throw new LQPException("Unexpected parent " + 
                        parent.getID() + ". " + 
                        "Don't know how to pull the Select past this operator. " +
                        "This situation should not have been encountered.");
                }
                parent = select.getParent();
            }

            // Remove 'correlated' annotation from attributes. We are actually
            // not sure that any of these attributes will cease to be correlated
            // when we remove this Select. We will call validate() in the end
            // to make sure we any attribute that is still correlated will
            // be annotated as 'correlated' again.
            Set<Attribute> attribute = select.getPredicate().getAttributes();
            for (Attribute attr:attribute)
            {
                if (attr.getAnnotation(Annotation.CORR_ATTR) != null)
                {
                    attr.removeAnnotation(Annotation.CORR_ATTR);
                }
            }

            // Append the predicate of the Select operator to the 
            // predicate of the Semi-join operator 
            ApplyOperator apply = (ApplyOperator)parent;
            AbstractJoinOperator semiJoin = (AbstractJoinOperator)apply.getOperator();
            AndExpression newJoinExpr = new AndExpression(semiJoin.getPredicate().getExpression(),
                                  select.getPredicate().getExpression());
            Predicate newJoinPredicate = new CommonPredicate(newJoinExpr);
            
            // Remove Select operator
            select.getParent().replaceChild(select, select.getChild(0));
            select.disconnect();
            
            // Replace Apply operator with a new Semi-join operator.
            AbstractJoinOperator newSemiJoin = null;
            if (semiJoin.getID() == OperatorID.SEMI_JOIN)
            {
                newSemiJoin = new SemiJoinOperator(newJoinPredicate);
            }else if (semiJoin.getID() == OperatorID.ANTI_SEMI_JOIN)
            {
                newSemiJoin = new AntiSemiJoinOperator(newJoinPredicate);
            }
            OptimiserUtils.replaceOperator(apply, newSemiJoin);
            newSemiJoin.update();
            
            // As explained above, we call validate() to make sure correlated
            // attributes get the proper annotation.
            root.validate();
        }
    }
    
    /**
     * Pulls a SELECT operator past a parent PROJECT operator.  If the SELECT
     * uses attributes not specified in the PROJECT then we must add them to
     * the PROJECT.  Because these operators lie on the empty side of a 
     * SEMI-JOIN the actual contents of the PROJECT do not matter so adding
     * an extra attribute causes no harm.
     * 
     * @param selectOp
     * 
     * @throws LQPException
     */
    private void pullSelectPastProject(SelectOperator selectOp)
        throws LQPException
    {
        // Find the attributes used in the heading
        List<Attribute> usedAttributesInHeading = 
            new LinkedList<Attribute>();
        for (Attribute attr : selectOp.getUsedAttributes())
        {
            if (selectOp.getHeading().contains(attr))
            {
                usedAttributesInHeading.add(attr);
            }
        }
        
        // Add these to the project if it is missing
        ProjectOperator projectOperator = 
            (ProjectOperator) selectOp.getParent();
        for (Attribute attr : usedAttributesInHeading )
        {
            if (!projectOperator.getHeading().contains(attr))
            {
                projectOperator.addAttribute(attr);
            }
        }
        
        // Nothing to do
        OptimiserUtils.pullPastNoChange(selectOp);
    }
}