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
package uk.ac.roe.wfau.firethorn.widgeon.adql ;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;

/**
 * Public interface for an ADQL resource.
 *
 */
public interface AdqlResource
extends DataResource
    {

    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlResource>
        {
        }

    /**
     * Factory interface for creating and selecting views.
     *
     */
    public static interface Factory
    extends DataResource.Factory<AdqlResource>
        {

        /**
         * Create a view of a resource.
         *
         */
        public AdqlResource create(final BaseResource base, final String name);

        /**
         * Select all the views of a resource.
         *
         */
        public Iterable<AdqlResource> select(final BaseResource base);

        /**
         * Select a view of a resource by name.
         *
         */
        public AdqlResource select(final BaseResource base, final String name);

        /**
         * Search for a view of a resource.
         *
         */
        public Iterable<AdqlResource> search(final BaseResource base, final String text);

        /**
         * Access to our catalog factory.
         *
         */
        public AdqlCatalog.Factory catalogs();

        }

    /**
     * Access to our base resource.
     *
     */
    public BaseResource base();

    /**
     * Public interface for accessing a resource's catalogs.
     *
     */
    public interface Catalogs
    extends DataResource.Catalogs<AdqlCatalog>
        {
        }

    /**
     * Access to this resource's catalogs.
     *
     */
    @Override
    public Catalogs catalogs();
    }

