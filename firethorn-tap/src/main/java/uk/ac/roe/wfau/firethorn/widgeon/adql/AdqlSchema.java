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
 * Public interface for an ADQL schema.
 *
 */
public interface AdqlSchema
extends DataResource.DataSchema<AdqlCatalog>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlSchema>
        {
        }

    /**
     * Factory interface for creating and selecting schemas.
     *
     */
    public static interface Factory
    extends DataResource.DataSchema.Factory<AdqlCatalog, AdqlSchema>
        {

        /**
         * Find an existing schema view, or create a new one.
         *
         */
        public AdqlSchema cascade(final AdqlCatalog parent, final BaseResource.BaseSchema<?> base);

        /**
         * Create a new view of a schema.
         *
         */
        public AdqlSchema create(final AdqlCatalog parent, final BaseResource.BaseSchema<?> base, final String name);

        /**
         * Select all the views of a schema.
         *
         */
        public Iterable<AdqlSchema> select(final BaseResource.BaseSchema<?> base);

        /**
         * Select a schema view based on parent resource.
         *
         */
        public AdqlSchema select(final AdqlResource parent, final BaseResource.BaseSchema<?> base);

        /**
         * Select a schema view based on parent catalog.
         *
         */
        public AdqlSchema select(final AdqlCatalog parent, final BaseResource.BaseSchema<?> base);

        /**
         * Access to our table factory.
         *
         */
        public AdqlTable.Factory tables();

        }

    /**
     * Access to our base schema.
     *
     */
    public BaseResource.BaseSchema<?> base();

    /**
     * Public interface for accessing a schema's tables.
     *
     */
    public interface Tables
    extends DataResource.DataSchema.Tables<AdqlTable>
        {
        }

    /**
     * Access to this schema's tables.
     *
     */
    @Override
    public AdqlSchema.Tables tables();

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

    }