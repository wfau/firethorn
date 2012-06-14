/*
 *
 */
package uk.ac.roe.wfau.firethorn.common.entity ;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.CacheMode;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.HibernateException;

import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;
import uk.ac.roe.wfau.firethorn.common.ident.LongIdentifier;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.UpdateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.DeleteAtomicMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.DeleteEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * Generic base class for a persistent Entity Factory.
 *
 */
@Slf4j
@Repository
public abstract class AbstractFactory<EntityType extends Entity>
implements Entity.Factory<EntityType>
    {

    /**
     * Get the class of Entity we manage.
     * Required because we can't get this from the generics at runtime, because ....
     * http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
     * http://stackoverflow.com/questions/2225979/getting-t-class-despite-javas-type-erasure
     *
     */
    public abstract Class etype();

    /**
     * Create an Identifier from a String.
     *
     */
    @Override
    public Identifier ident(final String string)
        {
        return new LongIdentifier(
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
     * Get a named query.
     *
     */
    public Query query(final String name)
        {
        return womble.hibernate().query(
            name
            );
        }

    /**
     * Insert a new Entity into the database.
     *
     */
    @CreateEntityMethod
    public EntityType insert(final EntityType entity)
        {
        log.debug("insert(EntityType)");
        log.debug("  entity [{}]", entity);
        return (EntityType) womble.hibernate().insert(
            entity
            );
        }

    /**
     * Select a specific Entity by Identifier.
     *
     */
    @Override
    @SelectEntityMethod
    public EntityType select(final Identifier ident)
        {
        log.debug("select(Class, Identifier)");
        log.debug("  ident [{}]", (ident != null) ? null : ident.value());
        return (EntityType) womble.hibernate().select(
            etype(),
            ident
            );
        }

    /**
     * Update an Entity.
     *
     */
    @UpdateEntityMethod
    public EntityType update(final EntityType entity)
        {
        log.debug("update(EntityType)");
        log.debug("  entity [{}]", entity);
        if (etype().isInstance(entity))
            {
            return (EntityType) womble.hibernate().update(
                (EntityType) entity
                );
            }
        else {
            log.error(
                "Update not supported for [" + entity.getClass().getName() + "]"
                );
            throw new IllegalArgumentException(
                "Update not supported for [" + entity.getClass().getName() + "]"
                );
            }
        }

    /**
     * Delete an Entity.
     *
     */
    @DeleteEntityMethod
    public void delete(final EntityType entity)
        {
        log.debug("delete(EntityType)");
        log.debug("  entity [{}]", entity);
        if (etype().isInstance(entity))
            {
            womble.hibernate().delete(
                (EntityType) entity
                );
            }
        else {
            log.error(
                "Delete not supported for [" + entity.getClass().getName() + "]"
                );
            throw new IllegalArgumentException(
                "Delete not supported for [" + entity.getClass().getName() + "]"
                );
            }
        }

    /**
     * Flush changes to the database.
     *
     */
    protected void flush()
        {
        womble.hibernate().flush();
        }

    /**
     * Clear the session state, discarding unsaved changes.
     *
     */
    protected void clear()
        {
        womble.hibernate().clear();
        }

    /**
     * Select a single object.
     *
     */
    @SelectEntityMethod
    public EntityType single(final Query query)
        {
        return (EntityType) womble.hibernate().single(
            query
            );
        }

    /**
     * Return the first result of a query.
     *
     */
    @SelectEntityMethod
    public EntityType first(final Query query)
        {
        return (EntityType) womble.hibernate().first(
            query
            );
        }

    /**
     * Select an Iterable set of objects.
     *
     */
    @SelectEntityMethod
    public Iterable<EntityType> iterable(final Query query)
        {
        return new Iterable<EntityType>()
            {
            @SelectEntityMethod
            public Iterator<EntityType> iterator()
                {
                try {
                    return (Iterator<EntityType>) query.iterate();
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


