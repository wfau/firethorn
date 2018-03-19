// Copyright (c) The University of Edinburgh, 2008-2012.
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

import uk.org.ogsadai.activity.relational.TupleSortActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.relational.SortOrder;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.SortOperator;

/**
 * Builds activities for operator SORT.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SortBuilder implements ActivityPipelineBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2012.";
    
    private String mResourceID;

    @Override
    public SingleActivityOutput build(
            Operator op, 
            List<SingleActivityOutput> outputs,
            PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        SortOperator operator = (SortOperator)op;
        SingleActivityOutput output = outputs.get(0);
    
        GenericActivity sort =
            new GenericActivity("uk.org.ogsadai.TupleSort");
        if (mResourceID != null) sort.setResourceID(mResourceID);
        sort.createInput(TupleSortActivity.INPUT_DATA);
        sort.createInput(TupleSortActivity.INPUT_COLUMN_IDS);
        sort.createInput(TupleSortActivity.INPUT_SORT_ORDERS);

        sort.createOutput(TupleSortActivity.OUTPUT_RESULT, 
            GenericActivity.LIMITED_VALIDATION);
        sort.connectInput(TupleSortActivity.INPUT_DATA, output);
        
        // extract and add sort attributes
        List<Attribute> sortAttributes = operator.getSortAttributes();
        String[] attributes = new String[sortAttributes.size()];
        int i=0;
        for (Attribute attribute : sortAttributes)
        {
            attributes[i++] = attribute.toString();
        }
        sort.addInputList(TupleSortActivity.INPUT_COLUMN_IDS, attributes);
        
        // extract and add sort orders for attributes
        List<SortOrder> sortOrders = operator.getSortOrders();
        String[] orders = new String[sortOrders.size()];
        i=0;
        for (SortOrder so : sortOrders)
        {
            orders[i++] = so.toString();
        }
        sort.addInputList(TupleSortActivity.INPUT_SORT_ORDERS, orders);
        
        builder.add(sort);
        return sort.getOutput(TupleSortActivity.OUTPUT_RESULT);
    }
    
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
    
}
