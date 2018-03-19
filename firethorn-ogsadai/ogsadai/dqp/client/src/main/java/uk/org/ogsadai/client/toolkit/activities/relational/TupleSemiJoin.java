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
 * <code>uk.org.ogsadai.activity.relational.TupleSemiJoinActivity</code> and 
 * <code>uk.org.ogsadai.activity.relational.TupleAntiJoinActivity</code>. These
 * activities perform a semi-join or anti-join on the input tuple lists. The 
 * choice of which activity to use is done in the constructor.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.
 * This is a mandatory input.</li>
 * <li> <code>data2</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.
 * This is a mandatory input.</li>
 * <li> <code>condition</code> Type: String. This is a mandatory input. The join
 * condition.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.TupleSemiJoin</code> or 
 * <code>uk.org.ogsadai.TupleAntiJoin</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>The right side of the join (<code>data2</code>) input is read in full 
 * before the other side (<code>data1</code>) is streamed through.
 * </li>
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
 * <li>
 * The server-side activity produces a semi-join or anti-join of two tuple 
 * lists.
 * </li>
 * <li>
 * The result of a semi join is the set of all tuples t from the left input 
 * for which there is at least one tuple s from the right input so that the 
 * joined tuple (t, s) matches the condition.
 * </li>
 * <li>
 * The result of an anti-join is the set of all tuples t from the left input 
 * for which there is no tuple s from the right input that would match the 
 * condition (t, s). 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleSemiJoin extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_ACTIVITY = 
        new ActivityName("uk.org.ogsadai.TupleSemiJoin");

    /** Semi-join activity name */
    public static final ActivityName SEMI_JOIN_ACTIVITY = 
        new ActivityName("uk.org.ogsadai.TupleSemiJoin");
    
    /** Anti-join activity name */
    public static final ActivityName ANTI_JOIN_ACTIVITY = 
        new ActivityName("uk.org.ogsadai.TupleAntiJoin");
    
    /** Data input name */
    private static final String INPUT_DATA_1 = "data1";
    /** Data input name */
    private static final String INPUT_DATA_2 = "data2";
    /** Data input name */
    private static final String INPUT_CONDITION = "condition";
    /** Data output name */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data input left */
    private SimpleActivityInput mLeftDataInput;
    /** Data input right */
    private SimpleActivityInput mRightDataInput;
    /** Join condition input */
    private SimpleActivityInput mConditionInput;
    /** Data output */
    private SimpleActivityOutput mOutput;

    /**
     * Constructs a new semi-join activity.
     */
    public TupleSemiJoin() 
    {
       this(DEFAULT_ACTIVITY);
    }
    
    /**
     * Constructs a new activity with the specified activity name.  Use 
     * <code>SEMI_JOIN_NAME</code> for semi-join and <code>ANTI_JOIN_NAME</code> 
     * for anti-join.
     * 
     * @param activityName
     */
    public TupleSemiJoin(ActivityName activityName) 
    {
        super(activityName);
        mLeftDataInput = new SimpleActivityInput(INPUT_DATA_1);
        mRightDataInput = new SimpleActivityInput(INPUT_DATA_2);
        mConditionInput = new SimpleActivityInput(INPUT_CONDITION);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] 
        { 
            mLeftDataInput,
            mRightDataInput, 
            mConditionInput 
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
     * Connects the data1 input to the given output.
     * 
     * @param output
     */
    public void connectData1Input(SingleActivityOutput output)
    {
        mLeftDataInput.connect(output);
    }
    
    /**
     * Connects the data2 input to the given output.
     * 
     * @param output
     */
    public void connectData2Input(SingleActivityOutput output)
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
     * Returns the result output.
     * 
     * @return result output
     */
    public SingleActivityOutput getResultOutput()
    {
        return mOutput.getSingleActivityOutputs()[0];
    }

}
