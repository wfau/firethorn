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

import org.hibernate.annotations.Index;
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
    name = TuesdayJdbcSchemaEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayJdbcSchema-select-parent",
            query = "FROM TuesdayJdbcSchemaEntity WHERE parent = :parent ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcSchema-select-parent.name",
            query = "FROM TuesdayJdbcSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcSchema-search-parent.text",
            query = "FROM TuesdayJdbcSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
    )
public class TuesdayJdbcSchemaEntity
    extends TuesdayBaseSchemaEntity<TuesdayJdbcSchema, TuesdayJdbcTable>
    implements TuesdayJdbcSchema
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcSchemaEntity";

    /**
     * Schema factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayJdbcSchema>
    implements TuesdayJdbcSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayJdbcSchemaEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayJdbcSchema create(TuesdayJdbcResource parent, String name)
            {
            return this.insert(
                new TuesdayJdbcSchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcSchema> select(TuesdayJdbcResource parent)
            {
            return super.list(
                super.query(
                    "TuesdayJdbcSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayJdbcSchema select(TuesdayJdbcResource parent, String name)
            {
            return super.first(
                super.query(
                    "TuesdayJdbcSchema-select-parent.name"
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
        public Iterable<TuesdayJdbcSchema> search(TuesdayJdbcResource parent, String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayJdbcSchema-search-parent.text"
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
        protected TuesdayJdbcTable.Factory tables;

        @Override
        public TuesdayJdbcTable.Factory tables()
            {
            return this.tables;
            }

        @Autowired
        protected TuesdayJdbcSchema.IdentFactory identifiers ;

        @Override
        public TuesdayJdbcSchema.IdentFactory identifiers()
            {
            return this.identifiers ;
            }
        }

    protected TuesdayJdbcSchemaEntity()
        {
        super();
        }

    protected TuesdayJdbcSchemaEntity(TuesdayJdbcResource resource, String name)
        {
        super(resource, name);
        this.resource = resource;
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayJdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayJdbcResource resource;
    @Override
    public TuesdayJdbcResource resource()
        {
        return this.resource;
        }

    @Override
    public TuesdayJdbcSchema.Tables tables()
        {
        return new TuesdayJdbcSchema.Tables()
            {
            @Override
            public Iterable<TuesdayJdbcTable> select()
                {
                return factories().jdbc().tables().select(
                    TuesdayJdbcSchemaEntity.this
                    );
                }
            @Override
            public TuesdayJdbcTable select(String name)
                {
                return factories().jdbc().tables().select(
                    TuesdayJdbcSchemaEntity.this,
                    name
                    );
                }
            @Override
            public TuesdayJdbcTable create(String name)
                {
                return factories().jdbc().tables().create(
                    TuesdayJdbcSchemaEntity.this,
                    name
                    );
                }
            @Override
            public TuesdayJdbcTable create(String name, TuesdayJdbcTable.JdbcTableType type)
                {
                return factories().jdbc().tables().create(
                    TuesdayJdbcSchemaEntity.this,
                    name,
                    type
                    );
                }
            @Override
            public Iterable<TuesdayJdbcTable> search(String text)
                {
                return factories().jdbc().tables().search(
                    TuesdayJdbcSchemaEntity.this,
                    text
                    );
                }
            };
        }

    @Override
    public String link()
        {
        return factories().jdbc().schemas().link(
            this
            );
        }
    }
