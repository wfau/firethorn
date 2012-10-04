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

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * Public interface for describing a data resource (JDBC database OR TAP service).
 * Called DataResource because the word Resource has already been used so many times to mean different things in different places.
 *
 */
public interface DataResource
extends ResourceStatus
    {

    /**
     * Access to this resource's catalogs.
     *
     */
    public Catalogs<?> catalogs();

    /**
     * Public interface for accessing a resource's catalogs.
     *
     */
    public interface Catalogs<CatalogType extends DataResource.DataCatalog<?>>
        {

        /**
         * Select all the catalogs from the resource.
         *
         */
        public Iterable<CatalogType> select();

        /**
         * Select a named catalog from the resource.
         *
         */
        public CatalogType select(final String name);

        /**
         * Text search for catalogs (name starts with).
         *
         */
        public Iterable<CatalogType> search(final String text);

        }

    /**
     * Public interface for a catalog.
     *
     */
    public interface DataCatalog<ResourceType extends DataResource>
    extends ResourceStatus
        {

        /**
         * Factory interface for creating and selecting catalogs.
         *
         */
        public static interface Factory<ResourceType extends DataResource, CatalogType extends DataResource.DataCatalog<?>>
        extends Entity.Factory<CatalogType>
            {

            /**
             * Select all the catalogs for a resource.
             *
             */
            public Iterable<CatalogType> select(final ResourceType parent);

            /**
             * Select a named catalog for a resource.
             *
             */
            public CatalogType select(final ResourceType parent, final String name);

            /**
             * Text search for catalogs (name starts with).
             *
             */
            public Iterable<CatalogType> search(final ResourceType parent, final String text);

            }

        /**
         * Access to our parent resource.
         *
         */
        public ResourceType parent();

        /**
         * Access to this catalog's schemas.
         *
         */
        public Schemas<?> schemas();

        /**
         * Public interface for accessing a catalog's schemas.
         *
         */
        public interface Schemas<SchemaType extends DataResource.DataSchema<?>>
            {

            /**
             * Select all the schemas from the catalog.
             *
             */
            public Iterable<SchemaType> select();

            /**
             * Select a named schema from the catalog.
             *
             */
            public SchemaType select(final String name);

            /**
             * Text search for schema (name starts with).
             *
             */
            public Iterable<SchemaType> search(final String text);

            }
        }

    /**
     * Public interface for a schema.
     *
     */
    public interface DataSchema<CatalogType extends DataResource.DataCatalog<?>>
    extends ResourceStatus
        {

        /**
         * Factory interface for creating and selecting schemas.
         *
         */
        public static interface Factory<CatalogType extends DataResource.DataCatalog<?>, SchemaType extends DataResource.DataSchema<?>>
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
        public Tables<?> tables();

        /**
         * Public interface for accessing a schema's tables.
         *
         */
        public interface Tables<TableType extends DataResource.DataTable<?>>
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

    /**
     * Public interface for a table.
     *
     */
    public interface DataTable<SchemaType extends DataResource.DataSchema<?>>
    extends ResourceStatus
        {

        /**
         * Factory interface for creating and selecting tables.
         *
         */
        public static interface Factory<SchemaType extends DataResource.DataSchema<?>, TableType extends DataResource.DataTable<?>>
        extends Entity.Factory<TableType>
            {

            /**
             * Select all the tables from a schema.
             *
             */
            public Iterable<TableType> select(final SchemaType parent);

            /**
             * Select a named table from a schema.
             *
             */
            public TableType select(final SchemaType parent, final String name);

            /**
             * Text search for tables (name starts with).
             *
             */
            public Iterable<TableType> search(final SchemaType parent, final String text);

            }

        /**
         * Access to our parent schema.
         *
         */
        public SchemaType parent();

        /**
         * Access to this table's columns.
         *
         */
        public Columns<?> columns();

        /**
         * Public interface for accessing a table's columns.
         *
         */
        public interface Columns<ColumnType extends DataResource.DataColumn<?>>
            {

            /**
             * Select all the columns from the table.
             *
             */
            public Iterable<ColumnType> select();

            /**
             * Select a named column from the table.
             *
             */
            public ColumnType select(final String name);

            /**
             * Text search for columns (name starts with).
             *
             */
            public Iterable<ColumnType> search(final String text);

            }
        }

    /**
     * Public interface for a column.
     *
     */
    public interface DataColumn<TableType extends DataResource.DataTable<?>>
    extends ResourceStatus
        {

        /**
         * Factory interface for creating and selecting columns.
         *
         */
        public static interface Factory<TableType extends DataResource.DataTable<?>, ColumnType extends DataResource.DataColumn<?>>
        extends Entity.Factory<ColumnType>
            {

            /**
             * Select all the columns from a table.
             *
             */
            public Iterable<ColumnType> select(final TableType parent);

            /**
             * Select a named column from a table.
             *
             */
            public ColumnType select(final TableType parent, final String name);

            /**
             * Text search for columns (name starts with).
             *
             */
            public Iterable<ColumnType> search(final TableType parent, final String text);

            }

        /**
         * Access to our parent table.
         *
         */
        public TableType parent();

        }
    }

