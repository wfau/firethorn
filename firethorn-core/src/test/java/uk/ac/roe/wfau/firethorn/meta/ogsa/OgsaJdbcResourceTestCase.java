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
package uk.ac.roe.wfau.firethorn.meta.ogsa ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 * Tests for {@link OgsaJdbcResource} connections.
 *
 */
public class OgsaJdbcResourceTestCase
extends TestBase
    {
    
    @Test
    public void testCreate001()
    throws Exception
        {
        final OgsaService service = factories().ogsa().services().entities().primary();

        final JdbcResource resource = factories().jdbc().resources().entities().create(
            "ATLASDR1",
            "atlas",
            config().property(
                "firethorn.atlas.url"
                ),
            config().property(
                "firethorn.atlas.user"
                ),
            config().property(
                "firethorn.atlas.pass"
                ),
            config().property(
                "firethorn.atlas.driver"
                )
            );
        
        final OgsaJdbcResource target = service.jdbc().create(
            resource
            );

        assertEquals(
            OgsaBaseResource.OgsaStatus.CREATED,
            target.ogstatus()
            );

        assertNotNull(
            target.ogsaid()
            );

        assertEquals(
            OgsaBaseResource.OgsaStatus.ACTIVE,
            target.ogstatus()
            );
        
        }

    @Test
    public void testCreate002()
    throws Exception
        {
        final JdbcResource resource = factories().jdbc().resources().entities().create(
            "ATLASDR1",
            "atlas",
            config().property(
                "firethorn.atlas.url"
                ),
            config().property(
                "firethorn.atlas.user"
                ),
            config().property(
                "firethorn.atlas.pass"
                ),
            config().property(
                "firethorn.atlas.driver"
                )
            );

        final OgsaJdbcResource target = resource.ogsa().primary();

        assertEquals(
            OgsaBaseResource.OgsaStatus.CREATED,
            target.ogstatus()
            );

        assertNotNull(
            target.ogsaid()
            );

        assertEquals(
            OgsaBaseResource.OgsaStatus.ACTIVE,
            target.ogstatus()
            );
        }
    }

