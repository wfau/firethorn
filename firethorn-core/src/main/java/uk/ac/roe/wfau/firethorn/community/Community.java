/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.community;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;

/**
 * Public interface for a Community of Identities.
 *
 */
public interface Community
extends NamedEntity
    {
        
    /**
     * Public interface for a {@link Community} {@link NamedEntity.NameFactory}.
     * @see NamedEntity.LinkFactory
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<Community>
        {
        }

    /**
     * Public interface for a {@link Community} {@link Entity.LinkFactory}.
     * @see Entity.LinkFactory
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<Community>
        {
        }

    /**
     * Public interface for a {@link Community} {@link Entity.IdentFactory}.
     * @see Entity.IdentFactory
     * 
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<Community>
        {
        }

    /**
     * Public interface for a {@link Community} {@link Entity.EntityFactory}.
     * @see Entity.EntityFactory
     *
     */
    public interface EntityFactory
    extends Entity.EntityFactory<Community>
        {
        /**
         * Access to the system community.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Community admins()
        throws ProtectionException;

        /**
         * Access to the guest community.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Community guests()
        throws ProtectionException;
        
        /**
         * Select or create a new {@link Community}.
         * @param name The {@link Community} name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Community create(final String name)
        throws ProtectionException, DuplicateEntityException;

        /**
         * Select or create a new {@link Community}.
         * @param name  The {@link Community} name.
         * @param space The {@link JdbcResource} to use for storing {@link Community} member's data.  
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Community create(final String name, final JdbcResource space)
        throws ProtectionException, DuplicateEntityException;

        /**
         * Select a Community based on name.
         * @param name The {@link Community} name.
         * @return The corresponding {@link Community}.
         * @throws EntityNotFoundException If no matching {@link Community} was found.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Community select(final String name)
        throws ProtectionException, EntityNotFoundException;

        /**
         * Search for a Community based on name.
         * @param name The {@link Community} name.
         * @return The corresponding {@link Community}, or null if no match found.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Community search(final String name)
        throws ProtectionException;

        /**
         * Login to a {@link Community} using name and password.
         * @param comm The {@link Community} name.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @return The corresponding {@link Identity}.
         * @throws UnauthorizedException If unable to login.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity login(final String comm, final String name, final String pass)
        throws ProtectionException, UnauthorizedException;
        
        }

    /**
     * Public interface for the {@link Community} {@link Entity.EntityServices}.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<Community>
        {
        /**
         * Our {@link Community.EntityFactory} instance.
         *
         */
        public Community.EntityFactory entities();

        /**
         * Our {@link Identity} factory instance.
         * 
         */
        public Identity.EntityFactory identities();

        }

    /**
     * Access to the {@link Community} members.
     *
     */
    public interface Members
        {
        /**
         * Create a new {@link Identity} with a generated name.
         * @return The new {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity create()
        throws ProtectionException;

        /**
         * Create a new {@link Identity}.
         * @param name The {@link Identity} name.
         * @return The new {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity create(final String name)
        throws ProtectionException, DuplicateEntityException;
        
        /**
         * Create a new {@link Identity}.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @return The new {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity create(final String name, final String pass)
        throws ProtectionException, DuplicateEntityException;

        /**
         * Select an existing {@link Identity} by name.
         * @param name The name of the {@link Identity}.
         * @return The corresponding {@link Identity}.
         * @throws NameNotFoundException If no matching {@link Identity} was found.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity select(final String name)
        throws ProtectionException, NameNotFoundException;

        /**
         * Select an {@link Identity} by name.
         * @param name The name of the {@link Identity}.
         * @param create If true, then the {@link Identity} is created if needed.
         * @return The corresponding {@link Identity}, or null if not selected or created.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity search(final String name, boolean create)
        throws ProtectionException;
        
        /**
         * Search for an existing {@link Identity} by name.
         * @param name The name of the {@link Identity}.
         * @return The corresponding {@link Identity}, or null if not found.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity search(final String name)
        throws ProtectionException;

        /**
         * Login to an {@link Identity} using name and password.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @return The corresponding {@link Identity}.
         * @throws UnauthorizedException If unable to login.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity login(final String name, final String pass)
        throws ProtectionException, UnauthorizedException;

        }

    /**
     * Access to the {@link Community} members.
     * @return A {@link Members} interface for accessing the {@link Community} members. 
     *
     */
    public Members members();

    /**
     * The {@link JdbcResource} to use as storage space for this {@link Community}.
     * @return The {@link JdbcResource} storage space, or null if no space is allocated.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public JdbcResource space()
    throws ProtectionException;

    /**
     * The {@link JdbcResource} to use as storage space for this {@link Community}.
     * @param  create A flag to indicate if the storage space should be created automatically. 
     * @return The {@link JdbcResource} storage space, or null if no space is allocated.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public JdbcResource space(final boolean create)
    throws ProtectionException;

    /**
     * Login to this {@link Community} using name and password.
     * @param name The {@link Identity} name.
     * @param pass The {@link Identity} password.
     * @return The corresponding {@link Identity}.
     * @throws UnauthorizedException If unable to login.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public Identity login(final String name, final String pass)
    throws UnauthorizedException, ProtectionException;

    /**
     * Flag to allow accounts to be created automatically on login.
     * @return The autocreate flag.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public Boolean autocreate()
    throws ProtectionException;

    /**
     * Flag to allow accounts to be created automatically on login.
     * @param value The flag value.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void autocreate(final Boolean value)
    throws ProtectionException;

    /**
     * Flag to allow users to create their own accounts.
     * @return The usercreate flag.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public Boolean usercreate()
    throws ProtectionException;

    /**
     * Flag to allow users to create their own accounts.
     * @param value The flag value.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void usercreate(final Boolean value)
    throws ProtectionException;

    }
