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
package uk.ac.roe.wfau.firethorn.widgeon.data;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * Public interface for a catalog.
 *
 */
public interface DataCatalog<ResourceType extends DataResource>
extends DataComponent
    {

    /**
     * Factory interface for creating and selecting catalogs.
     *
     */
    public static interface Factory<ResourceType extends DataResource, CatalogType extends DataCatalog<?>>
    extends Entity.Factory<CatalogType>
        {

        /**
         * Select all the catalogs for a resource.
         *
         */
        public Iterable<CatalogType> select(final ResourceType parent);

        /**
         * Select a named catalog for a resource.
         *
         */
        public CatalogType select(final ResourceType parent, final String name);

        /**
         * Text search for catalogs (name starts with).
         *
         */
        public Iterable<CatalogType> search(final ResourceType parent, final String text);

        }

    /**
     * Access to our parent resource.
     *
     */
    public ResourceType parent();

    /**
     * Access to this catalog's schemas.
     *
     */
    public DataCatalog.Schemas<?> schemas();

    /**
     * Public interface for accessing a catalog's schemas.
     *
     */
    public interface Schemas<SchemaType extends DataSchema<?>>
        {

        /**
         * Select all the schemas from the catalog.
         *
         */
        public Iterable<SchemaType> select();

        /**
         * Select a named schema from the catalog.
         *
         */
        public SchemaType select(final String name);

        /**
         * Text search for schema (name starts with).
         *
         */
        public Iterable<SchemaType> search(final String text);

        }
    }