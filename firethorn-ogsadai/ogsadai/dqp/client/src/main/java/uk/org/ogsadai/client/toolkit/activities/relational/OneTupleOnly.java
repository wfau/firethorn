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
 * <code>uk.org.ogsadai.activity.relational.OneTupleOnlyActivity</code>.
 * This activity completes successfully only if the input data list contains 
 * at most one tuple. 
 * If the input is an empty tuple list then a null tuple is returned. 
 * If the input list has more than one entry then an exception is raised.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This list
 * contains exactly one tuple which is either the input tuple or, if the input 
 * list was empty, a null tuple. 
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.OneTupleOnly</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
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
 * <li>Depending on the input data, this activity either writes exactly one 
 * tuple to the output or raises an exception.</li>
 * <li>If the input tuple list is empty a NULL tuple is written to the output.</li>
 * <li>If the input tuple list has one element, this element is written to the
 * output.</li>
 * <li>If the input tuple list contains more than one element, a 
 * <code>uk.org.ogsadai.TOO_MANY_TUPLES</code> is raised.
 * </li>
 * <li> For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 * data: { metadata  (a, b, c) }
 * result: { metadata  (a, b, c) }
 * </pre>
 * <pre>
 * data: { metadata }
 * result: { metadata  (null, null, null) }
 * </pre>
 * <pre>
 * data: { metadata (a, b, c) (d, e, f) }
 * result: -
 * exception: uk.org.ogsadai.TOO_MANY_TUPLES
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class OneTupleOnly extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.OneTupleOnly");
    
    /** Data input name */
    private static final String INPUT_DATA = "data";
    /** Data output name */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data input */
    private ActivityInput mDataInput;
    /** Output */
    private ActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public OneTupleOnly() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public OneTupleOnly(ActivityName activityName) 
    {
        super(activityName);
        mDataInput = new SimpleActivityInput(INPUT_DATA);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { mDataInput };
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
     * Connects the data input to the given output.
     * 
     * @param output
     */
    public void connectDataInput(SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }
    
    /**
     * Returns the data output.
     * 
     * @return data output
     */
    public SingleActivityOutput getResultOutput()
    {
        return mOutput.getSingleActivityOutputs()[0];
    }

}
