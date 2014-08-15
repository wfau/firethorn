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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcInsertDataClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc.JdbcSelectDataClient;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 * OGSA-DAI client workflow to process a query.
 *
 */
@Slf4j
public class PipelineClient
    {
    public static final String DRER_IDENT = "DataRequestExecutionResource" ;

    private final URL endpoint ;

    /**
     * Public constructor.
     *
     * @param endpoint The OGSA-DAI service endpoint URL.
     *
     */
    public PipelineClient(final URL endpoint)
        {
        this.endpoint = endpoint ;
        }

    /**
     * Execute the pipeline, using the supplied parameters. 
     * 
     * @param param The pipeline parameters
     * @return The PipelineResult.
     *
     */
    public PipelineResult execute(final PipelineParam param)
        {
        //
        // Our ogsadai client.
        final Server server = new JerseyServer();
        server.setDefaultBaseServicesURL(
            this.endpoint
            );
        //
        // Lookup our DRER.
        final DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                DRER_IDENT
                )
            );
        //
        // Create our pipeline.
        final PipelineWorkflow pipeline = new PipelineWorkflow();

        //
        // Add our SQLQuery Activity.
/*
 * 
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
 * 
 */
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

/*
 * Create table.
 * 
 */
        
        //
        // Add our Delays Activity.
        final DelaysClient delay = new DelaysClient(
            select.results(),
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
/*
 * Row number column.
 * 
 */
	    //
        // Create our Insert Activity.
        final JdbcInsertDataClient insert = new JdbcInsertDataClient(
            limits.output(),
            param.insert()
            );
        pipeline.add(
            insert
            );
        
        //
        // Create our delivery handler.
        final DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        pipeline.add(
            delivery
            );
        delivery.connectInput(
            insert.results()
            );
        //
        // Execute our pipeline.
        try {
            final RequestResource request = drer.execute(
                pipeline,
                RequestExecutionType.SYNCHRONOUS
                );
            result(
                request.getRequestExecutionStatus()
                );
            }
        catch (final Exception ouch)
            {
            log.debug("Exception during request processing [{}]", ouch);
            result(
                ouch
                );
            }
        return this.result;
        }

    private PipelineResult result ;
    public PipelineResult result()
        {
        return this.result;
        }

    private void result(final RequestExecutionStatus status)
        {
        if (RequestExecutionStatus.COMPLETED.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.COMPLETED
                );
            }
        else if (RequestExecutionStatus.COMPLETED_WITH_ERROR.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.FAILED
                );
            }
        else if (RequestExecutionStatus.TERMINATED.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.CANCELLED
                );
            }
        else if (RequestExecutionStatus.ERROR.equals(status))
            {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.FAILED
                );
            }
        else {
            this.result = new PipelineResultImpl(
                PipelineResult.Result.FAILED,
                "Unknown RequestExecutionStatus [" + status + "]"
                );
            }
        }

    private void result(final Throwable ouch)
        {
        this.result = new PipelineResultImpl(
            ouch
            );
        }

    private static class PipelineResultImpl
    implements PipelineResult
        {

        protected PipelineResultImpl(final Result result)
            {
            this(
                result,
                null,
                null
                );
            }

        protected PipelineResultImpl(final Result result, final String message)
            {
            this(
                result,
                message,
                null
                );
            }

        protected PipelineResultImpl(final Throwable cause)
            {
            this(
                Result.FAILED,
                cause.getMessage(),
                cause
                );
            }

        protected PipelineResultImpl(final Result result, final String message, final Throwable cause)
            {
            this.cause   = cause   ;
            this.result  = result  ;
            this.message = message ;
            }

        private final String message;
        @Override
        public String message()
            {
            return this.message;
            }

        private final Throwable cause;
        @Override
        public Throwable cause()
            {
            return this.cause;
            }

        private final Result result;
        @Override
        public Result result()
            {
            return this.result;
            }
        }
    }

