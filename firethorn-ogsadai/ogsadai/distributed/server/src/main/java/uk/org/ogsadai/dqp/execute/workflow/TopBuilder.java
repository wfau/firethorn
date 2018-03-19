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

package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;

import uk.org.ogsadai.activity.block.HeadActivity;
import uk.org.ogsadai.activity.relational.TupleSortActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.LongData;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.TopOperator;

/**
 * Builds activities for operator LIMIT.
 *
 * @author The OGSA-DAI Project Team.
 */
public class TopBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2011.";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(
            Operator op, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        TopOperator operator = (TopOperator)op;
        SingleActivityOutput output = outputs.get(0);
    
        GenericActivity head =
            new GenericActivity("uk.org.ogsadai.activity.block.HeadActivity");
        head.createInput(HeadActivity.INPUT_DATA);
        head.createInput(HeadActivity.INPUT_LIMIT);
        head.createInput(HeadActivity.INPUT_GRANULARITY);

        head.createOutput(HeadActivity.OUTPUT_RESULT, 
            GenericActivity.LIMITED_VALIDATION);
        head.connectInput(TupleSortActivity.INPUT_DATA, output);
        
        // extract limit
        long limit = operator.getLimit();
        head.addInput(HeadActivity.INPUT_LIMIT, new LongData(limit));

        // list depth is always 1 because we have a tuple list
        head.addInput(HeadActivity.INPUT_GRANULARITY, new IntegerData(1));
        
        builder.add(head);
        return head.getOutput(HeadActivity.OUTPUT_RESULT);
    }
}
