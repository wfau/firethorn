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
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.relational.MetadataRenameActivity</code>. This
 * activity modifies the tuple metadata and replaces the column name in each 
 * column with the specified name.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * </li>
 * <li> <code>resultColumnNames</code>. Type: A list of {@link java.lang.String}. 
 * This input specifies the new column names. This is a mandatory input.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.MetadataRename</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>The <tt>resultColumnNames</tt> input is read first then the <tt>data</tt>
 * input.</li>
 * </ul>
 * <p>
 * Activity contracts:
 * </p>
 * <ul>
 * <li> None. </li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>The server-side activity modifies the tuple metadata of the input tuple
 * list. The column name in each column is replaced with corresponding column 
 * name from the <tt>resultColumnNames</tt> input.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class MetadataRename extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.MetadataRename");
    
    /** Data input name */
    private static final String INPUT_DATA = "data";
    /** Column names input name */
    private static final String INPUT_RESULT_COLUMN_NAMES = "resultColumnNames";
    /** Data output name */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data input */
    private ActivityInput mDataInput;
    /** Output column names input */
    private ActivityInput mColumnNamesInput;
    /** Output */
    private ActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public MetadataRename() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public MetadataRename(ActivityName activityName) 
    {
        super(activityName);
        mDataInput = new SimpleActivityInput(INPUT_DATA);
        mColumnNamesInput = new SimpleActivityInput(INPUT_RESULT_COLUMN_NAMES);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { mDataInput, mColumnNamesInput };
    }

    // Method override
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[] {mOutput};
    }

    // Method override
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // no validation
    }
        
    /**
     * Adds the result columns names.  All the names in the given array will
     * be included in the list of names given to the activity.
     * 
     * @param columnNames 
     *            new column names
     */
    public void addResultColumnNames(final String[] columnNames)
    {
        mColumnNamesInput.add(ListBegin.VALUE);
        for (String name : columnNames)
        {
            mColumnNamesInput.add(new StringData(name));
        }
        mColumnNamesInput.add(ListEnd.VALUE);
    }

    /**
     * Connects the data input to the given output.
     * 
     * @param output
     */
    public void connectDataInput(SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }
    
    /**
     * Connects the result column names input to the given output.
     * 
     * @param output
     */
    public void connectResultColumnNamesInput(SingleActivityOutput output)
    {
        mColumnNamesInput.connect(output);
    }
    
    /**
     * Returns the result output.
     * 
     * @return result output
     */
    public SingleActivityOutput getResultOutput()
    {
        return mOutput.getSingleActivityOutputs()[0];
    }

}
