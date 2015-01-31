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
import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.MalformedListEndException;
import uk.org.ogsadai.exception.MalformedListBeginException;

/**
 * Takes as input a list and a value and outputs a copy of that list and also 
 * another list with the same number of items but with every item replaced by 
 * the value given as input.  Additionally the activity can operate at different
 * list depths.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>repeatedInput</code>. 
 * Type: one of {@link java.lang.Object} or an OGSA-DAI list of 
 * {@link java.lang.Object} possibly containing nested lists.  The input to be
 * repeated for each value of the controlling input's list.
 * </li>
 * <li>
 * <code>input</code>. 
 * Type: OGSA-DAI list of {@link java.lang.Object} possibly containing nested 
 * lists.  The controlling input.
 * </li>
 * <li>
 * <code>granularity</code>. 
 * Type: {@link java.lang.Integer}.
 * The depth of the list at which to operate.  This is an optional input with a
 * default value of 1.  This default value will work fine for simple non-nested
 * lists.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>repeatedOutput</code>. 
 * Type: OGSA-DAI list of {@link java.lang.Object} possibly containing nested
 * lists.
 * </li>
 * <li>
 * <code>output</code>. 
 * Type: OGSA-DAI list of {@link java.lang.Object} possibly containing nested 
 * lists.  An exact copy of the data received from the <code>input</code> input.
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
 * In each iteration, first a value is read from input 
 * <code>repeatedInput</code> (which can be a single object, a list of objects, 
 * or a nested list), then an integer is read from the <code>granularity</code>
 * input and finally a value is read from the <code>input</code> input (which 
 * again can be a single object, a list of objects, or a nested list).
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
 * Outputs a copy of the <code>input</code> input where all the data values 
 * (at the depth specified by the value of the <code>granularity</code> input)
 * are replaced with the value or list of values received on the 
 * <code>repeatedInput</code> input.
 * </li>
 * <li> 
 * The default granularity is 1, thus for list input(s) it will operate on the
 * elements inside the list.
 * </li>
 * <li>
 * <li>
 * For example, if the repeated input is <code>1</code> and the input is
 * (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker)<br>
 * &nbsp;&nbsp;<code>{ A B }</code><br>
 * then the repeated output will be<br>
 * &nbsp;&nbsp;<code>{ 1 1 }</code>
 * </li>
 * <li>
 * For example, if the repeated input is <code>1</code> and the input is<br>
 * &nbsp;&nbsp;<code>{ { A B } { C } }
 * </code><br>
 * then the repeated output will be<br>
 * &nbsp;&nbsp;<code>{ 1 1 }</code>
 * </li>
 * <li>
 * For example, if the repeated input is<br>
 * &nbsp;&nbsp;<code>{ D }</code><br>
 * and the input is<br>
 * &nbsp;&nbsp;<code>{ A B }</code><br>
 * then the repeated output will be<br>
 * &nbsp;&nbsp;<code>{ { D } { D } 
 * }</code>
 * </li>
 * <li>
 * For example, if the repeated input is <code>1</code>, the granularity is 
 * <code>2</code> and the input is<br>
 * &nbsp;&nbsp;<code>{ { A B } { C }
 * }</code><br>
 * then the repeated output will be<br>
 * &nbsp;&nbsp;<code> { { 1 1 } { 1 } }
 * </code>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListControlledRepeatActivity extends IterativeActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ListControlledRepeatActivity.class);
    
    /** Name of input literal which will be repeated. */
    public static final String INPUT_REPEAT = "repeatedInput";

    /** Name of control stream input. This is simply outputted unchanged. */
    public static final String INPUT_CONTROL_STREAM = "input";
    
    /** Name of granularity input. */
    public static final String INPUT_GRANULARITY = "granularity";

    /** Name of repeated literal output. */
    public static final String OUTPUT_REPEAT = "repeatedOutput";

    /** Name of control stream output. */
    public static final String OUTPUT_CONTROL_STREAM = "output";
    
    /** Input block reader for repeated input. */
    private BlockReader mRepeatedInput;
    
    /** Input block reader for control input.*/
    private BlockReader mControlInput;
   
    /** Input block reader for control input.*/
    private BlockReader mGranularityInput;
   
    /** Output block writer for repeated output. */
    private BlockWriter mRepeatOutput;
    
    /** Output block writer for repeated output. */
    private BlockWriter mControlOutput;
    
    /** The list of repeated blocks. */
    private List mRepeatedList;
    
    /** The granularity depth of the control input. */
    private int mControlListDepth;
    
    /** The granularity where the repetition will be applied to.*/
    private int mGranularity = 1;
    
    /** 
     * Flag indicating if the repeated block has already been repeated or 
     * not. 
     */
    private boolean mIsRepeated = false;
    
    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        validateInput(INPUT_REPEAT);
        validateInput(INPUT_CONTROL_STREAM);
        validateOutput(OUTPUT_REPEAT);
        validateOutput(OUTPUT_CONTROL_STREAM);
        mRepeatedInput = getInput(INPUT_REPEAT);
        mControlInput = getInput(INPUT_CONTROL_STREAM);
        mRepeatOutput = getOutput(OUTPUT_REPEAT);
        mControlOutput = getOutput(OUTPUT_CONTROL_STREAM);
        mGranularityInput = getInput(INPUT_GRANULARITY);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void processIteration() throws ActivityProcessingException,
            ActivityTerminatedException, ActivityUserException
    {
        Object block = null;
        try
        {
            if ((block = mControlInput.read()) != ControlBlock.NO_MORE_DATA)
            {
                if (block == ControlBlock.LIST_BEGIN)
                {
                    mControlListDepth++;
                    // get next repeatable logical block and next granularity
                    if (mControlListDepth == 1)
                    {
                        mRepeatedList = storeRepeatedInput();
                        getNextGranularity();
                    }
                    if (mGranularity >= mControlListDepth)
                    {
                        mRepeatOutput.write(block);
                    }
                    else if(mGranularity == mControlListDepth -1)
                    {
                        //repeat the list
                        for (int i=0; i< mRepeatedList.size(); i++)
                        {
                            mRepeatOutput.write(mRepeatedList.get(i));
                        }
                        mIsRepeated = true;
                    }
                }
                else if (block == ControlBlock.LIST_END)
                {
                    mControlListDepth--;
                    if (mGranularity == mControlListDepth)
                    {
                        mIsRepeated = false;
                    }
                    else if(mGranularity > mControlListDepth)
                    {
                        mRepeatOutput.write(block);
                    }
                }
                //inside lists
                else
                {
                    // repeat if there elements in the list
                    if (!mIsRepeated)
                    {
                        //and if there is no granularity mismatch
                        if (mGranularity > mControlListDepth)
                        {
                            throw new MismatchedInputGranularityException(
                                    INPUT_CONTROL_STREAM, 
                                    INPUT_GRANULARITY,
                                    mGranularity);
                        }
                        else
                        {
                            // repeat the list
                            for (int i = 0; i < mRepeatedList.size(); i++)
                            {
                                mRepeatOutput.write(mRepeatedList.get(i));
                            }
                        }
                    }
                }
                mControlOutput.write(block);
            }
            else
            {
                if (mControlListDepth > 0)
                {
                    throw new MalformedListBeginException(INPUT_CONTROL_STREAM);
                }
                else if (mControlListDepth < 0)
                {
                    throw new MalformedListEndException(INPUT_CONTROL_STREAM);
                }
                Object repeatBlock = mRepeatedInput.read();
                Object granularityBlock = (mGranularityInput == null)? 
                        ControlBlock.NO_MORE_DATA : mGranularityInput.read();
                if (repeatBlock != ControlBlock.NO_MORE_DATA 
                        || granularityBlock != ControlBlock.NO_MORE_DATA)
                {
                    throw new UnmatchedActivityInputsException(
                        new String[] {
                            INPUT_CONTROL_STREAM, 
                            INPUT_REPEAT, 
                            INPUT_GRANULARITY},
                        new Object[] { 
                            block, 
                            repeatBlock, 
                            granularityBlock });
                }
                iterativeStageComplete();
            }
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
            //early finish
            iterativeStageComplete();
        }
        catch (MalformedListBeginException e)
        {
            throw new ActivityUserException(e);
        }
        catch (MalformedListEndException e)
        {
            throw new ActivityUserException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // no post-processing required
    }
    
    /**
     * Auxiliary method that stores the logical block that is to be repeated.
     * 
     * @return A list containing these blocks.
     * @throws ActivityPipeProcessingException
     *             if something happens during activity pipe processing
     * @throws ActivityTerminatedException
     *             if the activity terminates unexpectedly
     * @throws ActivityUserException
     *             in cases of malformed lists being read
     */
    private List storeRepeatedInput() throws ActivityPipeProcessingException,
            ActivityTerminatedException, ActivityUserException
    {
        List output = new ArrayList();
        Object block = null;
        int listDepth = 0;
        try
        {
            block = mRepeatedInput.read();
            if (block == ControlBlock.NO_MORE_DATA)
            {
                throw new UnmatchedActivityInputsException(
                        new String[] {INPUT_CONTROL_STREAM, INPUT_REPEAT},
                        new Object[] { "data", block } );
            }
            while (block != ControlBlock.NO_MORE_DATA)
            {
                if (block == ControlBlock.LIST_BEGIN)
                {
                    listDepth++;
                    output.add(block);
                }
                else if (block == ControlBlock.LIST_END)
                {
                    listDepth--; 
                    output.add(block);
                    if (listDepth == 0)
                    {
                        return output;
                    }
                }
                else if (listDepth == 0)          
                {
                    output.add(block);
                    return output;
                }
                else
                {
                    output.add(block);
                }
                block = mRepeatedInput.read();
            }
            if (listDepth > 0)
            {
                throw new MalformedListBeginException(INPUT_REPEAT);
            }
            else if (listDepth < 0)
            {
                throw new MalformedListEndException(INPUT_REPEAT);
            }
            else
            {
                return output;
            }
        }
        catch (PipeIOException e)
        {
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        catch (MalformedListBeginException e)
        {
            throw new ActivityUserException(e);
        }
        catch (MalformedListEndException e)
        {
            throw new ActivityUserException(e);
        }
    }
    
    /**
     * Auxiliary method that reads the next value from the granularity input, if
     * it exists. Otherwise, the default value is 1.
     * 
     * @throws InvalidInputValueException
     *             if the granularity is not an Integer
     * @throws ActivityPipeProcessingException
     *             if something happens during activity pipe processing
     * @throws ActivityTerminatedException
     *             if the activity terminates unexpectedly
     * @throws UnmatchedActivityInputsException 
     *             if the granularity input was closed but
     * @throws DataError
     *             if the granularity input was closed due to a data error
     */
    private void getNextGranularity() throws InvalidInputValueException,
            ActivityTerminatedException,
            ActivityPipeProcessingException,
            UnmatchedActivityInputsException, 
            DataError
    {

        if (mGranularityInput != null)
        {
            try
            {
                Object block = mGranularityInput.read();
                if (block instanceof Integer)
                {
                    mGranularity = ((Integer) block).intValue();
                }
                else if (block == ControlBlock.NO_MORE_DATA)
                {
                    throw new UnmatchedActivityInputsException(
                        new String[] {
                            INPUT_CONTROL_STREAM, 
                            INPUT_REPEAT, 
                            INPUT_GRANULARITY},
                        // Some dummy data so that the exception message
                        // indicates that the first two inputs have more 
                        // data. This data is not used in the actual message.
                        new Object[] { "data", "data", block } );
                }
                else
                {
                    throw new InvalidInputValueException(
                            INPUT_GRANULARITY,
                            Integer.class,  
                            block.getClass());
                }
            }
            catch (PipeIOException e)
            {
                throw new ActivityPipeProcessingException(e);
            }
            catch (PipeTerminatedException e)
            {
                throw new ActivityTerminatedException();
            }
        }
        else
        {
            mGranularity = 1;
        }
    }
}
