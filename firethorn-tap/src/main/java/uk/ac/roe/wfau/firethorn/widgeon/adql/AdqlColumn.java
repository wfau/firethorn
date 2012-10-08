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
 * Public interface for an ADQL column.
 *
 */
public interface AdqlColumn
extends DataResource.DataColumn<AdqlTable>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlColumn>
        {
        }

    /**
     * Factory interface for creating and selecting columns.
     *
     */
    public static interface Factory
    extends DataResource.DataColumn.Factory<AdqlTable, AdqlColumn>
        {

        /**
         * Find an existing column view, or create a new one.
         *
         */
        public AdqlColumn cascade(final AdqlTable parent, final BaseResource.BaseColumn<?> base);

        /**
         * Create a new View of a column.
         *
         */
        public AdqlColumn create(final AdqlTable parent, final BaseResource.BaseColumn<?> base, final String name);

        /**
         * Select all the views of a column.
         *
         */
        public Iterable<AdqlColumn> select(final BaseResource.BaseColumn<?> base);

        /**
         * Select a column view based on parent resource.
         *
         */
        public AdqlColumn select(final AdqlResource parent, final BaseResource.BaseColumn<?> base);

        /**
         * Select a column view based on parent catalog.
         *
         */
        public AdqlColumn select(final AdqlCatalog parent, final BaseResource.BaseColumn<?> base);

        /**
         * Select a column view based on parent schema.
         *
         */
        public AdqlColumn select(final AdqlSchema parent, final BaseResource.BaseColumn<?> base);

        /**
         * Select a column view based on parent table.
         *
         */
        public AdqlColumn select(final AdqlTable parent, final BaseResource.BaseColumn<?> base);

        }

    /**
     * Access to our base column.
     *
     */
    public BaseResource.BaseColumn<?> base();

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

    /**
     * Access to our parent table.
     *
     */
    public AdqlTable table();

    }