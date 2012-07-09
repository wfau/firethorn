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
 * Public interface for a base Widgeon, describing a real data resource (JDBC database OR TAP service).
 *
 */
public interface WidgeonBase
extends Widgeon
    {

    /**
     * Factory interface for creating and selecting Widgeons.
     *
     */
    public static interface Factory
    extends Entity.Factory<WidgeonBase>
        {

        /**
         * Select all of the Widgeons.
         *
         */
        public Iterable<WidgeonBase> select();

        /**
         * Select a Widgeon by name.
         *
         */
        public WidgeonBase select(String name)
        throws NameNotFoundException;

        /**
         * Create a Widgeon from a registry URI.
         * 
         */
        public WidgeonBase create(String name, URI uri);

        /**
         * Create a Widgeon from a VOSI URL.
         * 
         */
        public WidgeonBase create(String name, URL url);

        /**
         * Create a Widgeon from a JDBC DataSource.
         * 
         */
        public WidgeonBase create(String name, DataSource src);

        /**
         * Access to our View factory.
         * 
         */
        public WidgeonView.Factory views();

        /**
         * Access to our Catalog factory.
         * 
         */
        public WidgeonBase.Catalog.Factory catalogs();

        }

    /**
     * Public interface for accessing a Widgeon's Views.
     *
     */
    public interface Views
        {

        /*
         * Create a new view of the Widgeon.
         *
         */
        public WidgeonView create(String name);

        /*
         * Select all the views of the Widgeon.
         *
         */
        public Iterable<WidgeonView> select();

        /*
         * Select a named view of the Widgeon.
         *
         */
        public WidgeonView select(String name)
        throws NameNotFoundException;

        }

    /**
     * Access to this Widgeon's Views.
     *
     */
    public WidgeonBase.Views views();

    /**
     * Public interface for accessing a Widgeon's Catalogs.
     *
     */
    public interface Catalogs
    extends Widgeon.Catalogs<WidgeonBase.Catalog>
        {

        /**
         * Create a new Catalog for the Widgeon.
         *
         */
        public WidgeonBase.Catalog create(String name);

        }

    /**
     * Access to this Widgeon's Catalogs.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for Catalog metadata.
     *
     */
    public interface Catalog
    extends Widgeon.Catalog<WidgeonBase>
        {

        /**
         * Factory interface for creating and selecting Catalog.
         *
         */
        public static interface Factory
        extends Widgeon.Catalog.Factory<WidgeonBase, WidgeonBase.Catalog>
            {

            /**
             * Create a new Catalog for a Widgeon.
             *
             */
            public WidgeonBase.Catalog create(WidgeonBase parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public WidgeonView.Catalog.Factory views();

            /**
             * Access to our Schema factory.
             * 
             */
            public WidgeonBase.Schema.Factory schemas();

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
            public Iterable<WidgeonView.Catalog> select();

            /**
             * Search for a specific view of the Catalog.
             *
             */
            public WidgeonView.Catalog search(WidgeonView parent);

            }

        /**
         * Access to this Catalog's Views.
         *
         */
        public WidgeonBase.Catalog.Views views();

        /**
         * Public interface for accessing a Catalog's Schemas.
         *
         */
        public interface Schemas
        extends Widgeon.Catalog.Schemas<WidgeonBase.Schema>
            {

            /**
             * Create a new Schema for the Catalog.
             *
             */
            public WidgeonBase.Schema create(String name);

            }

        /**
         * Access to this Catalog's Schemas.
         *
         */
        public Schemas schemas();

        /**
         * Access to our parent Widgeon.
         *
         */
        public WidgeonBase widgeon();

        }

    /**
     * Public interface for Schema metadata.
     *
     */
    public interface Schema
    extends Widgeon.Schema<WidgeonBase.Catalog>
        {

        /**
         * Factory interface for creating and selecting Schemas.
         *
         */
        public static interface Factory
        extends Widgeon.Schema.Factory<WidgeonBase.Catalog, WidgeonBase.Schema>
            {

            /**
             * Create a new Schema for a Catalog.
             *
             */
            public WidgeonBase.Schema create(WidgeonBase.Catalog parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public WidgeonView.Schema.Factory views();

            /**
             * Access to our Table factory.
             * 
             */
            public WidgeonBase.Table.Factory tables();

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
            public Iterable<WidgeonView.Schema> select();

            /**
             * Search for a specific view of the Schema.
             *
             */
            public WidgeonView.Schema search(WidgeonView.Catalog parent);

            }

        /**
         * Access to this Schema's Views.
         *
         */
        public WidgeonBase.Schema.Views views();

        /**
         * Public interface for accessing a Schema's Tables.
         *
         */
        public interface Tables
        extends Widgeon.Schema.Tables<WidgeonBase.Table>
            {

            /**
             * Create a new Table for the Schema.
             *
             */
            public WidgeonBase.Table create(String name);

            }

        /**
         * Access to this Schema's Tables.
         *
         */
        public Tables tables();

        /**
         * Access to our parent Widgeon.
         *
         */
        public WidgeonBase widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public WidgeonBase.Catalog catalog();

        }

    /**
     * Public interface for Table metadata.
     *
     */
    public interface Table
    extends Widgeon.Table<WidgeonBase.Schema>
        {

        /**
         * Factory interface for creating and selecting Tables.
         *
         */
        public static interface Factory
        extends Widgeon.Table.Factory<WidgeonBase.Schema, WidgeonBase.Table>
            {

            /**
             * Create a new Table for a Schema.
             *
             */
            public WidgeonBase.Table create(WidgeonBase.Schema parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public WidgeonView.Table.Factory views();

            /**
             * Access to our Column factory.
             * 
             */
            public WidgeonBase.Column.Factory columns();

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
            public Iterable<WidgeonView.Table> select();

            /**
             * Search for a specific view of the Table.
             *
             */
            public WidgeonView.Table search(WidgeonView.Schema parent);

            }

        /**
         * Access to this Table's Views.
         *
         */
        public WidgeonBase.Table.Views views();

        /**
         * Public interface for accessing a Table's Columns.
         *
         */
        public interface Columns
        extends Widgeon.Table.Columns<WidgeonBase.Column>
            {

            /**
             * Create a new Column for the Table.
             *
             */
            public WidgeonBase.Column create(String name);

            }

        /**
         * Access to this Table's Columns.
         *
         */
        public Columns columns();

        /**
         * Access to our parent Widgeon.
         *
         */
        public WidgeonBase widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public WidgeonBase.Catalog catalog();

        /**
         * Access to our parent Schema.
         *
         */
        public WidgeonBase.Schema schema();

        }

    /**
     * Public interface for Column metadata.
     *
     */
    public interface Column
    extends Widgeon.Column<WidgeonBase.Table>
        {

        /**
         * Factory interface for creating and selecting Columns.
         *
         */
        public static interface Factory
        extends Widgeon.Column.Factory<WidgeonBase.Table, WidgeonBase.Column>
            {

            /**
             * Create a new Column for a Table.
             *
             */
            public WidgeonBase.Column create(WidgeonBase.Table parent, String name);

            /**
             * Access to our View factory.
             * 
             */
            public WidgeonView.Column.Factory views();

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
            public Iterable<WidgeonView.Column> select();

            /**
             * Search for a specific view of the Column.
             *
             */
            public WidgeonView.Column search(WidgeonView.Table parent);

            }

        /**
         * Access to this Column's Views.
         *
         */
        public WidgeonBase.Column.Views views();

        /**
         * Access to our parent Widgeon.
         *
         */
        public WidgeonBase widgeon();

        /**
         * Access to our parent Catalog.
         *
         */
        public WidgeonBase.Catalog catalog();

        /**
         * Access to our parent Schema.
         *
         */
        public WidgeonBase.Schema schema();

        /**
         * Access to our parent Table.
         *
         */
        public WidgeonBase.Table table();

        }
    }

