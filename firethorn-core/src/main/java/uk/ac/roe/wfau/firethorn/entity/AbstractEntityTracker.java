/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder.Worker;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;

/**
 * Abstract base class for EntityListTracker implementations. 
 *
 */
@Slf4j
public abstract class AbstractEntityTracker<EntityType extends NamedEntity>
implements EntityBuilder<EntityType>
    {
    /**
     * Our map of Entity(s) to process.
     * 
     */
    private Map<String, EntityType> todo = new HashMap<String, EntityType>(); 

    @Override
    public Iterable<EntityType> todo()
        {
        return todo.values();
        }
    
    /**
     * Our map of processed Entity(s).
     * 
     */
    private Map<String, EntityType> done = new HashMap<String, EntityType>(); 

    @Override
    public Iterable<EntityType> done()
        {
        return done.values();
        }

    /**
     * Constructor replacement.
     * @see http://stackoverflow.com/a/12206542
     * @param source The initial list of Entity(s) to process.
     * 
     */
    public AbstractEntityTracker<EntityType> init(final Iterable<EntityType> source)
        {
        log.debug("init(Iterable<EntityType>)");
        for (EntityType entity : source)
            {
            log.debug("  entity [{}]", entity.name());
            todo.put(
                entity.name(),
                entity
                );
            }
        return this;
        }
    
    @Override
    public EntityType select(final String name, final Worker<EntityType> handler)
    throws DuplicateEntityException
        {
        log.debug("select(String, Handler<EntityType>)");
        log.debug("  name [{}]", name);
        log.debug("  todo [{}]", todo.size());
        log.debug("  done [{}]", done.size());
        //
        // Check for a duplicate in the done list.
        EntityType entity = done.get(name);
        if (entity != null)
            {
            throw new DuplicateEntityException(
                entity
                );
            }
        //
        // Check for an existing Entity in the todo list.
        entity = todo.get(name);
        if (entity != null)
            {
            //
            // Remove from the todo list.
            todo.remove(
                name
                );
            //
            // Apply the updates.
            handler.update(
                entity
                );
            }
        //
        // Create a new Entity.
        else {
            entity = handler.create(
                name
                );
            }
        //
        // Save in the done list.
        done.put(
            name,
            entity
            );
        //
        // Return the Entity for processing. 
        return entity;
        }

    @Override
    public void finish()
        {
        for (EntityType entity : todo.values())
            {
            finish(
                entity
                );
            }
        }

    /**
     * Finish an un-processed Entity.
     *
     */
    protected abstract void finish(final EntityType entity);

    /**
     * Create a new Entity.
     * 
    protected abstract EntityType create(final String name, final ParamType param)
    throws DuplicateEntityException;
     */

    /**
     * Update an existing Entity.
     * 
    protected abstract void update(final EntityType entity, final ParamType param)
    throws DuplicateEntityException;
     */

    }
