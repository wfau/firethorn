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

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * 
 *
 */
public class RedmineBug450TestCase
    extends AtlasQueryTestBase
    {

    /**
     * Test with brackets on the 'OR'.
     *
     */
    @Test
    public void test001()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource" +
            " WHERE" +
            "    (" +
            "        ra > 180" +
            "    AND" +
            "        ra < 181" +
            "    )" +
            "AND" +
            "    (" +
            "    dec > -3" +
            "        AND" +
            "        (" +
            "            dec < -2" +
            "        OR" +
            "            dec < -3" +
            "        )" +
            "    )",

            " SELECT" +
            "    {ATLAS_VERSION}.dbo.atlassource.ra as ra," +
            "    {ATLAS_VERSION}.dbo.atlassource.dec as dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra > 180 AND {ATLAS_VERSION}.dbo.atlassource.ra < 181)" +
            " AND" +
            "    ({ATLAS_VERSION}.dbo.atlassource.dec > -3 AND ({ATLAS_VERSION}.dbo.atlassource.dec < -2 OR {ATLAS_VERSION}.dbo.atlassource.dec < -3))",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * Test with brackets on the 'AND'.
     *
     */
    @Test
    public void test002()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource" +
            " WHERE" +
            "    (" +
            "        ra > 180" +
            "    AND" +
            "        ra < 181" +
            "    )" +
            "AND" +
            "    (" +
            "        (" +
            "            dec > -3" +
            "        AND" +
            "            dec < -2" +
            "        )" +
            "    OR" +
            "        dec < -3" +
            "    )",

            " SELECT" +
            "    {ATLAS_VERSION}.dbo.atlassource.ra as ra," +
            "    {ATLAS_VERSION}.dbo.atlassource.dec as dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra > 180 AND {ATLAS_VERSION}.dbo.atlassource.ra < 181)" +
            " AND" +
            "    (({ATLAS_VERSION}.dbo.atlassource.dec > -3 AND {ATLAS_VERSION}.dbo.atlassource.dec < -2) OR {ATLAS_VERSION}.dbo.atlassource.dec < -3)",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * Test with brackets on all of the comparisons.
     *
     */
    @Test
    public void test003()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource" +
            " WHERE" +
            "    (" +
            "        (ra > 180)" +
            "    AND" +
            "        (ra < 181)" +
            "    )" +
            "AND" +
            "    (" +
            "    (dec > -3)" +
            "        AND" +
            "        (" +
            "            (dec < -2)" +
            "        OR" +
            "            (dec < -3)" +
            "        )" +
            "    )",

            " SELECT" +
            "    {ATLAS_VERSION}.dbo.atlassource.ra as ra," +
            "    {ATLAS_VERSION}.dbo.atlassource.dec as dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    (({ATLAS_VERSION}.dbo.atlassource.ra > 180) AND ({ATLAS_VERSION}.dbo.atlassource.ra < 181))" +
            " AND" +
            "    (({ATLAS_VERSION}.dbo.atlassource.dec > -3) AND (({ATLAS_VERSION}.dbo.atlassource.dec < -2) OR ({ATLAS_VERSION}.dbo.atlassource.dec < -3)))",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * Test with no extra brackets.
     *
     */
    @Test
    public void test004()
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource" +
            " WHERE" +
            "    ra > 180" +
            "AND" +
            "    ra < 181" +
            "AND" +
            "    dec > -3" +
            "AND" +
            "    dec < -2" +
            "OR" +
            "    dec < -3",

            " SELECT" +
            "    {ATLAS_VERSION}.dbo.atlassource.ra as ra," +
            "    {ATLAS_VERSION}.dbo.atlassource.dec as dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    {ATLAS_VERSION}.dbo.atlassource.ra > 180 AND {ATLAS_VERSION}.dbo.atlassource.ra < 181" +
            " AND" +
            "    {ATLAS_VERSION}.dbo.atlassource.dec > -3 AND {ATLAS_VERSION}.dbo.atlassource.dec < -2 OR {ATLAS_VERSION}.dbo.atlassource.dec < -3",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }
    }
