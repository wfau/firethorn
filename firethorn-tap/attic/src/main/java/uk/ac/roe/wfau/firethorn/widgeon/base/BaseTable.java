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
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataTable;

/**
 * Public interface for a table.
 *
 */
public interface BaseTable<SchemaType extends BaseSchema<?>>
extends DataTable<SchemaType>
    {
    /**
     * Factory interface for creating and selecting tables.
     *
     */
    public static interface Factory<SchemaType extends BaseSchema<?>, TableType extends BaseTable<SchemaType>>
    extends DataTable.Factory<SchemaType, TableType>
        {
        /**
         * Access to our view factory.
         *
         */
        public AdqlTable.Factory views();

        }

    /**
     * Public interface for accessing a table's Views.
     *
     */
    public interface Views
        {
        /**
         * Select all the ADQL tables linked to this table.
         *
         */
        public Iterable<AdqlTable> select();

        /**
         * Search for an ADQL table based on parent schema.
         *
         */
        public AdqlTable search(final AdqlSchema parent);

        }

    /**
     * Access to this table's views.
     *
     */
    public BaseTable.Views views();

    /**
     * Public interface for accessing a table's columns.
     *
     */
    public interface Columns<ColumnType extends BaseColumn<?>>
    extends DataTable.Columns<ColumnType>
        {
        }

    /**
     * Access to this table's Columns.
     *
     */
    @Override
    public BaseTable.Columns<?> columns();

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
    public SchemaType schema();

    }