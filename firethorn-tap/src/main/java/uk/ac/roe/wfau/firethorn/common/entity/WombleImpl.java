/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

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

import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;
import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

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
    @Autowired
    private HibernateExceptionTranslator translator;

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
            return translator.translateExceptionIfPossible(
                ouch
                );
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
                if (stateness() == StateFullNess.STATE_LESS)
                    {
                    logger.debug("Using state LESS session to fetch named query [{}]", name);
                    Query query = stateless().session().getNamedQuery(
                        name
                        );
                    if (query != null)
                        {
                        query.setCacheMode(null);
                        query.setFlushMode(null);
                        }
                    else {
                        logger.error("Unable to find query [{}]", name);
                        }                        
                    return query ;
                    }
                else {
                    logger.debug("Using state FULL session to fetch named query [{}]", name);
                    return statefull().session().getNamedQuery(
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
        public AbstractEntity insert(final AbstractEntity entity)
            {
            logger.debug("insert(AbstractEntity)");
            logger.debug("  entity [{}]", entity);
            try {
                if (entity == null)
                    {
                    logger.error("Inserting null entity");
                    throw new IllegalArgumentException(
                        "Inserting null entity"
                        );
                    }
                if (entity.ident() != null)
                    {
                    if (entity.ident().value() != null)
                        {
                        logger.error("Inserting entity with existing ident");
                        throw new IllegalArgumentException(
                            "Inserting entity with existing ident"
                            );
                        }
                    }
                if (stateness() == StateFullNess.STATE_LESS)
                    {
                    logger.debug("Using state LESS session to insert entity [{}]", entity);
                    stateless().session().insert(
                        entity
                        );
                    }
                else {
                    logger.debug("Using state FULL session to insert entity [{}]", entity);
                    statefull().session().save(
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
        public AbstractEntity select(final Class type, final Identifier ident)
            {
            logger.debug("select(Class, Identifier)");
            logger.debug("  class [{}]", type);
            logger.debug("  ident [{}]", (ident != null) ? null : ident.value());
            try {
                if (ident == null)
                    {
                    return null ;
                    }
                if (stateness() == StateFullNess.STATE_LESS)
                    {
                    logger.debug("Using state LESS session to select entity [{}][{}]", type, ident.value());
                    return (AbstractEntity) stateless().session().get(
                        type,
                        ident.value()
                        );
                    }
                else {
                    logger.debug("Using state FULL session to select entity [{}][{}]", type, ident.value());
                    return (AbstractEntity) statefull().session().get(
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
        public AbstractEntity update(final AbstractEntity entity)
            {
            logger.debug("update(AbstractEntity)");
            logger.debug("  entity [{}]", entity);
            try {
                if (entity == null)
                    {
                    logger.error("Updating null entity");
                    throw new IllegalArgumentException(
                        "Updating null entity"
                        );
                    }
                if (entity.ident() == null)
                    {
                    logger.error("Updating entity with null ident");
                    throw new IllegalArgumentException(
                        "Updating entity with null ident"
                        );
                    }
                if (entity.ident().value() == null)
                    {
                    logger.error("Updating entity with null ident");
                    throw new IllegalArgumentException(
                        "Updating entity with null ident"
                        );
                    }
                if (stateness() == StateFullNess.STATE_LESS)
                    {
                    logger.debug("Using state LESS session to update entity [{}]", entity);
                    throw new UnsupportedOperationException(); 
                    }
                else {
                    logger.debug("Using state FULL session to update entity [{}]", entity);
                    statefull().session().update(
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
        public void delete(final AbstractEntity entity)
            {
            logger.debug("delete(AbstractEntity)");
            logger.debug("  entity [{}]", entity);
            try {
                if (entity == null)
                    {
                    logger.error("Deleting null entity");
                    throw new IllegalArgumentException(
                        "Deleting null entity"
                        );
                    }
                if (entity.ident() == null)
                    {
                    logger.error("Deleting entity with null ident");
                    throw new IllegalArgumentException(
                        "Deleting entity with null ident"
                        );
                    }
                if (stateness() == StateFullNess.STATE_LESS)
                    {
                    logger.debug("Using state LESS session to delete entity [{}]", entity);
                    throw new UnsupportedOperationException(); 
                    }
                else {
                    logger.debug("Using state FULL session to delete entity [{}]", entity);
                    statefull().session().delete(
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
                if (stateness() == StateFullNess.STATE_LESS)
                    {
                    logger.debug("Flushing state LESS session");
                    throw new UnsupportedOperationException(); 
                    }
                else {
                    logger.debug("Flushing state FULL session");
                    statefull().session().flush();
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
         * Clear the current session, discarding unsaved changes.
         *
         */
        @Override
        public void clear()
            {
            logger.debug("clear()");
            try {
                if (stateness() == StateFullNess.STATE_LESS)
                    {
                    logger.debug("Clearing state LESS session");
                    throw new UnsupportedOperationException(); 
                    }
                else {
                    logger.debug("Clearing state FULL session");
                    statefull().session().clear();
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
         * Select a single Entity from a Query.
         *
         */
        @Override
        public AbstractEntity single(final Query query)
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
        public AbstractEntity first(final Query query)
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
                    return (AbstractEntity) results.get(0);
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


        /**
         * A ThreadLocal to hold the current StateFullNess.
         *
         */
        protected ThreadLocal<StateFullNess> stateness = 
             new ThreadLocal<StateFullNess>()
                {
                };

        /**
         * Get the statefullness of the current thread.
         *
         */
        @Override
        public StateFullNess stateness()
            {
            logger.debug("Checking stateness");
            if (stateness.get() == null)
                {
                stateness(
                    StateFullNess.STATE_FULL
                    );
                }
            return stateness.get();
            }

        /**
         * Set the statefullness of the current thread.
         *
         */
        @Override
        public void stateness(final StateFullNess state)
            {
            logger.debug("Setting stateness to [{}]", state);
// Error checking goes here ....
            stateness.set(
                state
                );
            }

        /**
         * Access to the current state FULL state.
         *
         */
        @Override
        public StateFullState statefull()
            {
// Error checking goes here ....
            return new StateFullState()
                {
                @Override
                public Session session()
                    {
                    return factory.getCurrentSession();
                    }
                };
            }

        /**
         * A ThreadLocal to hold the current StateLessState.
         *
         */
        protected ThreadLocal<StateLessState> stateless = 
             new ThreadLocal<StateLessState>()
                {
                };

        /**
         * Access to the current state LESS state.
         *
         */
        @Override
        public StateLessState stateless()
            {
            logger.debug("stateless()");
// Error checking goes here ....
            if (stateness.get() == StateFullNess.STATE_LESS)
                {
                if (stateless.get() == null)
                    {
                    logger.debug("Creating new StatelessState");
                    stateless.set(
                        new StateLessState()
                            {
                            /**
                             * Our Hibernate StatelessSession.
                             *
                             */
                            private StatelessSession session ;

                            /**
                             * Access to our Hibernate StatelessSession.
                             *
                             */
                             @Override
                            public StatelessSession session()
                                {
                                return this.session ;
                                }

                            /**
                             * Our Hibernate Transaction.
                             *
                             */
                            private Transaction transaction ;

                            /**
                             * Access to our Hibernate Transaction.
                             *
                             */
                            @Override
                            public Transaction transaction()
                                {
                                return this.transaction ;
                                }

                            /**
                             * Begin a new StatelessSession Transaction.
                             *
                             */
                            @Override
                            public void begin()
                                {
                                logger.debug("begin()");
                                if (this.session == null)
                                    {
                                    logger.debug("Starting new session");
                                    this.session = hibernate().factory().openStatelessSession();
                                    if (this.transaction == null)
                                        {
                                        logger.debug("transaction is null, starting new one");
                                        this.transaction = this.session.beginTransaction();
                                        }
                                    else {
                                        logger.warn("begin() called inside existing transaction");
                                        }
                                    }
                                else {
                                    logger.warn("begin() called inside existing session");
                                    }
                                }

                            /**
                             * Commit the current StatelessSession Transaction.
                             *
                             */
                            @Override
                            public void commit()
                                {
                                logger.debug("commit()");
                                if (this.transaction == null)
                                    {
                                    logger.error("commit() called with no transaction");
                                    }
                                else {
                                    logger.debug("Comitting current transaction");
                                    this.transaction.commit();
                                    this.transaction = null ;
                                    this.close();
                                    }
                                }

                            /**
                             * Rollback the current StatelessSession Transaction.
                             *
                             */
                            @Override
                            public void rollback()
                                {
                                logger.debug("rollback()");
                                if (this.transaction == null)
                                    {
                                    logger.error("rollback() called with no transaction");
                                    }
                                else {
                                    logger.debug("Rolling back current transaction");
                                    try {
                                        this.transaction.rollback();
                                        }
                                    finally {
                                        this.transaction = null ;
                                        this.close();
                                        }
                                    }
                                }

                            /**
                             * Close the current StatelessSession Transaction.
                             *
                             */
                            @Override
                            public void close()
                                {
                                logger.debug("close()");
                                if (this.session == null)
                                    {
                                    logger.error("close() called with no session");
                                    }
                                else {
                                    try {
                                        this.session.close();
                                        }
                                    finally {
                                        this.session = null ;
                                        this.transaction = null ;
                                        }
                                    }
                                }

                            /**
                             * Release the current StatelessState.
                             *
                             */
                            @Override
                            public void done()
                                {
                                logger.debug("done()");
                                stateless.set(
                                    null
                                    );
                                }
                            }
                        );
                    }
                return stateless.get();
                }
            else {
                return null ;
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
    }

