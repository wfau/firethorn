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

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
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

            " SELECT" +
            "    DistanceMins" +
            " FROM" +
            "    atlassourcexDR7photoobj AS CrossMatch, " +
            "    (" +
            "    SELECT" +
            "        source.sourceid as id" +
            "    FROM" +
            "        atlassource as source" +
            "    WHERE" +
            "        source.ra BETWEEN 182 AND 184" +
            "    AND" +
            "        source.dec BETWEEN -1 AND -3" +
            "    AND" +
            "        mergedclass =1" +
            "    GROUP BY" +
            "        source.sourceid" +
            "    ) AS nested" +
            " WHERE" +
            "    DistanceMins < 2/60.0" +
            " AND" +
            "    sdsstype = 3" +
            " AND" +
            "    sdssPrimary = 1" +
            " AND " +
            "    nested.id = CrossMatch.masterObjID",

            " SELECT" +
            "    CrossMatch.distanceMins AS DistanceMins" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj AS CrossMatch," +
            "    (" +
            "    SELECT" +
            "        source.sourceID AS id" +
            "    FROM" +
            "        {ATLAS_VERSION}.dbo.atlasSource AS source" +
            "    WHERE" +
            "        source.ra BETWEEN 182 AND 184" +
            "    AND" +
            "        source.dec BETWEEN -1 AND -3" +
            "    AND" +
            "        source.mergedClass = 1" +
            "    GROUP BY" +
            "        source.sourceID" +
            "    ) AS nested" +
            " WHERE" +
            "    CrossMatch.distanceMins < 2 / 60.0" +
            " AND" +
            "    CrossMatch.sdssType = 3" +
            " AND" +
            "    CrossMatch.sdssPrimary = 1" +
            " AND" +
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

            " SELECT" +
            "    DistanceMins" +
            " FROM" +
            "    (" +
            "    SELECT" +
            "        TOP 10000 *" +
            "    FROM" +
            "        atlassourcexDR7photoobj" +
            "    WHERE" +
            "        sdssPrimary = 1" +
            "    ) AS Crossmatch" +
            " WHERE" +
            "    DistanceMins < 2/60.0" +
            " AND" +
            "    sdsstype = 3",

            " SELECT" +
            "    Crossmatch.distanceMins AS DistanceMins" +
            " FROM" +
            "    (" +
            "    SELECT TOP 10000" +
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.masterObjID AS masterObjID," +
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.slaveObjID AS slaveObjID," +
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.distanceMins AS distanceMins," +
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.sdssType AS sdssType," +
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.sdssPrimary AS sdssPrimary" +
            "    FROM" +
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj" +
            "    WHERE" +
            "        {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj.sdssPrimary = 1" +
            "    ) AS Crossmatch" +
            " WHERE" +
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

            " SELECT" +
            "    DistanceMins" +
            " FROM" +
            "    atlasSourceXDR7PhotoObj as CrossMatch" +
            " WHERE" +
            "    (" +
            "    SELECT" +
            "        s.sourceID" +
            "    FROM" +
            "        atlasSource as s" +
            "    WHERE" +
            "        ra > 182 AND ra < 184" +
            "    AND" +
            "        dec > -3 AND dec < -1" +
            "    AND" +
            "        CrossMatch.masterObjID = sourceID" +
            "    ) > 0",

            " SELECT" +
            "    DistanceMins" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj as CrossMatch" +
            " WHERE" +
            "    (" +
            "    SELECT" +
            "        sourceID" +
            "    FROM" +
            "        {ATLAS_VERSION}.dbo.atlasSource" +
            "    WHERE" +
            "        ra BETWEEN 182 AND 184" +
            "    AND" +
            "        dec BETWEEN -1 AND -3" +
            "    AND" +
            "        CrossMatch.masterObjID = sourceID" +
            "    ) > 0",

            new ExpectedField[] {
                new ExpectedField("DistanceMins", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }
    }

