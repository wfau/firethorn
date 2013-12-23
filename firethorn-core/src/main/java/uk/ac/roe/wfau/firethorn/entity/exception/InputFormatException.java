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

import uk.ac.roe.wfau.firethorn.exception.FirethornUncheckedException;

/**
 * Base class for input format exceptions.
 *
 */
public abstract class InputFormatException
extends FirethornUncheckedException
    {
    /**
     * Default version UID.
     *
     */
    private static final long serialVersionUID = -1L;

    /**
     * Protected constructor, with input and message.
     *
     */
    protected InputFormatException(final String input, final String message)
        {
        this(
            input,
            message,
            null
            );
        }

    /**
     * Protected constructor, with input, message and cause.
     *
     */
    protected InputFormatException(final String input, final String message, final Throwable cause)
        {
        super(
            message,
            cause
            );
        this.input = input ;
        }

    private final String input;

    public String input()
        {
        return this.input;
        }
    }

