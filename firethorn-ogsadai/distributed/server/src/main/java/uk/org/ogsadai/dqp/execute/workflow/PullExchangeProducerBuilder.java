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
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.PullExchangeProducerOperator;

/**
 * Builds activities for operator PULL EXCHANGE PRODUCER.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PullExchangeProducerBuilder implements ActivityPipelineBuilder
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
        throws ActivityConstructionException
    {
        PullExchangeProducerOperator operator = (PullExchangeProducerOperator)op;
        // prepare data source
        String dataSource = operator.getDataSourceID();
        builder.addDataSourceToSetup(dataSource);
        
        TupleSerialiser tupleSerialiser = operator.getTupleSerialiser();
        SingleActivityOutput output = 
            tupleSerialiser.addSerialiser(outputs.get(0), builder);

        GenericActivity write =
            new GenericActivity("uk.org.ogsadai.WriteToDataSource");
        write.createInput("input");
        write.connectInput("input", output);
        write.setResourceID(dataSource);
        builder.add(write);
        
        return null;
    }
}
