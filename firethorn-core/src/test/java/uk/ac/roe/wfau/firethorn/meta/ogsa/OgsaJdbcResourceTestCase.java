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

import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
public class OgsaJdbcResourceTestCase
extends TestBase
    {
        
    @Test
    public void testPing000()
    throws Exception
        {
        final OgsaService service = factories().ogsa().services().create(
            "http://localhost:8081/albert"
            );
        assertNotNull(
            service
            );
        assertNull(
            service.http()
            );
        assertEquals(
            HttpStatus.OK,
            service.ping()
            );
        assertEquals(
            HttpStatus.OK,
            service.http()
            );
        }
    
    @Test
    public void testCreate001()
    throws Exception
        {
        final OgsaService service = factories().ogsa().services().create(
            "http://localhost:8081/albert/services"
            );

        JdbcResource resource = factories().jdbc().resources().create(
            "ogsa-id",
            "ATLASDR1",
            "atlas",
            "jdbc:jtds:sqlserver://localhost:1432/ATLASDR1",
            "user",
            "pass",
            "net.sourceforge.jtds.jdbc.Driver"
            );
        
        OgsaJdbcResource created = service.jdbc().create(
            resource
            );

        created.init();
        
        }

    @Test
    public void testCreate002()
    throws Exception
        {
        final OgsaService service = factories().ogsa().services().create(
            "http://localhost:8081/albert/services"
            );

        JdbcResource resource = factories().jdbc().resources().create(
            "ogsa-id",
            "ATLASDR1",
            "atlas",
            "jdbc:jtds:sqlserver://localhost:1432/ATLASDR1",
            "user",
            "pass",
            "net.sourceforge.jtds.jdbc.Driver"
            );
        
        OgsaJdbcResource created = service.jdbc().create(
            resource
            );

        created.init();
        created.init();
        
        }

    @Test
    public void testCreate003()
    throws Exception
        {
        for (int i = 0 ; i < 10 ; i++)
            {
            testCreate002();
            }
        }
    }

