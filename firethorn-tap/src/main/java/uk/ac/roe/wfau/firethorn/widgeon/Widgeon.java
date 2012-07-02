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
     * Access to this Widgeon's Catalog.
     *
     */
    public Catalogs catalogs();

    /**
     * Public interface for accessing the Catalog for a Widgeon.
     *
     */
    public interface Catalogs<CatalogType extends Widgeon.Catalog>
        {

        /**
         * Select all the Catalog from the Widgeon.
         *
         */
        public Iterable<CatalogType> select();

        /**
         * Select a named Catalog from the Widgeon.
         *
         */
        public CatalogType select(String name)
        throws NameNotFoundException;

        /**
         * Search for a named Catalog from the Widgeon.
         *
         */
        public CatalogType search(String name);

        }

    /**
     * Public interface for Catalog metadata.
     *
     */
    public interface Catalog<WidgeonType extends Widgeon>
    extends WidgeonComponent<WidgeonType>
        {

        /**
         * Factory interface for creating and selecting Catalog.
         *
         */
        public static interface Factory<WidgeonType extends Widgeon, CatalogType extends Widgeon.Catalog>
        extends Entity.Factory<CatalogType>
            {

            /**
             * Select all the Catalog from a Widgeon.
             *
             */
            public Iterable<CatalogType> select(WidgeonType parent);

            /**
             * Select a named Catalog from a Widgeon.
             *
             */
            public CatalogType select(WidgeonType parent, String name)
            throws NameNotFoundException;

            /**
             * Search for a named Catalog from a Widgeon.
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
         * Public interface for accessing the Schemas for a Catalog.
         *
         */
        public interface Schemas<SchemaType extends Widgeon.Catalog.Schema>
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

        /**
         * Public interface for Schema metadata.
         *
         */
        public interface Schema<CatalogType extends Widgeon.Catalog>
        extends WidgeonComponent<CatalogType>
            {

            /**
             * Factory interface for creating and selecting Schemas.
             *
             */
            public static interface Factory<CatalogType extends Widgeon.Catalog, SchemaType extends Widgeon.Catalog.Schema>
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
                 * Search for named Schema from a Catalog.
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
             * Public interface for accessing the Tables for a Schema.
             *
             */
            public interface Tables<TableType extends Widgeon.Catalog.Schema.Table>
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

            /**
             * Public interface for Table metadata.
             *
             */
            public interface Table<SchemaType extends Widgeon.Catalog.Schema>
            extends WidgeonComponent<SchemaType>
                {

                /**
                 * Factory interface for creating and selecting Tables.
                 *
                 */
                public static interface Factory<SchemaType extends Widgeon.Catalog.Schema, TableType extends Widgeon.Catalog.Schema.Table>
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
                 * Public interface for accessing the Columns for a Table.
                 *
                 */
                public interface Columns<ColumnType extends Widgeon.Catalog.Schema.Table.Column>
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
                public interface Column<TableType extends Widgeon.Catalog.Schema.Table>
                extends WidgeonComponent<TableType>
                    {

                    /**
                     * Factory interface for creating and selecting Columns.
                     *
                     */
                    public static interface Factory<TableType extends Widgeon.Catalog.Schema.Table, ColumnType extends Widgeon.Catalog.Schema.Table.Column>
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
             * Access to our Catalog factory.
             * 
             */
            public Widgeon.Base.Catalog.Factory catalogs();

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
         * Public interface for accessing a Widgeon's Catalogs.
         *
         */
        public interface Catalogs
        extends Widgeon.Catalogs<Widgeon.Base.Catalog>
            {

            /**
             * Create a new Catalog for the Widgeon.
             *
             */
            public Widgeon.Base.Catalog create(String name);

            }

        /**
         * Access to the Catalogs for this Widgeon.
         *
         */
        public Catalogs catalogs();

        /**
         * Public interface for Catalog metadata.
         *
         */
        public interface Catalog
        extends Widgeon.Catalog<Widgeon.Base>
            {

            /**
             * Factory interface for creating and selecting Catalog.
             *
             */
            public static interface Factory
            extends Widgeon.Catalog.Factory<Widgeon.Base, Widgeon.Base.Catalog>
                {

                /**
                 * Create a new Catalog for a Widgeon.
                 *
                 */
                public Widgeon.Base.Catalog create(Widgeon.Base parent, String name);

                /**
                 * Access to our View factory.
                 * 
                 */
                public Widgeon.View.Catalog.Factory views();

                /**
                 * Access to our Schema factory.
                 * 
                 */
                public Widgeon.Base.Catalog.Schema.Factory schemas();

                }

            /**
             * Public interface for accessing the Views of a Catalog.
             *
             */
            public interface Views
                {

                /*
                 * Select all the Views of the Catalog.
                 *
                 */
                public Iterable<Widgeon.View.Catalog> select();

                }

            /**
             * Access to the Views of this Catalog.
             *
             */
            public Widgeon.Base.Catalog.Views views();

            /**
             * Public interface for accessing the Schemas of a Catalog.
             *
             */
            public interface Schemas
            extends Widgeon.Catalog.Schemas<Widgeon.Base.Catalog.Schema>
                {

                /**
                 * Create a new Schema for the Catalog.
                 *
                 */
                public Widgeon.Base.Catalog.Schema create(String name);

                }

            /**
             * Access to the Schemas for this Catalog.
             *
             */
            public Schemas schemas();

            /**
             * Public interface for Schema metadata.
             *
             */
            public interface Schema
            extends Widgeon.Catalog.Schema
                {

                /**
                 * Factory interface for creating and selecting Schemas.
                 *
                 */
                public static interface Factory
                extends Widgeon.Catalog.Schema.Factory<Widgeon.Base.Catalog, Widgeon.Base.Catalog.Schema>
                    {

                    /**
                     * Create a new Schema for a Catalog.
                     *
                     */
                    public Widgeon.Base.Catalog.Schema create(Widgeon.Base.Catalog parent, String name);

                    /**
                     * Access to our View factory.
                     * 
                     */
                    public Widgeon.View.Catalog.Schema.Factory views();

                    /**
                     * Access to our Table factory.
                     * 
                     */
                    public Widgeon.Base.Catalog.Schema.Table.Factory tables();

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
                    public Iterable<Widgeon.View.Catalog.Schema> select();

                    }

                /**
                 * Access to the Views of this Schema.
                 *
                 */
                public Widgeon.Base.Catalog.Schema.Views views();

                /**
                 * Public interface for accessing the Tables of a Schema.
                 *
                 */
                public interface Tables
                extends Widgeon.Catalog.Schema.Tables<Widgeon.Base.Catalog.Schema.Table>
                    {

                    /**
                     * Create a new Table for the Schema.
                     *
                     */
                    public Widgeon.Base.Catalog.Schema.Table create(String name);

                    }

                /**
                 * Access to the Tables for this Schema.
                 *
                 */
                public Tables tables();

                /**
                 * Public interface for Table metadata.
                 *
                 */
                public interface Table
                extends Widgeon.Catalog.Schema.Table
                    {

                    /**
                     * Factory interface for creating and selecting Tables.
                     *
                     */
                    public static interface Factory
                    extends Widgeon.Catalog.Schema.Table.Factory<Widgeon.Base.Catalog.Schema, Widgeon.Base.Catalog.Schema.Table>
                        {

                        /**
                         * Create a new Table for a Schema.
                         *
                         */
                        public Widgeon.Base.Catalog.Schema.Table create(Widgeon.Base.Catalog.Schema parent, String name);

                        /**
                         * Access to our View factory.
                         * 
                         */
                        public Widgeon.View.Catalog.Schema.Table.Factory views();

                        /**
                         * Access to our Column factory.
                         * 
                         */
                        public Widgeon.Base.Catalog.Schema.Table.Column.Factory columns();

                        }

                    /**
                     * Public interface for accessing the Views of a Table.
                     *
                     */
                    public interface Views
                        {

                        /*
                         * Select all the Views of the Table.
                         *
                         */
                        public Iterable<Widgeon.View.Catalog.Schema.Table> select();

                        }

                    /**
                     * Access to the Views of this Table.
                     *
                     */
                    public Widgeon.Base.Catalog.Schema.Table.Views views();

                    /**
                     * Public interface for accessing the Columns of a Table.
                     *
                     */
                    public interface Columns
                    extends Widgeon.Catalog.Schema.Table.Columns<Widgeon.Base.Catalog.Schema.Table.Column>
                        {

                        /**
                         * Create a new Column for the Table.
                         *
                         */
                        public Widgeon.Base.Catalog.Schema.Table.Column create(String name);

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
                    extends Widgeon.Catalog.Schema.Table.Column
                        {

                        /**
                         * Factory interface for creating and selecting Columns.
                         *
                         */
                        public static interface Factory
                        extends Widgeon.Catalog.Schema.Table.Column.Factory<Widgeon.Base.Catalog.Schema.Table, Widgeon.Base.Catalog.Schema.Table.Column>
                            {

                            /**
                             * Create a new Column for a Table.
                             *
                             */
                            public Widgeon.Base.Catalog.Schema.Table.Column create(Widgeon.Base.Catalog.Schema.Table parent, String name);

                            /**
                             * Access to our View factory.
                             * 
                             */
                            public Widgeon.View.Catalog.Schema.Table.Column.Factory views();

                            }

                        /**
                         * Public interface for accessing the Views of a Column.
                         *
                         */
                        public interface Views
                            {

                            /*
                             * Select all the Views of the Column.
                             *
                             */
                            public Iterable<Widgeon.View.Catalog.Schema.Table.Column> select();

                            }

                        /**
                         * Access to the Views of this Column.
                         *
                         */
                        public Widgeon.Base.Catalog.Schema.Table.Column.Views views();

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
             * Access to our Catalog factory.
             * 
             */
            public Widgeon.View.Catalog.Factory catalogs();

            }

        /**
         * Access to our underlying Widgeon.
         *
         */
        public Widgeon.Base base();

        /**
         * Public interface for accessing the Catalog of a Widgeon.
         *
         */
        public interface Catalogs
        extends Widgeon.Catalogs<Widgeon.View.Catalog>
            {

            /**
             * Find an existing View of a Catalog, or create a new one.
             *
            public Widgeon.View.Catalog cascade(Widgeon.Base.Catalog base);
             */

            }

        /**
         * Access to the Catalogs for this Widgeon.
         *
         */
        public Catalogs catalogs();

        /**
         * Public interface for a Catalog View.
         *
         */
        public interface Catalog
        extends Widgeon.Catalog<Widgeon.View>
            {

            /**
             * Factory interface for creating and selecting Catalog Views.
             *
             */
            public static interface Factory
            extends Widgeon.Catalog.Factory<Widgeon.View, Widgeon.View.Catalog>
                {

                /**
                 * Find an existing View of a Catalog, or create a new one.
                 *
                 */
                public Widgeon.View.Catalog cascade(Widgeon.View parent, Widgeon.Base.Catalog base);

                /**
                 * Create a new View of a Catalog.
                 *
                 */
                public Widgeon.View.Catalog create(Widgeon.View parent, Widgeon.Base.Catalog base, String name);

                /**
                 * Select all the views of a Catalog.
                 *
                 */
                public Iterable<Widgeon.View.Catalog> select(Widgeon.Base.Catalog base);

                /**
                 * Access to our Schema factory.
                 * 
                 */
                public Widgeon.View.Catalog.Schema.Factory schemas();

                }

            /**
             * Access to our underlying Catalog.
             *
             */
            public Widgeon.Base.Catalog base();

            /**
             * Public interface for accessing the Schemas of a Catalog.
             *
             */
            public interface Schemas
            extends Widgeon.Catalog.Schemas<Widgeon.View.Catalog.Schema>
                {
                }

            /**
             * Access to the Schemass for this Catalog.
             *
             */
            public Schemas schemas();

            /**
             * Public interface for Schema metadata.
             *
             */
            public interface Schema
            extends Widgeon.Catalog.Schema<Widgeon.View.Catalog>
                {

                /**
                 * Factory interface for creating and selecting Schemas.
                 *
                 */
                public static interface Factory
                extends Widgeon.Catalog.Schema.Factory<Widgeon.View.Catalog, Widgeon.View.Catalog.Schema>
                    {

                    /**
                     * Find an existing View of a Schema, or create a new one.
                     *
                     */
                    public Widgeon.View.Catalog.Schema cascade(Widgeon.View.Catalog parent, Widgeon.Base.Catalog.Schema base);

                    /**
                     * Create a new View of a Schema.
                     *
                     */
                    public Widgeon.View.Catalog.Schema create(Widgeon.View.Catalog parent, Widgeon.Base.Catalog.Schema base, String name);

                    /**
                     * Select all the views of a Schema.
                     *
                     */
                    public Iterable<Widgeon.View.Catalog.Schema> select(Widgeon.Base.Catalog.Schema base);

                    /**
                     * Access to our Table factory.
                     * 
                     */
                    public Widgeon.View.Catalog.Schema.Table.Factory tables();
                    
                    }

                /**
                 * Access to our underlying Schema.
                 *
                 */
                public Widgeon.Base.Catalog.Schema base();

                /**
                 * Public interface for accessing the Tables of a Schema.
                 *
                 */
                public interface Tables
                extends Widgeon.Catalog.Schema.Tables<Widgeon.View.Catalog.Schema.Table>
                    {
                    }

                /**
                 * Access to the Tables for this Schema.
                 *
                 */
                public Tables tables();

                /**
                 * Public interface for Table metadata.
                 *
                 */
                public interface Table
                extends Widgeon.Catalog.Schema.Table<Widgeon.View.Catalog.Schema>
                    {

                    /**
                     * Factory interface for creating and selecting Tables.
                     *
                     */
                    public static interface Factory
                    extends Widgeon.Catalog.Schema.Table.Factory<Widgeon.View.Catalog.Schema, Widgeon.View.Catalog.Schema.Table>
                        {

                        /**
                         * Find an existing View of a Table, or create a new one.
                         *
                         */
                        public Widgeon.View.Catalog.Schema.Table cascade(Widgeon.View.Catalog.Schema parent, Widgeon.Base.Catalog.Schema.Table base);

                        /**
                         * Create a new View of a Table.
                         *
                         */
                        public Widgeon.View.Catalog.Schema.Table create(Widgeon.View.Catalog.Schema parent, Widgeon.Base.Catalog.Schema.Table base, String name);

                        /**
                         * Select all the views of a Table.
                         *
                         */
                        public Iterable<Widgeon.View.Catalog.Schema.Table> select(Widgeon.Base.Catalog.Schema.Table base);

                        /**
                         * Access to our Column factory.
                         * 
                         */
                        public Widgeon.View.Catalog.Schema.Table.Column.Factory columns();

                        }

                    /**
                     * Access to our underlying Table.
                     *
                     */
                    public Widgeon.Base.Catalog.Schema.Table base();

                    /**
                     * Public interface for accessing the Columns of a Table.
                     *
                     */
                    public interface Columns
                    extends Widgeon.Catalog.Schema.Table.Columns<Widgeon.View.Catalog.Schema.Table.Column>
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
                    extends Widgeon.Catalog.Schema.Table.Column<Widgeon.View.Catalog.Schema.Table>
                        {

                        /**
                         * Factory interface for creating and selecting Columns.
                         *
                         */
                        public static interface Factory
                        extends Widgeon.Catalog.Schema.Table.Column.Factory<Widgeon.View.Catalog.Schema.Table, Widgeon.View.Catalog.Schema.Table.Column>
                            {

                            /**
                             * Find an existing View of a Column, or create a new one.
                             *
                             */
                            public Widgeon.View.Catalog.Schema.Table.Column cascade(Widgeon.View.Catalog.Schema.Table parent, Widgeon.Base.Catalog.Schema.Table.Column base);

                            /**
                             * Create a new View of a Column.
                             *
                             */
                            public Widgeon.View.Catalog.Schema.Table.Column create(Widgeon.View.Catalog.Schema.Table parent, Widgeon.Base.Catalog.Schema.Table.Column base, String name);

                            /**
                             * Select all the views of a Column.
                             *
                             */
                            public Iterable<Widgeon.View.Catalog.Schema.Table.Column> select(Widgeon.Base.Catalog.Schema.Table.Column base);

                            }

                        /**
                         * Access to our underlying Column.
                         *
                         */
                        public Widgeon.Base.Catalog.Schema.Table.Column base();

                        }
                    }
                }
            }
        }
    }

