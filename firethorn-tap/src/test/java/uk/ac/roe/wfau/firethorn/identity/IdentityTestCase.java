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
package uk.ac.roe.wfau.firethorn.identity ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.junit.Test;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 *
 */
@Slf4j
public class IdentityTestCase
extends TestBase
    {

    private static Identifier[] ident = new Identifier[10] ;

    @Test
    public void test000()
        {
        ident[0] = womble().identities().create(
            "albert"
            ).ident();
        ident[1] = womble().identities().create(
            "albert"
            ).ident();
        }

    /**
     * Our Autowired Identity context.
     * 
     */
    @Autowired
    protected Identity.Context context ;

    @Test
    public void test001()
        {
        assertNotNull(
            context.current().ident()
            );
        ident[2] = context.current().ident();
        }

    @Test
    public void test002()
        {
        assertNotNull(
            context.current().ident()
            );
        ident[3] = context.current().ident();
        }

    @Test
    public void test003()
        {
        assertEquals(
            ident[2],
            ident[3]
            );
        }

    }

