// Copyright (c) The University of Edinburgh, 2007-2008.
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
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.MalformedListBeginException;
import uk.org.ogsadai.exception.MalformedListEndException;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * Splits an input stream into N random partitions. The activity operates at a
 * specified level of granularity which must be greater than zero. The
 * partitions are not necessarily of the same size. A random seed may be
 * provided so that a particular partitioning can be reproduced.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list (can be nested) of any data type.
 * The data input that will be randomly split. This is a mandatory input. 
 * The OGSA-DAI list must be of at least the granularity provided by the <code>granularity</code> input.
 * </li>
 * <li><code>seed</code>. Type: {@link java.lang.Long}. It specifies a seed for
 * the random generator so that a particular partition can be reproduced.
 * This is an optional
 * input, if omitted then the default value is <code>null</code>.
 * </li>
 * <li><code>granularity</code>. Type: {@link java.lang.Integer}</li>.
 * It specifies the granularity level at which the activity operates.
 * This is an optional input, if omitted then the default value is 1(i.e.
 * an OGSA-DAI list).
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li><code>data</code>. Type: This is exactly the same type as the input <code>data</code>. 
 * The data read from the <code>data</code> input split across a number of outputs.
 * Two or more outputs must be provided. 
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>One block from input <code>seed</code> and another from input
 * <code>granularity</code> are consumed (if provided) before reading from the <code>data</code> input.
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
 * <li>The server-side activity reads a logical block from the data input and
 * randomly sends it to one of the data outputs. It operates on the elements
 * inside a list, at the specified granularity.</li>
 * <li>If a metadata block is encountered, preceding the data blocks, then the
 * metadata is sent to each output. For example 
 * (note that on all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker), 
 * if the input is<br>
 * &nbsp;&nbsp;<code>{ metadata data1 data2 data3 data4 }</code><br> 
 * a random split (at granularity 1) 
 * with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ metadata data1 data4 }</code><br>
 * on output 1 and<br>
 * &nbsp;&nbsp;<code>{ metadata data2 data3 }</code><br> on output 2.
 * <li>For example, a random split of<br>
 * &nbsp;&nbsp;<code>{ 1 2 3 } { A B C D E }</code><br>
 * at granularity 1 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ 1 2 } { B C E }</code><br>
 * on output 1 and<br>
 * &nbsp;&nbsp;<code>{3 } { A D }</code> on output 2.
 * <li>Another example, a random split of<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } } { { A } { B } { C } { D } { E } }</code><br>
 * at granularity 1 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } } { { B } { C } { E } }</code><br>
 * on output 1 and <br>
 * &nbsp;&nbsp;<code>{ { 3 6 } } { { A } { D } }</code><br>
 * on output 2.
 * </li>
 * <li>Another example, a random split of<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } } { { A } { B } { C } { D } { E } }</code><br>
 *  at granularity 0 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } }</code><br>
 * on output 1 and<br> 
 * &nbsp;&nbsp;<code>{ { A } { B } { C } { D } { E } }</code><br>
 * on output 2.
 * </li>
 * <li>Another example, a random split of<br>
 * &nbsp;&nbsp;<code>{ { 1 4 } { 2 5 } { 3 6 } } { { A } { B } { C } { D } { E } }</code><br>
 * at granularity 2 with two outputs might produce<br>
 * &nbsp;&nbsp;<code>{ { 1 } { 2 } { 3 } } { { } { B } { C } { } { E } }</code><br>
 * on output 1 and<br>
 * &nbsp;&nbsp;<code>{ { 4 } { 5 } { 6 } } { { A } { } { } { D } { } }</code><br>
 * on output 2.
 * </li>
 * <li>A random seed can be specified to reproduce a particular partition of
 * the input data set.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListRandomSplitActivity extends IterativeActivity
{
    /** Copyright. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2007-2008.";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ListRandomSplitActivity.class);

    /**
     * Activity input name (<code>data</code>) - Data to be split
     * (OGSA-DAI list of any data type - can be nested).
     */
    public static final String INPUT_DATA = "data";

    /**
     * Activity input name (<code>seed</code>) - The seed
     * ({@link java.lang.Long}).
     */
    public static final String INPUT_RANDOM_SEED = "seed";

    /**
     * Activity input name (<code>granularity</code>) - The granularity level 
     * to apply the partioning to.
     * ({@link java.lang.Integer}).
     */
    public static final String INPUT_GRANULARITY = "granularity";

    /**
     * Activity output name (<code>data</code>) - Produced data
     * (OGSA-DAI list of any data type - can be nested).
     */
    public static final String OUTPUT_DATA = "data";

    /** Block reader for data input. */
    private BlockReader mDataInput;

    /** Block reader for granularity input (optional - default value 1). */
    private BlockReader mGranularityInput;

    /** Block reader for random seed input (optional - default value <code>null</code>). */
    private BlockReader mSeedInput;

    /** Indicates whether the end of the random seed input has been reached. */
    private boolean mSeedClosed = false;

    /** Outputs. */
    private BlockWriter[] mOutputs;

    /**
     * {@inheritDoc}
     */
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        mDataInput = getInput(INPUT_DATA);
        mSeedInput = getInput(INPUT_RANDOM_SEED);
        mGranularityInput = getInput(INPUT_GRANULARITY);

        List outputs = getOutputs(OUTPUT_DATA);
        if (outputs.size() < 2)
        {
            throw new InvalidActivityOutputsException(2, OUTPUT_DATA);
        }
        mOutputs = new BlockWriter[outputs.size()];
        for (int i = 0; i < mOutputs.length; i++)
        {
            mOutputs[i] = (BlockWriter) outputs.get(i);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration() 
        throws ActivityProcessingException,
               ActivityTerminatedException, 
               ActivityUserException
    {
        try
        {
            Split split = getSplit();
            int granularity = getGranularity();

            Object block = mDataInput.read();
            if (block == ControlBlock.NO_MORE_DATA)
            {
                if (mGranularityInput != null && granularity > 0)
                {
                    throw new UnmatchedActivityInputsException(
                            new String[] {INPUT_DATA, INPUT_GRANULARITY },
                            new Object[] {block, new Integer(granularity) });
                }
                if (mSeedInput != null && !mSeedClosed)
                {
                    throw new UnmatchedActivityInputsException(
                            new String[] {INPUT_DATA, INPUT_RANDOM_SEED }, 
                            new Object[] {block, "something" });
                }
                iterativeStageComplete();
            }
            else
            {
                split(block, split, granularity);
            }
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        catch (PipeClosedException e)
        {
            //No more input wanted.
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
     * Returns the next granularity value.
     * 
     * @return granularity or -1 if the input was closed
     * @throws PipeIOException
     * @throws PipeTerminatedException
     *             if the pipe was terminated
     * @throws DataError
     *             if a data error occurs
     * @throws InvalidInputValueException
     *             if the granularity has the wrong type.
     * @throws UnmatchedActivityInputsException
     *             if input values of different granurity are read
     * @throws InvalidInputGranularityException
     *             if the granularity cannot be supported by the activity
     *             functionality.
     */
    private int getGranularity() 
        throws PipeIOException,
               PipeTerminatedException, 
               DataError, 
               InvalidInputValueException,
               UnmatchedActivityInputsException, 
               InvalidInputGranularityException
    {
        int result = 1;
        if (mGranularityInput != null)
        {
            Object block = mGranularityInput.read();
            if (block == ControlBlock.NO_MORE_DATA)
            {
                result = -1;
            }
            else if (!(block instanceof Integer))
            {
                throw new InvalidInputValueException(INPUT_GRANULARITY,
                        Integer.class, block.getClass());
            }
            else
            {
                result = ((Integer) block).intValue();
                if (result < 1)
                {
                    throw new InvalidInputGranularityException(
                            INPUT_GRANULARITY, 1);
                }
            }
        }
        return result;
    }

    /**
     * Returns the split object that distributes objects to outputs. The next
     * random seed value is read from the input if available.
     * 
     * @return a split object
     * @throws PipeIOException
     *             if something is wrong with reading/writing from/to the pipe
     * @throws PipeTerminatedException
     *             if the pipe was terminated
     * @throws DataError
     *             if a data error occurs if a data error occurs.
     * @throws InvalidInputValueException
     *             if the random seed has the wrong type
     */
    private Split getSplit() throws PipeIOException, PipeTerminatedException,
            DataError, InvalidInputValueException
    {
        Long seed = null;
        if (mSeedInput != null)
        {
            Object block = mSeedInput.read();
            if (block != ControlBlock.NO_MORE_DATA)
            {
                if (!(block instanceof Long))
                {
                    throw new InvalidInputValueException(INPUT_RANDOM_SEED,
                            Long.class, block.getClass());
                }
                seed = (Long) block;
            }
            else
            {
                mSeedClosed = true;
            }
        }
        Split split = new RandomSplit(seed);
        split.setSize(mOutputs.length);
        return split;
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // No post-processing
    }

    /**
     * Splits a list at the specified granularity level.
     * 
     * @param first
     *            the first data block.
     * @param split
     *            the list to be split.
     * @param granularity
     *            the granularity level.
     * @throws DataError
     *             if a data error occurs
     * @throws PipeIOException
     *             if something is wrong with reading/writing from/to the pipe
     * @throws PipeTerminatedException
     *             if the pipe was terminated
     * @throws PipeClosedException
     *             if the pipe is closed
     * @throws MalformedListBeginException
     *             If the list has malformed list begin marker.
     * @throws MalformedListEndException
     *             If the list has malformed list end marker.
     * @throws UnmatchedActivityInputsException
     *             if input values of different granurity are read
     */
    private void split(Object first, Split split, int granularity)
        throws DataError, 
               PipeIOException, 
               PipeTerminatedException,
               PipeClosedException, 
               MalformedListBeginException,
               MalformedListEndException, 
               UnmatchedActivityInputsException
    {
        if (granularity < 0)
        {
            throw new UnmatchedActivityInputsException(
                    new String[] {INPUT_DATA, INPUT_GRANULARITY}, 
                    new Object[] { first, ControlBlock.NO_MORE_DATA });
        }

        if (!validateGranularity(first, granularity, ControlBlock.LIST_BEGIN))
        {
            throw new MalformedListBeginException(INPUT_DATA);
        }

        Object block = mDataInput.read();
        if (block == ControlBlock.NO_MORE_DATA)
        {
            iterativeStageComplete();
        }
        else if (block == ControlBlock.LIST_BEGIN)
        {
            // we have granularity > 0
            block = splitLists(split);
        }
        else
        {
            // we have granularity 0
            block = split(block, split);
        }

        if (!validateGranularity(block, granularity, ControlBlock.LIST_END))
        {
            throw new MalformedListBeginException(INPUT_DATA);
        }
    }

    /**
     * Iterates through list markers and validates that the lists are nested to
     * the specified level.
     * 
     * @param first
     *            first data block
     * @param granularity
     *            expected granularity
     * @param marker
     *            begin or end list markers?
     * @return <code>true</code> if the input has expected granularity,
     *         <code>false</code> otherwise
     * @throws PipeClosedException
     *             if the pipe is closed
     * @throws PipeIOException
     *             if something is wrong with reading/writing from/to the pipe
     * @throws PipeTerminatedException
     *             if the pipe was terminated
     * @throws DataError
     *             if a data error occurs
     */
    private boolean validateGranularity(
            Object first, int granularity, Object marker) 
        throws PipeClosedException, 
               PipeIOException,
               PipeTerminatedException, DataError
    {
        Object block = first;
        // make sure the incoming data has the required granularity
        for (int i = 0; i < granularity - 1; i++)
        {
            if (block != marker)
            {
                return false;
            }
            for (int j = 0; j < mOutputs.length; j++)
            {
                mOutputs[j].write(block);
            }
            block = mDataInput.read();
        }
        if (block != marker)
        {
            return false;
        }
        for (int j = 0; j < mOutputs.length; j++)
        {
            mOutputs[j].write(block);
        }
        return true;
    }

    /**
     * Splits an input sequence in which logical blocks are lists.
     * 
     * @param split
     *            the random split generator
     * @return the last block that was read from the input, either
     *         <code>ControlBlock.LIST_END</code> or
     *         <code>ControlBlock.NO_MORE_DATA</code>
     * @throws DataError
     *             if a data error occurs
     * @throws PipeIOException
     *             if something is wrong with reading/writing from/to the pipe
     * @throws PipeTerminatedException
     *             if the pipe was terminated
     * @throws PipeClosedException
     *             if the pipe is closed
     * @throws MalformedListBeginException
     *             If the list has malformed list begin marker
     * @throws MalformedListEndException
     *             If the list has malformed list end marker
     */
    private Object splitLists(Split split) 
        throws DataError, 
               PipeIOException,
               PipeTerminatedException, 
               PipeClosedException,
               MalformedListBeginException, 
               MalformedListEndException
    {
        // write the complete list to this output
        int output = split.next();

        // start from 1 since the first block has been read already
        int listDepth = 1;

        // write the first block that has been read before
        mOutputs[output].write(ControlBlock.LIST_BEGIN);

        // now iterate through the complete list
        Object block;
        while ((block = mDataInput.read()) != ControlBlock.NO_MORE_DATA)
        {
            if (block == ControlBlock.LIST_BEGIN)
            {
                listDepth++;
            }
            else if (block == ControlBlock.LIST_END)
            {
                if (listDepth > 0)
                {
                    listDepth--;
                }
                else
                {
                    // we have reached the end of the nested list
                    return block;
                }
            }
            mOutputs[output].write(block);
        }

        // we have reached the end of the list
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
            return block;
        }
    }

    /**
     * Splits a sequence of blocks.
     * 
     * @param first
     *            first block
     * @param split
     *            random split generator
     * @return the last block that was read from the input, either
     *         <code>ControlBlock.LIST_END</code> or
     *         <code>ControlBlock.NO_MORE_DATA</code>
     * @throws DataError
     *             if a data error occurs
     * @throws PipeIOException
     *             if something is wrong with reading/writing from/to the pipe
     * @throws PipeTerminatedException
     *             if the pipe was terminated
     * @throws PipeClosedException
     *             if the pipe is closed
     */
    private Object split(Object first, Split split) 
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
            }
        }
        else
        {
            mOutputs[split.next()].write(first);
        }

        Object block;
        while ((block = mDataInput.read()) != ControlBlock.NO_MORE_DATA)
        {
            if (block == ControlBlock.LIST_END)
            {
                return block;
            }
            mOutputs[split.next()].write(block);
        }
        iterativeStageComplete();
        return block;
    }
}
