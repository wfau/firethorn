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
import uk.ac.roe.wfau.firethorn.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;

/**
 * Public interface for a table.
 *
 */
public interface BaseTable<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
extends TreeComponent
    {

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
     * A resolver to resolve links. 
     * @todo Change the name to LinkResolver, unless this extends a base class resolver.
     *
     */
    public static interface EntityResolver
        {
        /**
         * Resolve a link into a {@link BaseTable}.
         * @throws IdentifierFormatException
         * @throws IdentifierNotFoundException 
         * @throws EntityNotFoundException 
         *  
         */
        public BaseTable<?,?> resolve(String link)
        throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException;
        }

    /**
     * {@link TreeComponent.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType,?>>
    extends TreeComponent.EntityFactory<TableType>
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

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices<TableType extends BaseTable<?,?>>
    extends NamedEntity.EntityServices<TableType>
        {
        /**
         * {@link BaseColumn.AliasFactory} instance.
         *
         */
        public BaseTable.AliasFactory<TableType> aliases();
        }
    
    /**
     * The next {@link BaseTable} this table is derived from.
     *
     */
    public BaseTable<?, ?> base();

    /**
     * The root {@link BaseTable} this table is derived from.
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
     * The {@link BlueQuery} that generated this table.
     *
     */
    public BlueQuery bluequery();

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
     * The fully qualified table name.
     *
     */
    public String fullname();

    /**
     * The {@link BaseTable} metadata.
     *
     */
    public interface Metadata
        {
        /**
         * The table name.
         * 
         */
        public String name();

        }

    /**
     * The {@link BaseTable} metadata.
     *
     */
    public AdqlTable.Metadata meta();

    }
