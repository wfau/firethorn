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
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * Public interface for a column.
 *
 */
public interface BaseColumn<ColumnType extends BaseColumn<ColumnType>>
extends TreeComponent
    {

    /**
     * The special value for size to indicate a non-array field, {@value}.
     *
     */
    public static final Integer NON_ARRAY_SIZE = new Integer(0);

    /**
     * The special value for size to indicate a variable size array, {@value}.
     *
     */
    public static final Integer VAR_ARRAY_SIZE = new Integer(-1);

    /**
     * {@link Entity.AliasFactory} interface.
     *
     */
    public static interface AliasFactory<ColumnType extends BaseColumn<ColumnType>>
    extends Entity.AliasFactory<ColumnType>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory<ColumnType extends BaseColumn<ColumnType>>
    extends Entity.LinkFactory<ColumnType>
        {
        }

    /**
     * {@link Entity.EntityResolver} interface.
     *
     */
    public static interface EntityResolver<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
    extends Entity.EntityFactory<ColumnType>
        {
        }
    
    /**
     * {@link TreeComponent.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
    extends TreeComponent.EntityFactory<ColumnType>
        {
        /**
         * Select all the columns from a table.
         * @throws ProtectionException 
         *
         */
        public Iterable<ColumnType> select(final TableType parent)
        throws ProtectionException;

        /**
         * Select a column by name.
         * @throws ProtectionException 
         *
         */
        public ColumnType select(final TableType parent, final String name)
        throws NameNotFoundException, ProtectionException;

        /**
         * Search for a column by name.
         * @throws ProtectionException 
         *
         */
        public ColumnType search(final TableType parent, final String name)
        throws ProtectionException;

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices<ColumnType extends BaseColumn<ColumnType>>
    extends NamedEntity.EntityServices<ColumnType>
        {
        /**
         * {@link BaseColumn.AliasFactory} instance.
         *
         */
        public BaseColumn.AliasFactory<ColumnType> aliases();

        }
    
    /**
     * The {@link BaseColumn} this column is derived from.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseColumn<?> base()
    throws ProtectionException;

    /**
     * The root of the chain this column is derived from.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseColumn<?> root()
    throws ProtectionException;

    /**
     * Our parent {@link BaseTable table}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseTable<?,?> table()
    throws ProtectionException;

    /**
     * Our parent {@link BaseSchema schema}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseSchema<?,?> schema()
    throws ProtectionException;

    /**
     * Our parent {@link BaseResource resource}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseResource<?> resource()
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
     * The {@link BaseColumn} metadata interface.
     *
     */
    public interface Metadata
        {
        /**
         * The column name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        @Deprecated
        public String name()
        throws ProtectionException;

        }

    /**
     * The {@link AdqlColumn} metadata.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public AdqlColumn.Modifier meta()
    throws ProtectionException;

    /**
     * Update the {@link AdqlColumn} properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final AdqlColumn.Metadata.Adql meta)
    throws ProtectionException;
    
    }
