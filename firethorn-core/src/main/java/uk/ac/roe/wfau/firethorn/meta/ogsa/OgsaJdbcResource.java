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
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 *
 *
 */
public interface OgsaJdbcResource
extends OgsaBaseResource
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<OgsaJdbcResource>
        {
        }

    /**
     * {@link NamedEntity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<OgsaJdbcResource>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaJdbcResource>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends OgsaBaseResource.EntityFactory<OgsaJdbcResource>
        {
        /**
         * Select all the {@link OgsaJdbcResource}(s).
         * @return An {@link Iterable} set of {@link OgsaJdbcResource}(s). 
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaJdbcResource> select()
        throws ProtectionException;

        /**
         * Select the {@link OgsaJdbcResource}(s) for a {@link OgsaService}.
         * @param service The {@link OgsaService} service.
         * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaJdbcResource> select(final OgsaService service)
        throws ProtectionException;

        /**
         * Select all the {@link OgsaJdbcResource}(s) for a {@link JdbcResource}.
         * @param source The {@link JdbcResource} resource.
         * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<OgsaJdbcResource> select(final JdbcResource source)
        throws ProtectionException;

        /**
        * Select the {@link OgsaJdbcResource}(s) for an {@link OgsaService} and {@link JdbcResource}.
        * @param service The {@link OgsaService}.
        * @param source  The {@link JdbcResource}.
        * @return An {@link Iterable} list of {@link OgsaJdbcResource}(s).
        * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
        *
        */
       public Iterable<OgsaJdbcResource> select(final OgsaService service, final JdbcResource source)
       throws ProtectionException;

        /**
         * Create a new {@link OgsaJdbcResource} for a {@link JdbcResource}..
         * @param source  The {@link JdbcResource}.
         * @return A new {@link OgsaJdbcResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaJdbcResource create(final JdbcResource source)
        throws ProtectionException;

        /**
         * Create a new {@link OgsaJdbcResource} for an {@link OgsaService} and {@link JdbcResource}..
         * @param service The {@link OgsaService}.
         * @param source  The {@link JdbcResource}.
         * @return A new {@link OgsaJdbcResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaJdbcResource create(final OgsaService service, final JdbcResource source)
        throws ProtectionException;

        /**
         * Select the primary {@link OgsaJdbcResource} for a {@link JdbcResource}.
         * @param source The {@link JdbcResource} resource.
         * @return The primary {@link OgsaJdbcResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaJdbcResource primary(final JdbcResource source)
        throws ProtectionException;

        /**
         * Select the primary {@link OgsaJdbcResource} for an {@link OgsaService} and {@link JdbcResource}.
         * @param service The {@link OgsaService}.
         * @param source  The {@link JdbcResource}.
         * @return The primary {@link OgsaJdbcResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public OgsaJdbcResource primary(final OgsaService service, final JdbcResource source)
        throws ProtectionException;
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<OgsaJdbcResource>
        {
        /**
         * Our {@link OgsaJdbcResource.EntityFactory} instance.
         *
         */
        public OgsaJdbcResource.EntityFactory entities();
        }
    
    /**
     * The parent {@link JdbcResource}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *  
     */
    public JdbcResource resource()
    throws ProtectionException;

    }
