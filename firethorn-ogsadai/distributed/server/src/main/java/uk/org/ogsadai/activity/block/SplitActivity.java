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

import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityOutputsException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.MalformedListEndException;
import uk.org.ogsadai.exception.MalformedListBeginException;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * An activity that splits an input stream to multiple output streams using a
 * round robin algorithm.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>input</code>.  Type: {@link java.lang.Object} or OGSA-DAI list
 *      of {@link java.lang.Object} possibly including sublists.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>output</code>.  Type: {@link java.lang.Object} or OGSA-DAI list
 *      of {@link java.lang.Object} possibly including sublists. This output
 *      may be specified multiple times.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters:  none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>
 * Each object read from the input is immediately written to one of the outputs.
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
 * Reads blocks from the input and writes them to the outputs using a round 
 * robin algorithm.  For example, if there are two outputs and the input blocks
 * are<br>
 * &nbsp;&nbsp;<code>a b c d e</code><br>
 * then<br>
 * &nbsp;&nbsp;<code>a c e</code><br>
 * will be written to the first output and<br>
 * &nbsp;&nbsp;<code>b d</code><br>
 * will be written to the second output.
 * </li>
 * <li>
 * Blocks of type {@link uk.org.ogsadai.metadata.MetadataWrapper} are treated
 * as special cases and may be written to multiple outputs.  Whenever a block
 * is about to be written to an output it will be preceded on that output by
 * the last read <code>MetadataWrapper</code> block unless that block has 
 * already been written to the output.  For example, if there are two outputs
 * and the input stream is<br>
 * &nbsp;&nbsp;<code>MD1 a b c MD2 d MD3 e f</code><br>
 * where <code>MD1</code>, <code>MD2</code> and <code>MD3</code>
 * are of type <code>MetadataWrapper</code> then<br>
 * &nbsp;&nbsp;<code>MD1 a c MD3 e</code><br>
 * will be written to the first output and<br>
 * &nbsp;&nbsp;<code>MD1 b MD2 d MD3 f</code><br>
 * will be written to the second output.
 * </li>
 * <li>
 * If the input contains any OGSA-DAI lists then the list markers and the 
 * contents of the list will be written to the same output.  List are therefore
 * treated as if they were a single block.  List as sometimes referred to a 
 * <i>logical blocks</i> because although they are streamed through activities
 * in multiple blocks any activities treat them as if they are single blocks.
 * For example, if the input stream was (note that on all the 
 * examples we use '<code>{</code>' to denote the list begin marker and 
 * '<code>}</code>' to denote the list end marker)<br>
 * &nbsp;&nbsp;<code>{ a b } { c d } { e f }</code><br>
 * then with stream<br>
 * &nbsp;&nbsp;<code>{ a b } { e f }</code><br>
 * would be written to the first output and<br>
 * &nbsp;&nbsp;<code>{ c d }</code><br>
 * would be written to the second output.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SplitActivity extends IterativeActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007";

    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SplitActivity.class);
    
    /** Activity inputs name.*/
    public static final String INPUT_NAME = "input";

    /** Activity output name. */
    public static final String OUTPUT_NAME = "output";
    
    /** The input block reader. */
    private BlockReader mInput;
    
    /** The output block writer. */
    private BlockWriter[] mOutputs;
    
    /** The volume of outputs. */
    private int mWritersSize;
    
    /** Indicates to the number of outputs that metadata has been written to. */
    private int mHasWrittenMetadata;
    
    /**
     * Counter of the current block so as to compute on which output is should
     * be written to.
     */
    private int mPosition = 0;
    
    /** Counts the granularity depth of the input. */
    int mListDepth;
    
    /** The current metadata block. */
    Object mMetadataBlock; 
    
    /** The previous metadata block. */
    Object mPrevMetadataBlock;
    
    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateInput(INPUT_NAME);
        mInput = getInput();

        List outputs = getOutputs(OUTPUT_NAME);
        mWritersSize = outputs.size();
        if (mWritersSize < 2)
        {
            throw new InvalidActivityOutputsException(mWritersSize,
                    OUTPUT_NAME);
        }
        mOutputs = new BlockWriter[mWritersSize];
        for (int i=0; i< mWritersSize; i++)
        {
            mOutputs[i] = (BlockWriter)outputs.get(i);
        }
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
            block = mInput.read();
            if (block != ControlBlock.NO_MORE_DATA)
            {
                if (block == ControlBlock.LIST_BEGIN)
                {
                    mOutputs[mPosition].write(block);
                    mListDepth++;
                }
                else if (block == ControlBlock.LIST_END)
                {
                    mOutputs[mPosition].write(block);
                    mListDepth--; 
                    if (mListDepth == 0)
                    {                                         
                        mPosition++;
                        //reached at the end of inputs
                        if (mPosition == mWritersSize) 
                        {
                            mPosition = 0;
                        }
                    }
                }
                // granularity -> 0
                else if (mListDepth == 0)
                {
                    if (block instanceof MetadataWrapper)
                    {
                        mPrevMetadataBlock = mMetadataBlock;
                        mMetadataBlock = block;
                    }
                    else
                    {
                        splitGranularityZero(mPosition,block,
                                mMetadataBlock,mPrevMetadataBlock);
                        mPosition++;
                        //reached at the end of inputs
                        if (mPosition == mWritersSize) 
                        {
                            mPosition =0;
                        }
                    }               
                }
                //granularity >=1
                else
                {
                    mOutputs[mPosition].write(block);
                }
                //list with more end markers
                if (mListDepth < 0)
                {
                    throw new MalformedListEndException(INPUT_NAME);
                }
            }
            else
            {
                if (mListDepth > 0)
                {
                    throw new MalformedListBeginException(INPUT_NAME);
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
            // abandon early
            iterativeStageComplete();
        }
        catch (MalformedListEndException e)
        {
            throw new ActivityUserException(e);
        }
        catch (MalformedListBeginException e)
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
     * This method applies the splitting algorithm in case of zero granularity of
     * the input.
     * 
     * @param pos
     *            indicates on which output the current block should be written
     *            to.
     * @param block
     *            the current block
     * @param metadataBlock
     *            the current metadata block
     * @param prevMetadataBlock
     *            the previous metadata block
     * @throws PipeTerminatedException
     *             if an input pipe is terminated by another thread
     * @throws PipeIOException
     *             if a pipe error occurs
     * @throws PipeClosedException
     *             if no more output is wanted so pipe is closed.
     */
    private void splitGranularityZero(int pos, Object block,
        Object metadataBlock, Object prevMetadataBlock)
        throws PipeClosedException, PipeIOException,
        PipeTerminatedException
    {
        if (pos != mWritersSize -1)
        {
            if (metadataBlock == null)
            {
                mOutputs[pos].write(block);
            }
            else
            {
                if (prevMetadataBlock != metadataBlock
                        && prevMetadataBlock != null)
                {
                    mHasWrittenMetadata = 0;
                    mOutputs[pos].write(metadataBlock);
                }
                else
                {
                    if (mHasWrittenMetadata < mWritersSize)
                    {
                        mOutputs[pos].write(metadataBlock);
                    }
                }
                mHasWrittenMetadata++;
                mOutputs[pos].write(block);
            }
        }
        else
        {
            if (metadataBlock == null)
            {
                mOutputs[mWritersSize - 1].write(block);
            }
            else
            {
                if (prevMetadataBlock != metadataBlock
                        && prevMetadataBlock != null)
                {
                    mHasWrittenMetadata = 0;
                    mOutputs[mWritersSize - 1].write(metadataBlock);
                }
                else
                {
                    if (mHasWrittenMetadata < mWritersSize)
                    {
                        mOutputs[mWritersSize - 1].write(metadataBlock);
                        mHasWrittenMetadata++;
                    }
                }
                mOutputs[mWritersSize - 1].write(block);
            }
        }
    }
}
