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
package uk.ac.roe.wfau.firethorn.ogsadai.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.Test;

import java.net.URL;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;

import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.ServerProxy;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;

import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;

import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;

import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;

import uk.org.ogsadai.client.toolkit.resource.ResourceFactory;

import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLBulkLoadTuple;

import uk.org.ogsadai.client.toolkit.activities.delivery.WriteToDataSource;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;

import uk.org.ogsadai.client.toolkit.activities.transform.TupleToCSV;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToByteArrays;

/**
 *
 *
 */
public class SimpleQueryTestCase
    {

    /**
     * Our debug logger.
     * 
     */
    private static Log log = LogFactory.getLog(SimpleQueryTestCase.class);

    /**
     * Our test service endpoint.
     * 
     */
    public static final String endpoint = "http://localhost:8081/ogsadai/services/" ;

    /**
     * Simple test for our direct JDBC service.
     *
     */
    @Test
    public void test001()
    throws Exception
        {

        String dataset = "twomass" ;
        String sql     = 
            " SELECT" +
            "    COUNT(pts_key)" +
            " FROM" +
            "    twomass_psc" +
            " WHERE" +
            "    ra  BETWEEN '55.0' AND '59.9'" +
            " AND" +
            "    dec BETWEEN '20.0' AND '24.9'"
            ;

        //
        // Get server.
        Server server = new JerseyServer();        
        //Server server = new ServerProxy();
        server.setDefaultBaseServicesURL(
            new URL(
                endpoint
                )
            );

        //
        // Get DRER.
        DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                "DataRequestExecutionResource"
                )
            );

        //
        // SQL query.
        SQLQuery query = new SQLQuery();
        query.setResourceID(
            dataset
            );
        query.addExpression(
            sql
            );

        //
        // Transform.        
        TupleToByteArrays tupleToByteArrays = new TupleToByteArrays();
        tupleToByteArrays.connectDataInput(
            query.getDataOutput()
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
        pipeline.add(query);
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
                log.error("Exception [" + ouch.getClass().getName() + "][" + ouch.getMessage() + "]");
                ouch = ouch.getCause();
                }
            }

        //
        // Get request status.
        RequestStatus status = requestResource.getRequestStatus();
        log.debug(status.getExecutionStatus());

        //
        // Get our results.
        if (tupleToByteArrays.hasNextResult())
            {
            // Get ResultSet.
            ResultSet rs = tupleToByteArrays.nextResultAsResultSet();
            // Get ResultSet meta data.
            ResultSetMetaData md = rs.getMetaData();

            // Get column names and initial column widths.
            int numColumns = md.getColumnCount();
            log.info("");
            // Get ResultSet rows.
            while (rs.next()) 
                {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < numColumns; i++)
                    {
                    Object field = rs.getObject(i + 1);
                    if (field == null)
                        {
                        builder.append(
                            pad(
                                "null",
                                10
                                )
                            );
                        }
                    else {
                        builder.append(
                            pad(
                                field.toString(),
                                10
                                )
                            );
                        }

                    builder.append(" ");
                    }
                log.info(
                    builder.toString()
                    );
                }
            rs.close();
            }
        }

    /**
     * Pad a string out to a given width with space characters.
     *
     */
    public static String pad(String base, int width)
        {
        StringBuffer baseBuffer = new StringBuffer(base);
        int padLength = width - base.length();
        for (int i = 0; i < padLength; i++)
            {
            baseBuffer.append(" ");
            }
        return baseBuffer.toString();
        }
    }
