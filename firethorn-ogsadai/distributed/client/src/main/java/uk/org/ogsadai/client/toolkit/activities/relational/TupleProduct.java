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
 * <code>uk.org.ogsadai.activity.relational.TupleProductActivity</code>. This
 * activity constructs the product from two tuple inputs. This activity iterates 
 * and stores one tuple list in full before iterating through the other tuple
 * list and producing the product. If the list is too long to be stored in 
 * memory it is written to a file.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data1</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>data2</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object. 
 * This is a mandatory input.</li>
 * <li> <code>readFirst</code>. Type: {@link java.lang.String} The name of the
 * data input that is read first (and stored). 
 * This is an optional input with the default value <code>data2</code>.</li>
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
 * <code>uk.org.ogsadai.TupleProduct</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>A value is read from the readFirst input, then a complete tuple list is
 * read from one data input (as specified by the readFirst value), and finally
 * the other data input is streamed through, producing product tuples by 
 * combining each streamed tuple with each of the stored tuples.</li>
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
 * Produces the product of the two input tuple lists, that is, each tuple on
 * the left is merged with each tuple on the right, producing a tuple list of
 * length n*m if n is the length of data list 1 and m is the length of data 
 * list 2.
 * </li>
 * <li>
 * The input stream of the readFirst input is stored. 
 * If the length of the
 * input lists is known in advance, or can be estimated, the user should ensure
 * that the longer list is connected to the other input for minimum
 * memory/storage use.
 * </li>
 * <li>
 * By default, the input from data2 is read first and stored.
 * </li>
 * <li>
 * For example (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker and 
 * parentheses to denote OGSA-DAI tuples):
 * <pre>
 *   data1: { metadataA (1, 2) (3, 4) }
 *   data2: { metadataB (a, b) (c, d) }
 *   result: { metadataAB (1, 2, a, b) (1, 2, c, d) (3, 4, a, b) (3, 4, c, d) }
 * </pre>
 * <pre>
 *   readFirst: data1
 *   data1: { metadataA (1, 2) (3, 4) }
 *   data2: { metadataB (a, b) (c, d) }
 *   result: { metadataAB (1, 2, a, b) (3, 4, a, b) (1, 2, c, d) (3, 4, c, d) }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class TupleProduct extends BaseActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Default activity name */
    public static final ActivityName DEFAULT_NAME = 
        new ActivityName("uk.org.ogsadai.TupleProduct");
    
    /** Data1 input name */
    private static final String INPUT_DATA_1 = "data1";
    /** Data2 input name */
    private static final String INPUT_DATA_2 = "data2";
    /** Read first input name */
    private static final String INPUT_READ_FIRST = "readFirst";
    /** Data output name */
    private static final String OUTPUT_RESULT = "result";
    
    /** Data1 input. */
    private SimpleActivityInput mData1Input;
    /** Data2 input. */
    private SimpleActivityInput mData2Input;
    /** ReadFirst input. */
    private SimpleActivityInput mReadFirstInput;
    /** Data output. */
    private SimpleActivityOutput mOutput;

    /**
     * Constructs a new activity.
     */
    public TupleProduct() 
    {
       this(DEFAULT_NAME);
    }
    
    /**
     * Constructs a new activity with a user-provided name.
     * 
     * @param activityName
     */
    public TupleProduct(ActivityName activityName) 
    {
        super(activityName);
        mData1Input = new SimpleActivityInput(INPUT_DATA_1);
        mData2Input = new SimpleActivityInput(INPUT_DATA_2);
        mReadFirstInput = new SimpleActivityInput(INPUT_READ_FIRST);
        mOutput = new SimpleActivityOutput(OUTPUT_RESULT);
    }

    // Method override
    protected ActivityInput[] getInputs()
    {
        return new ActivityInput[] { mData1Input, mData2Input, mReadFirstInput };
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
     * Adds a value to the readFirst input.
     * 
     * @param readFirst
     *            readFirst value to add.
     */
    public void addReadFirst(final String readFirst)
    {
        mReadFirstInput.add(new StringData(readFirst));
    }

    /**
     * Connects the readFirst input to the given output.
     * 
     * @param output
     *            output to connect
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
