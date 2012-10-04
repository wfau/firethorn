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
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;

/**
 * Public interface for table metadata.
 *
 */
public interface AdqlTable
extends DataResource.DataTable<AdqlSchema>
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
    extends DataResource.DataTable.Factory<AdqlSchema, AdqlTable>
        {

        /**
         * Find an existing table view, or create a new one.
         *
         */
        public AdqlTable cascade(final AdqlSchema parent, final BaseResource.BaseTable<?> base);

        /**
         * Create a new view of a table.
         *
         */
        public AdqlTable create(final AdqlSchema parent, final BaseResource.BaseTable<?> base, final String name);

        /**
         * Select all the views of a table.
         *
         */
        public Iterable<AdqlTable> select(final BaseResource.BaseTable<?> base);

        /**
         * Select a table view based on parent resource.
         *
         */
        public AdqlTable select(final AdqlResource parent, final BaseResource.BaseTable<?> base);

        /**
         * Select a table view based on parent catalog.
         *
         */
        public AdqlTable select(final AdqlCatalog parent, final BaseResource.BaseTable<?> base);

        /**
         * Select a table view based on parent schema.
         *
         */
        public AdqlTable select(final AdqlSchema parent, final BaseResource.BaseTable<?> base);

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
    public BaseResource.BaseTable<?> base();

    /**
     * Public interface for accessing a table's columns.
     *
     */
    public interface Columns
    extends DataResource.DataTable.Columns<AdqlColumn>
        {

        /**
         * Search for a view of a specific column.
         *
         */
        public AdqlColumn select(final BaseResource.BaseColumn<?> base);

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