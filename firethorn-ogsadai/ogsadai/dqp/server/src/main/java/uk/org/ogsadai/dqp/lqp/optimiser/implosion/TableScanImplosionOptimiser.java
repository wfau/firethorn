// Copyright (c) The University of Edinburgh, 2008.
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

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityStatistics;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.AbstractJoinOperator;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectProjectJoinTableScanQuery;
import uk.org.ogsadai.dqp.lqp.operators.TableScanOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Optimiser trying to implode all TABLE SCAN operators. It can be configured
 * with one or many resources that can accept only select predicates.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TableScanImplosionOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2011";

    private List<SelectOperator> mProcessedSelects = 
        new LinkedList<SelectOperator>();

    private CardinalityEstimator mEstimator;
    
    public void setCardinalityEstimator(
            CardinalityEstimator cardinalityEstimator)
    {
        mEstimator = cardinalityEstimator;
    }
    
    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot, 
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration, 
        RequestDetails requestDetails) throws LQPException
    {
        traverse(lqpRoot);

        return lqpRoot;
    }

    /**
     * Traverses the LQP and calls implode on all TABLE SCAN operators. Right
     * branches are imploded first.
     * 
     * @param currentOperator
     * @throws LQPException
     */
    private void traverse(Operator currentOperator) throws LQPException
    {
        if (currentOperator.getID() == OperatorID.TABLE_SCAN)
        {
            implode((TableScanOperator) currentOperator);
        }
        else
        {
            // Implode right branches first
            if (currentOperator.isBinary())
            {
                traverse(currentOperator.getChild(1));
            }
            traverse(currentOperator.getChild(0));
        }
    }
    
    
    /**
     * Implodes parent SELECT, PROJECT and JOIN operators into the
     * SQL query.
     * 
     * @param tableScanOp
     *            table scan operator
     *            
     * @throws LQPException if an error occurs 
     */
    private void implode(TableScanOperator tableScanOp)
        throws LQPException
    {
        Operator parent = tableScanOp.getParent();
        DataNode dataNode = tableScanOp.getDataNode();

        // If the data node cannot implode the next operator then stop 
        // imploding
        if (!dataNode.supportsOperator(parent.getID())) return;

        SelectProjectJoinTableScanQuery query = 
            (SelectProjectJoinTableScanQuery) tableScanOp.getQuery();
        
        if ((parent.getID() == OperatorID.PRODUCT || 
             parent.getID() == OperatorID.INNER_THETA_JOIN))
        {
            // try to implode only if from left branch
            if (((BinaryOperator) parent).getChildIndex(tableScanOp) == 0)
            {
                // check if right child is a table scan and merge with its
                // query, then swallow
                Operator rightChild = parent.getChild(1);

                if (rightChild.getID() == OperatorID.TABLE_SCAN)
                {
                    TableScanOperator rightTableScan = 
                        (TableScanOperator) rightChild;
                    
                    // Only implode is on the same data node.  This allows
                    // more than one local data node.
                    if (tableScanOp.getDataNode().equals(
                        rightTableScan.getDataNode()))
                    {
                        if (parent.getID() == OperatorID.INNER_THETA_JOIN)
                        {
                            Predicate joinPredicate = 
                                ((AbstractJoinOperator) parent).getPredicate();

                            // Only implode in data node can handle join
                            // predicate
                            
                            // ACH/AK - Do not understand how join predicate
                            // could be null here. But reluctant to change
                            // existing semantics.
                            if (joinPredicate == null || 
                                dataNode.supportsExpression(
                                    joinPredicate.getExpression()))
                            {
                                query.merge((SelectProjectJoinTableScanQuery)
                                    rightTableScan.getQuery());
    
                                if (joinPredicate != null)
                                {
                                    query.addPredicate(joinPredicate);
                                }
                                
                                removeParentAndContinueImploding(tableScanOp);
                            }
                        }
                        else // PRODUCT
                        {
                            query.merge((SelectProjectJoinTableScanQuery)
                                rightTableScan.getQuery());
                            
                            removeParentAndContinueImploding(tableScanOp);
                        }
                    }
                }
            }
        }
        else if (parent.getID() == OperatorID.SELECT &&
                 !mProcessedSelects.contains((SelectOperator) parent))
        {
            Predicate pred = ((SelectOperator) parent).getPredicate();
            
            // If data node allows implosion of this expression then do so
            if (dataNode.supportsExpression(pred.getExpression()))
            {
                // add condition to query + swallow
                query.addPredicate(pred);

                removeParentAndContinueImploding(tableScanOp);
            }
            else
            {
                mProcessedSelects.add((SelectOperator) parent);
                pushParentUpAndContinueImploding(tableScanOp);
            }
        }
        else if (parent.getID() == OperatorID.PROJECT)
        {
            ProjectOperator project = (ProjectOperator) parent;
            ProjectImplosionHelper helper = 
                new ProjectImplosionHelper(dataNode, project, mEstimator);
            
            helper.implodeProject(tableScanOp);
            
            if ( helper.implodedEverything() )
            {
                removeParentAndContinueImploding(tableScanOp);
            }
            else
            {
                // We were not able to implode everything
                ProjectOperator newProject = 
                    helper.getUnimplodedProject(tableScanOp);
                
                parent.getParent().replaceChild(parent, newProject);
                parent.disconnect();
                newProject.setChild(0, tableScanOp);
                
                // add data and evaluation node annotations
                Annotation.addDataNodeAnnotation(
                        newProject, 
                        Annotation.getDataNodeAnnotation(project));
                Annotation.addEvaluationNodeAnnotation(
                        newProject, 
                        Annotation.getEvaluationNodeAnnotation(project));
                
                
                tableScanOp.getParent().update();
            }
        }
    }
    
    /**
     * Removes the parent operator of the given table scan operator, connect the
     * grandparent to the table scan operator and then updates and continues
     * to implode.
     * 
     * @param tableScanOp    table scan operator.
     * @throws LQPException  if an invalid query plan is produced.
     */
    private void removeParentAndContinueImploding(
        TableScanOperator tableScanOp) throws LQPException
    {
        Operator parentOp = tableScanOp.getParent();
        parentOp.getParent().replaceChild(parentOp, tableScanOp);
        parentOp.disconnect();
        tableScanOp.getParent().update();
        
        Double cardinality = Annotation.getCardinalityAnnotation(parentOp);
        if (cardinality != null)
        {
            Annotation.addCardinalityAnnotation(tableScanOp, cardinality);
        }
        CardinalityStatistics cardStats = 
                Annotation.getCardinalityStatisticsAnnotation(parentOp);
        if (cardStats != null)
        {
            Annotation.addCardinalityStatisticsAnnotation(
                    tableScanOp, cardStats);
        }
        implode(tableScanOp);
    }
    
    /**
     * Pushes the table scan operator's parent SELECT past any other SELECT
     * operators and continues trying to implode the table scan.
     * 
     * @param tableScanOp    table scan operator.
     * 
     * @throws LQPException  if an invalid query plan is produced.
     */
    private void pushParentUpAndContinueImploding(TableScanOperator tableScanOp)
        throws LQPException
    {
        // Remove the parent
        Operator parentOp = tableScanOp.getParent();
        parentOp.getParent().replaceChild(parentOp, tableScanOp);
        parentOp.disconnect();
        tableScanOp.getParent().update();

        // Put the parent at the end of the selects
        Operator firstNonSelect = tableScanOp.getParent();
        Operator firstNonSelectsChild = tableScanOp;
        while (firstNonSelect.getID() == OperatorID.SELECT)
        {
            firstNonSelectsChild = firstNonSelect;
            firstNonSelect = firstNonSelect.getParent();
        }
        firstNonSelect.replaceChild(firstNonSelectsChild, parentOp);
        parentOp.setChild(0, firstNonSelectsChild);
        firstNonSelect.update();
        
        Operator op = tableScanOp.getParent();
        while (op != parentOp.getParent())
        {
            op.accept(mEstimator);
            op = op.getParent();
        }
        
        implode(tableScanOp);
    }
        
}
