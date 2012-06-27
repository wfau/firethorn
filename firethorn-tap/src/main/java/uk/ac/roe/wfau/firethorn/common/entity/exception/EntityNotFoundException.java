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
public class EntityNotFoundException
extends RuntimeException
    {

    public EntityNotFoundException()
        {
        this(
            null,
            null
            );
        }

    public EntityNotFoundException(Identifier ident)
        {
        this(
            ident,
            null
            );
        }

    public EntityNotFoundException(Exception cause)
        {
        this(
            null,
            cause
            );
        }

    public EntityNotFoundException(Identifier ident, Exception cause)
        {
        super(
            cause
            );
        this.ident = ident ;
        }

    private Identifier ident;

    public Identifier ident()
        {
        return this.ident;
        }
    }

