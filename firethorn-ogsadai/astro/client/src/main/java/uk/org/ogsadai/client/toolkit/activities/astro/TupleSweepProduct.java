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


package uk.org.ogsadai.client.toolkit.activities.astro;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.DoubleData;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.StringData;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.astro.TupleSweepProductActivity</code>.
 * This activity executes a join of two ordered datasets where the join
 * condition is a range:
 * <pre>
 * a - windowSize &lt; x &lt; a + windowSize
 * </pre>
 * where a is a column of the first dataset and x is a column of the second
 * dataset.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li><code>data1</code>. Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * </li>
 * <li><code>data2</code>. Type: Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. This is a mandatory input.
 * </li>
 * <li><code>column1</code>. Type: {@link java.lang.String} or
 * {@link java.lang.Number}. Index or name of column in data1 to match against
 * the range. This is a mandatory input.</li>
 * <li><code>column2</code>. Type: {@link java.lang.String} or
 * {@link java.lang.Number}. Index or name of column in data2 to match against
 * the range. This is a mandatory input.</li>
 * <li><code>windowSize</code>. {@link java.lang.Number}. Specifies the range of
 * the sweep window. This is a mandatory input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>result</code>. Type: OGSA-DAI list of
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. The tuples produced by the
 * query.</li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>First, a value is read from each of the column1, column2, windowSize
 * inputs. Values from the second data input that match the sweep window are
 * stored in memory. The first data input is streamed through.</li>
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
 * This activity accepts two ordered tuple lists and performs an (inner) join
 * with condition <code>column1 - windowSize < column2 < column1 + windowSize.</code></li>
 * <li>Both input data sets must be ordered.</li>
 * <li>The columns that are being compared must be numeric.</li>
 * <li>Partial data may be produced if an error occurs at any stage of
 * processing.</li>
 * <li>The activity stores the matching window of the data2 input for each value
 * of data1. All data is stored in memory. Users should be aware that if the
 * window size provided is large there is a risk of the activity running out of
 * memory.</li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote the list
 * begin marker and '<code>}</code>' to denote the list end marker and
 * parentheses to denote OGSA-DAI tuples):
 * 
 * <pre>
 * column1: a
 * column2: x
 * windowSize: 0.2
 * data1: { metadata(a, b) (1.0, 2) (2.0, 1) (NULL, 1) (3, 5) }
 * data2: { metadata(x, y) (1.1, 56) (1.9, -23) (2.1, 2) (NULL, 0) }
 * result: { metadata(a, b, x, y) (1.0, 2, 1.1, 56) (2.0, 1, 1.9, -23) (2.0, 1, 2.1, 2) }
 * </pre>
 * 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TupleSweepProduct extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 201";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.TupleSweepProduct");
    
    /** Data1 input name */
    private static final String INPUT_DATA_1 = "data1";
    /** Data2 input name */
    private static final String INPUT_DATA_2 = "data2";
    /** Window size input name. */
    public static final String INPUT_OFFSET = "offset";
    /** Window size input name. */
    public static final String INPUT_SIZE = "size";
    /** Window size input name. */
    public static final String INPUT_RADIUS = "radius";
    /** Column 1 input name. */
    public static final String INPUT_COLUMN_1 = "column1";
    /** Column 2 input name. */
    public static final String INPUT_COLUMN_2 = "column2";
    /** Data output name */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data1 input. */
    private SimpleActivityInput mData1Input;
    /** Data2 input. */
    private SimpleActivityInput mData2Input;
    /** Column 1 input. */
    private SimpleActivityInput mColumn1Input;
    /** Column 2 input. */
    private SimpleActivityInput mColumn2Input;
    /** Offset input. */
    private SimpleActivityInput mOffsetInput;
    /** Size input. */
    private SimpleActivityInput mSizeInput;
    /** Window radius input. */
    private SimpleActivityInput mRadiusInput;
    /** Data output. */
    private SimpleActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public TupleSweepProduct() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public TupleSweepProduct(ActivityName activityName) 
    {
        super(activityName);
        mData1Input = new SimpleActivityInput(INPUT_DATA_1);
        mData2Input = new SimpleActivityInput(INPUT_DATA_2);
        mColumn1Input = new SimpleActivityInput(INPUT_COLUMN_1);
        mColumn2Input = new SimpleActivityInput(INPUT_COLUMN_2);
        mOffsetInput = new SimpleActivityInput(INPUT_OFFSET, true);
        mSizeInput = new SimpleActivityInput(INPUT_SIZE, true);
        mRadiusInput = new SimpleActivityInput(INPUT_RADIUS, true);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { 
                mData1Input, mData2Input,
                mColumn1Input, mColumn2Input, 
                mOffsetInput, mSizeInput,
                mRadiusInput};
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
     * Adds a column index to the column1 input.
     * 
     * @param column1
     *            column1 value to add.
     */
    public void addColumn1(final int column1)
    {
        mColumn1Input.add(new IntegerData(column1));
    }

    /**
     * Adds a column name to the column1 input.
     * 
     * @param column1
     *            column1 value to add.
     */
    public void addColumn1(final String column1)
    {
        mColumn1Input.add(new StringData(column1));
    }

    /**
     * Adds a column index to the column2 input.
     * 
     * @param column2
     *            column2 value to add.
     */
    public void addColumn2(final int column2)
    {
        mColumn2Input.add(new IntegerData(column2));
    }

    /**
     * Adds a column name to the column2 input.
     * 
     * @param column2
     *            column2 value to add.
     */
    public void addColumn2(final String column2)
    {
        mColumn2Input.add(new StringData(column2));
    }

    /**
     * Connects the data1 input to the given output.
     * 
     * @param output
     */
    public void connectData1Input(SingleActivityOutput output)
    {
        mData1Input.connect(output);
    }
    
    /**
     * Connects the data2 input to the given output.
     * 
     * @param output
     */
    public void connectData2Input(SingleActivityOutput output)
    {
        mData2Input.connect(output);
    }

    /**
     * Connects the column1 input to the given output.
     * 
     * @param output
     */
    public void connectColumn1Input(SingleActivityOutput output)
    {
        mData1Input.connect(output);
    }
    
    /**
     * Connects the column2 input to the given output.
     * 
     * @param output
     */
    public void connectColumn2Input(SingleActivityOutput output)
    {
        mData2Input.connect(output);
    }

    /**
     * Adds a value to the windowSize input.
     * 
     * @param windowSize
     *            windowSize value to add.
     */
    public void addOffsetAndSize(final double offset, final double size)
    {
        mOffsetInput.add(new DoubleData(offset));
        mSizeInput.add(new DoubleData(size));
    }

    /**
     * Connects the offset input to the given output.
     * 
     * @param output
     *            output to connect
     */
    public void connectOffsetInput(SingleActivityOutput output)
    {
        mOffsetInput.connect(output);
    }
    
    /**
     * Connects the size input to the given output.
     * 
     * @param output
     *            output to connect
     */
    public void connectSizeInput(SingleActivityOutput output)
    {
        mSizeInput.connect(output);
    }
    
    /**
     * Adds a value to the radius input.
     * 
     * @param windowSize
     *            windowSize value to add.
     */
    public void addRadius(final double radius)
    {
        mRadiusInput.add(new DoubleData(radius));
    }

    /**
     * Connects the radius input to the given output.
     * 
     * @param output
     *            output to connect
     */
    public void connectRadiusInput(SingleActivityOutput output)
    {
        mRadiusInput.connect(output);
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
