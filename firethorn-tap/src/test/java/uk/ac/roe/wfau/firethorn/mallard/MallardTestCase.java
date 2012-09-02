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

import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceBase;
import uk.ac.roe.wfau.firethorn.widgeon.DataResourceView;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.*;

/**
 *
 */
@Slf4j
public class MallardTestCase
extends TestBase
    {

    private DataService alpha ;
    public  DataService alpha()
        {
        return this.alpha ;
        }

    private DataService beta ;
    public  DataService beta()
        {
        return this.beta ;
        }

    @Before
    public void before()
    throws Exception
        {
        alpha = womble().services().create(
            this.unique(
                "mallard-A"
                )
            );
        beta = womble().services().create(
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

        alpha().resources().insert(
            womble().resources().create(
                this.unique(
                    "widgeon-A"
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
        }

    @Test
    public void test002()
    throws Exception
        {
        flush();
        assertFalse(
            womble().hibernate().session().isDirty()
            );

        alpha().resources().insert(
            womble().resources().create(
                this.unique(
                    "widgeon-A"
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
                alpha().resources().select()
                )
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        alpha().resources().insert(
            womble().resources().create(
                this.unique(
                    "widgeon-A"
                    )
                ).views().create(
                    "default"
                    )
            );

        assertEquals(
            1,
            count(
                womble().services().select(
                    alpha().ident()
                    ).resources().select()
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        alpha().resources().insert(
            womble().resources().create(
                this.unique(
                    "widgeon-A"
                    )
                ).views().create(
                    "default"
                    )
            );
        alpha().resources().insert(
            womble().resources().create(
                this.unique(
                    "widgeon-B"
                    )
                ).views().create(
                    "default"
                    )
            );

        assertEquals(
            2,
            count(
                alpha().resources().select()
                )
            );
        }


    @Test
    public void test005()
    throws Exception
        {
        DataResourceBase base = womble().resources().create(
            this.unique(
                "widgeon-A"
                )
            );

        alpha().resources().insert(
            base.views().create(
                "view-A"
                )
            );
        alpha().resources().insert(
            base.views().create(
                "view-B"
                )
            );

        assertEquals(
            2,
            count(
                alpha().resources().select()
                )
            );
        }
    }

