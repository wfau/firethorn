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
package uk.ac.roe.wfau.firethorn.widgeon.base ;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;

/**
 * Public interface for a physical resource, describing a real data resource (JDBC database OR TAP service).
 *
 */
public interface BaseResource
extends DataResource
    {
    /**
     * Factory interface for creating and selecting resources.
     *
     */
    public static interface Factory
    extends FactoryTemplate<BaseResource>
        {
        }

    /**
     * Factory interface for creating and selecting resources.
     *
     */
    public static interface FactoryTemplate<ResourceType extends BaseResource>
    extends Entity.Factory<ResourceType>
        {
        /**
         * Select all of the resources.
         *
         */
        public Iterable<ResourceType> select();

        /**
         * Select resources by name.
         *
         */
        public Iterable<ResourceType> select(final String name);

        /**
         * Text search for resources (name starts with).
         *
         */
        public Iterable<ResourceType> search(final String text);

        /**
         * Access to our view factory.
         *
         */
        public AdqlResource.Factory views();

        }

    /**
     * Public interface for accessing a resource's Views.
     *
     */
    public interface Views
        {
        /**
         * Create a new view of the resource.
         *
         */
        public AdqlResource create(final String name);

        /**
         * Select all the views of the resource.
         *
         */
        public Iterable<AdqlResource> select();

        /**
         * Select a named view of the resource.
         *
         */
        public AdqlResource select(final String name);

        }

    /**
     * Access to this resource's Views.
     *
     */
    public BaseResource.Views views();

    /**
     * Public interface for accessing a resource's catalogs.
     *
     */
    public interface Catalogs<CatalogType extends BaseResource.BaseCatalog<?>>
    extends DataResource.Catalogs<CatalogType>
        {
        }

    /**
     * Access to this resource's catalogs.
     *
     */
    @Override
    public Catalogs<?> catalogs();

    /**
     * Public interface for a catalog.
     *
     */
    public interface BaseCatalog<ResourceType extends BaseResource>
    extends DataResource.DataCatalog<ResourceType>
        {
        /**
         * Factory interface for accessing catalogs.
         *
         */
        public static interface Factory<ResourceType extends BaseResource, CatalogType extends BaseResource.BaseCatalog<ResourceType>>
        extends DataResource.DataCatalog.Factory<ResourceType, CatalogType>
            {
            /**
             * Access to our View factory.
             *
             */
            public AdqlResource.AdqlCatalog.Factory views();

            }

        /**
         * Public interface for accessing a catalog's views.
         *
         */
        public interface Views
            {

            /**
             * Select all the views of the catalog.
             *
             */
            public Iterable<AdqlResource.AdqlCatalog> select();

            /**
             * Search for catalog view based on parent resource.
             *
             */
            public AdqlResource.AdqlCatalog search(final AdqlResource parent);

            }

        /**
         * Access to this catalog's Views.
         *
         */
        public BaseResource.BaseCatalog.Views views();

        /**
         * Public interface for accessing a catalog's schemas.
         *
         */
        public interface Schemas<SchemaType extends BaseResource.BaseSchema<?>>
        extends DataResource.DataCatalog.Schemas<SchemaType>
            {
            }

        /**
         * Access to this catalog's schemas.
         *
         */
        @Override
        public BaseResource.BaseCatalog.Schemas<?> schemas();

        /**
         * Access to our parent resource.
         *
         */
        public BaseResource resource();

        }

    /**
     * Public interface for a schema.
     *
     */
    public interface BaseSchema<CatalogType extends BaseResource.BaseCatalog<?>>
    extends DataResource.DataSchema<CatalogType>
        {
        /**
         * Factory interface for accessing schemas.
         *
         */
        public static interface Factory<CatalogType extends BaseResource.BaseCatalog<?>, SchemaType extends BaseResource.BaseSchema<CatalogType>>
        extends DataResource.DataSchema.Factory<CatalogType, SchemaType>
            {
            /**
             * Access to our View factory.
             *
             */
            public AdqlResource.AdqlSchema.Factory views();

            }

        /**
         * Public interface for accessing a schema's Views.
         *
         */
        public interface Views
            {
            /**
             * Select all the views of the schema.
             *
             */
            public Iterable<AdqlResource.AdqlSchema> select();

            /**
             * Search for schema view based on parent resource.
             *
             */
            public AdqlResource.AdqlSchema search(final AdqlResource parent);

            /**
             * Search for schema view based on parent catalog.
             *
             */
            public AdqlResource.AdqlSchema search(final AdqlResource.AdqlCatalog parent);

            }

        /**
         * Access to this schema's views.
         *
         */
        public Views views();

        /**
         * Public interface for accessing a schema's tables.
         *
         */
        public interface Tables<TableType extends BaseResource.BaseTable<?>>
        extends DataResource.DataSchema.Tables<TableType>
            {
            }

        /**
         * Access to this schema's tables.
         *
         */
        @Override
        public Tables<?> tables();

        /**
         * Access to our parent resource.
         *
         */
        public BaseResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public CatalogType catalog();

        }

    /**
     * Public interface for a table.
     *
     */
    public interface BaseTable<SchemaType extends BaseResource.BaseSchema<?>>
    extends DataResource.DataTable<SchemaType>
        {
        /**
         * Factory interface for creating and selecting tables.
         *
         */
        public static interface Factory<SchemaType extends BaseResource.BaseSchema<?>, TableType extends BaseResource.BaseTable<SchemaType>>
        extends DataResource.DataTable.Factory<SchemaType, TableType>
            {
            /**
             * Access to our view factory.
             *
             */
            public AdqlResource.AdqlTable.Factory views();

            }

        /**
         * Public interface for accessing a table's Views.
         *
         */
        public interface Views
            {
            /**
             * Select all the views of the table.
             *
             */
            public Iterable<AdqlResource.AdqlTable> select();

            /**
             * Search for table view based on parent resource.
             *
             */
            public AdqlResource.AdqlTable search(final AdqlResource parent);

            /**
             * Search for table view based on parent catalog.
             *
             */
            public AdqlResource.AdqlTable search(final AdqlResource.AdqlCatalog parent);

            /**
             * Search for table view based on parent schema.
             *
             */
            public AdqlResource.AdqlTable search(final AdqlResource.AdqlSchema parent);

            }

        /**
         * Access to this table's views.
         *
         */
        public Views views();

        /**
         * Public interface for accessing a table's columns.
         *
         */
        public interface Columns<ColumnType extends BaseResource.BaseColumn<?>>
        extends DataResource.DataTable.Columns<ColumnType>
            {
            }

        /**
         * Access to this table's Columns.
         *
         */
        @Override
        public Columns<?> columns();

        /**
         * Access to our parent resource.
         *
         */
        public BaseResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public BaseResource.BaseCatalog<?> catalog();

        /**
         * Access to our parent schema.
         *
         */
        public SchemaType schema();

        }

    /**
     * Public interface for a column.
     *
     */
    public interface BaseColumn<TableType extends BaseResource.BaseTable<?>>
    extends DataResource.DataColumn<TableType>
        {
        /**
         * Factory interface for creating and selecting columns.
         *
         */
        public static interface Factory<TableType extends BaseResource.BaseTable<?>, ColumnType extends BaseResource.BaseColumn<TableType>>
        extends DataResource.DataColumn.Factory<TableType, ColumnType>
            {
            /**
             * Access to our view factory.
             *
             */
            public AdqlResource.AdqlColumn.Factory views();

            }

        /**
         * Public interface for accessing a column's views.
         *
         */
        public interface Views
            {
            /**
             * Select all the views of the column.
             *
             */
            public Iterable<AdqlResource.AdqlColumn> select();

            /**
             * Search for column view based on parent resource.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource parent);

            /**
             * Search for column view based on parent catalog.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource.AdqlCatalog parent);

            /**
             * Search for column view based on parent schema.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource.AdqlSchema parent);

            /**
             * Search for column view based on parent table.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource.AdqlTable parent);

            }

        /**
         * Access to this column's views.
         *
         */
        public Views views();

        /**
         * Access to our parent resource.
         *
         */
        public BaseResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public BaseResource.BaseCatalog<?> catalog();

        /**
         * Access to our parent schema.
         *
         */
        public BaseResource.BaseSchema<?> schema();

        /**
         * Access to our parent table.
         *
         */
        public TableType table();

        }
    }

