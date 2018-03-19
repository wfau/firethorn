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

package uk.org.ogsadai.dqp.execute.partition;

import java.util.List;

import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.execute.LocalWorkflowProcessor;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;

/**
 * Implements partition factory.
 *
 * @author The OGSA-DAI Project Team.
 */
public class SimplePartitionFactory implements PartitionFactory
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE =
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Local workflow processor. */
    private LocalWorkflowProcessor mLocalExec;
    /** DQP resource accessor, supplies configuration parameters. */
    private final DQPResourceAccessor mResourceAccessor;
    /** Partition event listener. */
    List<PartitionEventListener> mPartitionListeners;
    
    /**
     * Constructor.
     * 
     * @param processor
     *            local workflow processor
     * @param resource
     *            resource accessor, used to provide configuration parameters
     */
    public SimplePartitionFactory(
        LocalWorkflowProcessor processor,
        DQPResourceAccessor resource,
        List<PartitionEventListener> partitionListeners)
    {
        mLocalExec = processor;
        mResourceAccessor = resource;
        mPartitionListeners = partitionListeners;
    }
    
    /**
     * {@inheritDoc}
     */
    public Partition createPartition(
        EvaluationNode node, RequestDetails requestDetails)
    {
        Partition partition;
        if (node.isLocal())
        {
            partition = new LocalPartition(mLocalExec, requestDetails);
        }
        else
        {
            partition =
                new RemotePartition(node, requestDetails,
                    mResourceAccessor.getPollInterval());
        }
        partition.registerPartitionEventListeners(mPartitionListeners);

        return partition;
    }

    /**
     * {@inheritDoc}
     */
    public Partition createLocalPartition(RequestDetails requestDetails)
    {
        return new LocalPartition(mLocalExec, requestDetails);
    }

}
