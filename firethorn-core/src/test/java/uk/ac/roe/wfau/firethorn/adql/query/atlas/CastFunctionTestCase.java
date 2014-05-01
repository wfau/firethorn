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
public class CastFunctionTestCase
extends AtlasQueryTestBase
    {

    /**
     * CAST(ra AS INT)
     *
     */
    @Test
    public void test001()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    CAST(ra  AS INT)," +
            "    CAST(dec AS INT)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra  AS INT) AS ra," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.dec AS INT) AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.INTEGER, 0),
                new ExpectedField("dec", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }

    /**
     * CAST(ra AS SHORT)
     * CAST(ra AS SMALLINT)
     * CAST(ra AS INT)
     * CAST(ra AS INTEGER)
     * CAST(ra AS LONG)
     * CAST(ra AS BIGINT)
     * CAST(ra AS FLOAT)
     * CAST(ra AS DOUBLE)
     *
     */
    @Test
    public void test002()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    CAST(ra  AS SHORT)    AS ra_short," +
            "    CAST(ra  AS SMALLINT) AS ra_small," +
            "    CAST(ra  AS INT)      AS ra_int," +
            "    CAST(ra  AS INTEGER)  AS ra_integer," +
            "    CAST(ra  AS LONG)     AS ra_long," +
            "    CAST(ra  AS BIGINT)   AS ra_big," +
            "    CAST(ra  AS FLOAT)    AS ra_float," +
            "    CAST(ra  AS DOUBLE)   AS ra_double" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS SHORT)    AS ra_short," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS SMALLINT) AS ra_small," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS INT)      AS ra_int," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS INTEGER)  AS ra_integer," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS LONG)     AS ra_long," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS BIGINT)   AS ra_big," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS FLOAT)    AS ra_float," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra AS DOUBLE)   AS ra_double" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                    new ExpectedField("ra_short",   AdqlColumn.Type.SHORT,   0),
                    new ExpectedField("ra_small",   AdqlColumn.Type.SHORT,   0),
                    new ExpectedField("ra_int",     AdqlColumn.Type.INTEGER, 0),
                    new ExpectedField("ra_integer", AdqlColumn.Type.INTEGER, 0),
                    new ExpectedField("ra_long",    AdqlColumn.Type.LONG,    0),
                    new ExpectedField("ra_big",     AdqlColumn.Type.LONG,    0),
                    new ExpectedField("ra_float",   AdqlColumn.Type.FLOAT,   0),
                    new ExpectedField("ra_double",  AdqlColumn.Type.DOUBLE,  0),
                }
            );
        }

    /**
     * CAST((ra * 6) AS INT) AS rasixth
     *
     */
    @Test
    public void test003()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    CAST((ra * 6) AS INT) AS rasixth," +
            "    CAST(dec * 6  AS INT) AS decsixth"  +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    CAST(({ATLAS_VERSION}.dbo.atlassource.ra) * 6 AS INT) AS rasixth," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.dec  * 6 AS INT) AS decsixth" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("rasixth",  AdqlColumn.Type.INTEGER, 0),
                new ExpectedField("decsixth", AdqlColumn.Type.INTEGER, 0)
                }
            );
        }

    /**
     * SELECT CAST((ra * 6) AS INT) => CASTED
     * @todo This should be replaced by a generated column name.
     *
     */
    @Test
    public void test004()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    CAST((ra * 6) AS INT)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.ra  * 6 AS INT) AS CASTED," +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("CASTED",  AdqlColumn.Type.INTEGER, 0),
                }
            );
        }

    /**
     * GROUP BY (CAST((ra * 6) AS INT) / 6)
     *
     */
    @Test
    public void test005()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    COUNT(ra) AS binsize," +
            "    (CAST((ra * 6) AS INT) / 6) AS binvalue" +
            " FROM" +
            "    atlasSource" +
            " GROUP BY" +
            "    (CAST((ra * 6) AS INT) / 6)",

            " SELECT TOP 5" +
            "    COUNT({ATLAS_VERSION}.dbo.atlassource.ra) AS binsize," +
            "    (CAST(({ATLAS_VERSION}.dbo.atlassource.ra * 6) AS INT) / 6) AS binvalue" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " GROUP BY" +
            "    (CAST(({ATLAS_VERSION}.dbo.atlassource.ra * 6) AS INT) / 6)",

            new ExpectedField[] {
                new ExpectedField("binsize",  AdqlColumn.Type.LONG,    0),
                new ExpectedField("binvalue", AdqlColumn.Type.INTEGER, 0),
                }
            );
        }
    }
