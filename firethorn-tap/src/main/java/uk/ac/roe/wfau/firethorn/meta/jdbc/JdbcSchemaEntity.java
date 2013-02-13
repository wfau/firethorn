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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

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

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;

/**
 *
 *
 */
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcSchemaEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcSchema-select-parent",
            query = "FROM JdbcSchemaEntity WHERE parent = :parent ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "JdbcSchema-select-parent.name",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "JdbcSchema-search-parent.text",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident desc"
            )
        }
    )
public class JdbcSchemaEntity
    extends BaseSchemaEntity<JdbcSchema, JdbcTable>
    implements JdbcSchema
    {
    protected static final String DB_TABLE_NAME = "JdbcSchemaEntity";

    /**
     * Schema factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcSchema>
    implements JdbcSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcSchemaEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public JdbcSchema create(final JdbcResource parent, final String name)
            {
            return this.insert(
                new JdbcSchemaEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcSchema> select(final JdbcResource parent)
            {
            return super.list(
                super.query(
                    "JdbcSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcSchema select(final JdbcResource parent, final String name)
            {
            return super.first(
                super.query(
                    "JdbcSchema-select-parent.name"
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
        public Iterable<JdbcSchema> search(final JdbcResource parent, final String text)
            {
            return super.iterable(
                super.query(
                    "JdbcSchema-search-parent.text"
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
        protected JdbcTable.Factory tables;
        @Override
        public JdbcTable.Factory tables()
            {
            return this.tables;
            }

        @Autowired
        protected JdbcSchema.IdentFactory idents ;
        @Override
        public JdbcSchema.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected JdbcSchema.LinkFactory links;
        @Override
        public JdbcSchema.LinkFactory links()
            {
            return this.links;
            }
        }

    protected JdbcSchemaEntity()
        {
        super();
        }

    protected JdbcSchemaEntity(final JdbcResource resource, final String name)
        {
        super(resource, name);
        this.resource = resource;
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = true // TODO - false
        )
    private JdbcResource resource;
    @Override
    public JdbcResource resource()
        {
        return this.resource;
        }
    /**
     * Test method.
     *
     */
    public void resource(final JdbcResource resource)
        {
        this.resource = resource;
        super.resource(resource);
        this.update();
        }

    @Override
    public JdbcSchema.Tables tables()
        {
        return new JdbcSchema.Tables()
            {
            @Override
            public Iterable<JdbcTable> select()
                {
                return factories().jdbc().tables().select(
                    JdbcSchemaEntity.this
                    );
                }
            @Override
            public JdbcTable select(final String name)
                {
                return factories().jdbc().tables().select(
                    JdbcSchemaEntity.this,
                    name
                    );
                }
            @Override
            public JdbcTable create(final String name)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    name
                    );
                }
            @Override
            public JdbcTable create(final String name, final JdbcTable.JdbcTableType type)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    name,
                    type
                    );
                }
            @Override
            public Iterable<JdbcTable> search(final String text)
                {
                return factories().jdbc().tables().search(
                    JdbcSchemaEntity.this,
                    text
                    );
                }
            };
        }

    @Override
    public String link()
        {
        return factories().jdbc().schemas().links().link(
            this
            );
        }
    }
