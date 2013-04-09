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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnInfo;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnType;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;

/**
 *
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcColumn-select-table",
            query = "FROM JdbcColumnEntity WHERE table = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-select-parent",
            query = "FROM JdbcColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-select-parent.name",
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-search-parent.text",
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class JdbcColumnEntity
    extends BaseColumnEntity<JdbcColumn>
    implements JdbcColumn
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = "JdbcColumnEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_TYPE_COL = "jdbctype" ;
    protected static final String DB_JDBC_SIZE_COL = "jdbcsize" ;

    /**
     * Column factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcColumn>
    implements JdbcColumn.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcColumnEntity.class ;
            }

        @Override
        @Deprecated
        @CreateEntityMethod
        public JdbcColumn create(final JdbcTable parent, final String name)
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public JdbcColumn create(final JdbcTable parent, final String name, final int type, final int size)
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    name,
                    type,
                    size
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcColumn> select(final JdbcTable parent)
            {
            return super.list(
                super.query(
                    "JdbcColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcColumn select(final JdbcTable parent, final String name)
        throws NotFoundException
            {
            return super.single(
                super.query(
                    "JdbcColumn-select-parent.name"
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
        public Iterable<JdbcColumn> search(final JdbcTable parent, final String text)
            {
            return super.iterable(
                super.query(
                    "JdbcColumn-search-parent.text"
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
        protected JdbcColumn.IdentFactory idents;
        @Override
        public JdbcColumn.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        protected JdbcColumn.LinkFactory links;
        @Override
        public JdbcColumn.LinkFactory links()
            {
            return this.links;
            }
        }

    protected JdbcColumnEntity()
        {
        super();
        }

    @Deprecated
    protected JdbcColumnEntity(final JdbcTable table, final String name)
        {
        super(table, name);
        this.table = table;
        }

    protected JdbcColumnEntity(final JdbcTable table, final String name, final int type, final int size)
        {
        super(table, name);
        this.table = table;
        this.info().jdbc().type(type);
        this.info().jdbc().size(size);
        }

    @Override
    public JdbcColumn base()
        {
        return this ;
        }
    @Override
    public JdbcColumn root()
        {
        return this ;
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = JdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcTable table;
    @Override
    public JdbcTable table()
        {
        return this.table;
        }
    @Override
    public JdbcSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public JdbcResource resource()
        {
        return this.table().resource();
        }

    @Override
    public String alias()
        {
        return this.name();
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private int jdbctype ;

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private int jdbcsize;

    @Override
    public String link()
        {
        return factories().jdbc().columns().links().link(
            this
            );
        }

    @Override
    public void scanimpl()
        {
        log.debug("scanimpl()");
        }

    @Override
    public JdbcColumn.Info info()
        {
        return new JdbcColumn.Info()
            {
            @Override
            public AdqlColumn.Info.Meta adql()
                {
                return new AdqlColumn.Info.Meta()
                    {
                    @Override
                    public Integer size()
                        {
                        if (JdbcColumnEntity.this.usersize != null)
                            {
                            return JdbcColumnEntity.this.usersize;
                            }
                        else {
                            return JdbcColumnEntity.this.adqlsize;
                            }
                        }

                    @Override
                    public void size(final Integer size)
                        {
                        JdbcColumnEntity.this.usersize = size ;
                        if (size != null)
                            {
                            JdbcColumnEntity.this.adqlsize = size ;
                            }
                        else {
                            JdbcColumnEntity.this.adqlsize = new Integer(
                                JdbcColumnEntity.this.jdbcsize
                                );
                            }
                        }

                    @Override
                    public AdqlColumn.Type type()
                        {
                        if (JdbcColumnEntity.this.usertype != null)
                            {
                            return JdbcColumnEntity.this.usertype;
                            }
                        else {
                            return JdbcColumnEntity.this.adqltype;
                            }
                        }

                    @Override
                    public void type(final AdqlColumn.Type type)
                        {
                        JdbcColumnEntity.this.usertype = type ;
                        if (type != null)
                            {
                            JdbcColumnEntity.this.adqltype = type ;
                            }
                        else {
                            JdbcColumnEntity.this.adqltype = AdqlColumn.Type.jdbc(
                                JdbcColumnEntity.this.jdbctype
                                );
                            }
                        }
                    };
                }

            @Override
            public JdbcColumn.Info.Meta jdbc()
                {
                return new JdbcColumn.Info.Meta()
                    {
                    @Override
                    public int size()
                        {
                        return JdbcColumnEntity.this.jdbcsize;
                        }

                    @Override
                    public void size(final int size)
                        {
                        JdbcColumnEntity.this.jdbcsize = size ;
                        if (JdbcColumnEntity.this.usersize == null)
                            {
                            JdbcColumnEntity.this.adqlsize = new Integer(size);
                            }
                        }

                    @Override
                    public int type()
                        {
                        return JdbcColumnEntity.this.jdbctype;
                        }

                    @Override
                    public void type(final int type)
                        {
                        JdbcColumnEntity.this.jdbctype = type ;
                        if (JdbcColumnEntity.this.usertype == null)
                            {
                            JdbcColumnEntity.this.adqltype = AdqlColumn.Type.jdbc(
                                type
                                );
                            }
                        }
                    };
                }
            };
        }
    }
