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

import java.util.LinkedList;
import java.util.List;

import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.execute.ExecutionException;
import uk.org.ogsadai.dqp.execute.LocalWorkflowProcessor;
import uk.org.ogsadai.dqp.execute.workflow.PipelineWorkflowBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.resource.request.RequestStatus;

/**
 * A local partition which executes sub-workflows.
 *
 * @author The OGSA-DAI Project Team.
 */
public class LocalPartition implements Partition
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(LocalPartition.class);
    /** Root operator of this partition. */
    private Operator mRoot;
    /** Local evaluation node. */
    private final EvaluationNode mNode;
    /** Names of data sources that are created at setup. */
    private final List<String> mSetupDataSources;
    /** Names of data sinks that are created at setup. */
    private final List<String> mSetupDataSinks;
    /** Executes the pipeline. */
    private LocalWorkflowProcessor mExecutor;
    /** Unconnected root output of the activity pipeline. */
    private SingleActivityOutput mUnconnectedOutput;
    /** Activity pipeline. */
    private PipelineWorkflow mPipeline; 
    /** Details of parent request. */
    private RequestDetails mRequestDetails;
    /** Partition listeners list. */
    List<PartitionEventListener> mPartitionListeners;
    
    /**
     * Constructs a new local partition.
     * 
     * @param exec
     *            subworkflow executor service
     * @param requestDetails
     *            details of parent request
     */
    public LocalPartition(
        LocalWorkflowProcessor exec, RequestDetails requestDetails)
    {
        mNode = exec.getLocalNode();
        mSetupDataSources = new LinkedList<String>();
        mSetupDataSinks = new LinkedList<String>();
        mExecutor = exec;
        mRequestDetails = requestDetails;
    }
    
    /**
     * {@inheritDoc}
     */
    public void buildWorkflow(PipelineWorkflowBuilder builder) 
        throws ActivityConstructionException
    {
        builder.build(mRoot);
        mUnconnectedOutput = builder.getUnconnectedOutput();
        mPipeline = builder.getPipelineWorkflow();

        if (LOG.isDebugEnabled())
        {
            LOG.debug(mPipeline.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addDataSourceToSetup(String dataSource)
    {
        mSetupDataSources.add(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    public void addDataSinkToSetup(String dataSink)
    {
        mSetupDataSinks.add(dataSink);
    }

    /**
     * {@inheritDoc}
     */
    public EvaluationNode getEvaluationNode()
    {
        return mNode;
    }
    
    /**
     * {@inheritDoc}
     */
    public void executeSetup() throws ExecutionException
    {
        mExecutor.createDataSources(mSetupDataSources);
        mExecutor.createDataSinks(mSetupDataSinks);
    }

    /**
     * {@inheritDoc}
     */
    public void execute() throws ExecutionException
    {
        mPipeline = mNode.transformWorkflow(mPipeline, mRequestDetails);

        // Issue workflow execute event
        if (mPartitionListeners != null)
            for (PartitionEventListener pl : mPartitionListeners)
                pl.partitionExecuteEvent(mRequestDetails, null, mNode,
                    mPipeline);

        // execute sub-workflow
        // connect output of sub-workflow to input of task executor activity
        // stream output through to activity output
        // this method blocks until the request has completed
        mExecutor.executePipeline(mPipeline, mUnconnectedOutput);
    }
    
    /**
     * {@inheritDoc}
     */
    public void cleanup()
    {
        // destroy data sources
        mExecutor.destroyDataSources(mSetupDataSources);
        // destroy data sinks
        mExecutor.destroyDataSinks(mSetupDataSinks);
    }

    /**
     * {@inheritDoc}
     */
    public void setRoot(Operator root)
    {
        mRoot = root;
    }
    
    /**
     * {@inheritDoc}
     */
    public Operator getRoot()
    {
        return mRoot;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "LOCAL PARTITION (" + mNode + ")" + mRoot;
    }

    /**
     * {@inheritDoc}
     */
    public RequestStatus getRequestStatus()
    {
        return mExecutor.getRequestStatusBuilder().getRequestStatus();
    }

    /**
     * {@inheritDoc}
     */
    public void registerPartitionEventListeners(List<PartitionEventListener> partitionListeners)
    {
        mPartitionListeners = partitionListeners;
    }
}
