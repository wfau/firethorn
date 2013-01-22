/**
 *
 *  Copyright (c) 2011, AstroDAbis
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 *  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 *  OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 *  OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import java.sql.ResultSet;

import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;

import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.ServerProxy;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;

import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLBulkLoadTuple;

import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;

import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.CreateTable;

/**
 * Wrapper class to implement an OGSA-DAI query.
 *
 */
public class SimpleQueryActivityImpl
implements SimpleQueryActivity
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(SimpleQueryActivityImpl.class);

    /**
     * Public constructor.
     * 
     */
    public SimpleQueryActivityImpl(SimpleQueryParam param)
        {
        this.param  = param ;
        this.status = Status.PENDING ;
        }

    /**
     * Our activity parameters.
     * 
     */
    private SimpleQueryParam param ;

    /**
     * Execute our activity.
     * 
     */
    public void execute()
        {
        //
        // Get a server connection.
        Server server = new JerseyServer();
        server.setDefaultBaseServicesURL(
            param.endpoint()
            );
        //
        // DataRequestExecutionResource.
        DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                "DataRequestExecutionResource"
                )
            );
        //
        // SQL query activity.
        SQLQuery query = new SQLQuery();
        query.setResourceID(
            new ResourceID(
                param.dataset()
                )
            );
        query.addExpression(
            param.statement()
            );

        //
        // Create the new table.
        CreateTable create = new CreateTable();
        create.setResourceID(
            new ResourceID(
                param.results().resource()
                )
            );
        create.setTableName(
            param.results().tablename()
            );
        create.connectTuples(
            query.getDataOutput()
            );

        //
        // Results writer.
        SQLBulkLoadTuple writer = new SQLBulkLoadTuple();
        writer.setResourceID(
            new ResourceID(
                param.results().resource()
                )
            );
        writer.addTableName(
            param.results().tablename()
            );
        writer.connectDataInput(
            create.getDataOutput()
            );
        //
        // Delivery.
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(
            writer.getDataOutput()
            );
        //
        // Workflow.
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(query);
        pipeline.add(create);
        pipeline.add(writer);
        pipeline.add(deliverToRequestStatus);
        //
        // Execute.
        try {
            //
            // Execute the request.
            this.status = Status.RUNNING ;
            RequestResource requestResource = drer.execute(
                pipeline,
                RequestExecutionType.SYNCHRONOUS
                );
            //
            // Check the request status.
            try {
                this.status(
                    requestResource.getRequestExecutionStatus()
                    );
                }
            //
            // Log anything that goes wrong.
            catch (Throwable ouch)
                {
                this.status = Status.FAILED ;
                while (ouch != null)
                    {
                    logger.error("Exception reading result status", ouch);
                    ouch = ouch.getCause();
                    }
                }
            }
        //
        // Log anything that goes wrong.
        catch (Throwable ouch)
            {
            this.status = Status.FAILED ;
            while (ouch != null)
                {
                logger.error("Exception executing query", ouch);
                ouch = ouch.getCause();
                }
            }
        }

    /**
     * Our activity status.
     * 
     */
    private SimpleQueryActivity.Status status ;

    /**
     * Our activity status.
     * 
     */
    public SimpleQueryActivity.Status status()
        {
        return this.status ;
        }

    /**
     * Convert an ogsadai RequestExecutionStatus to our Status enum.
     *
     */
    protected void status(RequestExecutionStatus value)
        {
logger.debug("SimpleQueryActivityImpl.status(RequestExecutionStatus)");
logger.debug("  Value [{}]", value);
logger.debug("  Local [{}]", value.getLocalPart());
logger.debug("  Space [{}]", value.getNamespace());

        if (RequestExecutionStatus.COMPLETED.equals(value))
            {
            this.status = SimpleQueryActivity.Status.COMPLETED ;
            }
        else if (RequestExecutionStatus.COMPLETED_WITH_ERROR.equals(value))
            {
            this.status = SimpleQueryActivity.Status.FAILED ;
            }
        else if (RequestExecutionStatus.TERMINATED.equals(value))
            {
            this.status = SimpleQueryActivity.Status.CANCELLED ;
            }
        else if (RequestExecutionStatus.ERROR.equals(value))
            {
            this.status = SimpleQueryActivity.Status.FAILED ;
            }
        else {
            logger.warn("Unknown RequestExecutionStatus value [{}}", value);
            this.status = SimpleQueryActivity.Status.FAILED ;
            }
        }     
    }
