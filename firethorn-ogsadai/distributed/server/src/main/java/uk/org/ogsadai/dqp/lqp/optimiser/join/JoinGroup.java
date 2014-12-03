package uk.org.ogsadai.dqp.lqp.optimiser.join;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.operators.ApplyOperator;
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;

/**
 * A class used to represent a group of relations (with related predicates) that
 * can be joined together in any order.
 * 
 * @author The OGSA-DAI Project Team
 */
public class JoinGroup
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh,  2009";

    /** List of applicable predicates. */
    private List<Predicate> mPredicateList = new ArrayList<Predicate>();
    /** List of semi-join predicates. */
    private List<SemiJoinDetails> mSemiJoinList = 
        new ArrayList<SemiJoinDetails>();
    /** List of relations in the join group. */
    private List<Operator> mRelationList = new ArrayList<Operator>();
    /** Operator following the join group. **/
    private Operator mJoinGroupParent;
    /** Original child of the join group parent. **/
    private Operator mJoinGroupParentOriginalChild;
    /** 
     * Variable used when building the joins. set true when a join is added. 
     **/
    private boolean mHasJoin;

    /**
     * Creates a new join group. The first join group operator is used to
     * extract information about the join group parent. Joins inside the group
     * can be freely reordered, so information about the join group parent is
     * needed to reconnect the reordered join tree.
     * 
     * @param rootGroupOperator
     *            first operator in the join group
     */
    public JoinGroup(Operator rootGroupOperator)
    {
        mJoinGroupParentOriginalChild = rootGroupOperator;
        mJoinGroupParent = rootGroupOperator.getParent();
    }

    /**
     * Adds a predicate to the join group.
     * 
     * @param predicate
     *            predicate object
     */
    public void addPredicate(Predicate predicate) 
    {
        mPredicateList.add(predicate);
    }

    /**
     * Adds a semi-join predicate to the join group.
     * 
     * @param predicate
     *            predicate object
     */
    public void addSemiJoinPredicate(
        Predicate predicate, 
        List<Attribute> lhsAttributes,
        SemiJoinOperator semiJoinOperator)
    {
        mSemiJoinList.add(
            new SemiJoinDetails(predicate, lhsAttributes, semiJoinOperator));
    }

    /**
     * Adds an apply semi-join predicate to the join group.
     * 
     * @param predicate
     *            predicate object
     */
    public void addAppliedSemiJoinPredicate(
        Predicate predicate, 
        List<Attribute> lhsAttributes, 
        ApplyOperator applyOperator)
    {
        mSemiJoinList.add(new SemiJoinDetails(
            predicate, lhsAttributes, applyOperator));
    }

    /**
     * Adds a relation to the join group.
     * 
     * @param operator
     *            operator whose result represents intermediate relation
     */
    public void addRelation(Operator operator)
    {
        mRelationList.add(operator);
    }

    /**
     * Gets the read-only list of predicates for the join group.
     * 
     * @return list of predicates
     */
    public List<Predicate> getPredicates()
    {
        return Collections.unmodifiableList(mPredicateList);
    }

    /**
     * Gets the read-only list of relations in the join group.
     * 
     * @return list of relations
     */
    public List<Operator> getRelations()
    {
        return Collections.unmodifiableList(mRelationList);
    }
    
    /**
     * Reconnects reordered join tree with the original parent of the join
     * group.
     * 
     * @param operator
     *            root of the reordered join tree.
     */
    public void reconnectWithParent(Operator operator)
    {
        mJoinGroupParent.replaceChild(mJoinGroupParentOriginalChild, operator);
    }

    /**
     * Get all possible joins. Cross joins have the predicate set to
     * <code>null</code>.
     * 
     * @return a list of all possible joins
     */
    public List<PossibleJoin> getPossibleJoins()
    {
        List<PossibleJoin> result = new ArrayList<PossibleJoin>();

        for (Predicate p : mPredicateList)
        {
            Set<Attribute> usedAttrs = p.getAttributes();
            for (int i = 0; i < mRelationList.size() - 1; i++)
            {
                Operator firstOp = mRelationList.get(i);
                for (int j = i + 1; j < mRelationList.size(); j++)
                {
                    Operator secondOp = mRelationList.get(j);
                    if (firstOp.getHeading().createMerged(
                        secondOp.getHeading())
                        .containsAllUnambiguous(usedAttrs))
                    {
                        // XXX ACH - I think this may well be a bug.
                        // we only want the cases where firstOp and secondOp
                        // HAVE TO be combined to contain all the attributes.
                        // If one of them happens to contain them all then
                        // this would be a product. No?
                        result.add(new InnerThetaJoinPossibleJoin(
                            firstOp, secondOp, p));
                    }
                }
            }
        }
        
        for (SemiJoinDetails semiJoinDetails : mSemiJoinList)
        {
            for( Operator firstOp : mRelationList)
            {
                if (firstOp.getHeading().containsAllUnambiguous(
                    semiJoinDetails.lhsAttrs))
                {
                    if (semiJoinDetails.applyOperator == null)
                    {
                        result.add(new SemiJoinPossibleJoin(
                            firstOp, 
                            semiJoinDetails.semiJoinOperator));
                    }
                    else
                    {
                        result.add(new AppliedSemiJoinPossibleJoin(
                            firstOp, 
                            semiJoinDetails.applyOperator));
                    }
                }
            }
        }
        
        for (int i = 0; i < mRelationList.size() - 1; i++)
        {
            Operator leftOp = mRelationList.get(i);
            for (int j = i + 1; j < mRelationList.size(); j++)
            {
                Operator rightOp = mRelationList.get(j);
                result.add(
                    new ProductPossibleJoin(leftOp, rightOp));
            }
        }
        return result;
    }

    /**
     * Removes a predicate from the join group.
     * 
     * @param pred predicate to remove.
     */
    public void removePredicate(Predicate pred)
    {
        mPredicateList.remove(pred);
    }

    public void removeSemiJoinPredicate(Predicate predicate)
    {
        SemiJoinDetails details = null;
        for (SemiJoinDetails d : mSemiJoinList)
        {
            if (d.predicate == predicate)
            {
                details = d;
            }
        }
        
        if (details != null) mSemiJoinList.remove(details);
    }

    /**
     * Removes a relation from the join group.
     * 
     * @param relation relation to remove.
     */
    public void removeRelation(Operator relation)
    {
        mRelationList.remove(relation);
    }
    
    public boolean hasJoin()
    {
        return mHasJoin;
    }
    
    public void setHasJoin()
    {
        mHasJoin = true;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("JoinGroup(\n  Predicates: ");
        for (Predicate p : mPredicateList)
            sb.append("\t").append(p).append('\n');
        sb.append("\n  SemiJoins: ");
        for (SemiJoinDetails sjd : mSemiJoinList)
            sb.append("\t").append(sjd.predicate).append(", ").append(sjd.lhsAttrs).append('\n');
        sb.append("\n  Relations: ");
        for (Operator o : mRelationList)
            sb.append("\t").append(o.getHeading().getAttributes()).append('\n');
        return sb.toString();
    }
    
    
    private class SemiJoinDetails
    {
        public Predicate predicate;
        public List<Attribute> lhsAttrs;
        public SemiJoinOperator semiJoinOperator;
        public ApplyOperator applyOperator;
        
        public SemiJoinDetails(
            Predicate predicate, 
            List<Attribute> lhsAttrs, 
            SemiJoinOperator semiJoinOperator)
        {
            this.predicate = predicate;
            this.lhsAttrs = lhsAttrs;
            this.semiJoinOperator = semiJoinOperator;
        }

        public SemiJoinDetails(
            Predicate predicate, 
            List<Attribute> lhsAttrs, 
            ApplyOperator applyOperator)
        {
            this.predicate = predicate;
            this.lhsAttrs = lhsAttrs;
            this.applyOperator = applyOperator;
        }
    }
}
