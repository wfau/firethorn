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
 * http://redmine.roe.ac.uk/issues/642
 *
 */
public class RedmineBug642TestCase
    extends AtlasQueryTestBase
    {

	/**
     * Fails table prefix on '*'.
     * Known to fail.
     * http://redmine.roe.ac.uk/issues/642
     *  
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            "SELECT min(utDate) AS 'MIN', max(utDate) AS 'MAX', min(dateobs) AS 'MIN', max(dateobs) AS 'MAX', min(mjdobs) AS 'MIN', "
            + " max(mjdobs) AS 'MAX' "
            + " FROM MultiFrame "
            + " WHERE utStart > 0"
               
            );
    }
}