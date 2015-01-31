// Copyright (c) The University of Edinburgh,  2002 - 2008.
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

import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.IterativeActivity;
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
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * This activity takes a number of inputs and merges their contents
 * into one output.  The activity will switch to the next input after
 * reading all available blocks from the current input. Note that the
 * output will keep only the metadata found in the first input.
 * 
 * <p>
 * Activity Inputs:
 * </p>
 * <ul>
 * <li><code>input</code>. Type: OGSA-DAI list of {@link java.lang.Object}. 
 * A number of list inputs that must be greater or equal to two.</li> 
 * <li><code>input</code>. Type: OGSA-DAI list of {@link java.lang.Object}. 
 * A number of list inputs that must be greater or equal to two.</li> 
 * <li><code>partial</code>. Type: {@link java.lang.Boolean}. 
 * Indicates whether any missing inputs in a list are to be 
 * treated as an error (true) or whether these can be ignored (false).
 * This is an optional input and defaults to false.</li>
 * </ul>
 * <p>
 * Activity Outputs:
 * <ul>
 * <li><code>output</code>. Type: OGSA-DAI list of {@link java.lang.Object} of 
 * the concatenated list of the objects from all the inputs.</li>
 * </ul>
 * 
 * <p>
 * Configuration parameters: none.
 * </p>
 * 
 * <ul>
 * <li>
 * Activity input/output ordering: the objects are concatenated in the order 
 * in which the lists are input.
 * </li>
 * </ul>
 * 
 * <p>
 * Activity contracts:  none.
 * </p>
 * 
 * <p>
 * Target data resource: none.
 * </p>
 * 
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>This activity takes a number of list input(s) and concatenates their 
 * contents into one list output. It will switch to the next input after 
 * reading all available blocks from the current input. Input partial specifies 
 * whether it is considered an error if some of the inputs are unavailable. </li>
 * <li>When operating in partial mode when the activity will attempt to be 
 * robust to errors in any of its inputs. If all an error is detected at all 
 * inputs the activity will close the current list and stop.</li>
 * <li>We assume that we concatenate similar objects; if inputs contain metadata 
 * those would represent the same entities, thus we can keep the metadata of only 
 * the first available metadata object for the output. </li>
 * <li>This is a simplified version of the activity; input granularity could be 
 * used to generalise it as seen in other ListX block activities.</li> 
 * </ul>
 * 
 * <p>
 * This activity extends the
 * {@link uk.org.ogsadai.activity.IterativeActivity base class}. Note that since
 *      each input is read until no more data can be retrieved the whole
 *      activity processing takes place in one call of the
 *      <code>processIteration()</code> method. Therefore, once all blocks are
 *      written to output, the <code>iterativeStageComplete()</code> method 
 *      should be called so that the iterative processing is finalised.
 * </p>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListConcatenateActivity extends IterativeActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh,  2002 - 2008";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG = DAILogger
            .getLogger(ListConcatenateActivity.class);

    /** Activity inputs name - blocks to be concatenated. */
    public static final String INPUT_NAME = "input";
    
    /** Activity input name - whether partial results should be returned or not.*/
    public static final String INPUT_PARTIAL = "partial";

    /** Activity output name - concatenated output. */
    public static final String OUTPUT_NAME = "output";
    
    /** Reader for partial input.*/

    private BlockReader mInputPartial;

    /** Output writer. */
    private BlockWriter mOutput;

    /** The list of inputs. */
    private List mInputs;
    
    /** Indicates if a metadata block was found or not. */
    private boolean mFoundMetadata;
    
    /** Indicates if no more data can be read from an input. */
    private boolean mNoMoreData = false;
    
    /** Flag showing if the partial input value is true or not. */
    private boolean mIsPartial = false;
    
    /** Indicates if its first iteration or not. */
    private boolean mIsFirstIteration = true;
    
    /** Indicates if list start has been written. */
    private boolean mHasWrittenListStart = false;
    
    /** Indicates if list end has been written. */
    private boolean mHasWrittenListEnd = false;

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // perform input/output validation
        mInputs = getInputs(INPUT_NAME);
        if (mInputs.size() < 2)
        {
            throw new InvalidActivityInputsException(mInputs.size(),
                    INPUT_NAME);
        }
        validateOutput(OUTPUT_NAME);
        mOutput = getOutput();
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration() throws ActivityProcessingException,
            ActivityTerminatedException, ActivityUserException
    {
        // When in partial mode we need to keep track of the number of unique
        // inputs that have produced a data error.  If every input has produced
        // a unique input we must stop.
        int dataErrorCount = 0;
        boolean[] dataErrorFlags = new boolean[mInputs.size()];
        for (int i = 0; i < dataErrorFlags.length; ++i)
        {
            dataErrorFlags[i] = false;
        }
        if (hasInput(INPUT_PARTIAL))
        {
            mInputPartial = getInput(INPUT_PARTIAL);
            Object partialObj = null;
            try
            {
                partialObj = mInputPartial.read();
                mIsPartial = ((Boolean) partialObj).booleanValue();
            }
            catch (ClassCastException e)
            {
                throw new InvalidInputValueException(
                    INPUT_PARTIAL, partialObj);
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
            mIsPartial = false;
        }
        while ((!mNoMoreData) && (dataErrorCount < mInputs.size()))
        {
            mFoundMetadata = false;
            // iterate through all the inputs
            for (int i = 0; i < mInputs.size(); i++)
            {
                mHasWrittenListEnd = false;
                try
                {
                    BlockReader input = (BlockReader) mInputs.get(i);
                    Object block = null;
                    while (!mNoMoreData)
                    {
                        //if partial is true just continue
                        //to the next input
                        if (mIsPartial)
                        {
                            try
                            {
                                block = input.read();
                            }
                            catch (DataError e)
                            {
                                if (dataErrorFlags[i] == false)
                                {
                                    ++dataErrorCount;
                                    dataErrorFlags[i] = true;
                                }
                                break;
                            }
                        }
                        else
                        {
                            block = input.read();
                        }
                        if (block == ControlBlock.NO_MORE_DATA)
                        {
                            mNoMoreData = true;
                        }
                        else
                        {
                            if (block == ControlBlock.LIST_BEGIN 
                                    && !mHasWrittenListStart)
                            {
                                // begin sequence
                                mOutput.write(block);
                                mHasWrittenListStart = true;
                                
                            }
                            else
                            {
                                if (block == ControlBlock.LIST_END)
                                {
                                    if (i == mInputs.size() - 1)
                                    {
                                        // end sequence
                                        mOutput.write(block);
                                        mHasWrittenListStart = false;
                                        mHasWrittenListEnd = true;
                                    }
                                    break;
                                }
                                else if (block != ControlBlock.LIST_BEGIN)
                                {
                                    if (block instanceof MetadataWrapper
                                            && !mFoundMetadata)
                                    {
                                        mFoundMetadata = true;
                                        mOutput.write(block);
                                    }
                                    else if (!(block instanceof MetadataWrapper))
                                    {
                                        mOutput.write(block);
                                    }
                                }
                            }
                        }
                    }
                    if (mNoMoreData)
                    {
                        if (!mHasWrittenListEnd && mHasWrittenListStart)
                        {
                            mOutput.write(ControlBlock.LIST_END);
                        }
                        break;
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
                    // consumer wants no more data, so just ignore
                    // therefore finalise iteration
                    iterativeStageComplete();
                }               
            }
            
            // If every input had a data error ensure we output the list begin
            // and list end markers.
            if (dataErrorCount == mInputs.size())
            {
                try
                {
                    if (!mHasWrittenListEnd)
                    {
                        if (!mHasWrittenListStart)
                        {
                            mOutput.write(ControlBlock.LIST_BEGIN);
                        }
                        mOutput.write(ControlBlock.LIST_END);
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
                    // consumer wants no more data, so just ignore
                    // therefore finalise iteration
                    iterativeStageComplete();
                }
            }
            
            // all inputs are read by now so stop iteration
            iterativeStageComplete();
        }
    }

    /**
     * {@inheritDoc}
     *
     * No post-processing.
     */
    protected void postprocess() throws ActivityUserException,
                                        ActivityProcessingException, 
                                        ActivityTerminatedException
    {
        // no post-processing required
    }
}
