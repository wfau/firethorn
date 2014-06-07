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
package uk.ac.roe.wfau.firethorn.entity.access;

import uk.ac.roe.wfau.firethorn.access.Action;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.util.EmptyIterable;

/**
 * Simple implementation of the EntityProtector interface.
 * The Entity owner is allowed to do anything.
 * Anyone else is not allowed to do anything.
 *
 */
public class SimpleEntityProtector
    extends AbstractEntityProtector
    implements EntityProtector
    {
    /**
     * Public constructor.
     * 
     */
    public SimpleEntityProtector(final Entity entity)
        {
        this.entity = entity ;
        }

    /**
     * The entity being protected. 
     * 
     */
    protected Entity entity ;

    /**
     * The entity being protected. 
     * 
     */
    public Entity entity()
        {
        return this.entity;
        }
    
    @Override
    public Iterable<Action> actions()
        {
        return new EmptyIterable<Action>();
        }

    @Override
    public boolean allow(Identity identity, Action action)
        {
        if (identity.equals(entity.owner()))
            {
            return true ;
            }
        else {
            return false ;
            }
        }
    }
