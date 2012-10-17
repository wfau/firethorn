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

import static org.junit.Assert.assertNotNull;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.test.TestBase;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTable;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcTable;

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
        final BaseResource one = womble().resources().jdbc().resources().create(
            "albert"
            );
        assertNotNull(
            one
            );
        assertNotNull(
            one.name()
            );
        log.debug("One [{}][{}]", one.ident(), one.name());

        final BaseResource two = womble().resources().jdbc().resources().create(
            "albert"
            );
        assertNotNull(
            two
            );
        assertNotNull(
            two.name()
            );
        log.debug("Two [{}][{}]", two.ident(), two.name());

        for (final BaseResource widgeon : womble().resources().jdbc().resources().select())
            {
            log.debug("DataResource [{}]", widgeon);
            }

        }

    @Test
    public void nested()
        {
        nested(
            womble().resources().jdbc().resources().create(
                "widgeon-0001"
                )
            );
        nested(
            womble().resources().jdbc().resources().create(
                "widgeon-0002"
                )
            );

        for (final JdbcResource widgeon : womble().resources().jdbc().resources().select())
            {
            display(
                widgeon
                );
            }
        }

    public void nested(final JdbcResource widgeon)
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

    public void nested(final JdbcCatalog catalog)
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

    public void nested(final JdbcSchema schema)
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

    public void nested(final JdbcTable table)
        {
        table.columns().create(
            "column-0001"
            );
        table.columns().create(
            "column-0002"
            );
        }

    public void display(final BaseResource widgeon)
        {
        log.debug("-------");
        log.debug("DataResource [{}]", widgeon);
        for (final BaseCatalog<?> catalog : widgeon.catalogs().select())
            {
            log.debug("  DataCatalog [{}]", catalog);
            for (final BaseSchema<?> schema : catalog.schemas().select())
                {
                log.debug("  DataSchema [{}]", schema);
                for (final BaseTable<?> table : schema.tables().select())
                    {
                    log.debug("  DataTable [{}]", table);
                    for (final BaseColumn<?> column : table.columns().select())
                        {
                        log.debug("  DataColumn [{}]", column);
                        }
                    }
                }
            }
        }
    }

