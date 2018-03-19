// Copyright (c) The University of Edinburgh, 2008-2009.
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

package uk.org.ogsadai.resource.dataresource.dqp;

import java.util.Calendar;

import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceLifetime;
import uk.org.ogsadai.resource.ResourceState;
import uk.org.ogsadai.resource.dataresource.DataResource;
import uk.org.ogsadai.resource.dataresource.DataResourceState;

/**
 * A DQP resource which manages a data dictionary and accesses remote data and
 * evaluation nodes.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class DQPResource implements DataResource
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";

    /** Resource state. */
    private DQPResourceState mState;
    
    /** Logger for this class. */
    private static final DAILogger LOG = DAILogger.getLogger(DQPResource.class);
    
    /**
     * {@inheritDoc}
     */
    public ResourceState getState()
    {
        LOG.debug("In DQPResource#getState()");
        return mState.getState();
    }
    
    /**
     * Returns the DQP resource state.
     * 
     * @return resource state
     */
    public DQPResourceState getDQPResourceState()
    {
        return mState;
    }

    /**
     * {@inheritDoc}
     */
    public void initialize(ResourceState resourceState)
    {
        LOG.debug("In DQPResource#initialize()");
        try
        {
            mState = new SimpleDQPResourceState((DataResourceState)resourceState);
        }
        catch(Throwable t)
        {
            LOG.debug("DQPResource#initialize throws " + t.getLocalizedMessage());
            LOG.error(t);
        }
        LOG.debug("Exiting DQPResource#initialize()");
    }

    /**
     * {@inheritDoc}
     */
    public ResourceAccessor createResourceAccessor(
            SecurityContext securityContext)
    {
        return new DQPResourceAccessor(this, securityContext);
    }

    /**
     * {@inheritDoc}
     */
    public void destroy()
    {
        ResourceLifetime resourceLifetime = 
            mState.getState().getResourceLifetime();
        resourceLifetime.setTerminationTime(Calendar.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    public ResourceID getResourceID()
    {
        return mState.getState().getResourceID();
    }
    
    /**
     * Returns the local evaluation node of this DQP resource.
     * 
     * @return local evaluation node
     */
    public EvaluationNode getLocalNode()
    {
        return mState.getFederation().getLocalNode();
    }
    
    /**
     * Returns the compiler configuration.
     * 
     * @return compiler configuration
     */
    public CompilerConfiguration getCompilerConfiguration()
    {
        return mState.getCompilerConfiguration();
    }

    /**
     * Returns the federation exposed by this resource.
     * 
     * @return the federation
     */
    public DQPFederation getFederation()
    {
        return mState.getFederation();
    }
    
}
