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
package uk.ac.roe.wfau.firethorn.common.womble ;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;

import org.springframework.dao.DataAccessException;

import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.config.ConfigProperty;

import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Spring and Hibernate toolkit.
 *
 */
public interface Womble
    {

    /**
     * Our Spring stuff.
     *
     */
    public SpringStuff spring();

    /**
     * Inner class to handle Spring suff.
     *
     */
    public interface SpringStuff
        {
        /**
         * The current ApplicationContext.
         *
         */
        public ApplicationContext context();

        }

    /**
     * Our Hibernate stuff.
     *
     */
    public HibernateStuff hibernate();

    /**
     * Inner class to handle Hibernate stuff.
     *
     */
    public interface HibernateStuff
        {

        /**
         * Wrap a Hibernate Exception.
         *
         */
        public DataAccessException convert(final HibernateException ouch);

        /**
         * Get the current Hibernate SessionFactory.
         *
         */
        public SessionFactory factory();

        /**
         * Get the current Hibernate Session.
         *
         */
        public Session session();

        /**
         * Get a named query.
         *
         */
        public Query query(final String name);

        /**
         * Insert a new Entity.
         *
         */
        public Entity insert(final Entity entity);

        /**
         * Select an existing existing Entity by Identifier.
         *
         */
        public Entity select(final Class<?> type, final Identifier ident);

        /**
         * Update an existing Entity.
         *
         */
        public Entity update(final Entity entity);

        /**
         * Delete an existing Entity.
         *
         */
        public void delete(final Entity entity);

        /**
         * Flush current changes to the database.
         *
         */
        public void flush();

        /**
         * Clear the current session, discarding unsaved changes.
         *
         */
        public void clear();

        /**
         * Select a single Entity from a Query.
         *
         */
        public Entity single(final Query query);

        /**
         * Select the first Entity from a Query.
         *
         */
        public Entity first(final Query query);

        }

    /**
     * Factory interface for accessing our resource factories..
     *
     */
    public interface ResourceFactories
        {

        public BaseResource.Factory base();

        public JdbcResource.Factory jdbc();

        }

    /**
     * Access to our DataResource factory.
     *
     */
    public ResourceFactories resources();

    /**
     * Access to our AdqlService factory.
     *
     */
    public AdqlService.Factory services();

    /**
     * Access to our Identity factory.
     *
     */
    public Identity.Factory identities();

    /**
     * Access to the current Identity context.
     *
     */
    public Identity.Context context();

    /**
     * Access to our ConfigProperty factory.
     *
     */
    public ConfigProperty.Factory config();

    }

