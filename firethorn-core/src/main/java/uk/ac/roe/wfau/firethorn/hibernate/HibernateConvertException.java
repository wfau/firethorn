/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.hibernate ;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;

/**
 * Exception thrown to indicate a problem occurred when trying to convert an {@link Entity} to the current {@link Thread}.  
 *
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class HibernateConvertException
extends InternalServerErrorException
    {
    /**
     * Serialzable version UID.
     *
     */
	private static final long serialVersionUID = -4383677463253408234L;

	/**
     * Default message for simple constructor.
     *
     */
    public static final String DEFAULT_MESSAGE = "Unable to convert entity to current thread [:ident:]" ;

    /**
     * Create a default message.
     *
     */
    public static String message(final Identifier ident)
        {
        if (ident != null)
            {
            return DEFAULT_MESSAGE.replace(":ident:", ident.toString());
            }
        else {
            return DEFAULT_MESSAGE.replace(":ident:", null);
            }
        }

    /**
     * Public constructor, using default message.
     *
     */
    public HibernateConvertException(final Identifier ident)
        {
        this(
            ident,
            message(
                ident
                ),
            null
            );
        }

    /**
     * Public constructor, with specific message.
     *
     */
    public HibernateConvertException(final Identifier ident, final String message)
        {
        this(
            ident,
            message,
            null
            );
        }

    /**
     * Public constructor, with specific cause.
     *
     */
    public HibernateConvertException(final Identifier ident, final Throwable cause)
        {
        this(
            ident,
            message(
                ident
                ),
            cause
            );
        }

    /**
     * Public constructor, with specific message and cause.
     *
     */
    public HibernateConvertException(final Identifier ident, final String message, final Throwable cause)
        {
        super(
            message,
            cause
            );
        this.ident = ident ;
        }

    private final Identifier ident;

    public Identifier ident()
        {
        return this.ident;
        }
    }

