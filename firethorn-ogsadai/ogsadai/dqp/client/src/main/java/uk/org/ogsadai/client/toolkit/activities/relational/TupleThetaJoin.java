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
import uk.org.ogsadai.data.StringData;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.block.TupleThetaJoinActivity</code>. This
 * activity performs a theta join on the input tuple lists. This requires the
 * existance of a primary expression in the join condition.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code> Type: Tuple list. This is a mandatory input.</li>
 * <li> <code>data2</code> Type: Tuple list. This is a mandatory input.</li>
 * <li> <code>condition</code> Type: String. This is a mandatory input. The join
 * condition.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: Tuple list.</li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li> None. </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>The left side of the join input is read in full before the other side
 * is streamed through.</li>
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
 * <li>The server-side activity joins two tuple lists. 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleThetaJoin extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.TupleThetaJoin");
    
    /** Data 1 stream input name */
    private static final String INPUT_LEFT = "data1";
    /** Data 2 stream input name */
    private static final String INPUT_RIGHT = "data2";
    /** Join condition input name */
    private static final String INPUT_CONDITION = "condition";
    /** Name of input that indicates which data input is read first. */
    private static final String INPUT_READ_FIRST = "readFirst";
    /** Result output name */
    private static final String OUTPUT = "result";
    
    /** Data input left */
    private SimpleActivityInput mLeftDataInput;
    /** Data input right */
    private SimpleActivityInput mRightDataInput;
    /** Join condition input */
    private SimpleActivityInput mConditionInput;
    /** Input that indicates which data input is read first. */
    private SimpleActivityInput mReadFirstInput;
    /** Data output */
    private SimpleActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public TupleThetaJoin() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public TupleThetaJoin(ActivityName activityName) 
    {
        super(activityName);
        mLeftDataInput = new SimpleActivityInput(INPUT_LEFT);
        mRightDataInput = new SimpleActivityInput(INPUT_RIGHT);
        mConditionInput = new SimpleActivityInput(INPUT_CONDITION);
        mReadFirstInput = new SimpleActivityInput(INPUT_READ_FIRST);
        mOutput = new SimpleActivityOutput(OUTPUT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] 
        { 
            mLeftDataInput,
            mRightDataInput, 
            mConditionInput,
            mReadFirstInput
        };
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
    public void connectLeftDataInput(SingleActivityOutput output)
    {
        mLeftDataInput.connect(output);
    }
    
    /**
     * Connects the data input to the given output.
     * 
     * @param output
     */
    public void connectRightDataInput(SingleActivityOutput output)
    {
        mRightDataInput.connect(output);
    }

    /**
     * Adds a join condition.
     * 
     * @param condition
     *            join condition
     */
    public void addCondition(String condition)
    {
        mConditionInput.add(new StringData(condition));
    }
    
    /**
     * Connects the data input to the given output.
     * 
     * @param output
     */
    public void connectConditionInput(SingleActivityOutput output)
    {
        mConditionInput.connect(output);
    }
    
    /**
     * Indicates that the right data input is read first and stored.
     */
    public void addReadFirstRightInput()
    {
        mReadFirstInput.add(new StringData(INPUT_RIGHT));
    }

    /**
     * Indicates that the left data input is read first and stored.
     */
    public void addReadFirstLeftInput()
    {
        mReadFirstInput.add(new StringData(INPUT_LEFT));
    }

    /**
     * Connects the read first input to the given output.
     * 
     * @param output
     *            output to connect to
     */
    public void connectReadFirstInput(SingleActivityOutput output)
    {
        mReadFirstInput.connect(output);
    }
    
    /**
     * Returns the data output.
     * 
     * @return data output
     */
    public SingleActivityOutput getDataOutput()
    {
        return mOutput.getSingleActivityOutputs()[0];
    }

}
