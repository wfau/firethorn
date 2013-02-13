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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaColumn;

/**
 *
 *
 */
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
    protected static final String DB_TABLE_NAME = "JdbcColumnEntity";

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
            {
            return super.first(
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

    protected JdbcColumnEntity(final JdbcTable table, final String name)
        {
        super(table, name);
        this.table = table;
        }

    protected JdbcColumnEntity(final JdbcTable table, final String name, final int type, final int size)
        {
        super(table, name);
        this.table = table;
        this.sqltype = type;
        this.sqlsize = size;
        }

    @Override
    public OgsaColumn<?> ogsa()
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

    @Override
    public String link()
        {
        return factories().jdbc().columns().links().link(
            this
            );
        }

    /**
     * Metadata database column name.
     *
     */
    protected static final String SQL_TYPE_COL = "sqltype" ;

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = SQL_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private int sqltype ;
    @Override
    public int sqltype()
        {
        return this.sqltype;
        }
    @Override
    public void sqltype(final int type)
        {
        this.sqltype = type;
        }

    /**
     * Metadata database column name.
     *
     */
    protected static final String SQL_SIZE_COL = "sqlsize" ;

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = SQL_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private int sqlsize;
    @Override
    public int sqlsize()
        {
        return this.sqlsize;
        }
    @Override
    public void sqlsize(final int size)
        {
        this.sqlsize = size;
        }
    }
