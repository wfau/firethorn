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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTable;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataTable;

/**
 * Public interface for an ADQL table.
 *
 */
public interface AdqlTable
extends DataTable<AdqlSchema>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlTable>
        {
        }

    /**
     * Factory interface for creating and selecting tables.
     *
     */
    public static interface Factory
    extends DataTable.Factory<AdqlSchema, AdqlTable>
        {

        /**
         * Find an existing ADQL table, or create a new one.
         *
         */
        @Deprecated
        public AdqlTable cascade(final AdqlSchema parent, final BaseTable<?> base);

        /**
         * Create a new ADQL table.
         *
         */
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?> base);

        /**
         * Create a new ADQL table.
         *
         */
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?> base, final String name);

        /**
         * Select all the ADQL tables linked to a base table.
         *
         */
        public Iterable<AdqlTable> select(final BaseTable<?> base);

        /**
         * Select an ADQL table based on parent resource.
         *
         */
        @Deprecated
        public AdqlTable select(final AdqlResource parent, final BaseTable<?> base);

        /**
         * Select an ADQL table based on parent catalog.
         *
         */
        @Deprecated
        public AdqlTable select(final AdqlCatalog parent, final BaseTable<?> base);

        /**
         * Select an ADQL table based on parent schema.
         *
         */
        @Deprecated
        public AdqlTable select(final AdqlSchema parent, final BaseTable<?> base);

        /**
         * Access to our column factory.
         *
         */
        public AdqlColumn.Factory adqlColumns();

        }

    /**
     * Access to our base table.
     *
     */
    public BaseTable<?> base();

    /**
     * Public interface for accessing a table's columns.
     *
     */
    public interface Columns
    extends DataTable.Columns<AdqlColumn>
        {

        /**
         * Search for a view of a specific column.
         *
         */
        public AdqlColumn select(final BaseColumn<?> base);

        }

    /**
     * Access to this table's columns.
     *
     */
    @Override
    public AdqlTable.Columns columns();

    /**
     * Access to our parent resource.
     *
     */
    public AdqlResource resource();

    /**
     * Access to our parent catalog.
     *
     */
    public AdqlCatalog catalog();

    /**
     * Access to our parent schema.
     *
     */
    public AdqlSchema schema();

    }