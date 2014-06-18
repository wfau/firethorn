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
package uk.ac.roe.wfau.firethorn.entity.exception ;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.exception.FirethornCheckedException;

/**
 * Base class for duplicate entity exceptions.
 *
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateEntityException
extends FirethornCheckedException
    {
    /**
     * Serialzable version UID.
     *
     */
    private static final long serialVersionUID = 7151237280291616348L;

    /**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Duplicate entity" ;

    /**
     * Public constructor, using default message.
     *
     */
    public DuplicateEntityException(final Entity entity)
        {
        this(
            entity,
            DEFAULT_MESSAGE
            );
        }

    /**
     * Public constructor, with specific message.
     *
     */
    public DuplicateEntityException(final Entity entity, final String message)
        {
        super(
            message
            );
        this.entity = entity ;
        }

    /**
     * The existing entity.
     * 
     */
    private Entity entity ;

    /**
     * The existing entity.
     * 
     */
    public Entity entity()
        {
        return this.entity;
        }
    }

