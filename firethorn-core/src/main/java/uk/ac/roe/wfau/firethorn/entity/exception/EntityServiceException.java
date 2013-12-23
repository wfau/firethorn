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
     * Serialzable version UID.
     *
     */
    private static final long serialVersionUID = 3260535328135842568L;

    /**
     * Public constructor.
     *
     */
    public EntityServiceException()
        {
        super();
        }

    /**
     * Public constructor.
     *
     */
    public EntityServiceException(String message)
        {
        super(
            message
            );
        }

    /**
     * Public constructor.
     *
     */
    public EntityServiceException(Throwable cause)
        {
        super(
            cause
            );
        }

    /**
     * Public constructor.
     *
     */
    public EntityServiceException(String message, Throwable cause)
        {
        super(
            message,
            cause
            );
        }
    }
