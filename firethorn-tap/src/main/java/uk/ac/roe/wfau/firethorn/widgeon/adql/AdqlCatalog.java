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
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseCatalog;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataCatalog;

/**
 * Public interface for an ADQL catalog.
 *
 */
public interface AdqlCatalog
extends DataCatalog<AdqlResource>
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlCatalog>
        {
        }

    /**
     * Factory interface for creating and selecting catalog views.
     *
     */
    public static interface Factory
    extends DataCatalog.Factory<AdqlResource, AdqlCatalog>
        {

        /**
         * Find an existing catalog view, or create a new one.
         *
         */
        public AdqlCatalog cascade(final AdqlResource parent, final BaseCatalog<?> base);

        /**
         * Create a view of a catalog.
         *
         */
        public AdqlCatalog create(final AdqlResource parent, final BaseCatalog<?> base, final String name);

        /**
         * Select all the views of a catalog.
         *
         */
        public Iterable<AdqlCatalog> select(final BaseCatalog<?> base);

        /**
         * Select a catalog view based on parent resource.
         *
         */
        public AdqlCatalog select(final AdqlResource parent, final BaseCatalog<?> base);

        /**
         * Access to our schema factory.
         *
         */
        public AdqlSchema.Factory schemas();

        }

    /**
     * Access to our base catalog.
     *
     */
    public BaseCatalog<?> base();

    /**
     * Public interface for accessing a catalog's schemas.
     *
     */
    public interface Schemas
    extends DataCatalog.Schemas<AdqlSchema>
        {
        }

    /**
     * Access to this catalog's schemas.
     *
     */
    @Override
    public AdqlCatalog.Schemas schemas();

    /**
     * Access to our parent resource.
     *
     */
    public AdqlResource resource();

    }