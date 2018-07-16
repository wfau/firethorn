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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *  
         */
        public BaseTable<?,?> resolve(String link)
        throws ProtectionException, IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException;
        }

    /**
     * {@link TreeComponent.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType,?>>
    extends TreeComponent.EntityFactory<TableType>
        {
        /**
         * Select all the tables for a schema.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<TableType> select(final SchemaType parent)
        throws ProtectionException;

        /**
         * Search for a schema table by name
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public TableType search(final SchemaType parent, final String name)
        throws ProtectionException;

        /**
         * Select a schema table by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public TableType select(final SchemaType parent, final String name)
        throws ProtectionException, NameNotFoundException;

        /**
         * Select a schema table by identifier.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public TableType select(final SchemaType parent, final Identifier ident)
        throws ProtectionException, IdentifierNotFoundException;
        
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public BaseTable.AliasFactory<TableType> aliases()
        throws ProtectionException;

        }
    
    /**
     * The next {@link BaseTable} this table is derived from.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseTable<?, ?> base()
    throws ProtectionException;

    /**
     * The root {@link BaseTable} this table is derived from.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseTable<?, ?> root()
    throws ProtectionException;

    /**
     * Our parent {@link BaseSchema schema}. 
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseSchema<?,TableType> schema()
    throws ProtectionException;

    /**
     * Our parent {@link BaseResource resource}. 
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseResource<?> resource()
    throws ProtectionException;

    /**
     * Our table {@link BaseColumn columns}.
     *
     */
    public interface Columns<ColumnType>
        {
        /**
         * Select all the columns for this table.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<ColumnType> select()
        throws ProtectionException;

        /**
         * Search a column by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public ColumnType search(final String name)
        throws ProtectionException;

        /**
         * Select a column by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public ColumnType select(final String name)
        throws ProtectionException, NameNotFoundException;

        /**
         * Select a column by {@link Identifier}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public ColumnType select(final Identifier ident)
        throws ProtectionException, IdentifierNotFoundException;

        }

    /**
     * Our table {@link BaseColumn columns}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public Columns<ColumnType> columns()
    throws ProtectionException;

    /**
     * The {@link BlueQuery} that generated this table.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BlueQuery bluequery()
    throws ProtectionException;

    /**
     * The unique name.
     * TODO Rename this to uniquename ?
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public String alias()
    throws ProtectionException;

    /**
     * The {@link BaseTable} metadata.
     *
     */
    public interface Metadata
        {
        /**
         * The table name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * ** Fix and test IvoaTable constructor before we remove this ..
         * 
         */
        @Deprecated
        public String name()
        throws ProtectionException;

        }

    /**
     * The {@link BaseTable} metadata.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public AdqlTable.Metadata meta()
    throws ProtectionException;

    /**
     * Empty count value, {@value}.
     *
     */
    public static final Long EMPTY_COUNT_VALUE = new Long(-1L);
    
    /**
     * The {@link BaseTable} rowcount.
     * @return The number of rows in the table, or {@link EMPTY_COUNT_VALUE} if the table is empty.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public Long rowcount()
    throws ProtectionException;

    }
