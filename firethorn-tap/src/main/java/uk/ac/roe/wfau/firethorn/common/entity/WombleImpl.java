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

import org.springframework.context.ApplicationContext;

import org.springframework.dao.DataAccessException;

import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

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
    public static void womble(Womble womble)
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
     * A ThreadLocal to hold the current StateFullNess.
     *
     */
    private ThreadLocal<StateFullNess> stateness = 
         new ThreadLocal<StateFullNess>()
            {
            };

    /**
     * A ThreadLocal to hold the current StateLessState.
     *
     */
    private ThreadLocal<StateLessState> stateless = 
         new ThreadLocal<StateLessState>()
            {
            };

    /**
     * Our Hibernate components.
     * 
     */
    private HibernateStuff hibernate;
    public  HibernateStuff hibernate()
        {
        if (hibernate == null)
            {
            hibernate = new HibernateStuff()
                {
                /**
                 * Wrap a Hibernate Exception.
                 *
                 */
                public DataAccessException convert(HibernateException ouch)
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
                 * Get the statefullness of the current thread.
                 *
                 */
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
                public void stateness(StateFullNess state)
                    {
                    logger.debug("Setting stateness to [{}]", state);

// Error checking goes here ....

                    stateness.set(
                        state
                        );
                    }

                @Override
                public StateFullState statefull()
                    {
                    return new StateFullState();
                    }

                @Override
                public StateLessState stateless()
                    {
                    logger.debug("stateless()");
                    if (stateless.get() == null)
                        {
                        logger.debug("Creating new StatelessState");
                        stateless.set(
                            new StateLessState()
                            );
                        }
                    return stateless.get();
                    }
                };
            }
        return hibernate ;
        }

    /**
     * Inner class to hold Hibernate Session state.
     *
     */
    public class StateFullState
    implements Womble.StateFullState
        {
        /**
         * The current Session.
         *
         */
        public Session session()
            {
            return factory.getCurrentSession();
            }
        }

    /**
     * Inner class to hold Hibernate StatelessSession state.
     *
     */
    public class StateLessState
    implements Womble.StateLessState
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

