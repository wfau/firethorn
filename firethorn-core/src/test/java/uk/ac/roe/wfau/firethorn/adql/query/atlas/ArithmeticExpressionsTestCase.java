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
public class ArithmeticExpressionsTestCase
    extends AtlasQueryTestBase
    {

    @Test
    public void test001S()
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            " SELECT TOP 10" +
            "    ra + dec" +
            " FROM" +
            "    twomass_psc",

            " SELECT TOP 10" + 
            "    TWOMASS.dbo.twomass_psc.ra + TWOMASS.dbo.twomass_psc.dec AS SUM" + 
            " FROM" + 
            "    TWOMASS.dbo.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("SUM", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    @Test
    public void test002S()
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            " SELECT TOP 10" +
            "    (ra + dec)" +
            " FROM" +
            "    twomass_psc",

            " SELECT TOP 10" + 
            "    TWOMASS.dbo.twomass_psc.ra + TWOMASS.dbo.twomass_psc.dec AS SUM" + 
            " FROM" + 
            "    TWOMASS.dbo.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("SUM", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    @Test
    public void test003S()
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            " SELECT TOP 10" +
            "    ra + dec AS mysum" +
            " FROM" +
            "    twomass_psc",

            " SELECT TOP 10" + 
            "    TWOMASS.dbo.twomass_psc.ra + TWOMASS.dbo.twomass_psc.dec AS mysum" + 
            " FROM" + 
            "    TWOMASS.dbo.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("mysum", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }

    @Test
    public void test004S()
        {
        validate(
            Level.STRICT,
            State.VALID,
        
            " SELECT TOP 10" +
            "    (ra + dec) AS mysum" +
            " FROM" +
            "    twomass_psc",

            " SELECT TOP 10" + 
            "    TWOMASS.dbo.twomass_psc.ra + TWOMASS.dbo.twomass_psc.dec AS mysum" + 
            " FROM" + 
            "    TWOMASS.dbo.twomass_psc",

            new ExpectedField[] {
                new ExpectedField("mysum", AdqlColumn.Type.DOUBLE, 0)
                }
            );
        }
    }
