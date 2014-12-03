// Copyright (c) The University of Edinburgh, 2009.
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

import java.net.MalformedURLException;
import java.net.URL;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.DataSinkResource;
import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.ServerProxy;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.resource.ResourceID;

/**
 * A simple evaluation node for Axis-like services that have a service base URL 
 * and resource IDs.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SimpleEvaluationNode implements EvaluationNode
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** DRER ID. */
    private final ResourceID mDRERID;
    /** Service base URL. */
    private final URL mServiceURL;
    /** Data source service URL. */
    private final URL mDSoSURL;
    /** Data sink service URL. */
    private final URL mDSiSURL;
    /** Indicates whether the evaluation node is local. */
    private final boolean mIsLocal;
    
    /** 
     * Workflow transformer that converts presentation layer independent
     * workflows to Axis and GT compatible workflows.
     */
    private final WorkflowTransformer mWorkflowTransformer;

    /**
     * Constructs a new evaluation node.
     * 
     * @param url
     *            services base URL
     * @param drerID
     *            execution resource ID
     * @param dsos
     *            data source service name
     * @param dsis
     *            data sink service name
     * @param isLocal
     *            <tt>true</tt> if the evaluation node to local (on the same
     *            OGSA-DAI instance as the DQP resource), <tt>false</tt>
     *            if it is remote.
     * @param workflowTransformer
     *            workflow transformer user to transform presentation layer
     *            neutral workflows to Axis and GT compatible workflows.
     *            
     * @throws MalformedURLException
     */
    public SimpleEvaluationNode(
        String url, String drerID, 
        String dsos, 
        String dsis,
        boolean isLocal,
        WorkflowTransformer workflowTransformer) 
        throws MalformedURLException
    {
        mDRERID = new ResourceID(drerID);
        mServiceURL = new URL(url);
        mDSoSURL = new URL(url + "/" + dsos);
        mDSiSURL = new URL(url + "/" + dsis);
        mWorkflowTransformer = workflowTransformer;
        mIsLocal = isLocal;
    }
    
    /**
     * Constructs a new evaluation node.
     * 
     * @param url
     *            services base URL
     * @param drerID
     *            execution resource ID
     * @param dsos
     *            data source service name
     * @param dsis
     *            data sink service name
     * @param isLocal
     *            <tt>true</tt> if the evaluation node to local (on the same
     *            OGSA-DAI instance as the DQP resource), <tt>false</tt>
     *            if it is remote.
     *            
     * @throws MalformedURLException
     */
    public SimpleEvaluationNode(
        String url, String drerID, String dsos, String dsis, boolean isLocal) 
        throws MalformedURLException
    {
        this(url, 
             drerID, 
             dsos, 
             dsis, 
             isLocal, 
             new SimpleDQPWorkflowTransformer());
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLocal()
    {
        return mIsLocal;
    }

    /**
     * Returns the base URL of this evaluation node.
     * 
     * @return service base URL
     */
    protected URL getURL()
    {
        return mServiceURL;
    }
    
    /**
     * Returns the data source service URL of this evaluation node.
     * 
     * @return data source service URL
     */
    public URL getDataSourceServiceURL()
    {
        return mDSoSURL;
    }

    /**
     * Returns the data sink service URL of this evaluation node.
     * 
     * @return data sink service URL
     */
    public URL getDataSinkServiceURL()
    {
        return mDSiSURL;
    }

    /**
     * Returns the DRER ID.
     * 
     * @return DRER ID
     */
    protected ResourceID getDRERID()
    {
        return mDRERID;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return 31 + mDRERID.hashCode() + mServiceURL.toString().hashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object instanceof SimpleEvaluationNode)
        {
            SimpleEvaluationNode node = (SimpleEvaluationNode)object;
            return (node.mDRERID.equals(mDRERID) 
                    && node.mServiceURL.toString().equalsIgnoreCase(
                            mServiceURL.toString()));
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataRequestExecutionResource getDRER(RequestDetails requestDetails)
    {
        Server service = getServer();
        DataRequestExecutionResource drer = 
            service.getDataRequestExecutionResource(getDRERID());
        return new TransformDataRequestExecutionResource(
            drer, mWorkflowTransformer);
    }
    
    /**
     * {@inheritDoc}
     */
    public DataSourceResource getDataSourceResource(
        ResourceID resourceID,
        RequestDetails requestDetails)
    {
        Server service = getServer();
        return service.getDataSourceResource(
            getDataSourceServiceURL(), resourceID);
    }

    /**
     * {@inheritDoc}
     */
    public DataSinkResource getDataSinkResource(
        ResourceID resourceID,
        RequestDetails requestDetails)
    {
        Server service = getServer();
        return service.getDataSinkResource(
            getDataSinkServiceURL(), resourceID);
    }

    /**
     * {@inheritDoc}
     */
    public PipelineWorkflow transformWorkflow(PipelineWorkflow workflow,
        RequestDetails requestDetails)
    {
        return (PipelineWorkflow) mWorkflowTransformer.transform(workflow);
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "EvaluationNode [URL=" + mServiceURL + "]";
    }

    /**
     * {@inheritDoc}
     * 
     * Using a proxy here is a bad idea. Better to have different Axis and GT
     * versions I think.
     * 
     * TODO: Have access and GT versions.
     * 
     * @return the client toolkit server.
     */
    public Server getServer()
    {
        Server s = new ServerProxy();
        s.setDefaultBaseServicesURL(getURL());
        
        return s;
    }
}
