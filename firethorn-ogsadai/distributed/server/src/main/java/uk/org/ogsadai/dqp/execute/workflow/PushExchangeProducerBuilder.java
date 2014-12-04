// Copyright (c) The University of Edinburgh, 2009.
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
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.PushExchangeProducerOperator;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Builds activities for operator PUSH EXCHANGE PRODUCER.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PushExchangeProducerBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op,
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        PushExchangeProducerOperator operator = (PushExchangeProducerOperator)op;
        ResourceID dataSink = new ResourceID(operator.getDataSinkID());
        
        TupleSerialiser tupleSerialiser = operator.getTupleSerialiser();
        SingleActivityOutput output = 
            tupleSerialiser.addSerialiser(outputs.get(0), builder);
        
        DQPDeliverToDataSink deliver = 
            new DQPDeliverToDataSink("uk.org.ogsadai.DeliverToDataSink");
        deliver.createInput("mode");
        deliver.createInput("numberOfBlocks");
        deliver.createInput("resourceID");
        deliver.createInput("data");
        
        deliver.addInput("mode", "BLOCK");
        deliver.addInput("numberOfBlocks", operator.getNumBlocks());
        deliver.addInput("resourceID", dataSink.toString());
        deliver.connectInput("data", output);
        
        // the parent of the exchange producer is the exchange consumer
        // get the partition from which we exchange data
        Partition destination =
            Annotation.getPartitionAnnotation(operator.getParent());
        deliver.setEvaluationNode(destination.getEvaluationNode());
        builder.add(deliver);

        return null;
    }

}
