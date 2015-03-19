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

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
  * Tests for {@link OgsaIvoaResource} connections.
 *
 */
public class OgsaIvoaResourceTestCase
extends TestBase
    {
    
    @Test
    public void testCreate001()
    throws Exception
        {
        final OgsaService service = factories().ogsa().services().primary();

        final IvoaResource resource = factories().ivoa().resources().create(
            "ogsa:ident",
            "ivoa:ident"
            );
        resource.endpoints().create(
            "http://exaple.org/tap"
            );

        final OgsaIvoaResource target = service.ivoa().create(
            resource
            );

        target.init();
        
        }

    
    @Test
    public void testCreate002()
    throws Exception
        {
        final OgsaService service = factories().ogsa().services().primary();

        final IvoaResource resource = factories().ivoa().resources().create(
            "ogsa:ident",
            "ivoa:ident"
            );
        resource.endpoints().create(
            "http://exaple.org/tap"
            );

        final OgsaIvoaResource target = service.ivoa().create(
            resource
            );

        target.init();

        }
    }

