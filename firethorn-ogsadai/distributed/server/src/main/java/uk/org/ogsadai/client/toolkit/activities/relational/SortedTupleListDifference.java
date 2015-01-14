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

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.relational.SortedTupleListDifferenceActivity</code>
 * . This activity performs a set difference on the input lists. It is assumed
 * that both input lists are sorted.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * This is the data from which the other list is removed.</li>
 * <li> <code>data2</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * This is the data which is removed from the other list.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.SortedTupleListDifference</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A list begin marker and the metadata block is read from each input before
 * processing starts.</li>
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
 * <li>The server-side activity removes occurrences of tuples of the
 * <code>data2</code> list from the <code>data1</code> input list.</li>
 * <li>The input lists are assumed to be sorted by all columns.</li>
 * <li>If the metadata of the input lists do not match a user exception
 * <code>MISMATCHED_METADATA_ERROR</code> is thrown.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SortedTupleListDifference extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.SortedTupleListDifference");
    
    /** Data input name */
    private static final String INPUT_DATA_1 = "data1";
    /** Difference data input name */
    private static final String INPUT_DATA_2 = "data2";
    /** Data output name */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data input */
    private SimpleActivityInput mDataInput;
    /** Difference input */
    private SimpleActivityInput mDifferenceInput;
    /** Data output */
    private SimpleActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public SortedTupleListDifference() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public SortedTupleListDifference(ActivityName activityName) 
    {
        super(activityName);
        mDataInput = new SimpleActivityInput(INPUT_DATA_1);
        mDifferenceInput = new SimpleActivityInput(INPUT_DATA_2);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { mDataInput, mDifferenceInput };
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
     * Connects the data 1 input to the given output.
     * 
     * @param output
     *            output to connect to
     */
    public void connectData1Input(SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }
    
    /**
     * Connects the data 2 input to the given output.
     * 
     * @param output
     *            output to connect to
     */
    public void connectData2Input(SingleActivityOutput output)
    {
        mDifferenceInput.connect(output);
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
