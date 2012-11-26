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
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 *
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
        @NamedQuery(
            name  = "TuesdayJdbcTable-select-parent",
            query = "FROM TuesdayJdbcTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcTable-select-parent.name",
            query = "FROM TuesdayJdbcTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcTable-search-parent.text",
            query = "FROM TuesdayJdbcTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class TuesdayJdbcTableEntity
extends TuesdayBaseTableEntity<TuesdayJdbcTable, TuesdayJdbcColumn>
    implements TuesdayJdbcTable
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcTableEntity";

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayJdbcTable>
    implements TuesdayJdbcTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayJdbcTableEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayJdbcTable create(final TuesdayJdbcSchema parent, final String name)
            {
            return this.insert(
                new TuesdayJdbcTableEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcTable> select(final TuesdayJdbcSchema parent)
            {
            return super.list(
                super.query(
                    "TuesdayJdbcTable-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayJdbcTable select(final TuesdayJdbcSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "TuesdayJdbcTable-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcTable> search(final TuesdayJdbcSchema parent, final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayJdbcTable-search-parent.text"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Autowired
        protected TuesdayJdbcColumn.Factory columns;

        @Override
        public TuesdayJdbcColumn.Factory columns()
            {
            return this.columns;
            }

        @Autowired
        protected TuesdayJdbcTable.IdentFactory identifiers ;

        @Override
        public TuesdayJdbcTable.IdentFactory identifiers()
            {
            return this.identifiers ;
            }
        }
    
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
    public TuesdayJdbcResource resource()
        {
        return this.schema().resource();
        }

    @Override
    public TuesdayOgsaTable<TuesdayJdbcTable, TuesdayJdbcColumn> ogsa()
        {
        return this;
        }

    @Override
    public TuesdayJdbcTable.Columns columns()
        {
        return new TuesdayJdbcTable.Columns()
            {
            @Override
            public Iterable<TuesdayJdbcColumn> select()
                {
                // TODO Auto-generated method stub
                return null;
                }
            @Override
            public TuesdayJdbcColumn select(String name)
                {
                // TODO Auto-generated method stub
                return null;
                }
            };
        }

    @Override
    public String alias()
        {
        // TODO Auto-generated method stub
        return null;
        }
    }
