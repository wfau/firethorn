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
        DataResourceBase one = womble().resources().create(
            "albert"
            );
        assertNotNull(
            one
            );
        assertNotNull(
            one.name()
            );
        log.debug("One [{}][{}]", one.ident(), one.name());

        DataResourceBase two = womble().resources().create(
            "albert"
            );
        assertNotNull(
            two
            );
        assertNotNull(
            two.name()
            );
        log.debug("Two [{}][{}]", two.ident(), two.name());

        for (DataResourceBase widgeon : womble().resources().select())
            {
            log.debug("DataResource [{}]", widgeon);
            }

        }

    @Test
    public void nested()
        {
        nested(
            womble().resources().create(
                "widgeon-0001"
                )
            );
        nested(
            womble().resources().create(
                "widgeon-0002"
                )
            );

        for (DataResourceBase widgeon : womble().resources().select())
            {
            display(
                widgeon
                );
            }
        }

    public void nested(DataResourceBase widgeon)
        {
        nested(
            widgeon.catalogs().create(
                "catalog-0001"
                )
            );
        nested(
            widgeon.catalogs().create(
                "catalog-0002"
                )
            );
        }

    public void nested(DataResourceBase.Catalog catalog)
        {
        nested(
            catalog.schemas().create(
                "schema-0001"
                )
            );
        nested(
            catalog.schemas().create(
                "schema-0002"
                )
            );
        }

    public void nested(DataResourceBase.Schema schema)
        {
        nested(
            schema.tables().create(
                "table-0001"
                )
            );
        nested(
            schema.tables().create(
                "table-0002"
                )
            );
        }

    public void nested(DataResourceBase.Table table)
        {
        table.columns().create(
            "column-0001"
            );
        table.columns().create(
            "column-0002"
            );
        }

    public void display(DataResourceBase widgeon)
        {
        log.debug("-------");
        log.debug("DataResource [{}]", widgeon);
        for (DataResourceBase.Catalog catalog : widgeon.catalogs().select())
            {
            log.debug("  Catalog [{}]", catalog);
            for (DataResourceBase.Schema schema : catalog.schemas().select())
                {
                log.debug("  Schema [{}]", schema);
                for (DataResourceBase.Table table : schema.tables().select())
                    {
                    log.debug("  Table [{}]", table);
                    for (DataResourceBase.Column column : table.columns().select())
                        {
                        log.debug("  Column [{}]", column);
                        }
                    }
                }
            }
        }
    }

