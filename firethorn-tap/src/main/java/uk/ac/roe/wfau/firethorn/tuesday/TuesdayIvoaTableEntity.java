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
    name = TuesdayIvoaTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayIvoaTable-select-parent",
            query = "FROM TuesdayIvoaTableEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayIvoaTable-select-parent.name",
            query = "FROM TuesdayIvoaTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayIvoaTable-search-parent.text",
            query = "FROM TuesdayIvoaTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class TuesdayIvoaTableEntity
    extends TuesdayBaseTableEntity<TuesdayIvoaTable, TuesdayIvoaColumn>
    implements TuesdayIvoaTable
    {
    protected static final String DB_TABLE_NAME = "TuesdayIvoaTableEntity";

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayIvoaTable>
    implements TuesdayIvoaTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayIvoaTableEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayIvoaTable create(final TuesdayIvoaSchema parent, final String name)
            {
            return this.insert(
                new TuesdayIvoaTableEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayIvoaTable> select(final TuesdayIvoaSchema parent)
            {
            return super.list(
                super.query(
                    "TuesdayIvoaTable-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayIvoaTable select(final TuesdayIvoaSchema parent, final String name)
            {
            return super.first(
                super.query(
                    "TuesdayIvoaTable-select-parent.name"
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
        public Iterable<TuesdayIvoaTable> search(final TuesdayIvoaSchema parent, final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayIvoaTable-search-parent.text"
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
        protected TuesdayIvoaColumn.Factory columns;

        @Override
        public TuesdayIvoaColumn.Factory columns()
            {
            return this.columns;
            }

        @Autowired
        protected TuesdayIvoaTable.IdentFactory identifiers ;

        @Override
        public TuesdayIvoaTable.IdentFactory identifiers()
            {
            return this.identifiers ;
            }
        }
    
    protected TuesdayIvoaTableEntity()
        {
        super();
        }

    protected TuesdayIvoaTableEntity(TuesdayIvoaSchema schema, String name)
        {
        super(schema, name);
        this.schema = schema;
        }

    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayIvoaSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayIvoaSchema schema;
    @Override
    public TuesdayIvoaSchema schema()
        {
        return this.schema;
        }
    @Override
    public TuesdayIvoaResource resource()
        {
        return this.schema().resource();
        }

    @Override
    public TuesdayOgsaTable<TuesdayIvoaTable, TuesdayIvoaColumn> ogsa()
        {
        return this;
        }

    @Override
    public TuesdayIvoaTable.Columns columns()
        {
        return new TuesdayIvoaTable.Columns()
            {
            @Override
            public Iterable<TuesdayIvoaColumn> select()
                {
                return factories().ivoa().columns().select(
                    TuesdayIvoaTableEntity.this
                    );
                }

            @Override
            public TuesdayIvoaColumn select(String name)
                {
                return factories().ivoa().columns().select(
                    TuesdayIvoaTableEntity.this,
                    name
                    );
                }
            };
        }
    
    @Override
    public String alias()
        {
        // TODO Auto-generated method stub
        return null;
        }

    @Override
    public String link()
        {
        // TODO Auto-generated method stub
        return null;
        }
    }
