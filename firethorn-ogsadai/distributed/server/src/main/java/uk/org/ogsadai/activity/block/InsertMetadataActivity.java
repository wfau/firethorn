// Copyright (c) The University of Edinburgh, 2010.
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
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * TODO: document.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class InsertMetadataActivity extends MatchedIterativeActivity
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    /** Name of names input. */
    public static final String INPUT_DATA = "data";
    /** Name of metadata input. */
    public static final String INPUT_METADATA = "metadata";
    /** Name of result output. */
    public static final String OUTPUT = "output";

    /** Result output block writer. */
    private BlockWriter mResultOutput;

    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] {
            new TypedListActivityInput(INPUT_DATA, Object.class),
            new TypedActivityInput(INPUT_METADATA, MetadataWrapper.class)};
    }

    @Override
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        // No post-processing
    }

    @Override
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT);
        mResultOutput = getOutput(OUTPUT);
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        ListIterator dataInput = (ListIterator) iterationData[0];
        MetadataWrapper metadata = (MetadataWrapper) iterationData[1];
        
        try
        {
            Object block;
            mResultOutput.write(ControlBlock.LIST_BEGIN);
            mResultOutput.write(metadata);
            
            while ( (block = dataInput.nextValue()) != null)
            {
                if (!(block instanceof MetadataWrapper))
                {
                    mResultOutput.write(block);
                }
            }
            mResultOutput.write(ControlBlock.LIST_END);
        }
        catch (PipeClosedException e)
        {
           // Do nothing
        } 
        catch (PipeIOException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
    }
}
