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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
public class OgsaResourceTestCase
extends TestBase
    {

    @Test
    public void test001()
    throws Exception
        {
        assertNotNull(
            factories().ogsa().services().create(
                "http://localhost:8080/example"
                )
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        final OgsaService created = factories().ogsa().services().create(
            "http://localhost:8080/example"
            );
        assertNotNull(
            factories().adql().resources().select(
                created.ident()
                )
            );
        }
    }

