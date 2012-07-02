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
     * Access to this Widgeon's Sparrow.
     *
     */
    public Sparrows sparrows();

    /**
     * Public interface for accessing the Sparrow for a Widgeon.
     *
     */
    public interface Sparrows<SparrowType extends Widgeon.Sparrow>
        {

        /**
         * Select all the Sparrow from the Widgeon.
         *
         */
        public Iterable<SparrowType> select();

        /**
         * Select a named Sparrow from the Widgeon.
         *
         */
        public SparrowType select(String name)
        throws NameNotFoundException;

        /**
         * Search for a named Sparrow from the Widgeon.
         *
         */
        public SparrowType search(String name);

        }

    /**
     * Public interface for Sparrow metadata.
     *
     */
    public interface Sparrow<WidgeonType extends Widgeon>
    extends WidgeonComponent<WidgeonType>
        {

        /**
         * Factory interface for creating and selecting Sparrow.
         *
         */
        public static interface Factory<WidgeonType extends Widgeon, SparrowType extends Widgeon.Sparrow>
        extends Entity.Factory<SparrowType>
            {

            /**
             * Select all the Sparrow from a Widgeon.
             *
             */
            public Iterable<SparrowType> select(WidgeonType parent);

            /**
             * Select a named Sparrow from a Widgeon.
             *
             */
            public SparrowType select(WidgeonType parent, String name)
            throws NameNotFoundException;

            /**
             * Search for a named Sparrow from a Widgeon.
             *
             */
            public SparrowType search(WidgeonType parent, String name);

            }

        /**
         * Access to this Sparrow's Warblers.
         *
         */
        public Warblers warblers();

        /**
         * Public interface for accessing the Warblers for a Sparrow.
         *
         */
        public interface Warblers<WarblerType extends Widgeon.Sparrow.Warbler>
            {

            /**
             * Select all the Warblers from the Sparrow.
             *
             */
            public Iterable<WarblerType> select();

            /**
             * Select a named Warbler from the Sparrow.
             *
             */
            public WarblerType select(String name)
            throws NameNotFoundException;

            /**
             * Search for a named Warbler from the Sparrow.
             *
             */
            public WarblerType search(String name);

            }

        /**
         * Public interface for Warbler metadata.
         *
         */
        public interface Warbler<SparrowType extends Widgeon.Sparrow>
        extends WidgeonComponent<SparrowType>
            {

            /**
             * Factory interface for creating and selecting Warblers.
             *
             */
            public static interface Factory<SparrowType extends Widgeon.Sparrow, WarblerType extends Widgeon.Sparrow.Warbler>
            extends Entity.Factory<WarblerType>
                {

                /**
                 * Select all the Warblers from a Sparrow.
                 *
                 */
                public Iterable<WarblerType> select(SparrowType parent);

                /**
                 * Select a named Warbler from a Sparrow.
                 *
                 */
                public WarblerType select(SparrowType parent, String name)
                throws NameNotFoundException;

                /**
                 * Search for named Warbler from a Sparrow.
                 *
                 */
                public WarblerType search(SparrowType parent, String name);

                }

            /**
             * Access to this Warbler's Tables.
             *
             */
            public Tables tables();

            /**
             * Public interface for accessing the Tables for a Warbler.
             *
             */
            public interface Tables<TableType extends Widgeon.Sparrow.Warbler.Table>
                {

                /**
                 * Select all the Tables from the Warbler.
                 *
                 */
                public Iterable<TableType> select();

                /**
                 * Select a named Table from the Warbler.
                 *
                 */
                public TableType select(String name)
                throws NameNotFoundException;

                /**
                 * Search for a named Table from the Warbler.
                 *
                 */
                public TableType search(String name);

                }

            /**
             * Public interface for Table metadata.
             *
             */
            public interface Table<WarblerType extends Widgeon.Sparrow.Warbler>
            extends WidgeonComponent<WarblerType>
                {

                /**
                 * Factory interface for creating and selecting Tables.
                 *
                 */
                public static interface Factory<WarblerType extends Widgeon.Sparrow.Warbler, TableType extends Widgeon.Sparrow.Warbler.Table>
                extends Entity.Factory<TableType>
                    {

                    /**
                     * Select all the Tables from a Warbler.
                     *
                     */
                    public Iterable<TableType> select(WarblerType parent);

                    /**
                     * Select a named Table from a Warbler.
                     *
                     */
                    public TableType select(WarblerType parent, String name)
                    throws NameNotFoundException;

                    /**
                     * Search for a named Table from a Warbler.
                     *
                     */
                    public TableType search(WarblerType parent, String name);

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
                public interface Columns<ColumnType extends Widgeon.Sparrow.Warbler.Table.Column>
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
                public interface Column<TableType extends Widgeon.Sparrow.Warbler.Table>
                extends WidgeonComponent<TableType>
                    {

                    /**
                     * Factory interface for creating and selecting Columns.
                     *
                     */
                    public static interface Factory<TableType extends Widgeon.Sparrow.Warbler.Table, ColumnType extends Widgeon.Sparrow.Warbler.Table.Column>
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
             * Access to our Sparrow factory.
             * 
             */
            public Widgeon.Base.Sparrow.Factory sparrows();

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
         * Public interface for accessing a Widgeon's Sparrows.
         *
         */
        public interface Sparrows
        extends Widgeon.Sparrows<Widgeon.Base.Sparrow>
            {

            /**
             * Create a new Sparrow for the Widgeon.
             *
             */
            public Widgeon.Base.Sparrow create(String name);

            }

        /**
         * Access to the Sparrows for this Widgeon.
         *
         */
        public Sparrows sparrows();

        /**
         * Public interface for Sparrow metadata.
         *
         */
        public interface Sparrow
        extends Widgeon.Sparrow<Widgeon.Base>
            {

            /**
             * Factory interface for creating and selecting Sparrow.
             *
             */
            public static interface Factory
            extends Widgeon.Sparrow.Factory<Widgeon.Base, Widgeon.Base.Sparrow>
                {

                /**
                 * Create a new Sparrow for a Widgeon.
                 *
                 */
                public Widgeon.Base.Sparrow create(Widgeon.Base parent, String name);

                /**
                 * Access to our View factory.
                 * 
                 */
                public Widgeon.View.Sparrow.Factory views();

                /**
                 * Access to our Warbler factory.
                 * 
                 */
                public Widgeon.Base.Sparrow.Warbler.Factory warblers();

                }

            /**
             * Public interface for accessing the Views of a Sparrow.
             *
             */
            public interface Views
                {

                /*
                 * Select all the Views of the Sparrow.
                 *
                 */
                public Iterable<Widgeon.View.Sparrow> select();

                }

            /**
             * Access to the Views of this Sparrow.
             *
             */
            public Widgeon.Base.Sparrow.Views views();

            /**
             * Public interface for accessing the Warblers of a Sparrow.
             *
             */
            public interface Warblers
            extends Widgeon.Sparrow.Warblers<Widgeon.Base.Sparrow.Warbler>
                {

                /**
                 * Create a new Warbler for the Sparrow.
                 *
                 */
                public Widgeon.Base.Sparrow.Warbler create(String name);

                }

            /**
             * Access to the Warblers for this Sparrow.
             *
             */
            public Warblers warblers();

            /**
             * Public interface for Warbler metadata.
             *
             */
            public interface Warbler
            extends Widgeon.Sparrow.Warbler
                {

                /**
                 * Factory interface for creating and selecting Warblers.
                 *
                 */
                public static interface Factory
                extends Widgeon.Sparrow.Warbler.Factory<Widgeon.Base.Sparrow, Widgeon.Base.Sparrow.Warbler>
                    {

                    /**
                     * Create a new Warbler for a Sparrow.
                     *
                     */
                    public Widgeon.Base.Sparrow.Warbler create(Widgeon.Base.Sparrow parent, String name);

                    /**
                     * Access to our View factory.
                     * 
                     */
                    public Widgeon.View.Sparrow.Warbler.Factory views();

                    /**
                     * Access to our Table factory.
                     * 
                     */
                    public Widgeon.Base.Sparrow.Warbler.Table.Factory tables();

                    }

                /**
                 * Public interface for accessing the Views of a Warbler.
                 *
                 */
                public interface Views
                    {

                    /*
                     * Select all the Views of the Warbler.
                     *
                     */
                    public Iterable<Widgeon.View.Sparrow.Warbler> select();

                    }

                /**
                 * Access to the Views of this Warbler.
                 *
                 */
                public Widgeon.Base.Sparrow.Warbler.Views views();

                /**
                 * Public interface for accessing the Tables of a Warbler.
                 *
                 */
                public interface Tables
                extends Widgeon.Sparrow.Warbler.Tables<Widgeon.Base.Sparrow.Warbler.Table>
                    {

                    /**
                     * Create a new Table for the Warbler.
                     *
                     */
                    public Widgeon.Base.Sparrow.Warbler.Table create(String name);

                    }

                /**
                 * Access to the Tables for this Warbler.
                 *
                 */
                public Tables tables();

                /**
                 * Public interface for Table metadata.
                 *
                 */
                public interface Table
                extends Widgeon.Sparrow.Warbler.Table
                    {

                    /**
                     * Factory interface for creating and selecting Tables.
                     *
                     */
                    public static interface Factory
                    extends Widgeon.Sparrow.Warbler.Table.Factory<Widgeon.Base.Sparrow.Warbler, Widgeon.Base.Sparrow.Warbler.Table>
                        {

                        /**
                         * Create a new Table for a Warbler.
                         *
                         */
                        public Widgeon.Base.Sparrow.Warbler.Table create(Widgeon.Base.Sparrow.Warbler parent, String name);

                        /**
                         * Access to our View factory.
                         * 
                         */
                        public Widgeon.View.Sparrow.Warbler.Table.Factory views();

                        /**
                         * Access to our Column factory.
                         * 
                         */
                        public Widgeon.Base.Sparrow.Warbler.Table.Column.Factory columns();

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
                        public Iterable<Widgeon.View.Sparrow.Warbler.Table> select();

                        }

                    /**
                     * Access to the Views of this Table.
                     *
                     */
                    public Widgeon.Base.Sparrow.Warbler.Table.Views views();

                    /**
                     * Public interface for accessing the Columns of a Table.
                     *
                     */
                    public interface Columns
                    extends Widgeon.Sparrow.Warbler.Table.Columns<Widgeon.Base.Sparrow.Warbler.Table.Column>
                        {

                        /**
                         * Create a new Column for the Table.
                         *
                         */
                        public Widgeon.Base.Sparrow.Warbler.Table.Column create(String name);

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
                    extends Widgeon.Sparrow.Warbler.Table.Column
                        {

                        /**
                         * Factory interface for creating and selecting Columns.
                         *
                         */
                        public static interface Factory
                        extends Widgeon.Sparrow.Warbler.Table.Column.Factory<Widgeon.Base.Sparrow.Warbler.Table, Widgeon.Base.Sparrow.Warbler.Table.Column>
                            {

                            /**
                             * Create a new Column for a Table.
                             *
                             */
                            public Widgeon.Base.Sparrow.Warbler.Table.Column create(Widgeon.Base.Sparrow.Warbler.Table parent, String name);

                            /**
                             * Access to our View factory.
                             * 
                             */
                            public Widgeon.View.Sparrow.Warbler.Table.Column.Factory views();

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
                            public Iterable<Widgeon.View.Sparrow.Warbler.Table.Column> select();

                            }

                        /**
                         * Access to the Views of this Column.
                         *
                         */
                        public Widgeon.Base.Sparrow.Warbler.Table.Column.Views views();

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
             * Access to our Sparrow factory.
             * 
             */
            public Widgeon.View.Sparrow.Factory sparrows();

            }

        /**
         * Access to our underlying Widgeon.
         *
         */
        public Widgeon.Base base();

        /**
         * Public interface for accessing the Sparrow of a Widgeon.
         *
         */
        public interface Sparrows
        extends Widgeon.Sparrows<Widgeon.View.Sparrow>
            {

            /**
             * Find an existing View of a Sparrow, or create a new one.
             *
            public Widgeon.View.Sparrow cascade(Widgeon.Base.Sparrow base);
             */

            }

        /**
         * Access to the Sparrows for this Widgeon.
         *
         */
        public Sparrows sparrows();

        /**
         * Public interface for a Sparrow View.
         *
         */
        public interface Sparrow
        extends Widgeon.Sparrow<Widgeon.View>
            {

            /**
             * Factory interface for creating and selecting Sparrow Views.
             *
             */
            public static interface Factory
            extends Widgeon.Sparrow.Factory<Widgeon.View, Widgeon.View.Sparrow>
                {

                /**
                 * Find an existing View of a Sparrow, or create a new one.
                 *
                 */
                public Widgeon.View.Sparrow cascade(Widgeon.View parent, Widgeon.Base.Sparrow base);

                /**
                 * Create a new View of a Sparrow.
                 *
                 */
                public Widgeon.View.Sparrow create(Widgeon.View parent, Widgeon.Base.Sparrow base, String name);

                /**
                 * Select all the views of a Sparrow.
                 *
                 */
                public Iterable<Widgeon.View.Sparrow> select(Widgeon.Base.Sparrow base);

                /**
                 * Access to our Warbler factory.
                 * 
                 */
                public Widgeon.View.Sparrow.Warbler.Factory warblers();

                }

            /**
             * Access to our underlying Sparrow.
             *
             */
            public Widgeon.Base.Sparrow base();

            /**
             * Public interface for accessing the Warblers of a Sparrow.
             *
             */
            public interface Warblers
            extends Widgeon.Sparrow.Warblers<Widgeon.View.Sparrow.Warbler>
                {
                }

            /**
             * Access to the Warblerss for this Sparrow.
             *
             */
            public Warblers warblers();

            /**
             * Public interface for Warbler metadata.
             *
             */
            public interface Warbler
            extends Widgeon.Sparrow.Warbler<Widgeon.View.Sparrow>
                {

                /**
                 * Factory interface for creating and selecting Warblers.
                 *
                 */
                public static interface Factory
                extends Widgeon.Sparrow.Warbler.Factory<Widgeon.View.Sparrow, Widgeon.View.Sparrow.Warbler>
                    {

                    /**
                     * Find an existing View of a Warbler, or create a new one.
                     *
                     */
                    public Widgeon.View.Sparrow.Warbler cascade(Widgeon.View.Sparrow parent, Widgeon.Base.Sparrow.Warbler base);

                    /**
                     * Create a new View of a Warbler.
                     *
                     */
                    public Widgeon.View.Sparrow.Warbler create(Widgeon.View.Sparrow parent, Widgeon.Base.Sparrow.Warbler base, String name);

                    /**
                     * Select all the views of a Warbler.
                     *
                     */
                    public Iterable<Widgeon.View.Sparrow.Warbler> select(Widgeon.Base.Sparrow.Warbler base);

                    /**
                     * Access to our Table factory.
                     * 
                     */
                    public Widgeon.View.Sparrow.Warbler.Table.Factory tables();
                    
                    }

                /**
                 * Access to our underlying Warbler.
                 *
                 */
                public Widgeon.Base.Sparrow.Warbler base();

                /**
                 * Public interface for accessing the Tables of a Warbler.
                 *
                 */
                public interface Tables
                extends Widgeon.Sparrow.Warbler.Tables<Widgeon.View.Sparrow.Warbler.Table>
                    {
                    }

                /**
                 * Access to the Tables for this Warbler.
                 *
                 */
                public Tables tables();

                /**
                 * Public interface for Table metadata.
                 *
                 */
                public interface Table
                extends Widgeon.Sparrow.Warbler.Table<Widgeon.View.Sparrow.Warbler>
                    {

                    /**
                     * Factory interface for creating and selecting Tables.
                     *
                     */
                    public static interface Factory
                    extends Widgeon.Sparrow.Warbler.Table.Factory<Widgeon.View.Sparrow.Warbler, Widgeon.View.Sparrow.Warbler.Table>
                        {

                        /**
                         * Find an existing View of a Table, or create a new one.
                         *
                         */
                        public Widgeon.View.Sparrow.Warbler.Table cascade(Widgeon.View.Sparrow.Warbler parent, Widgeon.Base.Sparrow.Warbler.Table base);

                        /**
                         * Create a new View of a Table.
                         *
                         */
                        public Widgeon.View.Sparrow.Warbler.Table create(Widgeon.View.Sparrow.Warbler parent, Widgeon.Base.Sparrow.Warbler.Table base, String name);

                        /**
                         * Select all the views of a Table.
                         *
                         */
                        public Iterable<Widgeon.View.Sparrow.Warbler.Table> select(Widgeon.Base.Sparrow.Warbler.Table base);

                        /**
                         * Access to our Column factory.
                         * 
                         */
                        public Widgeon.View.Sparrow.Warbler.Table.Column.Factory columns();

                        }

                    /**
                     * Access to our underlying Table.
                     *
                     */
                    public Widgeon.Base.Sparrow.Warbler.Table base();

                    /**
                     * Public interface for accessing the Columns of a Table.
                     *
                     */
                    public interface Columns
                    extends Widgeon.Sparrow.Warbler.Table.Columns<Widgeon.View.Sparrow.Warbler.Table.Column>
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
                    extends Widgeon.Sparrow.Warbler.Table.Column<Widgeon.View.Sparrow.Warbler.Table>
                        {

                        /**
                         * Factory interface for creating and selecting Columns.
                         *
                         */
                        public static interface Factory
                        extends Widgeon.Sparrow.Warbler.Table.Column.Factory<Widgeon.View.Sparrow.Warbler.Table, Widgeon.View.Sparrow.Warbler.Table.Column>
                            {

                            /**
                             * Find an existing View of a Column, or create a new one.
                             *
                             */
                            public Widgeon.View.Sparrow.Warbler.Table.Column cascade(Widgeon.View.Sparrow.Warbler.Table parent, Widgeon.Base.Sparrow.Warbler.Table.Column base);

                            /**
                             * Create a new View of a Column.
                             *
                             */
                            public Widgeon.View.Sparrow.Warbler.Table.Column create(Widgeon.View.Sparrow.Warbler.Table parent, Widgeon.Base.Sparrow.Warbler.Table.Column base, String name);

                            /**
                             * Select all the views of a Column.
                             *
                             */
                            public Iterable<Widgeon.View.Sparrow.Warbler.Table.Column> select(Widgeon.Base.Sparrow.Warbler.Table.Column base);

                            }

                        /**
                         * Access to our underlying Column.
                         *
                         */
                        public Widgeon.Base.Sparrow.Warbler.Table.Column base();

                        }
                    }
                }
            }
        }
    }

