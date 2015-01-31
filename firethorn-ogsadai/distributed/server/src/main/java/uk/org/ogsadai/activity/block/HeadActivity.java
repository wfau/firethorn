// Copyright (c) The University of Edinburgh, 2011.
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

import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.UnmatchedActivityInputsException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.InvalidInputValueException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * Takes a data input and returns the at most <code>limit</code> blocks after
 * which the activity indicates to the input that no more data is required.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>input</code>. 
 * Type: OGSA-DAI list of {@link java.lang.Object} possibly containing nested 
 * lists. The data input.
 * </li>
 * <li>
 * <code>limit</code>. 
 * Type: {@link java.lang.Long}.
 * The maximum number of objects to stream through to the output.
 * </li>
 * <li>
 * <code>listDepth</code>. 
 * Type: {@link java.lang.Integer}.
 * The depth of the list at which to operate.  This is an optional input with a
 * default value of 0.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>output</code>. 
 * Type: OGSA-DAI list of {@link java.lang.Object} possibly containing nested 
 * lists.  An exact copy of the data received from the <code>input</code> input,
 * containing up to <code>limit</code> blocks.
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.Head</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>
 * First the value is read from input <code>limit</code>, then an integer is
 * read from the <code>granularity</code> and finally the <code>data</code> 
 * input is read.
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
 * Outputs a copy of the <code>input</code> input up to the number of objects
 * defined by the value of the <code>limit</code> input, and at the depth 
 * specified by the value of the <code>granularity</code> input. Objects can
 * be single blocks, lists, or nested lists, depending on the granularity.
 * </li> 
 * <li> 
 * The default granularity is 0, thus for list input(s) it will operate on
 * entire lists.
 * </li> 
 * <li>
 * For example:
 * <pre>
 * data  : { 1 2 3 } { 4 5 6 } { 7 8 9 } { 0 1 2 3 }
 * limit : 2
 * output: { 1 2 3 } { 4 5 6 }
 * </pre>
 * </li>
 * <li>
 * For example:
 * <pre>
 * data       : { 1 2 3 4 5 6 7 8 9 0 1 2 3 }
 * limit      : 5
 * granularity: 1
 * output     : { 1 2 3 4 5 }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class HeadActivity extends ActivityBase
{

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2011.";

    /** Name of data input. */
    public static final String INPUT_DATA = "data";
    /** Name of limit input. */
    public static final String INPUT_LIMIT = "limit";
    /** Name of list depth input. */
    public static final String INPUT_GRANULARITY = "granularity";
    /** Name of data output. */
    public static final String OUTPUT_RESULT = "result";
    
    private BlockReader mData;
    private BlockReader mLimit;
    private BlockReader mListDepth;
    private BlockWriter mOutput;

    /**
     * Checks that the required inputs and outputs were added and assigns them.
     * 
     * @throws ActivityUserException
     *             if an expected input or output is missing
     */
    protected void preprocess() throws ActivityUserException
    {
        validateInput(INPUT_DATA);
        mData = getInput(INPUT_DATA);
        validateInput(INPUT_LIMIT);
        mLimit = getInput(INPUT_LIMIT);
        mListDepth = getInput(INPUT_GRANULARITY);
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }
    
    @Override
    public void process() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException 
    {
        preprocess();
        try 
        {
            long limit = getLimit();
            int listDepth = getListDepth(limit);
            writeHeadList(limit, listDepth);
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
            // stop processing if no more data is requested by the consumer
        }
    }
    
    private void writeHeadList(long limit, int listDepth) 
        throws DataError, 
               PipeIOException, 
               PipeTerminatedException, 
               PipeClosedException
    {
        long count = 0;
        Object block;
        int currentListDepth = 0;
        while ((block = mData.read()) != ControlBlock.NO_MORE_DATA)
        {
            if (block == ControlBlock.LIST_BEGIN)
            {
                if (currentListDepth == listDepth-1)
                {
                    count = 0;
                }
                else if (currentListDepth == listDepth)
                {
                    count++;
                }
                if (count <= limit)
                {
                    currentListDepth++;
                }
            }
            else if (block == ControlBlock.LIST_END)
            {
                currentListDepth--;
            }
            else if (currentListDepth == listDepth)
            {
                // we don't count metadata blocks
                if (!(block instanceof MetadataWrapper))
                {
                    count++;
                }
            }
            if (count <= limit)
            {
                mOutput.write(block);
            }
            else 
            {
                while (currentListDepth-- > 0)
                {
                    mOutput.write(ControlBlock.LIST_END);
                }
                break;
            }
        }
    }
    
    private long getLimit() 
        throws ActivityUserException, PipeIOException, PipeTerminatedException
    {
        Object block = mLimit.read();
        if (block == ControlBlock.NO_MORE_DATA)
        {
            throw new InvalidInputValueException(INPUT_LIMIT, block);
        }
        if (block instanceof Number)
        {
            return ((Number)block).longValue();
        }
        throw new InvalidInputValueException(
                INPUT_LIMIT, Long.class, block.getClass());
    }

    private int getListDepth(long limit) 
        throws ActivityUserException, PipeIOException, PipeTerminatedException
    {
        if (mListDepth == null)
        {
            return 0;
        }
            
        Object block = mListDepth.read();
        if (block == ControlBlock.NO_MORE_DATA)
        {
            throw new UnmatchedActivityInputsException(
                    new String[] {INPUT_LIMIT, INPUT_GRANULARITY},
                    new Object[] {limit, block});
        }
        if (block instanceof Number)
        {
            int result = ((Number)block).intValue();
            if (result < 0)
            {
                throw new InvalidInputValueException(INPUT_GRANULARITY, result);
            }
            return result;
        }
        else
        {
            throw new InvalidInputValueException(
                    INPUT_LIMIT, Long.class, block.getClass());
        }
    }
}
