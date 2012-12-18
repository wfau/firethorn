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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.common.entity.LongIdentifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 *
 */
public class AdqlTableTestCase
extends TestBase
    {
    @Test
    public void testShortFails()
    throws Exception
        {
        try {
            factories().adql().tables().links().parse(
                "adql/table/21"
                );
            fail(
                "IdentifierFormatException expected"
                );
            }
        catch (IdentifierFormatException ouch)
            {
            }
        catch (Exception ouch)
            {
            fail(
                "IdentifierFormatException expected"
                );
            }
        }

    @Test
    public void testSimple()
    throws Exception
        {
        assertEquals(
            new LongIdentifier(
                21
                ),
            factories().adql().tables().links().parse(
                "/adql/table/21"
                )
            );
        }

    @Test
    public void testBefore()
    throws Exception
        {
        assertEquals(
            new LongIdentifier(
                21
                ),
            factories().adql().tables().links().parse(
                "frog/adql/table/21"
                )
            );
        }

    @Test
    public void testAfter()
    throws Exception
        {
        assertEquals(
            new LongIdentifier(
                21
                ),
            factories().adql().tables().links().parse(
                "/adql/table/21/frog"
                )
            );
        }

    @Test
    public void testMiddle()
    throws Exception
        {
        assertEquals(
            new LongIdentifier(
                21
                ),
            factories().adql().tables().links().parse(
                "frog/adql/table/21/frog"
                )
            );
        }

    @Test
    public void testNotBefore()
    throws Exception
        {
        try {
            factories().adql().tables().links().parse(
                "/frogadql/table/21"
                );
            fail(
                "IdentifierFormatException expected"
                );
            }
        catch (IdentifierFormatException ouch)
            {
            }
        catch (Exception ouch)
            {
            fail(
                "IdentifierFormatException expected"
                );
            }
        }

    @Test
    public void testNotAfter()
    throws Exception
        {
        try {
            factories().adql().tables().links().parse(
                "/adql/table/21frog"
                );
            fail(
                "IdentifierFormatException expected"
                );
            }
        catch (IdentifierFormatException ouch)
            {
            }
        catch (Exception ouch)
            {
            fail(
                "IdentifierFormatException expected"
                );
            }
        }

    @Test
    public void testNotMiddle()
    throws Exception
        {
        try {
            factories().adql().tables().links().parse(
                "/frogadql/table/21frog"
                );
            fail(
                "IdentifierFormatException expected"
                );
            }
        catch (IdentifierFormatException ouch)
            {
            }
        catch (Exception ouch)
            {
            fail(
                "IdentifierFormatException expected"
                );
            }
        }
    }
