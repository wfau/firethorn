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

import java.util.List;

import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.execute.ExecutionException;
import uk.org.ogsadai.dqp.execute.workflow.PipelineWorkflowBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.resource.request.RequestStatus;

/**
 * A DQP query plan partition.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface Partition
{
    /**
     * Sets the root operator of the partition.
     * 
     * @param root
     *            root operator
     */
    public void setRoot(Operator root);
    
    /**
     * Returns the root operator of the partition.
     * 
     * @return root operator
     */
    public Operator getRoot();
    
    /**
     * Returns the evaluation node which processes this partition.
     * 
     * @return evaluation node
     */
    public EvaluationNode getEvaluationNode();

    /**
     * Builds the workflows.
     * @param pipelineWorkflowBuilder 
     * 
     * @throws ActivityConstructionException
     */
    public void buildWorkflow(PipelineWorkflowBuilder pipelineWorkflowBuilder) 
        throws ActivityConstructionException;

    /**
     * Adds a data source to the list of setup tasks.
     * 
     * @param dataSource
     *            data source name
     */
    public void addDataSourceToSetup(String dataSource);
    
    /**
     * Adds a data sink to the list of setup tasks.
     * 
     * @param dataSink
     *            data sink name
     */
    public void addDataSinkToSetup(String dataSink);

    /**
     * Returns the request status of the main workflow. Note that this method
     * returns <code>null</code> if execute() has not been invoked before.
     * 
     * @return request status or <code>null</code> if the workflow has not been
     *         executed or an error occurred before the workflow could be
     *         executed
     */
    public RequestStatus getRequestStatus();
    
    /**
     * Executes the setup workflows, for example, to create data sources.
     * 
     * @throws ExecutionException
     */
    public void executeSetup() throws ExecutionException;
    
    /**
     * Executes the partition workflow. This method blocks until the workflow
     * has completed. 
     * 
     * @throws ExecutionException
     */
    public void execute() throws ExecutionException;
    
    /**
     * Releases the resources that have been allocated for this partition.
     */
    public void cleanup();
    
    /**
     * Registers a list of partition event listeners.
     * 
     * @param partitionListeners
     */
    void registerPartitionEventListeners(
        List<PartitionEventListener> partitionListeners);
}
