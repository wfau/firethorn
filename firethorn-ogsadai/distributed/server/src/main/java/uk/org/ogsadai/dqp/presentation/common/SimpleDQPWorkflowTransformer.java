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

import java.util.List;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ParallelWorkflow;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.SequenceWorkflow;
import uk.org.ogsadai.client.toolkit.Workflow;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.execute.workflow.DQPDeliverToDataSink;
import uk.org.ogsadai.dqp.execute.workflow.DQPObtainFromDataSource;

/**
 * Workflow transformer that transforms a DQP generated workflow so that it
 * is compatible with presentation layers that operate using service URL and
 * resource ID.  This includes the Globus Toolkit and Axis presentation layers.
 * <p>
 * Workflow transforms such as this are used to keep the presentation layer
 * details out of core DQP code.
 * <p>
 * Instances of activities with activity name 
 * <tt>uk.org.ogsadai.ObtainFromDataSource</tt> are given an addition input 
 * called '<tt>url</tt>' that is the URL of the node on which
 * the data source resource resides.  Details of the node are obtained by
 * casting the activity instance to <tt>DQPObtainFromDataSource</tt> and 
 * obtaining the evaluation node via the <tt>getEvaluationNode</tt> method.
 * The evaluation node returned is assumed to be of type 
 * <tt>SimpleEvaluationNode</tt> and hence the URL can be obtained by calling
 * the <tt>getDataSourceServiceURL</tt> method.
 *
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleDQPWorkflowTransformer implements WorkflowTransformer
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009.";
    
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SimpleDQPWorkflowTransformer.class);
    
    /** Activity name for ObtainFromDataSource activity. */
    private static final ActivityName OBTAIN_FROM_DATA_SOURCE_ACTIVITY_NAME = 
        new ActivityName("uk.org.ogsadai.ObtainFromDataSource");

    /** Activity name for DeliverToDataSink activity. */
    private static final ActivityName DELIVER_TO_DATA_SINK_ACTIVITY_NAME = 
        new ActivityName("uk.org.ogsadai.DeliverToDataSink");
    
    /**
     * Constructor.
     */
    public SimpleDQPWorkflowTransformer()
    {
        LOG.debug("In SimpleDQPWorkflowTransformer constructor.");
    }

    /**
     * {@inheritDoc}
     */
    public Workflow transform(Workflow workflow)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("About to transform workflow:\n" + workflow);
        }
        
        if (workflow instanceof ParallelWorkflow)
        {
            LOG.debug("Parallel worklow");
        }
        else if (workflow instanceof SequenceWorkflow)
        {
            LOG.debug("Sequence workflow");
        }
        else if (workflow instanceof PipelineWorkflow)
        {
            LOG.debug("Pipeline workflow");
            PipelineWorkflow pipeline = (PipelineWorkflow) workflow;
            
            List<Activity> activities = pipeline.getActivities();
            
            for( Activity activity: activities)
            {
                if (activity instanceof GenericActivity)
                {
                    GenericActivity genericActivity = 
                        (GenericActivity) activity;
                    
                    if (activity.getActivityName().equals(
                        OBTAIN_FROM_DATA_SOURCE_ACTIVITY_NAME))
                    {
                        LOG.debug("Found uk.org.ogsadai.ObtainFromDataSource");
                        
                        if (activity instanceof DQPObtainFromDataSource)
                        {
                            // Cast to get the evaluation node
                            DQPObtainFromDataSource obtain = 
                                (DQPObtainFromDataSource) activity;
                            
                            SimpleEvaluationNode evaluationNode = 
                                (SimpleEvaluationNode) 
                                    obtain.getEvaluationNode();
                        
                            // Add new input that is url
                            genericActivity.createInput("url");
                            genericActivity.addInput(
                                "url",
                                evaluationNode.
                                    getDataSourceServiceURL().toString());
                            
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("Added new URL: " + 
                                    evaluationNode.getDataSourceServiceURL());
                            }
                        }
                    }
                    else if (activity.getActivityName().equals(
                        DELIVER_TO_DATA_SINK_ACTIVITY_NAME))
                    {
                        LOG.debug("Found uk.org.ogsadai.DeliverToDataSink");
                        
                        if (activity instanceof DQPDeliverToDataSink)
                        {
                            // Cast to get the evaluation node
                            DQPDeliverToDataSink deliver = 
                                (DQPDeliverToDataSink) activity;
                            
                            SimpleEvaluationNode evaluationNode = 
                                (SimpleEvaluationNode) 
                                    deliver.getEvaluationNode();
                        
                            // Add new input that is url
                            genericActivity.createInput("url");
                            genericActivity.addInput(
                                "url",
                                evaluationNode.
                                    getDataSinkServiceURL().toString());
                            
                            if (LOG.isDebugEnabled())
                            {
                                LOG.debug("Added new URL: " + 
                                    evaluationNode.getDataSinkServiceURL());
                            }
                        }
                    }
                }
            }
        }
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Transformed workflow is:\n" + workflow);
        }

        return workflow;
    }
}
