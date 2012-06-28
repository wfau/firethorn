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

import uk.ac.roe.wfau.firethorn.common.entity.Identifier ;

/**
 *
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NameNotFoundException
extends EntityNotFoundException
    {

    /**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Unable to find object for name [:name:]" ;

    /**
     * Create a default message.
     *
     */
    public static String message(String name)
        {
        return DEFAULT_MESSAGE.replace(":name:", name);
        }

    /**
     * Public constructor, using default message.
     *
     */
    public NameNotFoundException(String name)
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
    public NameNotFoundException(String name, String message)
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
    public NameNotFoundException(String name, Throwable cause)
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
    public NameNotFoundException(String name, String message, Throwable cause)
        {
        super(
            message,
            cause
            );
        this.name = name ;
        }

    private String name;

    public String name()
        {
        return this.name;
        }
    }

