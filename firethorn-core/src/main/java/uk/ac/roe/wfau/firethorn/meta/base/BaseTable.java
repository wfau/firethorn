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
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

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
     * Name factory interface.
     *
     */
    public static interface NameFactory<TableType extends BaseTable<?,?>>
    extends Entity.NameFactory<TableType>
        {
        }

    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory<TableType extends BaseTable<?,?>>
    extends Entity.AliasFactory<TableType>
        {
        /**
         * Create a Table alias.
         *
         */
        @Override
		public String alias(final TableType table);
        }

    /**
     * Table resolver interface.
     *
     */
    public static interface Resolver
    extends Entity.EntityFactory<BaseTable<?,?>>
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
    extends Entity.EntityFactory<TableType>
        {
        /**
         * Select all the tables.
         *
         */
        public Iterable<TableType> select(final SchemaType parent);

        /**
         * Select a table by name.
         * @throws NotFoundException 
         *
         */
        public TableType select(final SchemaType parent, final String name)
        throws NotFoundException;

        /**
         * Text search for tables (name starts with).
         *
         */
        public Iterable<TableType> search(final SchemaType parent, final String text);

        /**
         * AliasFactory implementation.
         *
         */
        public AliasFactory<TableType> aliases();

        /**
         * NameFactory implementation.
         *
         */
        public NameFactory<TableType> names();

        }

    /**
     * The OGSA-DAI DQP table alias.
     *
     */
    public String alias();

    /**
     * The fully qualified name.
     *
     */
    public StringBuilder namebuilder();

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
        public ColumnType select(final String name)
        throws NotFoundException;

        /**
         * Select a column by ident.
         * 
         */
        public ColumnType select(final Identifier ident)
        throws NotFoundException;

        }

    /**
     * The table columns.
     *
     */
    public Columns<ColumnType> columns();

    /**
     * The AdqlTables linked to this table.
     *
     */
    @Deprecated
    interface Linked
        {
        public Iterable<AdqlTable> select();
        }

    /**
     * The AdqlTables linked to this table.
     *
     */
    @Deprecated
    public Linked linked();

    /**
     * The AdqlQuery that generated this table.
     *
     */
    public AdqlQuery query();

    /**
     * Base table metadata.
     *
     */
    public interface Metadata
        {
        }

    /**
     * Access to the table metadata.
     *
     */
    public AdqlTable.Metadata meta();

    }
