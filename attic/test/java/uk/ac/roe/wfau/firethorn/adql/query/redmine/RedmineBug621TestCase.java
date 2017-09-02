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
 * http://redmine.roe.ac.uk/issues/621
 *
 */
public class RedmineBug621TestCase
    extends AtlasQueryTestBase
    {

    /**
     * http://redmine.roe.ac.uk/issues/621
     * Known to fail
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            "SELECT max(ml.ra) as fra, max(ml.dec) as fdec,avg(umgPnt),avg(gmrpnt),"
            + " avg(rmiPnt),avg(imzPnt) "
            + " from atlasSource as s , atlasMergeLog as ml "
            + " where uppErrBits<=0 and gppErrBits <=0 and rppErrBits <=0 "
            + " and ippErrBits <0 and zppErrBits<=0 and ml.frameSetID=s.frameSetID "
            + " and mergedClass=-1 group by ml.frameSetID"

            );
        }
    }
