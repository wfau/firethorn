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
package uk.ac.roe.wfau.firethorn.widgeon.data ;

import uk.ac.roe.wfau.firethorn.entity.Entity;



/**
 * Public interface for describing a resource.
 *
 */
public interface DataResource
extends DataComponent
    {
    /**
     * Factory interface for creating and selecting resources.
     *
     */
    public static interface Factory<ResourceType extends DataResource>
    extends Entity.Factory<ResourceType>
        {
        /**
         * Select all of the resources.
         *
         */
        public Iterable<ResourceType> select();

        /**
         * Select resources by name.
         *
         */
        public Iterable<ResourceType> select(final String name);

        /**
         * Text search for resources (name starts with).
         *
         */
        public Iterable<ResourceType> search(final String text);

        }

    /**
     * Access to this resources catalogs.
     *
     */
    public Catalogs<?> catalogs();

    /**
     * Public interface for accessing a resources catalogs.
     *
     */
    public interface Catalogs<CatalogType extends DataCatalog<?>>
        {

        /**
         * Select all the catalogs from the resource.
         *
         */
        public Iterable<CatalogType> select();

        /**
         * Select a named catalog from the resource.
         *
         */
        public CatalogType select(final String name);

        /**
         * Text search for catalogs (name starts with).
         *
         */
        public Iterable<CatalogType> search(final String text);

        }
    }

