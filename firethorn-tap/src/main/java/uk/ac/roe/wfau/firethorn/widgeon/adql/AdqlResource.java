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

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;

/**
 * Public interface for a ADQL view of a DataResource.
 *
 */
public interface AdqlResource
extends DataResource
    {

    /**
     * Factory interface for creating and selecting views.
     *
     */
    public static interface Factory
    extends Entity.Factory<AdqlResource>
        {

        /**
         * Create a view of a resource.
         *
         */
        public AdqlResource create(final BaseResource base, final String name);

        /**
         * Select all the views of a resource.
         *
         */
        public Iterable<AdqlResource> select(final BaseResource base);

        /**
         * Select a view of a resource by name.
         *
         */
        public AdqlResource select(final BaseResource base, final String name)
        throws NameNotFoundException;

        /**
         * Search for a view of a resource by name.
         *
         */
        public AdqlResource search(final BaseResource base, final String name);

        /**
         * Access to our catalog factory.
         *
         */
        public AdqlResource.AdqlCatalog.Factory catalogs();

        }

    /**
     * Access to our base resource.
     *
     */
    public BaseResource base();

    /**
     * Public interface for accessing a resource's catalogs.
     *
     */
    public interface Catalogs
    extends DataResource.Catalogs<AdqlResource.AdqlCatalog>
        {
        }

    /**
     * Access to this resource's catalogs.
     *
     */
    @Override
    public Catalogs catalogs();

    /**
     * Public interface for a catalog view.
     *
     */
    public interface AdqlCatalog
    extends DataResource.DataCatalog<AdqlResource>
        {

        /**
         * Factory interface for creating and selecting catalog views.
         *
         */
        public static interface Factory
        extends DataResource.DataCatalog.Factory<AdqlResource, AdqlResource.AdqlCatalog>
            {

            /**
             * Find an existing catalog view, or create a new one.
             *
             */
            public AdqlResource.AdqlCatalog cascade(final AdqlResource parent, final BaseResource.BaseCatalog<?> base);

            /**
             * Create a view of a catalog.
             *
             */
            public AdqlResource.AdqlCatalog create(final AdqlResource parent, final BaseResource.BaseCatalog<?> base, final String name);

            /**
             * Select all the views of a catalog.
             *
             */
            public Iterable<AdqlResource.AdqlCatalog> select(final BaseResource.BaseCatalog<?> base);

            /**
             * Search for a catalog view based on parent resource.
             *
             */
            public AdqlResource.AdqlCatalog search(final AdqlResource parent, final BaseResource.BaseCatalog<?> base);

            /**
             * Access to our schema factory.
             *
             */
            public AdqlResource.AdqlSchema.Factory schemas();

            }

        /**
         * Access to our base catalog.
         *
         */
        public BaseResource.BaseCatalog<?> base();

        /**
         * Public interface for accessing a catalog's schemas.
         *
         */
        public interface Schemas
        extends DataResource.DataCatalog.Schemas<AdqlResource.AdqlSchema>
            {
            }

        /**
         * Access to this catalog's schemas.
         *
         */
        @Override
        public Schemas schemas();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        }

    /**
     * Public interface for schema metadata.
     *
     */
    public interface AdqlSchema
    extends DataResource.DataSchema<AdqlResource.AdqlCatalog>
        {

        /**
         * Factory interface for creating and selecting schemas.
         *
         */
        public static interface Factory
        extends DataResource.DataSchema.Factory<AdqlResource.AdqlCatalog, AdqlResource.AdqlSchema>
            {

            /**
             * Find an existing schema view, or create a new one.
             *
             */
            public AdqlResource.AdqlSchema cascade(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base);

            /**
             * Create a new view of a schema.
             *
             */
            public AdqlResource.AdqlSchema create(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base, final String name);

            /**
             * Select all the views of a schema.
             *
             */
            public Iterable<AdqlResource.AdqlSchema> select(final BaseResource.BaseSchema<?> base);

            /**
             * Search for a schema view based on parent resource.
             *
             */
            public AdqlResource.AdqlSchema search(final AdqlResource parent, final BaseResource.BaseSchema<?> base);

            /**
             * Search for a schema view based on parent catalog.
             *
             */
            public AdqlResource.AdqlSchema search(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseSchema<?> base);

            /**
             * Access to our table factory.
             *
             */
            public AdqlResource.AdqlTable.Factory tables();

            }

        /**
         * Access to our base schema.
         *
         */
        public BaseResource.BaseSchema<?> base();

        /**
         * Public interface for accessing a schema's tables.
         *
         */
        public interface Tables
        extends DataResource.DataSchema.Tables<AdqlResource.AdqlTable>
            {
            }

        /**
         * Access to this schema's tables.
         *
         */
        @Override
        public Tables tables();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public AdqlResource.AdqlCatalog catalog();

        }

    /**
     * Public interface for table metadata.
     *
     */
    public interface AdqlTable
    extends DataResource.DataTable<AdqlResource.AdqlSchema>
        {

        /**
         * Factory interface for creating and selecting tables.
         *
         */
        public static interface Factory
        extends DataResource.DataTable.Factory<AdqlResource.AdqlSchema, AdqlResource.AdqlTable>
            {

            /**
             * Find an existing table view, or create a new one.
             *
             */
            public AdqlResource.AdqlTable cascade(final AdqlResource.AdqlSchema parent, final BaseResource.BaseTable<?> base);

            /**
             * Create a new view of a table.
             *
             */
            public AdqlResource.AdqlTable create(final AdqlResource.AdqlSchema parent, final BaseResource.BaseTable<?> base, final String name);

            /**
             * Select all the views of a table.
             *
             */
            public Iterable<AdqlResource.AdqlTable> select(final BaseResource.BaseTable<?> base);

            /**
             * Search for a table view based on parent resource.
             *
             */
            public AdqlResource.AdqlTable search(final AdqlResource parent, final BaseResource.BaseTable<?> base);

            /**
             * Search for a table view based on parent catalog.
             *
             */
            public AdqlResource.AdqlTable search(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseTable<?> base);

            /**
             * Search for a table view based on parent schema.
             *
             */
            public AdqlResource.AdqlTable search(final AdqlResource.AdqlSchema parent, final BaseResource.BaseTable<?> base);

            /**
             * Access to our column factory.
             *
             */
            public AdqlResource.AdqlColumn.Factory adqlColumns();

            }

        /**
         * Access to our base table.
         *
         */
        public BaseResource.BaseTable<?> base();

        /**
         * Public interface for accessing a table's adqlColumns.
         *
         */
        public interface Columns
        extends DataResource.DataTable.Columns<AdqlResource.AdqlColumn>
            {

            /**
             * Search for a view of a specific column.
             *
             */
            public AdqlResource.AdqlColumn search(final BaseResource.BaseColumn<?> base);

            }

        /**
         * Access to this table's adqlColumns.
         *
         */
        @Override
        public Columns columns();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public AdqlResource.AdqlCatalog catalog();

        /**
         * Access to our parent schema.
         *
         */
        public AdqlResource.AdqlSchema schema();

        }

    /**
     * Public interface for column metadata.
     *
     */
    public interface AdqlColumn
    extends DataResource.DataColumn<AdqlResource.AdqlTable>
        {

        /**
         * Factory interface for creating and selecting adqlColumns.
         *
         */
        public static interface Factory
        extends DataResource.DataColumn.Factory<AdqlResource.AdqlTable, AdqlResource.AdqlColumn>
            {

            /**
             * Find an existing column view, or create a new one.
             *
             */
            public AdqlResource.AdqlColumn cascade(final AdqlResource.AdqlTable parent, final BaseResource.BaseColumn<?> base);

            /**
             * Create a new View of a column.
             *
             */
            public AdqlResource.AdqlColumn create(final AdqlResource.AdqlTable parent, final BaseResource.BaseColumn<?> base, final String name);

            /**
             * Select all the views of a column.
             *
             */
            public Iterable<AdqlResource.AdqlColumn> select(final BaseResource.BaseColumn<?> base);

            /**
             * Search for a column view based on parent resource.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource parent, final BaseResource.BaseColumn<?> base);

            /**
             * Search for a column view based on parent catalog.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource.AdqlCatalog parent, final BaseResource.BaseColumn<?> base);

            /**
             * Search for a column view based on parent schema.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource.AdqlSchema parent, final BaseResource.BaseColumn<?> base);

            /**
             * Search for a column view based on parent table.
             *
             */
            public AdqlResource.AdqlColumn search(final AdqlResource.AdqlTable parent, final BaseResource.BaseColumn<?> base);

            }

        /**
         * Access to our base column.
         *
         */
        public BaseResource.BaseColumn<?> base();

        /**
         * Access to our parent resource.
         *
         */
        public AdqlResource resource();

        /**
         * Access to our parent catalog.
         *
         */
        public AdqlResource.AdqlCatalog catalog();

        /**
         * Access to our parent schema.
         *
         */
        public AdqlResource.AdqlSchema schema();

        /**
         * Access to our parent table.
         *
         */
        public AdqlResource.AdqlTable table();

        }
    }

