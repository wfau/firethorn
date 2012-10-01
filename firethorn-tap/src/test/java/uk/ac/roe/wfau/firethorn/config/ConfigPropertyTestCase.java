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

import static org.junit.Assert.*;

import java.net.URI;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;


/**
 *
 *
 */
@Slf4j
public class ConfigPropertyTestCase
extends TestBase
    {

    @Test
    public void testCreate()
    throws Exception
        {
        assertNotNull(
            womble().config().create(
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
        URI key = this.unique(
            URI.create(
                "urn:test-property"
                )
            ); 
        assertNotNull(
            womble().config().create(
                key,
                "test property name",
                "test property value"
                )
            );
        assertNotNull(
            womble().config().select(
                key
                )
            );
        }

    @Test
    public void testSelectName()
    throws Exception
        {
        URI key = this.unique(
            URI.create(
                "urn:test-property"
                )
            ); 
        assertNotNull(
            womble().config().create(
                key,
                "test property name",
                "test property value"
                )
            );
        assertNotNull(
            womble().config().select(
                key
                )
            );
        assertEquals(
            "test property name",
            womble().config().select(
                key
                ).name()
            );
        }

    @Test
    public void testSelectValue()
    throws Exception
        {
        URI key = this.unique(
            URI.create(
                "urn:test-property"
                )
            ); 
        assertNotNull(
            womble().config().create(
                key,
                "test property name",
                "test property value"
                )
            );
        assertNotNull(
            womble().config().select(
                key
                )
            );
        assertEquals(
            "test property value",
            womble().config().select(
                key
                ).toString()
            );
        }
    }
