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
package uk.ac.roe.wfau.firethorn.spring;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 * HibernateThings implementation.
 *
 */
@Slf4j
@Component
public class HibernateThingsImpl
    implements HibernateThings
    {
    /**
     * Our Hibernate SessionFactory.
     *
     */
    @Autowired
    private SessionFactory factory;

    @Override
    public DataAccessException convert(final HibernateException ouch)
        {
        log.error("Hibernate excepion [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
        throw ouch ;
        }

    @Override
    public SessionFactory factory()
        {
        return this.factory;
        }

    @Override
    public Session session()
        {
        return this.factory.getCurrentSession();
        }

    @Override
    public Query query(final String name)
        {
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

    @Override
    public Entity insert(final Entity entity)
        {
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
                log.error("Attempting to insert an entity with ident already set [{}][{}]", entity.getClass().getName(), entity.ident());
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

    @Override
    public Entity select(final Class<?> type, final Identifier ident)
        {
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

    @Override
    public Entity update(final Entity entity)
        {
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

    @Override
    public void delete(final Entity entity)
        {
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

    @Override
    public void flush()
        {
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

    @Override
    public void clear()
        {
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

    @Override
    public Entity single(final Query query)
        {
        return first(
            query
            );
        }

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
            return (Entity) query.uniqueResult();
            /*
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
            */
            }
        catch (final HibernateException ouch)
            {
            throw convert(
                ouch
                );
            }
        }
    }
