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

/**
 *
 */
@Slf4j
public class SequenceTestCase
extends TestBase
    {

    private String prefix ;

    @Before
    public void before()
        {
        prefix = unique();
        }

    public String prefix(String name)
        {
        StringBuilder builder = new StringBuilder();
        builder.append(
            prefix
            );
        builder.append(
            "-"
            );
        builder.append(
            name
            );
        return builder.toString();
        }

    public Widgeon.Base create(String base)
        {
        return womble().widgeons().create(
            prefix(
                base
                ),
            URI.create(
                "ivo://org.astrogrid.test/test-data"
                )
            );
        }

    public Widgeon.Base.Schema create(String base, String schema)
        {
        return this.select(
            base
            ).schemas().create(
                schema
                );
        }

    public Widgeon.Base.Schema.Catalog create(String base, String schema, String catalog)
        {
        return this.select(
            base
            ).schemas().select(
                schema
                ).catalogs().create(
                    catalog
                    );
        }

    public Widgeon.Base.Schema.Catalog.Table create(String base, String schema, String catalog, String table)
        {
        return this.select(
            base
            ).schemas().select(
                schema
                ).catalogs().select(
                    catalog
                    ).tables().create(
                        table
                        );
        }

    public Widgeon.Base.Schema.Catalog.Table.Column create(String base, String schema, String catalog, String table, String column)
        {
        return this.select(
            base
            ).schemas().select(
                schema
                ).catalogs().select(
                    catalog
                    ).tables().select(
                        table
                        ).columns().create(
                            column
                            );
        }


    public Widgeon.Base select(String base)
        {
        return womble().widgeons().select(
            prefix(
                base
                )
            );
        }

    public Widgeon.Base.Schema select(String base, String schema)
        {
        return this.select(
            base
            ).schemas().select(
                schema
                );
        }

    public Widgeon.Base.Schema.Catalog select(String base, String schema, String catalog)
        {
        return this.select(
            base
            ).schemas().select(
                schema
                ).catalogs().select(
                    catalog
                    );
        }

    public Widgeon.Base.Schema.Catalog.Table select(String base, String schema, String catalog, String table)
        {
        return this.select(
            base
            ).schemas().select(
                schema
                ).catalogs().select(
                    catalog
                    ).tables().select(
                        table
                        );
        }

    public Widgeon.Base.Schema.Catalog.Table.Column select(String base, String schema, String catalog, String table, String column)
        {
        return this.select(
            base
            ).schemas().select(
                schema
                ).catalogs().select(
                    catalog
                    ).tables().select(
                        table
                        ).columns().select(
                            column
                            );
        }

    @Test
    public void test000()
        {
        assertNotNull(
            create(
                "base-A"
                )
            );
        }

    @Test
    public void test001()
        {
        assertNotNull(
            create(
                "base-A"
                )
            );
        assertNotNull(
            select(
                "base-A"
                )
            );

        }

    @Test
    public void test002()
        {
        assertNotNull(
            create(
                "base-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A"
                )
            );

        assertNotNull(
            select(
                "base-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A"
                )
            );
        }

    @Test
    public void test003()
        {
        assertNotNull(
            create(
                "base-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A",
                "catalog-A"
                )
            );

        assertNotNull(
            select(
                "base-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A",
                "catalog-A"
                )
            );
        }

    @Test
    public void test004()
        {
        assertNotNull(
            create(
                "base-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A",
                "catalog-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A",
                "catalog-A",
                "table-A"
                )
            );

        assertNotNull(
            select(
                "base-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A",
                "catalog-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A",
                "catalog-A",
                "table-A"
                )
            );
        }

    @Test
    public void test005()
        {
        assertNotNull(
            create(
                "base-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A",
                "catalog-A"
                )
            );
        assertNotNull(
            create(
                "base-A",
                "schema-A",
                "catalog-A",
                "table-A"
                )
            );

        assertNotNull(
            create(
                "base-A",
                "schema-A",
                "catalog-A",
                "table-A",
                "column-A"
                )
            );

        assertNotNull(
            select(
                "base-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A",
                "catalog-A"
                )
            );
        assertNotNull(
            select(
                "base-A",
                "schema-A",
                "catalog-A",
                "table-A"
                )
            );

        assertNotNull(
            select(
                "base-A",
                "schema-A",
                "catalog-A",
                "table-A",
                "column-A"
                )
            );
        }
    }


