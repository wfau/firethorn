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

import static org.junit.Assert.assertNotNull;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;

/**
 *
 */
@Slf4j
public class AdqlSchemaTestCase
extends TestBase
    {

    private AdqlResource resource ;
    public AdqlResource resource()
        {
        return this.resource ;
        }

    private AdqlCatalog catalog;
    public AdqlCatalog catalog()
        {
        return this.catalog ;
        }
    
    @Before
    @Override
    public void before()
    throws Exception
        {
        this.resource  = womble().adql().resources().create(
            this.unique(
                "resource-A"
                )
            );
        this.catalog = this.resource.catalogs().create(
            "catalog-A"
            );
        }
    
    @Test
    public void test003()
    throws Exception
        {
        //
        // Select missing schema fails.
        assertIsNull(
            catalog().schemas().select(
                "schema-A"
                )
            );
        }

    @Test
    public void test004()
    throws Exception
        {
        //
        // Create catalog with name.
        assertNotNull(
            catalog().schemas().create(
                "schema-A"
                )
            );
        //
        // Select catalog by name.
        assertNotNull(
            catalog().schemas().select(
                "schema-A"
                )
            );
        }
    }

