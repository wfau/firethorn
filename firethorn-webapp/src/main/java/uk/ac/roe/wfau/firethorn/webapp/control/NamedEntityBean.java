/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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

import uk.ac.roe.wfau.firethorn.entity.NamedEntity;

/**
 * Java Bean interface for a <code>NamedEntity</code>.
 * @todo Remove this interface, just use Beans.
 * 
 */
public interface NamedEntityBean<EntityType extends NamedEntity>
extends EntityBean<EntityType>
    {
    /**
     * The Entity name.
     *
     */
    public String getName();

    /**
     * The Entity description.
     *
     */
    public String getText();

    }
