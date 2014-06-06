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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.State;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase;

/**
 * JUnit test case for RedMine issue.
 * http://redmine.roe.ac.uk/issues/446
 *
 */
public class RedmineBug446TestCase
    extends AtlasQueryTestBase
    {

    /**
     * Using column alias in GROUP BY.
     * Works in the ADQL parser, but fails in SQLServer - NOT VALID SQL.
     * https://stackoverflow.com/questions/2681494/why-doesnt-oracle-sql-allow-us-to-use-column-aliases-in-group-by-clauses
     *
     * ** Bug is that this should be rejected by the parser, with an appropriate syntax error.
     * ** Allowing this to get through the ADQL parser causes side effects later on in OGSA-DAI and SQLServer.
     * 
     */
    @Test
    public void test001()
    throws Exception
        {
        validate(
            Level.LEGACY,
            State.PARSE_ERROR,

            " SELECT" +
            "    ROUND(l*6.0,0)/6.0 AS lon," +
            "    ROUND(b*6.0,0)/6.0 AS lat," +
            "    COUNT(*)           AS num" +
            " FROM" +
            "    ATLASDR1.atlasSource" +
            " WHERE" +
            "    (priOrSec=0 OR priOrSec=frameSetID)" +
            " GROUP BY" +
            "    lon," +
            "    lat"
            );
        }
    }
