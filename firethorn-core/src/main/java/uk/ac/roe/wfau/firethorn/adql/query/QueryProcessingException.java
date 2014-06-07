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
package uk.ac.roe.wfau.firethorn.adql.query;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 *
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class QueryProcessingException
    extends Exception
    {

    /**
     * Serial version UID
     *
     */
    private static final long serialVersionUID = -2400544777912750220L;

    /**
     * Public constructor.
     *
     */
    public QueryProcessingException()
        {
        }

    /**
     * Public constructor.
     *
     */
    public QueryProcessingException(String message)
        {
        super(
            message
            );
        }

    /**
     * Public constructor.
     *
     */
    public QueryProcessingException(Throwable cause)
        {
        super(
            cause
            );
        }

    /**
     * Public constructor.
     *
     */
    public QueryProcessingException(String message, Throwable cause)
        {
        super(
            message,
            cause
            );
        }
    }
