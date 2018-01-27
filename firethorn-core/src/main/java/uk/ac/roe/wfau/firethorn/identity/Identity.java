/*
 * Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.roe.wfau.firethorn.identity;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.community.Community;
import uk.ac.roe.wfau.firethorn.community.UnauthorizedException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.hibernate.HibernateConvertException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 * Public interface for a Member of a Community.
 *
 */
public interface Identity
extends Entity, NamedEntity
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<Identity>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<Identity>
        {
        }

    /**
     * {@link Entity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<Identity>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Entity.EntityFactory<Identity>
        {
        /**
         * Access to the system {@link Identity}.
         * 
         */
        public Identity admin();

        /**
         * Create a new {@link Identity} with a generated name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity create(final Community community)
        throws ProtectionException;

        /**
         * Create a new {@link Identity}.
         * @param name The {@link Identity} name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity create(final Community community, final String name)
        throws ProtectionException, DuplicateEntityException;

        /**
         * Create a new {@link Identity}.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity create(final Community community, final String name, final String pass)
        throws ProtectionException, DuplicateEntityException;

        /**
         * Select an {@link Identity}, based on name.
         * @param community The {@link Community}.
         * @param name The {@link Identity} name.
         * @return The corresponding {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity select(final Community community, final String name)
        throws ProtectionException, NameNotFoundException;

        /**
         * Search for an {@link Identity} based on name.
         * @param community The {@link Community}.
         * @param name The {@link Identity} name.
         * @return The corresponding {@link Identity}, or null if the {@link Identity} was not found.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity search(final Community community, final String name)
        throws ProtectionException;

        /**
         * Select an {@link Identity}, based on name.
         * @param community The {@link Community}.
         * @param name The {@link Identity} name.
         * @param create Create a new {@link Identity} if not found.
         * @return The corresponding {@link Identity}, or null if the {@link Identity} was not found or created.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Identity search(final Community community, final String name, boolean create)
        throws ProtectionException;
        
        /**
         * Select an {@link Identity}, from any {@link Community} based on just the name.
         * @param name The {@link Identity} name.
         * @return The corresponding {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
        public Identity select(final String name)
        throws ProtectionException, NameNotFoundException;
         */

        /**
         * Search for an {@link Identity},from any {@link Community}, based on just the name.
         * @param name The {@link Identity} name.
         * @return The corresponding {@link Identity}, or null if the {@link Identity} was not found.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
        public Identity search(final String name)
        throws ProtectionException;
         */
        
        /**
         * Login to an {@link Identity} using name and password.
         * @param community The {@link Community}.
         * @param name The {@link Identity} name.
         * @param pass The {@link Identity} password.
         * @return The corresponding {@link Identity}.
         * @throws UnauthorizedException If unable to login.
         *
         */
        public Identity login(final Community community, final String name, final String pass)
        throws UnauthorizedException;
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<Identity>
        {
        /**
         * Our {@link Identity.EntityFactory} instance.
         *
         */
        public Identity.EntityFactory entities();
        }

    /**
     * The parent {@link Community}.
     *
     */
    public Community community();

    /**
     * The ADQL spaces for this {@link Identity}.
     * 
     */
    public static interface AdqlSpaces
    	{
        /**
         * The {@link AdqlSchema} spaces for this {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Iterable<AdqlSchema> select()
        throws ProtectionException;

        /**
         * The current {@link AdqlSchema} space for this {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public AdqlSchema current()
        throws ProtectionException;
    	
    	}

    /**
     * The Jdbc spaces for this {@link Identity}.
     * 
     */
    public static interface JdbcSpaces
    	{
        /**
         * The {@link JdbcSchema} spaces for this {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Iterable<JdbcSchema> select()
        throws ProtectionException;

        /**
         * The current {@link JdbcSchema} space for this {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public JdbcSchema current()
        throws ProtectionException;
    	
    	}
    
    /**
     * The storage spaces for this {@link Identity}.
     * 
     */
    public static interface Spaces
    	{
        /**
         * The ADQL spaces for this {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public AdqlSpaces adql()
        throws ProtectionException;

        /**
         * The JDBC spaces for this {@link Identity}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public JdbcSpaces jdbc()
        throws ProtectionException;

    	}

    /**
     * The storage spaces for this {@link Identity}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public Spaces spaces()
    throws ProtectionException;

    /**
     * Get the {@link Entity} instance linked to the current {@link Thread}.
     * @todo Move this to a base class.
     * 
     */
    public Identity rebase()
	throws HibernateConvertException;

    /**
     * Login to an {@link Identity} using name and password.
     * @param name The {@link Identity} name (for comparison).
     * @param pass The {@link Identity} password.
     * @throws UnauthorizedException If the login failed.
     *
     */
    public void login(final String name, final String pass)
    throws UnauthorizedException;

    }

