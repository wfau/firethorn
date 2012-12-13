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
package uk.ac.roe.wfau.firethorn.tuesday;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;

/**
 *
 *
 */
public interface TuesdayBaseTable<TableType extends TuesdayBaseTable<TableType, ColumnType>, ColumnType extends TuesdayBaseColumn<ColumnType>>
extends TuesdayBaseComponent
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TuesdayBaseTable<?,?>>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayBaseTable<?,?>>
        {
        }

    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory<TableType extends TuesdayBaseTable<?,?>>
        {
        /**
         * Create a Table alias.
         *
         */
        public String alias(final TableType table);
        }

    /**
     * Alias resolver interface.
     *
     */
    public static interface AliasResolver
        {
        /**
         * Resolve an alias into a table.
         *
         */
        public TuesdayBaseTable<?,?> select(final String alias)
        throws NotFoundException;

        }

    /**
     * Table factory interface.
     *
     */
    public static interface Factory<SchemaType extends TuesdayBaseSchema<SchemaType, TableType>, TableType extends TuesdayBaseTable<TableType,?>>
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

    public String alias();

    public StringBuilder fullname();

    public TuesdayOgsaTable<?, ?> ogsa();

    public TuesdayBaseSchema<?,TableType> schema();

    public TuesdayBaseResource<?> resource();

    /**
     * The table columns.
     *
     */
    public interface Columns<ColumnType extends TuesdayBaseColumn<ColumnType>>
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
        public Iterable<TuesdayAdqlTable> select();
        }

    /**
     * The tables linked to this table.
     *
     */
    public Linked linked();

    }
