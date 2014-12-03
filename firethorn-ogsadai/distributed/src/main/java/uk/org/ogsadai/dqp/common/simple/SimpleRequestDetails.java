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

package uk.org.ogsadai.dqp.common.simple;

import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestResource;

/**
 * Contains details of a user request.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleRequestDetails implements RequestDetails
{
    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Security context. */
    protected SecurityContext mSecurityContext;
    /** Request resource */
    protected RequestResource mRequestResource;
    /** ID of the Data Request Execution Resource executing the request. */
    protected ResourceID mResourceID;
    /** Coordinating activity instance name. */
    protected ActivityInstanceName mActivityInstanceName;
    
    /**
     * Constructor.
     * 
     * @param resourceID
     *            ID of Data Request Execution Resource
     * @param requestResource
     *            Request Resource
     * @param securityContext
     *            Security context
     * @param activityInstanceName
     *            Coordinating activity instance name
     */
    public SimpleRequestDetails(ResourceID resourceID,
        RequestResource requestResource, SecurityContext securityContext,
        ActivityInstanceName activityInstanceName)
    {
        mResourceID = resourceID;
        mRequestResource = requestResource;
        mSecurityContext = securityContext;
        mActivityInstanceName = activityInstanceName;
    }

    /**
     * {@inheritDoc}
     */
    public ResourceID getResourceID()
    {
        return mResourceID;
    }

    /**
     * {@inheritDoc}
     */
    public RequestResource getRequestResource()
    {
        return mRequestResource;
    }

    /**
     * {@inheritDoc}
     */
    public SecurityContext getSecurityContext()
    {
        return mSecurityContext;
    }
    
    /**
     * {@inheritDoc}
     */
    public ActivityInstanceName getActivityInstanceName()
    {
        return mActivityInstanceName;
    }
}
