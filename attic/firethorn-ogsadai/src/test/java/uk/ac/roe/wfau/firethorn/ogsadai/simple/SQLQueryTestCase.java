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
import java.sql.ResultSetMetaData;

import java.util.Vector;

import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;  

import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestStatus;

import uk.org.ogsadai.client.toolkit.Server;
//import uk.org.ogsadai.client.toolkit.ServerProxy;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;

import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToByteArrays;

import uk.org.ogsadai.client.toolkit.presentation.jersey.JerseyServer;

public class SQLQueryTestCase
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(SQLQueryTestCase.class);

    /**
     * Our test service endpoint.
     * 
     */
    public static final String endpoint = "http://kappa.virtual.metagrid.co.uk:8080/ogsadai/services/" ;

    /**
     * Dummy test to avoid 'no tests' exception.
     *
     */
    @Test
    public void frog00()
    throws Exception
        {
        }

    /**
     * Simple test for our direct JDBC service.
     * Test disabled, as it requires a JDBC connection to local test database. 
     *
     */
    //@Test
    public void frog001()
    throws Exception
        {

        String dataset  = "twomass" ;
        String adql     = "SELECT * FROM psc LIMIT 1000" ;

        //String dataset  = "ukidss" ;
        //String adql     = "SELECT * FROM gcsPointSource" ;

        // Get server.
        Server server = new JerseyServer();
        server.setDefaultBaseServicesURL(
            new URL(
                endpoint
                )
            );

        // Get DRER.
        DataRequestExecutionResource drer = server.getDataRequestExecutionResource(
            new ResourceID(
                "DataRequestExecutionResource"
                )
            );
        //applySecurityToResource(drer);

        // SQL query.
        SQLQuery query = new SQLQuery();
        query.setResourceID(
            dataset
            );
        query.addExpression(
            adql
            );

        // Transform.        
        TupleToByteArrays tupleToByteArrays = new TupleToByteArrays();
        tupleToByteArrays.connectDataInput(
            query.getDataOutput()
            );
        tupleToByteArrays.addSize(
            20
            );

        // Deliver.
        DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
        deliverToRequestStatus.connectInput(
            tupleToByteArrays.getResultOutput()
            );

        // Workflow.
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(query);
        pipeline.add(tupleToByteArrays);
        pipeline.add(deliverToRequestStatus);

        // Execute.
        RequestResource requestResource = null;
        try {
            requestResource = drer.execute(
                pipeline,
                RequestExecutionType.SYNCHRONOUS
                );
            }

        catch (Throwable ouch)
            {
            while (ouch != null)
                {
                logger.error("Exception executing query", ouch);
                ouch = ouch.getCause();
                }
            }

        // Get request status.
        RequestStatus status = requestResource.getRequestStatus();
        logger.debug("Status [{}]", status.getExecutionStatus());

        // Get results.
        if (tupleToByteArrays.hasNextResult())
            {
            // Get ResultSet.
            ResultSet rs = tupleToByteArrays.nextResultAsResultSet();
            // Get ResultSet meta data.
            ResultSetMetaData md = rs.getMetaData();

            // Get column names and initial column widths.
            int numColumns = md.getColumnCount();
            logger.info("");
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
                logger.info(
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
