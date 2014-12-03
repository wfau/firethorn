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
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionUtils;

/**
 * Builds activities for operator INNER THETA JOIN. This builder creates a 
 * DerbyJoin activity and handles the renaming of the columns.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class DerbyJoinBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";
    
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
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        Expression expression = operator.getPredicate().getExpression();
        String sql = ExpressionUtils.generateSQL(expression);
        
        GenericActivity derbyJoin =
            new GenericActivity("uk.org.ogsadai.DerbyJoin");
        derbyJoin.setResourceID(mResourceID);
        derbyJoin.createInput("data1");
        derbyJoin.createInput("data2");
        derbyJoin.createInput("condition");
        derbyJoin.createOutput("result");
        derbyJoin.addInput("condition", sql);
        derbyJoin.connectInput("data1", outputLeft);
        derbyJoin.connectInput("data2", outputRight);
        builder.add(derbyJoin);

        return derbyJoin.getOutput("result");
    }
   
}