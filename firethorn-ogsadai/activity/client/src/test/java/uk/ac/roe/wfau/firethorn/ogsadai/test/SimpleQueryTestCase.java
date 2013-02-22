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

/**
 *
 *
 */
public class SimpleQueryTestCase
extends SimpleQueryTestBase
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
            "    twomass_psc AS twomass" +
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
            "ukidss",
            " SELECT" +
            "    ukidss.ra," +
            "    ukidss.dec" +
            " FROM" +
            "    gcsPointSource AS ukidss" +
            " WHERE" +
            "    ukidss.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    ukidss.dec BETWEEN '20.0' AND '22.9'"
            );
        }

    /**
     * Multiple catalog, TWOMASS and UKIDSSDR1, using JDBC direct.
     *
     */
    @Test
    public void test003()
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
    }

