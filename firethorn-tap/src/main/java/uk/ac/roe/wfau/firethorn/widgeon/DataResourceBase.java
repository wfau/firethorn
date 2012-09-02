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
 * Public interface for a base DataResource, describing a real data resource (JDBC database OR TAP service).
 *
 */
public interface DataResourceBase
extends DataResource
    {

    /**
     * Factory interface for creating and selecting DataResources.
     *
     */
    public static interface Factory
    extends Entity.Factory<DataResourceBase>
        {

        /**
         * Select all of the Widgeons.
         *
         */
        public Iterable<DataResourceBase> select();

        /**
         * Select DataResource(s) by name.
         *
         */
        public Iterable<DataResourceBase> select(String name);

        /**
         * Search for DataResource(s) by name.
         *
         */
        public Iterable<DataResourceBase> search(String text);

        /**
         * Create a DataResource from a registry URI.
         * 
         */
        public DataResourceBase create(String name, URI uri);

        /**
         * Create a DataResource from a VOSI URL.
         * 
         */
        public DataResourceBase create(String name, URL url);

        /**
         * Create a DataResource from a JDBC DataSource.
         * 
         */
        public DataResourceBase create(String name, DataSource src);

        /**
         * Access to our View factory.
         * 
         */
        public DataResourceView.Factory views();

        /**
         * Access to our Catalog factory.
         * 
         */
        public DataResourceBase.Catalog.Factory catalogs();

        }

    /**
     * Public interface for accessing a DataResource's Views.
     *
     */
    public interface Views
        {

        /*
         * Create a new view of the DataResource.
         *
         */
        public DataResourceView create(String name);

        /*
         * Select all the views of the DataResource.
         *
         */
        public Iterable<DataResourceView> select();

        /*
         * Select a named view of the DataResource.
         *
         */
        public DataResourceView select(String name)
        throws NameNotFoundException;

        }

    /**
     * Access to this DataResource's Views.
     *
     */
    public DataResourceBase.Views views();

    /**
     * Public interface for accessing a DataResource's Catalogs.
     *
     */
    public interface Catalogs
    extends DataResource.Catalogs<DataResourceBase.Catalog>
        {

        /**
         * Create a new Catalog for the DataResource.
         *
         */
        public DataResourceBase.Catalog create(String name);

        }

    /**
     * Access to this DataResource's Catalogs.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for Catalog metadata.
     *
     */
    public interface Catalog
    extends DataResource.Catalog<DataResourceBase>
        {

        /**
         * Factory interface for creating and selecting Catalog.
         *
         */
        public static interface Factory
        extends DataResource.Catalog.Factory<DataResourceBase, DataResourceBase.Catalog>
            {

            /**
             * Create a new Catalog for a DataResource.
             *
             */
            public DataResourceBase.Catalog create(DataResourceBase parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public DataResourceView.Catalog.Factory views();

            /**
             * Access to our Schema factory.
             * 
             */
            public DataResourceBase.Schema.Factory schemas();

            }

        /**
         * Public interface for accessing a Catalog's Views.
         *
         */
        public interface Views
            {

            /*
             * Select all the views of the Catalog.
             *
             */
            public Iterable<DataResourceView.Catalog> select();

            /**
             * Search for a specific view of the Catalog.
             *
             */
            public DataResourceView.Catalog search(DataResourceView parent);

            }

        /**
         * Access to this Catalog's Views.
         *
         */
        public DataResourceBase.Catalog.Views views();

        /**
         * Public interface for accessing a Catalog's Schemas.
         *
         */
        public interface Schemas
        extends DataResource.Catalog.Schemas<DataResourceBase.Schema>
            {

            /**
             * Create a new Schema for the Catalog.
             *
             */
            public DataResourceBase.Schema create(String name);

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
        public DataResourceBase widgeon();

        }

    /**
     * Public interface for Schema metadata.
     *
     */
    public interface Schema
    extends DataResource.Schema<DataResourceBase.Catalog>
        {

        /**
         * Factory interface for creating and selecting Schemas.
         *
         */
        public static interface Factory
        extends DataResource.Schema.Factory<DataResourceBase.Catalog, DataResourceBase.Schema>
            {

            /**
             * Create a new Schema for a Catalog.
             *
             */
            public DataResourceBase.Schema create(DataResourceBase.Catalog parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public DataResourceView.Schema.Factory views();

            /**
             * Access to our Table factory.
             * 
             */
            public DataResourceBase.Table.Factory tables();

            }

        /**
         * Public interface for accessing a Schema's Views.
         *
         */
        public interface Views
            {

            /*
             * Select all the views of the Schema.
             *
             */
            public Iterable<DataResourceView.Schema> select();

            /**
             * Search for a specific view of the Schema.
             *
             */
            public DataResourceView.Schema search(DataResourceView.Catalog parent);

            }

        /**
         * Access to this Schema's Views.
         *
         */
        public DataResourceBase.Schema.Views views();

        /**
         * Public interface for accessing a Schema's Tables.
         *
         */
        public interface Tables
        extends DataResource.Schema.Tables<DataResourceBase.Table>
            {

            /**
             * Create a new Table for the Schema.
             *
             */
            public DataResourceBase.Table create(String name);

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
        public DataResourceBase widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public DataResourceBase.Catalog catalog();

        }

    /**
     * Public interface for Table metadata.
     *
     */
    public interface Table
    extends DataResource.Table<DataResourceBase.Schema>
        {

        /**
         * Factory interface for creating and selecting Tables.
         *
         */
        public static interface Factory
        extends DataResource.Table.Factory<DataResourceBase.Schema, DataResourceBase.Table>
            {

            /**
             * Create a new Table for a Schema.
             *
             */
            public DataResourceBase.Table create(DataResourceBase.Schema parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public DataResourceView.Table.Factory views();

            /**
             * Access to our Column factory.
             * 
             */
            public DataResourceBase.Column.Factory columns();

            }

        /**
         * Public interface for accessing a Table's Views.
         *
         */
        public interface Views
            {

            /*
             * Select all the views of the Table.
             *
             */
            public Iterable<DataResourceView.Table> select();

            /**
             * Search for a specific view of the Table.
             *
             */
            public DataResourceView.Table search(DataResourceView.Schema parent);

            }

        /**
         * Access to this Table's Views.
         *
         */
        public DataResourceBase.Table.Views views();

        /**
         * Public interface for accessing a Table's Columns.
         *
         */
        public interface Columns
        extends DataResource.Table.Columns<DataResourceBase.Column>
            {

            /**
             * Create a new Column for the Table.
             *
             */
            public DataResourceBase.Column create(String name);

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
        public DataResourceBase widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public DataResourceBase.Catalog catalog();

        /**
         * Access to our parent Schema.
         *
         */
        public DataResourceBase.Schema schema();

        }

    /**
     * Public interface for Column metadata.
     *
     */
    public interface Column
    extends DataResource.Column<DataResourceBase.Table>
        {

        /**
         * Factory interface for creating and selecting Columns.
         *
         */
        public static interface Factory
        extends DataResource.Column.Factory<DataResourceBase.Table, DataResourceBase.Column>
            {

            /**
             * Create a new Column for a Table.
             *
             */
            public DataResourceBase.Column create(DataResourceBase.Table parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public DataResourceView.Column.Factory views();

            }

        /**
         * Public interface for accessing a Column's Views.
         *
         */
        public interface Views
            {

            /*
             * Select all the views of the Column.
             *
             */
            public Iterable<DataResourceView.Column> select();

            /**
             * Search for a specific view of the Column.
             *
             */
            public DataResourceView.Column search(DataResourceView.Table parent);

            }

        /**
         * Access to this Column's Views.
         *
         */
        public DataResourceBase.Column.Views views();

        /**
         * Access to our parent DataResource.
         *
         */
        public DataResourceBase widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public DataResourceBase.Catalog catalog();

        /**
         * Access to our parent Schema.
         *
         */
        public DataResourceBase.Schema schema();

        /**
         * Access to our parent Table.
         *
         */
        public DataResourceBase.Table table();

        }
    }

