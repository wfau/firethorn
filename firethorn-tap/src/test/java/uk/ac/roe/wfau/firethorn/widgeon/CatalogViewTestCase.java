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
public class CatalogViewTestCase
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );
        //
        // Select view schema works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    )
            );
        //
        // Select missing view catalog fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    ).catalogs().select(
                        "catalog-A"
                        );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );
        //
        // Select view schema works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    )
            );
        //
        // Create base catalog.
        assertNotNull(
            base().schemas().select(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    )
            );
        //
        // Select view catalog works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    ).catalogs().select(
                        "catalog-A"
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
        // Create base schema and catalog.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                ).catalogs().create(
                    "catalog-A"
                    )
            );
        //
        // Select view scvhema and catalog works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    ).catalogs().select(
                        "catalog-A"
                        )
            );
        }

    }

