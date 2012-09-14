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

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

import org.springframework.context.ApplicationContext;

import org.springframework.dao.DataAccessException;

//import org.springframework.orm.hibernate4.HibernateExceptionTranslator;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.mallard.DataService;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Spring and Hibernate toolkit.
 *
 */
@Slf4j
@Component("womble")
public class WombleImpl
implements Womble
    {

    /**
     * Our global singleton instance.
     *
     */
    static Womble instance = null ;

    /**
     * Access to our singleton instance.
     *
     */
    public static Womble womble()
        {
        return instance ;
        }

    /**
     * Initialise our singleton instance.
     *
     */
    public static void womble(final Womble womble)
        {
        if (instance == null)
            {
            if (womble != null)
                {
                instance = womble ;
                }
            else {
                log.warn("Womble param was null :-(");
                }
            }
        else {
            log.warn("Womble instance is already set to [{}]", instance);
            }
        }

    /**
     * Private constructor.
     *
     */
    private WombleImpl()
        {
        }

    /**
     * Our Spring ApplicationContext.
     *
     */
    @Autowired
    private ApplicationContext appcontext ;

    /**
     * Make this instance the singleton instance.
     *
     */
    @PostConstruct
    public void postConstruct()
        {
        womble(
            appcontext.getBean(
                Womble.class
                )
            );
        }

    /**
     * Our Spring components.
     *
     */
    @Override
    public SpringStuff spring()
        {
        return new SpringStuff()
            {
            @Override
            public ApplicationContext context()
                {
                return appcontext ;
                }
            };
        }

    /**
     * Our autowired Hibernate SessionFactory.
     *
     */
    @Autowired
    private SessionFactory factory;

    /**
     * Our autowired Hibernate ExceptionTranslator.
     *
    @Autowired
    private HibernateExceptionTranslator translator;
     */

    /**
     * Our Hibernate components.
     *
     */
    @Override
    public HibernateStuff hibernate()
        {
        return this.hibernate ;
        }

    /**
     * Our Hibernate components.
     *
     */
    private final HibernateStuff hibernate = new HibernateStuff()
        {

        /**
         * Wrap a Hibernate Exception.
         *
         */
        @Override
        public DataAccessException convert(final HibernateException ouch)
            {
            log.error(
                "Error executing Hibernate query [{}][{}]",
                ouch.getClass().getName(),
                ouch.getMessage()
                );
            throw ouch ;
/*
 *
            return translator.translateExceptionIfPossible(
                ouch
                );
 *
 */
            }

        /**
         * Get the current Hibernate SessionFactory.
         *
         */
        @Override
        public SessionFactory factory()
            {
            return factory ;
            }

        /**
         * Get the current Hibernate Session.
         *
         */
        @Override
        public Session session()
            {
            return factory.getCurrentSession();
            }

        /**
         * Get a named query.
         *
         */
        @Override
        public Query query(final String name)
            {
            //log.debug("query(String)");
            //log.debug("  name [{}]", name);
            try {
                if (name == null)
                    {
                    log.error("Query name required");
                    throw new IllegalArgumentException(
                        "Query name required"
                        );
                    }
                else {
                    return session().getNamedQuery(
                        name
                        );
                    }
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            }

        /**
         * Insert a new Entity.
         *
         */
        @Override
        public Entity insert(final Entity entity)
            {
            //log.debug("insert(Entity)");
            //log.debug("  {}", entity);
            try {
                if (entity == null)
                    {
                    log.error("Attempting to insert a null entity");
                    throw new IllegalArgumentException(
                        "Attempting to insert a null entity"
                        );
                    }
                else if (entity.ident() != null)
                    {
                    log.error("Attempting to insert an entity with ident already set");
                    throw new IllegalArgumentException(
                        "Attempting to insert an entity with ident already set"
                        );
                    }
                else {
                    session().save(
                        entity
                        );
                    }
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            return entity ;
            }

        /**
         * Select an existing existing Entity by Identifier.
         *
         */
        @Override
        public Entity select(final Class<?> type, final Identifier ident)
            {
            //log.debug("select(Class, Identifier)");
            //log.debug("  class [{}]", type);
            //log.debug("  ident [{}]", (ident != null) ? ident.value() : null);
            try {
                if (ident == null)
                    {
                    return null ;
                    }
                else {
                    return (Entity) session().get(
                        type,
                        ident.value()
                        );
                    }
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            }

        /**
         * Update an existing Entity.
         *
         */
        @Override
        public Entity update(final Entity entity)
            {
            //log.debug("update(Entity)");
            //log.debug("  {}", entity);
            try {
                if (entity == null)
                    {
                    log.error("Attempting to update a null entity");
                    throw new IllegalArgumentException(
                        "Attempting to update null entity"
                        );
                    }
                else if (entity.ident() == null)
                    {
                    log.error("Attempting to update an entity with a null ident");
                    throw new IllegalArgumentException(
                        "Attempting to update an entity with a null ident"
                        );
                    }
                else {
                    session().merge(
                        entity
                        );
                    }
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            return entity ;
            }

        /**
         * Delete an existing Entity.
         *
         */
        @Override
        public void delete(final Entity entity)
            {
            //log.debug("delete(Entity)");
            //log.debug("  {}", entity);
            try {
                if (entity == null)
                    {
                    log.error("Attempting to delete a null entity");
                    throw new IllegalArgumentException(
                        "Attempting to delete a null entity"
                        );
                    }
                else if (entity.ident() == null)
                    {
                    log.error("Attempting to delete an entity with a null ident");
                    throw new IllegalArgumentException(
                        "Attempting to delete an entity with a null ident"
                        );
                    }
                else {
                    session().delete(
                        entity
                        );
                    }
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            }

        /**
         * Flush current changes to the database.
         *
         */
        @Override
        public void flush()
            {
            //log.debug("flush()");
            try {
                session().flush();
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            }

        /**
         * Clear the current session, discarding unsaved changes.
         *
         */
        @Override
        public void clear()
            {
            //log.debug("clear()");
            try {
                session().clear();
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            }

        /**
         * Select a single Entity from a Query.
         *
         */
        @Override
        public Entity single(final Query query)
            {
            return first(
                query
                );
            }

        /**
         * Select the first Entity from a Query.
         *
         */
        @Override
        public Entity first(final Query query)
            {
            if (query == null)
                {
                log.error("Attempting to get data from a null query");
                throw new IllegalArgumentException(
                    "Attempting to get data from a null query"
                    );
                }
            try {
                final ScrollableResults results = query.scroll(
                    ScrollMode.FORWARD_ONLY
                    );
                if (results.next())
                    {
                    return (Entity) results.get(0);
                    }
                else {
                    return null ;
                    }
                }
            catch (final HibernateException ouch)
                {
                throw convert(
                    ouch
                    );
                }
            }
        };

/*
 * Flush changes ..
 *
 * Clear session ..
 *
 * Evict objects from cache ..
 * http://www.basilv.com/psd/blog/2010/avoiding-caching-to-improve-hibernate-performance
 *
 * Use StatelessSession
 * http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html/batch.html#batch-statelesssession
 * https://www.owasp.org/index.php/Hibernate-Guidelines#About_StatelessSession
 *
 *
 *
 */


    /**
     * Our Autowired BaseResource factory.
     *
     */
    @Autowired
    protected BaseResource.Factory baseResources ;

    /**
     * Our Autowired JdbcResource factory.
     *
     */
    @Autowired
    protected JdbcResource.Factory jdbcResources ;


    @Override
    public ResourceFactories resources()
        {
        return new ResourceFactories()
            {
            @Override
            public BaseResource.Factory base()
                {
                return baseResources;
                }

            @Override
            public JdbcResource.Factory jdbc()
                {
                return jdbcResources ;
                }
            };
        }

    /**
     * Our Autowired DataService factory.
     *
     */
    @Autowired
    protected DataService.Factory services ;

    @Override
    public DataService.Factory services()
        {
        return this.services ;
        }

    /**
     * Our Autowired Identity factory.
     *
     */
    @Autowired
    protected Identity.Factory identities ;

    @Override
    public Identity.Factory identities()
        {
        return this.identities ;
        }

    /**
     * Our Autowired Identity context factory.
     *
     */
    @Autowired
    protected Identity.Context.Factory contexts ;

    @Override
    public Identity.Context context()
        {
        return this.contexts.context();
        }
    }

