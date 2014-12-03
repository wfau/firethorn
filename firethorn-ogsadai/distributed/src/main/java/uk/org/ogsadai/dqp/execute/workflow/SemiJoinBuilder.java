// Copyright (c) The University of Edinburgh, 2009-2012.
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
import uk.org.ogsadai.dqp.lqp.operators.SemiJoinOperator;

/**
 * Builds activities for operator INNER THETA JOIN.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SemiJoinBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2012";

    private String mResourceID;

    /**
     * Sets the resource ID of the data resource for storing temporary tables.
     * 
     * @param resourceID
     *            data resource ID
     */
    public void setResourceID(String resourceID)
    {
        mResourceID = resourceID;
    }

    @Override
    public SingleActivityOutput build(
            Operator op, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        String sql = ((SemiJoinOperator) op).getPredicate().toString();
        
        GenericActivity join =
            new GenericActivity("uk.org.ogsadai.TupleSemiJoin");
        if (mResourceID != null) join.setResourceID(mResourceID);
        join.createInput("data1");
        join.createInput("data2");
        join.createInput("readFirst");
        join.createInput("condition");
        join.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        join.connectInput("data1", outputLeft);
        join.connectInput("data2", outputRight);        
        join.addInput("condition", sql);
        join.addInput("readFirst", "data2");
        builder.add(join);
        
        return join.getOutput("result");
    }
}
