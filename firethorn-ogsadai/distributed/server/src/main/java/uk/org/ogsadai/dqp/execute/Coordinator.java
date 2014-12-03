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

package uk.org.ogsadai.dqp.execute;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.execute.CoordinatorExtension.Type;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.dqp.execute.partition.PartitionBuilder;
import uk.org.ogsadai.dqp.execute.partition.PartitionFactory;
import uk.org.ogsadai.dqp.execute.partition.PartitionEventListener;
import uk.org.ogsadai.dqp.execute.workflow.ConnectorMap;
import uk.org.ogsadai.dqp.execute.workflow.CrossPipelineWorkflowBuilder;
import uk.org.ogsadai.dqp.execute.workflow.PipelineWorkflowBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.exception.RequestProcessingException;
import uk.org.ogsadai.exception.RequestTerminatedException;
import uk.org.ogsadai.exception.RequestUserException;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;
import uk.org.ogsadai.resource.dataresource.dqp.SimpleRequestDQPFederation;
import uk.org.ogsadai.util.concurrency.TaskProcessingService;

/**
 * Coordinates construction and execution of a DQP query plan.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class Coordinator
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(Coordinator.class);
    
    /** Result cardinality context key prefix. */
    public static final String CARD_CTX_KEY_PREFIX = "dqp.cardinality";

    /** DQP resource accessor. */
    private DQPResourceAccessor mResourceAccessor;
    /** DQP federation. */
    private SimpleRequestDQPFederation mRequestFederation;
    /** Partition factory. */
    private PartitionFactory mPartitionFactory;
    /** Task processing service. */
    private TaskProcessingService mTaskProcessingService;
    /** Compiler configuration. */
    private CompilerConfiguration mCompilerConfig;
    /** Request resource id. */
    private ResourceID mRequestResourceID;
    
    /**
     * Sets request resource ID.
     * 
     * @param resourceID
     *            resource ID
     */
    public void setRequestResourceID(ResourceID resourceID)
    {
        mRequestResourceID = resourceID;
    }
    
    /**
     * Sets the DQP resource accessor.
     * 
     * @param resourceAccessor 
     *            resource accessor.
     */
    public void setResourceAccessor(DQPResourceAccessor resourceAccessor)
    {
        mResourceAccessor = resourceAccessor;
    }
    
    /**
     * Sets the task processing service to which the partition are submitted for
     * execution.
     * 
     * @param service
     *            task processing service
     */
    public void setTaskProcessingService(TaskProcessingService service)
    {
        mTaskProcessingService = service;
    }

    /**
     * Sets the partition factory.
     * 
     * @param factory
     *            partition factory
     */
    public void setPartitionFactory(PartitionFactory factory)
    {
        mPartitionFactory = factory;
    }

    /**
     * Sets the DQP federation.
     * 
     * @param federation
     *            dqp federation
     */
    public void setRequestFederation(SimpleRequestDQPFederation federation)
    {
        mRequestFederation = federation;
    }

    /**
     * Sets the compiler configuration.
     * 
     * @param config
     *            compiler configuration
     */
    public void setCompilerConfiguration(CompilerConfiguration config)
    {
        mCompilerConfig = config;
    }

    /**
     * Constructs a query plan and executes it. Registers estimated cardinality
     * in the OGSA-DAI context with dynamically constructed key matching
     * <code>dqp.cardinality.{REQUEST_RESOURCE_ID}</code>
     * 
     * @param sql
     *            SQL query
     * @param requestDetails
     *            details relating to the request.
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
     */
    public void execute(final String sql, final RequestDetails requestDetails)
        throws ActivityUserException, ActivityProcessingException,
        ActivityTerminatedException
    {
        Operator root;
        Set<Partition> partitions = null;
        try
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Query: " + sql);
            }
            QueryPlanBuilder queryPlanBuilder = 
                mCompilerConfig.getQueryPlanBuilder();
            root = queryPlanBuilder.buildQueryPlan(
                sql, mResourceAccessor, requestDetails);
            LOG.debug("Built validated LQP");
            root = optimiseLQP(root, requestDetails);
            LOG.debug("Applied optimisers");
        }
        catch (ActivityUserException e)
        {
            throw (ActivityUserException) e;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            throw new ActivityProcessingException(e);
        }
        try
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Query plan: " + root);
            }
            PartitionBuilder partitioning = new PartitionBuilder(
                mResourceAccessor, mPartitionFactory, requestDetails);
            root = partitioning.buildAndAnnotate(root);
            partitions = partitioning.getPartitions();
            Set<Callable> tasks = new HashSet<Callable>();
            List<PipelineWorkflowBuilder> builders = 
                new LinkedList<PipelineWorkflowBuilder>();
            ConnectorMap connectors = new ConnectorMap(mResourceAccessor);
            
            for (final Partition partition : partitions)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Partition: " + partition);
                }
                PipelineWorkflowBuilder builder =
                    new PipelineWorkflowBuilder(partition, connectors);
                builders.add(builder);
                builder.setConfiguration(mCompilerConfig);
                partition.buildWorkflow(builder);
                LOG.debug("Built workflow.");
            }
            
            // Apply any cross pipeline workflow builders
            for (final PipelineWorkflowBuilder builder: builders)
            {
                List<CrossPipelineWorkflowBuilder> crossPipelineBuilders =
                    builder.getCrossPipelineWorkflowBuilders();

                for (CrossPipelineWorkflowBuilder crossPipelineBuilder :
                     crossPipelineBuilders)
                {
                    crossPipelineBuilder.build(builders);
                }
            }
            
            for (final Partition partition : partitions)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Partition: " + partition);
                }
                LOG.debug("Executing setup ... ");
                partition.executeSetup();
                LOG.debug("Done setting up.");
                tasks.add(createPartitionCallable(partition));                    
            }
            LOG.debug("Submitting processes to task service");
            
            processCoordinatorExtensions(CoordinatorExtension.Type.PRE,
                partitions, requestDetails);
            
            mTaskProcessingService.processAll(tasks, true);
            LOG.debug("Done executing all tasks.");

            processCoordinatorExtensions(CoordinatorExtension.Type.POST,
                partitions, requestDetails);
        }
        catch (ActivityConstructionException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (ExecutionException e)
        {
            throw new ActivityUserException(e.getCause());
        }
        catch (RequestProcessingException e)
        {
            throw new ActivityProcessingException(e.getCause());
        }
        catch (RequestTerminatedException e)
        {
            throw new ActivityTerminatedException();
        }
        catch (RequestUserException e)
        {
            throw new ActivityProcessingException(e.getCause());
        }
        catch (ExtensionProcessingException e)
        {
            throw new ActivityProcessingException(e);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            throw new ActivityProcessingException(e);
        }
        finally
        {
            LOG.debug("Cleaning up.");
            cleanup(partitions);
            LOG.debug("Cleaned up.");
        }
    }

    /**
     * Processes coordinator extensions of a given type.
     * 
     * @param type
     *            type of extensions to be processed
     * @param partitions
     * @param requestDetails
     * 
     * @throws ExtensionProcessingException
     */
    private void processCoordinatorExtensions(Type type,
        Set<Partition> partitions, RequestDetails requestDetails)
        throws ExtensionProcessingException
    {
        if (mCompilerConfig.getCoordinatorExtensions() != null)
        {
            for (CoordinatorExtension ext : mCompilerConfig
                .getCoordinatorExtensions())
            {
                if (ext.getType() == type)
                {
                    ext.process(partitions, requestDetails);
                }
            }
        }
    }
    
    /**
     * Optimises LQP.
     * 
     * @param rootOperator
     *            root operator of the query plan to be optimised
     * @param requestDetails
     *            details relating to the request.
     * @return optimised query plan
     * @throws ActivityUserException
     */
    private Operator optimiseLQP(
        Operator rootOperator, RequestDetails requestDetails)
        throws ActivityUserException
    {
        List<Optimiser> optChain = mCompilerConfig.getOptimisationChain();
        Operator currentRoot = rootOperator;
        Optimiser currentOpt = null;
        try
        {
            for (Optimiser opt : optChain)
            {
                currentOpt = opt;
                currentRoot = opt.optimise(
                    currentRoot, 
                    mRequestFederation,
                    mCompilerConfig,
                    requestDetails);
                
                // ensure that LQPs are validated after each optimisation - this
                // helps to detect optimiser defects
                currentRoot.validate();
            }
        }
        catch (LQPException e)
        {
            if (LOG.isDebugEnabled())
            {
                String optName = (currentOpt == null) ? "" : currentOpt
                    .getClass().getCanonicalName();
                
                LOG.debug("Problems with " + optName + " optimiser\n");

            }
            throw new ActivityUserException(e);
        }

        return currentRoot;
    }

    /**
     * Creates a callable object that will execute partition.
     * 
     * @param partition
     *            partition to execute
     * @return
     */
    private Callable createPartitionCallable(final Partition partition)
    {
        Callable task = new Callable()
        {
            public Object call() throws Exception
            {
                try
                {
                    partition.execute();
                }
                catch (ExecutionException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("EXECUTION EXCEPTION: " + partition);
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        LOG.debug(sw.toString());
                    }
                    throw e;
                }
                LOG.debug(partition + ": EXECUTE COMPLETED.");
                return null;
            }
        };
        return task;
    }

    /**
     * Calls <code>Partition#cleanup()</code> on all partitions in a set.
     * 
     * @param partitions
     *            set of partitions to be cleaned up
     */
    private void cleanup(final Set<Partition> partitions)
    {
        if (partitions == null)
        {
            return;
        }
        for (Partition partition : partitions)
        {
            partition.cleanup();
        }
    }

}
