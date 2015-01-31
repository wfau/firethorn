// Copyright (c) The University of Edinburgh,  2007-2008.
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
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.MalformedListEndException;
import uk.org.ogsadai.exception.MalformedListBeginException;

/**
 * This activity aims at repeating the value (logical block) of one input
 * for every available value (logical block) of the other input.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>repeatedInput</code>. Type: This can be any type of blocks. The
 * input whose value will be repeated. This is a mandatory input. Note that by
 * value it is denoted a logical block, i.e. a usual java Object or an OGSA-DAI
 * list of Objects or an OGSA-DAI list of lists of Objects and so on. 
 * </li>
 * <li> <code>input</code>. Type: This can be any type of blocks. The input
 * that for every available value that it has, the value of <code>repeatedInput
 * </code>
 * will be repeated.This is a mandatory input. Note that by value it is denoted
 * a logical block, i.e. a usual java Object or an OGSA-DAI list of Objects or
 * an OGSA-DAI list of lists of Objects and so on. This input may also be referred
 * as controlled stream input since it "controls" the streaming pace.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>repeatedOutput</code>. Type: This can be any type of blocks.
 * The output containing the value of the <code>repeatedInput</code> input
 * repeated as many times as the <code>input</code> input dictates.Note that
 * by value it is denoted a logical block, i.e. a usual java Object or an
 * OGSA-DAI list of Objects or an OGSA-DAI list of lists of Objects and so on.
 * </li>
 * <li> <code>output</code>. Type: This can be any type of blocks. The output
 * containing the values exactly as read from the <code>input</code> input.
 * These values can then be consumed together with the newly produced repeated
 * values to the inputs of one or more activities.Note that by value it is
 * denoted a logical block, i.e. a usual java Object or an OGSA-DAI list of
 * Objects or an OGSA-DAI list of lists of Objects and so on. </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>One logical block is read from <code>repeatedInput</code> input before reading from the 
 * <code>input</code> input.</li>
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
 * <li> The behaviour of this activity can be best understood through the
 * following two examples (in all the examples we use '<code>{</code>' to denote
 * the list begin marker and '<code>}</code>' to denote the list end marker).
 * </li>
 * <li> First example:
 * 
 * <pre>
 * repeatedInput   : { A B }
 * input           : x y
 * repeatedOutput  : { A B } { A B } 
 * output          : x y
 * </pre>
 * 
 * </li>
 * <li> Second example:
 * 
 * <pre>
 * repeatedInput   : { A B }
 * input           : { C D } { C D }
 * repeatedOutput  : { A B } { A B }
 * output          : { C D } { C D }
 * </pre>
 * </li>
 * <li> If malformed lists are provided, corresponding errors will be thrown.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team
 */
public class ControlledRepeatActivity extends IterativeActivity
{
    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2008.";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(ControlledRepeatActivity.class);
    
    /** Activity input name(<code>repeatedInput</code>) - The logical block 
     * which will be repeated (can be of any type). */
    public static final String INPUT_REPEAT = "repeatedInput";

    /** Activity input name(<code>input</code>) - This is simply outputted 
     * unchanged and can be of any type. */
    public static final String INPUT_CONTROL_STREAM = "input";

    /**
     * Activity output name (<code>repeatedOutput</code>)
     * - The logical block of <code>repeatedInput</code> repeated as many 
     * times as dictated by <code>input</code>.
     */
    public static final String OUTPUT_REPEAT = "repeatedOutput";

    /**
     * Activity output name (<code>output</code>)
     * - The logical blocks as read from <code>input</code> input.
     */
    public static final String OUTPUT_CONTROL_STREAM = "output";
    
    /** Input block reader for repeated input. */
    private BlockReader mRepeatedInput;
    
    /** Input block reader for control input. */
    private BlockReader mControlInput;
    
    /** Output block writer for repeated output. */
    private BlockWriter mRepeatedOutput;
    
    /** Output block writer for control output. */
    private BlockWriter mControlOutput;
    
    /** The list of repeated blocks. */
    private List mRepeatedList;
    
    /** The granularity depth of the control input. */
    private int mControlListDepth;
    
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
        mRepeatedOutput = getOutput(OUTPUT_REPEAT);
        mControlOutput = getOutput(OUTPUT_CONTROL_STREAM);        
        mRepeatedList = storeRepeatedInput();
        mControlListDepth = 0;
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
                    if (mControlListDepth == 0)
                    {
                        //repeat the list
                        for (int i=0; i< mRepeatedList.size(); i++)
                        {
                            mRepeatedOutput.write(mRepeatedList.get(i));
                        }
                    }
                    mControlListDepth++;
                }
                else if (block == ControlBlock.LIST_END)
                {
                    mControlListDepth--;               
                }
                // granularity -> 0
                else if (mControlListDepth == 0)
                {
                    //repeat the list
                    for (int i=0; i< mRepeatedList.size(); i++)
                    {
                        mRepeatedOutput.write(mRepeatedList.get(i));
                    }                  
                }
                //list with more end markers
                if (mControlListDepth < 0)
                {
                    throw new MalformedListEndException(INPUT_CONTROL_STREAM);
                }
                mControlOutput.write(block);
            }
            else
            {
                //list with more start markers
                if (mControlListDepth > 0)
                {
                    throw new MalformedListBeginException(INPUT_CONTROL_STREAM);
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
            //No more input wanted.
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
     * Auxiliary method that stores the blocks of the input to be repeated
     * into a list.
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
            while ((block = mRepeatedInput.read()) != ControlBlock.NO_MORE_DATA)
            {
                if (block == ControlBlock.LIST_BEGIN)
                {
                    listDepth++;
                }
                else if (block == ControlBlock.LIST_END)
                {
                    listDepth--;                    
                }
                output.add(block);
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
        catch (DataError e)
        {
            iterativeStageComplete();
            throw e;
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
}