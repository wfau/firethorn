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
package uk.ac.roe.wfau.firethorn.adql.query ;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;


/**
 * 
 *
 */
@Slf4j
public class SimpleQueryTestCase
extends TwomassQueryTestBase
    {
    /**
     * Simple ADQL query for the imported table.
     *
     */
    private static final String QUERY_001 =
        "SELECT"
            + " ra,"
            + " dec,"
            + " pts_key"
        + " FROM"
            + " adql_twomass.twomass_psc"
        + " WHERE"
            + " ra BETWEEN '56.0' AND '57.9'"
        + " AND"
            + " dec BETWEEN '24.0' AND '24.2'"
        + ""
        ;

    @Test
    public void test000()
    throws Exception
        {
        debug(
            this.schema.queries().create(
                QUERY_001
                )
            );
        // TODO verify the query contents ..
        } 
    }

