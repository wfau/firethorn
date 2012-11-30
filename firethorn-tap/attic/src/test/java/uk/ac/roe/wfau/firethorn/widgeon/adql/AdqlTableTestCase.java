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

import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceTestBase;

/**
 *
 */
@Slf4j
public class AdqlTableTestCase
extends JdbcResourceTestBase
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

    private AdqlSchema schema;
    public AdqlSchema schema()
        {
        return this.schema ;
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
        this.schema = this.catalog.schemas().create(
            "schema-A"
            );
        }

    @Test
    public void test000()
    throws Exception
        {
        
        }

    @Test
    public void test001()
    throws Exception
        {
        }
    }

