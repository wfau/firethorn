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
    name = TuesdayJdbcColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        }
    )
public class TuesdayJdbcColumnEntity
    extends TuesdayBaseColumnEntity
    implements TuesdayJdbcColumn
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcColumnEntity";

    protected TuesdayJdbcColumnEntity() 
        {
        super();
        }

    protected TuesdayJdbcColumnEntity(TuesdayJdbcTable table, String name) 
        {
        super(name);
        this.table = table;
        }

    @Override
    public TuesdayOgsaColumn ogsa()
        {
        return this ;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayJdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayJdbcTable table;
    @Override
    public TuesdayJdbcTable table()
        {
        return this.table;
        }
    @Override
    public TuesdayJdbcSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public TuesdayJdbcCatalog catalog()
        {
        return this.table().catalog();
        }
    @Override
    public TuesdayJdbcResource resource()
        {
        return this.table().resource();
        }
    }
