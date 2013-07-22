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
package uk.ac.roe.wfau.firethorn.meta.base;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
    )
public abstract class BaseResourceEntity<SchemaType extends BaseSchema<SchemaType,?>>
    extends BaseComponentEntity
    implements BaseResource<SchemaType>
    {

    protected BaseResourceEntity()
        {
        super();
        }

    protected BaseResourceEntity(final String name)
        {
        super(
            name
            );
        }

    @Override
    public StringBuilder namebuilder()
        {
        return new StringBuilder(
            this.name()
            );
        }

    @Override
    public String ogsaid()
        {
        throw new UnsupportedOperationException(
            "ogsaid not available for BaseResource"
            );
        }
    @Override
    public void ogsaid(final String ogsaid)
        {
        throw new UnsupportedOperationException(
            "ogsaid not available for BaseResource"
            );
        }
    }
