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
            "    (ra > 10 AND ra < 20) OR (dec > 100 AND dec < 110)",

            " SELECT" +
            "    {ATLAS_VERSION}.dbo.atlassource.ra as ra," +
            "    {ATLAS_VERSION}.dbo.atlassource.dec as dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlassource" +
            " WHERE" +
            "    ({ATLAS_VERSION}.dbo.atlassource.ra > 10 AND {ATLAS_VERSION}.dbo.atlassource.ra < 20) OR ({ATLAS_VERSION}.dbo.atlassource.dec > 100 AND {ATLAS_VERSION}.dbo.atlassource.dec < 110)",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }
    }
