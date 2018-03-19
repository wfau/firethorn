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


package uk.org.ogsadai.dqp.execute.partition;

import java.util.HashSet;
import java.util.Set;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ExchangeOperator;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;
import uk.org.ogsadai.util.UniqueName;

/**
 * Builds partitions and assigns to operators using annotations.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class PartitionBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(PartitionBuilder.class);

    /** DQP resource accessor. */
    private final DQPResourceAccessor mResourceAccessor;
    
    /** Creates partitions. */
    private PartitionFactory mPartitionFactory;

    /** Details of parent request. */
    private RequestDetails mRequestDetails;

    /** Set of all partitions. */
    private Set<Partition> mPartitions = new HashSet<Partition>();

    /**
     * Constructs a new partition builder. The root partition is the node that
     * executes the query plan and which receives the results.
     * 
     * @param resourceAccessor 
     *            DQP resource accessor, used to get some configuration
     *            properties.
     * @param factory
     * @param requestDetails
     */
    public PartitionBuilder(
        DQPResourceAccessor resourceAccessor,
        PartitionFactory factory,
        RequestDetails requestDetails)
    {
        mResourceAccessor = resourceAccessor;
        mPartitionFactory = factory;
        mRequestDetails = requestDetails;
    }

    /**
     * Builds partitions and assigns to operators using
     * <code>lqp.partition->Partition</code> annotation. Splits EXCHANGE
     * operators into EXCHANGE_PRODUCER - EXCHANGE_CONSUMER pairs.
     * 
     * Expects LQP with <code>evaluation.node->EvaluationNode</code> annotation.
     * 
     * @param root
     *            LQP root
     * @return annotated LQP
     * @throws LQPException
     */
    public Operator buildAndAnnotate(Operator root) throws LQPException
    {
        EvaluationNode evalNode = getEvalNode(root);
        Partition partition;

        if (evalNode.isLocal())
        {
            partition = mPartitionFactory.createPartition(evalNode,
                mRequestDetails);
        }
        else
        {
            partition = mPartitionFactory.createLocalPartition(mRequestDetails);

            // add exchange
            ExchangeOperator ex = new ExchangeOperator();

            ex.setChild(0, root.getChild(0));
            root.replaceChild(root.getChild(0), ex);
            root.update();

        }
        partition.setRoot(root);
        mPartitions.add(partition);

        Annotation.addPartinionAnnotation(root, partition);
        buildAndAssignPartitions(root.getChild(0), partition);

        return root;
    }

    /**
     * Walks the tree, builds partitions, splits EXCHANGE and annotates
     * operators.
     * 
     * @param currentOperator
     *            operator to annotate
     * @param currentPartition
     *            current partition
     * @throws LQPException
     */
    private void buildAndAssignPartitions(
        Operator currentOperator,
        Partition currentPartition) throws LQPException
    {
        if (currentOperator == null)
        {
            return;
        }
        else if (currentOperator.getID() == OperatorID.EXCHANGE)
        {
            String exchangeId = "Exchange-" + UniqueName.newName();
            
            ExchangeOperatorFactory factory;
            if (mResourceAccessor.getUsePushModel())
            {
                factory = new PushExchangeOperatorFactory(mResourceAccessor);
            }
            else
            {
                factory = new PullExchangeOperatorFactory(mResourceAccessor);
            }
            
            Operator consumer = factory.createExchangeConsumerOperator(
                exchangeId);
            Operator producer = factory.createExchangeProducerOperator(
                exchangeId);

            // get data node assigned to EXCHANGE child
            EvaluationNode exchangeChildEvalNode = getEvalNode(currentOperator
                .getChild(0));

            // reconnect
            currentOperator.getParent().replaceChild(currentOperator, consumer);
            consumer.setChild(0, producer);
            producer.setChild(0, currentOperator.getChild(0));
            currentOperator.disconnect();
            consumer.getParent().update();

            // add annotations and create new partition
            Annotation.addPartinionAnnotation(consumer, currentPartition);
            Partition newPartition = mPartitionFactory.createPartition(
                exchangeChildEvalNode, mRequestDetails);
            newPartition.setRoot(producer);
            mPartitions.add(newPartition);
            Annotation.addPartinionAnnotation(producer, newPartition);

            // continue partition assignment
            buildAndAssignPartitions(producer.getChild(0), newPartition);
        }
        else
        {
            EvaluationNode evalNode = getEvalNode(currentOperator);
            if (!evalNode.equals(currentPartition.getEvaluationNode()))
            {
                throw new LQPException(
                    "Inconsistent evaluation.node annotations");
            }
            Annotation
                .addPartinionAnnotation(currentOperator, currentPartition);

            buildAndAssignPartitions(currentOperator.getChild(0),
                currentPartition);
            if (currentOperator.isBinary())
            {
                buildAndAssignPartitions(currentOperator.getChild(1),
                    currentPartition);
            }
        }
    }

    /**
     * Returns evaluation node defined by
     * <code>evaluation.node->EvaluationNode</code> annotation.
     * 
     * @param op
     *            annotated operator
     * @return evaluation node
     * @throws LQPException
     */
    private EvaluationNode getEvalNode(Operator op) throws LQPException
    {
        EvaluationNode evalNode = Annotation.getEvaluationNodeAnnotation(op);

        if (evalNode == null)
        {
            throw new LQPException(
                "Query plan is not annotated with EVALUATION or DATA NODES. "
                    + "Make sure that your optimisation chain includes partitioning optimiser.");
        }

        return evalNode;
    }

    /**
     * Returns the set of partitions.
     * 
     * @return partitions
     */
    public Set<Partition> getPartitions()
    {
        return mPartitions;
    }

}
