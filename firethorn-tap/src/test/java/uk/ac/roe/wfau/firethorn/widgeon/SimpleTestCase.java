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
import static org.junit.Assert.*;

import uk.ac.roe.wfau.firethorn.test.TestBase;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 *
 */
@Slf4j
public class SimpleTestCase
extends TestBase
    {

    @Test
    public void simple()
        {
        Widgeon.Base one = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        assertNotNull(
            one
            );
        assertNotNull(
            one.name()
            );
        log.debug("One [{}][{}]", one.ident(), one.name());

        Widgeon.Base two = womble().widgeons().create(
            "albert",
            URI.create("ivo://org.astrogrid.test/0001")
            );
        assertNotNull(
            two
            );
        assertNotNull(
            two.name()
            );
        log.debug("Two [{}][{}]", two.ident(), two.name());

        for (Widgeon.Base widgeon : womble().widgeons().select())
            {
            log.debug("Widgeon [{}]", widgeon);
            }

        }

    @Test
    public void nested()
        {
        nested(
            womble().widgeons().create(
                "widgeon-0001",
                URI.create("ivo://org.astrogrid.test/0001")
                )
            );
        nested(
            womble().widgeons().create(
                "widgeon-0002",
                URI.create("ivo://org.astrogrid.test/0002")
                )
            );

        for (Widgeon.Base widgeon : womble().widgeons().select())
            {
            display(
                widgeon
                );
            }
        }

    public void nested(Widgeon.Base widgeon)
        {
        nested(
            widgeon.schemas().create(
                "schema-0001"
                )
            );
        nested(
            widgeon.schemas().create(
                "schema-0002"
                )
            );
        }

    public void nested(Widgeon.Base.Schema schema)
        {
        nested(
            schema.catalogs().create(
                "catalog-0001"
                )
            );
        nested(
            schema.catalogs().create(
                "catalog-0002"
                )
            );
        }

    public void nested(Widgeon.Base.Schema.Catalog catalog)
        {
        nested(
            catalog.tables().create(
                "table-0001"
                )
            );
        nested(
            catalog.tables().create(
                "table-0002"
                )
            );
        }

    public void nested(Widgeon.Base.Schema.Catalog.Table table)
        {
        table.columns().create(
            "column-0001"
            );
        table.columns().create(
            "column-0002"
            );
        }

    public void display(Widgeon.Base widgeon)
        {
        log.debug("-------");
        log.debug("Widgeon [{}]", widgeon);
        for (Widgeon.Base.Schema schema : widgeon.schemas().select())
            {
            log.debug("  Schema [{}]", schema);
            for (Widgeon.Base.Schema.Catalog catalog : schema.catalogs().select())
                {
                log.debug("  Catalog [{}]", catalog);
                for (Widgeon.Base.Schema.Catalog.Table table : catalog.tables().select())
                    {
                    log.debug("  Table [{}]", table);
                    for (Widgeon.Base.Schema.Catalog.Table.Column column : table.columns().select())
                        {
                        log.debug("  Column [{}]", column);
                        }
                    }
                }
            }
        }
    }

