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
public class AdqlCatalogTestCase
extends DataResourceTestBase
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
        try {
            base().views().select(
                "view-A"
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
        try {
            base().catalogs().select(
                "catalog-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }

        //
        // Select view with old name fails.
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }

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
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
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
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
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
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "view-changed",
                ouch.name()
                );            
            }
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
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
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
        try {
            base().catalogs().select(
                "catalog-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
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
        try {
            base().catalogs().select(
                "catalog-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
        //
        // Select base catalog with new name works.
        assertNotNull(
            base().catalogs().select(
                "base-changed"
                )
            );
        //
        // Select catalog view with old name fails.
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
        //
        // Select catalog view with new name fails.
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "view-changed",
                ouch.name()
                );            
            }
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
        try {
            base().catalogs().select(
                "catalog-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
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
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
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
        try {
            base().catalogs().select(
                "catalog-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
        //
        // Select base catalog with new name works.
        assertNotNull(
            base().catalogs().select(
                "base-changed"
                )
            );
        //
        // Select catalog view with old name fails.
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "catalog-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "catalog-A",
                ouch.name()
                );            
            }
        //
        // Select catalog view with new name fails.
        try {
            base().views().select(
                "view-A"
                ).catalogs().select(
                    "view-changed"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "view-changed",
                ouch.name()
                );            
            }
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

