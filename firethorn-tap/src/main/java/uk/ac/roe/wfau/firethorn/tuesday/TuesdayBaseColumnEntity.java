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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
    name = TuesdayBaseColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                TuesdayBaseEntity.DB_NAME_COL,
                TuesdayBaseEntity.DB_PARENT_COL,
                }
            )
        }
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public abstract class TuesdayBaseColumnEntity<ColumnType extends TuesdayBaseColumn<ColumnType>>
extends TuesdayBaseEntity
    implements TuesdayBaseColumn<ColumnType>
    {
    protected static final String DB_TABLE_NAME = "TuesdayBaseColumnEntity";

    protected TuesdayBaseColumnEntity()
        {
        super();
        }

    protected TuesdayBaseColumnEntity(String name)
        {
        super(name);
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String type ;
    @Override
    public String type()
        {
        return this.type;
        }
    @Override
    public void type(String type)
        {
        this.type = type;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer size ;
    @Override
    public Integer size()
        {
        return this.size;
        }

    @Override
    public void size(Integer size)
        {
        this.size = size;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_UCD_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ucd;
    @Override
    public String ucd()
        {
        return this.ucd;
        }

    @Override
    public void ucd(String ucd)
        {
        this.ucd = ucd;
        }

    @Override
    public String alias()
        {
        return "ogsa_column_ident";
        }

    @Override
    public StringBuilder fullname()
        {
        return this.table().fullname().append(".").append(this.name());
        }
    
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayBaseTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayBaseTable<?,ColumnType> parent;
    @Override
    public TuesdayBaseTable<?,ColumnType> table()
        {
        return this.parent;
        }
    @Override
    public TuesdayBaseSchema<?,?> schema()
        {
        return this.table().schema();
        }
    @Override
    public TuesdayBaseResource<?> resource()
        {
        return this.table().resource();
        }

    @Override
    public Linked linked()
        {
        return new Linked()
            {
            @Override
            public Iterable<TuesdayAdqlColumn> select()
                {
                //"SELECT FROM TuesdayAdqlColumn WHERE base = :base"
                return null;
                }
            };
        }

    @Override
    public abstract TuesdayOgsaColumn<?> ogsa();
    
    }
