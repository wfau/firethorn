// Copyright (c) The University of Edinburgh,   2002 - 2009.
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

package uk.org.ogsadai.activity.relational;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.expression.arithmetic.TableColumn;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.serialise.UnsupportedTupleTypeException;

/**
 * Produces a list of tuples containing information about attributes in the
 * input tuple list.  This activity is associated with DQP's filtered table
 * scan.  Filtered table scan is an alternative to a simple JOIN operation when
 * the table scan for one side of the join is expected to produce a small 
 * number of tuples but a table scan for the other side will produce many
 * tuples.  In such cases it may be more efficient to obtain the tuples from
 * the smaller size and use the values in these tuples to limit the data 
 * returned by the other table scan.  This activity is designed scan the tuples
 * produced by the smaller table scan and build up some information that can
 * be used to restrict the data required by the other table scan.
 * <p>
 * This activity works best when there is a small amount of absolute values
 * from the small table scan.  For example, consider the case where we wish
 * to execute the following JOIN over distributed tables:
 * <bt>
 * <tt>
 * SELECT * FROM myTable t JOIN postcode pc ON t.postcode = pc.postcode<br/>
 * WHERE t.id = 1234<br/>
 * </tt>
 * If sending <tt>SELECT * FROM myTable WHERE t.id = 1234</tt> to one database
 * returns just a single tuple with postcode value <tt>"EH5 2PU"</tt> then it 
 * would be inefficient to extract data for every postcode from the 
 * <tt>postcode</tt> table.  This in example this activity will scan the
 * table from querying my table can construct a (tuple-based) data structure
 * that specifies that the only absolute value of the <tt>postcode</tt> column
 * is <tt>"EH5 2PU"</tt>.  This data structure can be used as input to the
 * FilteredTableScan activity and would restrict the data output to only 
 * those tuples with a <tt>postcode</tt> attribute value of <tt>"EH5 2PU"</tt>.
 * <p>
 * The user specifies as input to this activity the maximum number of absolute
 * values permitted.  If there are more absolute values than this then the
 * activity simply outputs the minimum and maximum value.
 * <p>
 * Essentially this activity should be considered, in this implementation at 
 * least, to be ideal for small sets of absolute values but the support for 
 * min and max allows the activity to at least do no harm if there are more than
 * the supported number of absolute values.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>columnIds</code>  Type: OGSA-DAI list of <tt>java.lang.String</tt>
 * objects specifying the names of the attributes (columns) to calculate
 * statistics for.  Column names may be either simply column names such as
 * <tt>"myColumn"</tt> or prefixed with a table name such as 
 * <tt>"myTable.myColumn"</tt>. This is a mandatory input.</li>
 * <li> <code>numBuckets</code>  Type: Number.  Specifies the integer maximum
 * number of buckets. This is a mandatory input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. An echo of the 
 * <tt>data</tt> input.
 * </li>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. These tuples specify
 * the attribute statistics, see below for examples of the tuple format.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: The <tt>columnIds</tt> and 
 * <tt>numBuckets</tt> inputs are read first the the <tt>data</tt> input is
 * streamed. As as each block from the data input is read it is written to
 * the <tt>data</tt> output.  Finally the result data is written to the
 * <tt>result</tt> output.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>If the input data contains less tuples than the specified maximum number
 * of buckets then tuples will be written to the result output specifying 
 * the absolute values of the specified columns.  For example, if the column
 * IDs are <tt>"col1"</tt> and <tt>"col2"</tt> and the <tt>data</tt> input 
 * stream was (in all the examples we use '<tt>{</tt>' to denote
 * the list begin marker and '<tt>}</tt>'):
 * <pre>
 * {
 * Metadata("col1":Integer, "col2":String, "col3":Double)
 * (1, "a", "hello")
 * (2, "b", "goodbye")
 * (3, "c", "hi")
 * }
 * </pre>
 * then tuples and metadata written to the <tt>result</tt> output
 * would be:
 * <pre>
 * {
 * Metadata("attributeName":String, "min":String, "max":String)
 * ("col1", "1", NULL)
 * ("col1", "2", NULL)
 * ("col1", "3", NULL)
 * ("col2", "'a'", NULL)
 * ("col2", "'b'", NULL)
 * ("col2", "'c'", NULL)
 * }
 * </pre>
 * Note that all attributes in the <tt>result</tt> output tuples are of type
 * <tt>String</tt> and values for attributes of type <tt>String</tt> are
 * enclosed in single quotes.  This is because this format is designed to allow
 * a later activity to easily append restrictions to a given SQL query.
 * <p>
 * </li>
 * <li>
 * If the input number of input tuples is more than the maximum bucket size
 * then this simple implementation simply returns the minimum and maximum 
 * values.  For example, for the same input stream as above but with a maximum
 * bucket size of 2 the <tt>result</tt> output would be:
 * <pre>
 * {
 * Metadata("attributeName":String, "min":String, "max":String)
 * ("col1", "1", "3")
 * ("col2", "'a'", "'c'")
 * }
 * </pre>
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class AttributeStatisticsActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(AttributeStatisticsActivity.class);
    
    /** Name of tuples input. */
    public static final String INPUT_DATA = "data";
    /** Name of attributes input. */
    public static final String INPUT_COLUMN_IDS = "columnIds";
    /** Name of numBuckets input. */
    public static final String INPUT_NUM_BUCKETS = "numBuckets";
    /** Name of data output. */
    public static final String OUTPUT_DATA = "data";
    /** Name of result output. */
    public static final String OUTPUT_RESULT = "result";
    
    /** Output block writer. */
    private BlockWriter mDataOutput;
    /** Output block writer. */
    private BlockWriter mResultOutput;
    
    /**
    * {@inheritDoc}
    */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedActivityInput(INPUT_NUM_BUCKETS, Number.class),
            new TypedListActivityInput(INPUT_COLUMN_IDS, String.class),
            new TupleListActivityInput(INPUT_DATA)
        };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT_DATA);
        validateOutput(OUTPUT_RESULT);
        mDataOutput = getOutput(OUTPUT_DATA);
        mResultOutput = getOutput(OUTPUT_RESULT);
    }

    /**
    * {@inheritDoc}
    */
    protected void postprocess() throws ActivityUserException, 
        ActivityProcessingException, ActivityTerminatedException
    {
        // No post-processing
    }


    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
            ActivityUserException
    {
        // Inputs
        int numBuckets = ((Number)iterationData[0]).intValue();
        ListIterator attributes = (ListIterator) iterationData[1];
        TupleListIterator tuples = (TupleListIterator)iterationData[2];
        LOG.debug("Starting iteration.");
        
        // Setup map from attribute name to attribute information
        List<String> attributeList = getAttributes(attributes);
        List<AttributeInfo> attributeInfo = new LinkedList<AttributeInfo>();
        
        LOG.debug("Setting up attribute maps.");
        MetadataWrapper metadata = tuples.getMetadataWrapper();
        TupleMetadata inputMetaData = 
            (TupleMetadata) metadata.getMetadata();
        for(int i=0;i<attributeList.size();i++)
        {
            String currentName = attributeList.get(i);
            TableColumn column = new TableColumn(currentName);
            try
            {
                int index = column.getColumnIndex(inputMetaData);
                ColumnMetadata cmd = inputMetaData.getColumnMetadata(index);
                validateType(cmd.getType());
                AttributeInfo attrInfo = new AttributeInfo(
                        currentName, index, cmd.getType(), numBuckets);
                attributeInfo.add(attrInfo);
            } 
            catch (ColumnNotFoundException e)
            {
                throw new ActivityUserException(e);
            }
            catch (UnsupportedTupleTypeException e)
            {
                throw new ActivityUserException(e);
            }
        }
        
        try
        {
            LOG.debug("Starting to stream input tuples.");
            // write out tuples
            Tuple tuple;
            mDataOutput.write(ControlBlock.LIST_BEGIN);
            mDataOutput.write(metadata);
            while ((tuple = (Tuple)tuples.nextValue()) != null)
            {
                processTuple(attributeInfo, tuple);
                mDataOutput.write(tuple);
            }
            mDataOutput.write(ControlBlock.LIST_END);
            LOG.debug("Finished streaming input tuples.");
            
            // write out stats
            mResultOutput.write(ControlBlock.LIST_BEGIN);
            mResultOutput.write(new MetadataWrapper(createStatMetaData()));
            outputAttributeStats(attributeInfo, mResultOutput);
            mResultOutput.write(ControlBlock.LIST_END);
            LOG.debug("Finished writing output stats.");
        }  
        catch (PipeClosedException e)
        {
            iterativeStageComplete();
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        
        
    }

    /**
     * Outputs the bucket stats for the gathered information.
     * 
     * @param attributeInfo 
     * @param attributeList
     * @param indexes
     * @param indexesMetadata 
     * @param attributeValues
     * @param attributeNames 
     * @param numBuckets 
     * @param resultOutput
     * @throws PipeTerminatedException 
     * @throws PipeIOException 
     * @throws PipeClosedException 
     */
    private void outputAttributeStats(
        List<AttributeInfo> attributeInfo, 
        BlockWriter resultOutput) 
        throws PipeClosedException, PipeIOException, PipeTerminatedException
    {
        for(AttributeInfo attrInfo : attributeInfo)
        {
            if (!attrInfo.haveAbsoluteValues())
            {
                // Too many values
                outputMinMaxRange(attrInfo, resultOutput); 
            }
            else if (attrInfo.getAbsoluteValues().isEmpty())
            {
                // No data
                resultOutput.write(
                    createTuple(attrInfo.getName(), Null.VALUE, Null.VALUE));  
            }
            else
            {
                outputAbsolutes(attrInfo, resultOutput);
            }
        }
    }

    /**
     * Outputs buckets for ranges of values.
     * <p>
     * The algorithm used here is very basic.  Ideally this could be replaced
     * with some clever streaming clustering algorithm.  The assumption of this
     * activity is that typically absolute values can be used rather than 
     * ranges.  Support for ranges is simply designed to at least do not harm
     * for those cases when there are too many absolute values.
     * 
     * @param bucketCount 
     *    desired number of buckets
     * @param attrName
     *    name of the attribute being processed
     * @param type
     *    type of the attribute being processed (as defined in TupleTypes)
     * @param currentValues
     *    list of values for the attribute
     * @param statOutput
     *    block writer to which the output is written
     *    
     * @throws PipeTerminatedException 
     * @throws PipeIOException 
     * @throws PipeClosedException 
     */
    private void outputMinMaxRange(
        AttributeInfo attrInfo,
        BlockWriter resultOutput) 
        throws PipeClosedException, PipeIOException, PipeTerminatedException
    {
        resultOutput.write(
            createTuple(
                attrInfo.getName(), 
                stringValue(attrInfo.getMin(), attrInfo.getType()), 
                stringValue(attrInfo.getMax(), attrInfo.getType())));
    }



    /**
     * Outputs absolute values.
     * 
     * @param currentAttribute
     *    name of attribute
     * @param type
     *    type of this attribute
     * @param currentValues
     *    list of values for this attribute
     * @param resultOutput
     *    output to which the result should be written
     * @throws PipeTerminatedException 
     * @throws PipeIOException 
     * @throws PipeClosedException 
     */
    private void outputAbsolutes(
        AttributeInfo attrInfo,
        BlockWriter resultOutput) 
        throws PipeClosedException, PipeIOException, PipeTerminatedException
    {
        for(Object p: attrInfo.getAbsoluteValues())
        {
            resultOutput.write(
                createTuple(
                    attrInfo.getName(), 
                    stringValue(p, attrInfo.getType()), 
                    Null.VALUE));
        }
    }

    /**
     * 
     * @param p
     * @param type
     * @return
     */
    private String stringValue(Object p, int type)
    {
        switch(type)
        {
            case TupleTypes._INT:
            case TupleTypes._SHORT:
            case TupleTypes._LONG:
            case TupleTypes._FLOAT:
            case TupleTypes._DOUBLE:
            case TupleTypes._BIGDECIMAL:
                return p.toString();
            case TupleTypes._DATE:
            case TupleTypes._TIME:
            case TupleTypes._TIMESTAMP:
            case TupleTypes._CHAR:
            case TupleTypes._STRING:
                return "'" + p + "'";
            default:
                return "'" + p + "'";
        }
    }

    /** 
     * tuple creation method for the stats.
     * 
     * @param attributeName
     * @param minValue
     * @param maxValue
     * @return
     */
    private Tuple createTuple(String attributeName, Object minValue, Object maxValue)
    {
        List list = new ArrayList();
        list.add(attributeName);
        list.add(minValue);
        list.add(maxValue);
        return new SimpleTuple(list);
    }
    
    /**
     * Throws an exception if the given type is unsupported for statistics
     * gathering.
     * 
     * @param colType  column type, as defined in TupleTypes
     * 
     * @throws UnsupportedTupleTypeException 
     *     if the give type is unsupported for statistics gathering
     */
    private void validateType(int colType) throws UnsupportedTupleTypeException
    {
        switch(colType)
        {
            case TupleTypes._INT:
            case TupleTypes._SHORT:
            case TupleTypes._LONG:
            case TupleTypes._FLOAT:
            case TupleTypes._DOUBLE:
            case TupleTypes._BIGDECIMAL:
            case TupleTypes._DATE:
            case TupleTypes._TIME:
            case TupleTypes._TIMESTAMP:
            case TupleTypes._CHAR:
            case TupleTypes._STRING:
                break;
            case TupleTypes._ODBLOB:
            case TupleTypes._ODCLOB:
            case TupleTypes._ODNULL:
            case TupleTypes._FILE:
            default:
                throw new UnsupportedTupleTypeException(colType);
        }
    }

    /**
     * Methods create a tuplemetadata for the stats.
     * 
     * @return
     */
    private TupleMetadata createStatMetaData()
    {
        List<ColumnMetadata> columnmetadata = new ArrayList<ColumnMetadata>();
        String[] columnNames = new String[]{"attributeName", "min", "max"};
        int[] columnTypes = new int[]{TupleTypes._STRING,TupleTypes._STRING,TupleTypes._STRING};
        for (int i = 0; i < columnNames.length; i++)
        {
            ColumnMetadata colmetadata = 
                new SimpleColumnMetadata(columnNames[i], columnTypes[i], 0,0,10);
            columnmetadata.add(colmetadata);
        }
        return new SimpleTupleMetadata(columnmetadata);
    }

    /**
     * Processes a tuple, adding it attribute values to the attribute value
     * lists.
     * 
     * @param attributeInfo 
     *    mapping from attribute names to type and position. The position index
     *    is used to extract the correct value from the tuple.
     * @param tuple
     *    tuple to be processed
     */
    private void processTuple(
        List<AttributeInfo> attributeInfo, 
        Tuple tuple)
    {
        for (AttributeInfo attrInfo: attributeInfo)
        {
            attrInfo.addValue(tuple);
        }    
    }

    /**
     * Gets the list of attributes as strings.
     * 
     * @param attributes
     * @return
     */
    private List<String> getAttributes(ListIterator attributes) 
        throws ActivityUserException, ActivityProcessingException,
               ActivityTerminatedException
    {
        List<String> temporary = new ArrayList<String>();
        String tempString;
        while ((tempString = (String)attributes.nextValue()) != null)
        {
            temporary.add(tempString);
        }
        return temporary;
    }
    
    /**
     * Private class to hold information about an attribute including its
     * absolute values, min, max, type and position in a tuple.
     * 
     * @author The OGSA-DAI Project Team.
     *
     */
    private class AttributeInfo
    {
        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2008";
        
        String mName;
        int mMaxValues;
        int mPosition;
        int mType;
        List<Comparable<Object>> mValues;
        Comparable<Object> mMin;
        Comparable<Object> mMax;
        
        /**
         * Constructor.
         * 
         * @param position
         * @param type
         */
        AttributeInfo(String name, int position, int type, int maxValues)
        {
            mName = name;
            mPosition = position;
            mType = type;
            mMaxValues = maxValues;
            mValues = new LinkedList<Comparable<Object>>();
            mMin = null;
            mMax = null;
        }
        
        String getName()
        {
            return mName;
        }
        
        /**
         * returns the type.
         * 
         * @return
         */
        int getType()
        {
            return mType;
        } 
        
        boolean haveAbsoluteValues()
        {
            return mValues != null;
        }
        
        void addValue(Tuple tuple)
        {
            Comparable<Object> value = 
                (Comparable<Object>) tuple.getObject(mPosition);
                
            // Ignore null values
            if (value == Null.VALUE)
            {
                return;
            }
            
            if (mValues != null && mValues.size() < mMaxValues)
            {
                mValues.add(value);
            }
            else
            {
                // Too many absolute values, not storing them
                mValues = null;
            }
            
            if (mMin == null)
            {
                mMin = value;
                mMax = value;
            }
            else
            {
                if (value.compareTo(mMin)  < 0)
                {
                    mMin = value;
                }
                if (value.compareTo(mMax) > 0)
                {
                    mMax = value;
                }
            }
        }
        
        Comparable<Object> getMin()
        {
            return mMin;
        }
        
        Comparable<Object> getMax()
        {
            return mMax;
        }
        
        List<Comparable<Object>> getAbsoluteValues()
        {
            return mValues;
        }
    }
}
