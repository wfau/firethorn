// Copyright (c) The University of Edinburgh, 2011.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.dqp.lqp.optimiser.implosion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.LQPBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ArithmeticExpressionProjectExpression;
import uk.org.ogsadai.dqp.lqp.operators.AttributeProjectExpression;
import uk.org.ogsadai.dqp.lqp.operators.ProjectExpression;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectProjectJoinTableScanQuery;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.operators.TableScanQuery;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.TableColumn;

/**
 * Helper class to handle the implosion of PROJECT operators into TABLE SCAN
 * operators.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ProjectImplosionHelper
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011";

    private ProjectOperator mProject;
    
    // Data needed to create a new PROJECT operator (if we cannot
    // implode everything.
    private List<ArithmeticExpression> mExpressionsIfProjectOperatorRemains;
    private List<String> mAliasesIfProjectOperatorRemains;

    // Data needed to implode the expressions
    private List<ProjectExpression> mImplodableExpressions;
    
    private boolean mImplodedEverything;
    
    private boolean mNothingToImplode;

    private CardinalityEstimator mCardinalityEstimator;

    /**
     * Constructor.
     * 
     * @param dataNode   data node the PROJECT may be imploded into
     * @param project    PROJECT operator to possibly be imploded
     */
    public ProjectImplosionHelper(
            DataNode dataNode,
            ProjectOperator project,
            CardinalityEstimator cardinalityEstimator)
    {
        mCardinalityEstimator = cardinalityEstimator;
        mProject = project;
        
        // Data needed to create a new PROJECT operator (if we cannot
        // implode everything).
        mExpressionsIfProjectOperatorRemains =
            new LinkedList<ArithmeticExpression>();
        mAliasesIfProjectOperatorRemains = new LinkedList<String>();
        
        // Data needed to implode the expressions
        mImplodableExpressions = new LinkedList<ProjectExpression>();
        
        // Process each expression in turn
        List<ArithmeticExpression> projectExpressions = 
            project.getAttributeExpressions();
        List<String> projectAliases = project.getAttributeAliases();
        
        mImplodedEverything = true;
        mNothingToImplode = true;
        
        for (int i=0; i<projectExpressions.size(); ++i)
        {
            ArithmeticExpression expr = projectExpressions.get(i);
            String alias = projectAliases.get(i);
            
            if (dataNode.supportsArithmeticExpression(expr))
            {
                if (ExpressionUtils.isDerived(expr) || alias != null)
                {
                    if (alias == null) alias = LQPBuilder.getNextID();

                    ArithmeticExpressionProjectExpression aepr = 
                        new ArithmeticExpressionProjectExpression(
                            expr, 
                            alias,
                            project.getHeading().getAttributes().get(i));
                    mImplodableExpressions.add(aepr);
                    
                    mExpressionsIfProjectOperatorRemains.add(
                        new TableColumn(alias));
                    mAliasesIfProjectOperatorRemains.add(null);
                    mNothingToImplode = false;
                }
                else
                {
                    // This will be one attribute but the following code
                    // is the easiest way to add this
                    AttributeProjectExpression ape = 
                        new AttributeProjectExpression(
                            ExpressionUtils.getUsedAttributes(expr).iterator().
                                next());
                    mImplodableExpressions.add(ape);
                    
                    mExpressionsIfProjectOperatorRemains.add(expr);
                    mAliasesIfProjectOperatorRemains.add(null);
                }
            }
            else
            {
                mImplodedEverything = false;
                mExpressionsIfProjectOperatorRemains.add(expr);
                mAliasesIfProjectOperatorRemains.add(alias);
                
                for (Attribute attr : ExpressionUtils.getUsedAttributes(expr))
                {
                    mImplodableExpressions.add(
                        new AttributeProjectExpression(attr));
                }
            }
        }
        
        // If not imploded everything then remove duplicates.  No do wish
        // to do this if everything has been imploded because this will
        // affect the heading.
        if (!mImplodedEverything)
        {
            removeDuplicateAttributes();
        }
    }

    
    /**
     * Implodes the PROJECT into the given table scan operator if possible.
     * 
     * @param tableScan   table scan operator
     * 
     * @throws LQPException if an invalid query plan is produced.
     */
    public void implodeProject(TableScanOperator tableScan) throws LQPException 
    {
        TableScanQuery tsq = tableScan.getQuery();
        
        if (tsq instanceof SelectProjectJoinTableScanQuery )
        {
            SelectProjectJoinTableScanQuery spjtsq = 
                (SelectProjectJoinTableScanQuery) tsq;
            
            spjtsq.setProjectExpressions(mImplodableExpressions);
        }
        else
        {
            ExtendedTableScanQuery etsq = (ExtendedTableScanQuery) tsq;
            
            if (mImplodedEverything)
            {
                etsq.addProject(mProject);
            }
            else
            {
                // Stop if we have nothing to implode
                if (mNothingToImplode) return;
                
                // Need to create a new Project to implode
                List<ArithmeticExpression> exprs = 
                    new LinkedList<ArithmeticExpression>();
                List<String> aliases = new LinkedList<String>();
                
                for (ProjectExpression pe : mImplodableExpressions)
                {
                    if (pe instanceof ArithmeticExpressionProjectExpression)
                    {
                        ArithmeticExpressionProjectExpression aepe = 
                            (ArithmeticExpressionProjectExpression) pe;
                        
                        exprs.add(aepe.getExpression());
                        aliases.add(aepe.getAlias());
                    }
                    else
                    {
                        AttributeProjectExpression ape = 
                            (AttributeProjectExpression) pe;
                        
                        exprs.add(new TableColumn(ape.getAttribute()));
                        aliases.add(null);
                    }
                }

                ProjectOperator projectToImplode = 
                    new ProjectOperator(tableScan, exprs, aliases);
                projectToImplode.update();
                
                ProjectOperator nonImplodedProject = 
                    getUnimplodedProject(projectToImplode);

                mProject.getParent().replaceChild(mProject, nonImplodedProject);
                mProject.disconnect();
                
                // add data and evaluation node annotations
                Annotation.addDataNodeAnnotation(
                    nonImplodedProject, 
                    Annotation.getDataNodeAnnotation(mProject));
                Annotation.addEvaluationNodeAnnotation(
                    nonImplodedProject, 
                    Annotation.getEvaluationNodeAnnotation(mProject));
                
                nonImplodedProject.update();
                
                projectToImplode.accept(mCardinalityEstimator);
                nonImplodedProject.accept(mCardinalityEstimator);
                
                // implode project
                etsq.addProject(projectToImplode);
                
                nonImplodedProject.replaceChild(projectToImplode, tableScan);
                projectToImplode.disconnect();
                
                Annotation.addCardinalityAnnotation(
                        tableScan, 
                        Annotation.getCardinalityAnnotation(projectToImplode));
                Annotation.addCardinalityStatisticsAnnotation(
                        tableScan, 
                        Annotation.getCardinalityStatisticsAnnotation(
                                projectToImplode));

            }
        }
    }
    
    /**
     * Returns whether or not the whole PROJECT was imploded.
     * 
     * @return <tt>true</tt> if the whole PROJECT was imploded, <tt>false</tt>
     *         otherwise.
     */
    public boolean implodedEverything()
    {
        return mImplodedEverything;
    }

    /**
     * If the whole PROJECT was not imploded returns a PROJECT operator than
     * contains the unimploded parts.
     * 
     * @param childOp  child of the new PROJECT operator (should be the table
     *                 scan)
     *                 
     * @return new PROJECT operator
     * 
     * @throws LQPException if an invalid query plan is produced.
     */
    public ProjectOperator getUnimplodedProject(Operator childOp) 
        throws LQPException
    {
        return new ProjectOperator(
            childOp,
            mExpressionsIfProjectOperatorRemains,
            mAliasesIfProjectOperatorRemains);
                
    }
    
    private void removeDuplicateAttributes()
    {
        Set<Attribute> attributes = new HashSet<Attribute>();
        List<ProjectExpression> duplicateExpressions = 
            new ArrayList<ProjectExpression>();
        
        for (ProjectExpression pe : mImplodableExpressions)
        {
            if (pe instanceof AttributeProjectExpression)
            {
                AttributeProjectExpression ape = 
                    (AttributeProjectExpression) pe;
                Attribute attr = ape.getAttribute();
                if (attributes.contains(attr))
                {
                    duplicateExpressions.add(ape);
                }
                else
                {
                    attributes.add(attr);
                }
            }
        }
        mImplodableExpressions.removeAll(duplicateExpressions);
    }
}
