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


package uk.org.ogsadai.activity.block;

import java.util.List;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityOutputsException;
import uk.org.ogsadai.activity.IterativeActivity;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.exception.MalformedListBeginException;
import uk.org.ogsadai.exception.MalformedListEndException;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * An activity that splits an input into N random partitions. The partitions are
 * not necessarily of the same size. A random seed may be provided so that a
 * particular partitioning can be reproduced.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. This can be any type of blocks. Metadata blocks
 * may precede data blocks.</li>
 * <li> <code>seed</code>. Type: {@link java.lang.Long}. This is an optional
 * input. This specifies a seed for the random generator so that a particular
 * partition can be reproduced.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. This is the same type as the input data. Two or
 * more outputs must be provided. </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>One block from the input <code>seed</code> is consumed before a set of
 * metadata-data blocks from the <code>data</code> input. </li>
 * </ul>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>This activity reads a logical block from the data input and randomly
 * sends it to one of the data outputs.</li>
 * <li>If a metadata block is encountered, preceding the data blocks, then the
 * metadata is sent to each output. For example, if the input is<br>
 * &nbsp;&nbsp;<code>metadata data1 data2 data3 data4</code><br>
 * a random split with two outputs might produce<br>
 * &nbsp;&nbsp;<code>metadata data1 data4</code><br>
 * on output 1 and<br>
 * &nbsp;&nbsp;<code>metadata data2 data3</code><br>
 * on output 2.  Where the metadata block is an instance of 
 * <code>uk.org.ogsadai.metadata.MetadataWrapper</code>.
 * <li>A metadata block (an object of type 
 * <code>uk.org.ogsadai.metadata.MetadataWrapper</code>)
 * indicates the start of a new dataset. For each dataset, the activity reads a
 * value from the random seed input (if present). 
 * <li>This activity operates on granularity zero, that is, a list is treated
 * as one logical block. For example, if the input is (note that on all the 
 * examples we use '<code>{</code>' to denote the list begin marker and 
 * '<code>}</code>' to denote the list end marker)<br>
 * &nbsp;&nbsp;<code>{ 1 2 3 } { A }</code>
 * <br>a random split with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ 1 2 3 }</code><br>
 * on output 1 and<br> 
 * &nbsp;&nbsp;<code>{ A }</code><br>
 * on output 2.
 * <li>A random seed can be specified to reproduce a particular partition of
 * the input data set.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RandomSplitActivity extends IterativeActivity
{
    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Data input name */
    public static final String INPUT_DATA = "data";

    /** Random seed input name */
    public static final String INPUT_RANDOM_SEED = "seed";

    /** Output name */
    public static final String OUTPUT_DATA = "data";
    
    /** Data input */
    private BlockReader mDataInput;
    /** Random seed input (optional) - default value <code>null</code> */
    private BlockReader mSeedInput;
    /** Outputs */
    private BlockWriter[] mOutputs;
    
    /** Previous metadata block or <code>null</code> if none has been read */
    private MetadataWrapper mPreviousMetadata;

    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        mDataInput = getInput(INPUT_DATA);
        mSeedInput = getInput(INPUT_RANDOM_SEED);
        
        List outputs = getOutputs(OUTPUT_DATA);
        if (outputs.size() < 2)
        {
            throw new InvalidActivityOutputsException(2, OUTPUT_DATA);
        }
        mOutputs = new BlockWriter[outputs.size()];
        for (int i=0; i<mOutputs.length; i++)
        {
            mOutputs[i] = (BlockWriter)outputs.get(i);            
        }
    }

    protected void processIteration()
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        try
        {
            Split split = getSplit();
            if (mPreviousMetadata != null)
            {
                split(mPreviousMetadata, split);
            }
            else
            {
                Object block = mDataInput.read();
                if (block == ControlBlock.NO_MORE_DATA)
                {
                    iterativeStageComplete();
                }
                else if (block == ControlBlock.LIST_BEGIN)
                {
                    // we have granularity > 0
                    splitLists(split);
                }
                else 
                {
                    // we have granularity 0
                    split(block, split);
                }
            }
        }
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (PipeClosedException e)
        {
            iterativeStageComplete();
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
     * Returns the split that distributes blocks to the outputs.
     * @return a split object
     * @throws PipeIOException
     * @throws PipeTerminatedException
     * @throws DataError
     * @throws InvalidInputValueException
     */
    private Split getSplit() 
        throws PipeIOException, 
               PipeTerminatedException,
               DataError, 
               InvalidInputValueException
    {
        Long seed = null;
        if (mSeedInput != null)
        {
            Object block = mSeedInput.read();
            if (!(block instanceof Long))
            {
                throw new InvalidInputValueException(
                        INPUT_RANDOM_SEED, Long.class, block.getClass());
      
            }
            seed = (Long)block;
        }
        Split split = new RandomSplit(seed);
        split.setSize(mOutputs.length);
        return split;
    }

    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // no post-processing
    }
    
    /**
     * Splits a sequence of blocks in which each logical block is a list.
     * 
     * @param split
     *            split object
     * @throws DataError
     * @throws PipeIOException
     * @throws PipeTerminatedException
     * @throws PipeClosedException
     * @throws MalformedListBeginException
     * @throws MalformedListEndException
     */
    private void splitLists(Split split) 
        throws DataError, 
               PipeIOException, 
               PipeTerminatedException, 
               PipeClosedException, 
               MalformedListBeginException, 
               MalformedListEndException
    {
        Object block;
        int output = split.next();
        int listDepth = 1;
        
        // write the first block that has been read before
        mOutputs[output].write(ControlBlock.LIST_BEGIN);
        
        // now iterate until the end of this list
        while ((block = mDataInput.read()) != ControlBlock.NO_MORE_DATA) 
        {
            if (block == ControlBlock.LIST_BEGIN)
            {
                listDepth++;
            }
            else if (block == ControlBlock.LIST_END)
            {
                listDepth--;
            }
            mOutputs[output].write(block);
            if (listDepth == 0)
            {
                return;
            }
        }
        
        // we have reached the end of the stream
        if (listDepth > 0)
        {
            throw new MalformedListBeginException(INPUT_DATA);
        }
        else if (listDepth < 0)
        {
            throw new MalformedListEndException(INPUT_DATA);
        }
        else
        {
            iterativeStageComplete();
        }
    }
    
    /**
     * Splits a sequence of objects.
     * 
     * @param first
     *            first block in the sequence
     * @param split
     *            split object
     * @throws DataError
     * @throws PipeIOException
     * @throws PipeTerminatedException
     * @throws PipeClosedException
     */
    private void split(Object first, Split split)
        throws DataError, 
               PipeIOException, 
               PipeTerminatedException, 
               PipeClosedException
    {
        if (first instanceof MetadataWrapper)
        {
            for (int i = 0; i < mOutputs.length; i++)
            {
                mOutputs[i].write(first);
                mPreviousMetadata = null;
            }
        }
        else
        {
            mOutputs[split.next()].write(first);
        }
        
        Object block;
        while ((block = mDataInput.read()) != ControlBlock.NO_MORE_DATA)
        {
            if (block instanceof MetadataWrapper)
            {
                mPreviousMetadata = (MetadataWrapper)block;
                return;
            }
            mOutputs[split.next()].write(block);
        }
        iterativeStageComplete();
    }

}
