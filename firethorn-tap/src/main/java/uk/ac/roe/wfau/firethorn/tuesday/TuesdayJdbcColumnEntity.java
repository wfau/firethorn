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
    name = TuesdayJdbcColumnEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "TuesdayJdbcColumn-select-table",
            query = "FROM TuesdayJdbcColumnEntity WHERE table = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcColumn-select-parent",
            query = "FROM TuesdayJdbcColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcColumn-select-parent.name",
            query = "FROM TuesdayJdbcColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "TuesdayJdbcColumn-search-parent.text",
            query = "FROM TuesdayJdbcColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident desc"
            )
        }
    )
public class TuesdayJdbcColumnEntity
    extends TuesdayBaseColumnEntity<TuesdayJdbcColumn>
    implements TuesdayJdbcColumn
    {
    protected static final String DB_TABLE_NAME = "TuesdayJdbcColumnEntity";

    /**
     * Column factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<TuesdayJdbcColumn>
    implements TuesdayJdbcColumn.Factory
        {

        @Override
        public Class<?> etype()
            {
            return TuesdayJdbcColumnEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public TuesdayJdbcColumn create(final TuesdayJdbcTable parent, final String name)
            {
            return this.insert(
                new TuesdayJdbcColumnEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public TuesdayJdbcColumn create(final TuesdayJdbcTable parent, final String name, final int type, final int size)
            {
            return this.insert(
                new TuesdayJdbcColumnEntity(
                    parent,
                    name,
                    type,
                    size
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<TuesdayJdbcColumn> select(final TuesdayJdbcTable parent)
            {
            return super.list(
                super.query(
                    "TuesdayJdbcColumn-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public TuesdayJdbcColumn select(final TuesdayJdbcTable parent, final String name)
            {
            return super.first(
                super.query(
                    "TuesdayJdbcColumn-select-parent.name"
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
        public Iterable<TuesdayJdbcColumn> search(final TuesdayJdbcTable parent, final String text)
            {
            return super.iterable(
                super.query(
                    "TuesdayJdbcColumn-search-parent.text"
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
        protected TuesdayJdbcColumn.IdentFactory idents ;

        @Override
        public TuesdayJdbcColumn.IdentFactory idents()
            {
            return this.idents ;
            }
        }

    protected TuesdayJdbcColumnEntity() 
        {
        super();
        }

    protected TuesdayJdbcColumnEntity(final TuesdayJdbcTable table, final String name) 
        {
        super(table, name);
        this.table = table;
        }

    protected TuesdayJdbcColumnEntity(final TuesdayJdbcTable table, final String name, final int type, final int size)
        {
        super(table, name);
        this.table = table;
        this.sqltype = type;
        this.sqlsize = size;
        }

    @Override
    public TuesdayOgsaColumn<?> ogsa()
        {
        return this ;
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.EAGER,
        targetEntity = TuesdayJdbcTableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private TuesdayJdbcTable table;
    @Override
    public TuesdayJdbcTable table()
        {
        return this.table;
        }
    @Override
    public TuesdayJdbcSchema schema()
        {
        return this.table().schema();
        }
    @Override
    public TuesdayJdbcResource resource()
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
        // TODO Auto-generated method stub
        return null;
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
    public void sqltype(int type)
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
    public void sqlsize(int size)
        {
        this.sqlsize = size;
        }
    }
