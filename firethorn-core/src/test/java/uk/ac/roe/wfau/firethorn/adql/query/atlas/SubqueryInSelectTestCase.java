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

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 *
 *
 */
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
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    distanceMins" +
            " FROM" +
            "    atlasSourceXDR7PhotoObj as crossMatch," +
            "    (" +
            "    SELECT" +
            "        sourceID as id" +
            "    FROM" +
            "        atlasSource" +
            "    WHERE" +
            "        ra BETWEEN 182 AND 184" +
            "    AND" +
            "        dec BETWEEN -1 AND -3" +
            "    AND" +
            "        mergedClass = 1" +
            "    GROUP BY" +
            "        sourceID" +
            "    ) AS subQuery" +
            " WHERE" +
            "    distanceMins < 2/60.0" +
            " AND" +
            "    sdssType = 3" +
            " AND" +
            "    sdssPrimary = 1" +
            " AND" +
            "    subQuery.id = crossMatch.masterObjID",

            " SELECT" +
            "    crossMatch.distanceMins AS distanceMins" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSourceXDR7PhotoObj AS crossMatch," +
            "    (" +
            "    SELECT" +
            "        {ATLAS_VERSION}.dbo.atlasSource.sourceID AS id" +
            "    FROM" +
            "        {ATLAS_VERSION}.dbo.atlasSource" +
            "    WHERE" +
            "        {ATLAS_VERSION}.dbo.atlasSource.ra BETWEEN 182 AND 184" +
            "    AND" +
            "        {ATLAS_VERSION}.dbo.atlasSource.dec BETWEEN -1 AND -3" +
            "    AND" +
            "        {ATLAS_VERSION}.dbo.atlasSource.mergedClass = 1" +
            "    GROUP BY" +
            "        {ATLAS_VERSION}.dbo.atlasSource.sourceID" +
            "    ) AS subQuery" +
            " WHERE" +
            "    crossMatch.distanceMins < 2 / 60.0" +
            " AND" +
            "    crossMatch.sdssType = 3" +
            " AND" +
            "    crossMatch.sdssPrimary = 1" +
            " AND" +
            "    subQuery.id = crossMatch.masterObjID",

            new ExpectedField[] {
                new ExpectedField("distanceMins", AdqlColumn.AdqlType.FLOAT, 0)
                }
            );
        }


    /**
     * Test query  Subquery in From - returns 2876 rows"
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
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    distanceMins" +
            " FROM" +
            "    (" +
            "    SELECT TOP 10000" +
            "        *" +
            "    FROM" +
            "        atlasSourceXDR7PhotoObj" +
            "    WHERE" +
            "        sdssPrimary = 1" +
            "    ) AS crossMatch" +
            " WHERE" +
            "    distanceMins < 2/60.0" +
            " AND" +
            "    sdssType = 3",

            " SELECT" +
            "    crossMatch.distanceMins AS distanceMins" +
            " FROM" +
            "    (" +
            "    SELECT TOP 10000" +
            "        atlasdr1.dbo.atlasSourceXDR7PhotoObj.distanceMins AS distanceMins," +
            "        atlasdr1.dbo.atlasSourceXDR7PhotoObj.masterObjID  AS masterObjID," +
            "        atlasdr1.dbo.atlasSourceXDR7PhotoObj.sdssPrimary  AS sdssPrimary," +
            "        atlasdr1.dbo.atlasSourceXDR7PhotoObj.sdssType     AS sdssType," +
            "        atlasdr1.dbo.atlasSourceXDR7PhotoObj.slaveObjID   AS slaveObjID" +
            "    FROM" +
            "        atlasdr1.dbo.atlasSourceXDR7PhotoObj" +
            "    WHERE" +
            "        atlasdr1.dbo.atlasSourceXDR7PhotoObj.sdssPrimary = 1" +
            "    ) AS crossMatch" +
            " WHERE" +
            "    crossMatch.distanceMins < 2 / 60.0" +
            " AND" +
            "    crossMatch.sdssType = 3",

            new ExpectedField[] {
                new ExpectedField("distanceMins", AdqlColumn.AdqlType.FLOAT, 0)
                }
            );
        }

    /**
     * Test query Subquery in Where - returns 32378 rows
     * Known to fail, waiting for parser fix.
     * http://redmine.roe.ac.uk/issues/442
     *
     *  Query:
     *  Select DistanceMins From atlassourcexDR7photoobj as CrossMatch
     *  Where
     *	(Select s.sourceid From atlassource as s Where ra > 182 AND ra < 184 AND dec > -3 AND dec < -1 AND CrossMatch.masterOjbID = sourceID)>0
     *
     * RedMine issue rejected - expected behaviour, not a bug.
     *
     */
    @Test
    public void test002()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.PARSE_ERROR,

            " SELECT" +
            "    DistanceMins" +
            " FROM" +
            "    atlasSourceXDR7PhotoObj AS crossMatch" +
            " WHERE" +
            "    (" +
            "    SELECT" +
            "        sourceID" +
            "    FROM" +
            "        atlasSource" +
            "    WHERE" +
            "        ra BETWEEN 182 AND 184" +
            "    AND" +
            "        dec BETWEEN -1 AND -3" +
            "    AND" +
            "        crossMatch.masterOjbID = sourceID" +
            "    ) > 0"
            );
        }
    }

