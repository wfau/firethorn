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
         * Access to our AdqlResource factory.
         *
         */
        public AdqlResource.Factory views();

        }

    /**
     * Public interface for accessing a resources catalogs.
     *
     */
    public interface Catalogs<CatalogType extends BaseCatalog<?>>
    extends DataResource.Catalogs<CatalogType>
        {
        }

    /**
     * Access to this resources catalogs.
     *
     */
    @Override
    public BaseResource.Catalogs<?> catalogs();
    
    }

