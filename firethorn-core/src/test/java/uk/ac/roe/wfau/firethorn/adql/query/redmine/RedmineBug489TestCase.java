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
 * http://redmine.roe.ac.uk/issues/489
 *
 */
public class RedmineBug489TestCase
    extends AtlasQueryTestBase
    {

    /**
     * Brackets in SELECT expression.
     *
     */
    @Test
    public void test001()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    ((ra + 6) / 2) AS one," +
            "    (ra + (6 / 2)) AS two" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 5" +
            "    (({ATLAS_VERSION}.dbo.atlassource.ra  + 6) / 2) AS one," +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra  + (6 / 2)) AS two" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource",

            new ExpectedField[] {
                new ExpectedField("one", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("two", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        }

    /**
     * Brackets in WHERE expression.
     *
     */
    @Test
    public void test002()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    ((ra + 6) / 2) AS one," +
            "    (ra + (6 / 2)) AS two" +
            " FROM" +
            "    atlasSource" +
            " WHERE" +
            "    ((ra + 6) / 2) > 0" +
            " AND" +
            "    (ra + (6 / 2)) > 0",

            " SELECT TOP 5" +
            "    (({ATLAS_VERSION}.dbo.atlassource.ra  + 6) / 2) AS one," +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra  + (6 / 2)) AS two" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    (({ATLAS_VERSION}.dbo.atlassource.ra  + 6) / 2) > 0" +
            " AND" +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra  + (6 / 2)) > 0",

            new ExpectedField[] {
                new ExpectedField("one", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("two", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        }

    /**
     * Brackets in GROUP BY expression.
     *
     */
    @Test
    public void test003()
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 5" +
            "    MAX((ra + 6) / 2) AS one," +
            "    MIN(ra + (6 / 2)) AS two" +
            " FROM" +
            "    atlasSource" +
            " WHERE" +
            "    ((ra + 6) / 2) > 0" +
            " AND" +
            "    (ra + (6 / 2)) > 0" +
            " GROUP BY" +
            "    (ra + (6 / 2))",

            " SELECT TOP 5" +
            "    MAX(({ATLAS_VERSION}.dbo.atlassource.ra  + 6) / 2) AS one," +
            "    MIN({ATLAS_VERSION}.dbo.atlassource.ra  + (6 / 2)) AS two" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    (({ATLAS_VERSION}.dbo.atlassource.ra  + 6) / 2) > 0" +
            " AND" +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra  + (6 / 2)) > 0" +
            " GROUP BY" +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra  + (6 / 2))",

            new ExpectedField[] {
                new ExpectedField("one", AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("two", AdqlColumn.Type.DOUBLE, 0),
                }
            );
        }
    }
