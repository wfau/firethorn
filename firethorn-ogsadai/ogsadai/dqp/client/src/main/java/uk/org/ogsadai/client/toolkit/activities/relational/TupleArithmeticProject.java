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
 * <code>uk.org.ogsadai.activity.relational.TupleArithmeticProjectActivity</code>
 * . This activity projects columns according to a given set of arithmetic
 * expressions.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code> - Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * This is the data to which the projections are applied.</li>
 * <li> <code>expressions</code> - Type: OGSA-DAI list of {@link java.lang.String}
 * . This input specifies the projections as arithmetic expressions.</li>
 * <li> <code>resultColumnNames</code> - Type: OGSA-DAI list of {@link java.lang.String}
 * . This input specifies the names of the output columns.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code> Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.</li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.TupleArithmeticProject</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>The expressions and output columns names are read before reading a list
 * of tuples.</li>
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
 * <li>The server-side activity projects input columns to output columns 
 * according to a list of arithmetic expressions.</li>
 * <li>The number of input expressions must match the number of output column
 * names. Output column <code>n</code> is assigned the name
 * <code>resultColumnNames(n)</code> and its values are the results of applying
 * <code>expressions(n)</code>.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleArithmeticProject extends BaseActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name. */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.TupleArithmeticProject");
    
    /** Data input name. */
    private static final String INPUT_DATA = "data";
    /** Project expressions. */
    public static final String INPUT_EXPRESSION = "expressions";
    /** Result column names. */
    public static final String INPUT_RESULT_COLUMN_NAMES = "resultColumnNames";

    /** Result output name. */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data input. */
    private ActivityInput mDataInput;
    /** Project expressions input. */
    private SimpleActivityInput mExpressionsInput;
    /** Column names input. */
    private SimpleActivityInput mColumnNamesInput;
    /** Output. */
    private ActivityOutput mOutput;


    /**
     * Constructs a new activity with the default activity name.
     */
    public TupleArithmeticProject() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     *            activity name
     */
    public TupleArithmeticProject(ActivityName activityName) 
    {
        super(activityName);
        mDataInput = new SimpleActivityInput(INPUT_DATA);
        mExpressionsInput = new SimpleActivityInput(INPUT_EXPRESSION);
        mColumnNamesInput = new SimpleActivityInput(INPUT_RESULT_COLUMN_NAMES);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    /** 
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { 
            mDataInput, 
            mExpressionsInput, 
            mColumnNamesInput };
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
     * Connects the data input to the given output.
     * 
     * @param output
     *            output to connect to
     */
    public void connectDataInput(SingleActivityOutput output)
    {
        mDataInput.connect(output);
    }
    
    /**
     * Adds project expressions.
     * 
     * @param expressions
     *            projection expressions
     */
    public void addExpressions(String[] expressions)
    {
        mExpressionsInput.add(ListBegin.VALUE);
        for (String expression : expressions)
        {
            mExpressionsInput.add(new StringData(expression));
        }
        mExpressionsInput.add(ListEnd.VALUE);
    }
    
    /**
     * Connects the expressions input to the given output.
     * 
     * @param output
     *            output to connect to
     */
    public void connectExpressionInput(SingleActivityOutput output)
    {
        mExpressionsInput.connect(output);
    }

    /**
     * Adds the given result column names.
     * 
     * @param columnNames
     *            result column names
     */
    public void addResultColumnNames(String[] columnNames)
    {
        mColumnNamesInput.add(ListBegin.VALUE);
        for (String name : columnNames)
        {
            mColumnNamesInput.add(new StringData(name));
        }
        mColumnNamesInput.add(ListEnd.VALUE);
    }

    /**
     * Connects the output column names input to the given output.
     * 
     * @param output
     *            output to connect to
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
