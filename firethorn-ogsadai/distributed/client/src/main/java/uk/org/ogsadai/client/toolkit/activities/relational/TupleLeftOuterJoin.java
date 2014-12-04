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
 * <code>uk.org.ogsadai.activity.block.TupleLeftOuterJoinActivity</code>. 
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> Type: Tuple list. This is a mandatory input. There
 * must be at least two data inputs.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: Tuple list.</li>
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
 * <li>A list begin marker and the metadata block is read from each input
 * before processing proceeds.</li>
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
 * <li>The server-side activity executes a left outer join.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleLeftOuterJoin extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.TupleLeftOuterJoin");
    
    private static final String INPUT_LEFT = "data1";
    private static final String INPUT_RIGHT = "data2";
    private static final String INPUT_CONDITION = "condition";
    private static final String OUTPUT = "result";
    
    /** Left data input */
    private SimpleActivityInput mLeftInput;
    /** Right data input */
    private SimpleActivityInput mRightInput;
    /** Condition input */
    private SimpleActivityInput mConditionInput;
    /** Data output */
    private SimpleActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public TupleLeftOuterJoin() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public TupleLeftOuterJoin(ActivityName activityName) 
    {
        super(activityName);
        mLeftInput = new SimpleActivityInput(INPUT_LEFT);
        mRightInput = new SimpleActivityInput(INPUT_RIGHT);
        mConditionInput = new SimpleActivityInput(INPUT_CONDITION);
        mOutput = new SimpleActivityOutput(OUTPUT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { mLeftInput, mRightInput, mConditionInput };
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
        mLeftInput.connect(output);
    }
    
    /**
     * Connects the data input to the given output.
     * 
     * @param output
     */
    public void connectRightDataInput(SingleActivityOutput output)
    {
        mRightInput.connect(output);
    }

    /**
     * Adds a new join condition.
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
     * Returns the data output.
     * 
     * @return data output
     */
    public SingleActivityOutput getDataOutput()
    {
        return mOutput.getSingleActivityOutputs()[0];
    }

}
