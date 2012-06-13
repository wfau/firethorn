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

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.mallard.Mallard;
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
         * Get the current Hibernate Session.
         *
         */
        public Session session();

        /**
         * Get a named query.
         *
         */
        public Query query(String name);

        /**
         * Insert a new Entity.
         *
         */
        public Entity insert(Entity entity);

        /**
         * Select an existing existing Entity by Identifier.
         *
         */
        public Entity select(Class type, Identifier ident);

        /**
         * Update an existing Entity.
         *
         */
        public Entity update(Entity entity);

        /**
         * Delete an existing Entity.
         *
         */
        public void delete(Entity entity);

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
        public Entity single(Query query);

        /**
         * Select the first Entity from a Query.
         *
         */
        public Entity first(Query query);

        }        

    /**
     * Our Widgeon factory.
     * 
     */
    public Widgeon.Factory widgeons();

    /**
     * Our Mallard factory.
     * 
     */
    public Mallard.Factory mallards();

    }

