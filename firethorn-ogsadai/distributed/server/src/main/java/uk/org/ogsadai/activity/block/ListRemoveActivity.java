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

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
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
import uk.org.ogsadai.exception.MalformedListBeginException;
import uk.org.ogsadai.exception.MalformedListEndException;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * Reduces the granularity of its input by one. The granularity of the output 
 * is the same as the input's minus one. The granularity of the input cannot 
 * be less than one.
 * 
 * <p>
 * Activity Inputs:
 * </p>
 * <ul>
 * <li>
 * <code>input</code>. Type: OGSA-DAI list of {@link java.lang.Object} possibly
 * containing nested lists.
 * </li>
 * <li>
 * <code>level</code>.  Type: {@link java.lang.Number}. Specifies 
 * the integer level of the list to remove. Optional input that defaults to 1 
 * if not specified.
 * </li>
 * </ul>
 * 
 * <p>
 * Activity Outputs:
 * </li>
 * <ul>
 * <li>
 * <code>output</code>. Type: {@link java.lang.Object} or an OGSA-DAI list of
 * {@link java.lang.Object} possibly containing nested lists. The input data
 * stream reduced in granularity by one.
 * </li>
 * </ul>
 * 
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * 
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>The activity reduces the granularity of the input 
 * by one, i.e. it removes the outermost list from an input value.</li>
 * <li>If the input level is specified, then the list at that level is removed.
 * An example of removing the level two list would be (on all the examples we 
 * use '<code>{</code>' to denote the list begin marker and '<code>}</code>' 
 * to denote the list end marker)<br>
 * &nbsp;&nbsp;<code>{ { 1 2 3 } { 4 5 6 } }</code><br>
 * is transformed to<br>
 * &nbsp;&nbsp;<code>{ 1 2 3 4 5 6 }</code><br>
 * This 
 * removal of levels will work to any point where a list level exists. </li>
 * <li>Metadata will be written once for each collapsed list, when the 
 * surrounding lists go two below the removal level before the level is 
 * encountered again then a new metadata object will be written if encountered 
 * again at the removal level. For example, remove at level 3 of<br>
 * &nbsp;&nbsp;<code>{ { { MD 1 2 } {MD 3 } } { { MD 4 } { MD 5 6 } } }</code><br>
 * will output:<br> 
 * &nbsp;&nbsp;<code>{ { MD 1 2 3 } { MD 4 5 6 } }</code><br>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListRemoveActivity extends IterativeActivity
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh,  2002 - 2008";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG =
        DAILogger.getLogger(ListRemoveActivity.class);

    /** Name of input. */
    public static final String INPUT_NAME = "input";
    
    /** input name - level. */
    public static final String LEVEL_INPUT = "level";
    
    /** Name of outputs. */
    public static final String OUTPUT_NAME = "output";
    
    /** Output writer. */
    private BlockWriter mOutput;
    
    /** Input reader. */
    private BlockReader mInput;

    /** Input block reader for control input. */
    private BlockReader mLevelInput;
    
    /** default input level. */
    private static final int DEFAULT_LEVEL = 1;
    
    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        validateInput(INPUT_NAME);
        validateOutput(OUTPUT_NAME);
        
        mInput = getInput(INPUT_NAME);
        mLevelInput = getInput(LEVEL_INPUT);
        mOutput = getOutput(OUTPUT_NAME);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void processIteration() throws ActivityProcessingException,
    ActivityTerminatedException, ActivityUserException
    {
        try
        {
            int listDepth=0; 
            int prevListDepth = 0;
            int removeLevel=getLevel();
            boolean metadata = false;
            Object block = null;   
            while ((block = mInput.read()) != ControlBlock.NO_MORE_DATA)
            {
                //should have granularity of at least one
                if (listDepth == 0 && block != ControlBlock.LIST_BEGIN)
                {
                    throw new InvalidInputGranularityException(INPUT_NAME, 1);
                }  
                
                
                if (block == ControlBlock.LIST_BEGIN)
                {
                                   
                    listDepth++;
                    
                    if (listDepth == removeLevel) 
                    {
                        continue;
                    }    
         
                }
                else if (block == ControlBlock.LIST_END)
                {      
                    prevListDepth=listDepth;
                    listDepth--;
                    if(prevListDepth==removeLevel)
                    {
                        continue;
                    }
                }
                else if(block instanceof MetadataWrapper)
                {      
                    if (listDepth == removeLevel)
                    {
                        if (metadata)
                        {          
                            continue;
                        } else
                        {
                            metadata = true;
                        }
                    }
                    else if (listDepth < removeLevel)
                    {
                        metadata = false;
                    }
                }
                
                if(listDepth==removeLevel-2)
                {
                    metadata=false;
                }
                else if (listDepth < 0)
                {
                    throw new MalformedListEndException(INPUT_NAME);
                }
                mOutput.write(block);
            }
            if (listDepth > 0)
            {
                throw new MalformedListBeginException(INPUT_NAME);
            }
            iterativeStageComplete();
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
     * Gets the level of list to remove from the LEVEL_INPUT input.
     * 
     * Will return the DEFAULT_LEVEL if no input is given.
     * 
     * @return an integer representing the level to remove
     * @throws InvalidInputValueException 
     *         thrown when invalid value or value type is input
     * @throws DataError
     *         thrown when invalid value class is input 
     * @throws ActivityPipeProcessingException 
     *         thrown on a PipeIO error
     * @throws ActivityTerminatedException
     *         thrown on a PipeTerminitatedException 
     */
    private int getLevel() throws InvalidInputValueException, 
        DataError, ActivityPipeProcessingException, 
        ActivityTerminatedException
    {
        if (mLevelInput != null)
        {
        
            try
            {
                Object block = mLevelInput.read();
                if (block instanceof Number)
                {
                    int temp = ((Number) block).intValue();
                    if (temp > 0)
                    {
                        return temp;
                    } else
                    {
                        throw new InvalidInputValueException(LEVEL_INPUT,
                                new Integer(temp));
                    }
                } else if (block == ControlBlock.NO_MORE_DATA)
                {
                    return DEFAULT_LEVEL;
                } else
                {
                    throw new InvalidInputValueException(LEVEL_INPUT,
                            Number.class, block.getClass());
                }
            } catch (PipeIOException e)
            {
                throw new ActivityPipeProcessingException(e);
            } catch (PipeTerminatedException e)
            {
                throw new ActivityTerminatedException();
            }
        }
        else
        {
            return DEFAULT_LEVEL;
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
    
   
}
