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

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.exception.*;

/**
 *
 */
@Slf4j
public class WarblerViewTestCase
extends WidgeonViewTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        //
        // Create view.
        assertNotNull(
            base().views().create(
                "view-A"
                )
            );
        //
        // Create base sparrow.
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                )
            );
        //
        // Select sparrow view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).sparrows().select(
                    "sparrow-A"
                    )
            );
        //
        // Select missing warbler view fails.
        try {
            base().views().select(
                "view-A"
                ).sparrows().select(
                    "sparrow-A"
                    ).warblers().select(
                        "warbler-A"
                        );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "warbler-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test001()
    throws Exception
        {
        //
        // Create view.
        assertNotNull(
            base().views().create(
                "view-A"
                )
            );
        //
        // Create base sparrow.
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                )
            );
        //
        // Select sparrow view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).sparrows().select(
                    "sparrow-A"
                    )
            );
        //
        // Create base warbler.
        assertNotNull(
            base().sparrows().select(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    )
            );
        //
        // Select warbler view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).sparrows().select(
                    "sparrow-A"
                    ).warblers().select(
                        "warbler-A"
                        )
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        //
        // Create view.
        assertNotNull(
            base().views().create(
                "view-A"
                )
            );
        //
        // Create base sparrow and warbler.
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    )
            );
        //
        // Select sparrow and warbler view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).sparrows().select(
                    "sparrow-A"
                    ).warblers().select(
                        "warbler-A"
                        )
            );
        }

    }

