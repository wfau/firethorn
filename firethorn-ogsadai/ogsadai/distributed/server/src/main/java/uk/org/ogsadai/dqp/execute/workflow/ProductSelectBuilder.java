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
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.InnerThetaJoinOperator;

/**
 * Builds activities for operator INNER THETA JOIN.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ProductSelectBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG =
        DAILogger.getLogger(ProductSelectBuilder.class);

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
        LOG.debug("In new ProductSelectBuilder");
        
        InnerThetaJoinOperator operator = (InnerThetaJoinOperator)op;
        SingleActivityOutput outputLeft = outputs.get(0);
        SingleActivityOutput outputRight = outputs.get(1);
        
        GenericActivity product  = 
            new GenericActivity("uk.org.ogsadai.TupleProduct");
        if (mResourceID != null) product.setResourceID(mResourceID);
        product.createInput("data1");
        product.createInput("data2");
        product.createOutput("result");
        product.connectInput("data1", outputLeft);
        product.connectInput("data2", outputRight);
        
        String readFirst = (String)operator.getAnnotation("readFirst");
        if (readFirst == null || readFirst.equals("left"))
        {
            readFirst = "data1";
        }
        else
        {
            readFirst = "data2";
        }
        product.createInput("readFirst");
        product.addInput("readFirst", readFirst);

        builder.add(product);
        
        String sql = operator.getPredicate().toString();

        GenericActivity select =
            new GenericActivity("uk.org.ogsadai.TupleSelect");
        select.createInput("data");
        select.createInput("condition");
        select.createOutput("result", GenericActivity.LIMITED_VALIDATION);
        
        select.connectInput("data", product.getOutput("result"));
        select.addInput("condition", sql);
        builder.add(select);
        
        return select.getOutput("result");
    }
}
