// Copyright (c) The University of Edinburgh,  2007.
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

package uk.org.ogsadai.activity.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * An activity that computes the Cartesian product of two lists.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>input1</code>.  Type: OGSA-DAI list of {@link java.lang.Object}.
 * </li>
 * <li>
 * <code>input2</code>.  Type: OGSA-DAI list of {@link java.lang.Object}.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>output1</code>.  Type: OGSA-DAI list of {@link java.lang.Object}.
 * This list when contains the objects in the Cartesian project result that 
 * come from <code>input1</code>.  This list will be identical in length to
 * the list written to <code>output2</code> if 
 * {@link uk.org.ogsadai.metadata.MetadataWrapper} objects are ignored.
 * </li>
 * <li>
 * <code>output2</code>.  Type: OGSA-DAI list of {@link java.lang.Object}.
 * This list when contains the objects in the Cartesian project result that 
 * come from <code>input2</code>.  This list will be identical in length to
 * the list written to <code>output1</code> if 
 * {@link uk.org.ogsadai.metadata.MetadataWrapper} objects are ignored.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>
 * After the first item in the list is read from input <code>input1</code> then 
 * entire contents of the list is read from input <code>input2</code> and stored
 * in memory until the whole of input <code>input1</code> has been read.  Data
 * is written to the output as soon as possible.
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
 * Computes the Cartesian project of the two input lists and outputs the
 * result as two separate lists.  For example, if the list passed to the
 * <code>input1</code> input is (in all the examples we use '<code>{</code>' 
 * to denote the list begin marker and '<code>}</code>' to denote the list end 
 * marker)<br>
 * &nbsp;&nbsp;<code>{ 1 2 3 }</code><br>
 * and the list passed to the <code>input2</code> input is<br>
 * &nbsp;&nbsp;<code>{ a b }</code><br>
 * then the Cartesian project is the set 
 * <i>{(1,a) (1,b) (2,a) (2,b) (3,a) (3,b)}</i>.  This Cartesian product is
 * represented in the outputs by writing the values in the first value of each 
 * pair to output <code>output1</code> and writing the second value of each pair
 * to output <code>output2</code>.  Thus output <code>output1</code> will 
 * contain<br>
 * &nbsp;&nbsp;<code>{ 1 1 2 2 3 3}</code><br>
 * and output <code>output1</code> will contain<br>
 * &nbsp;&nbsp;<code>{ a b a b a b }</code></br>
 * </li>
 * <li>
 * Any objects of type <code>uk.org.ogsadai.metadata.MetadataWrapper</code> read
 * in the inputs will automatically be written to the corresponding output. For
 * example, <code>MetadataWrapper</code> objects written to input 
 * <code>input1</code> will be written to output <code>output1</code> and the
 * same goes of input <code>input2</code> and output <code>output2</code>.
 * <code>MetadataWrapper</code> objects are not used when computing the 
 * Cartesian project and should be ignored when matching up values from the
 * two output lists.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListMultiplyActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ListMultiplyActivity.class);
    
    /** Activity input name - first input of cartesian product.*/
    public static final String INPUT1 = "input1";

    /** Activity input name - second input of cartesian product.*/
    public static final String INPUT2 = "input2";

    /** Activity output name - first output of cartesian product.*/
    public static final String OUTPUT1 = "output1";

    /** Activity output name - first output of cartesian product. */
    public static final String OUTPUT2 = "output2";
    
    /** Output block writer for first output. */
    private BlockWriter mOutput1;
    
    /** Output block writer for second output. */
    private BlockWriter mOutput2;
    
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedListActivityInput(INPUT1, Object.class),//0
            new TypedListActivityInput(INPUT2, Object.class) };//1
    }
    
    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {

        validateOutput(OUTPUT1);
        validateOutput(OUTPUT2);
        mOutput1 = getOutput(OUTPUT1);
        mOutput2 = getOutput(OUTPUT2);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        final ListIterator input1Obj = (ListIterator) iterationData[0];
        boolean foundMd1 = false;
        final ListIterator input2Obj = (ListIterator) iterationData[1];
        final List input2Objs = new ArrayList();
        boolean readInput2 = false;
        try      
        {
            mOutput1.write(ControlBlock.LIST_BEGIN);
            mOutput2.write(ControlBlock.LIST_BEGIN);
            Object block1 = null;
            while ((block1 = input1Obj.nextValue()) != null)
            {
                if ( !(foundMd1) && block1 instanceof MetadataWrapper)
                {
                    mOutput1.write(block1);
                    foundMd1 = true;
                    continue;
                }
                else
                {
                    Object block2 = null;
                    if (readInput2)
                    {

                        for (Iterator it = input2Objs.iterator(); it.hasNext();)
                        {
                            mOutput1.write(block1);
                            mOutput2.write(it.next());
                        }
                    }
                    else
                    {
                        while ((block2 = input2Obj.nextValue()) != null)
                        {
                            if (block2 instanceof MetadataWrapper)
                            {
                                mOutput2.write(block2);
                                continue;
                            }
                            else
                            {                           
                                mOutput1.write(block1);
                                mOutput2.write(block2);
                                input2Objs.add(block2);
                            }                        
                        }
                        readInput2 = true;
                    } 
                }
            } 
            mOutput1.write(ControlBlock.LIST_END);
            mOutput2.write(ControlBlock.LIST_END);
        }
        catch (PipeIOException e)
        {
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        catch (PipeClosedException e)
        {
            // abandon early
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // no post-processing
    }
}