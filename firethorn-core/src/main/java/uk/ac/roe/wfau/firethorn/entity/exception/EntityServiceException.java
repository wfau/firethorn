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
package uk.ac.roe.wfau.firethorn.entity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.exception.FirethornUncheckedException;

/**
 * Base class for service errors.
 * This should gradually be replaced with more specific and descriptive errors.
 *
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class EntityServiceException
    extends FirethornUncheckedException
    {
    /**
     * Generated serialzable version UID.
     *
     */
    private static final long serialVersionUID = 3260535328135842568L;

    /**
     * Protected constructor.
     *
     */
    protected EntityServiceException()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected EntityServiceException(final String message)
        {
        super(
            message
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected EntityServiceException(final Throwable cause)
        {
        super(
            cause
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected EntityServiceException(final String message, final Throwable cause)
        {
        super(
            message,
            cause
            );
        }
    }
