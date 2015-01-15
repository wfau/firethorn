package uk.org.ogsadai.activity.astro;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.config.ConfigurationValueMissingException;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.resource.ResourceAccessor;

public class ExtractPhysicalSchemaToXMLFromFileActivity
    extends MatchedIterativeActivity
    implements ResourceActivity
{
    /**
     * Activity configuration key for physical metadata file to read.
     */
    public static final Key METADATA_FILE = new Key("dai.physicalmetadatafile");

    /** 
     * Activity input name <code>name</code> - table name pattern
     * ({@link java.lang.String}). 
     */
    public static final String NAME_INPUT = "name";
    
    /** 
     * Activity output name <code>result</code> - schema
     * (<code>char[]</code>). 
     */
    public static final String RESULT_OUTPUT = "result";

    private static final int BUFFER_SIZE = 2000;
    
    /** Resource settings. */
    private KeyValueProperties mSettings;
    
    /** File name of phyisical metadata file. */
    private String mPhysicalMetadataFileName;
    
    /** Activity output - schema. */
    private BlockWriter mOutput; 


    @Override
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor)
    {
        mSettings = 
            resourceAccessor.getResource().getState().getConfiguration();
    }

    @Override
    public Class getTargetResourceAccessorClass()
    {
        return ResourceAccessor.class;
    }

    @Override
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] { 
            new TypedActivityInput(NAME_INPUT, String.class)
        };
    }

    @Override
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        validateOutput(RESULT_OUTPUT);
        mOutput = getOutput();     

        if (!mSettings.containsKey(METADATA_FILE))
        {
            throw new 
            ActivityProcessingException(
                new ConfigurationValueMissingException(METADATA_FILE));
        }
        else
        {
            mPhysicalMetadataFileName = (String) mSettings.get(METADATA_FILE);
        }
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
    {
        // Get a reader to read from the file
        try
        {
            Reader reader = 
                new BufferedReader( new FileReader(mPhysicalMetadataFileName));

            mOutput.write(ControlBlock.LIST_BEGIN);
    
            int n;
            char[] buffer = new char[BUFFER_SIZE];
            while ((n = reader.read(buffer)) != -1)
            {
                if (n > 0)
                {
                    // Have some data to write out
                    if (n==BUFFER_SIZE)
                    {
                        mOutput.write(buffer);
                        buffer = new char[BUFFER_SIZE];
                    }
                    else
                    {
                        mOutput.write(Arrays.copyOf(buffer, n));
                    }
                }
            }
            
            mOutput.write(ControlBlock.LIST_END);
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
            // No more output wanted, so finish early.
            iterativeStageComplete();
        } 
        catch (FileNotFoundException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (IOException e)
        {
            throw new ActivityProcessingException(e);
        }
    }

    @Override
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        // No post processing
    }
}
