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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn.AliasFactory;

/**
 *
 *
 */
public interface BaseTable<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
extends BaseComponent
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * {@link Entity.NameFactory} interface.
     *
     */
    public static interface NameFactory<TableType extends BaseTable<?,?>>
    extends Entity.NameFactory<TableType>
        {
        }

    /**
     * {@link Entity.AliasFactory} interface.
     *
     */
    public static interface AliasFactory<TableType extends BaseTable<?,?>>
    extends Entity.AliasFactory<TableType>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory<TableType extends BaseTable<?,?>>
    extends Entity.LinkFactory<TableType>
        {
        }

    /**
     * {@link Entity.EntityResolver} interface.
     *
     */
    public static interface EntityResolver<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType,?>>
    extends Entity.EntityFactory<TableType>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType,?>>
    extends Entity.EntityFactory<TableType>
        {
        /**
         * Select all the tables.
         *
         */
        public Iterable<TableType> select(final SchemaType parent);

        /**
         * Search for a table by name
         *
         */
        public TableType search(final SchemaType parent, final String name);

        /**
         * Select a table by name.
         *
         */
        public TableType select(final SchemaType parent, final String name)
        throws NameNotFoundException;

        /**
         * Our local {@link BaseTable.AliasFactory} implementation.
         * @todo Move to services.
         *
         */
        public AliasFactory<TableType> aliases();

        /**
         * Our local {@link BaseTable.NameFactory} implementation.
         * @todo Move to services.
         *
         */
        public NameFactory<TableType> names();

        }

    /**
     * The {@link BaseTable} this table is derived from.
     *
     */
    public BaseTable<?, ?> base();

    /**
     * The root of the chain that this table is derived from.
     *
     */
    public BaseTable<?, ?> root();

    /**
     * Our parent {@link BaseSchema schema}. 
     *
     */
    public BaseSchema<?,TableType> schema();

    /**
     * Our parent {@link BaseResource resource}. 
     *
     */
    public BaseResource<?> resource();

    /**
     * Our table {@link BaseColumn columns}.
     *
     */
    public interface Columns<ColumnType>
        {
        /**
         * Select all of the {@link BaseColumn columns} in this table.
         *
         */
        public Iterable<ColumnType> select();

        /**
         * Search for a {@link BaseColumn column} by name.
         *
         */
        public ColumnType search(final String name);

        /**
         * Select a {@link BaseColumn column} by name.
         *
         */
        public ColumnType select(final String name)
        throws NameNotFoundException;

        /**
         * Select a {@link BaseColumn column} by ident.
         *
         */
        public ColumnType select(final Identifier ident)
        throws IdentifierNotFoundException;

        }

    /**
     * Our table {@link BaseColumn columns}.
     *
     */
    public Columns<ColumnType> columns();

    /**
     * The {@link AdqlQuery} that generated this table.
     *
     */
    public AdqlQuery query();

    /**
     * The OGSA-DAI DQP table alias.
     *
     */
    public String alias();

    /**
     * The fully qualified table name.
     *
     */
    public StringBuilder namebuilder();

    /**
     * The {@link BaseTable} metadata.
     *
     */
    public interface Metadata
        {
        /**
         * The table name.
         * 
        public String name();
         */

        /**
         * The table description.
         * 
        public String text();
         */
        
        }

    /**
     * The {@link BaseTable} metadata.
     *
     */
    public AdqlTable.Metadata meta();

    }
