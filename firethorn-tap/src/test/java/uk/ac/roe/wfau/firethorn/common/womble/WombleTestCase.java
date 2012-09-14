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
package uk.ac.roe.wfau.firethorn.common.womble ;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
public class WombleTestCase
extends TestBase
    {

    @Test
    public void simple()
        {
        assertNotNull(
            womble()
            );

        assertNotNull(
            womble().hibernate()
            );

        assertNotNull(
            womble().hibernate().factory()
            );

        assertNotNull(
            womble().hibernate().session()
            );

        assertNotNull(
            womble().resources()
            );

        assertNotNull(
            womble().services()
            );
        }
    }

