// Copyright (c) The University of Edinburgh,  2007.
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

package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.StringData;

/**
 * Copy of the OGSA-DAI SQLBulkLoadTupleActivity.
 * Modified to use batch insert.
 * http://viralpatel.net/blogs/batch-insert-in-java-jdbc/
 * @author The OGSA-DAI Project Team.
 * 
 */
public class BulkInsert extends BaseResourceActivity 
    implements ResourceActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Default activity name. */
    private final static ActivityName DEFAULT_ACTIVITY_NAME = 
        new ActivityName("uk.ac.roe.wfau.firethorn.BulkInsert");
    
    /** 
     * Activity input name(<code>data</code>) - 
     * Tuples to be bulk loaded. 
     * (OGSA-DAI tuple list).
     */
    public final static String DATA_INPUT = "data";
    
    /** 
     * Activity input name(<code>tableName</code>) - 
     * Table name. 
     * (<code>String</code>).
     */
    public final static String TABLENAME_INPUT = "tableName";
    
    /** 
     * Activity output name(<code>result</code>) - 
     * Number of inserted tuples. 
     * (<code>Integer</code>).
     */
    public final static String OUTPUT_RESULT = "result";
    
    /** Data input. */
    private ActivityInput mDataInput;
    
    /** Tablename input. */
    private ActivityInput mTableNameInput;
    
    /** Data output. */
    private ActivityOutput mResultOutput;
        
    
    /**
     * Constructor.
     */
    public BulkInsert()
    {
        super(DEFAULT_ACTIVITY_NAME);
        mDataInput = new SimpleActivityInput(DATA_INPUT);
        mTableNameInput = new SimpleActivityInput(TABLENAME_INPUT);
        
        mResultOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    /**
     * Adds a new table name.
     * 
     * @param tableName the table into which data is to be loaded
     */
    public void addTableName(final String tableName)
    {
        mTableNameInput.add(new StringData(tableName));
    }
    
    /**
     * Connects the table name input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectTableNameInput(final SingleActivityOutput output)
    {
        mTableNameInput.connect(output);
    }
    
    /**
     * Connects the tuple input data to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectDataInput(final SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }
        
    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // Empty method
    }
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[]{ mDataInput, mTableNameInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[]{ mResultOutput };
    }
    
    /**
     * Gets the data output.
     * 
     * @return the status of the bulk loads
     */
    public SingleActivityOutput getDataOutput()
    {
        return mResultOutput.getSingleActivityOutputs()[0];
    }
    
    
    /**
     * Determines if more results are available.
     * 
     * @return 
     *   true if more data is available, false otherwise
     *   
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     *         
     */
    public boolean hasNextResult() throws
        DataStreamErrorException,
        UnexpectedDataValueException,
        DataSourceUsageException
    {
        return mResultOutput.getDataValueIterator().hasNext();   
    }
    
    /**
     * Gets the number of tuples loaded by the next bulk load.
     * 
     * @return 
     *   the number of tuples which were loaded, if more results are present
     *   -1 if no more results are available
     *   
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     *         
     */
    public int nextResult() throws
        DataStreamErrorException,
        UnexpectedDataValueException,
        DataSourceUsageException
    {
        if ( !hasNextResult())
        {
            return -1;
        }
        
        return mResultOutput.getDataValueIterator().nextAsInt();
    }
}
