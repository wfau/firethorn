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
package uk.ac.roe.wfau.firethorn.widgeon.base;

import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataColumn;

/**
 * Public interface for a column.
 *
 */
public interface BaseColumn<TableType extends BaseTable<?>>
extends DataColumn<TableType>
    {
    /**
     * Factory interface for creating and selecting columns.
     *
     */
    public static interface Factory<TableType extends BaseTable<?>, ColumnType extends BaseColumn<TableType>>
    extends DataColumn.Factory<TableType, ColumnType>
        {
        /**
         * Access to our view factory.
         *
         */
        public AdqlColumn.Factory views();

        }

    /**
     * Public interface for accessing a column's views.
     *
     */
    public interface Views
        {
        /**
         * Select all the views of the column.
         *
         */
        public Iterable<AdqlColumn> select();

        /**
         * Search for column view based on parent resource.
         *
         */
        public AdqlColumn search(final AdqlResource parent);

        /**
         * Search for column view based on parent catalog.
         *
         */
        public AdqlColumn search(final AdqlCatalog parent);

        /**
         * Search for column view based on parent schema.
         *
         */
        public AdqlColumn search(final AdqlSchema parent);

        /**
         * Search for column view based on parent table.
         *
         */
        public AdqlColumn search(final AdqlTable parent);

        }

    /**
     * Access to this column's views.
     *
     */
    public BaseColumn.Views views();

    /**
     * Access to our parent resource.
     *
     */
    public BaseResource resource();

    /**
     * Access to our parent catalog.
     *
     */
    public BaseCatalog<?> catalog();

    /**
     * Access to our parent schema.
     *
     */
    public BaseSchema<?> schema();

    /**
     * Access to our parent table.
     *
     */
    public TableType table();

    }