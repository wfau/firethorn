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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 * A global instance to handle Hibernate related things.
 *
 */
public interface HibernateThings
    {
    /**
     * Wrap a Hibernate Exception.
     *
     */
    public DataAccessException convert(final HibernateException ouch);

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
    public Query query(final String name);

    /**
     * Insert a new Entity.
     *
     */
    public Entity insert(final Entity entity);

    /**
     * Select an existing existing Entity by Identifier.
     *
     */
    public Entity select(final Class<?> type, final Identifier ident);

    /**
     * Refresh (fetch) an Entity from the database.
     *
     */
    public Entity refresh(final Entity entity);

    /**
     * Evict an Entity from the cache.
     *
     */
    public void evict(final Entity entity);

    /**
     * Delete an existing Entity.
     *
     */
    public void delete(final Entity entity);

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
    public Entity single(final Query query);

    /**
     * Select the first Entity from a Query.
     *
     */
    public Entity first(final Query query);

    /**
     * Execute a Runnable within a Session.
     * 
    public void execute(Runnable runnable);
     */
    
    }
