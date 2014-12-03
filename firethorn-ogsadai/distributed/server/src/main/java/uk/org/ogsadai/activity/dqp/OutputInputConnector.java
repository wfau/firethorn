// Copyright (c) The University of Edinburgh 2008-2011.
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


package uk.org.ogsadai.activity.dqp;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;

/**
 * Streams blocks from a block reader to a block writer.
 *
 * @author The OGSA-DAI Project Team.
 */
public class OutputInputConnector implements Callable<Void>
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008-2011.";
    
    /** Input to read from. */
    private BlockReader mInput;
    /** Indicates when the input has been set. */
    private CountDownLatch mInputLock = new CountDownLatch(1);
    /** Output to write to. */
    private BlockWriter mOutput;
    
    /**
     * Constructs a new connector which connects the given input and output.
     * 
     * @param input
     *            data producer
     * @param output
     *            data consumer
     */
    public OutputInputConnector(BlockReader input, BlockWriter output)
    {
        mInput = input;
        mInputLock.countDown();
        mOutput = output;
    }

    /**
     * Constructs a new connector with the given output pipe. The input must be
     * set later. The connector will wait indefinitely until the input is set
     * using <code>setInput</code>.
     * 
     * @param output
     *            data consumer
     */
    public OutputInputConnector(BlockWriter output)
    {
        mOutput = output;
    }
    
    /**
     * Sets the input of this connector.
     * 
     * @param input
     *            data input
     */
    public void setInput(BlockReader input)
    {
        mInput = input;
        mInputLock.countDown();
    }

    /**
     * {@inheritDoc}
     * Reads from an input and writes the blocks out to an output. If the input
     * has not been set at the start of this method the thread will be suspended
     * until the input has been set.
     */
    public Void call() 
        throws PipeClosedException, 
               ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        waitForInput();
        Object block;
        try 
        {
            while ((block = mInput.read()) != ControlBlock.NO_MORE_DATA)
            {
                mOutput.write(block);
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
        return null;
    }

    /**
     * Waits for an input to become available.
     * 
     * @throws ActivityTerminatedException
     *             if the wait is interrupted
     */
    private void waitForInput() throws ActivityTerminatedException
    {
        try
        {
            mInputLock.await();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

}
