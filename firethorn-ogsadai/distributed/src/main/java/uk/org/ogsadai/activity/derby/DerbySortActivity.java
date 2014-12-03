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

package uk.org.ogsadai.activity.derby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.block.MismatchedInputGranularityException;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedOptionalListActivityInput;
import uk.org.ogsadai.client.toolkit.activities.relational.SortOrder;
import uk.org.ogsadai.expression.arithmetic.ColumnNameAmbiguousException;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.EnhancedJDBCConnectionProvider;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.sort.DerbySort;

/**
 * This activity sorts a tuple list by storing it in the provided database.
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
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>None.</li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>
 * The <code>columnIds</code> and <code>sortOrders</code> are read in
 * completely, then the <code>TupleMetadata</code> from <code>data</code>. The
 * tuples are then read in turn.
 * </li>
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
public class DerbySortActivity extends MatchedIterativeActivity implements ResourceActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";
    
    /** Name of data input */
    public static final String INPUT_DATA = "data";
    /** Name of sort columns input */
    public static final String INPUT_COLUMN_IDS = "columnsIds";

    public static final String INPUT_SORT_ORDERS = "sortOrders";
    /** Name of data output */
    public static final String OUTPUT_RESULT = "result";
    
    /** Output block writer */
    private BlockWriter mOutput;

    private EnhancedJDBCConnectionProvider mResource;

    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
                new TupleListActivityInput(INPUT_DATA), 
                new TypedOptionalListActivityInput(INPUT_COLUMN_IDS,
                        String.class, null),
                new TypedOptionalListActivityInput(INPUT_SORT_ORDERS, 
                        String.class, null)};
    }

    @Override
    protected void preprocess()
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException,
               ActivityTerminatedException,
               ActivityUserException
    {
        TupleListIterator tuples = (TupleListIterator)iterationData[0];
        ListIterator cols = (ListIterator)iterationData[1];
        ListIterator sortOrders = (ListIterator) iterationData[2];
        
        TupleMetadata tmd = 
                (TupleMetadata) tuples.getMetadataWrapper().getMetadata();
        int[] columns = getSortColumns(cols, tmd);
        SortOrder[] orders = getSortOrders(sortOrders, columns.length);
        
        DerbySort sort = new DerbySort();
        sort.setIndex(columns, orders);
        sort.setResource(mResource);
        sort.store(tuples);
        sort.writeSorted(mOutput);
    }

    @Override
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // no post-processing
    }
    
    private SortOrder[] getSortOrders(
            final ListIterator sortOrdersList,
            int expectedSize) 
    throws ActivityUserException,
           ActivityProcessingException, 
           ActivityTerminatedException 
    {
        if (sortOrdersList == null)
        {
            SortOrder[] orders = new SortOrder[expectedSize];
            Arrays.fill(orders, SortOrder.ASC);
            return orders;
        }
        
        SortOrder[] sortOrders = new SortOrder[expectedSize];
        int idx = 0;

        String value;
        while ((value = (String) sortOrdersList.nextValue()) != null) 
        {
            if (idx == expectedSize) 
            {
                throw new MismatchedInputGranularityException(
                        INPUT_SORT_ORDERS, INPUT_COLUMN_IDS, expectedSize);
            }
            sortOrders[idx] = (value.toUpperCase().equals("ASC")) ? 
                    SortOrder.ASC : SortOrder.DESC;
            idx++;
        }

        if (idx < expectedSize) 
        {
            throw new MismatchedInputGranularityException(
                    INPUT_SORT_ORDERS, INPUT_COLUMN_IDS, expectedSize);
        }
        return sortOrders;
    }
    
    private int[] getSortColumns(ListIterator columnList, TupleMetadata metadata) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // If the columns are not specified then we must sort on every column
        if (columnList == null)
        {
            int[] result = new int[metadata.getColumnCount()];
            for (int i=0; i<result.length; ++i)
            {
                result[i] = i;
            }
            return result;
        }
        
        // The columns have been specified, find the indexes
        List<Integer> sort = new ArrayList<Integer>();
        String value;
        while ((value = (String)columnList.nextValue()) != null)
        {
            TableColumn column = new TableColumn(value);
            try
            {
                sort.add(column.getColumnIndex(metadata));
            }
            catch (ColumnNotFoundException e)
            {
                throw new ActivityUserException(e);
            }
            catch (ColumnNameAmbiguousException e)
            {
                throw new ActivityUserException(e);
            }
        }
        
        int[] result = new int[sort.size()];
        int i=0;
        for (Integer col : sort)
        {
            result[i++] = col;
        }
        return result;
    }

    @Override
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor) 
    {
        mResource = (EnhancedJDBCConnectionProvider)resourceAccessor;
    }

    @Override
    public Class<EnhancedJDBCConnectionProvider> getTargetResourceAccessorClass() 
    {
        return EnhancedJDBCConnectionProvider.class;
    }



}
