/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToByteArrays;
import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;


/**
 *
 *
 */
@Slf4j
public class SimpleClient
    {

    /**
     * Public constructor.
     *
     */
    public SimpleClient(String endpoint)
        {
        this.endpoint = endpoint ;
        }

    private String endpoint;

    /**
     * Simple test for OGSA-DAI queries.
     *
     */
    public ResultSet execute(String dataset, String query)
    throws Exception
        {
        //
        // Create our server client.
        Server server = new JerseyServer();        
        server.setDefaultBaseServicesURL(
            new URL(
                this.endpoint
                )
            );

        //
        // DRER.
        DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                "DataRequestExecutionResource"
                )
            );

        //
        // SQL query.
        SQLQuery sqlquery = new SQLQuery();
        sqlquery.setResourceID(
            dataset
            );
        sqlquery.addExpression(
            query
            );

        //
        // Transform.        
        TupleToByteArrays tupleToByteArrays = new TupleToByteArrays();
        tupleToByteArrays.connectDataInput(
            sqlquery.getDataOutput()
            );
        tupleToByteArrays.addSize(
            20
            );

        //
        // Delivery.
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(
            tupleToByteArrays.getResultOutput()
            );

        //
        // Pipeline workflow.
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(sqlquery);
        pipeline.add(tupleToByteArrays);
        pipeline.add(deliverToRequestStatus);

        //
        // Execute the workflow.
        RequestResource requestResource = null;
        try {
            requestResource = drer.execute(
                pipeline,
                RequestExecutionType.SYNCHRONOUS
                );
            }

        catch (Throwable ouch)
            {
            log.error("Exception executing query");
            while (ouch != null)
                {
                log.error("Exception [{}][{}]", ouch.getClass(), ouch.getMessage());
                ouch = ouch.getCause();
                }
            return null ;
            }

        //
        // Get request status.
        RequestStatus status = requestResource.getRequestStatus();
        log.debug("Status [{}]", status.getExecutionStatus());

        //
        // Get our results.
        if (tupleToByteArrays.hasNextResult())
            {
            return tupleToByteArrays.nextResultAsResultSet();
            }
        else {
            return null ;
            }
        }
    }
