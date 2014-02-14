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
package uk.ac.roe.wfau.firethorn.entity.access;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.access.Action;
import uk.ac.roe.wfau.firethorn.access.ProtectorException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 *
 *
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class EntityProtectorException
extends ProtectorException
    {
    /**
     * Serial version ID. 
     *
     */
    private static final long serialVersionUID = 2997847926699125402L;

    /**
     * Public constructor.
     * 
     */
    public EntityProtectorException(final Identity identity, final Action action, final Entity entity)
        {
        super(identity, action);
        this.entity = entity;
        }

    private Entity entity;
    public Entity entity()
        {
        return this.entity;
        }
    }
