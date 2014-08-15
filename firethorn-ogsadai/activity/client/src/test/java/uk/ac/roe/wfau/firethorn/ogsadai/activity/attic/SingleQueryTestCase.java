/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.attic;

import org.junit.Test;

/**
 *
 *
 */
public class SingleQueryTestCase
extends SimpleQueryTestBase
    {
    /**
     * Single catalog, TWOMASS.
     *
     */
    @Test
    public void test001()
    throws Exception
        {
        // endpoint="http://localhost:8089/ogsa-dai/services";

        execute(
            endpoint,
            "atlas",
            " SELECT" +
            "    twomass.ra," +
            "    twomass.dec" +
            " FROM" +
            "    TWOMASS.dbo.twomass_psc AS twomass" +
            " WHERE" +
            "    twomass.ra  BETWEEN '55.0' AND '55.9'" +
            " AND" +
            "    twomass.dec BETWEEN '20.0' AND '22.9'"
            );
        }
    }

