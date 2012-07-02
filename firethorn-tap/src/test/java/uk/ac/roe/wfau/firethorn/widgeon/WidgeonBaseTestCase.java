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
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;


/**
 *
 */
@Slf4j
public class WidgeonBaseTestCase
extends WidgeonBaseTestBase
    {

    @Test
    public void test000()
    throws Exception
        {
        assertNotNull(
            base()
            );
        }

    @Test
    public void test001()
    throws Exception
        {
        try {
            base().sparrows().select(
                "sparrow-A"
                );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "sparrow-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test002()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                )
            );
        }

    @Test
    public void test003()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                )
            );
        assertNotNull(
            base().sparrows().select(
                "sparrow-A"
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                )
            );
        try {
            base().sparrows().select(
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
    public void test005()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    )
            );

        assertNotNull(
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    )
            );
        }

    @Test
    public void test006()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    )
            );
        try {
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "table-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test007()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    ).tables().create(
                        "table-A"
                        )
            );

        assertNotNull(
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        )
            );
        }

    @Test
    public void test008()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    ).tables().create(
                        "table-A"
                        )
            );
        try {
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test009()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        assertNotNull(
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            )
            );
        }

    @Test
    public void test010()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        try {
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-a",
                ouch.name()
                );            
            }
        }

    @Test
    public void test011()
    throws Exception
        {
        assertNotNull(
            base().sparrows().create(
                "sparrow-A"
                ).warblers().create(
                    "warbler-A"
                    ).tables().create(
                        "table-A"
                        ).columns().create(
                            "column-A"
                            )
            );

        try {
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-a",
                ouch.name()
                );            
            }

        base().sparrows().select(
            "sparrow-A"
            ).warblers().select(
                "warbler-A"
                ).tables().select(
                    "table-A"
                    ).columns().select(
                        "column-A"
                        ).name(
                            "column-a"
                            );

        assertNotNull(
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-a"
                            )
            );

        try {
            base().sparrows().select(
                "sparrow-A"
                ).warblers().select(
                    "warbler-A"
                    ).tables().select(
                        "table-A"
                        ).columns().select(
                            "column-A"
                            );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "column-A",
                ouch.name()
                );            
            }
        }
    }

