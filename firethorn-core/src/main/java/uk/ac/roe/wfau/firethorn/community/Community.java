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

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
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
         * Create a new {@link Community}.
         * @param uri The {@link Community} URI.
         *
         */
        public Community create(final String uri);

        /**
         * Select or create a {@link Community}.
         * @param uri  The {@link Community} URI.
         * @param name The {@link Community} name.
         *
         */
        public Community create(final String uri, final String name);

        /**
         * Select or create a {@link Community}.
         * @param uri   The {@link Community} URI.
         * @param space The {@link JdbcResource} to use for storing {@link Community} member's data.  
         * @param name  The {@link Community} name.
         *
         */
        public Community create(final String uri, final String name, final JdbcResource space);

        /**
         * Select a Community based on URI.
         * @param uri The {@link Community} URI.
         * @return The corresponding {@link Community}.
         * @throws EntityNotFoundException If no matching {@link Community} was found.
         *
         */
        public Community select(final String uri)
        throws EntityNotFoundException;

        /**
         * Our local Identity member factory.
         * 
        public Identity.EntityFactory members();
         */
        
        }

    /**
     * EntityServices interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<Community>
        {
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
         * Create a new {@link Identity}.
         * @param name The {@link Identity} name.
         * @return The new {@link Identity}.
         *
         */
        public Identity create(final String name);

        /**
         * Select an existing {@link Identity} by name.
         * @param name The {@link Identity} name to look for.
         * @return The corresponding {@link Identity}.
         * @throws NameNotFoundException If no matching {@link Identity} was found.
         *
         */
        public Identity select(final String name)
        throws NameNotFoundException;

        }

    /**
     * Access to the {@link Community} members.
     * @return A {@link Members} interface for accessing the {@link Community} members. 
     *
     */
    public Members members();

    /**
     * The unique identifier (URI) for this {@link Community}.
     * @return The {@link Community} URI.
     *
     */
    public String uri();

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

    }
