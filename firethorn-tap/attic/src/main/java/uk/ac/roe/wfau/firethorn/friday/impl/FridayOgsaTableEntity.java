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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parent;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.friday.api.FridayAdqlTable;
import uk.ac.roe.wfau.firethorn.friday.api.FridayJdbcSchema;
import uk.ac.roe.wfau.firethorn.friday.api.FridayOgsaResource;
import uk.ac.roe.wfau.firethorn.friday.api.FridayOgsaTable;

/**
 *
 *
 */
@Access(
    AccessType.FIELD
    )
@Embeddable
@MappedSuperclass
public class FridayOgsaTableEntity
extends FridayAbstractComponent
implements FridayOgsaTable
    {
    @Parent
    protected FridayJdbcTableEntity entity ;
    protected FridayJdbcTableEntity getEntity()
        {
        return this.entity ;
        }
    protected void setEntity(FridayJdbcTableEntity entity)
        {
        this.entity = entity ;
        }
    
    @Column(
        unique = false,
        nullable = true,
        updatable = true
        )
    private String alias ;

    public String alias()
        {
        return this.alias ;
        }

    @Override
    public FridayOgsaResource resource()
        {
        return null;
        }

    @Override
    public FridayAdqlTable adql()
        {
        return null;
        }

    }
