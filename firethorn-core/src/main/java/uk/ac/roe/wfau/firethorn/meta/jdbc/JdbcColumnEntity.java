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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
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
            query = "FROM JdbcColumnEntity WHERE table = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-select-parent",
            query = "FROM JdbcColumnEntity WHERE parent = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-select-parent.name",
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcColumn-search-parent.text",
            query = "FROM JdbcColumnEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident asc"
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
     * Alias factory implementation.
     * @todo Move to a separate package.
     *
     */
    @Component
    public static class AliasFactory
    implements JdbcColumn.AliasFactory
        {
        @Override
        public String alias(final JdbcColumn column)
            {
            return "JDBC_".concat(
                column.ident().toString()
                );
            }
        }
    
    /**
     * Column factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractFactory<JdbcColumn>
    implements JdbcColumn.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcColumnEntity.class ;
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

        @CreateEntityMethod
        private JdbcColumn create(final JdbcTable parent, final String name, final JdbcColumn.Type type, final Integer size)
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
        @CreateEntityMethod
        public JdbcColumn create(final JdbcTable parent, final AdqlQuery.SelectField field)
            {
            if (field.jdbc() != null)
                {
                return create(
                    parent,
                    field.name(),
                    field.jdbc()
                    );
                }
            else if (field.adql() != null)
                {
                return create(
                    parent,
                    field.name(),
                    field.adql()
                    );
                }
            else {
                return create(
                    parent,
                    field.name(),
                    field.type().jdbc(),
                    field.arraysize()
                    );
                }
            }

        private JdbcColumn create(final JdbcTable parent, String name, final JdbcColumn template)
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    name,
                    template.meta().jdbc().type(),
                    template.meta().jdbc().size()
                    )
                );
            }

        private JdbcColumn create(final JdbcTable parent, String name, final AdqlColumn template)
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    name,
                    template.meta().adql().type().jdbc(),
                    template.meta().adql().arraysize()
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

        @Autowired
        protected JdbcColumn.AliasFactory aliases;
        @Override
        public JdbcColumn.AliasFactory aliases()
            {
            return this.aliases;
            }
        }

    protected JdbcColumnEntity()
        {
        super();
        }

    protected JdbcColumnEntity(final JdbcTable table, final String name, final int type, final int size)
        {
        this(
            table,
            name,
            JdbcColumn.Type.jdbc(
                type
                ),
            new Integer(
                size
                )
            );
        }

    protected JdbcColumnEntity(final JdbcTable table, final String name, final JdbcColumn.Type type, final Integer size)
        {
        super(table, name);
        this.table    = table;
        this.jdbctype = type ;
        this.jdbcsize = size ;
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
        fetch = FetchType.LAZY,
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

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private JdbcColumn.Type jdbctype ;
    protected JdbcColumn.Type jdbctype()
        {
        return this.jdbctype;
        }
    protected void jdbctype(final JdbcColumn.Type type)
        {
        this.jdbctype = type;
        }
    @Override
    protected AdqlColumn.Type adqltype()
        {
        if (this.jdbctype != null)
            {
            if (this.jdbctype == JdbcColumn.Type.ARRAY)
                {
                return AdqlColumn.Type.UNKNOWN;
                }
            else {
                return this.jdbctype.adql();
                }
            }
        else {
            return null;
            }
        }
    
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_SIZE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer jdbcsize;
    protected Integer jdbcsize()
        {
        return this.jdbcsize;
        }
    protected void jdbcsize(final Integer size)
        {
        this.jdbcsize = size;
        }
    @Override
    protected Integer adqlsize()
        {
        switch (this.jdbctype)
            {
            //
            // Array type.
            case ARRAY :
                return JdbcColumn.Metadata.JdbcMeta.VAR_ARRAY_SIZE;

            // TODO unlimited TEXT size

            //
            // Character types.
            case LONGNVARCHAR :
            case LONGVARCHAR :
            case NVARCHAR :
            case VARCHAR :
            case NCHAR :
            case CHAR :
                return this.jdbcsize ;

            //
            // Date time values
//TODO Are these fixed formats and sizes ?
            case DATE:
            case TIME:
            case TIMESTAMP:
                return this.jdbcsize ;
                
            //
            // Blob types.
            case BLOB:
            case CLOB:
            case NCLOB:
                return this.jdbcsize ;

            //
            // Binary types.
            case BINARY:
            case VARBINARY:
                return this.jdbcsize ;
            
            //
            // Single value types.
            default :
                return JdbcColumn.Metadata.JdbcMeta.NON_ARRAY_SIZE;
            }
        }

    protected JdbcColumn.Metadata.JdbcMeta jdbcmeta()
        {
        return new JdbcColumn.Metadata.JdbcMeta()
            {
            @Override
            public Integer size()
                {
                return jdbcsize();
                }
            @Override
            public JdbcColumn.Type type()
                {
                return jdbctype();
                }
            @Override
            public void size(final Integer size)
                {
                jdbcsize(
                    size
                    );
                }
            @Override
            public void type(final Type type)
                {
                jdbctype(
                    type
                    );
                }
            };
        }

    @Override
    public JdbcColumn.Metadata meta()
        {
        return new JdbcColumn.Metadata()
            {
            @Override
            public AdqlColumn.Metadata.AdqlMeta adql()
                {
                return adqlmeta();
                }

            @Override
            public JdbcColumn.Metadata .JdbcMeta jdbc()
                {
                return jdbcmeta();
                }
            };
        }

    @Override
    public String link()
        {
        return factories().jdbc().columns().links().link(
            this
            );
        }

    @Override
    public String alias()
        {
        return factories().jdbc().columns().aliases().alias(
            this
            );
        }

    @Override
    public void scanimpl()
        {
        log.debug("scanimpl()");
        }
    }
