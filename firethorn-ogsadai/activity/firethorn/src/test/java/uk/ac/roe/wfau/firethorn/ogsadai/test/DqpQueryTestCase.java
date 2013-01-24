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
public class DqpQueryTestCase
extends SimpleQueryTestBase
    {

    /**
     * Our debug logger.
     * 
     */
    private static Log log = LogFactory.getLog(DqpQueryTestCase.class);

    /**
     * Our test service endpoint.
     * 
     */
    public static final String endpoint = "http://localhost:8081/albert/services/" ;

    /**
     * Multiple catalog, TWOMASS and UKIDSSDR1, using JDBC direct.
     *
     */
    //@Test
    public void test001()
    throws Exception
        {
        execute(
            endpoint,
            "uber",
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
     * Multiple catalog, TWOMASS and UKIDSSDR1, using DQP.
     *
     */
    @Test
    public void test002()
    throws Exception
        {
        execute(
            endpoint,
            "mydqp",
            " SELECT" +
            "    twomass.ra AS tmra," +
            "    ukidss.ra  AS ukra,"  +
            "    (twomass.ra - ukidss.ra) AS difra," +
            "    twomass.dec AS tmdec," +
            "    ukidss.dec  AS ukdec,"  +
            "    (twomass.ra - ukidss.ra) AS difdec," +
            "    match.distanceMins AS dist" +
            " FROM" +
            "    twomass_dbo.twomass_psc   AS twomass," +
            "    ukidss_dbo.gcsPointSource AS ukidss," +
            "    ukidss_dbo.gcsSourceXtwomass_psc AS match" +
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
    }

