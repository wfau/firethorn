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
package uk.ac.roe.wfau.firethorn.widgeon.base;

import uk.ac.roe.wfau.firethorn.widgeon.data.DataCatalog;

/**
 * Public interface for a catalog.
 *
 */
public interface BaseCatalog<ResourceType extends BaseResource>
extends DataCatalog<ResourceType>
    {
    /**
     * Factory interface for accessing catalogs.
     *
     */
    public static interface Factory<ResourceType extends BaseResource, CatalogType extends BaseCatalog<ResourceType>>
    extends DataCatalog.Factory<ResourceType, CatalogType>
        {
        /**
         * Access to our ADQL factory.
         *
        public AdqlCatalog.Factory views();
         */

        }

    /**
     * Public interface for accessing a catalogs schemas.
     *
     */
    public interface Schemas<SchemaType extends BaseSchema<?>>
    extends DataCatalog.Schemas<SchemaType>
        {
        }

    @Override
    public BaseCatalog.Schemas<?> schemas();

    /**
     * Access to our parent resource.
     *
     */
    public BaseResource resource();

    }