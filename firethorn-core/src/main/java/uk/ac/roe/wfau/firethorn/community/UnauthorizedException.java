/*
 *  Copyright (C) 2018 Royal Observatory, University of Edinburgh, UK
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

package uk.ac.roe.wfau.firethorn.community;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.exception.FirethornCheckedException;

/**
 * 
 * An Exception to represent a failed login or authorization attempt.
 * https://stackoverflow.com/a/6937030
 * https://www.loggly.com/blog/http-status-code-diagram/
 * 
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException
extends FirethornCheckedException
    {

    /**
     * Generated version ID.
     * 
     */
    private static final long serialVersionUID = -1147639146683222245L;

    /**
     * Default message for simple constructor, {@value}.
     *
     */
    public static final String DEFAULT_MESSAGE = "Authorization failed" ;

    /**
     * Public constructor, with default message.
     * 
     */
    public UnauthorizedException()
        {
        this(
            DEFAULT_MESSAGE,
            null
            );
        }

    /**
     * Public constructor, with specific message.
     * @param message The Exception message.
     * 
     */
    public UnauthorizedException(String message)
        {
        this(
            message,
            null
            );
        }

    /**
     * Public constructor, with default message.
     * @param cause The original cause.
     * 
     */
    public UnauthorizedException(Throwable cause)
        {
        this(
            DEFAULT_MESSAGE,
            cause
            );
        }

    /**
     * Public constructor, with specific message.
     * @param message The Exception message.
     * @param cause The original cause.
     * 
     */
    public UnauthorizedException(final String message, final Throwable cause)
        {
        super(message, cause);
        }
    }
