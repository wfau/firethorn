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

import java.net.URI;
import java.net.URL;

import javax.sql.DataSource;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.exception.*;

/**
 * Public interface for describing a data resource (JDBC database OR TAP service).
 * Called DataResource because the word Resource has already been used so many times to mean different things in different places.
 *
 */
public interface DataResource
extends DataResourceEntity
    {

    /**
     * Access to this DataResource's Catalog.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for accessing a DataResource's Catalogs.
     *
     */
    public interface Catalogs<CatalogType extends DataResource.Catalog>
        {

        /**
         * Select all the Catalogs from the DataResource.
         *
         */
        public Iterable<CatalogType> select();

        /**
         * Select a named Catalog from the DataResource.
         *
         */
        public CatalogType select(String name)
        throws NameNotFoundException;

        /**
         * Search for a named Catalog from the DataResource.
         *
         */
        public CatalogType search(String name);

        }

    /**
     * Public interface for Catalog metadata.
     *
     */
    public interface Catalog<WidgeonType extends DataResource>
    extends DataResourceComponent<WidgeonType>
        {

        /**
         * Factory interface for creating and selecting Catalogs.
         *
         */
        public static interface Factory<WidgeonType extends DataResource, CatalogType extends DataResource.Catalog>
        extends Entity.Factory<CatalogType>
            {

            /**
             * Select all the Catalogs from a DataResource.
             *
             */
            public Iterable<CatalogType> select(WidgeonType parent);

            /**
             * Select a named Catalog from a DataResource.
             *
             */
            public CatalogType select(WidgeonType parent, String name)
            throws NameNotFoundException;

            /**
             * Search for a named Catalog from a DataResource.
             *
             */
            public CatalogType search(WidgeonType parent, String name);

            }

        /**
         * Access to this Catalog's Schemas.
         *
         */
        public Schemas schemas();

        /**
         * Public interface for accessing a Catalog's Schemas.
         *
         */
        public interface Schemas<SchemaType extends DataResource.Schema>
            {

            /**
             * Select all the Schemas from the Catalog.
             *
             */
            public Iterable<SchemaType> select();

            /**
             * Select a named Schema from the Catalog.
             *
             */
            public SchemaType select(String name)
            throws NameNotFoundException;

            /**
             * Search for a named Schema from the Catalog.
             *
             */
            public SchemaType search(String name);

            }
        }

    /**
     * Public interface for Schema metadata.
     *
     */
    public interface Schema<CatalogType extends DataResource.Catalog>
    extends DataResourceComponent<CatalogType>
        {

        /**
         * Factory interface for creating and selecting Schemas.
         *
         */
        public static interface Factory<CatalogType extends DataResource.Catalog, SchemaType extends DataResource.Schema>
        extends Entity.Factory<SchemaType>
            {

            /**
             * Select all the Schemas from a Catalog.
             *
             */
            public Iterable<SchemaType> select(CatalogType parent);

            /**
             * Select a named Schema from a Catalog.
             *
             */
            public SchemaType select(CatalogType parent, String name)
            throws NameNotFoundException;

            /**
             * Search for a named Schema from a Catalog.
             *
             */
            public SchemaType search(CatalogType parent, String name);

            }

        /**
         * Access to this Schema's Tables.
         *
         */
        public Tables tables();

        /**
         * Public interface for accessing a Schema's Tables.
         *
         */
        public interface Tables<TableType extends DataResource.Table>
            {

            /**
             * Select all the Tables from the Schema.
             *
             */
            public Iterable<TableType> select();

            /**
             * Select a named Table from the Schema.
             *
             */
            public TableType select(String name)
            throws NameNotFoundException;

            /**
             * Search for a named Table from the Schema.
             *
             */
            public TableType search(String name);

            }
        }

    /**
     * Public interface for Table metadata.
     *
     */
    public interface Table<SchemaType extends DataResource.Schema>
    extends DataResourceComponent<SchemaType>
        {

        /**
         * Factory interface for creating and selecting Tables.
         *
         */
        public static interface Factory<SchemaType extends DataResource.Schema, TableType extends DataResource.Table>
        extends Entity.Factory<TableType>
            {

            /**
             * Select all the Tables from a Schema.
             *
             */
            public Iterable<TableType> select(SchemaType parent);

            /**
             * Select a named Table from a Schema.
             *
             */
            public TableType select(SchemaType parent, String name)
            throws NameNotFoundException;

            /**
             * Search for a named Table from a Schema.
             *
             */
            public TableType search(SchemaType parent, String name);

            }

        /**
         * Access to this Table's Columns.
         *
         */
        public Columns columns();

        /**
         * Public interface for accessing a Table's Columns.
         *
         */
        public interface Columns<ColumnType extends DataResource.Column>
            {

            /**
             * Select all the Columns from the Table.
             *
             */
            public Iterable<ColumnType> select();

            /**
             * Select a named Column from the Table.
             *
             */
            public ColumnType select(String name)
            throws NameNotFoundException;

            /**
             * Search for a named Column from the Table.
             *
             */
            public ColumnType search(String name);

            }
        }

    /**
     * Public interface for Column metadata.
     *
     */
    public interface Column<TableType extends DataResource.Table>
    extends DataResourceComponent<TableType>
        {

        /**
         * Factory interface for creating and selecting Columns.
         *
         */
        public static interface Factory<TableType extends DataResource.Table, ColumnType extends DataResource.Column>
        extends Entity.Factory<ColumnType>
            {

            /**
             * Select all the Columns from a Table.
             *
             */
            public Iterable<ColumnType> select(TableType parent);

            /**
             * Select a named Column from a Table.
             *
             */
            public ColumnType select(TableType parent, String name)
            throws NameNotFoundException;

            /**
             * Search for a named Column from a Table.
             *
             */
            public ColumnType search(TableType parent, String name);

            }
        }
    }

