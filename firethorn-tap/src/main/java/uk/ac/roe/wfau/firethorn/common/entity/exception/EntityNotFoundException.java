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
package uk.ac.roe.wfau.firethorn.common.entity.exception ;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class for not found exceptions.
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException
extends Exception
    {

    /**
     * 
     *
     */
    private static final long serialVersionUID = 2424668742190703687L;
    /**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Object not found" ;

    /**
     * Public constructor, using default message.
     *
     */
    public EntityNotFoundException()
        {
        this(
            DEFAULT_MESSAGE,
            null
            );
        }

    /**
     * Public constructor, with specific message.
     *
     */
    public EntityNotFoundException(final String message)
        {
        this(
            message,
            null
            );
        }

    /**
     * Public constructor, using default message.
     *
     */
    public EntityNotFoundException(final Throwable cause)
        {
        this(
            DEFAULT_MESSAGE,
            cause
            );
        }

    /**
     * Public constructor, with specific message.
     *
     */
    public EntityNotFoundException(final String message, final Throwable cause)
        {
        super(
            message,
            cause
            );
        }
    }

