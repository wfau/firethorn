// Copyright (c) The University of Edinburgh, 2012.
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
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;

/**
 * Builds activities for operator INNER THETA JOIN.
 *
 * @author The OGSA-DAI Project Team.
 */
public class DerbyStreamingJoinBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    /** Logger. */
    private static final DAILogger LOG =
        DAILogger.getLogger(DerbyStreamingJoinBuilder.class);
    
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

    public SingleActivityOutput build(
            Operator op,
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        LOG.debug("In DerbyStreamingJoinBuilder");
        
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        GenericActivity join  = 
            new GenericActivity("uk.org.ogsadai.TupleJoin");
        if (mResourceID != null) join.setResourceID(mResourceID);
        join.createInput("data1");
        join.createInput("data2");
        join.createInput("condition");
        join.createOutput("result");
        join.connectInput("data1", outputLeft);
        join.connectInput("data2", outputRight);
        
        String readFirst = (String)operator.getAnnotation("readFirst");
        if (readFirst == null || readFirst.equals("left"))
        {
            readFirst = "data1";
        }
        else
        {
            readFirst = "data2";
        }
        join.createInput("readFirst");
        join.addInput("readFirst", readFirst);

        String sql = operator.getPredicate().toString();
        join.addInput("condition", sql);

        builder.add(join);
              
        return join.getOutput("result");
    }
    
}
