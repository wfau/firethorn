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

package uk.org.ogsadai.dqp.lqp.optimiser.partitioner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Branch;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.ScanOperator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;
import uk.org.ogsadai.dqp.lqp.operators.ExchangeOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.OptimiserUtils;
import uk.org.ogsadai.dqp.lqp.optimiser.join.BatchTableScanOptimiser;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;

/**
 * Partitioning optimiser annotates each operator with an EvaluationNode on
 * which it should be executed. Partition boundaries are marked by EXCHANGE
 * operators. Exchange operators are annotated with the same 
 * <code>evaluation.node</code> annotation as their parent (destination node).
 * 
 * Execution engine expects a properly partitioned query plan. In a properly
 * partitioned query plan - all operators have <code>evaluation.node</code> 
 * annotation which are consistent within a partition. Partition boundaries are 
 * defined by EXCHANGE operators. The EXCHANGE operator's child and parent have 
 * different evaluation node annotations. The EXCHANGE operator is annotated
 * with the same annotation as its parent operator (destination node). All
 * partitions can be remote - execution engine will add local partition that
 * deals with final results transfer automatically.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class PartitioningOptimiser implements Optimiser
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(PartitioningOptimiser.class);

    /** Evaluation node with most scans. */
    Map<EvaluationNode, Integer> mEvaluationNodeScanCountMap;
    
    /**
     * {@inheritDoc}
     */
    public Operator optimise(
        Operator lqpRoot, 
        RequestDQPFederation requestFederation,
        CompilerConfiguration compilerConfiguration, 
        RequestDetails requestDetails) throws LQPException
    {
        fillEvaluationNodeScanCount(lqpRoot);
        annotate(lqpRoot);
        insertExchangeOperators(lqpRoot);

        return lqpRoot;
    }

    /**
     * Executes the partitioning algorithm and annotates operators with
     * EvaluationNodes. State is reset on each call.
     * 
     * @param lqpRoot LQP to annotate
     */
    public void annotateWithEvalNodes(Operator lqpRoot)
    {
        // count scans for each evaluator
        fillEvaluationNodeScanCount(lqpRoot);
        annotate(lqpRoot);
    }
    
    /**
     * Finds leaf scan operators, annotates with DataNodes and propagates
     * annotations up the tree.
     * 
     * @param lqpRoot
     *            query plan root operator
     * @throws LQPException
     */
    private void annotate(Operator lqpRoot)
    {
        if (lqpRoot.getChild(0) == null)
        {            
            if(lqpRoot instanceof ScanOperator)
            {
                // We have a leaf scan operator. We should annotate with a specific
                // data node. Here we do not care about replicas.
                DataNode dataNode = ((ScanOperator) lqpRoot).getDataNode();
                Annotation.addDataNodeAnnotation(lqpRoot, dataNode);
            }
            else
            {
                // This may happen if we have tuple producing relation valued
                // UDF. Choose some evaluator?
                // TODO: Add support for relation valued UDFs
                
                throw new IllegalStateException(
                    "Leaf opearators that are not a ScanOperator are not supported");
            }                
            propagateUpTheTree(lqpRoot);
        }
        else
        {
            if (lqpRoot.isBinary())
            {
                annotate(lqpRoot.getChild(1));
            }
            annotate(lqpRoot.getChild(0));
        }
    }

    /**
     * Counts scans for each evaluator and fills the EvaluationNode->count
     * map.
     * 
     * @param root LQP
     */
    private void fillEvaluationNodeScanCount(Operator root)
    {
        // Find a resource with most scans
        List<Operator> scans = OptimiserUtils.getLeafs(Branch.LEFT, root);
        mEvaluationNodeScanCountMap = new HashMap<EvaluationNode, Integer>();
        for (Operator o : scans)
        {
            //EvaluationNode en = ((ScanOperator) o).getDataNode().getEvaluationNode();
            ScanOperator oper = (ScanOperator) o;
            LOG.debug("ScanOperator [" + oper + "]");
            LOG.debug("ScanOperator [" + oper.toString() + "]");

            DataNode data = oper.getDataNode();
            LOG.debug("DataNode [" + data + "]");
            LOG.debug("DataNode [" + data.toString() + "]");

            EvaluationNode eval = data.getEvaluationNode();
            LOG.debug("EvalNode [" + eval + "]");
            LOG.debug("EvalNode [" + eval.toString() + "]");
        
            Integer cnt = mEvaluationNodeScanCountMap.get(eval);
            if (cnt == null)
            {
                mEvaluationNodeScanCountMap.put(eval, 1);
            }
            else
            {
                mEvaluationNodeScanCountMap.put(eval, ++cnt);
            }
        }
    }

    /**
     * Propagates DataNode annotations up the tree. When it must be decided
     * which evaluation node will execute a binary operator, resources with most
     * scans are preferred. If two resources have the same number of scans -
     * left one is preferred.
     * 
     * @param currentOperator
     *            current operator to annotate
     */
    private void propagateUpTheTree(Operator currentOperator)
    {
        Operator parent = currentOperator.getParent();
        EvaluationNode evalNode = 
            Annotation.getEvaluationNodeAnnotation(currentOperator);
        DataNode dataNode = Annotation.getDataNodeAnnotation(currentOperator);

        if (parent == null)
        {
            // we have reached root of the tree
            return;
        }
        else if (parent.isBinary())
        {
            if (((BinaryOperator) parent).getChildIndex(currentOperator) == 1)
            {
                // if we are climbing up from right branch - stop and let left
                // branch decide
                return;
            }
            else
            {
                // get right data node and choose local one - insert exchange
                // operator
                EvaluationNode rightOpEvalNode = Annotation
                    .getEvaluationNodeAnnotation(parent.getChild(1));
                EvaluationNode leftOpEvalNode = Annotation
                    .getEvaluationNodeAnnotation(parent.getChild(0));

                DataNode rightOpDataNode = 
                    Annotation.getDataNodeAnnotation(parent.getChild(1));
                DataNode leftOpDataNode = 
                    Annotation.getDataNodeAnnotation(parent.getChild(0));

                if (rightOpDataNode != null && 
                    rightOpDataNode.equals(leftOpDataNode))
                {
                    // continue using the same data node
                    Annotation.addDataNodeAnnotation(parent, dataNode);
                    propagateUpTheTree(parent);
                }
                else  if (rightOpEvalNode.equals(evalNode))
                {
                    // continue using the same evaluation node
                    Annotation.addEvaluationNodeAnnotation(parent, evalNode);
                    propagateUpTheTree(parent);
                }
                else if (mEvaluationNodeScanCountMap.get(rightOpEvalNode)
                    > mEvaluationNodeScanCountMap.get(leftOpEvalNode))
                {
                    Annotation.addEvaluationNodeAnnotation(parent,
                        rightOpEvalNode);
                    propagateUpTheTree(parent);
                }
                else
                {
                    Annotation.addEvaluationNodeAnnotation(parent, evalNode);
                    propagateUpTheTree(parent);
                }
            }
        }
        else
        {
            // tag with the same data node and continue
            if (dataNode != null)
            {
                Annotation.addDataNodeAnnotation(parent, dataNode);
            }
            else
            {
                Annotation.addEvaluationNodeAnnotation(parent, evalNode);
            }
            propagateUpTheTree(parent);
        }
    }

    /**
     * Inserts EXCHAGE operators on partition boundaries signified by changes of
     * the <code>evaluation.node</code> annotation.
     * 
     * @param lqp
     * @throws LQPException
     */
    void insertExchangeOperators(Operator lqp) throws LQPException
    {
        List<Operator> leafs = OptimiserUtils.getLeafs(Branch.RIGHT, lqp);

        for (Operator o : leafs)
        {
            walkUpTheTreeCheckInsertExchange(o);
        }
    }
    
    /**
     * Walks up the tree starting from currentOperator and inserts EXCHANGE
     * operators when partition boundary is detected. When a walker encounters a
     * binary operator and comes from the right branch - the process is
     * terminated.
     * 
     * @param currentOperator
     *            operator from which to start walking
     * @throws LQPException
     */
    private void walkUpTheTreeCheckInsertExchange(Operator currentOperator)
        throws LQPException
    {
        Operator parent = currentOperator.getParent();
        if (parent == null)
        {
            // we have reached root of the tree
            return;
        }
        else if (parent.isBinary())
        {
            if (((BinaryOperator) parent).getChildIndex(currentOperator) == 1)
            {
                checkInsertExchange(currentOperator);
                return;
            }
            else
            {
                checkInsertExchange(currentOperator);
                walkUpTheTreeCheckInsertExchange(parent);
            }
        }
        else
        {
            checkInsertExchange(currentOperator);
            walkUpTheTreeCheckInsertExchange(parent);
        }
    }
    
    /**
     * Checks if there is a partition boundary between the operator and its
     * parent and inserts EXCHANGE if true.
     * 
     * @param operator
     *            possible boundary operator
     * @throws LQPException
     */
    private void checkInsertExchange(Operator operator) throws LQPException
    {
        Operator parent = operator.getParent();
        
        if (operator.getID() != OperatorID.EXCHANGE &&
            !Annotation.getEvaluationNodeAnnotation(operator).equals(
                    Annotation.getEvaluationNodeAnnotation(parent)))
        {            
            ExchangeOperator ex = new ExchangeOperator();

            ex.setChild(0, operator);
            parent.replaceChild(operator, ex);
            parent.update();
            
            Annotation.addCardinalityAnnotation(
                    ex, 
                    Annotation.getCardinalityAnnotation(ex.getChild(0)));
            Annotation.addCardinalityStatisticsAnnotation(
                    ex, 
                    Annotation.getCardinalityStatisticsAnnotation(
                            ex.getChild(0)));
            
            Annotation.addEvaluationNodeAnnotation(
                ex, Annotation.getEvaluationNodeAnnotation(parent));
        }
    }
}
