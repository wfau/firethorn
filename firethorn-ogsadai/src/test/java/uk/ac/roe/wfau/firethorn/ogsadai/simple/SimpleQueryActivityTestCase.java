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

//import uk.org.ogsadai.resource.ResourceID;
//import uk.org.ogsadai.resource.request.RequestStatus;

//import uk.org.ogsadai.client.toolkit.Server;
//import uk.org.ogsadai.client.toolkit.ServerProxy;
//import uk.org.ogsadai.client.toolkit.PipelineWorkflow;

//import uk.org.ogsadai.client.toolkit.RequestResource;
//import uk.org.ogsadai.client.toolkit.RequestExecutionType;

//import uk.org.ogsadai.client.toolkit.DataSourceResource;
//import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;

//import uk.org.ogsadai.client.toolkit.resource.ResourceFactory;

//import uk.org.ogsadai.client.toolkit.activities.sql.SQLQuery;
//import uk.org.ogsadai.client.toolkit.activities.sql.SQLBulkLoadTuple;

//import uk.org.ogsadai.client.toolkit.activities.delivery.WriteToDataSource;
//import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;

//import uk.org.ogsadai.client.toolkit.activities.transform.TupleToCSV;
//import uk.org.ogsadai.client.toolkit.activities.transform.TupleToByteArrays;

//import org.jiscinvolve.astrodabis.galium.ogsadai.activity.client.ResultProcessing;

public class SimpleQueryActivityTestCase
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(SimpleQueryActivityTestCase.class);

    /**
     * Our test service endpoint.
     * 
     */
    public static final String endpoint = "http://kappa.virtual.metagrid.co.uk:8080/ogsadai/services/" ;

    /**
     * Generate a job ident..
     * @todo This is just a placeholder for now.
     *
     */
    protected String identifier()
        {
        StringBuilder builder = new StringBuilder();
        builder.append("results");
        builder.append(
            System.currentTimeMillis()
            );
        return builder.toString();
        }

    /**
     * Run a query, process the results and store them in our results table.
     *
     */
    @Test
    public void frog002()
    throws Exception
        {

        String dataset   = "twomass" ;
      //String statement = "SELECT * FROM psc LIMIT 5" ;
        String statement = "SELECT * FROM psc" ;
        String resource  = "results" ;
        String tablename = "test_005" ;

        //
        // Create our query params.
        SimpleQueryParam param = new SimpleQueryParamImpl(
            new URL(endpoint),
            resource,
            tablename,
            dataset,
            identifier(),
            statement
            );

        //
        // Create our activity.
        SimpleQueryActivity activity = new SimpleQueryActivityImpl(
            param
            );

        //
        // Run our activity.
        activity.execute();

        //
        // Check the status.
        logger.debug("Activity status [{}]", activity.status());
        }

    }
