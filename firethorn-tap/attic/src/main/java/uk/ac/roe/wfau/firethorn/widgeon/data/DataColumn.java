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

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 * Public interface for a column.
 *
 */
public interface DataColumn<TableType extends DataTable<?>>
extends DataComponent
    {

    /**
     * Factory interface for creating and selecting columns.
     *
     */
    public static interface Factory<TableType extends DataTable<?>, ColumnType extends DataColumn<?>>
    extends Entity.Factory<ColumnType>
        {

        /**
         * Select all the columns from a table.
         *
         */
        public Iterable<ColumnType> select(final TableType parent);

        /**
         * Select a named column from a table.
         *
         */
        public ColumnType select(final TableType parent, final String name);

        /**
         * Text search for columns (name starts with).
         *
         */
        public Iterable<ColumnType> search(final TableType parent, final String text);

        }

    /**
     * Access to our parent table.
     *
     */
    public TableType parent();

    }