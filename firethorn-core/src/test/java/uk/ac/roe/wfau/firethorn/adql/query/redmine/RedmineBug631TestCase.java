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
 * http://redmine.roe.ac.uk/issues/631
 *
 */
public class RedmineBug631TestCase
    extends AtlasQueryTestBase
    {

    /**
     * Simpler example
     * Known to fail
     */
    @Test
    public void test004()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            "select cast(x as int),cast(y as int),count(*) "
            + " from atlasDetection as d, atlasMergeLog as l "
            + " where l.gmfid=d.multiframeid and l.deprecated=0 "
            + " group by cast(x as int),cast(y as int) order by cast(x as int),cast(y as int)  "
        	) ;          

        
        }
    
   }
