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
package uk.ac.roe.wfau.firethorn.tuesday;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayIvoaCatalogEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        }
    )
public class TuesdayIvoaCatalogEntity
    extends TuesdayBaseCatalogEntity
    implements TuesdayIvoaCatalog
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaCatalogEntity";

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayIvoaResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayIvoaResource resource;
    @Override
    public TuesdayIvoaResource resource()
        {
        return this.resource;
        }

    @Override
    public Schemas schemas()
        {
        return new Schemas()
            {
            @Override
            public Iterable<TuesdayIvoaSchema> select()
                {
                // TODO Auto-generated method stub
                return null;
                }
            @Override
            public TuesdayIvoaSchema select(String name)
                {
                // TODO Auto-generated method stub
                return null;
                }
            };
        }
    }
