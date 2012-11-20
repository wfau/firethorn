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
package uk.ac.roe.wfau.firethorn.friday.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcCatalog;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcResource;
import uk.ac.roe.wfau.firethorn.friday.api.FridayOgsaResource;

/**
 *
 *
 */
@Access(
    AccessType.FIELD
    )
@MappedSuperclass
public class FridayAbstractEntity
extends AbstractEntity
    {

    @Override
    public String link()
        {
        return null;
        }

    @Column(
        unique = false,
        nullable = true,
        updatable = true
        )
    private String text ;
    
    public String text()
        {
        return this.text ;
        }
    }
