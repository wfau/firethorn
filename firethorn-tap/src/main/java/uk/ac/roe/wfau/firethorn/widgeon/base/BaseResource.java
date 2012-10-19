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
package uk.ac.roe.wfau.firethorn.widgeon.base ;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.data.DataResource;

/**
 * Public interface for a data resource (local JDBC database OR remote IVOA TAP service).
 *
 */
public interface BaseResource
extends DataResource
    {

    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<BaseResource>
        {
        }

    /**
     * Factory interface for creating and selecting resources.
     *
    public static interface Factory
    extends FactoryTemplate<BaseResource>
        {
        }
     */

    /**
     * Factory interface for creating and selecting resources.
     *
     */
    public static interface Factory<ResourceType extends BaseResource>
    extends DataResource.Factory<ResourceType>
        {
        /**
         * Select all of the resources.
         *
        public Iterable<ResourceType> select();
         */

        /**
         * Select resources by name.
         *
        public Iterable<ResourceType> select(final String name);
         */

        /**
         * Text search for resources (name starts with).
         *
        public Iterable<ResourceType> search(final String text);
         */

        /**
         * Access to our AdqlResource factory.
         *
         */
        public AdqlResource.Factory views();

        }

    /**
     * Public interface for accessing a resource's Views.
     *
     */
    public interface Views
        {
        /**
         * Create a new view of the resource.
         *
         */
        public AdqlResource create(final String name);

        /**
         * Select all the views of the resource.
         *
         */
        public Iterable<AdqlResource> select();

        /**
         * Select a named view of the resource.
         *
         */
        public AdqlResource select(final String name);

        }

    /**
     * Access to this resource's Views.
     *
     */
    public BaseResource.Views views();

    /**
     * Public interface for accessing a resource's catalogs.
     *
     */
    public interface Catalogs<CatalogType extends BaseCatalog<?>>
    extends DataResource.Catalogs<CatalogType>
        {
        }

    /**
     * Access to this resource's catalogs.
     *
     */
    @Override
    public BaseResource.Catalogs<?> catalogs();
    
    }

