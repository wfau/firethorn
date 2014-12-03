// Copyright (c) The University of Edinburgh, 2010.
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

package uk.org.ogsadai.client.toolkit.activities.relational;

import junit.framework.TestCase;

import org.easymock.MockControl;

import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.test.SingleOutputTestActivity;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.RequestBuilder;

/**
 * TupleSort activity client toolkit class test.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleSortTest extends TestCase 
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /** The activity to use for the tests*/
    TupleSort mActivity;

    /**
     * {@inheritDoc}
     */
    public void setUp()
    {
        mActivity = new TupleSort();
    }
    
    /**
     * The constructor.
     * 
     * @param arg0
     */
    public TupleSortTest(String arg0)
    {
        super(arg0);
    }
    
    /**
     * Tests that full set of inputs works OK.
     * 
     * @throws Exception
     */
    public void testInputs() throws Exception
    {
        // test if illegal state is detected
        try 
        {
            mActivity.addSortOrders(new SortOrder[] { SortOrder.ASC,
                    SortOrder.DESC });
            fail("Expected IllegalStateException");
        } 
        catch (IllegalStateException e) 
        {
            // Expected
        }
        
        // add inputs
        String [] colIDs = new String[] {"a", "b", "c"};
        mActivity.addColumnsIds(colIDs);

        // test if illegal number of sort orders is detected
        try 
        {
            mActivity.addSortOrders(new SortOrder[] { SortOrder.ASC,
                    SortOrder.DESC });
            fail("Expected IllegalStateException");
        } 
        catch (IllegalArgumentException e) 
        {
            // Expected
        }

        mActivity.addSortOrders(new SortOrder[] { SortOrder.ASC,
                SortOrder.DESC, SortOrder.ASC });

        SingleOutputTestActivity inputActivity = 
            new SingleOutputTestActivity();
        mActivity.connectDataInput(inputActivity.getResultOutput());
        
        // verify the inputs can be retrieved
        ActivityInput[] inputs = mActivity.getInputs();
        assertEquals(3, inputs.length);

        // verify the output can be retrieved
        ActivityOutput[] outputs = mActivity.getOutputs();
        assertEquals(1, outputs.length);

        // provide an output for the data
        DeliverToRequestStatus drs = new DeliverToRequestStatus();
        drs.connectInput(mActivity.getResultOutput());

        // provide a callback for building the response
        MockControl builderControl;
        builderControl = MockControl.createControl(RequestBuilder.class);
        RequestBuilder builder = (RequestBuilder) builderControl.getMock();
        builder.mustValidateState();
        builderControl.setReturnValue(true);
        builder.addActivity(null, null, null, null);
        builderControl.setMatcher(MockControl.ALWAYS_MATCHER);
        builderControl.replay();
        
        // verify the build request is ok
        mActivity.buildRequest(builder);
        builderControl.verify();
    }
}
