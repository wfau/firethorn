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
        public AdqlCatalog cascade(final AdqlResource parent, final BaseCatalog<?> base);
         */

        /**
         * Create an ADQL catalog.
         *
         */
        public AdqlCatalog create(final AdqlResource parent, final String name);

        /**
         * Access to our schema factory.
         *
         */
        public AdqlSchema.Factory schemas();

        }

    /**
     * Public interface for accessing a catalogs schemas.
     *
     */
    public interface Schemas
    extends DataCatalog.Schemas<AdqlSchema>
        {
        }

    @Override
    public AdqlCatalog.Schemas schemas();

    /**
     * Access to our parent resource.
     *
     */
    public AdqlResource resource();

    }