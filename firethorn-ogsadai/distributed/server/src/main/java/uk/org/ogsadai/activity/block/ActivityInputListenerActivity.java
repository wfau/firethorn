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

import java.util.ArrayList;

import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.InvalidActivityInputsException;
import uk.org.ogsadai.activity.InvalidActivityOutputsException;
import uk.org.ogsadai.activity.event.ActivityInputListener;
import uk.org.ogsadai.activity.event.InputDetails;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.common.msgs.DAILogger;

/**
 * Activity used to sit in front of an activity input and make calls to
 * activity input listeners.  Instances of these activities are inserted into
 * the workflow at server by the {@link 
 * uk.org.ogsadai.activity.event.ActivityInputListenerWorkflowTransformation}
 * class.
 * <p>
 * The activity has three inputs:
 * <ul>
 *   <li>
 *     <tt>inputDetails</tt> - single object describing the details of the
 *     activity and input that this activity is monitoring.
 *   </li>
 *   <li>
 *     <tt>listeners</tt> - a stream of 
 *     {@link uk.org.ogsadai.activity.event.ActivityInputListener} objects
 *     that are the listeners to be be informed when a block is read.
 *   </li>
 *   <li>
 *     <tt>input</tt> - the input stream being observed.  For each block
 *     read an event will be send each of the listeners.
 *   </li>
 * </ul>
 * The activity has one output:
 * <ul>
 *   <li>
 *     <tt>output</tt> - an echo of the data read from the <tt>input</tt>
 *     input.
 *   </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ActivityInputListenerActivity extends ActivityBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008.";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ActivityInputListenerActivity.class);
    
    /** Name of the input input. */
    public static final String INPUT_INPUT_NAME = "input";
    /** Name of the input details input. */
    public static final String INPUT_DETAILS_INPUT_NAME = "inputDetails";
    /** Name of the listeners input. */
    public static final String LISTENERS_INPUT_NAME = "listeners";
    /** Name of the output output. */
    public static final String OUTPUT_OUTPUT_NAME = "output";

    /**
     * {@inheritDoc}
     */
    public void process() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        BlockReader inputReader = 
            getAndValidateInput(INPUT_INPUT_NAME);
        BlockReader inputDetailsReader = 
            getAndValidateInput(INPUT_DETAILS_INPUT_NAME);
        BlockReader listenersReader = 
            getAndValidateInput(LISTENERS_INPUT_NAME);
        BlockWriter outputWriter = 
            getAndValidateOutput(OUTPUT_OUTPUT_NAME);
        
        InputDetails inputDetails = null;
        ActivityInputListener[] listeners = null;
        
        try
        {
            inputDetails = getInputDetails(inputDetailsReader);
            listeners = getListeners(listenersReader);
        }
        catch (PipeIOException e)
        {
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }

        try
        {
            Object block;
            
            while ((block = inputReader.read()) != ControlBlock.NO_MORE_DATA)
            {
                for (int i=0; i<listeners.length; ++i)
                {
                    listeners[i].blockReadEvent(inputDetails, block);
                }
                outputWriter.write(block);
            }
            for (int i=0; i<listeners.length; ++i)
            {
                listeners[i].inputCompletedEvent(inputDetails);
            }
            outputWriter.closeForWriting();
        }
        catch (PipeIOException e)
        {
            for (int i=0; i<listeners.length; ++i)
            {
                listeners[i].inputErrorEvent(inputDetails);
            }
            throw new ActivityPipeProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            for (int i=0; i<listeners.length; ++i)
            {
                listeners[i].inputTerminatedEvent(inputDetails);
            }
            throw new ActivityTerminatedException();
        }
        catch (PipeClosedException e)
        {
            for (int i=0; i<listeners.length; ++i)
            {
                listeners[i].inputClosedByConsumerEvent(inputDetails);
            }
            // consumer wants no more data, so just ignore
        }
        catch (DataError e)
        {
            for (int i=0; i<listeners.length; ++i)
            {
                listeners[i].inputErrorEvent(inputDetails);
            }
            throw e;
        }
    }
    
    /**
     * Gets details of the input that this activity is monitoring.
     * <p>
     * This information is obtained by reading the <tt>inputDetails</tt> input.
     * 
     * @param inputDetailsReader reader to use to read the <tt>inputDetails</tt>
     *        input.

     * @return details of the input this activity is monitoring.
     * 
     * @throws DataError
     *    if a data error occurs while reading the data.
     * @throws PipeIOException
     *    if a Pipe IO error occurs while reading from the pipe.
     * @throws PipeTerminatedException
     *    if a pipe termination error occurs.
     */
    private InputDetails getInputDetails(BlockReader inputDetailsReader) 
        throws DataError, PipeIOException, PipeTerminatedException
    {
        InputDetails inputDetails = (InputDetails) inputDetailsReader.read();
        inputDetailsReader.closeForReading();
        return inputDetails;
    }

    /**
     * Gets the array of activity input listeners to be notified of the data
     * values received on the <tt>input</tt> input. 
     * <p>
     * This information is obtained by reading the <tt>listeners</tt> input.
     * 
     * @param listenersReader reader to use to read the <tt>listeners</tt>
     *        input.
     * 
     * @return array of activity input listeners.
     * 
     * @throws DataError
     *    if a data error occurs while reading the data.
     * @throws PipeIOException
     *    if a Pipe IO error occurs while reading from the pipe.
     * @throws PipeTerminatedException
     *    if a pipe termination error occurs.
     */
    private ActivityInputListener[] getListeners(BlockReader listenersReader) 
        throws DataError, PipeIOException, PipeTerminatedException
    {
        ArrayList listeners = new ArrayList();
        
        Object block;
        while ((block = listenersReader.read()) != ControlBlock.NO_MORE_DATA)
        {
            if (block instanceof ActivityInputListener)
            {
                listeners.add(block);
            }
        }
        listenersReader.closeForReading();
        return (ActivityInputListener[]) listeners.toArray(
            new ActivityInputListener[]{});
    }
    
    /**
     * Gets the reader for the named input.
     * 
     * @param inputName  name of input.
     * 
     * @return reader for the input.
     * 
     * @throws InvalidActivityInputsException
     *     if the input is not specified in the workflow.
     */
    private BlockReader getAndValidateInput(final String inputName) 
        throws InvalidActivityInputsException
    {
        BlockReader input = getInput(inputName);
        if (input == null)
        {
            throw new InvalidActivityInputsException(1, inputName);
        }
        return input;
    }


    /**
     * Gets the writer for the named output.
     * 
     * @param outputName  name of output.
     * 
     * @return writer for the output.
     * 
     * @throws InvalidActivityOutputsException
     *     if the output is not specified in the workflow.
     */
    private BlockWriter getAndValidateOutput(final String outputName) 
        throws InvalidActivityOutputsException
    {
        BlockWriter output = getOutput(outputName);
        if (output == null)
        {
            throw new InvalidActivityOutputsException(1, outputName);
        }
        return output;
    }
}
