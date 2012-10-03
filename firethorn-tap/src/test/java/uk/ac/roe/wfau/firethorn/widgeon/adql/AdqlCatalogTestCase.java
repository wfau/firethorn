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
public class AdqlCatalogTestCase
extends JdbcResourceTestBase
    {

    @Test
    public void test003()
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
        // Select missing catalog view fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        //
        // Create base catalog.
        assertNotNull(
            base().catalogs().create(
                "catalog-A"
                )
            );
        //
        // Create view.
        assertNotNull(
            base().views().create(
                "view-A"
                )
            );
        //
        // Select catalog view.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        }

    @Test
    public void test005()
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
        // Select catalog view.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        }

    //
    // Check that view name follows base name.
    @Test
    public void test006()
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
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
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
        // Change base catalog name.
        base().catalogs().select(
            "catalog-A"
            ).name(
                "changed"
                );
        //
        // Select base with old name fails.
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                )
            );

        //
        // Select view with old name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );

        //
        // Select base with new name works.
        assertNotNull(
            base().catalogs().select(
                "changed"
                )
            );
        //
        // Select view with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "changed"
                    )
            );
        }

    //
    // Check that view name replaces base name.
    @Test
    public void test007()
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
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
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
        // Change catalog view name.
        base().views().select(
            "view-A"
            ).catalogs().select(
                "catalog-A"
                ).name(
                    "changed"
                    );
        //
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        //
        // Select catalog view with old name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Select catalog view with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "changed"
                    )
            );
        }

    //
    // Check that view name reverts to the  base name.
    @Test
    public void test008()
    throws Exception
        {
        //
        // Create the view.
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
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
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
        // Change catalog view name
        base().views().select(
            "view-A"
            ).catalogs().select(
                "catalog-A"
                ).name(
                    "view-changed"
                    );
        //
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        //
        // Select catalog view with old name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Select catalog view with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    )
            );
        //
        // Set catalog view name to null.
        base().views().select(
            "view-A"
            ).catalogs().select(
                "view-changed"
                ).name(
                    null
                    );
        //
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        //
        // Select catalog view with new name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    )
            );
        //
        // Select catalog view with old name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        }

    //
    // Check that view name reverts to new base name.
    @Test
    public void test009()
    throws Exception
        {
        //
        // Create the view.
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
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
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
        // Change catalog view name
        base().views().select(
            "view-A"
            ).catalogs().select(
                "catalog-A"
                ).name(
                    "view-changed"
                    );
        //
        // Select catalog view with old name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Select catalog view with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    )
            );
        //
        // Change base catalog name
        base().catalogs().select(
            "catalog-A"
            ).name(
                "base-changed"
                );
        //
        // Select base catalog with old name fails.
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        //
        // Select base catalog with new name works.
        assertNotNull(
            base().catalogs().select(
                "base-changed"
                )
            );
        //
        // Set catalog view name to null.
        base().views().select(
            "view-A"
            ).catalogs().select(
                "view-changed"
                ).name(
                    null
                    );
        //
        // Select base catalog with old name fails.
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        //
        // Select base catalog with new name works.
        assertNotNull(
            base().catalogs().select(
                "base-changed"
                )
            );
        //
        // Select catalog view with old name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Select catalog view with new name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    )
            );
        //
        // Select catalog view with new base name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "base-changed"
                    )
            );
        }

    //
    // Check that view name reverts to new base name.
    @Test
    public void test010()
    throws Exception
        {
        //
        // Create the view.
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
        // Select base catalog works.
        assertNotNull(
            base().catalogs().select(
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
        // Change base catalog name
        base().catalogs().select(
            "catalog-A"
            ).name(
                "base-changed"
                );
        //
        // Select base catalog with old name fails.
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        //
        // Select base catalog with new name works.
        assertNotNull(
            base().catalogs().select(
                "base-changed"
                )
            );
        //
        // Change catalog view name
        base().views().select(
            "view-A"
            ).catalogs().select(
                "base-changed"
                ).name(
                    "view-changed"
                    );
        //
        // Select catalog view with old name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Select catalog view with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    )
            );
        //
        // Set catalog view name to null.
        base().views().select(
            "view-A"
            ).catalogs().select(
                "view-changed"
                ).name(
                    null
                    );
        //
        // Select base catalog with old name fails.
        assertIsNull(
            base().catalogs().select(
                "catalog-A"
                )
            );
        //
        // Select base catalog with new name works.
        assertNotNull(
            base().catalogs().select(
                "base-changed"
                )
            );
        //
        // Select catalog view with old name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    )
            );
        //
        // Select catalog view with new name fails.
        assertIsNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    )
            );
        //
        // Select catalog view with new base name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "base-changed"
                    )
            );
        }
    }

