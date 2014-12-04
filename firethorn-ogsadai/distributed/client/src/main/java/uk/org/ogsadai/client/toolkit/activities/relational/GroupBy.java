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
 *  Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.relational.GroupByActivity</code>.
 * This activity groups a list of tuples according to some group by columns and 
 * aggregate functions.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>columnIds</code>. Type: OGSA-DAI list of {@link java.lang.String} 
 * The IDs of the group-by columns but the list may be empty.</li>
 * <li> <code>aggregates</code>. Type: OGSA-DAI list of {@link java.lang.String} 
 * The aggregate functions to be applied to each group. This is a mandatory
 * input but the list may be empty.</li>
 * <li> <code>resultColumnNames</code>. Type: OGSA-DAI list of {@link java.lang.String} 
 * Column names of the result columns. This is a mandatory input.</li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * The product of the two input data streams.
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.GroupBy</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A list is read from each of the <code>columnIds</code>, 
 * <code>aggregates</code>, and the <code>resultColumnNames</code> inputs 
 * before the input data is processed.
 * </li>
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
 * Input tuples are grouped by providing column names. For each group, the 
 * provided aggregate functions are applied to all tuples in the group. The
 * output tuple contains the grouping columns and the values of the aggregate
 * functions.
 * </li>
 * <li>
 * This activity behaves like a GROUP BY clause in an SQL statement.
 * </li>
 * <li>
 * The supported aggregate functions are configured server-side. Check with your
 * server administrator which functions are supported. 
 * </li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 *   columnIds: { "city" }
 *   aggregates: { }
 *   resultColumnNames: { "c" }
 *   data: { metadata("name", "city") ("Amy", "Edinburgh") ("Bartek", "Edinburgh") ("Charles", "London") }
 *   result: { metadata("c") ("Edinburgh") ("London") }
 * </pre>
 * <pre>
 *   columnIds: { "category" }
 *   aggregates: { "AVG(age)" }
 *   resultColumnNames: { "category", "average age" }
 *   data: { metadata("age", "category") (49, "A") (35, "B") (23, "A") (42, "A") (24, "B") (53, "C") }
 *   result: { metadata("category", "average age") ("A", 38) ("B", 29.5) ("C", 53) }
 * </pre>
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class GroupBy extends BaseActivity
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Default activity name. */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.GroupBy");
    
    /** Data input name. */
    private static final String INPUT_DATA = "data";
    /** Group by columns input name. */
    private static final String INPUT_GROUPBY_COLUMNS = "columnIds";
    /** Aggregate functions input name. */
    private static final String INPUT_AGGREGATES = "aggregates";
    /** Output column names input name. */
    private static final String INPUT_RESULT_COLUMN_NAMES = "resultColumnNames";
    /** Data output name. */
    private static final String OUTPUT_RESULT = "result";

    /** Data input. */
    private SimpleActivityInput mDataInput;
    /** Group by columns input. */
    private SimpleActivityInput mGroupByColumnsInput;
    /** Aggregate functions input. */
    private SimpleActivityInput mAggregatesInput;
    /** Column names input. */
    private SimpleActivityInput mResultColumnNamesInput;
    /** Data output. */
    private SimpleActivityOutput mOutput;


    /**
     * Constructs a new activity.
     */
    public GroupBy() 
    {
       this(DEFAULT_NAME);
    }

    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     *            activity name
     */
    public GroupBy(ActivityName activityName) 
    {
        super(activityName);
        
        mDataInput = new SimpleActivityInput(INPUT_DATA);

        mGroupByColumnsInput = new SimpleActivityInput(INPUT_GROUPBY_COLUMNS);
        mAggregatesInput = new SimpleActivityInput(INPUT_AGGREGATES);
        mResultColumnNamesInput = new SimpleActivityInput(
            INPUT_RESULT_COLUMN_NAMES, true);

        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    /** 
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { 
            mDataInput, 
            mGroupByColumnsInput, 
            mAggregatesInput,
            mResultColumnNamesInput 
        };
    }

    /** 
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs()
    {
        return new ActivityOutput[] { mOutput };
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
     * Adds the list of grouping attributes.
     * 
     * @param columnNames
     *            list of grouping attributes
     */
    public void addColumnsIds(String[] columnNames)
    {
        mGroupByColumnsInput.add(ListBegin.VALUE);
        for (String name : columnNames)
        {
            mGroupByColumnsInput.add(new StringData(name));
        }
        mGroupByColumnsInput.add(ListEnd.VALUE);
    }

    /**
     * Connects the group by attributes input to the given output.
     * 
     * @param index
     *            the input index where the output is connected
     * @param output
     *            output to connect to
     */
    public void connectColumnsIdsInput(int index, SingleActivityOutput output)
    {
        mGroupByColumnsInput.connect(index, output);
    }
    
    /**
     * Adds a list of aggregate function definitions.
     * 
     * @param aggregates
     *            list of aggregate functions
     */
    public void addAggregates(String[] aggregates)
    {
        mAggregatesInput.add(ListBegin.VALUE);
        for (String def : aggregates)
        {
            mAggregatesInput.add(new StringData(def));
        }
        mAggregatesInput.add(ListEnd.VALUE);
    }
    
    /**
     * Connects the column input to the given output.
     * 
     * @param index
     *            the input index where the output is connected
     * @param output
     */
    public void connectAggregatesInput(int index, SingleActivityOutput output)
    {
        mAggregatesInput.connect(index, output);
    }
    
    /**
     * Adds the list of result column names.
     * 
     * @param columnNames
     *            list of result column names.
     */
    public void addResultColumnNames(String[] columnNames)
    {
        mResultColumnNamesInput.add(ListBegin.VALUE);
        for (String name : columnNames)
        {
            mResultColumnNamesInput.add(new StringData(name));
        }
        mResultColumnNamesInput.add(ListEnd.VALUE);
    }

    /**
     * Connects the result column names input to the given output.
     * 
     * @param index
     *            the input index where the output is connected
     * @param output
     *            output to connect to
     */
    public void connectResultColumnNamesInput(
        int index, SingleActivityOutput output)
    {
        mResultColumnNamesInput.connect(index, output);
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
