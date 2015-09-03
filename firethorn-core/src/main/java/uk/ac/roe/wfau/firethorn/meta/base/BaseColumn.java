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
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
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
         *
         */
        public Iterable<ColumnType> select(final TableType parent);

        /**
         * Select a column by name.
         *
         */
        public ColumnType select(final TableType parent, final String name)
        throws NameNotFoundException;

        /**
         * Search for a column by name.
         *
         */
        public ColumnType search(final TableType parent, final String name);

        /**
         * Our local {@link BaseColumn.AliasFactory} implementation.
         * @todo Move to services.
         *
         */
        public AliasFactory<ColumnType> aliases();

        }

    /**
     * The {@link BaseColumn} this column is derived from.
     *
     */
    public BaseColumn<?>   base();

    /**
     * The root of the chain this column is derived from.
     *
     */
    public BaseColumn<?>   root();

    /**
     * Our parent {@link BaseTable table}.
     *
     */
    public BaseTable<?,?>  table();

    /**
     * Our parent {@link BaseSchema schema}.
     *
     */
    public BaseSchema<?,?> schema();

    /**
     * Our parent {@link BaseResource resource}.
     *
     */
    public BaseResource<?> resource();

    /**
     * The unique column alias, based on the identifier.
     *
     */
    public String alias();

    /**
     * The full name, including parent table and schema.
     *
     */
    public StringBuilder namebuilder();

    /**
     * The full name, including parent table and schema.
     *
     */
    public String fullname();

    /**
     * The {@link BaseColumn} metadata interface.
     *
     */
    public interface Metadata
        {
        /**
         * The column name.
         * 
         */
        @Deprecated
        public String name();

        }

    /**
     * The {@link AdqlColumn} metadata.
     *
     */
    public AdqlColumn.Modifier meta();

    /**
     * Update the {@link AdqlColumn} properties.
     * 
     */
    public void update(final AdqlColumn.Metadata.Adql meta);
    
    }
