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

import java.io.IOException;
import java.io.Reader;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.ReaderActivityInput;
import uk.org.ogsadai.activity.io.TypedActivityInput;

/**
 * An activity that reads an OGSA-DAI representation of a character stream and
 * outputs the same character stream represented as an OGSA-DAI list of 
 * character arrays where each array contains the specified number of 
 * characters. The final array in the list may contain less than the specified
 * number of characters.
 * <p>
 * This activity is useful to package up character steams in a sensible size
 * for delivery.  It is especially useful to prevent lots of small characters
 * arrays causing a bloated XML representation if the result is written to
 * the OGSA-DAI request status.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>.  Type: One of
 *      OGSA-DAI list of <code>char[]</code>,
 *      OGSA-DAI list of <code>byte[]</code>,
 *      {@link java.sql.Clob}, or
 *      {@link java.sql.Blob}.
 *   The input character stream.  If the input contains byte rather than 
 *   character data then the bytes are converted to characters using UTF-8
 *   encoding. 
 * </li>
 * <li>
 * <code>sizeInChars</code>.  Type: {@link java.lang.Integer}.  Size of the
 * output character arrays.  All character arrays in the output will be of
 * this size except the list one in a list which will be this size or less.
 * The value must be greater than zero.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>result</code>.  Type: OGSA-DAI list of <code>char[]</code>.  The
 * input character stream as an OGSA-DAI list of <code>char[]</code> of the
 * specified size.  The last array in the list whose size may be less than or
 * equal to the specified size.
 * size/.
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
 * The <code>sizeInChars</code> input is read first, followed by the
 * <code>data</code> input.  New character arrays are written to the output
 * as soon as enough data has been read from the <code>data</code> input
 * to complete the next array.
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
 * Reads the characters from the input and outputs OGSA-DAI lists of character
 * arrays of the specified size that represent the same character stream.
 * </li>
 * <li>
 * If the input is in byte rather than character format (i.e. OGSA-DAI list of
 * <code>byte[]</code> or <code>Blob</code>) then UTF-8 encoding is assumed.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CharArraysResizeActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007.";
    
    /** Name of the data input. */
    public static final String DATA_INPUT = "data";
    
    /** Name of the block size input. */
    public static final String BLOCK_SIZE_INPUT = "sizeInChars";
    
    /** Name of the activity output. */
    public static final String RESULT_OUTPUT = "result";
    
    /** The activity's only output. */
    private BlockWriter mOutput; 

    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedActivityInput(BLOCK_SIZE_INPUT, Integer.class),
            new ReaderActivityInput(DATA_INPUT)
        };
    }

    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        mOutput = getOutput();
    }

    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        int blockSize = ((Integer)iterationData[0]).intValue();
        if (blockSize <= 0) 
        {
            throw new ActivityUserException(
                    new ArraySizeInvalidException(blockSize, BLOCK_SIZE_INPUT));
        }
        Reader input = (Reader)iterationData[1];

        int numChars;
        int offset = 0;
        int length = blockSize;
        char[] block = new char[blockSize];
        try
        {
            mOutput.write(ControlBlock.LIST_BEGIN);
            while ((numChars = input.read(block, offset, length)) >= 0)
            {
                offset += numChars;
                if (offset < blockSize)
                {
                    // keep reading into the same array until it's full
                    length = blockSize - offset;
                }
                else
                {
                    mOutput.write(block);
                    block = new char[blockSize];
                    offset = 0;
                    length = blockSize;
                }
            }
            // the last block may be smaller
            if (offset > 0)
            {
                char[] lastBlock = new char[offset];
                System.arraycopy(block, 0, lastBlock, 0, offset);
                mOutput.write(lastBlock);
            }
            mOutput.write(ControlBlock.LIST_END);
        } 
        catch (PipeClosedException e)
        {
            iterativeStageComplete();
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e); 
        } 
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        catch (IOException e)
        {
            throw new ActivityUserException(e);
        }
    }

    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // nothing to do
    }

}
