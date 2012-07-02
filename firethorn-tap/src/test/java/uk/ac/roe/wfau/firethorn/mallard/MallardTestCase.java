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
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView;

/**
 *
 */
@Slf4j
public class MallardTestCase
extends TestBase
    {

    private Identifier[] ident = new Identifier[10] ;

    @Before
    public void before()
    throws Exception
        {
        ident[0] = womble().mallards().create(
            "albert"
            ).ident();
        ident[1] = womble().mallards().create(
            "albert"
            ).ident();
        flush();
        }

    @Test
    public void test001()
    throws Exception
        {
log.debug("--- test001() ---");
        Mallard mallard = womble().mallards().select(
            ident[0]
            );

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "albert",
            mallard.name()
            );

        mallard.name("Albert");

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            mallard.name()
            );
        }

    @Test
    public void test002()
    throws Exception
        {
log.debug("--- test002() ---");
        Mallard mallard = womble().mallards().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "albert",
            mallard.name()
            );

        mallard.name("Albert");

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            mallard.name()
            );

        flush();

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            mallard.name()
            );
        }

    @Test
    public void test003()
    throws Exception
        {
log.debug("--- test003() ---");

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        womble().mallards().select(
            ident[0]
            ).widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                ).views().create(
                    "default"
                    )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        }

    @Test
    public void test004()
    throws Exception
        {
log.debug("--- test004() ---");

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        womble().mallards().select(
            ident[0]
            ).widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                ).views().create(
                    "default"
                    )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        assertEquals(
            1,
            count(
                womble().mallards().select(
                    ident[0]
                    ).widgeons().select()
                )
            );

        }

/*
 * Can't rely on the sequence that tests are run.
 *
 
    @Test
    public void test004a()
    throws Exception
        {
log.debug("--- test004a() ---");
        assertEquals(
            1,
            count(
                womble().mallards().select(
                    ident[0]
                    ).widgeons().select()
                )
            );
        }

    @Test
    public void test004b()
    throws Exception
        {
log.debug("--- test004b() ---");
        assertEquals(
            1,
            count(
                womble().mallards().select(
                    ident[0]
                    ).widgeons().select()
                )
            );
        }
 *
 */

    @Test
    public void test005()
    throws Exception
        {
log.debug("--- test005() ---");

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        womble().mallards().select(
            ident[0]
            ).widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                ).views().create(
                    "default"
                    )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        }

    @Test
    public void test006()
    throws Exception
        {
log.debug("--- test006() ---");

        assertFalse(
            womble().hibernate().session().isDirty()
            );

        womble().mallards().select(
            ident[0]
            ).widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                ).views().create(
                    "default"
                    )
            );

        womble().mallards().select(
            ident[0]
            ).widgeons().insert(
            womble().widgeons().create(
                "0001",
                URI.create("ivo://org.astrogrid.test/0001")
                ).views().create(
                    "default"
                    )
            );

        assertTrue(
            womble().hibernate().session().isDirty()
            );

        flush();

        assertEquals(
            2,
            count(
                womble().mallards().select(
                    ident[0]
                    ).widgeons().select()
                )
            );
        }

    @Test
    public void test007()
    throws Exception
        {
log.debug("--- test007() ---");
        for (WidgeonView widgeon : womble().mallards().select(ident[0]).widgeons().select())
            {
            womble().mallards().select(
                ident[1]
                ).widgeons().insert(
                    widgeon
                    );
            }
        }

    @Test
    public void test008()
    throws Exception
        {
log.debug("--- test008() ---");
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        womble().mallards().select(
            ident[0]
            ).widgeons().insert(
                womble().widgeons().create(
                    "0001",
                    URI.create("ivo://org.astrogrid.test/0001")
                    ).views().create(
                        "default"
                        )
                );
        assertTrue(
            womble().hibernate().session().isDirty()
            );
        }

    @Test
    public void test009()
    throws Exception
        {
log.debug("--- test009() ---");
        assertEquals(
            3,
            count(
                womble().mallards().select(
                    ident[0]
                    ).widgeons().select()
                )
            );
        }

    @Test
    public void test010()
    throws Exception
        {
log.debug("--- test010() ---");
        assertEquals(
            2,
            count(
                womble().mallards().select(
                    ident[1]
                    ).widgeons().select()
                )
            );
        }
    }

