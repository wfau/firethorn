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
package uk.ac.roe.wfau.firethorn.adql.query.atlas ;

import static org.junit.Assert.assertEquals;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.atlas.AtlasQueryTestBase.ExpectedField;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;


/**
 *
 *
 */
@Slf4j
public class UserFunctionsTestCase
extends AtlasQueryTestBase
    {

    @Test
    public void test001()
    throws Exception
        {
        final AdqlQuery query = this.queryspace.queries().create(
				"SELECT\n" +
                "    fDMS(87.5) as fdmscol\n" +
                "FROM\n" +
                "    Filter\n" +
                ""
                );

        log.debug(" ADQL [{}]", query.adql());
        log.debug(" OSQL [{}]", query.osql());

        assertEquals(
            AdqlQuery.Syntax.State.VALID,
            query.syntax().state()
            );
        validate(
            query,
            new ExpectedField[] {
                new ExpectedField("fdmscol", AdqlColumn.Type.CHAR, 32)
                }
            );
        compare(
            query,
            "SELECT dbo.fDMS(87.5) AS fdmscol FROM {ATLAS_VERSION}.dbo.Filter"
            );
        }
    }

