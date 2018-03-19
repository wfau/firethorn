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

package uk.org.ogsadai.dqp.execute;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;

/**
 * Interface for query plan builders that build a query plan to execute a
 * given query.  
 *
 * @author The OGSA-DAI Project Team
 */
public interface QueryPlanBuilder
{
    /**
     * Builds a query plan.
     * <p>
     * Implementations of this method must be thread-safe as this method may be
     * called concurrently by multiple threads.
     * 
     * @param query
     *   user's query.
     *   
     * @param dqpResourceAccessor
     *   DQP resource accessor giving access to the resource's configuration.
     *  
     * @param requestDetails
     *   details of the user's request
     *   
     * @return
     *   root operator of the query plan that will execute the query.
     *   
     * @throws ActivityUserException
     *   if there was in error in the user's query.
     *   
     * @throws ActivityProcessingException 
     *   if there is an internal error while processing the query that is not
     *   due to an error in the user query.
     */
    public Operator buildQueryPlan(
        String query, 
        DQPResourceAccessor dqpResourceAccessor,
        RequestDetails requestDetails) 
        throws ActivityUserException, ActivityProcessingException;
}
