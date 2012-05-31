/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.HibernateException;

import org.springframework.dao.DataAccessException;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

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
        public DataAccessException convert(HibernateException ouch);

        /**
         * Get the current Hibernate SessionFactory.
         *
         */
        public SessionFactory factory();

        /**
         * Get the statefullness of the current thread.
         *
         */
        public StateFullNess stateness();

        /**
         * Set the statefullness of the current thread.
         *
         */
        public void stateness(StateFullNess state);

        /**
         * Access to the current stateFULL state.
         *
         */
        public StateFullState statefull();

        /**
         * Access to the current stateLESS state.
         *
         */
        public StateLessState stateless();

        }        

    /**
     * The statefullness of the current thread.
     *
     */
    public enum StateFullNess
        {
        STATE_FULL(),
        STATE_LESS();
        }

    /**
     * Inner class to hold Hibernate Session state.
     *
     */
    public interface StateFullState
        {
        /**
         * The current Hibernate Session.
         *
         */
        public Session session();
        }

    /**
     * Inner class to hold Hibernate StatelessSession state.
     *
     */
    public interface StateLessState
        {

        /**
         * The current StatelessSession.
         *
         */
        public StatelessSession session();

        /**
         * The current Transaction.
         *
         */
        public Transaction transaction();

        /**
         * Begin a new Transaction.
         *
         */
        public void begin();

        /**
         * Commit the current Transaction.
         *
         */
        public void commit();

        /**
         * Rollback the current Transaction.
         *
         */
        public void rollback();

        /**
         * Close the current Transaction.
         *
         */
        public void close();

        /**
         * Release the current StatelessState.
         *
         */
        public void done();

        }        

    /**
     * Our Widgeon factory.
     * 
     */
    public Widgeon.Factory widgeons();

    }

