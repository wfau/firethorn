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
package uk.ac.roe.wfau.firethorn.meta.base;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaTable;

/**
 *
 *
 */
public interface BaseTable<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
extends BaseComponent
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<BaseTable<?,?>>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory<TableType extends BaseTable<?,?>>
        {
        /**
         * Create a Table alias.
         *
         */
        public String alias(final TableType table);
        }

    /**
     * Table resolver interface.
     *
     */
    public static interface Resolver
    extends Entity.Factory<BaseTable<?,?>>
        {
        /**
         * Resolve an alias into a table.
         *
         */
        public BaseTable<?,?> resolve(final String alias)
        throws NotFoundException;

        }

    /**
     * Table factory interface.
     *
     */
    public static interface Factory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType,?>>
    extends Entity.Factory<TableType>
        {
        /**
         * Select all the tables from a schema.
         *
         */
        public Iterable<TableType> select(final SchemaType parent);

        /**
         * Select a named table from a schema.
         *
         */
        public TableType select(final SchemaType parent, final String name);

        /**
         * Text search for tables (name starts with).
         *
         */
        public Iterable<TableType> search(final SchemaType parent, final String text);

        /**
         * Our local alias factory.
         *
         */
        public AliasFactory<TableType> aliases();

        }

    public String type();
    public void type(final String type);

    public Integer size();
    public void size(final Integer size);

    public String ucd();
    public void ucd(final String ucd);

    /**
     * The OGSA-DAI DQP table alias.
     *
     */
    public String alias();

    /**
     * The fully qualified name.
     *
     */
    public StringBuilder fullname();

    public BaseTable<?, ?> base();
    public BaseTable<?, ?> root();

    public BaseSchema<?,TableType> schema();

    public BaseResource<?> resource();

    /**
     * The table columns.
     *
     */
    public interface Columns<ColumnType>
        {
        /**
         * Select all of the columns in this table.
         *
         */
        public Iterable<ColumnType> select();
        /**
         * Search for columns (name starts with).
         *
         */
        public Iterable<ColumnType> search(final String text);
        /**
         * Select a specific column by name.
         *
         */
        public ColumnType select(final String name);
        }

    /**
     * The table columns.
     *
     */
    public Columns<ColumnType> columns();

    /**
     * The tables linked to this table.
     *
     */
    interface Linked
        {
        public Iterable<AdqlTable> select();
        }

    /**
     * The tables linked to this table.
     *
     */
    public Linked linked();

    }
