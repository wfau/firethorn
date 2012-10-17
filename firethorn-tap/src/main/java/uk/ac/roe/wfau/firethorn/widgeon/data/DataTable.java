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
package uk.ac.roe.wfau.firethorn.widgeon.data;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * Public interface for a table.
 *
 */
public interface DataTable<SchemaType extends DataSchema<?>>
extends DataStatus
    {

    /**
     * Factory interface for creating and selecting tables.
     *
     */
    public static interface Factory<SchemaType extends DataSchema<?>, TableType extends DataTable<?>>
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

        }

    /**
     * Access to our parent schema.
     *
     */
    public SchemaType parent();

    /**
     * Access to this table's columns.
     *
     */
    public DataTable.Columns<?> columns();

    /**
     * Public interface for accessing a table's columns.
     *
     */
    public interface Columns<ColumnType extends DataColumn<?>>
        {

        /**
         * Select all the columns from the table.
         *
         */
        public Iterable<ColumnType> select();

        /**
         * Select a named column from the table.
         *
         */
        public ColumnType select(final String name);

        /**
         * Text search for columns (name starts with).
         *
         */
        public Iterable<ColumnType> search(final String text);

        }
    }