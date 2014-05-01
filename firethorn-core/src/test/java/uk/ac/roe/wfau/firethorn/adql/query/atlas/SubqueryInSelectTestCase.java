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
            "    DistanceMins" +
            " FROM" +
            "    atlassourcexDR7photoobj as CrossMatch," +
            "    (" +
            "    SELECT" +
            "        sourceId as id" +
            "    FROM" +
            "        atlasSource" +
            "    WHERE" +
            "        ra BETWEEN 182 AND 184" +
            "    AND" +
            "        dec BETWEEN -1 AND -3" +
            "    AND" +
            "        mergedclass = 1" +
            "    GROUP BY" +
            "        sourceId" +
            "    ) AS subquery" +
            " WHERE" +
            "    DistanceMins < 2/60.0" +
            " AND" +
            "    sdsstype = 3" +
            " AND" +
            "    sdssPrimary = 1" +
            " AND" +
            "    subquery.id = CrossMatch.masterObjID",

            " SELECT" +
            "    crossmatch.distancemins as distancemins" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassourcexdr7photoobj as crossmatch," +
            "    (" +
            "    SELECT" +
            "        {ATLAS_VERSION}.dbo.atlassource.sourceid as id" +
            "    FROM" +
            "        {ATLAS_VERSION}.dbo.atlassource" +
            "    WHERE" +
            "        {ATLAS_VERSION}.dbo.atlassource.ra BETWEEN 182 AND 184" +
            "    AND" +
            "        {ATLAS_VERSION}.dbo.atlassource.dec BETWEEN -1 AND -3" +
            "    AND" +
            "        {ATLAS_VERSION}.dbo.atlassource.mergedclass = 1" +
            "    GROUP BY" +
            "        {ATLAS_VERSION}.dbo.atlassource.sourceid" +
            "    ) AS subquery" +
            " WHERE" +
            "    crossmatch.distancemins < 2 / 60.0" +
            " AND" +
            "    crossmatch.sdsstype = 3" +
            " AND" +
            "    crossmatch.sdssprimary = 1" +
            " AND" +
            "    subquery.id = crossmatch.masterobjid",

            new ExpectedField[] {
                new ExpectedField("DistanceMins", AdqlColumn.Type.FLOAT, 0)
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
            "    DistanceMins" +
            " FROM" +
            "    (" +
            "    SELECT TOP 10000" +
            "        *" +
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
            "    crossmatch.distancemins AS distancemins" +
            " FROM" +
            "    (" +
            "    SELECT TOP 10000" +
            "        atlasdr1.dbo.atlassourcexdr7photoobj.masterobjid  AS masterobjid," +
            "        atlasdr1.dbo.atlassourcexdr7photoobj.slaveobjid   AS slaveobjid," +
            "        atlasdr1.dbo.atlassourcexdr7photoobj.distancemins AS distancemins," +
            "        atlasdr1.dbo.atlassourcexdr7photoobj.sdsstype     AS sdsstype," +
            "        atlasdr1.dbo.atlassourcexdr7photoobj.sdssprimary  AS sdssprimary" +
            "    FROM" +
            "        atlasdr1.dbo.atlassourcexdr7photoobj" +
            "    WHERE" +
            "        atlasdr1.dbo.atlassourcexdr7photoobj.sdssprimary = 1" +
            "    ) AS crossmatch" +
            " WHERE" +
            "    crossmatch.distancemins < 2 / 60.0" +
            " AND" +
            "    crossmatch.sdsstype = 3",

            new ExpectedField[] {
                new ExpectedField("DistanceMins", AdqlColumn.Type.FLOAT, 0)
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
     */
    @Test
    public void test002()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    DistanceMins" +
            " FROM" +
            "    atlassourcexDR7photoobj AS CrossMatch" +
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
            "        CrossMatch.masterOjbID = sourceID" +
            "    ) > 0",

            "",

            new ExpectedField[] {
                new ExpectedField("DistanceMins", AdqlColumn.Type.FLOAT, 0)
                }
            );
        }
    }

