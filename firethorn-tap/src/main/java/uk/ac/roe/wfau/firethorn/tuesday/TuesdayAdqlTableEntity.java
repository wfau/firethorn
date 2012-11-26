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
    name = TuesdayAdqlTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        }
    )
public class TuesdayAdqlTableEntity
    extends TuesdayBaseTableEntity
    implements TuesdayAdqlTable
    {
    protected static final String DB_TABLE_NAME = "TuesdayAdqlTableEntity";

    protected TuesdayAdqlTableEntity()
        {
        super();
        }

    protected TuesdayAdqlTableEntity(TuesdayBaseTable base, TuesdayAdqlSchema schema, String name)
        {
        super(schema, name);
        this.base = base;
        this.schema = schema;
        }
    
    @Override
    public String name()
        {
        if (super.name() == null)
            {
            return base().name();
            }
        else {
            return super.name();
            }
        }
    @Override
    public String text()
        {
        if (super.text() == null)
            {
            return base().text();
            }
        else {
            return super.text();
            }
        }
    @Override
    public String type()
        {
        if (super.type() == null)
            {
            return base().type();
            }
        else {
            return super.type();
            }
        }
    @Override
    public Integer size()
        {
        if (super.size() == null)
            {
            return base().size();
            }
        else {
            return super.size();
            }
        }
    @Override
    public String ucd()
        {
        if (super.ucd() == null)
            {
            return base().ucd();
            }
        else {
            return super.ucd();
            }
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayAdqlSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    private TuesdayAdqlSchema schema;
    @Override
    public TuesdayAdqlSchema schema()
        {
        return this.schema;
        }
    @Override
    public void schema(TuesdayAdqlSchema schema)
        {
        super.schema(schema);
        this.schema = schema;
        }
    @Override
    public TuesdayAdqlCatalog catalog()
        {
        return this.schema.catalog();
        }
    @Override
    public TuesdayAdqlResource resource()
        {
        return this.schema.resource();
        }

    @Override
    public Columns columns()
        {
        return new Columns()
            {
            @Override
            public Iterable<TuesdayAdqlColumn> select()
                {
                return null;
                }
            @Override
            public TuesdayAdqlColumn select(String name)
                {
                return null;
                }
            };
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayBaseTableEntity.class
        )
    @JoinColumn(
        name = DB_BASE_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayBaseTable base ;
    @Override
    public TuesdayBaseTable base()
        {
        return this.base ;
        }
    @Override
    public TuesdayOgsaTable<?> ogsa()
        {
        return base().ogsa();
        }
    }
