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
public class SchemaViewTestCase
extends WidgeonViewTestBase
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
        // Select missing view schema fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException expected");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        }

    @Test
    public void test004()
    throws Exception
        {
        //
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
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
        // Select view schema.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );
        //
        // Select view schema.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );
        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
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
        // Change base schema name.
        base().schemas().select(
            "schema-A"
            ).name(
                "changed"
                );
        //
        // Select base with old name fails.
        try {
            base().schemas().select(
                "schema-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }

        //
        // Select view with old name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }

        //
        // Select base with new name works.
        assertNotNull(
            base().schemas().select(
                "changed"
                )
            );
        //
        // Select view with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );

        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
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
        // Change view schema name.
        base().views().select(
            "view-A"
            ).schemas().select(
                "schema-A"
                ).name(
                    "changed"
                    );
        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
                "schema-A"
                )
            );
        //
        // Select view schema with old name fails.        
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select view schema with new name works.        
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );
        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
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
        // Change view schema name
        base().views().select(
            "view-A"
            ).schemas().select(
                "schema-A"
                ).name(
                    "view-changed"
                    );
        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
                "schema-A"
                )
            );
        //
        // Select view schema with old name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select view schema with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "view-changed"
                    )
            );
        //
        // Set view schema name to null.
        base().views().select(
            "view-A"
            ).schemas().select(
                "view-changed"
                ).name(
                    null
                    );
        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
                "schema-A"
                )
            );
        //
        // Select view schema with new name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
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
        // Select view schema with old name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );
        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
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
        // Change view schema name
        base().views().select(
            "view-A"
            ).schemas().select(
                "schema-A"
                ).name(
                    "view-changed"
                    );
        //
        // Select view schema with old name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select view schema with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "view-changed"
                    )
            );
        //
        // Change base schema name
        base().schemas().select(
            "schema-A"
            ).name(
                "base-changed"
                );
        //
        // Select base schema with old name fails.
        try {
            base().schemas().select(
                "schema-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select base schema with new name works.
        assertNotNull(
            base().schemas().select(
                "base-changed"
                )
            );
        //
        // Set view schema name to null.
        base().views().select(
            "view-A"
            ).schemas().select(
                "view-changed"
                ).name(
                    null
                    );
        //
        // Select base schema with old name fails.
        try {
            base().schemas().select(
                "schema-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select base schema with new name works.
        assertNotNull(
            base().schemas().select(
                "base-changed"
                )
            );
        //
        // Select view schema with old name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select view schema with new name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
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
        // Select view schema with new base name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
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
        // Create base schema.
        assertNotNull(
            base().schemas().create(
                "schema-A"
                )
            );
        //
        // Select base schema works.
        assertNotNull(
            base().schemas().select(
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
        // Change base schema name
        base().schemas().select(
            "schema-A"
            ).name(
                "base-changed"
                );
        //
        // Select base schema with old name fails.
        try {
            base().schemas().select(
                "schema-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select base schema with new name works.
        assertNotNull(
            base().schemas().select(
                "base-changed"
                )
            );
        //
        // Change view schema name
        base().views().select(
            "view-A"
            ).schemas().select(
                "base-changed"
                ).name(
                    "view-changed"
                    );
        //
        // Select view schema with old name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select view schema with new name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "view-changed"
                    )
            );
        //
        // Set view schema name to null.
        base().views().select(
            "view-A"
            ).schemas().select(
                "view-changed"
                ).name(
                    null
                    );
        //
        // Select base schema with old name fails.
        try {
            base().schemas().select(
                "schema-A"
                );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select base schema with new name works.
        assertNotNull(
            base().schemas().select(
                "base-changed"
                )
            );
        //
        // Select view schema with old name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
                    "schema-A"
                    );
            fail("NameNotFoundException");
            }
        catch (NameNotFoundException ouch)
            {
            assertEquals(
                "schema-A",
                ouch.name()
                );            
            }
        //
        // Select view schema with new name fails.
        try {
            base().views().select(
                "view-A"
                ).schemas().select(
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
        // Select view schema with new base name works.
        assertNotNull(
            base().views().select(
                "view-A"
                ).schemas().select(
                    "base-changed"
                    )
            );
        }
    }

