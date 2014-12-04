// Copyright (c) The University of Edinburgh, 2009-2010.
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

package uk.org.ogsadai.dqp.presentation.common;

import java.util.Date;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.DataSinkResource;
import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.ResourceProperty;
import uk.org.ogsadai.client.toolkit.SessionOperation;
import uk.org.ogsadai.client.toolkit.TerminationTime;
import uk.org.ogsadai.client.toolkit.Workflow;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ClientServerCompatibilityException;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.PropertyNameInvalidException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.ResourceNotDestroyedException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.client.toolkit.exception.TerminationTimeChangeRejectedException;
import uk.org.ogsadai.client.toolkit.exception.UnableToSetTerminationTimeException;
import uk.org.ogsadai.client.toolkit.resource.ResourceFactory;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourcePropertyName;
import uk.org.ogsadai.resource.ResourceType;
import uk.org.ogsadai.resource.property.ActivitiesMetaData;

/**
 * A data request execution resource proxy that transforms all outgoing requests
 * using a given workflow transformer before they are sent to the server.
 *
 * @author The OGSA-DAI Project Team
 */
public class TransformDataRequestExecutionResource 
    implements DataRequestExecutionResource
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(TransformDataRequestExecutionResource.class);
    
    /** DRER being wrapped. */
    protected DataRequestExecutionResource mDrer;
    
    /** Workflow transformer to apply to all outgoing requests. */
    protected WorkflowTransformer mWorkflowTransformer;
    
    /**
     * Constructor.
     * 
     * @param drer 
     *    Data Request Execution Resource proxy being wrapped.
     * 
     * @param transformer
     *    workflow transformer to apply to all outgoing workflows
     */
    public TransformDataRequestExecutionResource(
        DataRequestExecutionResource drer,
        WorkflowTransformer transformer)
    {
        LOG.debug("In TransformDataRequestExecutionResource constructor");
        mDrer = drer;
        mWorkflowTransformer = transformer;
    }

    /**
     * {@inheritDoc}
     */
    public DataSinkResource createDataSinkResource()
        throws ServerCommsException, ServerException, ResourceUnknownException,
        ClientException, RequestException, ClientToolkitException,
        ClientServerCompatibilityException
    {
        return mDrer.createDataSinkResource();
    }

    /**
     * {@inheritDoc}
     */
    public DataSourceResource createDataSourceResource()
        throws ServerCommsException, ServerException, ResourceUnknownException,
        ClientException, RequestException, ClientToolkitException,
        ClientServerCompatibilityException
    {
        return mDrer.createDataSourceResource();
    }

    /**
     * {@inheritDoc}
     */
    public RequestResource execute(
        Workflow workflow, 
        RequestExecutionType requestExecutionType)
        throws ServerCommsException, 
               ServerException, 
               ResourceUnknownException,
               ClientException, 
               RequestException, 
               ClientToolkitException
    {
        Workflow workflowToExecute = transformWorkflow(workflow);
        return mDrer.execute(workflowToExecute, requestExecutionType);
    }

    /**
     * {@inheritDoc}
     */
    public RequestResource execute(
        ResourceID resourceID,
        Workflow workflow,
        RequestExecutionType requestExecutionType)
        throws ServerCommsException,
               ServerException, 
               ResourceUnknownException, 
               ClientException,
               RequestException, 
               ClientToolkitException
    {
        Workflow workflowToExecute = transformWorkflow(workflow);
        return mDrer.execute(
            resourceID, workflowToExecute, requestExecutionType);
    }

    /**
     * {@inheritDoc}
     */
    public RequestResource execute(
        Workflow workflow, 
        RequestExecutionType requestExecutionType,
        SessionOperation sessionOperation) 
        throws ServerCommsException, 
               ServerException,
               ResourceUnknownException, 
               ClientException, 
               RequestException,
               ClientToolkitException
    {
        Workflow workflowToExecute = transformWorkflow(workflow);
        return mDrer.execute(
            workflowToExecute, requestExecutionType, sessionOperation);
    }

    /**
     * {@inheritDoc}
     */
    public RequestResource execute(
        ResourceID resourceID, 
        Workflow workflow,
        RequestExecutionType requestExecutionType, 
        SessionOperation sessionOperation)
        throws ServerCommsException, 
               ServerException, 
               ResourceUnknownException,
               ClientException, 
               RequestException, 
               ClientToolkitException
    {
        Workflow workflowToExecute = transformWorkflow(workflow);
        return mDrer.execute(
            workflowToExecute, requestExecutionType, sessionOperation);
    }

    /**
     * {@inheritDoc}
     */
    public void addServerCommsProperty(String arg0, Object arg1)
    {
        mDrer.addServerCommsProperty(arg0,arg1);
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() 
        throws ServerCommsException, 
               ServerException,
               ClientServerCompatibilityException, 
               ResourceUnknownException,
               ResourceNotDestroyedException
    {
        mDrer.destroy();
    }

    /**
     * {@inheritDoc}
     */
    public ActivitiesMetaData getActivityDetails() 
        throws ServerCommsException,
               ServerException, 
               ResourceUnknownException,
               ClientServerCompatibilityException, 
               ClientToolkitException
    {
        return mDrer.getActivityDetails();
    }

    /**
     * {@inheritDoc}
     */
    public Date getCurrentTime() 
        throws ServerCommsException, 
               ServerException,
               ClientServerCompatibilityException, 
               ResourceUnknownException,
               ClientToolkitException
    {
        return mDrer.getCurrentTime();
    }

    public ResourceProperty[] getMultipleResourceProperties(
        ResourcePropertyName[] arg0) 
        throws ServerCommsException,
               ServerException, 
               ClientServerCompatibilityException,
               ResourceUnknownException, 
               PropertyNameInvalidException
    {
        return mDrer.getMultipleResourceProperties(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public ResourceID getResourceID()
    {
        return mDrer.getResourceID();
    }

    /**
     * {@inheritDoc}
     */
    public ResourceProperty getResourceProperty(ResourcePropertyName arg0)
        throws ServerCommsException, 
               ServerException,
               ClientServerCompatibilityException, 
               ResourceUnknownException,
               PropertyNameInvalidException
    {
        return mDrer.getResourceProperty(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public ResourceType getResourceType()
    {
        return mDrer.getResourceType();
    }

    /**
     * {@inheritDoc}
     */
    public Date getTerminationTime() throws ServerCommsException,
        ServerException, ResourceUnknownException,
        ClientServerCompatibilityException, ClientToolkitException
    {
        return mDrer.getTerminationTime();
    }

    /**
     * {@inheritDoc}
     */
    public TerminationTime setTerminationTime(Date arg0)
        throws ServerCommsException, 
               ServerException,
               ClientServerCompatibilityException, 
               ResourceUnknownException,
               UnableToSetTerminationTimeException,
               TerminationTimeChangeRejectedException
    {
        return mDrer.setTerminationTime(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public void setTimeout(int arg0)
    {
        mDrer.setTimeout(arg0);
    }
    
    /**
     * Transforms the given workflow using the workflow transformer.
     * 
     * @param workflow input workflow.
     * 
     * @return transformed workflow.
     */
    protected Workflow transformWorkflow(Workflow workflow)
    {
        LOG.debug("About to transform a workflow");
        return mWorkflowTransformer.transform(workflow);
    }
}
