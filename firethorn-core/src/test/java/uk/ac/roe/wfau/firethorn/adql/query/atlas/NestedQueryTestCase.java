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
package uk.ac.roe.wfau.firethorn.adql.query.atlas ;

import static org.junit.Assert.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;



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
    public void test000S()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            "SELECT\n" +
            "    DistanceMins\n" +
            "FROM\n" +
            "    atlassourcexDR7photoobj AS CrossMatch, \n" +
            "    (\n" +
            "    SELECT\n" +
            "        source.sourceid as id\n" +
            "    FROM\n" +
            "        atlassource as source\n" +
            "    WHERE\n" +
            "        source.ra BETWEEN 182 AND 184\n" +
            "    AND\n" +
            "        source.dec BETWEEN -1 AND -3\n" +
            "    AND\n" +
            "        mergedclass =1\n" +
            "    GROUP BY\n" +
            "        source.sourceid\n" +
            "    ) AS nested\n" +
            "WHERE\n" +
            "    DistanceMins < 2/60.0\n" +
            "AND\n" +
            "    sdsstype = 3\n" +
            "AND\n" +
            "    sdssPrimary = 1\n" +
            "AND " +
            "    nested.id = CrossMatch.masterObjID",

            "SELECT\n" + 
            "    CrossMatch.distanceMins AS DistanceMins\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj AS CrossMatch,\n" + 
            "    (\n" + 
            "    SELECT\n" + 
            "        source.sourceID AS id\n" + 
            "    FROM\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSource AS source\n" + 
            "    WHERE\n" + 
            "        source.ra BETWEEN 182 AND 184\n" + 
            "    AND\n" + 
            "        source.dec BETWEEN -1 AND -3\n" + 
            "    AND\n" + 
            "        source.mergedClass = 1\n" + 
            "    GROUP BY\n" + 
            "        source.sourceID\n" + 
            "    ) AS nested\n" + 
            "WHERE\n" + 
            "    CrossMatch.distanceMins < 2 / 60.0\n" + 
            "AND\n" + 
            "    CrossMatch.sdssType = 3\n" + 
            "AND\n" + 
            "    CrossMatch.sdssPrimary = 1\n" + 
            "AND\n" + 
            "    nested.id = CrossMatch.masterObjID",

            new ExpectedField[] {
                new ExpectedField("DistanceMins", AdqlColumn.Type.FLOAT, 0)
                }
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
    public void test001S()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,
        
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
            "    sdsstype = 3",
            
            "SELECT\n" + 
            "    Crossmatch.distanceMins AS DistanceMins\n" + 
            "FROM\n" + 
            "    (\n" + 
            "    SELECT TOP 10000\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.masterObjID AS masterObjID,\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.slaveObjID AS slaveObjID,\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.distanceMins AS distanceMins,\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.sdssType AS sdssType,\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.sdssPrimary AS sdssPrimary\n" + 
            "    FROM\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj\n" + 
            "    WHERE\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.sdssPrimary = 1\n" + 
            "    ) AS Crossmatch\n" + 
            "WHERE\n" + 
            "    Crossmatch.distanceMins < 2 / 60.0 AND Crossmatch.sdssType = 3",

            new ExpectedField[] {
                new ExpectedField("DistanceMins", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }

    /**
     * TODO
     * Test query Subquery in Where - returns 32378 rows
     *
     *  Query:
     *  Select DistanceMins From atlassourcexDR7photoobj as CrossMatch
     *  Where
     *	(Select s.sourceid From atlassource as s Where ra > 182 AND ra < 184 AND dec > -3 AND dec < -1 AND CrossMatch.masterOjbID = sourceID)>0
     *
     */
    @Test
    public void test002S()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,
        
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
            "    ) > 0",
            
            "SELECT\n" + 
            "    DistanceMins\n" + 
            "FROM\n" + 
            "    {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj as CrossMatch\n" + 
            "WHERE\n" + 
            "    (\n" + 
            "    SELECT\n" + 
            "        sourceID\n" + 
            "    FROM\n" + 
            "        {ATLAS_VERSION}.dbo.atlasSource\n" + 
            "    WHERE\n" + 
            "        ra BETWEEN 182 AND 184\n" + 
            "    AND\n" + 
            "        dec BETWEEN -1 AND -3\n" + 
            "    AND\n" + 
            "        CrossMatch.masterObjID = sourceID\n" + 
            "    ) > 0",

            new ExpectedField[] {
                new ExpectedField("DistanceMins", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }
    }

