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
package uk.ac.roe.wfau.firethorn.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;


/**
 *
 *
 */
public class ConfigPropertyTestCase
extends TestBase
    {

    @Test
    public void testCreate()
    throws Exception
        {
        assertNotNull(
            factories().config().create(
                this.unique(
                    URI.create(
                        "urn:test-property"
                        )
                    ),
                "test property name",
                "test property value"
                )
            );

        }

    @Test
    public void testSelect()
    throws Exception
        {
        final URI key = this.unique(
            URI.create(
                "urn:test-property"
                )
            );
        assertNotNull(
            factories().config().create(
                key,
                "test property name",
                "test property value"
                )
            );
        assertNotNull(
            factories().config().select(
                key
                )
            );
        }

    @Test
    public void testSelectName()
    throws Exception
        {
        final URI key = this.unique(
            URI.create(
                "urn:test-property"
                )
            );
        assertNotNull(
            factories().config().create(
                key,
                "test property name",
                "test property value"
                )
            );
        assertNotNull(
            factories().config().select(
                key
                )
            );
        assertEquals(
            "test property name",
            factories().config().select(
                key
                ).name()
            );
        }

    @Test
    public void testSelectValue()
    throws Exception
        {
        final URI key = this.unique(
            URI.create(
                "urn:test-property"
                )
            );
        assertNotNull(
            factories().config().create(
                key,
                "test property name",
                "test property value"
                )
            );
        assertNotNull(
            factories().config().select(
                key
                )
            );
        assertEquals(
            "test property value",
            factories().config().select(
                key
                ).toString()
            );
        }
    }
