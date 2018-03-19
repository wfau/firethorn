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


package uk.org.ogsadai.dqp.execute.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityInput;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.execute.WorkflowBuilder;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;

/**
 * Builds client toolkit pipelines.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PipelineWorkflowBuilder implements WorkflowBuilder
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(PipelineWorkflowBuilder.class);

    /** Maps operator IDs to pipeline builders. */
    private CompilerConfiguration mCompilerConfig;
    /** The pipeline that is built. */
    private PipelineWorkflow mPipeline;
    /** The partition that creates the data sources. */
    private Partition mPartition;
    /** The unconnected output of the pipeline or <code>null</code>. */
    private SingleActivityOutput mUnconnectedOutput;
    /** 
     * Connectors used to connect external inputs and output used by the
     * apply functionality. 
     */
    private ConnectorMap mConnectors;
    
    /**
     * Cross pipeline workflow builders, applied after all workflows have
     * been built.
     */
    private List<CrossPipelineWorkflowBuilder> mCrossPipelineWorkflowBuilders;
   
    /** Operator currently being processed. */
    private Operator mCurrentOperator;
    
    /** 
     * Mapping from operator to the activities that were added when building
     * that operator.
     */
    private Map<Operator,List<Activity>> mOperatorActivities =
        new HashMap<Operator,List<Activity>>();

    /**
     * Constructs a new remote workflow builder.
     * 
     * @param partition
     *            partition
     * @param connectors
     *            map of external inputs and outputs to be connected between
     *            partitions
     */
    public PipelineWorkflowBuilder(Partition partition, ConnectorMap connectors)
    {
        mCrossPipelineWorkflowBuilders = 
            new LinkedList<CrossPipelineWorkflowBuilder>();
        mPipeline = new PipelineWorkflow();
        mPartition = partition;
        mConnectors = connectors;
    }

    /**
     * Registers a mapping between operator identifiers and  pipeline builders.
     * 
     * @param compilerConfig
     *            maps operators to builders 
     */
    public void setConfiguration(CompilerConfiguration compilerConfig)
    {
        mCompilerConfig = compilerConfig;
    }

    /**
     * {@inheritDoc}
     * @throws ActivityConstructionException 
     */
    public void build(Operator operator) throws ActivityConstructionException
    {
        mUnconnectedOutput = buildChild(operator); 
    }

    /**
     * Returns the unconnected root output of the pipeline.
     * 
     * @return root output
     */
    public SingleActivityOutput getUnconnectedOutput()
    {
        return mUnconnectedOutput;
    }

    /**
     * Returns the pipeline workflow that has been built.
     * 
     * @return pipeline
     */
    public PipelineWorkflow getPipelineWorkflow()
    {
        return mPipeline;
    }
    
    /**
     * Builds the subtree of the given operator.
     * 
     * @param operator
     *            operator
     * @return output from the subtree pipeline
     * @throws ActivityConstructionException 
     */
    public SingleActivityOutput buildChild(Operator operator)
        throws ActivityConstructionException
    {   
        List<SingleActivityOutput> childOutputs = 
            new ArrayList<SingleActivityOutput>();
        
        if (!operator.isPartitionLeaf() && operator.getChild(0) != null)
        {
            childOutputs.add(buildChild(operator.getChild(0)));
            if (operator.isBinary())
            {
                childOutputs.add(buildChild(operator.getChild(1)));
            }
        }
        return buildOperator(operator, childOutputs);
    }

    /**
     * Builds the activities for an operator.
     * 
     * @param operator
     *            operator
     * @param outputs
     *            outputs from child operators
     * @return output from this operator
     * @throws ActivityConstructionException
     */
    public SingleActivityOutput buildOperator(
            Operator operator, 
            List<SingleActivityOutput> outputs)
        throws ActivityConstructionException
    {   
        Map<String, ActivityPipelineBuilder> map = 
            mCompilerConfig.getBuilders(operator.getID());
        if (map == null)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug(
                    "Cannot get builder for operator with id: " + 
                    operator.getID());
            }
            throw new ActivityConstructionException(
                    new UnsupportedOperatorException(operator.getClass().getName()));
        }
        Object implementation =
            Annotation.getImplementationAnnotation(operator);
        ActivityPipelineBuilder builder = null;
        if (implementation != null)
        {
            builder = map.get(implementation);
        }
        else
        {
            // for now we're using the default builder
            builder = map.get(CompilerConfiguration.DEFAULT_BUILDER);
        }
        if (builder == null)
        {
            throw new ActivityConstructionException(
                    new Exception("Configuration exception: " +
                            "No builder defined for operator " +
                            operator.getClass().getName() + 
                            " and implementation annotation '" +
                            implementation + "'."));
        }
        
        mCurrentOperator = operator;
        return builder.build(operator, outputs, this);
    }

    /**
     * Adds an activity to the pipeline.
     * 
     * @param activity
     *            activity to add
     */
    public void add(Activity activity)
    {
        if (!mOperatorActivities.containsKey(mCurrentOperator))
        {
            mOperatorActivities.put(
                mCurrentOperator, new LinkedList<Activity>());
        }
        mOperatorActivities.get(mCurrentOperator).add(activity);
        
        mPipeline.add(activity);
    }

    /**
     * Sets the operator that is currently being processed.  The class normally
     * knows which operator is being processed but in cases where an operator
     * wraps other operators (for example, an APPLY operator wrapping a JOIN
     * operator) this class can get confused about what operator is currently
     * being processed.  This methods the wrapper operator to reset the current
     * operator after it has build the wrapped operator.
     * <p>
     * This class need to know the current operator only for the 
     * <tt>getLiteralInputs</tt> method.  If that method is not used, or wrapped
     * operators are not being used, then there is no need to call this method.
     * 
     * @param operator current operator.
     */
    public void setCurrentOperator(Operator operator)
    {
        mCurrentOperator = operator;
    }
    
    /**
     * Adds a data source to the setup of the partition.
     * 
     * @param dataSource
     *            data source name
     */
    public void addDataSourceToSetup(String dataSource)
    {
        mPartition.addDataSourceToSetup(dataSource);
    }
    
    /**
     * Adds a data sink to the setup of the partition.
     * 
     * @param dataSink
     *            data sink name
     */
    public void addDataSinkToSetup(String dataSink)
    {
        mPartition.addDataSinkToSetup(dataSink);
    }

    /**
     * Returns the partition that this builder builds.
     * 
     * @return partition
     */
    public Partition getPartition()
    {
        return mPartition;
    }

    /**
     * Adds an external input to the pipeline.
     * 
     * @param source
     *            source operator
     * @param target
     *            target operator
     * @param input
     *            input
     */
    public void addInput(
            Operator source,
            Operator target, 
            TraversableSingleActivityInput input)
    {
        mConnectors.addInput(source, target, input, this);
    }

   /**
    * Adds an external input to the pipeline.
    * 
    * @param source
    *            source operator
    * @param target
    *            target operator
    * @param output
    *            activity output
    */
    public void addOutput(
            Operator source, 
            Operator target, 
            TraversableSingleActivityOutput output)
    {
        mConnectors.addOutput(source, target, output, this);
    }
    
    /**
     * Gets the literal inputs that are associated with activities added when
     * building the given operator.
     * 
     * @param operator operator
     * 
     * @return list of all the literal inputs associated with the operator.
     */
    public List<TraversableSingleActivityInput> getLiteralInputs(
        Operator operator)
    {
        List<TraversableSingleActivityInput> result = 
            new LinkedList<TraversableSingleActivityInput>();
        
        List<Activity> activityList = mOperatorActivities.get(operator);
        if (activityList != null)
        {
            for (Activity activity : activityList)
            {
                GenericActivity genActivity = (GenericActivity) activity;
                
                Map<String, ArrayList<TraversableSingleActivityInput>> inputs =
                    genActivity.getAllInputs();
                
                for( Map.Entry<String,ArrayList<TraversableSingleActivityInput>>
                    inputEntry : inputs.entrySet())
                {
                    for( TraversableSingleActivityInput input :     
                         inputEntry.getValue())
                    {
                        if (input.isLiteral())
                        {
                            result.add(input);
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Gets whether this builder has build the specified operator.
     * 
     * @param op operator
     * 
     * @return <tt>true</tt> if this builder built the operator, <tt>false</tt>
     *         otherwise.
     */
    public boolean hasBuiltOperator(Operator op)
    {
        return mOperatorActivities.containsKey(op);
    }
    
    /**
     * Adds a cross pipeline workflow builder.  These builders will be collected
     * by the controller and executed after normal pipeline workflow builders
     * have been applied.
     * 
     * @param builder cross pipeline workflow builder.
     */
    public void addCrossPipelineWorkflowBuilder(
        CrossPipelineWorkflowBuilder builder)
    {
        mCrossPipelineWorkflowBuilders.add(builder);
    }

    /**
     * Gets all the cross pipeline workflow builders that have been added to 
     * this pipeline workflow builder.
     * 
     * @return list of cross pipeline workflow builders.
     */
    public List<CrossPipelineWorkflowBuilder> getCrossPipelineWorkflowBuilders()
    {
        return mCrossPipelineWorkflowBuilders;
    }
}
