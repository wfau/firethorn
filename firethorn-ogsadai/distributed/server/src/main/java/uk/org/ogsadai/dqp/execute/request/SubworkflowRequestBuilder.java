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


package uk.org.ogsadai.dqp.execute.request;

import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.activity.pipeline.ActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.ActivityInput;
import uk.org.ogsadai.activity.pipeline.ActivityInputStream;
import uk.org.ogsadai.activity.pipeline.ActivityOutput;
import uk.org.ogsadai.activity.pipeline.ActivityOutputStream;
import uk.org.ogsadai.activity.pipeline.ActivityPipeline;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.SimpleActivityPipeline;
import uk.org.ogsadai.activity.workflow.ActivityPipelineWorkflow;
import uk.org.ogsadai.activity.workflow.Workflow;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInputDescriptor;
import uk.org.ogsadai.client.toolkit.activity.RequestBuilder;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Builds an internal sub-workflow from a client toolkit request. 
 *
 * @author The OGSA-DAI Project Team.
 */
public class SubworkflowRequestBuilder implements RequestBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";
    
    /** Root workflow. */
    private Workflow mRoot;
    /** Current node in the workflow. */
    private Workflow mCurrent;
    /** Current activity pipeline if one is being built. */
    private ActivityPipeline mCurrentPipeline;
    
    /** Pipeline parent or <code>null</code> if there is none. */
    private Activity mParent;
    /** Name of the parent input. */ 
    private String mParentInput;
    /** Unconnected output - to be connected to the parent activity. */
    private SingleActivityOutput mUnconnectedOutput;
    
    /**
     * Returns the activity pipeline if the root workflow is a pipeline
     * workflow.
     * 
     * @return root activity pipeline
     * @throws IllegalStateException
     *             if the root workflow is not a pipeline
     */
    public ActivityPipeline getActivityPipeline()
    {
        if (mRoot instanceof ActivityPipelineWorkflow)
        {
            return ((ActivityPipelineWorkflow)mRoot).getActivityPipeline();
        }
        else
        {
            throw new IllegalStateException();
        }
    }

    /**
     * Connect the parent activity to an unconnected output of the current
     * pipeline.
     * 
     * @param parent
     *            parent activity
     * @param parentInputName
     *            parent input name
     * @param output
     *            output to connect to
     */
    public void connectParent(
            Activity parent,
            String parentInputName,
            SingleActivityOutput output)
    {
        mParent = parent;
        mParentInput = parentInputName;
        mUnconnectedOutput = output;
    }

    /**
     * {@inheritDoc}
     */
    public boolean mustValidateState() 
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void addActivity(
            ActivityName activityName,
            ActivityInstanceName instanceName,
            ActivityInputDescriptor[] inputs, 
            SingleActivityOutput[] outputs)
    {
        ActivityDescriptor descriptor = new SimpleActivityDescriptor(
                activityName, instanceName.toString());
        addInputsAndOutputs(descriptor, inputs, outputs);
        mCurrentPipeline.addActivity(descriptor);
    }

    /**
     * {@inheritDoc}
     */
    public void addActivity(
            ActivityName activityName,
            ActivityInstanceName instanceName, 
            ResourceID resourceID,
            ActivityInputDescriptor[] inputs, 
            SingleActivityOutput[] outputs)
    {
        ActivityDescriptor descriptor = new SimpleActivityDescriptor(
                resourceID, activityName, instanceName.toString());
        addInputsAndOutputs(descriptor, inputs, outputs);
        mCurrentPipeline.addActivity(descriptor);
    }

    /**
     * Adds inputs and outputs to an activity descriptor.
     * 
     * @param descriptor
     *            activity descriptor
     * @param inputs
     *            activity inputs
     * @param outputs
     *            activity outputs
     */
    private void addInputsAndOutputs(
            ActivityDescriptor descriptor,
            ActivityInputDescriptor[] inputs,
            SingleActivityOutput[] outputs)
    {
        for (ActivityInputDescriptor input : inputs)
        {
            SubworkflowActivityInputDescriptorVisitor visitor = 
                new SubworkflowActivityInputDescriptorVisitor();
            input.accept(visitor);
            ActivityInput activityInput = visitor.getInput();
            descriptor.addInput(activityInput);
        }
        for (SingleActivityOutput output : outputs)
        {
            ActivityOutput activityOutput = 
                new ActivityOutputStream(
                        output.getOutputName(), output.getPipeName());
            descriptor.addOutput(activityOutput);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void endParallel()
    {
        mCurrent = ((ParallelWorkflow) mCurrent).getParent();
    }

    /**
     * {@inheritDoc}
     */
    public void endPipeline()
    {
        Workflow workflow = new ActivityPipelineWorkflow(mCurrentPipeline);
        if (mRoot == null)
        {
            mCurrent = workflow;
            mRoot = mCurrent;
        }
        else
        {
            mCurrent.addChild(workflow);
        }
        mCurrentPipeline = null;
    }

    /**
     * {@inheritDoc}
     */
    public void endSequence()
    {
        mCurrent = ((SequenceWorkflow) mCurrent).getParent();
    }

    /**
     * {@inheritDoc}
     */
    public void startParallel()
    {
        if (mRoot == null)
        {
            mCurrent = new ParallelWorkflow(null);
            mRoot = mCurrent;
        }
        else
        {
            Workflow child = new ParallelWorkflow(mCurrent);
            mCurrent.addChild(child);
            mCurrent = child;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void startPipeline()
    {
        mCurrentPipeline = new SimpleActivityPipeline();
        connectParent();
    }

    /**
     * {@inheritDoc}
     */
    public void startSequence()
    {
        if (mRoot == null)
        {
            mCurrent = new SequenceWorkflow(null);
            mRoot = mCurrent;
        }
        else
        {
            Workflow child = new SequenceWorkflow(mCurrent);
            mCurrent.addChild(child);
            mCurrent = child;
        }
    }

    /**
     * Connects the parent to the current pipeline workflow.
     */
    private void connectParent()
    {
        if (mParent != null)
        {
            mCurrentPipeline.setParent(mParent);
            mCurrentPipeline.addActivity(mParent.getActivityDescriptor());
            mUnconnectedOutput.setIsConnected();
            mParent.getActivityDescriptor().addInput(
                    new ActivityInputStream(
                            mParentInput,
                            mUnconnectedOutput.getPipeName()));
        }
    }
}
