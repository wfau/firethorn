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
 * Called Widgeon because the word Resource has already been used so many times to mean different things in different places.
 *
 */
public interface Widgeon
extends WidgeonStatus
    {

    /**
     * Access to this Widgeon's Schema.
     *
     */
    public Schemas schemas();

    /**
     * Public interface for accessing the Schema for a Widgeon.
     *
     */
    public interface Schemas<SchemaType extends Widgeon.Schema>
        {

        /**
         * Select all the Schema from the Widgeon.
         *
         */
        public Iterable<SchemaType> select();

        /**
         * Select a named Schema from the Widgeon.
         *
         */
        public SchemaType select(String name)
        throws NameNotFoundException;

        /**
         * Search for a named Schema from the Widgeon.
         *
         */
        public SchemaType search(String name);

        }

    /**
     * Public interface for Schema metadata.
     *
     */
    public interface Schema<WidgeonType extends Widgeon>
    extends WidgeonComponent<WidgeonType>
        {

        /**
         * Factory interface for creating and selecting Schema.
         *
         */
        public static interface Factory<WidgeonType extends Widgeon, SchemaType extends Widgeon.Schema>
        extends Entity.Factory<SchemaType>
            {

            /**
             * Select all the Schema from a Widgeon.
             *
             */
            public Iterable<SchemaType> select(WidgeonType parent);

            /**
             * Select a named Schema from a Widgeon.
             *
             */
            public SchemaType select(WidgeonType parent, String name)
            throws NameNotFoundException;

            /**
             * Search for a named Schema from a Widgeon.
             *
             */
            public SchemaType search(WidgeonType parent, String name);

            }

        /**
         * Access to this Schema's Catalogs.
         *
         */
        public Catalogs catalogs();

        /**
         * Public interface for accessing the Catalogs for a Schema.
         *
         */
        public interface Catalogs<CatalogType extends Widgeon.Schema.Catalog>
            {

            /**
             * Select all the Catalogs from the Schema.
             *
             */
            public Iterable<CatalogType> select();

            /**
             * Select a named Catalog from the Schema.
             *
             */
            public CatalogType select(String name)
            throws NameNotFoundException;

            /**
             * Search for a named Catalog from the Schema.
             *
             */
            public CatalogType search(String name);

            }

        /**
         * Public interface for Catalog metadata.
         *
         */
        public interface Catalog<SchemaType extends Widgeon.Schema>
        extends WidgeonComponent<SchemaType>
            {

            /**
             * Factory interface for creating and selecting Catalogs.
             *
             */
            public static interface Factory<SchemaType extends Widgeon.Schema, CatalogType extends Widgeon.Schema.Catalog>
            extends Entity.Factory<CatalogType>
                {

                /**
                 * Select all the Catalogs from a Schema.
                 *
                 */
                public Iterable<CatalogType> select(SchemaType parent);

                /**
                 * Select a named Catalog from a Schema.
                 *
                 */
                public CatalogType select(SchemaType parent, String name)
                throws NameNotFoundException;

                /**
                 * Search for named Catalog from a Schema.
                 *
                 */
                public CatalogType search(SchemaType parent, String name);

                }

            /**
             * Access to this Catalog's Tables.
             *
             */
            public Tables tables();

            /**
             * Public interface for accessing the Tables for a Catalog.
             *
             */
            public interface Tables<TableType extends Widgeon.Schema.Catalog.Table>
                {

                /**
                 * Select all the Tables from the Catalog.
                 *
                 */
                public Iterable<TableType> select();

                /**
                 * Select a named Table from the Catalog.
                 *
                 */
                public TableType select(String name)
                throws NameNotFoundException;

                /**
                 * Search for a named Table from the Catalog.
                 *
                 */
                public TableType search(String name);

                }

            /**
             * Public interface for Table metadata.
             *
             */
            public interface Table<CatalogType extends Widgeon.Schema.Catalog>
            extends WidgeonComponent<CatalogType>
                {

                /**
                 * Factory interface for creating and selecting Tables.
                 *
                 */
                public static interface Factory<CatalogType extends Widgeon.Schema.Catalog, TableType extends Widgeon.Schema.Catalog.Table>
                extends Entity.Factory<TableType>
                    {

                    /**
                     * Select all the Tables from a Catalog.
                     *
                     */
                    public Iterable<TableType> select(CatalogType parent);

                    /**
                     * Select a named Table from a Catalog.
                     *
                     */
                    public TableType select(CatalogType parent, String name)
                    throws NameNotFoundException;

                    /**
                     * Search for a named Table from a Catalog.
                     *
                     */
                    public TableType search(CatalogType parent, String name);

                    }

                /**
                 * Access to this Table's Columns.
                 *
                 */
                public Columns columns();

                /**
                 * Public interface for accessing the Columns for a Table.
                 *
                 */
                public interface Columns<ColumnType extends Widgeon.Schema.Catalog.Table.Column>
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

                /**
                 * Public interface for Column metadata.
                 *
                 */
                public interface Column<TableType extends Widgeon.Schema.Catalog.Table>
                extends WidgeonComponent<TableType>
                    {

                    /**
                     * Factory interface for creating and selecting Columns.
                     *
                     */
                    public static interface Factory<TableType extends Widgeon.Schema.Catalog.Table, ColumnType extends Widgeon.Schema.Catalog.Table.Column>
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
            }
        }

    /**
     * Public interface for a base Widgeon, based on what is.
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
             * Select all of the Widgeons.
             *
             */
            public Iterable<Widgeon.Base> select();

            /**
             * Select a Widgeon by name.
             *
             */
            public Widgeon.Base select(String name)
            throws NameNotFoundException;

            /**
             * Serahc for a Widgeon by name.
             *
             */
            public Widgeon.Base search(String name);

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
         * Public interface for accessing the Views of a Widgeon.
         *
         */
        public interface Views
            {
            /*
             * Create a new View of the Widgeon.
             *
             */
            public Widgeon.View create(String name);

            /*
             * Select all the Views of the Widgeon.
             *
             */
            public Iterable<Widgeon.View> select();

            /*
             * Select a named View from the Widgeon.
             *
             */
            public Widgeon.View select(String name)
            throws NameNotFoundException;

            /*
             * Search for a named View from the Widgeon.
             *
             */
            public Widgeon.View search(String name);

            }

        /**
         * Access to the Views of this Widgeon.
         *
         */
        public Widgeon.Base.Views views();

        /**
         * Public interface for accessing the Schema for a Widgeon.
         *
         */
        public interface Schemas
        extends Widgeon.Schemas<Widgeon.Base.Schema>
            {

            /**
             * Create a new Schema for the Widgeon.
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
        extends Widgeon.Schema<Widgeon.Base>
            {

            /**
             * Factory interface for creating and selecting Schema.
             *
             */
            public static interface Factory
            extends Widgeon.Schema.Factory<Widgeon.Base, Widgeon.Base.Schema>
                {

                /**
                 * Create a new Schema for a Widgeon.
                 *
                 */
                public Widgeon.Base.Schema create(Widgeon.Base parent, String name);

                /**
                 * Access to our Catalog factory.
                 * 
                 */
                public Widgeon.Base.Schema.Catalog.Factory catalogs();

                }

            /**
             * Public interface for accessing the Views of a Schema.
             *
             */
            public interface Views
                {

                /*
                 * Select all the Views of the Schema.
                 *
                 */
                public Iterable<Widgeon.View.Schema> select();

                }

            /**
             * Access to the Views of this Schema.
             *
             */
            public Widgeon.Base.Schema.Views views();

            /**
             * Public interface for accessing the Catalogs for a Schema.
             *
             */
            public interface Catalogs
            extends Widgeon.Schema.Catalogs<Widgeon.Base.Schema.Catalog>
                {

                /**
                 * Create a new Catalog for the Schema.
                 *
                 */
                public Widgeon.Base.Schema.Catalog create(String name);

                }

            /**
             * Access to the Catalogs for this Schema.
             *
             */
            public Catalogs catalogs();

            /**
             * Public interface for Catalog metadata.
             *
             */
            public interface Catalog
            extends Widgeon.Schema.Catalog
                {

                /**
                 * Factory interface for creating and selecting Catalogs.
                 *
                 */
                public static interface Factory
                extends Widgeon.Schema.Catalog.Factory<Widgeon.Base.Schema, Widgeon.Base.Schema.Catalog>
                    {

                    /**
                     * Create a new Catalog for a Schema.
                     *
                     */
                    public Widgeon.Base.Schema.Catalog create(Widgeon.Base.Schema parent, String name);

                    /**
                     * Access to our Table factory.
                     * 
                     */
                    public Widgeon.Base.Schema.Catalog.Table.Factory tables();

                    }

                /**
                 * Public interface for accessing the Tables for a Catalog.
                 *
                 */
                public interface Tables
                extends Widgeon.Schema.Catalog.Tables<Widgeon.Base.Schema.Catalog.Table>
                    {

                    /**
                     * Create a new Table for the Catalog.
                     *
                     */
                    public Widgeon.Base.Schema.Catalog.Table create(String name);

                    }

                /**
                 * Access to the Tables for this Catalog.
                 *
                 */
                public Tables tables();

                /**
                 * Public interface for Table metadata.
                 *
                 */
                public interface Table
                extends Widgeon.Schema.Catalog.Table
                    {

                    /**
                     * Factory interface for creating and selecting Tables.
                     *
                     */
                    public static interface Factory
                    extends Widgeon.Schema.Catalog.Table.Factory<Widgeon.Base.Schema.Catalog, Widgeon.Base.Schema.Catalog.Table>
                        {

                        /**
                         * Create a new Table for a Catalog.
                         *
                         */
                        public Widgeon.Base.Schema.Catalog.Table create(Widgeon.Base.Schema.Catalog parent, String name);

                        /**
                         * Access to our Column factory.
                         * 
                         */
                        public Widgeon.Base.Schema.Catalog.Table.Column.Factory columns();

                        }

                    /**
                     * Public interface for accessing the Columns for a Table.
                     *
                     */
                    public interface Columns
                    extends Widgeon.Schema.Catalog.Table.Columns<Widgeon.Base.Schema.Catalog.Table.Column>
                        {

                        /**
                         * Create a new Column for the Table.
                         *
                         */
                        public Widgeon.Base.Schema.Catalog.Table.Column create(String name);

                        }

                    /**
                     * Access to the Columns for this Table.
                     *
                     */
                    public Columns columns();

                    /**
                     * Public interface for Column metadata.
                     *
                     */
                    public interface Column
                    extends Widgeon.Schema.Catalog.Table.Column
                        {

                        /**
                         * Factory interface for creating and selecting Columns.
                         *
                         */
                        public static interface Factory
                        extends Widgeon.Schema.Catalog.Table.Column.Factory<Widgeon.Base.Schema.Catalog.Table, Widgeon.Base.Schema.Catalog.Table.Column>
                            {

                            /**
                             * Create a new Column for a Table.
                             *
                             */
                            public Widgeon.Base.Schema.Catalog.Table.Column create(Widgeon.Base.Schema.Catalog.Table parent, String name);

                            }
                        }
                    }
                }
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
             * Select all the Views of a Widgeon.
             *
             */
            public Iterable<Widgeon.View> select(Widgeon.Base base);

            /**
             * Select a named View of a Widgeon.
             *
             */
            public Widgeon.View select(Widgeon.Base base, String name)
            throws NameNotFoundException;

            /**
             * Search for a named View of a Widgeon.
             *
             */
            public Widgeon.View search(Widgeon.Base base, String name);

            /**
             * Access to our Schema factory.
             * 
             */
            public Widgeon.View.Schema.Factory schemas();

            }

        /**
         * Access to our underlying Widgeon.
         *
         */
        public Widgeon.Base base();

        /**
         * Check our child schema.
         *
         */
        public void check();

        /**
         * Public interface for accessing the Schema for a Widgeon.
         *
         */
        public interface Schemas
        extends Widgeon.Schemas<Widgeon.View.Schema>
            {

            /**
             * Check for a matching View of a base Schema.
             *
             */
            public Widgeon.View.Schema check(Widgeon.Base.Schema base);

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
        extends Widgeon.Schema<Widgeon.View>
            {

            /**
             * Factory interface for creating and selecting Schema Views.
             *
             */
            public static interface Factory
            extends Widgeon.Schema.Factory<Widgeon.View, Widgeon.View.Schema>
                {

                /**
                 * Create a new View of a Schema.
                 *
                 */
                public Widgeon.View.Schema create(Widgeon.View parent, Widgeon.Base.Schema base);

                /**
                 * Create a new View of a Schema.
                 *
                 */
                public Widgeon.View.Schema create(Widgeon.View parent, Widgeon.Base.Schema base, String name);

                /**
                 * Search for a View of a Schema.
                 *
                 */
                public Widgeon.View.Schema search(Widgeon.View parent, Widgeon.Base.Schema base);

                /**
                 * Select all the views of a Schema.
                 *
                 */
                public Iterable<Widgeon.View.Schema> select(Widgeon.Base.Schema base);

                /**
                 * Access to our Catalog factory.
                 * 
                 */
                public Widgeon.View.Schema.Catalog.Factory catalogs();

                }

            /**
             * Access to our underlying Schema.
             *
             */
            public Widgeon.Base.Schema base();

            /**
             * Public interface for accessing the Catalogs for a Schema.
             *
             */
            public interface Catalogs
            extends Widgeon.Schema.Catalogs<Widgeon.View.Schema.Catalog>
                {

                /**
                 * Check for a matching View of a base Catalog.
                 *
                 */
                public Widgeon.View.Schema.Catalog check(Widgeon.Base.Schema.Catalog base);

                }

            /**
             * Access to the Catalogss for this Schema.
             *
             */
            public Catalogs catalogs();

            /**
             * Public interface for Catalog metadata.
             *
             */
            public interface Catalog
            extends Widgeon.Schema.Catalog<Widgeon.View.Schema>
                {

                /**
                 * Factory interface for creating and selecting Catalogs.
                 *
                 */
                public static interface Factory
                extends Widgeon.Schema.Catalog.Factory<Widgeon.View.Schema, Widgeon.View.Schema.Catalog>
                    {

                    /**
                     * Create a new View of a Catalog.
                     *
                     */
                    public Widgeon.View.Schema.Catalog create(Widgeon.View.Schema parent, Widgeon.Base.Schema.Catalog base);

                    /**
                     * Create a new View of a Catalog.
                     *
                     */
                    public Widgeon.View.Schema.Catalog create(Widgeon.View.Schema parent, Widgeon.Base.Schema.Catalog base, String name);

                    /**
                     * Search for a View of a Catalog.
                     *
                     */
                    public Widgeon.View.Schema.Catalog search(Widgeon.View.Schema parent, Widgeon.Base.Schema.Catalog base);

                    /**
                     * Access to our Table factory.
                     * 
                     */
                    public Widgeon.View.Schema.Catalog.Table.Factory tables();
                    
                    }

                /**
                 * Access to our underlying Catalog.
                 *
                 */
                public Widgeon.Base.Schema.Catalog base();

                /**
                 * Public interface for accessing the Tables for a Catalog.
                 *
                 */
                public interface Tables
                extends Widgeon.Schema.Catalog.Tables<Widgeon.View.Schema.Catalog.Table>
                    {
                    }

                /**
                 * Access to the Tables for this Catalog.
                 *
                 */
                public Tables tables();

                /**
                 * Public interface for Table metadata.
                 *
                 */
                public interface Table
                extends Widgeon.Schema.Catalog.Table<Widgeon.View.Schema.Catalog>
                    {

                    /**
                     * Factory interface for creating and selecting Tables.
                     *
                     */
                    public static interface Factory
                    extends Widgeon.Schema.Catalog.Table.Factory<Widgeon.View.Schema.Catalog, Widgeon.View.Schema.Catalog.Table>
                        {

                        /**
                         * Create a new View of a Table.
                         *
                         */
                        public Widgeon.View.Schema.Catalog.Table create(Widgeon.View.Schema.Catalog parent, Widgeon.Base.Schema.Catalog.Table base, String name);

                        /**
                         * Access to our Column factory.
                         * 
                         */
                        public Widgeon.View.Schema.Catalog.Table.Column.Factory columns();

                        }

                    /**
                     * Access to our underlying Table.
                     *
                     */
                    public Widgeon.Base.Schema.Catalog.Table base();

                    /**
                     * Public interface for accessing the Columns for a Table.
                     *
                     */
                    public interface Columns
                    extends Widgeon.Schema.Catalog.Table.Columns<Widgeon.View.Schema.Catalog.Table.Column>
                        {
                        }

                    /**
                     * Access to the Columns for this Table.
                     *
                     */
                    public Columns columns();

                    /**
                     * Public interface for Column metadata.
                     *
                     */
                    public interface Column
                    extends Widgeon.Schema.Catalog.Table.Column<Widgeon.View.Schema.Catalog.Table>
                        {

                        /**
                         * Factory interface for creating and selecting Columns.
                         *
                         */
                        public static interface Factory
                        extends Widgeon.Schema.Catalog.Table.Column.Factory<Widgeon.View.Schema.Catalog.Table, Widgeon.View.Schema.Catalog.Table.Column>
                            {

                            /**
                             * Create a new View of a Column.
                             *
                             */
                            public Widgeon.View.Schema.Catalog.Table.Column create(Widgeon.View.Schema.Catalog.Table parent, Widgeon.Base.Schema.Catalog.Table.Column base, String name);

                            }

                        /**
                         * Access to our underlying Column.
                         *
                         */
                        public Widgeon.Base.Schema.Catalog.Table.Column base();

                        }
                    }
                }
            }
        }
    }

