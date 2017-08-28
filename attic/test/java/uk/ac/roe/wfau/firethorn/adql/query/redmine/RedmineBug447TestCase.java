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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/447
 *
 */
public class RedmineBug447TestCase
    extends AtlasQueryTestBase
    {

    /**
     * Test for CAST to INT.
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            " SELECT TOP 10" +
            "    CAST(l AS INT) AS lon," +
            "    CAST(b AS INT) AS lat" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)",

            " SELECT TOP 10" +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.l AS INT) AS lon," +
            "    CAST({ATLAS_VERSION}.dbo.atlassource.b AS INT) AS lat" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    ({ATLAS_VERSION}.dbo.atlassource.priOrSec = 0 OR {ATLAS_VERSION}.dbo.atlassource.priOrSec = {ATLAS_VERSION}.dbo.atlassource.frameSetID)",

            new ExpectedField[] {
                new ExpectedField("lon", AdqlColumn.AdqlType.INTEGER, 0),
                new ExpectedField("lat", AdqlColumn.AdqlType.INTEGER, 0),
                }
            );
        }
    }
