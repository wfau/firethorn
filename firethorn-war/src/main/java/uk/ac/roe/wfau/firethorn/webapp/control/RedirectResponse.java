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
package uk.ac.roe.wfau.firethorn.webapp.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * This doesn't work - the Spring dispatcher doesn't recognise the response as a ResponseEntity.
 *
 */
public class RedirectResponse<EntityType extends Entity>
extends ResponseEntity<String>
    {

    public RedirectResponse(EntityBean<EntityType> bean)
        {
        this(
            HttpStatus.SEE_OTHER,
            bean
            );
        }

    public RedirectResponse(HttpStatus status, EntityBean<EntityType> bean)
        {
        super(
            bean.getIdent().toString(),
            new RedirectHeader(
                bean
                ),
            status
            );
        }
    }
