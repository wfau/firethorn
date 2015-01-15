package uk.org.ogsadai.activity.astro;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceFactoryActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedListActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.resource.ResourceCreationException;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceTypeException;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.dataresource.DataResource;
import uk.org.ogsadai.resource.dataresource.DataResourceState;

public class CreateGenericResourceActivity 
    extends MatchedIterativeActivity 
    implements ResourceFactoryActivity
{

    private static final ResourceID TEMPLATE_ID = 
        new ResourceID("uk.org.ogsadai.GENERIC_RESOURCE_TEMPLATE");
    
    /** Name of the properties input. */
    private static final String INPUT_PROPERTIES = "properties";

    /** Name of the optional resource ID input. */
    private static final String INPUT_RESOURCE_ID = "resourceId";

    /** Name of the output. */
    private static final String OUTPUT_RESULT = "result";

    /** Output */
    private BlockWriter mOutput;

    /** OGSA-DAI resource factory. */
    private ResourceFactory mResourceFactory;

    @Override
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] {
                new TypedOptionalActivityInput(INPUT_RESOURCE_ID, String.class, null),
                new TypedListActivityInput(INPUT_PROPERTIES, String.class),
        };
    }

    @Override
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_RESULT);
        mOutput = getOutput();
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        DataResource resource = createDataResource((String)iterationData[0]);
        
        // read input properties and add them to the resource configuration
        KeyValueProperties resourceConfig = 
            resource.getState().getConfiguration();
        ListIterator iterator = (ListIterator)iterationData[1];
        String property;
        while ((property = (String)iterator.nextValue()) != null)
        {
            String[] split = property.split("=");
            Key key = new Key(split[0]);
            String value = split[1];
            resourceConfig.put(key, value);
        }
        
        ResourceID id = mResourceFactory.addResource(resource);
        try
        {
            mOutput.write(id);
        } 
        catch (PipeClosedException e) 
        {
            // finish
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
    }

    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException
    {
        // no post-processing
    }

    @Override
    public void setResourceFactory(ResourceFactory resourceFactory) 
    {
        mResourceFactory = resourceFactory;
    }
    
    private DataResource createDataResource(String resourceID)
        throws ActivityUserException
    {
        try 
        {
            DataResource resource;
            if (resourceID == null)
            {
                resource = mResourceFactory.createDataResource(TEMPLATE_ID);
            }
            else
            {
                DataResourceState state = 
                    mResourceFactory.createDataResourceState(TEMPLATE_ID);
                state.setResourceID(new ResourceID(resourceID));
                String resourceClassName = state.getDataResourceClass();
                resource = mResourceFactory.createDataResource(resourceClassName);
                resource.initialize(state);
            }
            return resource;
        }
        catch (ResourceCreationException e)
        {
            throw new ActivityUserException(e);
        }
        catch (ResourceTypeException e) 
        {
            throw new ActivityUserException(e);
        }
        catch (ResourceUnknownException e) 
        {
            throw new ActivityUserException(e);
        }

    }

}
