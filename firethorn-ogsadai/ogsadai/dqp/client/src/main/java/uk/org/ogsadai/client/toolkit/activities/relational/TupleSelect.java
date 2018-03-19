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
 * <code>uk.org.ogsadai.activity.relational.TupleSelectActivity</code>. This
 * activity selects tuples from the input list according to a condition 
 * (similar to a WHERE clause in SQL).
 * <p> 
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li><code>condition</code>. Type: {@link java.lang.String}. The condition to evaluate
 * for each tuple.</li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * The tuples from the input data that meet the condition.
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.TupleSelect</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A value is read from the condition input before the input data is 
 * processed.</li>
 * </ul>
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
 * Evaluates the condition on a tuple and writes the tuple to the output if
 * the condition evaluates to <code>TRUE</code>.
 * </li>
 * <li>
 * The format of the condition is like an SQL WHERE or HAVING clause.
 * It can contain column names, arithmetic and boolean operators and constants. 
 * </li>
 * <li>
 * Columns can be identified by providing the full name (using the table name
 * prefix and column name TABLE.COLUMN) or by the simple name (using COLUMN name 
 * only without the table name prefix).
 * If a simple column name is ambiguous the full name must be used, otherwise
 * an exception is raised.
 * </li>
 * <li>
 * Like in SQL, comparison to a NULL value always evaluates to FALSE.
 * </li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 *   condition: a < b
 *   data: { metadata(a, b) (1, 2) (2, 1) (NULL, 1) }
 *   result: { metadata(a, b) (1, 2) }
 * </pre>
 * <pre>
 *   condition: (a + b)/3 = c
 *   data: { metadata(a, b, c) (1, 2, 3) (0, 3, 1) (0, 0, 0) }
 *   result: { metadata(a, b, c) (0, 3, 1) (0, 0, 0) }
 * </pre>
 * <pre>
 *   condition: A.id = B.id
 *   data: { metadata(A.id, B.id) (5, 5) (1, 2) (2, 3) (4, 4) }
 *   result: { metadata(A.id, B.id) (5, 5) (4, 4)  }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class TupleSelect extends BaseActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name. */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.TupleSelect");
    
    /** Data input name. */
    private static final String INPUT_DATA = "data";
    /** Condition input name. */
    private static final String INPUT_CONDITION = "condition";
    /** Result output name. */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data input. */
    private ActivityInput mDataInput;
    /** Condition input. */
    private ActivityInput mConditionInput;
    /** Output. */
    private ActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public TupleSelect() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public TupleSelect(ActivityName activityName) 
    {
        super(activityName);
        mDataInput = new SimpleActivityInput(INPUT_DATA);
        mConditionInput = new SimpleActivityInput(INPUT_CONDITION);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    /** 
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { 
            mDataInput, mConditionInput };
    }

    /** 
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[] {mOutput};
    }

    /** 
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException
    {
        // no validation
    }
        
    /**
     * Adds a String to the input.
     * 
     * @param condition
     *            String value to add to input.
     */
    public void addCondition(final String condition)
    {
        mConditionInput.add(new StringData(condition));
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
     * Connects the condition input to the given output.
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
