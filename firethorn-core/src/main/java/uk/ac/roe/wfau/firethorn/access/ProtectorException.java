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
package uk.ac.roe.wfau.firethorn.access;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 *
 *
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ProtectorException
extends Exception
    {
    /**
     * Serial version ID. 
     *
     */
    private static final long serialVersionUID = 8364245620094508772L;
    
    
    /**
     * Public constructor.
     * 
     */
    public ProtectorException(final Identity identity, final Action action)
        {
        super();
        this.identity = identity;
        this.action = action;
        }

    private Identity identity;
    public Identity identity()
        {
        return this.identity;
        }

    private Action action;    
    public Action action()
        {
        return this.action;
        }
    
    }
