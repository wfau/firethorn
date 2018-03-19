// Copyright (c) The University of Edinburgh, 2008.
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

package uk.org.ogsadai.dqp.execute.partition;

import uk.org.ogsadai.client.toolkit.Workflow;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.resource.ResourceID;

/**
 * @author The OGSA-DAI Project Team.
 */
public interface PartitionEventListener
{
    /**
     * Event raised when partition is submitted for execution.
     * 
     * @param requestDetails
     *            details of a parent request that led to the creation of a
     *            partition
     * @param childRequestID
     *            requestID associated with a remote partition,
     *            <code>null</code> if partition is local
     * @param evaluationNode
     *            partition evaluation node
     * @param workflow
     *            workflow associated with a partition
     */
    void partitionExecuteEvent(RequestDetails requestDetails,
        ResourceID childRequestID, EvaluationNode evaluationNode,
        Workflow workflow);
}
