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

/**
 *
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NameFormatException
extends RuntimeException
    {

    /**
     *
     *
     */
    private static final long serialVersionUID = -5574299798502620244L;
    /**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Invalid name [:name:]" ;

    /**
     * Create a default message.
     *
     */
    public static String message(final String name)
        {
        return DEFAULT_MESSAGE.replace(":name:", name);
        }

    /**
     * Public constructor, using default message.
     *
     */
    public NameFormatException(final String name)
        {
        this(
            name,
            message(
                name
                ),
            null
            );
        }

    /**
     * Public constructor, with specific message.
     *
     */
    public NameFormatException(final String name, final String message)
        {
        this(
            name,
            message,
            null
            );
        }

    /**
     * Public constructor, with specific cause.
     *
     */
    public NameFormatException(final String name, final Throwable cause)
        {
        this(
            name,
            message(
                name
                ),
            cause
            );
        }

    /**
     * Public constructor, with specific message and cause.
     *
     */
    public NameFormatException(final String name, final String message, final Throwable cause)
        {
        super(
            message,
            cause
            );
        this.name = name ;
        }

    private final String name;

    public String name()
        {
        return this.name;
        }
    }

