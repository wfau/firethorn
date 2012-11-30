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

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResourceTestBase;

/**
 *
 */
@Slf4j
public class AdqlColumnTestCase
extends JdbcResourceTestBase
    {
    public interface AdqlTargets
        {
        public AdqlResource resource();
        public AdqlCatalog  catalog();
        public AdqlSchema   schema();
        }
    public AdqlTargets adql()
        {
        return new AdqlTargets()
            {
            @Override
            public AdqlResource resource()
                {
                return adqlResource ;
                }
            @Override
            public AdqlCatalog catalog()
                {
                return adqlCatalog ;
                }
            @Override
            public AdqlSchema schema()
                {
                return adqlSchema ;
                }
            };
        }
    private AdqlResource adqlResource ;
    private AdqlCatalog  adqlCatalog;
    private AdqlSchema   adqlSchema;
    
    @Before
    @Override
    public void before()
    throws Exception
        {
        this.adqlResource  = womble().adql().resources().create(
            this.unique(
                "resource-A"
                )
            );
        this.adqlCatalog = this.adqlResource.catalogs().create(
            "catalog-A"
            );
        this.adqlSchema = this.adqlCatalog.schemas().create(
            "schema-A"
            );
        }

    @Test
    public void test000()
    throws Exception
        {
        }
    }

