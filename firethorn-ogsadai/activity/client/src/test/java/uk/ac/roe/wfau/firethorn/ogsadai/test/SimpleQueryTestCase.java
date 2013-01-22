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
    public static final String endpoint = "http://localhost:8081/albert/services/" ;

    /**
     * Simple test for our direct JDBC service.
     *
     */
    public void execute(String endpoint, String dataset, String query)
    throws Exception
        {
        //
        // Create our server client.
        Server server = new JerseyServer();        
        server.setDefaultBaseServicesURL(
            new URL(
                endpoint
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
                                "null"
                                )
                            );
                        }
                    else {
                        builder.append(
                            pad(
                                field.toString()
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
     * Single catalog, TWOMASS.
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        execute(
            endpoint,
            "twomass",
            " SELECT" +
            "    twomass.ra," +
            "    twomass.dec" +
            " FROM" +
            "    TWOMASS.dbo.twomass_psc AS twomass" +
            " WHERE" +
            "    twomass.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    twomass.dec BETWEEN '20.0' AND '22.9'"
            );
        }

    /**
     * Single catalog, UKIDSSDR1.
     *
     */
    @Test
    public void test002()
    throws Exception
        {
        execute(
            endpoint,
            "twomass",
            " SELECT" +
            "    ukidss.ra," +
            "    ukidss.dec" +
            " FROM" +
            "    UKIDSSDR5PLUS.dbo.gcsPointSource AS ukidss" +
            " WHERE" +
            "    ukidss.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    ukidss.dec BETWEEN '20.0' AND '22.9'"
            );
        }

    /**
     * Multiple catalog, TWOMASS, UKIDSSDR1.
     *
     */
    @Test
    public void test003()
    throws Exception
        {
        execute(
            endpoint,
            "twomass",
            " SELECT" +
            "    twomass.ra AS tmra," +
            "    ukidss.ra  AS ukra,"  +
            "    (twomass.ra - ukidss.ra) AS difra," +
            "    twomass.dec AS tmdec," +
            "    ukidss.dec  AS ukdec,"  +
            "    (twomass.ra - ukidss.ra) AS difdec," +
            "    match.distanceMins AS dist" +
            " FROM" +
            "    TWOMASS.dbo.twomass_psc AS twomass," +
            "    UKIDSSDR5PLUS.dbo.gcsPointSource AS ukidss," +
            "    UKIDSSDR5PLUS.dbo.gcsSourceXtwomass_psc AS match" +
            " WHERE" +
            "    ukidss.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    ukidss.dec BETWEEN '20.0' AND '22.9'" +
            " AND" +
            "    match.masterObjID = ukidss.sourceID" +
            " AND" +
            "    match.slaveObjID = twomass.pts_key" +
            " AND" +
            "    match.distanceMins < 1E-3"
            );
        }

    /**
     * Pad a string out to a given width with space characters.
     *
     */
    public static String pad(String base)
        {
        return pad(
            base,
            22
            );
        }

    /**
     * Pad a string out to a given width with space characters.
     *
     */
    public static String pad(String base, int width)
        {
        int count = width - base.length();
        if (count == 0)
            {
            return base ;
            }
        else if (count < 0)
            {
            return base.substring(
                0,
                width
                );
            }
        else {
            StringBuffer buffer = new StringBuffer(
                base
                );
            for (int i = 0; i < count; i++)
                {
                buffer.append(" ");
                }
            return buffer.toString();
            }
        }
    }
