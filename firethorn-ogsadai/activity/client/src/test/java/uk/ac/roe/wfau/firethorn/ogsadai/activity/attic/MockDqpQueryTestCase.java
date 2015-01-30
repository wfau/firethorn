/*
 *
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.attic;

import org.junit.Test;


/**
 *
 *
 */
public class MockDqpQueryTestCase
extends SimpleQueryTestBase
    {

    /**
     * Our test service endpoint.
     *
     */
    public static final String endpoint = "http://localhost:8081/albert/services" ;

    /**
     * TWOMASS and UKIDSSDR1, using single JDBC connection.
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        execute(
            endpoint,
            "wfau",
            " SELECT" +
            "    twomass.ra AS tmra," +
            "    ukidss.ra  AS ukra,"  +
            "    (twomass.ra - ukidss.ra) AS difra," +
            "    twomass.dec AS tmdec," +
            "    ukidss.dec  AS ukdec,"  +
            "    (twomass.ra - ukidss.ra) AS difdec," +
            "    neighbour.distanceMins AS dist" +
            " FROM" +
            "    TWOMASS.dbo.twomass_psc AS twomass," +
            "    UKIDSSDR5PLUS.dbo.gcsPointSource AS ukidss," +
            "    UKIDSSDR5PLUS.dbo.gcsSourceXtwomass_psc AS neighbour" +
            " WHERE" +
            "    ukidss.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    ukidss.dec BETWEEN '20.0' AND '22.9'" +
            " AND" +
            "    neighbour.masterObjID = ukidss.sourceID" +
            " AND" +
            "    neighbour.slaveObjID = twomass.pts_key" +
            " AND" +
            "    neighbour.distanceMins < 1E-3"
            );
        }

    /**
     * TWOMASS and UKIDSSDR1, using DQP, using BETWEEN.
     *
     */
    @Test
    public void test002()
    throws Exception
        {
        execute(
            endpoint,
            "mockdqp",
            " SELECT" +
            "    twomass.ra AS tmra," +
            "    ukidss.ra  AS ukra,"  +
            "    (twomass.ra - ukidss.ra) AS difra," +
            "    twomass.dec AS tmdec," +
            "    ukidss.dec  AS ukdec,"  +
            "    (twomass.ra - ukidss.ra) AS difdec," +
            "    neighbour.distanceMins AS dist" +
            " FROM" +
            "    table20 AS twomass," +
            "    table21 AS ukidss," +
            "    table22 AS neighbour" +
            " WHERE" +
            "    twomass.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    twomass.dec BETWEEN '20.0' AND '22.9'" +
            " AND" +
            "    ukidss.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    ukidss.dec BETWEEN '20.0' AND '22.9'" +
            " AND" +
            "    neighbour.masterObjID = ukidss.sourceID" +
            " AND" +
            "    neighbour.slaveObjID = twomass.pts_key" +
            " AND" +
            "    neighbour.distanceMins < 1E-3"
            );
        }

    /**
     * TWOMASS and UKIDSSDR1, using DQP, using GT and LT on both tables.
     *
     */
    @Test
    public void test003()
    throws Exception
        {
        execute(
            endpoint,
            "mockdqp",
            " SELECT" +
            "    twomass.ra AS tmra," +
            "    ukidss.ra  AS ukra,"  +
            "    (twomass.ra - ukidss.ra) AS difra," +
            "    twomass.dec AS tmdec," +
            "    ukidss.dec  AS ukdec,"  +
            "    (twomass.ra - ukidss.ra) AS difdec," +
            "    neighbour.distanceMins AS dist" +
            " FROM" +
            "    table20 AS twomass," +
            "    table21 AS ukidss," +
            "    table22 AS neighbour" +
            " WHERE" +

            "    twomass.ra  >= '55.0'" +
            " AND" +
            "    twomass.ra  <= '55.9'" +
            " AND" +
            "    twomass.dec >= '20.0'" +
            " AND" +
            "    twomass.dec <= '22.9'" +
            " AND" +

            "    ukidss.ra  >= '55.0'" +
            " AND" +
            "    ukidss.ra  <= '55.9'" +
            " AND" +
            "    ukidss.dec >= '20.0'" +
            " AND" +
            "    ukidss.dec <= '22.9'" +

            " AND" +
            "    neighbour.masterObjID = ukidss.sourceID" +
            " AND" +
            "    neighbour.slaveObjID = twomass.pts_key" +
            " AND" +
            "    neighbour.distanceMins < 1E-3"
            );
        }
    
    /**
     * ATLASDR1, using single JDBC connection.
     *
     */
    @Test
    public void test004()
    throws Exception
        {
        execute(
            endpoint,
            "atlas",
            " SELECT (POWER(filterID,2)*2) as p2 from Filter" 
            
            );
        }
    }

