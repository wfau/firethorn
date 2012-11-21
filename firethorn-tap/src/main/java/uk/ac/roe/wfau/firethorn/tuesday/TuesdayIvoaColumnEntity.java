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
import javax.persistence.UniqueConstraint;

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
    name = TuesdayIvoaColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            name = TuesdayIvoaColumnEntity.DB_TABLE_NAME + TuesdayBaseNameEntity.DB_PARENT_NAME_IDX,
            columnNames = {
                TuesdayBaseNameEntity.DB_NAME_COL,
                TuesdayBaseNameEntity.DB_PARENT_COL,
                }
            )
        }
    )
@NamedQueries(
        {
        }
    )
public class TuesdayIvoaColumnEntity
    extends TuesdayOgsaColumnEntity
    implements TuesdayIvoaColumn
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaColumnEntity";

    @Override
    public TuesdayIvoaColumn ogsa()
        {
        return this ;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayIvoaTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayIvoaTable table;
    @Override
    public TuesdayIvoaTable table()
        {
        return this.table;
        }
    @Override
    public TuesdayIvoaSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public TuesdayIvoaResource resource()
        {
        return this.table().resource();
        }

    @Override
    public String fullname()
        {
        StringBuilder builder = new StringBuilder();
        builder.append(resource().name());
        builder.append(".");
        builder.append(schema().name());
        builder.append(".");
        builder.append(table().name());
        builder.append(".");
        builder.append(this.name());
        return builder.toString();
        }
    }
