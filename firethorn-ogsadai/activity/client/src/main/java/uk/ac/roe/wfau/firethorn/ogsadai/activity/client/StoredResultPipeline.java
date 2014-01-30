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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
//import uk.org.ogsadai.client.toolkit.activities.sql.SQLBulkLoadTuple;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

/**
 *
 *
 */
@Slf4j
public class StoredResultPipeline
    {
    public static final String DRER_IDENT = "DataRequestExecutionResource" ;

    private final URL endpoint ;

    public StoredResultPipeline(final URL endpoint)
        {
        this.endpoint = endpoint ;
        }

    public PipelineResult execute(final PipelineParam param)
        {
        //
        // Create our ogsadai client.
        final Server server = new JerseyServer();
        server.setDefaultBaseServicesURL(
            this.endpoint
            );
        //
        // Create our DRER.
        final DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                DRER_IDENT
                )
            );
        //
        // Create our pipeline.
        final PipelineWorkflow pipeline = new PipelineWorkflow();
        //
        // Create our SQL query.
        final SQLQuery selector = new SQLQuery();
        pipeline.add(
            selector
            );
        selector.setResourceID(
            new ResourceID(
                param.source()
                )
            );
        selector.addExpression(
            param.query()
            );
        //
        // Create our test delay.
        final Delay delay = new Delay(
            param.delays()
            );
        pipeline.add(
            delay
            );
        delay.input(
            selector.getDataOutput()
            );
        //
		// Add our row number generator.
		final InsertRowid rowid = new InsertRowid();
        pipeline.add(
            rowid
            );
		rowid.setColname(
			"_rowid"
			);
	    rowid.connectDataInput(
	        delay.output()
	        );
        //
        // Create our results writer.
        final Insert inserter = new Insert();
        pipeline.add(
            inserter
            );
        inserter.resource(
            new ResourceID(
                param.store()
                )
            );
        inserter.table(
            param.table()
            );
        inserter.input(
            rowid.getDataOutput()
            );
/*
 * 
        inserter.first(
            20
            );
        inserter.block(
            2000
            );
 * 
 */
        
        //
        // Create our delivery handler.
        final DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        pipeline.add(
            delivery
            );
        delivery.connectInput(
            inserter.output()
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

