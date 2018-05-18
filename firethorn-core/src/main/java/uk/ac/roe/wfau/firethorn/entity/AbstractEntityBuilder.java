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
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 * Abstract base class for {@link EntityBuilder} implementations. 
 *
 */
@Slf4j
public abstract class AbstractEntityBuilder<EntityType extends NamedEntity, EntityParam>
implements EntityBuilder<EntityType, EntityParam>
    {
    /**
     * Resolve an entity name.
     * @param param The {@link EntityParam} containing the name.
     * @return The entity name.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    protected abstract String name(final EntityParam param)
    throws ProtectionException;

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
    public AbstractEntityBuilder<EntityType, EntityParam> init(final Iterable<EntityType> source)
        {
        log.trace("init(Iterable<EntityType, EntityParam>)");
        for (EntityType entity : source)
            {
            log.trace("  entity [{}]", entity.name());
            todo.put(
                entity.name(),
                entity
                );
            }
        return this;
        }
        
    @Override
    public EntityType build(final EntityParam param)
    throws DuplicateEntityException, ProtectionException
        {
        log.trace("build(EntityParam)");
        log.trace("  todo [{}]", todo.size());
        log.trace("  done [{}]", done.size());

        final String name = this.name(param);
        log.trace("  name [{}]", name);
        
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
            update(
                entity,
                param
                );
            }
        //
        // Create a new Entity.
        else {
            entity = create(
                param
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
     * Create a new Entity.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    protected abstract EntityType create(final EntityParam param)
    throws DuplicateEntityException, ProtectionException;

    /**
     * Update an existing Entity.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    protected abstract void update(final EntityType entity, final EntityParam param)
    throws ProtectionException;

    /**
     * Finish an un-processed Entity.
     *
     */
    protected void finish(final EntityType entity)
        {
        }
    }
