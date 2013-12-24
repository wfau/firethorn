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
package uk.ac.roe.wfau.firethorn.exception;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 *
 *
 */
public class IllegalStateTransition
    extends FirethornUncheckedException
    {

    /**
     * Serial version UID.
     *
     */
    private static final long serialVersionUID = 5967471376784982558L;

    private final Enum<?> prev ;
    public Enum<?> prev()
        {
        return this.prev;
        }

    private final Enum<?> next ;
    public Enum<?> next()
        {
        return this.next;
        }

    private final Entity entity;
    public Entity entity()
        {
        return this.entity;
        }

    public IllegalStateTransition(final Entity entity, final Enum<?> prev, final Enum<?> next)
        {
        this.entity = entity;
        this.prev = prev ;
        this.next = next ;
        }
    }
