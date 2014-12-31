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
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 * Tests for {@link OgsaService} entity classes.
 * 
 */
public class OgsaServiceTestCase
extends TestBase
    {

    public static final String EXAMPLE_ENDPOINT = "http://localhost:8080/example" ;
    
    @Test
    public void testCreate001()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().create(
            "http",
            "localhost",
            8080,
            "example"
            );
        assertNotNull(
            created
            );
        assertNotNull(
            created.ident()
            );
        assertNotNull(
            created.owner()
            );
        assertEquals(
            EXAMPLE_ENDPOINT,
            created.name()
            );
        assertEquals(
            EXAMPLE_ENDPOINT,
            created.endpoint()
            );
        }

    @Test
    public void testCreate002()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().create(
            EXAMPLE_ENDPOINT
            );
        assertNotNull(
            created
            );
        assertNotNull(
            created.ident()
            );
        assertNotNull(
            created.owner()
            );
        assertEquals(
            EXAMPLE_ENDPOINT,
            created.name()
            );
        assertEquals(
            EXAMPLE_ENDPOINT,
            created.endpoint()
            );
        }

    @Test
    public void testCreate003()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().create(
            "test.service",
            "http",
            "localhost",
            8080,
            "example"
            );
        assertNotNull(
            created
            );
        assertNotNull(
            created.ident()
            );
        assertNotNull(
            created.owner()
            );
        assertEquals(
            "test.service",
            created.name()
            );
        assertEquals(
            EXAMPLE_ENDPOINT,
            created.endpoint()
            );
        }

    @Test
    public void testCreate004()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().create(
            "test.service",
            EXAMPLE_ENDPOINT
            );
        assertNotNull(
            created
            );
        assertNotNull(
            created.ident()
            );
        assertNotNull(
            created.owner()
            );
        assertEquals(
            "test.service",
            created.name()
            );
        assertEquals(
            EXAMPLE_ENDPOINT,
            created.endpoint()
            );
        }

    @Test
    public void testCreateSelect001()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().create(
            "http",
            "localhost",
            8080,
            "example"
            );
        final OgsaService selected = factories().ogsa().services().select(
            created.ident()
            );
        assertEquals(
            created.ident(),
            selected.ident()
            );
        assertEquals(
            created.name(),
            selected.name()
            );
        assertEquals(
            created.owner().ident(),
            selected.owner().ident()
            );
        assertEquals(
            created.hashCode(),
            selected.hashCode()
            );
        assertEquals(
            created.endpoint(),
            selected.endpoint()
            );
        }

    @Test
    public void testCreateSelect002()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().create(
            EXAMPLE_ENDPOINT
            );
        final OgsaService selected = factories().ogsa().services().select(
            created.ident()
            );
        assertEquals(
            created.ident(),
            selected.ident()
            );
        assertEquals(
            created.name(),
            selected.name()
            );
        assertEquals(
            created.owner().ident(),
            selected.owner().ident()
            );
        assertEquals(
            created.hashCode(),
            selected.hashCode()
            );
        assertEquals(
            created.endpoint(),
            selected.endpoint()
            );
        }

    @Test
    public void testPrimary001()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().primary();
        assertNotNull(
            created
            );
        assertNotNull(
            created.ident()
            );
        assertNotNull(
            created.owner()
            );
        assertEquals(
            config().property(
                "firethorn.ogsadai.endpoint"
                ),
            created.name()
            );
        assertEquals(
            config().property(
                "firethorn.ogsadai.endpoint"
                ),
            created.endpoint()
            );
        }

    @Test
    public void testPrimary002()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().primary();
        assertNotNull(
            created
            );
        assertNull(
            created.http()
            );
        assertEquals(
            HttpStatus.OK,
            created.ping()
            );
        assertEquals(
            HttpStatus.OK,
            created.http()
            );
        }
    }

