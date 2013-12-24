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
public class SimpleQueryTestCase
    extends AtlasQueryTestBase
    {
    @Test
    public void test000()
    throws Exception
        {
        validate(
            Level.STRICT,
            State.VALID,

            " SELECT TOP 100" +
            "    ra," +
            "    dec" +
            " FROM" +
            "    atlasSource",

            " SELECT TOP 100" +
            "    {ATLAS_VERSION}.dbo.atlasSource.ra  AS ra," +
            "    {ATLAS_VERSION}.dbo.atlasSource.dec AS dec" +
            " FROM" +
            "    {ATLAS_VERSION}.dbo.atlasSource",

            new ExpectedField[] {
                new ExpectedField("ra",  AdqlColumn.Type.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }
    }
