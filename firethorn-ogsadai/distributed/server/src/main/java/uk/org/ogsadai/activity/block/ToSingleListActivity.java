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

import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.metadata.MetadataWrapper;

/**
 * TODO: document.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ToSingleListActivity extends ActivityBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    public static final String INPUT = "input";
    public static final String OUTPUT = "output";

    public void process() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT);
        validateInput(INPUT);
        BlockReader input = getInput(INPUT);
        BlockWriter output = getOutput(OUTPUT);
        
        boolean isMetadataWritten = false;
        
        try
        {
            output.write(ControlBlock.LIST_BEGIN);
            
            Object block;
            while ((block = input.read()) != ControlBlock.NO_MORE_DATA)
            {
                if (block instanceof MetadataWrapper)
                {
                    if (!isMetadataWritten)
                    {
                        output.write(block);
                        isMetadataWritten = true;
                    }
                }
                else if (! (block instanceof ControlBlock))
                {
                    output.write(block);
                }
            }
            
            output.write(ControlBlock.LIST_END);
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
