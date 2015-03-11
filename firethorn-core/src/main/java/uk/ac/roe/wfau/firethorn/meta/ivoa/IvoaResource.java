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
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;

/**
 *
 *
 */
public interface IvoaResource
extends BaseResource<IvoaSchema>
    {
    /**
     * {@link BaseResource.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<IvoaResource>
        {
        }

    /**
     * {@link BaseResource.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<IvoaResource>
        {
        }
    
    /**
     * {@link BaseResource.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseResource.LinkFactory<IvoaResource>
        {
        }

    /**
     * {@link BaseResource.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseResource.EntityFactory<IvoaResource>
        {
        /**
         * Create a new {@link IvoaResource}.
         *
         */
        public IvoaResource create(final String ivoaid);

        /**
         * Create a new {@link IvoaResource}.
         *
         */
        public IvoaResource create(final String ivoaid, final String name);

        /**
         * Our local {@link IvoaSchema.EntityFactory} implementation.
         * @todo - move to services
         *
         */
        @Deprecated
        public IvoaSchema.EntityFactory schemas();

        }

    /**
     * The resource {@link IvoaSchema schema}.
     *
     */
    public interface Schemas extends BaseResource.Schemas<IvoaSchema>
        {
        /**
         * Create a {@link IvoaSchema.Builder}.
         *
         */
        public IvoaSchema.Builder builder();  

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
        public String endpoint();

        /**
         * Get the endpoint capabilities.
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
        public Endpoint create(final String endpoint);

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

    /**
     * The {@link IvoaResource} metadata.
     *
     */
    public interface Metadata
    extends BaseResource.Metadata
        {
        /**
         * The IVOA metadata.
         * 
         */
        public interface Ivoa
            {
            }
        /**
         * The IVOA metadata.
         * 
         */
        public Ivoa ivoa();
        }

    @Override
    public IvoaResource.Metadata meta();
    
    /**
     * Interface to access the {@link OgsaIvoaResource} OGSA-DAI resources.
     * 
     */
    public interface OgsaIvoaResources
    extends OgsaBaseResources
        {
        /**
         * Select the primary {@link OgsaIvoaResource} OGSA-DAI resource.
         * 
         */
        public OgsaIvoaResource primary();
        
        /**
         * Select all the {@link OgsaIvoaResource} OGSA-DAI resources for this {@link IvoaResource}.
         * 
         */
        public Iterable<OgsaIvoaResource> select();

    }

    /**
     * Access the {@link OgsaIvoaResource} OGSA-DAI resources.
     * 
     */
    public OgsaIvoaResources ogsa();
    
    }
