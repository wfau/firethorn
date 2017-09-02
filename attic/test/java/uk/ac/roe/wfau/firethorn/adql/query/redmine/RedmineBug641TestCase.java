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
 * http://redmine.roe.ac.uk/issues/641
 *
 */
public class RedmineBug641TestCase
    extends AtlasQueryTestBase
    {

    /**
     * http://redmine.roe.ac.uk/issues/641
     * Known to fail
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            " select round(K_1apermag3*20,0)/20.0 as k,round( (Japermag3-K_1apermag3)*20,0)/20.0 as JK,count(*) as count "
            + " FROM gpsJHKsource s WHERE s.l between 19.7500 and 20.2500 and s.b "
            + " between -0.25 and 0.25 and Japermag3-K_1apermag3 between 0 and 6 and (PriOrSec=0 OR PriOrSec=framesetID)"
            + " and K_1apermag3 between 10 and 18 "
            + " group by round( K_1apermag3*20,0)/20.0 ,round( (Japermag3-K_1apermag3)*20,0)/20.0"
            );
        }
    }
