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
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;

@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayAdqlResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        }
    )
public class TuesdayAdqlResourceEntity
extends TuesdayBaseNameEntity
    implements TuesdayAdqlResource
    {
    protected static final String DB_TABLE_NAME = "TuesdayAdqlResourceEntity";

    @Override
    public Schemas schemas()
        {
        return new Schemas()
            {
            @Override
            public Iterable<TuesdayAdqlSchema> select()
                {
                return null;
                }
            @Override
            public TuesdayAdqlSchema select(String name)
                {
                return null;
                }
            };
        }
    }
