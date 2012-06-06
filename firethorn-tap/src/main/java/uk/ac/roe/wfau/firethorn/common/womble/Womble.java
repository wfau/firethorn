/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.womble ;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.HibernateException;

import org.springframework.dao.DataAccessException;
import org.springframework.context.ApplicationContext;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
//import uk.ac.roe.wfau.firethorn.common.entity.GenericEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;

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
         * Get a named query.
         *
         */
        public Query query(String name);

        /**
         * Insert a new Entity.
         *
         */
        public AbstractEntity insert(AbstractEntity entity);

        /**
         * Select an existing existing Entity by Identifier.
         *
         */
        public AbstractEntity select(Class type, Identifier ident);

        /**
         * Update an existing Entity.
         *
         */
        public AbstractEntity update(AbstractEntity entity);

        /**
         * Delete an existing Entity.
         *
         */
        public void delete(AbstractEntity entity);

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
        public AbstractEntity single(Query query);

        /**
         * Select the first Entity from a Query.
         *
         */
        public AbstractEntity first(Query query);

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
         * Access to the current state FULL state.
         *
         */
        public StateFullState statefull();

        /**
         * Inner class to hold Hibernate state FULL Session state.
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
         * Access to the current state LESS state.
         *
         */
        public StateLessState stateless();

        /**
         * Inner class to hold Hibernate state LESS Session state.
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
        }        

    /**
     * Our Widgeon factory.
     * 
     */
    public Widgeon.Factory widgeons();

    }

