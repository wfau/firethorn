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
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;

/**
 * Public interface for a table column.
 *
 */
public interface BaseColumn<ColumnType extends BaseColumn<ColumnType>>
extends BaseComponent
    {
    /**
     * The size for a non-array field.
     *
     */
    public static final Integer NON_ARRAY_SIZE = new Integer(0);

    /**
     * The size for a variable size array field.
     *
     */
    public static final Integer VAR_ARRAY_SIZE = new Integer(-1);

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
    public static interface AliasFactory<ColumnType extends BaseColumn<ColumnType>>
    extends Entity.AliasFactory<ColumnType>
        {
        }

    /**
     * Entity factory interface.
     *
     */
    public static interface EntityFactory<TableType extends BaseTable<TableType, ColumnType>, ColumnType extends BaseColumn<ColumnType>>
    extends Entity.EntityFactory<ColumnType>
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
         * AliasFactory implementation.
         *
         */
        public AliasFactory<ColumnType> aliases();

        }

    /**
     * The base column this is derived from.
     *
     */
    public BaseColumn<?>   base();

    /**
     * The root column this is derived from.
     *
     */
    public BaseColumn<?>   root();

    /**
     * The parent table.
     *
     */
    public BaseTable<?,?>  table();

    /**
     * The parent schema.
     *
     */
    public BaseSchema<?,?> schema();

    /**
     * The parent resource.
     *
     */
    public BaseResource<?> resource();

    /**
     * The unique column alias, based on the identifier.
     *
     */
    public String alias();

    /**
     * The full name, including parent table, schema and catalog.
     *
     */
    public StringBuilder namebuilder();

    /**
     * A class to represent a column UCD.
     * 
     */
    public interface UCD
        {
        /**
         * The UCD version.
         * 
         */
        public enum Type
            {
            ONE(),
            ONEPLUS();
            }

        /**
         * The UCD type.
         * 
         */
        public Type type();

        /**
         * The UCD value.
         * 
         */
        public String value();

        }

    /**
     * The column metadata.
     *
     */
    public interface Metadata
        {

        /**
         * The ADQL metadata.
         *
         */
        public interface AdqlMetadata
            {

            /**
             * The array size, or null if this is not an array.
             *
             */
            public Integer arraysize();

            /**
             * Set the array size.
             *
             */
            public void arraysize(final Integer size);

            /**
             * The ADQL type.
             *
             */
            public AdqlColumn.Type type();

            /**
             * Set the ADQL type.
             *
             */
            public void type(final AdqlColumn.Type type);

            /**
             * The ADQL units.
             *
             */
            public String units();
            /**
             * Set the ADQL units.
             *
             */
            public void units(final String unit);

            /**
             * The ADQL utype.
             *
             */
            public String utype();

            /**
             * Set the ADQL utype.
             *
             */
            public void utype(final String utype);
            
            /**
             * The column UCD.
             *
             */
            public UCD ucd();

            /**
             * Set the column UCD.
             *
             */
            public void ucd(final UCD.Type type, final String value);

            }

        /**
         * The ADQL metadata.
         *
         */
        public AdqlMetadata adql();

        }

    /**
     * Access to the column metadata.
     *
     */
    public Metadata meta();

    }
