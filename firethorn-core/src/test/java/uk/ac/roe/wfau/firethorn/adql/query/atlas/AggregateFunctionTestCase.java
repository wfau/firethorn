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
import uk.ac.roe.wfau.firethorn.adql.query.QueryProcessingException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 *
 *
 */
public class AggregateFunctionTestCase
    extends AtlasQueryTestBase
    {

    /**
     * MAX(), MIN()
     *
     */
    @Test
    public void test001S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    MAX(ra)," +
            "    MIN(ra)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    MAX({ATLAS_VERSION}.dbo.atlassource.ra) AS MAX," +
            "    MIN({ATLAS_VERSION}.dbo.atlassource.ra) AS MIN" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("MAX", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("MIN", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * SUM(), AVG()
     *
     */
    @Test
    public void test002S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    SUM(ra)," +
            "    AVG(ra)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    SUM({ATLAS_VERSION}.dbo.atlassource.ra) AS SUM," +
            "    AVG({ATLAS_VERSION}.dbo.atlassource.ra) AS AVG" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("SUM", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("AVG", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * +SUM(), -AVG()
     *
     */
    @Test
    public void test003S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    -SUM(ra)," +
            "    +AVG(ra)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "   -SUM({ATLAS_VERSION}.dbo.atlassource.ra) AS SUM," +
            "    AVG({ATLAS_VERSION}.dbo.atlassource.ra) AS AVG" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("SUM", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("AVG", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    /**
     * COUNT()
     *
     */
    @Test
    public void test004S()
    throws QueryProcessingException
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 5" +
            "    COUNT(ra)" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    COUNT({ATLAS_VERSION}.dbo.atlassource.ra) AS COUNT" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("COUNT", AdqlColumn.Type.LONG, 0)
                }
            );
        }
    }
