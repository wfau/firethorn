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
package uk.ac.roe.wfau.firethorn.webapp.control;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 * Java Bean interface for an <code>Entity</code>.
 *
 */
public interface EntityBean<EntityType extends Entity>
    {

	/**
	 * Iterable interface.
	 *
	 */
    public static interface Iter<EntityType extends Entity, BeanType extends EntityBean<EntityType>>
    extends Iterable<BeanType>
        {
        }

    /**
     * Access to the wrapped Entity.
     *
     */
    public EntityType entity();

    /**
     * Get the Entity URI.
     *
     */
    public URI getSelf();

    /**
     * Get the Entity type.
     *
     */
    public URI getType();

    /**
     * Get the Entity owner.
     *
     */
    public URI getOwner();

    /**
     * Get the Entity create date.
     *
     */
    public String getCreated();

    /**
     * Get the Entity modified date.
     *
     */
    public String getModified();

    }
