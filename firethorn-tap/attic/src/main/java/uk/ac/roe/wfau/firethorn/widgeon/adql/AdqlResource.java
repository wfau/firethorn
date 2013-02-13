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

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
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
         * Create an ADQL resource.
         *
         */
        public AdqlResource create(final String name);

        /**
         * Access to our catalog factory.
         *
         */
        public AdqlCatalog.Factory catalogs();

        }

    /**
     * Public interface for accessing the services associated with this resource.
     *
     */
    public interface Services
        {
    	/**
    	 * Create a new service associated with this resource.
    	 * 
    	 */
    	public AdqlService create(String name);

    	/**
         * Select all of the services associated with this resource.
         *
         */
        public Iterable<AdqlService> select();

        }

    /**
     * Access to the services associated with this resource.
     * 
     */
    public Services services();
    
    /**
     * Public interface for accessing a resources catalogs.
     *
     */
    public interface Catalogs
    extends DataResource.Catalogs<AdqlCatalog>
        {
        /**
         * Create a new catalog.
         * 
         */
        public AdqlCatalog create(String name);
        
        }

    @Override
    public Catalogs catalogs();
    }

