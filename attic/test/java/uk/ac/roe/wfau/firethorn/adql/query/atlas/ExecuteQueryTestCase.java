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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 * Test hangs in execution thread - ignore.
 *
 */
public class ExecuteQueryTestCase
    extends AtlasQueryTestBase
    {
    
    /**
     * Simple query so that test class doesn't fail when running JUnit tests
     * @throws Exception
     */
    @Test
    public void test000S()
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
                new ExpectedField("ra",  AdqlColumn.AdqlType.DOUBLE, 0),
                new ExpectedField("dec", AdqlColumn.AdqlType.DOUBLE, 0)
                }
            );
        }
    }
