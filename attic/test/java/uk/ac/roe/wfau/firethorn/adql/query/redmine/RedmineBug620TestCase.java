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

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/620
 *
 */
public class RedmineBug620TestCase
    extends AtlasQueryTestBase
    {

    /**
     * http://redmine.roe.ac.uk/issues/620
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.VALID,

            "SELECT  ATLASsource.*,2*DEGREES(ASIN(sqrt(power(-0.997825033922517-cx,2)+"
            + "power(-0.052293794140904105-cy,2)+"
            + "power(-0.040131792532559725-cz,2))/2))*60 as distance  "
            + " FROM ATLASsource WHERE dec > -2.7166666666666663 "
            + " and dec < -1.8833333333333329 and RA >= 182.58286454384174 and RA <= 183.41713545615823 "
            + " and ((cx * -0.997825033922517 + cy * -0.052293794140904105 + cz * -0.040131792532559725 ) >= 0.9999735576321774)"
            );
        }
    }
