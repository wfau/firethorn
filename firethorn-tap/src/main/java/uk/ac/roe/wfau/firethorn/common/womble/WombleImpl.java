/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.womble ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.HibernateException;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

import org.springframework.context.ApplicationContext;

import org.springframework.dao.DataAccessException;

//import org.springframework.orm.hibernate4.HibernateExceptionTranslator;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.mallard.Mallard;

/**
 * Spring and Hibernate toolkit.
 *
 */
@Component("womble")
public class WombleImpl
implements Womble
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        WombleImpl.class
        );

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
        logger.debug("-- WOMBLE --");
        logger.debug("Womble instance is [{}]", instance);
        return instance ;
        }

    /**
     * Initialise our singleton instance.
     *
     */
    public static void womble(final Womble womble)
        {
        logger.debug("-- WOMBLE --");
        logger.debug("womble(Womble)");
        if (instance == null)
            {
            logger.debug("Womble instance is not set");
            if (womble != null)
                {
                logger.debug("Womble instance set to [{}]", womble);
                instance = womble ;
                }
            else {
                logger.debug("Womble param was null :-(");
                }
            }
        else {
            logger.debug("Womble instance is already set to [{}]", instance);
            logger.debug("Womble already done :-)");
            }
        }

    /**
     * Private constructor.
     *
     */
    private WombleImpl()
        {
        logger.debug("-- WOMBLE --");
        logger.debug("Womble()");
        }

    /**
     * Make this instance the singleton instance.
     * 
     */
    @PostConstruct
    public void postConstruct()
        {
        logger.debug("-- WOMBLE --");
        logger.debug("Womble.postConstruct()");
        womble(
            (Womble) context.getBean(
                Womble.class
                )
            );
        }

    /**
     * Our Spring ApplicationContext.
     * 
     */
    @Autowired
    private ApplicationContext context ;

    /**
     * Our Spring components.
     * 
     */
    public SpringStuff spring()
        {
        return new SpringStuff()
            {
            @Override
            public ApplicationContext context()
                {
                return context ;
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
     */
//  @Autowired
//  private HibernateExceptionTranslator translator;

    /**
     * Our Hibernate components.
     * 
     */
    public HibernateStuff hibernate()
        {
        return this.hibernate ;
        }

    /**
     * Our Hibernate components.
     * 
     */
    private HibernateStuff hibernate = new HibernateStuff()
        {

        /**
         * Wrap a Hibernate Exception.
         *
         */
        @Override
        public DataAccessException convert(final HibernateException ouch)
            {
            logger.error(
                "Error executing Hibernate query [{}][{}]",
                ouch.getClass().getName(),
                ouch.getMessage()
                );
            throw ouch ;
/*
            return translator.translateExceptionIfPossible(
                ouch
                );
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
            logger.debug("query(String)");
            logger.debug("  name [{}]", name);
            try {
                if (name == null)
                    {
                    logger.error("Query name required");
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
            catch (HibernateException ouch)
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
            logger.debug("insert(Entity)");
            logger.debug("  entity [{}]", entity);
            try {
                if (entity == null)
                    {
                    logger.error("Inserting null entity");
                    throw new IllegalArgumentException(
                        "Inserting null entity"
                        );
                    }
                else if (entity.ident() != null)
                    {
                    if (entity.ident().value() != null)
                        {
                        logger.error("Inserting entity with existing ident");
                        throw new IllegalArgumentException(
                            "Inserting entity with existing ident"
                            );
                        }
                    }
                else {
Session current = session();
logger.debug("Current session [{}]", current);
logger.debug("Current transaction [{}]", current.getTransaction());
                    current.save(
                        entity
                        );
                    }
                }
            catch (HibernateException ouch)
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
        public Entity select(final Class type, final Identifier ident)
            {
            logger.debug("select(Class, Identifier)");
            logger.debug("  class [{}]", type);
            logger.debug("  ident [{}]", (ident != null) ? ident.value() : null);
            try {
                if (ident == null)
                    {
                    return null ;
                    }
                else {
Session current = session();
logger.debug("Current session [{}]", current);
logger.debug("Current transaction [{}]", current.getTransaction());
                    return (Entity) session().get(
                        type,
                        ident.value()
                        );
                    }
                }
            catch (HibernateException ouch)
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
            logger.debug("update(Entity)");
            logger.debug("  entity [{}]", entity);
            try {
                if (entity == null)
                    {
                    logger.error("Updating null entity");
                    throw new IllegalArgumentException(
                        "Updating null entity"
                        );
                    }
                else if (entity.ident() == null)
                    {
                    logger.error("Updating entity with null ident");
                    throw new IllegalArgumentException(
                        "Updating entity with null ident"
                        );
                    }
                else if (entity.ident().value() == null)
                    {
                    logger.error("Updating entity with null ident");
                    throw new IllegalArgumentException(
                        "Updating entity with null ident"
                        );
                    }
                else {
                    session().merge(
                        entity
                        );
                    }
                }
            catch (HibernateException ouch)
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
            logger.debug("delete(Entity)");
            logger.debug("  entity [{}]", entity);
            try {
                if (entity == null)
                    {
                    logger.error("Deleting null entity");
                    throw new IllegalArgumentException(
                        "Deleting null entity"
                        );
                    }
                else if (entity.ident() == null)
                    {
                    logger.error("Deleting entity with null ident");
                    throw new IllegalArgumentException(
                        "Deleting entity with null ident"
                        );
                    }
                else {
                    session().delete(
                        entity
                        );
                    }
                }
            catch (HibernateException ouch)
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
            logger.debug("flush()");
            try {
                session().flush();
                }
            catch (HibernateException ouch)
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
            logger.debug("clear()");
            try {
                session().clear();
                }
            catch (HibernateException ouch)
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
                logger.error("Null query");
                throw new IllegalArgumentException(
                    "Null query"
                    );
                }
            try {
                ScrollableResults results = query.scroll(
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
            catch (HibernateException ouch)
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
     * Our Widgeon factory.
     * 
     */
    @Autowired
    protected Widgeon.Factory widgeons ;

    /**
     * Access to our Widgeon factory.
     * 
     */
    @Override
    public Widgeon.Factory widgeons()
        {
        return this.widgeons ;
        }

    /**
     * Our Mallard factory.
     * 
     */
    @Autowired
    protected Mallard.Factory mallards ;

    /**
     * Our Mallard factory.
     * 
     */
    public Mallard.Factory mallards()
        {
        return this.mallards ;
        }

    }

