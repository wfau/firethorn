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
    name = TuesdayBaseTableEntity.DB_TABLE_NAME
    )
@Inheritance(
    strategy = InheritanceType.JOINED
    )
@NamedQueries(
        {
        }
    )
public abstract class TuesdayBaseTableEntity
extends TuesdayBaseNameEntity
    implements TuesdayBaseTable
    {
    protected static final String DB_TABLE_NAME = "TuesdayBaseTableEntity";

    protected static final String DB_TYPE_COL = "" +
    		"" +
    		"" +
    		"type";
    protected static final String DB_SIZE_COL = "size";
    protected static final String DB_UCD_COL  = "ucd";

    protected TuesdayBaseTableEntity()
        {
        super();
        }

    protected TuesdayBaseTableEntity(TuesdayBaseSchema schema, String name)
        {
        super(name);
        this.schema = schema;
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
        return null ;
        }
    @Override
    public String fullname()
        {
        StringBuilder builder = new StringBuilder();
        builder.append(this.schema().fullname());
        builder.append(".");
        builder.append(this.name());
        return builder.toString();
        }

    @Override
    public abstract TuesdayOgsaTable<?> ogsa();

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayBaseSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayBaseSchema schema;
    @Override
    public TuesdayBaseSchema schema()
        {
        return this.schema;
        }
    protected void schema(TuesdayBaseSchema schema)
        {
        this.schema = schema;
        }

    @Override
    public abstract TuesdayBaseResource resource();

    @Override
    public Linked linked()
        {
        return new Linked()
            {
            @Override
            public Iterable<TuesdayAdqlTable> select()
                {
                //"SELECT FROM TuesdayAdqlTable WHERE base = :base"
                return null;
                }
            };
        }
    }
