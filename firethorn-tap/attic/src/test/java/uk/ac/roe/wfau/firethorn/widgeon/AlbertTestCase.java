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
package uk.ac.roe.wfau.firethorn.widgeon ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;

/**
 *
 */
@Slf4j
public class AlbertTestCase
extends TestBase
    {

    private static Identifier[] ident = new Identifier[10] ;

    @Test
    public void test000()
    throws Exception
        {
        final DataResource object = womble().jdbc().resources().create(
            "albert"
            );
/*
 * Not always true.
 * True with Postgresql, false with Hsqldb.
        assertTrue(
            womble().hibernate().session().isDirty()
            );
 *
 */
        assertEquals(
            "albert",
            object.name()
            );

        womble().hibernate().flush();
        ident[0] = object.ident();

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
    public void test001()
    throws Exception
        {
        assertNotNull(
            ident[0]
            );
        final DataResource object = womble().jdbc().resources().select(
            ident[0]
            );
        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert",
            object.name()
            );

        object.name("Albert Augustus");

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert Augustus",
            object.name()
            );

       }

    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            ident[0]
            );
        final DataResource object = womble().jdbc().resources().select(
            ident[0]
            );

        assertFalse(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert Augustus",
            object.name()
            );

        object.name("Albert Augustus Charles Emmanuel");

        assertTrue(
            womble().hibernate().session().isDirty()
            );
        assertEquals(
            "Albert Augustus Charles Emmanuel",
            object.name()
            );

       }
    }

