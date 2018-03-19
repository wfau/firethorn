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

package uk.org.ogsadai.client.toolkit.activities.relational;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.StringData;

/**
 * This activity sorts a tuple list. If the list is too long to be sorted in
 * memory this activity will write to disk.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code> - Type: OGSA-DAI list of {@link uk.org.ogsadai.tuple.Tuple}
 * with the first item in the list an instance of
 * {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * The data to be sorted.</li>
 * <li>
 * <code>columnIds</code> - Type: OGSA-DAI list of {@link java.lang.String}.
 * This is a mandatory input which specifies the order in which columns are to
 * be sorted. The list can contain zero or more entries.</li>
 * <li>
 * <code>sortOrders</code> - Type: OGSA-DAI list of {@link java.lang.String}.
 * This is an optional input which specifies the sort order in which columns are
 * to be sorted. Two values are allowed: DESC for descending order and ASC for
 * ascending order. The list can contain zero or more entries. If defined, the
 * number of elements must be equal to the number for column IDs. Columns will
 * be sorted in ASCending order if no sort orders are defined.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. The sorted list.</li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.TupleSort</code></li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>
 * The <code>columnIds</code> and <code>sortOrders</code> are read in
 * completely, then the <code>TupleMetadata</code> from <code>data</code>. The
 * tuples are then read in turn.</li>
 * </ul>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li>None.</li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>
 * The <code>columnIds</code> and <code>sortOrders</code> are read in
 * completely, then the <code>TupleMetadata</code> from <code>data</code>.</li>
 * <li>
 * The tuples are then read in turn and inserted into the correct place in the
 * sorted list of tuples.</li>
 * <li>
 * Once all tuples have been read the sorted list is output.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleSort extends BaseActivity
{    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.TupleSort");
    
    /** Data input name */
    private static final String INPUT_DATA = "data";
    /** Sorting columns input name */
    private static final String INPUT_COLUMN_IDS = "columnIds";
    /** Sort orders input name. */
    public static final String INPUT_SORT_ORDERS = "sortOrders";
    /** Data output name */
    private static final String OUTPUT_RESULT = "result";

    /** Data input */
    private SimpleActivityInput mDataInput;
    /** Sorting column names input */
    private SimpleActivityInput mColumnsInput;
    /** Column sort orders input. */
    private SimpleActivityInput mSortOrdersInput;
    /** Data output */
    private SimpleActivityOutput mOutput;

    /** Number of sort columns. */
    private int mColumnsNumber = -1;
    
    /**
     * Constructs a new activity.
     */
    public TupleSort() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     *     Activity name.
     */
    public TupleSort(ActivityName activityName) 
    {
        super(activityName);
        mDataInput = new SimpleActivityInput(INPUT_DATA);
        mColumnsInput = new SimpleActivityInput(INPUT_COLUMN_IDS);
        mSortOrdersInput = new SimpleActivityInput(INPUT_SORT_ORDERS, true);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    @Override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { mDataInput, mColumnsInput, mSortOrdersInput };
    }

    @Override
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[] { mOutput };
    }

    @Override
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // no validation
    }
        
    /**
     * Connects the data input to the given output.
     * 
     * @param output
     *     Activity output.
     */
    public void connectDataInput(SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }
    
    /**
     * Adds the list of sorting columns.
     * 
     * @param columnNames
     *            list of column names by which to sort
     */
    public void addColumnsIds(String[] columnNames)
    {
        mColumnsNumber = columnNames.length;
        
        mColumnsInput.add(ListBegin.VALUE);
        for (int i = 0; i < columnNames.length; i++)
        {
            mColumnsInput.add(new StringData(columnNames[i]));
        }
        mColumnsInput.add(ListEnd.VALUE);
    }
    
    /**
     * Adds the list of sort orders. Expects column IDs to be added. A sort
     * order needs to be defined for every column ID. Adding sort orders is
     * optional.
     * 
     * @param sortOrders
     *            an array of sort orders
     */
    public void addSortOrders(SortOrder[] sortOrders)
    {
        if (mColumnsNumber == -1) 
        {
            throw new IllegalStateException("Column IDs need to be set first");
        } 
        else if (sortOrders.length != mColumnsNumber) 
        {
            throw new IllegalArgumentException(
                    "Sort order needs to be defined for every column ID.");
        }
        else
        {
            mSortOrdersInput.add(ListBegin.VALUE);
            for (int i = 0; i < sortOrders.length; i++)
            {
                mSortOrdersInput.add(new StringData(sortOrders[i].toString()));
            }
            mSortOrdersInput.add(ListEnd.VALUE);
        }
    }
    
    /**
     * Connects the columnIds input to the given output.
     * 
     * @param output
     *     Activity output.
     */
    public void connectColumnIdsInput(SingleActivityOutput output)
    {
        mColumnsInput.connect(output);
    }

    /**
     * Connects the sortOrders input to the given output.
     * 
     * @param output
     *     Activity output.
     */
    public void connectSortOrdersInput(SingleActivityOutput output)
    {
        mSortOrdersInput.connect(output);
    }

    /**
     * Returns the result output.
     * 
     * @return result output.
     */
    public SingleActivityOutput getResultOutput()
    {
        return mOutput.getSingleActivityOutputs()[0];
    }

}
