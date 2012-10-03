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
package uk.ac.roe.wfau.firethorn.widgeon.adql ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceTestBase;

/**
 *
 */
@Slf4j
public class AdqlSchemaTestCase
extends JdbcResourceTestBase
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
        // Create base catalog.
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                )
            );
        //
        // Select catalog view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Select missing schema view fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    ).schemas().select(
                        "schema-A"
                        )
            );
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
        // Create base catalog.
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                )
            );
        //
        // Select catalog view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Create base schema.
        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    )
            );
        //
        // Select schema view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    ).schemas().select(
                        "schema-A"
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
        // Create base catalog and schema.
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                ).schemas().create(
                    "schema-A"
                    )
            );
        //
        // Select catalog and schema view works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    ).schemas().select(
                        "schema-A"
                        )
            );
        }

    }

