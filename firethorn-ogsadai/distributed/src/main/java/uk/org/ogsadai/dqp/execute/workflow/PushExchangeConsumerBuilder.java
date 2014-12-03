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
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.PushExchangeConsumerOperator;

/**
 * Builds activities for operator EXCHANGE PRODUCER.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PushExchangeConsumerBuilder implements ActivityPipelineBuilder
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
        PushExchangeConsumerOperator operator = 
            (PushExchangeConsumerOperator)op;
        // prepare data sink
        String dataSink = operator.getDataSinkID();
        builder.addDataSinkToSetup(dataSink);
        
        GenericActivity read =
            new GenericActivity("uk.org.ogsadai.ReadFromDataSink");
        read.createOutput("output");
        read.setResourceID(dataSink);
        builder.add(read);

        TupleSerialiser tupleSerialiser = operator.getTupleSerialiser();
        return tupleSerialiser.addDeserialiser(read.getOutput("output"), builder);
    }

}
