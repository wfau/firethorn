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
         * @throws ProtectionException 
         * 
         */
        public Community admins()
        throws ProtectionException;

        /**
         * Access to the guest community.
         * 
         */
        public Community guests();
        
        /**
         * Select or create a new {@link Community}.
         * @param name The {@link Community} name.
         *
         */
        public Community create(final String name)
        throws DuplicateEntityException;

        /**
         * Select or create a new {@link Community}.
         * @param name  The {@link Community} name.
         * @param space The {@link JdbcResource} to use for storing {@link Community} member's data.  
         *
         */
        public Community create(final String name, final JdbcResource space)
        throws DuplicateEntityException;

        /**
         * Select a Community based on name.
         * @param name The {@link Community} name.
         * @return The corresponding {@link Community}.
         * @throws EntityNotFoundException If no matching {@link Community} was found.
         *
         */
        public Community select(final String name)
        throws EntityNotFoundException;

        /**
         * Search for a Community based on name.
         * @param name The {@link Community} name.
         * @return The corresponding {@link Community}, or null if no match found.
         *
         */
        public Community search(final String name);

        /**
         * Login to a {@link Community} using name and password.
         * @param comm The {@link Community} name.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @return The corresponding {@link Identity}.
         * @throws UnauthorizedException If unable to login.
         *
         */
        public Identity login(final String comm, final String name, final String pass)
        throws UnauthorizedException;
        
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
         *
         */
        public Identity create();

        /**
         * Create a new {@link Identity}.
         * @param name The {@link Identity} name.
         * @return The new {@link Identity}.
         *
         */
        public Identity create(final String name)
        throws DuplicateEntityException;
        
        /**
         * Create a new {@link Identity}.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @return The new {@link Identity}.
         *
         */
        public Identity create(final String name, final String pass)
        throws DuplicateEntityException;

        /**
         * Select an existing {@link Identity} by name.
         * @param name The name of the {@link Identity}.
         * @return The corresponding {@link Identity}.
         * @throws NameNotFoundException If no matching {@link Identity} was found.
         *
         */
        public Identity select(final String name)
        throws NameNotFoundException;

        /**
         * Select an {@link Identity} by name.
         * @param name The name of the {@link Identity}.
         * @param create If true, then the {@link Identity} is created if needed.
         * @return The corresponding {@link Identity}, or null if not selected or created.
         *
         */
        public Identity search(final String name, boolean create);
        
        /**
         * Search for an existing {@link Identity} by name.
         * @param name The name of the {@link Identity}.
         * @return The corresponding {@link Identity}, or null if not found.
         *
         */
        public Identity search(final String name);

        /**
         * Login to an {@link Identity} using name and password.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @return The corresponding {@link Identity}.
         * @throws UnauthorizedException If unable to login.
         *
         */
        public Identity login(final String name, final String pass)
        throws UnauthorizedException;

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
     *
     */
    public JdbcResource space();

    /**
     * The {@link JdbcResource} to use as storage space for this {@link Community}.
     * @param  create A flag to indicate if the storage space should be created automatically. 
     * @return The {@link JdbcResource} storage space, or null if no space is allocated.
     *
     */
    public JdbcResource space(final boolean create);

    /**
     * Login to this {@link Community} using name and password.
     * @param name The {@link Identity} name.
     * @param pass The {@link Identity} password.
     * @return The corresponding {@link Identity}.
     * @throws UnauthorizedException If unable to login.
     *
     */
    public Identity login(final String name, final String pass)
    throws UnauthorizedException;

    /**
     * Flag to allow accounts to be created automatically on login.
     * @return The autocreate flag.
     * 
     */
    public Boolean autocreate();

    /**
     * Flag to allow accounts to be created automatically on login.
     * 
     */
    public void autocreate(final Boolean value);

    /**
     * Flag to allow users to create their own accounts.
     * @return The usercreate flag.
     * 
     */
    public Boolean usercreate();

    /**
     * Flag to allow users to create their own accounts.
     * 
     */
    public void usercreate(final Boolean value);

    }
