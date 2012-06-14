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
 * Metadata structure describing a database resource.
 *
 */
public interface Widgeon
extends Entity
    {

    /**
     * Widgeon factory interface.
     *
     */
    public static interface Factory
    extends Entity.Factory<Widgeon>
        {
        /**
         * Create a new Widgeon.
         * 
         */
        public Widgeon create(String name, URI uri);

        /**
         * Create a new Widgeon.
         * 
         */
        public Widgeon create(String name, URL url);

        /**
         * Create a new Widgeon.
         * 
         */
        public Widgeon create(String name, DataSource src);

        /**
         * Access to our Schema factory.
         * 
         */
        public Widgeon.Schema.Factory schemas();

        }

    /**
     * Access to this Widgeon's Schema.
     *
     */
    public Schemas schemas();

    /**
     * Access to this Widgeon's Schema.
     *
     */
    public interface Schemas
        {
        /**
         * Create a new Schema.
         *
         */
        public Widgeon.Schema create(String name);

        /**
         * Select a Schema by name (unique within this Widgeon).
         *
         */
        public Widgeon.Schema select(String name);

        /**
         * Select all the Schema in this Widgeon.
         *
         */
        public Iterable<Widgeon.Schema> select();
        }

    public interface Schema
    extends Entity
        {
        public Widgeon parent();

        public interface Factory
        extends Entity.Factory<Schema>
            {
            /**
             * Create a new Schema.
             *
             */
            public Schema create(Widgeon parent, String name);

            /**
             * Select a Schema by name.
             *
             */
            public Schema select(Widgeon parent, String name);

            /**
             * Select all the Schema for a parent Widgeon.
             *
             */
            public Iterable<Schema> select(Widgeon parent);

            /**
             * Access to our Catalog factory.
             * 
             */
            public Schema.Catalog.Factory catalogs();

            }

        public Catalogs catalogs();
        public interface Catalogs
            {
            public Schema.Catalog create(String name);
            public Schema.Catalog select(String name);
            public Iterable<Schema.Catalog> select();
            }

        public interface Catalog
        extends Entity
            {
            public Schema parent();

            public interface Factory
            extends Entity.Factory<Catalog>
                {
                public Catalog create(Schema parent, String name);
                public Catalog select(Schema parent, String name);
                public Iterable<Catalog> select(Schema parent);

                /**
                 * Access to our Table factory.
                 * 
                 */
                public Catalog.Table.Factory tables();

                }

            public Tables tables();
            public interface Tables
                {
                public Catalog.Table create(String name);
                public Catalog.Table select(String name);
                public Iterable<Catalog.Table> select();
                }

            public interface Table
            extends Entity
                {
                public Catalog parent();

                public interface Factory
                extends Entity.Factory<Table>
                    {
                    public Table create(Catalog parent, String name);
                    public Table select(Catalog parent, String name);
                    public Iterable<Table> select(Catalog parent);

                    /**
                     * Access to our Column factory.
                     * 
                     */
                    public Table.Column.Factory columns();

                    }

                public Columns columns();
                public interface Columns
                    {
                    public Table.Column create(String name);
                    public Table.Column select(String name);
                    public Iterable<Table.Column> select();
                    }

                public interface Column
                extends Entity
                    {
                    public Table parent();

                    public interface Factory
                    extends Entity.Factory<Column>
                        {
                        public Column create(Table parent, String name);
                        public Column select(Table parent, String name);
                        public Iterable<Column> select(Table parent);
                        }
                    }
                }
            }
        }

    /**
     * A core resource built from what IS.
     *
    public interface Core
    extends Widgeon
        {
        public interface Views
            {
            public Widgeon.View create(String name);
            public Widgeon.View select(String name);
            public Iterable<Widgeon.View> select();
            }
        }
     */

    /**
     * A customised view of a resource.
     *
    public interface View
    extends Widgeon
        {
        public Widgeon parent();
        }
     */


    }

