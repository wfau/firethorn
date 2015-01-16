// Copyright (c) The University of Edinburgh, 2007-2011.
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
 * An activity that creates a DQP data resource based upon a resource template
 * available on the server.
 * <p>
 * Activity inputs:
 * <ul>
 * <li>
 * <code>resourceId</code>. Type: {@link java.lang.String}. The ID for the new
 * resource. This is an optional input. If omitted then the server will generate
 * a new unique ID. If provided then the value must be parsable into a valid
 * {@link uk.org.ogsadai.resource.ResourceID} and must not already be assigned
 * to a server-side resource.</li>
 * <li>
 * <code>templateId</code>. Type: {@link java.lang.String}. The ID for the
 * server-side resource template to use as a basis for the new resource. This is
 * an optional input. If omitted then the server will use the default template
 * ID specified as part of the activity configuration. If provided then the
 * value must be parsable into a valid
 * {@link uk.org.ogsadai.resource.ResourceID}.</li>
 * <li>
 * <code>configuration</code>. Type: OGSA-DAI list of <code>char[]</code>. The
 * configuration for the new DQP resource. The value must be well-formed
 * <code>DQPResourceConfig</code> XML document.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * <ul>
 * <li>
 * <code>result</code>. Type: {@link java.lang.String}. ID of the new resource.
 * This will be parsable into a {@link uk.org.ogsadai.resource.ResourceID}.</li>
 * </ul>
 * <p>
 * Configuration parameters:
 * <ul>
 * <li>
 * <code>dai.template.id</code> This must hold the ID of a resource template
 * deployed server-side. Furthermore this resource template must specify the
 * data resource implementation class
 * <code>uk.org.ogsadai.resource.dataresource.dqp.DQPResource</code>.</li>
 * <li>
 * <code>dqp.config.dir</code> This must hold the path of an existing directory
 * into which client-specified DQP configurations are placed. The directory must
 * be writable by the server.</li>
 * <li>
 * <code>dqp.context.template</code> This must hold the path to the context
 * template, relative to the configuration directory (see above). The file must
 * be readable by the server.</li>
 * <li>
 * <code>dqp.evaluation.node.factory</code> This optional parameter can be
 * used to specify the implementation class of the 
 * <code>uk.org.ogsadai.dqp.presentation.common.EvaluationNodeFactory</code>
 * that creates evaluation node objects when initialising the DQP resource. 
 * If it is not provided then the default evaluation node factory is used. 
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * <p>
 * Activity contracts: none.
 * <p>
 * Target data resource: none.
 * <p>
 * Behaviour:
 * <ul>
 * <li>
 * If a resource ID hasn't been provided then the activity auto-generates a
 * unique one.</li>
 * <li>
 * If a template ID hasn't been provided then the activity gets the default one
 * specified in its configuration.</li>
 * <li>
 * It attempts to load the given resource template. If the template cannot be
 * found and the ID was provided by the client then this error is returned to
 * the client. If the default template ID is being used or any other loading
 * problems arise then a server-side error is logged.</li>
 * <li>
 * If a configuration is provided then it is written to a file and parsed into a
 * DQP configuration.</li>
 * <li>
 * It then creates a resource using this template and checks that the resulting
 * resource implements <code>DQPResource</code>. If there is a creation problem
 * or the check fails then a server-side error is logged.</li>
 * <li>
 * It adds the resource to the resource manager.</li>
 * <li>
 * The ID of the new resource is then output.</li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CreateOriginalDQPActivity 
    extends MatchedIterativeActivity
    implements ResourceManagerActivity, 
               ResourceFactoryActivity,
               ConfigurableActivity
{
    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2011.";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(CreateOriginalDQPActivity.class);
    
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
            new TypedOptionalActivityInput(INPUT_ID, String.class),
            // Input 1
            new TypedOptionalActivityInput(INPUT_TEMPLATE_ID, String.class),
            // Input 2
            new ReaderActivityInput(INPUT_CONFIG)
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
