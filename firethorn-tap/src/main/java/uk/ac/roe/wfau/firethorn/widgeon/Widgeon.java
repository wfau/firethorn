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
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 * Public interface for describing a data resource (JDBC database OR TAP service).
 * Called Widgeon because the word Resource has already been used so many times to mean different things in different places.
 *
 */
public interface Widgeon
extends Entity
    {

    /**
     * Enum representing the status of a Widgeon component.
     *
     */
    public enum Status
        {
        ENABLED(true),
        DISABLED(false),
        CREATED(false),
        DELETED(false);

        /**
         * Private constructor.
         *
         */
        private Status(boolean enabled)
            {
            this.enabled = enabled ;
            }

        /**
         * Private enabled flag.
         *
         */
        private boolean enabled ;

        /**
         * Check to see if a component with this status is enabled.
         *
         */
        public boolean enabled()
            {
            return this.enabled ;
            }        

        }

    /**
     * The status of this Widgeon.
     *
     */
    public Status status();

    /**
     * Set the status of this Widgeon.
     *
     */
    public void status(Status status);

    /**
     * Access to this Widgeon's Schema.
     *
     */
    public Schemas schemas();

    /**
     * Public interface for accessing this Widgeon's Schema.
     *
     */
    public interface Schemas<SchemaType extends Widgeon.Schema>
        {
        /**
         * Select a named Schema.
         *
         */
        public SchemaType select(String name);

        /**
         * Select all the Schema in this Widgeon.
         *
         */
        public Iterable<SchemaType> select();

        }

    /**
     * Public interface for Schema metadata.
     *
     */
    public interface Schema
    extends Entity
        {
        /**
         * Access to our parent Widgeon.
         *
         */
        public Widgeon parent();

        /**
         * The status of this Schema.
         *
         */
        public Status status();

        /**
         * Set the status of this Schema.
         *
         */
        public void status(Status status);

        /**
         * Access to this Schema's Catalogs.
         *
         */
        public Catalogs catalogs();

        /**
         * Public interface for accessing this Schema's Catalogs.
         *
         */
        public interface Catalogs
            {
            /**
             * Select a named Catalog.
             *
             */
            public Schema.Catalog select(String name);

            /**
             * Select all the Catalogs in this Schema.
             *
             */
            public Iterable<Schema.Catalog> select();
            }

        /**
         * Public interface for Catalog metadata.
         *
         */
        public interface Catalog
        extends Entity
            {
            /**
             * Access to our parent Schema.
             *
             */
            public Schema parent();

            /**
             * The status of this Catalog.
             *
             */
            public Status status();

            /**
             * Set the status of this Catalog.
             *
             */
            public void status(Status status);

            /**
             * Access to this Catalog's Tables.
             *
             */
            public Tables tables();

            /**
             * Public interface for accessing this Catalog's Tables.
             *
             */
            public interface Tables
                {
                /**
                 * Select a named Table.
                 *
                 */
                public Catalog.Table select(String name);

                /**
                 * Select all the Tables in this Catalog.
                 *
                 */
                public Iterable<Catalog.Table> select();
                }

            /**
             * Public interface for Table metadata.
             *
             */
            public interface Table
            extends Entity
                {
                /**
                 * Access to our parent Catalog.
                 *
                 */
                public Catalog parent();

                /**
                 * The status of this Table.
                 *
                 */
                public Status status();

                /**
                 * Set the status of this Table.
                 *
                 */
                public void status(Status status);

                /**
                 * Access to this Table's Columns.
                 *
                 */
                public Columns columns();

                /**
                 * Public interface for accessing this Table's Columns.
                 *
                 */
                public interface Columns
                    {
                    /**
                     * Select a named Column.
                     *
                     */
                    public Table.Column select(String name);

                    /**
                     * Set the status of this Column.
                     *
                     */
                    public void status(Status status);

                    /**
                     * Select all the Columns in this Table.
                     *
                     */
                    public Iterable<Table.Column> select();
                    }

                /**
                 * Public interface for Column metadata.
                 *
                 */
                public interface Column
                extends Entity
                    {
                    /**
                     * Access to our parent Table.
                     *
                     */
                    public Table parent();

                    /**
                     * The status of this Column.
                     *
                     */
                    public Status status();

                    }
                }
            }
        }

    /**
     * Public interface for a base Widgeon, based on what IS.
     *
     */
    public interface Base
    extends Widgeon
        {

        /**
         * Factory interface for creating and selecting Widgeons.
         *
         */
        public static interface Factory
        extends Entity.Factory<Widgeon.Base>
            {

            /**
             * Select all of the Widgeon Entities.
             *
             */
            public Iterable<Widgeon.Base> select();

            /**
             * Create a Widgeon from a registry URI.
             * 
             */
            public Widgeon.Base create(String name, URI uri);

            /**
             * Create a Widgeon from a VOSI URL.
             * 
             */
            public Widgeon.Base create(String name, URL url);

            /**
             * Create a Widgeon from a JDBC DataSource.
             * 
             */
            public Widgeon.Base create(String name, DataSource src);

            /**
             * Access to our View factory.
             * 
             */
            public Widgeon.View.Factory views();

            /**
             * Access to our Schema factory.
             * 
             */
            public Widgeon.Base.Schema.Factory schemas();

            }

        /**
         * Public interface for accessing the Views of this Widgeon.
         *
         */
        public interface Views
            {
            /*
             * Create a new View of this Widgeon.
             *
             */
            public Widgeon.View create(String name);

            /*
             * Select all the Views of this Widgeon.
             *
             */
            public Iterable<Widgeon.View> select();

            /*
             * Select a named View of this Widgeon.
             *
             */
            public Widgeon.View select(String name);

            }

        /**
         * Access to the Views of this Widgeon.
         *
         */
        public Views views();

        /**
         * Public interface for accessing this Widgeon's Schema.
         *
         */
        public interface Schemas
        extends Widgeon.Schemas<Widgeon.Base.Schema>
            {
            /**
             * Create a new Schema for this Widgeon.
             *
             */
            public Widgeon.Base.Schema create(String name);
            }

        /**
         * Access to the Schemas for this Widgeon.
         *
         */
        public Schemas schemas();

        /**
         * Public interface for Schema metadata.
         *
         */
        public interface Schema
        extends Widgeon.Schema
            {

            /**
             * Factory interface for creating and selecting Schema.
             *
             */
            public static interface Factory
            extends Entity.Factory<Widgeon.Base.Schema>
                {
                /**
                 * Create a new Schema for a Base Widgeon.
                 *
                 */
                public Widgeon.Base.Schema create(Widgeon.Base parent, String name);

                /**
                 * Select a named Schema for a Widgeon.
                 *
                 */
                public Widgeon.Base.Schema select(Widgeon.Base parent, String name);

                /**
                 * Select all the Schema for a Widgeon.
                 *
                 */
                public Iterable<Widgeon.Base.Schema> select(Widgeon.Base parent);

                /**
                 * Access to our Catalog factory.
                 * 
                 */
                public Widgeon.Base.Schema.Catalog.Factory catalogs();

                }

            /**
             * Public interface for accessing this Schema's Catalogs.
             *
             */
            public interface Catalogs
            extends Widgeon.Schema.Catalogs
                {
                /**
                 * Create a new Table for this Catalog.
                 *
                 */
                public Widgeon.Base.Schema.Catalog create(String name);
                }

            /**
             * Access to the Catalogss for this Schema.
             *
             */
            public Catalogs catalogs();

            }
        }

    /**
     * Public interface for a View of a Widgeon.
     *
     */
    public interface View
    extends Widgeon
        {

        /**
         * Factory interface for creating and selecting Widgeon Views.
         *
         */
        public static interface Factory
        extends Entity.Factory<Widgeon.View>
            {
            /**
             * Create a View of a Widgeon.
             *
             */
            public Widgeon.View create(Widgeon.Base base, String name);

            /**
             * Select a named View of a Widgeon.
             *
             */
            public Widgeon.View select(Widgeon.Base base, String name);

            /**
             * Select all the Views for a Widgeon.
             *
             */
            public Iterable<Widgeon.View> select(Widgeon.Base base);

            /**
             * Access to our Schema factory.
             * 
             */
            public Widgeon.View.Schema.Factory schemas();

            }

        /**
         * Access to our base Widgeon.
         *
         */
        public Widgeon.Base base();

        /**
         * Public interface for accessing this Widgeon's Schema.
         *
         */
        public interface Schemas
        extends Widgeon.Schemas<Widgeon.View.Schema>
            {
            }

        /**
         * Access to the Schemas for this Widgeon.
         *
         */
        public Schemas schemas();

        /**
         * Public interface for a Schema View.
         *
         */
        public interface Schema
        extends Widgeon.Schema
            {

            /**
             * Factory interface for creating and selecting Schema Views.
             *
             */
            public static interface Factory
            extends Entity.Factory<Widgeon.View.Schema>
                {
                /**
                 * Create a View of a Schema.
                 *
                 */
                public Widgeon.View.Schema create(Widgeon.Base.Schema base, Widgeon.View parent, String name);

                /**
                 * Select a named Schema from a Widgeon View.
                 *
                 */
                public Widgeon.View.Schema select(Widgeon.View parent, String name);

                /**
                 * Select all the Schema from a Widgeon View.
                 *
                 */
                public Iterable<Widgeon.View.Schema> select(Widgeon.View parent);

                /**
                 * Access to our Catalog factory.
                 * 
                 */
                public Widgeon.View.Schema.Catalog.Factory catalogs();

                }

            /**
             * Access to our base Schema.
             *
             */
            public Widgeon.Base.Schema base();

            /**
             * Public interface for accessing this Schema's Catalogs.
             *
             */
            public interface Catalogs
            extends Widgeon.Schema.Catalogs
                {
                }

            /**
             * Access to the Catalogss for this Schema.
             *
             */
            public Catalogs catalogs();

            }
        }
    }

