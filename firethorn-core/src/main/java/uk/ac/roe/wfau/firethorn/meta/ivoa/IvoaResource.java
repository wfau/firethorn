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
package uk.ac.roe.wfau.firethorn.meta.ivoa;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.EntityTracker;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;

/**
 *
 *
 */
public interface IvoaResource
extends BaseResource<IvoaSchema>
    {
    /**
     *
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<IvoaResource>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * Resource factory interface.
     *
     */
    public static interface EntityFactory
    extends BaseResource.EntityFactory<IvoaResource>
        {
        /**
         * Create a new Resource.
         *
         */
        public IvoaResource create(final String ivoaid);

        /**
         * Create a new Resource.
         *
         */
        public IvoaResource create(final String ivoaid, final String name);

        /**
         * The resource schema factory.
         *
         */
        public IvoaSchema.EntityFactory schemas();

        }

    /**
     * Access to the resource schemas.
     *
     */
    public interface Schemas extends BaseResource.Schemas<IvoaSchema>
        {

        /**
         * Create a tracker for the resource schema.
         *
         */
        public IvoaSchema.Tracker tracker();  

        }

    @Override
    public Schemas schemas();

    /**
     * The resource registry URI.
     *
     */
    public String ivoaid();

    /**
     * The resource registry URI.
     *
     */
    public void ivoaid(final String ivoaid);

    /**
     * Public interface for a service Endpoint.
     * 
     */
    public interface Endpoint
        {
        /**
         * The resource endpoint URL.
         *
         */
        public String url();

        /**
         * Get the service capabilities.
         * 
         */

        }

    /**
     * Public  interface for the service Endpoint(s).
     * 
     */
    public interface Endpoints
        {
        /**
         * Add a new service Endpoint.
         * 
         */
        public Endpoint create(final String url);

        /**
         * Select a list of the service Endpoint(s).
         * 
         */
        public Iterable<Endpoint> select();

        }
    
    /**
     * Access to the service Endpoint(s).
     * 
     */
    public Endpoints endpoints();
    
    }
