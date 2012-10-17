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
package uk.ac.roe.wfau.firethorn.widgeon.data;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * Public interface for a schema.
 *
 */
public interface DataSchema<CatalogType extends DataCatalog<?>>
extends DataStatus
    {

    /**
     * Factory interface for creating and selecting schemas.
     *
     */
    public static interface Factory<CatalogType extends DataCatalog<?>, SchemaType extends DataSchema<?>>
    extends Entity.Factory<SchemaType>
        {

        /**
         * Select all the schemas from a catalog.
         *
         */
        public Iterable<SchemaType> select(final CatalogType parent);

        /**
         * Select a named schema from a catalog.
         *
         */
        public SchemaType select(final CatalogType parent, final String name);

        /**
         * Text search for schema (name starts with).
         *
         */
        public Iterable<SchemaType> search(final CatalogType parent, final String text);

        }

    /**
     * Access to our parent catalog.
     *
     */
    public CatalogType parent();

    /**
     * Access to this schema's Tables.
     *
     */
    public DataSchema.Tables<?> tables();

    /**
     * Public interface for accessing a schema's tables.
     *
     */
    public interface Tables<TableType extends DataTable<?>>
        {

        /**
         * Select all the tables from the schema.
         *
         */
        public Iterable<TableType> select();

        /**
         * Select a named table from the schema.
         *
         */
        public TableType select(final String name);

        /**
         * Text search for tables (name starts with).
         *
         */
        public Iterable<TableType> search(final String text);

        }
    }