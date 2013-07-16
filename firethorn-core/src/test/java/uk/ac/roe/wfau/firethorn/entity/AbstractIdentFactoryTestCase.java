/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.entity;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class AbstractIdentFactoryTestCase
    {

    private Entity.IdentFactory factory = new AbstractIdentFactory();
    
    @Test
    public void test000()
        {
        assertTrue(
            factory.ident("0001") instanceof LongIdentifier
            );
        }

    @Test(expected = IdentifierFormatException.class)
    public void test001()
        {
        assertFalse(
            factory.ident("000A") instanceof LongIdentifier
            );
        }

    @Test
    public void test002()
        {
        assertEquals(
            new Long(1),
            factory.ident("0001").value()
            );
        }

    @Test
    public void test010()
        {
        assertTrue(
            factory.ident("(0001:002)") instanceof ProxyIdentifier
            );
        }

    @Test
    public void test011()
        {
        assertEquals(
            new Long(1),
            ((ProxyIdentifier)factory.ident("(0001:002)")).parent().value()
            );
        }

    @Test
    public void test012()
        {
        assertEquals(
            new Long(2),
            ((ProxyIdentifier)factory.ident("(0001:002)")).base().value()
            );
        }

    @Test
    public void test020()
        {
        assertTrue(
            factory.ident("((0001:002):(0003:004))") instanceof ProxyIdentifier
            );
        }

    @Test
    public void test021()
        {
        assertTrue(
            ((ProxyIdentifier)factory.ident("((0001:002):(0003:004))")).parent() instanceof ProxyIdentifier
            );
        }

    @Test
    public void test022()
        {
        assertTrue(
            ((ProxyIdentifier)factory.ident("((0001:002):(0003:004))")).base() instanceof ProxyIdentifier
            );
        }
    
    @Test
    public void test023()
        {
        assertEquals(
            new Long(1),
            ((ProxyIdentifier) ((ProxyIdentifier) factory.ident("((0001:002):(0003:004))")).parent()).parent().value()
            );
        }

    @Test
    public void test024()
        {
        assertEquals(
            new Long(2),
            ((ProxyIdentifier) ((ProxyIdentifier) factory.ident("((0001:002):(0003:004))")).parent()).base().value()
            );
        }

    @Test
    public void test025()
        {
        assertEquals(
            new Long(3),
            ((ProxyIdentifier) ((ProxyIdentifier) factory.ident("((0001:002):(0003:004))")).base()).parent().value()
            );
        }

    @Test
    public void test026()
        {
        assertEquals(
            new Long(4),
            ((ProxyIdentifier) ((ProxyIdentifier) factory.ident("((0001:002):(0003:004))")).base()).base().value()
            );
        }
    }
