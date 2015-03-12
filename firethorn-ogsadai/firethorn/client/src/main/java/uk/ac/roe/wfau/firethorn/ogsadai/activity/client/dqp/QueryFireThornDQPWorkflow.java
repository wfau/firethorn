/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.dqp;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleResourceWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.PipelineResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.PipelineResult.Result;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.SimplePipelineResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.ogsa.OgsaServiceClient;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Workflow for the CreateFTDQPResource Activity.
 *
 */
@Slf4j
public class QueryFireThornDQPWorkflow
extends BaseWorkflow
    {
    
    public interface Param {
        
        /**
         * The {@link CreateFireThornDQPClient} params. 
         *
         */
        public CreateFireThornDQPClient.Param create();
                 
        /**
         * The SQL select query.
         * 
         */
        public String query();

        }
    
    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @throws MalformedURLException 
     *
     */
    public QueryFireThornDQPWorkflow(final String endpoint)
    throws MalformedURLException
        {
        super(
            endpoint
            );
        }

    /**
     * Public constructor.
     * @param endpoint The OGSA-DAI service endpoint URL.
     *
     */
    public QueryFireThornDQPWorkflow(final URL endpoint)
        {
        super(
            endpoint
            );
        }

    /**
     * Public constructor.
     * @param service Our {@link OgsaServiceClient} client.
     *
     */
    public QueryFireThornDQPWorkflow(final OgsaServiceClient service)
        {
        super(
            service
            );
        }
    
    /**
     * Execute our workflow.
     *   
     */
    public WorkflowResult execute(final Param param)
        {
        //
        // For the production system, we need to implement a MatchedNestedIterativeActivity.
        // Just to test it, two separate Activities.
        final ResourceWorkflowResult create = create(
            param
            ); 

        log.debug("Create status [{}]", create.status());
        if (create.status() != WorkflowResult.Status.COMPLETED)
            {
            return create ;
            }
        else {
            return query(
                create.result(),
                param
                ); 

            // If success .. do we need to delete ?
        
        
            }
        }
    
    protected ResourceWorkflowResult create(final Param param)
        {
        log.debug("Creating DQP [{}]", param.create().target());
        //
        // Create our pipeline.
        final PipelineWorkflow workflow = new PipelineWorkflow();

        final CreateFireThornDQPClient create = new CreateFireThornDQPClient(
            param.create()
            );
        workflow.add(
            create
            );

        //
        // Create our delivery handler.
        DeliverToRequestStatus deliver = new DeliverToRequestStatus();
        workflow.add(
            deliver
            );
        deliver.connectInput(
            create.output()
            );

        //
        // Execute our pipeline.
        try {
            final RequestResource request = service().drer().execute(
                workflow,
                RequestExecutionType.SYNCHRONOUS
                );
            return new SimpleResourceWorkflowResult(
                request,
                new ResourceID(
                    create.output().getDataValueIterator().nextAsString()
                    )
                );
            }
        catch (final Exception ouch)
            {
            log.debug("Exception during query processing [{}]", ouch);
            return new SimpleResourceWorkflowResult(
                ouch
                );
            }
        }
    
    protected WorkflowResult query(final ResourceID resource, final Param param)
        {
        log.debug("Querying DQP [{}]", resource.toString());

        //
        // Create our pipeline.
        final PipelineWorkflow workflow = new PipelineWorkflow();

        //
        // Add our SQLQuery Activity.
        // Use JdbcSelect if we know the target type.
        // Use DQPSelect if we know the target type.
        final SQLQuery select = new SQLQuery();
        workflow.add(
            select
            );
        select.setResourceID(
            resource
            );
        select.addExpression(
            param.query()
            );
        
        //
        // Create our delivery handler.
        final DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        workflow.add(
            delivery
            );
        delivery.connectInput(
            select.getDataOutput()
            );
        //
        // Execute our pipeline.
        try {
            final RequestResource request = service().drer().execute(
                workflow,
                RequestExecutionType.SYNCHRONOUS
                );
            return new SimpleWorkflowResult(
                request
                );
            }
        catch (final Exception ouch)
            {
            log.debug("Exception during query processing [{}]", ouch);
            return new SimpleWorkflowResult(
                ouch
                );
            }
        }
    }
