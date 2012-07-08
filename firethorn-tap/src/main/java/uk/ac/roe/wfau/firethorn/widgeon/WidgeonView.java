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
 * Public interface for a Widgeon view, describing a modifiable view an underlying base Widgeon.
 *
 */
public interface WidgeonView
extends Widgeon
    {

    /**
     * Factory interface for creating and selecting Widgeon Views.
     *
     */
    public static interface Factory
    extends Entity.Factory<WidgeonView>
        {

        /**
         * Create a View of a Widgeon.
         *
         */
        public WidgeonView create(WidgeonBase base, String name);

        /**
         * Select all the Views of a Widgeon.
         *
         */
        public Iterable<WidgeonView> select(WidgeonBase base);

        /**
         * Select a named View of a Widgeon.
         *
         */
        public WidgeonView select(WidgeonBase base, String name)
        throws NameNotFoundException;

        /**
         * Access to our Catalog factory.
         * 
         */
        public WidgeonView.Catalog.Factory catalogs();

        }

    /**
     * Access to our underlying Widgeon.
     *
     */
    public WidgeonBase base();

    /**
     * Public interface for accessing a Widgeon's Catalogs.
     *
     */
    public interface Catalogs
    extends Widgeon.Catalogs<WidgeonView.Catalog>
        {
        }

    /**
     * Access to this Widgeon's Catalogs.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for a Catalog View.
     *
     */
    public interface Catalog
    extends Widgeon.Catalog<WidgeonView>
        {

        /**
         * Factory interface for creating and selecting Catalog Views.
         *
         */
        public static interface Factory
        extends Widgeon.Catalog.Factory<WidgeonView, WidgeonView.Catalog>
            {

            /**
             * Find an existing View of a Catalog, or create a new one.
             *
             */
            public WidgeonView.Catalog cascade(WidgeonView parent, WidgeonBase.Catalog base);

            /**
             * Create a new View of a Catalog.
             *
             */
            public WidgeonView.Catalog create(WidgeonView parent, WidgeonBase.Catalog base, String name);

            /**
             * Select all the views of a Catalog.
             *
             */
            public Iterable<WidgeonView.Catalog> select(WidgeonBase.Catalog base);

            /**
             * Access to our Schema factory.
             * 
             */
            public WidgeonView.Schema.Factory schemas();

            }

        /**
         * Access to our underlying Catalog.
         *
         */
        public WidgeonBase.Catalog base();

        /**
         * Public interface for accessing a Catalog's Schemas.
         *
         */
        public interface Schemas
        extends Widgeon.Catalog.Schemas<WidgeonView.Schema>
            {
            }

        /**
         * Access to this Catalog's Schemas.
         *
         */
        public Schemas schemas();

        }

    /**
     * Public interface for Schema metadata.
     *
     */
    public interface Schema
    extends Widgeon.Schema<WidgeonView.Catalog>
        {

        /**
         * Factory interface for creating and selecting Schemas.
         *
         */
        public static interface Factory
        extends Widgeon.Schema.Factory<WidgeonView.Catalog, WidgeonView.Schema>
            {

            /**
             * Find an existing View of a Schema, or create a new one.
             *
             */
            public WidgeonView.Schema cascade(WidgeonView.Catalog parent, WidgeonBase.Schema base);

            /**
             * Create a new View of a Schema.
             *
             */
            public WidgeonView.Schema create(WidgeonView.Catalog parent, WidgeonBase.Schema base, String name);

            /**
             * Select all the views of a Schema.
             *
             */
            public Iterable<WidgeonView.Schema> select(WidgeonBase.Schema base);

            /**
             * Access to our Table factory.
             * 
             */
            public WidgeonView.Table.Factory tables();
            
            }

        /**
         * Access to our underlying Schema.
         *
         */
        public WidgeonBase.Schema base();

        /**
         * Public interface for accessing a Schema's Tables.
         *
         */
        public interface Tables
        extends Widgeon.Schema.Tables<WidgeonView.Table>
            {
            }

        /**
         * Access to this Schema's Tables.
         *
         */
        public Tables tables();

        }

    /**
     * Public interface for Table metadata.
     *
     */
    public interface Table
    extends Widgeon.Table<WidgeonView.Schema>
        {

        /**
         * Factory interface for creating and selecting Tables.
         *
         */
        public static interface Factory
        extends Widgeon.Table.Factory<WidgeonView.Schema, WidgeonView.Table>
            {

            /**
             * Find an existing View of a Table, or create a new one.
             *
             */
            public WidgeonView.Table cascade(WidgeonView.Schema parent, WidgeonBase.Table base);

            /**
             * Create a new View of a Table.
             *
             */
            public WidgeonView.Table create(WidgeonView.Schema parent, WidgeonBase.Table base, String name);

            /**
             * Select all the views of a Table.
             *
             */
            public Iterable<WidgeonView.Table> select(WidgeonBase.Table base);

            /**
             * Access to our Column factory.
             * 
             */
            public WidgeonView.Column.Factory columns();

            }

        /**
         * Access to our underlying Table.
         *
         */
        public WidgeonBase.Table base();

        /**
         * Public interface for accessing a Table's Columns.
         *
         */
        public interface Columns
        extends Widgeon.Table.Columns<WidgeonView.Column>
            {
            }

        /**
         * Access to this Table's Columns.
         *
         */
        public Columns columns();

        }

    /**
     * Public interface for Column metadata.
     *
     */
    public interface Column
    extends Widgeon.Column<WidgeonView.Table>
        {

        /**
         * Factory interface for creating and selecting Columns.
         *
         */
        public static interface Factory
        extends Widgeon.Column.Factory<WidgeonView.Table, WidgeonView.Column>
            {

            /**
             * Find an existing View of a Column, or create a new one.
             *
             */
            public WidgeonView.Column cascade(WidgeonView.Table parent, WidgeonBase.Column base);

            /**
             * Create a new View of a Column.
             *
             */
            public WidgeonView.Column create(WidgeonView.Table parent, WidgeonBase.Column base, String name);

            /**
             * Select all the views of a Column.
             *
             */
            public Iterable<WidgeonView.Column> select(WidgeonBase.Column base);

            }

        /**
         * Access to our underlying Column.
         *
         */
        public WidgeonBase.Column base();

        }
    }

