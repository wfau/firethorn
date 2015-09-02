/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.blue;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.SimpleWorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.WorkflowResult;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;

/**
 * OGSA-DAI client workflow to process a query.
 *
 */
@Slf4j
public class BlueWorkflowClient
implements BlueWorkflow
    {
    
    @Deprecated
    protected static final String DEFAULT_DRER_IDENT = "DataRequestExecutionResource" ;

    /**
     * Our OGSA-DAI service endpoint.
     * 
     */
    private final String endpoint ;
    
    /**
     * Our OGSA-DAI DataRequestExecutionResource identifier.
     * 
     */
    private final String drerid ;

    /**
     * Deprecated constructor.
     *
     * @param endpoint The OGSA-DAI service endpoint URL.
     *
     */
    @Deprecated
    public BlueWorkflowClient(final String endpoint)
        {
    	this(
			endpoint,
			DEFAULT_DRER_IDENT
			);
        }

    /**
     * Public constructor.
     *
     * @param endpoint The OGSA-DAI service endpoint URL.
     * @param endpoint The OGSA-DAI DataRequestExecutionResource identifier.
     *
     */
    public BlueWorkflowClient(final String endpoint, final String drerid)
        {
        this.drerid = drerid;
        this.endpoint = endpoint ;
        }
    
    /**
     * Execute the pipeline. 
     * 
     * @param param The pipeline parameters
     * @return The PipelineResult.
     *
     */
    public WorkflowResult execute(final Param param)
        {
        //
        // Our ogsadai server client.
        final Server server = new JerseyServer();
        try {
			server.setDefaultBaseServicesURL(
			    new URL(
					this.endpoint
					)
			    );
			}
		catch (final MalformedURLException ouch)
			{
            log.debug("MalformedURLException [{}]", ouch);
            return new SimpleWorkflowResult(
                ouch
                );
			}
        //
        // Lookup our DRER.
        final DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                this.drerid
                )
            );
        //
        // Create our pipeline.
        final PipelineWorkflow pipeline = new PipelineWorkflow();
        //
        // Add our SQLQuery Activity.
        final SQLQuery select = new SQLQuery();
        pipeline.add(
            select
            );
        select.setResourceID(
            new ResourceID(
                param.source()
                )
            );
        select.addExpression(
            param.query()
            );
        /*
         * 
        final JdbcSelectDataClient select = new JdbcSelectDataClient(
            new ResourceID(
                param.source()
                ),
            new JdbcSelectDataClient.Param()
                {
                @Override
                public String query()
                    {
                    return param.query();
                    }
                }
            );
        pipeline.add(
            select
            );
         * 
         */
        //
        // Add our Delays Activity.
        final DelaysClient delay = new DelaysClient(
            select.getDataOutput(),
            param.delays()
            );
        pipeline.add(
            delay
            );
        //
        // Add our Limits Activity.
        final LimitsClient limits = new LimitsClient(
            delay.output(),
            param.limits()
            );
        pipeline.add(
            limits
            );
        //
        // Add our Insert Activity.
        final JdbcInsertDataClient insert = new JdbcInsertDataClient(
            limits.output(),
            param.insert()
            );
        pipeline.add(
            insert
            );
        //
        // Add our delivery handler.
        final DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        pipeline.add(
            delivery
            );
        delivery.connectInput(
            insert.results()
            );
        //
        // Start our workflow running.
        try {
            final RequestResource request = drer.execute(
                pipeline,
                RequestExecutionType.ASYNCHRONOUS
                );
            return new SimpleWorkflowResult(
                request
                );
            }
        //
        // Catch any synchronous exceptions.
        catch (final Exception ouch)
            {
            log.debug("Exception during request processing [{}]", ouch);
            return new SimpleWorkflowResult(
                ouch
                );
            }
        }
    }

