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

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.*;

/**
 *
 */
@Slf4j
public class MallardTestCase
extends TestBase
    {

    private Mallard alpha ;
    public  Mallard alpha()
        {
        return this.alpha ;
        }

    private Mallard beta ;
    public  Mallard beta()
        {
        return this.beta ;
        }

    @Before
    public void before()
    throws Exception
        {
        alpha = womble().mallards().create(
            this.unique(
                "mallard-A"
                )
            );
        beta = womble().mallards().create(
            this.unique(
                "mallard-B"
                )
            );
        }

    @Test
    public void test000()
    throws Exception
        {
        String prev = alpha().name();
        String next = this.unique(
            "mallard-a"
            );
        assertFalse(
            prev.equals(
                next
                )
            );

        flush();
        assertEquals(
            prev,
            alpha().name()
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );

        alpha.name(
            next
            );

        assertEquals(
            next,
            alpha().name()
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        flush();
        assertEquals(
            next,
            alpha().name()
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        }

    @Test
    public void test001()
    throws Exception
        {
        flush();
        assertFalse(
            womble().hibernate().session().isDirty()
            );

        alpha().widgeons().insert(
            womble().widgeons().create(
                this.unique(
                    "widgeon-A"
                    ),
                URI.create("ivo://org.astrogrid.test/0000")
                ).views().create(
                    "default"
                    )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        flush();
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        flush();
        assertFalse(
            womble().hibernate().session().isDirty()
            );

        alpha().widgeons().insert(
            womble().widgeons().create(
                this.unique(
                    "widgeon-A"
                    ),
                URI.create(
                    "ivo://org.astrogrid.test/0000"
                    )
                ).views().create(
                    "default"
                    )
            );
        assertTrue(
            womble().hibernate().session().isDirty()
            );

        flush();
        assertFalse(
            womble().hibernate().session().isDirty()
            );

        assertEquals(
            1,
            count(
                alpha().widgeons().select()
                )
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        alpha().widgeons().insert(
            womble().widgeons().create(
                this.unique(
                    "widgeon-A"
                    ),
                URI.create(
                    "ivo://org.astrogrid.test/0000"
                    )
                ).views().create(
                    "default"
                    )
            );

        assertEquals(
            1,
            count(
                womble().mallards().select(
                    alpha().ident()
                    ).widgeons().select()
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        alpha().widgeons().insert(
            womble().widgeons().create(
                this.unique(
                    "widgeon-A"
                    ),
                URI.create(
                    "ivo://org.astrogrid.test/0000"
                    )
                ).views().create(
                    "default"
                    )
            );
        alpha().widgeons().insert(
            womble().widgeons().create(
                this.unique(
                    "widgeon-B"
                    ),
                URI.create(
                    "ivo://org.astrogrid.test/0001"
                    )
                ).views().create(
                    "default"
                    )
            );

        assertEquals(
            2,
            count(
                alpha().widgeons().select()
                )
            );
        }


    @Test
    public void test005()
    throws Exception
        {
        WidgeonBase base = womble().widgeons().create(
            this.unique(
                "widgeon-A"
                ),
            URI.create(
                "ivo://org.astrogrid.test/0000"
                )
            );

        alpha().widgeons().insert(
            base.views().create(
                "view-A"
                )
            );
        alpha().widgeons().insert(
            base.views().create(
                "view-B"
                )
            );

        assertEquals(
            2,
            count(
                alpha().widgeons().select()
                )
            );
        }
    }

