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
 * Public interface for a DataResource view, describing a modifiable view an underlying base DataResource.
 *
 */
public interface DataResourceView
extends DataResource
    {

    /**
     * Factory interface for creating and selecting DataResource Views.
     *
     */
    public static interface Factory
    extends Entity.Factory<DataResourceView>
        {

        /**
         * Create a view of a DataResource.
         *
         */
        public DataResourceView create(DataResourceBase base, String name);

        /**
         * Select all the views of a DataResource.
         *
         */
        public Iterable<DataResourceView> select(DataResourceBase base);

        /**
         * Select a view of a DataResource by name.
         *
         */
        public DataResourceView select(DataResourceBase base, String name)
        throws NameNotFoundException;

        /**
         * Search for a view of a DataResource by name.
         *
         */
        public DataResourceView search(DataResourceBase base, String name);

        /**
         * Access to our Catalog factory.
         * 
         */
        public DataResourceView.Catalog.Factory catalogs();

        }

    /**
     * Access to our base DataResource.
     *
     */
    public DataResourceBase base();

    /**
     * Public interface for accessing a DataResource's Catalogs.
     *
     */
    public interface Catalogs
    extends DataResource.Catalogs<DataResourceView.Catalog>
        {
        }

    /**
     * Access to this DataResource's Catalogs.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for a Catalog View.
     *
     */
    public interface Catalog
    extends DataResource.Catalog<DataResourceView>
        {

        /**
         * Factory interface for creating and selecting Catalog Views.
         *
         */
        public static interface Factory
        extends DataResource.Catalog.Factory<DataResourceView, DataResourceView.Catalog>
            {

            /**
             * Find an existing View of a Catalog, or create a new one.
             *
             */
            public DataResourceView.Catalog cascade(DataResourceView parent, DataResourceBase.Catalog base);

            /**
             * Create a new View of a Catalog.
             *
             */
            public DataResourceView.Catalog create(DataResourceView parent, DataResourceBase.Catalog base, String name);

            /**
             * Select all the views of a Catalog.
             *
             */
            public Iterable<DataResourceView.Catalog> select(DataResourceBase.Catalog base);

            /**
             * Search for a specific view of a Catalog.
             *
             */
            public DataResourceView.Catalog search(DataResourceView parent, DataResourceBase.Catalog base);

            /**
             * Access to our Schema factory.
             * 
             */
            public DataResourceView.Schema.Factory schemas();

            }

        /**
         * Access to our base Catalog.
         *
         */
        public DataResourceBase.Catalog base();

        /**
         * Public interface for accessing a Catalog's Schemas.
         *
         */
        public interface Schemas
        extends DataResource.Catalog.Schemas<DataResourceView.Schema>
            {
            }

        /**
         * Access to this Catalog's Schemas.
         *
         */
        public Schemas schemas();

        /**
         * Access to our parent DataResource.
         *
         */
        public DataResourceView widgeon();

        }

    /**
     * Public interface for Schema metadata.
     *
     */
    public interface Schema
    extends DataResource.Schema<DataResourceView.Catalog>
        {

        /**
         * Factory interface for creating and selecting Schemas.
         *
         */
        public static interface Factory
        extends DataResource.Schema.Factory<DataResourceView.Catalog, DataResourceView.Schema>
            {

            /**
             * Find an existing View of a Schema, or create a new one.
             *
             */
            public DataResourceView.Schema cascade(DataResourceView.Catalog parent, DataResourceBase.Schema base);

            /**
             * Create a new View of a Schema.
             *
             */
            public DataResourceView.Schema create(DataResourceView.Catalog parent, DataResourceBase.Schema base, String name);

            /**
             * Select all the views of a Schema.
             *
             */
            public Iterable<DataResourceView.Schema> select(DataResourceBase.Schema base);

            /**
             * Search for a specific view of a Schema.
             *
             */
            public DataResourceView.Schema search(DataResourceView.Catalog parent, DataResourceBase.Schema base);

            /**
             * Access to our Table factory.
             * 
             */
            public DataResourceView.Table.Factory tables();
            
            }

        /**
         * Access to our base Schema.
         *
         */
        public DataResourceBase.Schema base();

        /**
         * Public interface for accessing a Schema's Tables.
         *
         */
        public interface Tables
        extends DataResource.Schema.Tables<DataResourceView.Table>
            {
            }

        /**
         * Access to this Schema's Tables.
         *
         */
        public Tables tables();

        /**
         * Access to our parent DataResource.
         *
         */
        public DataResourceView widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public DataResourceView.Catalog catalog();

        }

    /**
     * Public interface for Table metadata.
     *
     */
    public interface Table
    extends DataResource.Table<DataResourceView.Schema>
        {

        /**
         * Factory interface for creating and selecting Tables.
         *
         */
        public static interface Factory
        extends DataResource.Table.Factory<DataResourceView.Schema, DataResourceView.Table>
            {

            /**
             * Find an existing View of a Table, or create a new one.
             *
             */
            public DataResourceView.Table cascade(DataResourceView.Schema parent, DataResourceBase.Table base);

            /**
             * Create a new View of a Table.
             *
             */
            public DataResourceView.Table create(DataResourceView.Schema parent, DataResourceBase.Table base, String name);

            /**
             * Select all the views of a Table.
             *
             */
            public Iterable<DataResourceView.Table> select(DataResourceBase.Table base);

            /**
             * Search for a specific view of a Table.
             *
             */
            public DataResourceView.Table search(DataResourceView.Schema parent, DataResourceBase.Table base);

            /**
             * Access to our Column factory.
             * 
             */
            public DataResourceView.Column.Factory columns();

            }

        /**
         * Access to our base Table.
         *
         */
        public DataResourceBase.Table base();

        /**
         * Public interface for accessing a Table's Columns.
         *
         */
        public interface Columns
        extends DataResource.Table.Columns<DataResourceView.Column>
            {

            /**
             * Search for a view of a spcific column.
             *
             */
            public DataResourceView.Column search(DataResourceBase.Column base);

            }

        /**
         * Access to this Table's Columns.
         *
         */
        public Columns columns();

        /**
         * Access to our parent DataResource.
         *
         */
        public DataResourceView widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public DataResourceView.Catalog catalog();

        /**
         * Access to our parent Schema.
         *
         */
        public DataResourceView.Schema schema();

        }

    /**
     * Public interface for Column metadata.
     *
     */
    public interface Column
    extends DataResource.Column<DataResourceView.Table>
        {

        /**
         * Factory interface for creating and selecting Columns.
         *
         */
        public static interface Factory
        extends DataResource.Column.Factory<DataResourceView.Table, DataResourceView.Column>
            {

            /**
             * Find an existing View of a Column, or create a new one.
             *
             */
            public DataResourceView.Column cascade(DataResourceView.Table parent, DataResourceBase.Column base);

            /**
             * Create a new View of a Column.
             *
             */
            public DataResourceView.Column create(DataResourceView.Table parent, DataResourceBase.Column base, String name);

            /**
             * Search for a specific view of a Column.
             *
             */
            public DataResourceView.Column search(DataResourceView.Table parent, DataResourceBase.Column base);

            /**
             * Select all the views of a Column.
             *
             */
            public Iterable<DataResourceView.Column> select(DataResourceBase.Column base);

            }

        /**
         * Access to our base Column.
         *
         */
        public DataResourceBase.Column base();

        /**
         * Access to our parent DataResource.
         *
         */
        public DataResourceView widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public DataResourceView.Catalog catalog();

        /**
         * Access to our parent Schema.
         *
         */
        public DataResourceView.Schema schema();

        /**
         * Access to our parent Table.
         *
         */
        public DataResourceView.Table table();

        }
    }

