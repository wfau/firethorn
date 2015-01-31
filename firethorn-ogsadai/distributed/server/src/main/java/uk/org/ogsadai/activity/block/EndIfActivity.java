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
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;

/**
 * TODO: this implementation only works for a single logical value.  If we
 * have multiple logical values coming in on the true and false input lines
 * then we would need have a control input that provided true/false that came
 * from the IfX activity.  This may be how we wish to implement this type of
 * thing in the future.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class EndIfActivity extends ActivityBase
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2010";
    
    public static final String TRUE_INPUT = "trueInput";
    public static final String FALSE_INPUT = "falseInput";
    public static final String OUTPUT = "output";

    public void process() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(OUTPUT);
        validateInput(TRUE_INPUT);
        validateInput(FALSE_INPUT);
        
        BlockReader trueInput = getInput(TRUE_INPUT);
        BlockReader falseInput = getInput(FALSE_INPUT);
        BlockWriter output = getOutput(OUTPUT);
        
        // Need two theads reading the inputs but only one will write to the
        // output.  Start up one new thread and also use the current one.
        
        EndIfEchoRunnable trueInputRunnable = 
            new EndIfEchoRunnable(trueInput, output);
        EndIfEchoRunnable falseInputRunnable  = 
            new EndIfEchoRunnable(falseInput, output);

        Thread thread = new Thread(trueInputRunnable);
        thread.start();
        falseInputRunnable.run();
        
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            // Ignore
        }
        
        trueInputRunnable.throwAnyExceptions();
        falseInputRunnable.throwAnyExceptions();
    }
    
    private class EndIfEchoRunnable implements Runnable
    {
        private BlockReader mInput;
        private BlockWriter mOutput;
        private ActivityProcessingException mActivityProcessingException = null;
        private ActivityTerminatedException mActivityTerminatedException = null;
        private DataError mDataError = null;
        
        public EndIfEchoRunnable(BlockReader input, BlockWriter output)
        {
            mInput = input;
            mOutput = output;
        }
        
        public void run()
        {
            Object block;
            
            try
            {
                while ((block = mInput.read()) != ControlBlock.NO_MORE_DATA)
                {
                    mOutput.write(block);
                }
            }
            catch (PipeClosedException e)
            {
               // Do nothing
            } 
            catch (PipeIOException e)
            {
                mActivityProcessingException = 
                    new ActivityProcessingException(e);
            }
            catch (PipeTerminatedException e)
            {
                mActivityTerminatedException = 
                    new ActivityTerminatedException();
            }
            catch (DataError e)
            {
                mDataError = e;
            }
        }
        
        public void throwAnyExceptions() 
            throws DataError, ActivityProcessingException, ActivityTerminatedException
        {
            if (mDataError != null) throw mDataError;
            if (mActivityProcessingException != null) throw mActivityProcessingException;
            if (mActivityTerminatedException != null) throw mActivityTerminatedException;
        }
    }    
}
