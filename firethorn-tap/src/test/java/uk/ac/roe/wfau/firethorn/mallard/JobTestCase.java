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
package uk.ac.roe.wfau.firethorn.mallard ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 *
 */
@Slf4j
public class JobTestCase
extends TestBase
    {

    private static Identifier[] ident = new Identifier[10] ;

    private static final int NUM_JOBS = 1000 ;

    @Test
    public void test000()
    throws Exception
        {
        ident[0] = womble().services().create(
            "test-mallard"
            ).ident();
        ident[1] = womble().services().create(
            "test-mallard"
            ).ident();
        }

    @Test
    public void test001()
    throws Exception
        {
        assertNotNull(
            ident[0]
            );
        DataService service = womble().services().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            0,
            count(
                service.jobs().select()
                )
            );
        service.jobs().create(
            "job-name",
            "job-adql"            
            );
        assertEquals(
            1,
            count(
                service.jobs().select()
                )
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            ident[0]
            );
        DataService service = womble().services().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            1,
            count(
                service.jobs().select()
                )
            );
        service.jobs().create(
            "job-name",
            "job-adql"            
            );
        assertEquals(
            2,
            count(
                service.jobs().select()
                )
            );
        }

/*
 * Don't rely on test sequence.
    @Test
    public void test003()
    throws Exception
        {
        assertNotNull(
            ident[0]
            );
        DataService mallard = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            2,
            count(
                mallard.jobs().select()
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        assertNotNull(
            ident[1]
            );
        DataService mallard = womble().mallards().select(
            ident[1]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            0,
            count(
                mallard.jobs().select()
                )
            );
        for (int i = 0 ; i < NUM_JOBS ; i++)
            {
            mallard.jobs().create(
                "job-name",
                "job-adql"            
                );
            }
        assertEquals(
            NUM_JOBS,
            count(
                mallard.jobs().select()
                )
            );
        }

    @Test
    public void test005()
    throws Exception
        {
        assertNotNull(
            ident[1]
            );
        DataService mallard = womble().mallards().select(
            ident[1]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            NUM_JOBS,
            count(
                mallard.jobs().select()
                )
            );
        }
 *
 */
    }

