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
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Parent;
import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.NameFormatException;
import uk.ac.roe.wfau.firethorn.identity.Identity;

/**
 *
 *
 */
@Access(
    AccessType.FIELD
    )
@Embeddable
@MappedSuperclass
public abstract class FridayAbstractComponent
implements Entity
    {
    @Parent
    protected Entity entity ;
    protected Entity getEntity()
        {
        return this.entity ;
        }
    protected void setEntity(Entity entity)
        {
        this.entity = entity ;
        }

    @Override
    public String link()
        {
        return entity.link();
        }

    @Override
    public Identifier ident()
        {
        return entity.ident();
        }

    @Override
    public Identity owner()
        {
        return entity.owner();
        }

    @Override
    public DateTime created()
        {
        return entity.created();
        }

    @Override
    public DateTime modified()
        {
        return entity.modified();
        }

    @Override
    public void update()
        {
        entity.update();
        }
    @Override
    public void delete()
        {
        entity.delete();
        }

    @Column(
        unique = false,
        nullable = true,
        updatable = true
        )
    private String name ;

    @Override
    public String name()
        {
        return this.name;
        }
    @Override
    public void name(String name)
    throws NameFormatException
        {
        this.name = name ;
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
