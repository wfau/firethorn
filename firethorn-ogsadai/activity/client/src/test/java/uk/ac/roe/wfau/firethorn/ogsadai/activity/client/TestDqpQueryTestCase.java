/*

 *
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

/**
 *
 *
 */
@Slf4j
public class TestDqpQueryTestCase
extends SimpleQueryTestBase
    {

    /**
     * TWOMASS and UKIDSSDR1, using test DQP, using GT and LT on both tables.
     *
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        execute(
            endpoint,
            "testdqp",
            " SELECT" +
            "    twomass.ra AS tmra," +
            "    ukidss.ra  AS ukra,"  +
            "    (twomass.ra - ukidss.ra) AS difra," +
            "    twomass.dec AS tmdec," +
            "    ukidss.dec  AS ukdec,"  +
            "    (twomass.ra - ukidss.ra) AS difdec," +
            "    neighbour.distanceMins AS dist" +
            " FROM" +
            "     JDBC_5   AS twomass," +
            "     JDBC_120 AS ukidss," +
            "     JDBC_41  AS neighbour" +
            " WHERE" +
            "    twomass.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    twomass.dec BETWEEN '20.0' AND '22.9'" +
            " AND" +
            "    ukidss.ra   BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    ukidss.dec  BETWEEN '20.0' AND '22.9'" +
            " AND" +
            "    neighbour.masterObjID = ukidss.sourceID" +
            " AND" +
            "    neighbour.slaveObjID = twomass.pts_key" +
            " AND" +
            "    neighbour.distanceMins < 1E-3"

            );
        }
    }

