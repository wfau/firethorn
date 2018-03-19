// Copyright (c) The University of Edinburgh,  2009.
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


package uk.org.ogsadai.client.toolkit.activities.transform;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;

/**
 * An activity that converts a list of binary arrays into OGSA-DAI tuples.
 * The result is output as an OGSA-DAI list of tuples.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of <code>byte[]</code>. This is a
 * list of tuples rendered in an OGSA-DAI internal binary format to be converted
 * into tuples.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>result</code> Type: OGSA-DAI list of {@link uk.org.ogsadai.tuple.Tuple}
 * with the first item in the list an instance of
 * {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.ByteArrayToTuple</code>
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
 * <li>
 * The activity receives as input a list of tuples in binary format and converts
 * the binary data into a list of OGSA-DAI tuples.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ByteArraysToTuple extends BaseActivity implements Activity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2009.";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = 
        new ActivityName("uk.org.ogsadai.ByteArraysToTuple");
    
    /** Data input name. */
    public static final String DATA_INPUT = "data";
    
    /** Result output name. */
    public static final String RESULT_OUTPUT = "result";

    /** Data input. */
    private ActivityInput mDataInput;
    
    /** Result output. */
    private ActivityOutput mResultOutput;
    
    /**
     * Constructor.
     */
    public ByteArraysToTuple()
    {
        super(DEFAULT_ACTIVITY_NAME);
        mDataInput                 = new SimpleActivityInput(DATA_INPUT);
        mResultOutput              = new SimpleActivityOutput(RESULT_OUTPUT);
    }

    /**
     * Connects the data input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectDataInput(final SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }

    /**
     * Gets the result output.  This output will contain the web row set output.
     * 
     * @return data output.
     */
    public SingleActivityOutput getResultOutput()
    {
        return mResultOutput.getSingleActivityOutputs()[0];
    }
    
    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        mDataInput.validateState();
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[]{ mDataInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[]{ mResultOutput };
    }
    
}
