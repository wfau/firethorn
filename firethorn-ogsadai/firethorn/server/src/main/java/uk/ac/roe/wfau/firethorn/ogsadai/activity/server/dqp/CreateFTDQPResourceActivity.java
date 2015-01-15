/**
 * Copyright (c) 2015, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.dqp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ConfigurableActivity;
import uk.org.ogsadai.activity.extension.ResourceFactoryActivity;
import uk.org.ogsadai.activity.extension.ResourceManagerActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.ReaderActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.common.files.FileNotFoundException;
import uk.org.ogsadai.common.files.FileUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.exception.DAIClassMissingInterfaceException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceIDAlreadyAssignedException;
import uk.org.ogsadai.resource.ResourceManager;
import uk.org.ogsadai.resource.ResourceTypeException;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.dataresource.DataResource;
import uk.org.ogsadai.resource.dataresource.DataResourceState;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResource;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceState;
import uk.org.ogsadai.tools.DQPEditor;

/**
 * An activity to create a FireThorn DQP resource.
 * 
 */
public class CreateFTDQPResourceActivity
    extends MatchedIterativeActivity
    implements ResourceManagerActivity, 
               ResourceFactoryActivity,
               ConfigurableActivity
    {
    /**
     * Copyright
     *
     */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) 2015, ROE (http://www.roe.ac.uk/)";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(CreateFTDQPResourceActivity.class);
    
    /** 
     * Key into activities configuration file denoting the
     * default template resource ID (<code>dai.template.id</code>).
     */
    public static final Key TEMPLATE_ID = new Key("dai.template.id");
    
    /** 
     * Key into activities configuration file denoting the directory into
     * which new DQP configuration files are placed (<code>dqp.config.dir</code>).
     */
    private static final Key CONFIG_DIR = new Key("dqp.config.dir");

    /** 
     * Key into activities configuration file denoting the path of the context 
     * template. 
     */
    private static final Key CONTEXT_TEMPLATE = new Key("dqp.context.template");
    /** 
     * Key into activities configuration file specifying the EvaluationNode
     * factory class.      
     */
    private static final Key EVALUATION_NODE_FACTORY_CLASS = 
        new Key("dqp.evaluation.node.factory");

    /** Configuration path key. */
    public static final Key CONFIG_PATH = new Key("dqp.config.path");


    /**
     * Activity input name (<code>resourceId</code>)
     * - ID of the new resource to create (<code>String</code>). 
     */
    public static final String INPUT_ID = "resourceId";

    /**
     * Activity input name (<code>templateId</code>)
     * - ID of the resource template (<code>String</code>). 
     */
    public static final String INPUT_TEMPLATE_ID = "templateId";

    /**
     * Activity input name (<code>configuration</code>)
     * - ID of the new resource to create (<code>String</code>). 
     */
    public static final String INPUT_CONFIG = "configuration";

    /**
     * Activity output name (<code>result</code>)
     * - ID of the newly created resource (<code>String</code>). 
     */
    public static final String RESULT = "result";

    /** Result block writer. */
    private BlockWriter mResult;
    
    /** OGSA-DAI resource manager. */
    private ResourceManager mResourceManager;

    /** OGSA-DAI resource factory utility. */
    private ResourceFactory mResourceFactory;

    /** Template resource ID. */
    private ResourceID mTemplateID;

    /** Configuration directory. */
    private String mConfigDir;

    /** Path of context template (relative to <code>mConfigDir</code>. */
    private String mContextTemplate;

    private String mEvaluationNodeFactoryClass;

    /**
     * {@inheritDoc}
     */
    public void setResourceManager(ResourceManager resourceManager)
    {
        mResourceManager = resourceManager;        
    }

    /**
     * {@inheritDoc}
     */
    public void setResourceFactory(ResourceFactory resourceFactory)
    {
        mResourceFactory = resourceFactory;        
    }
    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] 
        {
            // Input 0
            new TypedOptionalActivityInput(INPUT_ID, String.class)
        };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(RESULT);
        mResult = getOutput();
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException
    {
        // Get resource ID.
        ResourceID resourceID;
        if (iterationData[0] == null)
        {
            // Create new.
            resourceID = mResourceManager.createUniqueID();
        }
        else
        {
            // Use one given as input.
            resourceID =  new ResourceID((String)iterationData[0]);
        }
        // Get template resource ID.
        ResourceID templateID;
        if (iterationData[1] == null)
        {
            // Use default.
            templateID = mTemplateID;
        }
        else
        {
            // Use one given as input.
            templateID =  new ResourceID((String)iterationData[1]);
        }
        String config;
        String context;
        if (iterationData[2] != null)
        {
            try
            {
                config = writeConfig((Reader)iterationData[2], resourceID);
                context = writeContext(resourceID, config);
            }
            catch (IOException e)
            {
                throw new ActivityProcessingException(e);
            }
            catch (FileNotFoundException e) 
            {
                throw new ActivityProcessingException(e);
            }
        }
        else
        {
            throw new ActivityUserException(new ErrorID(
                "uk.org.ogsadai.MISSING_DQP_RESOURCE_CONFIG"));
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Resource ID:   " + resourceID);
            LOG.debug("Template ID:   " + templateID);
            LOG.debug("Configuration: " + config);
            LOG.debug("Context:       " + context);
        }
        try
        {
            // Create resource from template.
            DQPResource resource = null;
            try
            {
                resource = createResource(templateID, context);
            } 
            catch (ResourceUnknownException e)
            {
                if (iterationData[1] == null)
                {
                    // Cannot be found.
                    throw e;
                }
                else
                {
                    // Cannot be found but was requested by client.
                    throw new ActivityUserException(e);
                }
            }
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Created resource: " + resource);
            }
            DQPResourceState state = 
                resource.getDQPResourceState();
            state.getState().setResourceID(resourceID);

            // Add the resource to the OGSA-DAI resource manager
            // via the resource factory utility.
            mResourceFactory.addResource(resourceID, resource);
            // Output the resource ID.
            mResult.write(resource.getResourceID().toString());
        }
        catch (PipeClosedException e)
        {
            // No more input wanted.
            iterativeStageComplete();
        }
        catch (PipeTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        catch (PipeIOException e)
        {
            throw new ActivityPipeProcessingException(e);
        }
        catch (ResourceIDAlreadyAssignedException e)
        {
            throw new ActivityUserException(e);
        }
        catch (ActivityUserException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ActivityProcessingException(e);
        }
    }
    
    private File getResourceDir(ResourceID resourceID) throws IOException
    {
        // Create new resource directory.
        File resourceDir = new File(getConfigDir(), resourceID.toString());
        if (! resourceDir.exists())
        {
            resourceDir.mkdir();
        }
        return resourceDir;
    }
    
    private String getConfigDir() throws IOException
    {
        String configDir;
        if((new File(mConfigDir)).isAbsolute())
        {
            configDir = mConfigDir;
        }
        else
        {
            configDir = (new File((File) OGSADAIContext.getInstance().get(
                OGSADAIConstants.CONFIG_DIR), mConfigDir)).getCanonicalPath();
        }
        return configDir;
    }

    private String writeConfig(Reader config, ResourceID resourceID)
        throws IOException
    {
        File resourceDir = getResourceDir(resourceID);
        // Set up file name for resource configuration file only.
        String configFileName = 
            mConfigDir + "/" + resourceID.toString() + "/DQPResourceConfig.xml";
        // Set up file name for writing configuration.
        File configFile = new File(resourceDir, "DQPResourceConfig.xml");
        String file = configFile.getCanonicalPath();
        FileWriter writer = new FileWriter(file);
        BufferedReader configReader = new BufferedReader(config);
        String s = null;
        while ((s = configReader.readLine()) != null)
        {
            writer.write(s);
            writer.write("\n");
        }
        writer.flush();
        writer.close();
        configReader.close();
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Wrote configuration to " + file);
        }
        return configFileName;
    }
    
    private String writeContext(ResourceID resourceID, String configFileName)
        throws IOException, FileNotFoundException
    {
        // copy the context template to the resource config directory
        File resourceDir = getResourceDir(resourceID);
        File contextFile = new File(resourceDir, "DQPContext.xml");
        File contextTemplate = new File(getConfigDir(), mContextTemplate);
        FileUtilities.copyFile(contextTemplate, contextFile);
        
        // insert the file name of the DQP federation into the config
        File ogsadaiConfigDir = 
            (File)OGSADAIContext.getInstance().get(OGSADAIConstants.CONFIG_DIR);
        DQPEditor editor = new DQPEditor();
        editor.initialize(ogsadaiConfigDir);
        editor.setFederationFile(
                resourceID.toString(),
                mEvaluationNodeFactoryClass,
                configFileName);
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Wrote context to " + contextFile);
        }
        
        return mConfigDir + "/" + resourceID.toString() + "/DQPContext.xml";
    }

    /**
     * {@inheritDoc}
     *
     * No post-processing.
     */
    protected void postprocess() 
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        // No post-processing.
    }

    /**
     * Create a new resource from the specified template.
     * 
     * @param templateResourceID
     *            Resource template ID.
     * @param config
     *            DQP resource configuration file
     * @return Newly created resource.
     * @throws ResourceTypeException
     *             if the template type doesn't match the expected resource typ
     * @throws ResourceUnknownException
     *             if the template resource type is unknown
     * @throws DAIClassMissingInterfaceException
     *             if the template doesn't create DQP resources
     */
    protected DQPResource createResource(ResourceID templateResourceID, String context) 
        throws ResourceUnknownException, 
               ResourceTypeException, 
               DAIClassMissingInterfaceException
    {
        DataResourceState state =
            mResourceFactory.createDataResourceState(templateResourceID);
        state.getConfiguration().put(CONFIG_PATH, context);
        String resourceClassName = state.getDataResourceClass();
        DataResource resource = 
            mResourceFactory.createDataResource(resourceClassName);
        resource.initialize(state);
        if (! (resource instanceof DQPResource))
        {
            // Server deployer has specified a template that does not
            // create DQP resources.
            throw new DAIClassMissingInterfaceException(
                    resource.getClass().getName(), DQPResource.class.getName());
        }
        return (DQPResource)resource;
    }
    
    /** 
     * {@inheritDoc}
     * 
     * Extracts value with key <code>dai.template.id</code> and attempts
     * to parse into a <code>uk.org.ogsadai.resource.ResourceID</code>.
     */
    public void configureActivity(KeyValueProperties properties)
    {
        mTemplateID = new ResourceID((String)properties.get(TEMPLATE_ID));
        mConfigDir = (String)properties.get(CONFIG_DIR);
        mContextTemplate = (String)properties.get(CONTEXT_TEMPLATE);
        if (properties.containsKey(EVALUATION_NODE_FACTORY_CLASS))
        {
            mEvaluationNodeFactoryClass = 
                (String)properties.get(EVALUATION_NODE_FACTORY_CLASS);
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Template resource ID: " + mTemplateID);
            LOG.debug("Configuration directory: " + mConfigDir);
            LOG.debug("Context template: " + mContextTemplate);
            LOG.debug("EvaluationNode factory class: " + mEvaluationNodeFactoryClass);
        }
    }
    
}
