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

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * Java Bean interface for an <code>Entity</code>.
 *
 */
public interface EntityBean<EntityType extends Entity>
    {
    /**
     * The wrapped Entity.
     *
     */
    public EntityType entity();

    /**
     * The Entity ID.
     *
    public String id();
     */

    /**
     * The Entity URI.
     *
    public URI uri();
     */

    /**
     * The Entity URI.
     *
     */
    public URI getIdent();

    /**
     * The Entity type.
     *
     */
    public URI getType();

    /**
     * The Entity name.
     *
     */
    public String getName();

    /**
     * The Entity create date.
     *
     */
    public String getCreated();

    /**
     * The Entity modified date.
     *
     */
    public String getModified();

    }
