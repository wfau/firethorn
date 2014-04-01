/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query.atlas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class GroupByBugsTestCase
    extends AtlasQueryTestBase
    {

    /**
     * Original query.
     *
     *
     */
    @Test
    public void test001()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    CAST(ROUND(l*6.0,0) AS INT)/6.0 AS lon," +
            "    CAST(ROUND(b*6.0,0) AS INT)/6.0 AS lat," +
            "    COUNT(*)                        AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    CAST(ROUND(l*6.0,0) AS INT)/6.0," +
            "    CAST(ROUND(b*6.0,0) AS INT)/6.0",

            " SELECT" +
            "    CAST(ROUND(l*6.0,0) AS INT)/6.0 AS lon," +
            "    CAST(ROUND(b*6.0,0) AS INT)/6.0 AS lat," +
            "    COUNT(*)                        AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    CAST(ROUND(l*6.0,0) AS INT)/6.0," +
            "    CAST(ROUND(b*6.0,0) AS INT)/6.0",

            new ExpectedField[] {
                new ExpectedField("lon", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("lat", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("num", AdqlColumn.Type.LONG, 0),
                }
            );
        }

    /**
     * Test for CAST.
     *
     */
    @Test
    public void test002()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    CAST(l AS INT) AS lon," +
            "    CAST(b AS INT) AS lat," +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)",

            " SELECT TOP 10" +
            "    CAST(l AS INT) AS lon," +
            "    CAST(b AS INT) AS lat," +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)",

            new ExpectedField[] {
                new ExpectedField("lon", AdqlColumn.Type.INTEGER, 0),
                new ExpectedField("lat", AdqlColumn.Type.INTEGER, 0),
                }
            );
        }

    /**
     * Without CAST - fails on expression in GROUP BY.
     *
     */
    @Test
    public void test003()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    ROUND(l*6.0,0)/6.0," +
            "    ROUND(b*6.0,0)/6.0",

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    ROUND(l*6.0,0)/6.0," +
            "    ROUND(b*6.0,0)/6.0",

            new ExpectedField[] {
                new ExpectedField("lon", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("lat", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("num", AdqlColumn.Type.LONG, 0),
                }
            );
        }
    
    /**
     * Remove ROUND and leave [expression] in GROUP BY.
     *
     */
    @Test
    public void test004()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    (l*6.0,0)/6.0," +
            "    (b*6.0,0)/6.0",

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    (l*6.0,0)/6.0," +
            "    (b*6.0,0)/6.0",

            new ExpectedField[] {
                new ExpectedField("lon", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("lat", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("num", AdqlColumn.Type.LONG, 0),
                }
            );
        }
    
    /**
     * Remove [expression] in GROUP BY - TEST PASSES.
     *
     */
    @Test
    public void test005()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    l," +
            "    b",
            
            " SELECT" +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.l * 6.0,0,0)/ 6.0 AS lon," +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.b * 6.0,0,0)/ 6.0 AS lat," +
            "    COUNT(*) AS num" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    {ATLAS_VERSION}.dbo.atlassource.priOrSec = 0 OR {ATLAS_VERSION}.dbo.atlassource.priOrSec = {ATLAS_VERSION}.dbo.atlassource.frameSetID" +
            " GROUP BY" +
            "    {ATLAS_VERSION}.dbo.atlassource.l," +
            "    {ATLAS_VERSION}.dbo.atlassource.b",

            new ExpectedField[] {
                new ExpectedField("lon", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("lat", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("num", AdqlColumn.Type.LONG, 0),
                }
            );
        }
 
    /**
     * Using column alias in GROUP BY.
     * Works in the ADQL parser, but fails in SQLServer - NOT VALID SQL.
     * https://stackoverflow.com/questions/2681494/why-doesnt-oracle-sql-allow-us-to-use-column-aliases-in-group-by-clauses
     *
     * ** Bug is that this should be rejected by the parser, with an appropriate syntax error.
     * ** Allowing this to get through the ADQL parser causes side effects later on in OGSA-DAI and SQLServer.
     * 
     */
    @Test
    public void test006()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    lon," +
            "    lat"
            );
        }

    /**
     * Try using nested query to separate calculation and GROUP BY.
     * Probably should work, fails with internal parser error.
     * [AdqlParserImpl] Error parsing query [ADQLColumn with unknown DBLink class [adql.db.DefaultDBColumn]]
     *
     */
    @Test
    public void test007()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.VALID,

            " SELECT" +
            "     nested.lon      AS lon," +
            "     nested.lat      AS lat," +
            "     COUNT(*) AS num" +
            " FROM" +
            "     (SELECT" +
            "         ROUND(l*6.0,0)/6.0 AS lon," +
            "         ROUND(b*6.0,0)/6.0 AS lat" +
            "     FROM" +
            "         ATLASDR1.atlasSource" +
            "     WHERE" +
            "         priOrSec = 0 OR priOrSec = frameSetID" +
            "     ) AS nested" +
            " GROUP BY" +
            "     nested.lon," +
            "     nested.lat",

            " SELECT" +
            "     nested.lon AS lon," +
            "     nested.lat AS lat," +
            "     COUNT(*) AS num" +
            " FROM" +
            "     SELECT" +
            "         ROUND({ATLAS_VERSION}.dbo.atlassource.l * 6.0,0,0)/ 6.0 AS lon," +
            "         ROUND({ATLAS_VERSION}.dbo.atlassource.b * 6.0,0,0)/ 6.0 AS lat" +
            "     FROM" +
            "         {ATLAS_VERSION}.dbo.atlassource" +
            "     WHERE" +
            "         {ATLAS_VERSION}.dbo.atlassource.priOrSec = 0 OR {ATLAS_VERSION}.dbo.atlassource.priOrSec = {ATLAS_VERSION}.dbo.atlassource.frameSetID" +
            "     AS nested" +
            " GROUP BY" +
            "     nested.lon," +
            "     nested.lat",

            new ExpectedField[] {
                new ExpectedField("lon", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("lat", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("num", AdqlColumn.Type.LONG, 0),
                }
            );
        }

    /**
     * Missing GROUP BY clause passes ADQL parser.
     * Works in the ADQL parser, but fails in SQLServer - NOT VALID SQL.
     *
     * ** Bug is that this should be rejected by the parser, with an appropriate syntax error.
     * ** Allowing this to get through the ADQL parser causes side effects later on in OGSA-DAI and SQLServer.
     * 
     */
    @Test
    public void test008()
        {
        final AdqlQuery query = validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)"
            );
        }

    }
