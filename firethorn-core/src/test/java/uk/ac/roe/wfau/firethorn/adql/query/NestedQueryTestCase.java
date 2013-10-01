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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;


/**
 *
 *
 */
@Slf4j
public class NestedQueryTestCase
extends AtlasQueryTestBase
    {

    /**
     * Test Subquery alias - returns 8778 rows"
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
        compare(
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

            "SELECT CrossMatch.distanceMins AS DistanceMins FROM ATLASv20130426.dbo.atlasSourceXDR7PhotoObj AS CrossMatch , (SELECT s.sourceID AS id FROM ATLASv20130426.dbo.atlasSource AS s WHERE s.ra > 182 AND s.ra < 184 AND s.dec > -3 AND s.dec < -1 AND s.mergedClass = 1 GROUP BY s.sourceID) AS T WHERE CrossMatch.distanceMins < 2/60.0 AND CrossMatch.sdssType = 3 AND CrossMatch.sdssPrimary = 1 AND T.id = CrossMatch.masterObjID"
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
    @Test
    public void test001()
    throws Exception
        {
        compare(
            "SELECT\n" + 
            "    DistanceMins\n" + 
            "FROM\n" + 
            "    (\n" + 
            "    SELECT\n" + 
            "        TOP 10000 *\n" + 
            "    FROM\n" + 
            "        atlassourcexDR7photoobj\n" + 
            "    WHERE\n" + 
            "        sdssPrimary = 1\n" + 
            "    ) AS Crossmatch\n" + 
            "WHERE\n" + 
            "    DistanceMins < 2/60.0\n" + 
            "AND\n" + 
            "    sdsstype = 3\n" + 
            "",
            
            "SELECT Crossmatch.distanceMins AS DistanceMins FROM (SELECT TOP 10000 ATLASv20130426.dbo.atlasSourceXDR7PhotoObj.masterObjID AS masterObjID,ATLASv20130426.dbo.atlasSourceXDR7PhotoObj.slaveObjID AS slaveObjID,ATLASv20130426.dbo.atlasSourceXDR7PhotoObj.distanceMins AS distanceMins,ATLASv20130426.dbo.atlasSourceXDR7PhotoObj.sdssType AS sdssType,ATLASv20130426.dbo.atlasSourceXDR7PhotoObj.sdssPrimary AS sdssPrimary FROM ATLASv20130426.dbo.atlasSourceXDR7PhotoObj WHERE ATLASv20130426.dbo.atlasSourceXDR7PhotoObj.sdssPrimary = 1) AS Crossmatch WHERE Crossmatch.distanceMins < 2/60.0 AND Crossmatch.sdssType = 3"
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
    @Test
    public void test002()
    throws Exception
        {
        compare(
            "SELECT\n" + 
            "    DistanceMins\n" + 
            "FROM\n" + 
            "    atlasSourceXDR7PhotoObj as CrossMatch\n" + 
            "WHERE\n" + 
            "    (\n" + 
            "    SELECT\n" + 
            "        s.sourceID\n" + 
            "    FROM\n" + 
            "        atlasSource as s\n" + 
            "    WHERE\n" + 
            "        ra > 182 AND ra < 184\n" + 
            "    AND\n" + 
            "        dec > -3 AND dec < -1\n" + 
            "    AND\n" + 
            "        CrossMatch.masterObjID = sourceID\n" + 
            "    ) > 0\n" + 
            "",

            "SELECT DistanceMins FROM ATLASv20130426.dbo.atlasSourceXDR7PhotoObj as CrossMatch WHERE ( SELECT s.sourceID FROM ATLASv20130426.dbo.atlasSource as s WHERE ra > 182 AND ra < 184 AND dec > -3 AND dec < -1 AND CrossMatch.masterObjID = sourceID ) > 0"
            );
        }
    }

