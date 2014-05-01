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
package uk.ac.roe.wfau.firethorn.adql.query.redmine;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/440
 *
 */
public class RedmineBug440TestCase
    extends AtlasQueryTestBase
    {

    /**
     * Fails on expression in GROUP BY.
     *
     */
    @Test
    public void test003()
        {
        validate(
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
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.l * 6.0,0,0)/ 6.0 AS lon," +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.b * 6.0,0,0)/ 6.0 AS lat," +
            "    COUNT(*) AS num" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    ({ATLAS_VERSION}.dbo.atlassource.priOrSec = 0 OR {ATLAS_VERSION}.dbo.atlassource.priOrSec = {ATLAS_VERSION}.dbo.atlassource.frameSetID)" +
            " GROUP BY" +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.l * 6.0,0,0)/ 6.0," +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.b * 6.0,0,0)/ 6.0",

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
        validate(
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
            "    (l*6.0)/6.0," +
            "    (b*6.0)/6.0",

            " SELECT" +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.l * 6.0,0,0)/ 6.0 AS lon," +
            "    ROUND({ATLAS_VERSION}.dbo.atlassource.b * 6.0,0,0)/ 6.0 AS lat," +
            "    COUNT(*) AS num" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    ({ATLAS_VERSION}.dbo.atlassource.priOrSec = 0 OR {ATLAS_VERSION}.dbo.atlassource.priOrSec = {ATLAS_VERSION}.dbo.frameSetID)" +
            " GROUP BY" +
            "    ({ATLAS_VERSION}.dbo.atlassource.l * 6.0)/ 6.0," +
            "    ({ATLAS_VERSION}.dbo.atlassource.b * 6.0)/ 6.0",

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
        validate(
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
            "    ({ATLAS_VERSION}.dbo.atlassource.priOrSec = 0 OR {ATLAS_VERSION}.dbo.atlassource.priOrSec = {ATLAS_VERSION}.dbo.atlassource.frameSetID)" +
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
    }
