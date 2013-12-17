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
				"SELECT TOP 5\n" +
                "    fHMS(ra),\n" +
                "    fDMS(dec)\n" +
                "FROM\n" +
                "    atlasSource\n" +
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
                new ExpectedField("fHMS", AdqlColumn.Type.CHAR, 32),
                new ExpectedField("fDMS", AdqlColumn.Type.CHAR, 32)
                }
            );
        validate(
            query,
            "SELECT TOP 5 {ATLAS_VERSION}.dbo.fHMS({ATLAS_VERSION}.dbo.atlasSource.ra) AS fhms, {ATLAS_VERSION}.dbo.fDMS({ATLAS_VERSION}.dbo.atlasSource.dec) AS fdms FROM {ATLAS_VERSION}.dbo.atlasSource"
            );
        }

    
    @Test
    public void test002()
    throws Exception
        {
        final AdqlQuery query = this.queryspace.queries().create(
                "SELECT TOP 5\n" +
                "    fHMS(ra)  AS fra,\n" +
                "    fDMS(dec) AS fdec\n" +
                "FROM\n" +
                "    atlasSource\n" +
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
                new ExpectedField("fra",  AdqlColumn.Type.CHAR, 32),
                new ExpectedField("fdec", AdqlColumn.Type.CHAR, 32)
                }
            );
        validate(
            query,
            "SELECT TOP 3 {ATLAS_VERSION}.dbo.fHMS({ATLAS_VERSION}.dbo.atlasSource.ra) AS fra, {ATLAS_VERSION}.dbo.fDMS({ATLAS_VERSION}.dbo.atlasSource.dec) AS fdec FROM {ATLAS_VERSION}.dbo.atlasSource"
            );
        }
    
    }

