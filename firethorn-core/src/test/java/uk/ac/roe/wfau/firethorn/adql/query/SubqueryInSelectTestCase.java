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
package uk.ac.roe.wfau.firethorn.adql.query ;

import static org.junit.Assert.*;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;


/**
 *
 *
 */
@Slf4j
public class SubqueryInSelectTestCase
extends AtlasQueryTestBase
    {

    /**
     * Test query Subquery in Select - returns 8778 rows"
     *
     * Query:
     * Select DistanceMins From atlassourcexDR7photoobj as CrossMatch,
	 * (Select s.sourceid as id From atlassource as s Where ra > 182 AND ra < 184
	 * AND dec > -3 AND dec < -1 AND mergedclass =1 group by s.sourceid) AS T
     * Where DistanceMins < 2/60.0 AND sdsstype = 3 AND sdssPrimary = 1 AND T.id = CrossMatch.masterObjID
     *
     */
	@Test
    public void test000()
    throws Exception
        {
        test(
            "SELECT\n" + 
            "    DistanceMins\n" + 
            "FROM\n" + 
            "    atlassourcexDR7photoobj AS CrossMatch, \n" + 
            "    (\n" + 
            "    SELECT\n" + 
            "        s.sourceid as id\n" + 
            "    FROM\n" + 
            "        atlassource as s\n" + 
            "    WHERE\n" + 
            "        ra > 182 AND ra < 184\n" + 
            "    AND\n" + 
            "        dec > -3 AND dec < -1\n" + 
            "    AND\n" + 
            "        mergedclass =1\n" + 
            "    GROUP BY\n" + 
            "        s.sourceid\n" + 
            "    ) AS T\n" + 
            "WHERE\n" + 
            "    DistanceMins < 2/60.0\n" + 
            "AND\n" + 
            "    sdsstype = 3\n" + 
            "AND\n" + 
            "    sdssPrimary = 1\n" + 
            "AND " + 
            "    T.id = CrossMatch.masterObjID\n" + 
            "",

            "SELECT\n" + 
            "    CrossMatch.distanceMins AS DistanceMins\n" + 
            "FROM\n" + 
            "    ATLASv20130426.dbo.atlasSourceXDR7PhotoObj AS CrossMatch ,\n" + 
            "    (SELECT\n" + 
            "        s.sourceID AS id\n" + 
            "    FROM\n" + 
            "        ATLASv20130426.dbo.atlasSource AS s\n" + 
            "    WHERE\n" + 
            "        s.ra > 182 AND s.ra < 184\n" + 
            "    AND\n" + 
            "        s.dec > -3 AND s.dec < -1\n" + 
            "    AND\n" + 
            "        s.mergedClass = 1\n" + 
            "    GROUP BY\n" + 
            "        s.sourceID) AS T\n" + 
            "WHERE\n" + 
            "    CrossMatch.distanceMins < 2/60.0\n" + 
            "AND\n" + 
            "    CrossMatch.sdssType = 3\n" + 
            "AND\n" + 
            "    CrossMatch.sdssPrimary = 1\n" + 
            "AND\n" + 
            "    T.id = CrossMatch.masterObjID\"\n" + 
            ""
            );
        }


    /**
     * Test query Subquery in From - returns 2876 rows"
     *
     * Query:
     * Select DistanceMins From (Select Top 10000 * From atlassourcexDR7photoobj Where sdssPrimary = 1) as Crossmatch,
     * Where DistanceMins < 2/60.0 AND sdsstype = 3
     *
     */
    private static final String QUERY_001 =

          "SELECT"
        + "    DistanceMins"
        + " FROM"
        + "		(Select Top 10000 * From atlassourcexDR7photoobj Where sdssPrimary = 1) as Crossmatch"
        + " WHERE"
        + "   DistanceMins < 2/60.0"
        + " AND"
        + "    sdsstype = 3"
        + ""
        ;

    @Test
    public void test001()
    throws Exception
        {
        final AdqlQuery query = this.schema.queries().create(
            QUERY_001
            );
        debug(query);
        assertEquals(
            "FROG",
            query.osql().replace('\n', ' ')
            );
        }

    /**
     * Test query Subquery in Where - returns 32378 rows
     *
     *  Query:
     *  Select DistanceMins From atlassourcexDR7photoobj as CrossMatch
     *  Where
     *	(Select s.sourceid From atlassource as s Where ra > 182 AND ra < 184 AND dec > -3 AND dec < -1 AND CrossMatch.masterOjbID = sourceID)>0
     *
     */
    private static final String QUERY_002 =

          "SELECT"
        + "    DistanceMins"
        + " FROM"
        + "    atlassourcexDR7photoobj as CrossMatch"
        + " WHERE"
        + "    (Select s.sourceid From atlassource as s Where ra > 182 AND ra < 184 AND dec > -3 AND dec < -1 AND CrossMatch.masterOjbID = sourceID)>0"
        + ""
        ;

    @Test
    public void test002()
    throws Exception
        {
        final AdqlQuery query = this.schema.queries().create(
            QUERY_002
            );
        debug(query);
        assertEquals(
            "FROG",
            query.osql().replace('\n', ' ')
            );
        }
    }

