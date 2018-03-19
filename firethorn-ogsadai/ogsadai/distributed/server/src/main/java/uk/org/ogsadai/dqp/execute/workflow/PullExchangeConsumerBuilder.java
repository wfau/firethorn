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


package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.PullExchangeConsumerOperator;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Builds activities for operator PULL EXCHANGE CONSUMER.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PullExchangeConsumerBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op,
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder)
    {
        PullExchangeConsumerOperator operator = (PullExchangeConsumerOperator)op;
        ResourceID dataSource = new ResourceID(operator.getDataSourceID());
        
        // obtain data from remote data source on destination partition
        DQPObtainFromDataSource obtain = 
            new DQPObtainFromDataSource("uk.org.ogsadai.ObtainFromDataSource");
        obtain.createInput("mode");
        obtain.createInput("numberOfBlocks");
        obtain.createInput("resourceID");
        obtain.createOutput("data");
        
        obtain.addInput("mode", "BLOCK");
        obtain.addInput("numberOfBlocks", operator.getNumBlocks());
        obtain.addInput("resourceID", dataSource.toString());
        // the child of the exchange consumer is the exchange producer
        // get the partition from which we exchange data
        Partition source = Annotation.getPartitionAnnotation(
            operator.getChild(0));
        obtain.setEvaluationNode(source.getEvaluationNode());
        builder.add(obtain);
        
        TupleSerialiser tupleSerialiser = operator.getTupleSerialiser();
        return tupleSerialiser.addDeserialiser(
            obtain.getOutput("data"), builder);
    }
}
