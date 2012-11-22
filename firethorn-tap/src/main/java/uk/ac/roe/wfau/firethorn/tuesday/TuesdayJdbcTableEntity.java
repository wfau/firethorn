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
        @UniqueConstraint(
            name = TuesdayJdbcTableEntity.DB_TABLE_NAME + TuesdayBaseNameEntity.DB_PARENT_NAME_IDX,
            columnNames = {
                TuesdayBaseNameEntity.DB_NAME_COL,
                TuesdayBaseNameEntity.DB_PARENT_COL,
                }
            )
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = TuesdayJdbcTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        }
    )
public class TuesdayJdbcTableEntity
extends TuesdayBaseTableEntity
    implements TuesdayJdbcTable
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcTableEntity";

    protected TuesdayJdbcTableEntity()
        {
        super();
        }

    protected TuesdayJdbcTableEntity(TuesdayJdbcSchema schema, String name)
        {
        super(schema, name);
        this.schema = schema;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayJdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayJdbcSchema schema;
    @Override
    public TuesdayJdbcSchema schema()
        {
        return this.schema;
        }
    @Override
    public TuesdayJdbcCatalog catalog()
        {
        return this.schema().catalog();
        }
    @Override
    public TuesdayJdbcResource resource()
        {
        return this.schema().resource();
        }

    @Override
    public TuesdayJdbcTable.Columns columns()
        {
        return new TuesdayJdbcTable.Columns()
            {
            @Override
            public Iterable<TuesdayJdbcColumn> select()
                {
                return null;
                }
            @Override
            public TuesdayJdbcColumn select(String name)
                {
                return null;
                }
            };
        }

    @Override
    public String fullname()
        {
        StringBuilder builder = new StringBuilder();
        builder.append(this.resource().name());
        builder.append(".");
        builder.append(this.catalog().name());
        builder.append(".");
        builder.append(this.schema().name());
        builder.append(".");
        builder.append(this.name());
        return builder.toString();
        }

    @Override
    public String alias()
        {
        return null;
        }

    @Override
    public void alias(String alias)
        {
        }

    @Override
    public TuesdayOgsaTable<TuesdayJdbcColumn> ogsa()
        {
        return this;
        }
    }
