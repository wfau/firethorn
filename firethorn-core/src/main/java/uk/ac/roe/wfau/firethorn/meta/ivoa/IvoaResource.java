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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaIvoaResource;

/**
 * Public interface for an external IVOA resource.
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaResource create(final String endpoint)
        throws ProtectionException;    

        /**
         * Create a new {@link IvoaResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaResource create(final String name, final String endpoint)
        throws ProtectionException;    

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<IvoaResource>
        {
        /**
         * Our {@link IvoaResource.EntityFactory} instance.
         *
         */
        public IvoaResource.EntityFactory entities();

        /**
         * Our {@link IvoaSchema.EntityFactory} instance.
         *
         */
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaSchema.Builder builder()  
        throws ProtectionException;    

        }
    @Override
    public Schemas schemas()
    throws ProtectionException;    

    /**
     * Public interface for a service Endpoint.
     * 
     */
    public interface Endpoint
        {
        /**
         * The parent resource.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public IvoaResource resource()
        throws ProtectionException;    

        /**
         * The resource endpoint URL as a String.
         * @todo Find a better name ?
         *
         */
        public String string();

        }

    /**
     * The service endpoint.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public Endpoint endpoint()
    throws ProtectionException;    
    
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Ivoa ivoa()
        throws ProtectionException;    

        }

    @Override
    public IvoaResource.Metadata meta()
    throws ProtectionException;    
    
    /**
     * Interface to access the {@link OgsaIvoaResource} OGSA-DAI resources.
     * 
     */
    public interface OgsaIvoaResources
    extends OgsaBaseResources
        {
        /**
         * Select the primary {@link OgsaIvoaResource} OGSA-DAI resource.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public OgsaIvoaResource primary()
        throws ProtectionException;    
        
        /**
         * Select all the {@link OgsaIvoaResource} OGSA-DAI resources for this {@link IvoaResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Iterable<OgsaIvoaResource> select()
        throws ProtectionException;    

        }

    /**
     * Access the {@link OgsaIvoaResource} OGSA-DAI resources.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public OgsaIvoaResources ogsa()
    throws ProtectionException;    

    }
