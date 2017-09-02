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
 * http://redmine.roe.ac.uk/issues/625
 *
 */
public class RedmineBug625TestCase
    extends AtlasQueryTestBase
    {

    /**
     * http://redmine.roe.ac.uk/issues/625
     * Known to fail
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            "SELECT atlas.sourceID, atlas.ra, atlas.dec, fsc.seqNo, fsc.ra, fsc.dec  "
            + " FROM atlasSource AS atlas, ROSAT..rosat_fsc AS fsc, atlasSourceXrosat_fsc AS x "
            + " WHERE x.masterObjID=atlas.sourceID AND x.slaveObjID=fsc.seqNo AND x.distanceMins<0.1"

            );
        }
    }