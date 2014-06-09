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

import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;

/**
 * Common interface for building lists of {@link Entity}(s).
 * The builder maintains two lists of {@link Entity}(s),
 * A {@link todo} list that contains the {@link Entity}(s) that have yet to be processed.
 * A {@link done} list that contains the {@link Entity}(s) that have been processed.
 * As each {@link Entity} is processed, it is moved from the todo list to the done list.  
 *
 */
public interface EntityBuilder<EntityType extends NamedEntity, EntityMeta>
    {
    /**
     * A list of Entity(s) that have yet to be processed.
     * 
     */
    public Iterable<EntityType> todo();

    /**
     * A list of Entity(s) that have already been processed.
     * 
     */
    public Iterable<EntityType> done();

    /**
     * Select an Entity for processing.
     * 
     */
    public EntityType select(final String name, final EntityMeta meta)
    throws DuplicateEntityException;

    /**
     * Finish processing the list.   
     *   
     */
    public void finish();

    }
