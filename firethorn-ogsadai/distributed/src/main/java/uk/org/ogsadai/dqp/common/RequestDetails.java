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

package uk.org.ogsadai.dqp.common;

import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestResource;

/**
 * Contains details of a user request.
 *
 * @author The OGSA-DAI Project Team
 */
public interface RequestDetails
{
    /**
     * Returns the Data Request Execution Resource ID.
     * 
     * @return resource ID.
     */
    public ResourceID getResourceID();
    
    /**
     * Returns the request resource.
     * 
     * @return request resource.
     */
    public RequestResource getRequestResource();
    
    /**
     * Returns the security context for the request.
     * 
     * @return security context.
     */
    public SecurityContext getSecurityContext();
    
    /**
     * Returns instance name of an activity encapsulating a coordinator.
     * 
     * @return activity instance name.
     */
    public ActivityInstanceName getActivityInstanceName();
}
