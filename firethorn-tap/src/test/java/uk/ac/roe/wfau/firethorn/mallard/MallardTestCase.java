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

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

/**
 *
 */
@Slf4j
public class MallardTestCase
extends TestBase
    {

    private static Identifier[] ident = new Identifier[10] ;

    @Test
    public void test000()
        {
        ident[0] = womble().mallards().create(
            "albert"
            ).ident();
        ident[1] = womble().mallards().create(
            "albert"
            ).ident();
        }

    @Test
    public void test001()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "albert",
            object.name()
            );

        object.name("Albert");

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            object.name()
            );
        }

    @Test
    public void test002()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            object.name()
            );
        }

    @Test
    public void test003()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        object.widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );
        }


    @Test
    public void test004()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertEquals(
            1,
            count(
                object.widgeons().select()
                )
            );
        }

    @Test
    public void test005()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        object.widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );
        }

    @Test
    public void test006()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertEquals(
            2,
            count(
                object.widgeons().select()
                )
            );
        }

    @Test
    public void test007()
        {
        assertNotNull(
            ident[0]
            );
        assertNotNull(
            ident[1]
            );
        Mallard frog = womble().mallards().select(
            ident[0]
            );
        Mallard toad = womble().mallards().select(
            ident[1]
            );
        for (Widgeon widgeon : frog.widgeons().select())
            {
            toad.widgeons().insert(
                widgeon
                );
            }
        }

    @Test
    public void test008()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        object.widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        }

    @Test
    public void test009()
        {
        assertNotNull(
            ident[0]
            );
        Mallard object = womble().mallards().select(
            ident[0]
            );
        assertEquals(
            3,
            count(
                object.widgeons().select()
                )
            );
        }

    @Test
    public void test010()
        {
        assertNotNull(
            ident[1]
            );
        Mallard object = womble().mallards().select(
            ident[1]
            );
        assertEquals(
            2,
            count(
                object.widgeons().select()
                )
            );
        }
    }

