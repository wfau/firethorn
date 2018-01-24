/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaResource;

/**
 *
 *
 */
public interface OgsaIvoaResource
    extends OgsaBaseResource
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaIvoaResource>
        {
        }
    
    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaIvoaResource>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<OgsaIvoaResource>
        {
        /**
         * Select all the {@link OgsaIvoaResource}(s).
         * @return An {@link Iterable} set of {@link OgsaIvoaResource}(s). 
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaIvoaResource> select()
        throws ProtectionException;

        /**
         * Select the {@link OgsaIvoaResource}(s) for a {@link OgsaService}.
         * @param service The {@link OgsaService} service.
         * @return An {@link Iterable} list of {@link OgsaIvoaResource}(s).
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaIvoaResource> select(final OgsaService service)
        throws ProtectionException;

        /**
         * Select the {@link OgsaIvoaResource}(s) for an {@link IvoaResource}.
         * @param source  The {@link IvoaResource}.
         * @return An {@link Iterable} list of {@link OgsaIvoaResource}(s).
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaIvoaResource> select(final IvoaResource source)
        throws ProtectionException;

        /**
         * Select the {@link OgsaIvoaResource}(s) for an {@link OgsaService} and {@link IvoaResource}.
         * @param service The {@link OgsaService}.
         * @param source  The {@link IvoaResource}.
         * @return An {@link Iterable} list of {@link OgsaIvoaResource}(s).
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaIvoaResource> select(final OgsaService service, final IvoaResource source)
        throws ProtectionException;

        /**
         * Create a new {@link OgsaIvoaResource} for an {@link OgsaService} and {@link IvoaResource}.
         * @param service The parent {@link OgsaService}.
         * @param source  The source {@link IvoaResource}.
         * @return A new {@link OgsaIvoaResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaIvoaResource create(final OgsaService service, final IvoaResource source)
        throws ProtectionException;

        /**
         * Select the primary {@link OgsaIvoaResource} for a {@link IvoaResource}.
         * @param source The {@link IvoaResource} resource.
         * @return The primary {@link OgsaIvoaResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaIvoaResource primary(final IvoaResource source)
        throws ProtectionException;

        /**
         * Select the primary {@link OgsaIvoaResource} for an {@link OgsaService} and {@link IvoaResource}.
         * @param service The {@link OgsaService}.
         * @param source  The {@link IvoaResource}.
         * @return The primary {@link OgsaIvoaResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaIvoaResource primary(final OgsaService service, final IvoaResource source)
        throws ProtectionException;
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends Entity.EntityServices<OgsaIvoaResource>
        {
        /**
         * Our {@link OgsaIvoaResource.EntityFactory} instance.
         *
         */
        public OgsaIvoaResource.EntityFactory entities();
        }
    
    /**
     * The parent {@link IvoaResource}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *  
     */
    public IvoaResource resource()
    throws ProtectionException;
    
    /**
     * Initialize the OGSA-DAI resource, creating a new one if needed.
     * @return The resource status.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *  
     */
    public OgsaStatus init()
    throws ProtectionException;

    }
