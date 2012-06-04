/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.CacheMode;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.HibernateException;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.common.ident.LongIdent;

/**
 * Generic base class for a persistent Entity Factory.
 *
 */
@Repository
public abstract class AbstractFactory<InterfaceType extends GenericEntity, ObjectType extends InterfaceType>
implements GenericEntity.Factory
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(
        AbstractFactory.class
        );

    /**
     * Create an Identifier from a String.
     *
     */
    @Override
    public Identifier ident(String string)
        {
        return new LongIdent(
            string
            );
        }

    /**
     * Our autowired reference to the global Womble.
     *
     */
    @Autowired
    private Womble womble ;
    public Womble womble()
        {
        return womble;
        }

    /**
     * Inner class to wrap common Hibernate functions.
     *
     */
    public class HibernateTools<ObjectType>
        {

        /**
         * Get a named query.
         *
         */
        public Query query(String name)
            {
            logger.debug("query(String)");
            try {
                if (stateness() == Womble.StateFullNess.STATE_LESS)
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
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            }

        /**
         * Insert a new persistent object.
         *
         */
        public ObjectType insert(ObjectType object)
            {
            logger.debug("insert(ObjectType)");
            try {
                if (stateness() == Womble.StateFullNess.STATE_LESS)
                    {
                    logger.debug("Using state LESS session to insert object [{}]", object);
                    stateless().session().insert(
                        object
                        );
                    }
                else {
                    logger.debug("Using state FULL session to insert object [{}]", object);
                    statefull().session().save(
                        object
                        );
                    //this.flush();
                    }
                }
            catch (HibernateException ouch)
                {
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            return object ;
            }

        /**
         * Select a specific object.
         *
         */
        public ObjectType select(Class type, Identifier ident)
            {
            logger.debug("select(Class, Identifier)");
            try {
                if (ident == null)
                    {
                    return null ;
                    }
                else {
                    if (stateness() == Womble.StateFullNess.STATE_LESS)
                        {
                        logger.debug("Using state LESS session to select object [{}][{}]", type, ident);
                        return (ObjectType) stateless().session().get(
                            type,
                            ident.value()
                            );
                        }
                    else {
                        logger.debug("Using state FULL session to select object [{}][{}]", type, ident);
                        return (ObjectType) statefull().session().get(
                            type,
                            ident.value()
                            );
                        }
                    }
                }
            catch (HibernateException ouch)
                {
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            }

        /**
         * Update an object.
         *
         */
        public InterfaceType update(InterfaceType object)
            {
            logger.debug("update(InterfaceType)");
            try {
                statefull().session().update(
                    object
                    );
                }
            catch (HibernateException ouch)
                {
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            return object ;
            }

        /**
         * Delete an object.
         *
         */
        public void delete(InterfaceType object)
            {
            logger.debug("update(InterfaceType)");
            try {
                statefull().session().delete(
                    object
                    );
                }
            catch (HibernateException ouch)
                {
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            }

        /**
         * Flush changes to the database.
         *
         */
        protected void flush()
            {
            logger.debug("flush()");
            try {
                statefull().session().flush();
                }
            catch (HibernateException ouch)
                {
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            }

        /**
         * Clear the session state, discarding unsaved changes.
         *
         */
        protected void clear()
            {
            logger.debug("clear()");
            try {
                statefull().session().clear();
                }
            catch (HibernateException ouch)
                {
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            }

        /**
         * Select a single object.
         *
         */
        public InterfaceType single(Query query)
            {
            return (InterfaceType) first(query);
            }

        /**
         * Return the first result of a query.
         *
         */
        public ObjectType first(Query query)
            {
            try {
                ScrollableResults results = query.scroll(
                    ScrollMode.FORWARD_ONLY
                    );
                if (results.next())
                    {
                    return (ObjectType) results.get(0);
                    }
                else {
                    return null ;
                    }
                }
            catch (HibernateException ouch)
                {
                throw womble.hibernate().convert(
                    ouch
                    );
                }
            }

        /**
         * Select an Iterable set of objects.
         *
         */
        public Iterable<InterfaceType> iterable(final Query query)
            {
            return new Iterable<InterfaceType>()
                {
                public Iterator<InterfaceType> iterator()
                    {
                    try {
                        return (Iterator<InterfaceType>) query.iterate();
                        }
                    catch (HibernateException ouch)
                        {
                        throw womble.hibernate().convert(
                            ouch
                            );
                        }
                    }
                };
            }
        }

    public HibernateTools<ObjectType> tools()
        {
        return new HibernateTools<ObjectType>();
        }

    public Womble.StateFullNess stateness()
        {
        return womble.hibernate().stateness();
        }

    public Womble.StateFullState statefull()
        {
        return womble.hibernate().statefull();
        }

    public Womble.StateLessState stateless()
        {
        return womble.hibernate().stateless();
        }

    }


