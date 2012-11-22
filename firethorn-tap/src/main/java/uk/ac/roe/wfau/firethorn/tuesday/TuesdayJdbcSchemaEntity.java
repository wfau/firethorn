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
    name = TuesdayJdbcSchemaEntity.DB_TABLE_NAME,
    uniqueConstraints={
        @UniqueConstraint(
            name = TuesdayJdbcSchemaEntity.DB_TABLE_NAME + TuesdayBaseNameEntity.DB_PARENT_NAME_IDX,
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
public class TuesdayJdbcSchemaEntity
    extends TuesdayBaseSchemaEntity
    implements TuesdayJdbcSchema
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcSchemaEntity";

    protected TuesdayJdbcSchemaEntity()
        {
        super();
        }

    protected TuesdayJdbcSchemaEntity(TuesdayJdbcCatalog catalog, String name)
        {
        super(name);
        this.catalog = catalog;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayJdbcCatalogEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayJdbcCatalog catalog;
    @Override
    public TuesdayJdbcCatalog catalog()
        {
        return this.catalog;
        }
    @Override
    public TuesdayJdbcResource resource()
        {
        return this.catalog().resource();
        }

    @Override
    public Tables tables()
        {
        return new Tables()
            {
            @Override
            public Iterable<TuesdayJdbcTable> select()
                {
                return null;
                }
            @Override
            public TuesdayJdbcTable select(String name)
                {
                return null;
                }
            };
        }

    @Override
    public String fullname()
        {
        // TODO Auto-generated method stub
        return null;
        }
    }
