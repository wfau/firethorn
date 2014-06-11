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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumnEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;

/**
 *
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcColumnEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = JdbcColumnEntity.DB_PARENT_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                BaseComponentEntity.DB_NAME_COL,
                BaseComponentEntity.DB_PARENT_COL
                }
            )
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
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcColumnEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_TYPE_COL = "jdbctype" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_SIZE_COL = "jdbcsize" ;

    /**
     * {@link EntityBuilder} implementation.
     *
     */
    public static abstract class Builder
    extends AbstractEntityBuilder<JdbcColumn, JdbcColumn.Metadata>
    implements JdbcColumn.Builder
        {
        public Builder(final Iterable<JdbcColumn> source)
            {
            this.init(
                source
                );
            }

        @Override
        protected String name(JdbcColumn.Metadata meta)
            {
            return meta.jdbc().name();
            }

        @Override
        protected void update(final JdbcColumn column, final JdbcColumn.Metadata meta)
            {
            column.update(
                meta
                );
            }
        }
    
    /**
     * {@link JdbcColumn.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements JdbcColumn.AliasFactory
        {
        private static final String PREFIX = "JDBC_";

        @Override
        public String alias(final JdbcColumn column)
            {
            return PREFIX.concat(
                column.ident().toString()
                );
            }

        @Override
        public boolean matches(String alias)
            {
            return alias.startsWith(
                PREFIX
                );
            }
        
        @Override
        public JdbcColumn resolve(String alias)
            throws EntityNotFoundException
            {
            return entities.select(
                idents.ident(
                    alias.substring(
                        PREFIX.length()
                        )
                    )
                );
            }

        /**
         * Our {@link JdbcColumn.IdentFactory}.
         * 
         */
        @Autowired
        private JdbcColumn.IdentFactory idents ;

        /**
         * Our {@link JdbcColumn.EntityFactory}.
         * 
         */
        @Autowired
        private JdbcColumn.EntityFactory entities;
        
        }

    /**
     * {@link JdbcColumn.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<JdbcColumn>
    implements JdbcColumn.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcColumnEntity.class ;
            }

        @Override
        @CreateMethod
        public JdbcColumn create(final JdbcTable parent, final JdbcColumn.Metadata meta)
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    meta
                    )
                );
            }
        
        @Override
        @Deprecated
        @CreateMethod
        public JdbcColumn create(final JdbcTable parent, final String name, final JdbcColumn.Type type)
            {
            return this.insert(
                new JdbcColumnEntity(
                    parent,
                    name,
                    type.sqltype(),
                    type.sqlsize()
                    )
                );
            }

        @Override
        @Deprecated
        @CreateMethod
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

        @Deprecated
        @CreateMethod
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
        @CreateMethod
        public JdbcColumn create(final JdbcTable parent, final AdqlQuery.SelectField field)
            {
            log.debug("JdbcColumn create(JdbcTable, AdqlQuery.SelectField)");
            log.debug("  name [{}]", field.name());
/*
 * 
            if (field.jdbc() != null)
                {
                log.debug("ADQL");
                log.debug("  name [{}]", field.adql().namebuilder());
                log.debug("  type [{}]", field.adql().meta().adql().type());
                log.debug("  type [{}]", field.adql().meta().adql().type().jdbc());
                log.debug("  size [{}]", field.adql().meta().adql().arraysize());

                log.debug("JDBC");
                log.debug("  name [{}]", field.jdbc().namebuilder());
                log.debug("  type [{}]", field.jdbc().meta().jdbc().type());
                log.debug("  size [{}]", field.jdbc().meta().jdbc().size());

                log.debug("TEST");
                log.debug("  type [{}]", field.type().jdbc());
                log.debug("  size [{}]", field.arraysize());

                if (field.type().jdbc() != field.jdbc().meta().jdbc().type())
                    {
                    log.error("Type mismatch");
                    }
                
                // TODO include a base reference.
            	// TODO inherit the metadata
                log.debug("");
                return create(
                    parent,
                    field.name(),
                    field.jdbc()
                    );
                }
            else if (field.adql() != null)
                {
            	// TODO include a base reference.
            	// TODO inherit the metadata
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
 *             
 */
            return create(
                parent,
                field.name(),
                field.type().jdbc(),
                field.arraysize()
                );
            }

        @Override
        @SelectMethod
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
        @SelectMethod
        public JdbcColumn select(final JdbcTable parent, final String name)
        throws NameNotFoundException
            {
            try {
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
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate column [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public JdbcColumn search(final JdbcTable parent, final String name)
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

    /**
     * Protected constructor.
     *
     */
    protected JdbcColumnEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcColumnEntity(final JdbcTable table, final JdbcColumn.Metadata meta)
        {
        this(
            table,
            meta.jdbc().name(),
            meta.jdbc().type(),
            meta.jdbc().size()
            );
        this.update(
            meta
            );
        }
    
    /**
     * Protected constructor.
     *
     */
    @Deprecated
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

    /**
     * Protected constructor.
     *
     */
    protected JdbcColumnEntity(final JdbcTable table, final String name, final JdbcColumn.Type type, final Integer size)
        {
        super(table, name);
        this.table    = table;
        this.jdbctype = type ;
        this.jdbcsize = size ;
        if (this.jdbcsize <= 0)
            {
            this.jdbcsize = resource().jdbcsize(
                type
                );
            }
        }

    @Override
    public JdbcColumn base()
        {
        return self() ;
        }
    @Override
    public JdbcColumn root()
        {
        return self() ;
        }

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
        if (this.adqltype != null)
            {
            return this.adqltype;
            }
        else if (this.jdbctype != null)
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
        if (this.adqlsize != null)
            {
            return this.adqlsize ;
            }
        else {
            // TODO Move the calculation into the JdbcColumn.Type enum.
            switch (this.jdbctype)
                {
                //
                // Array type.
                case ARRAY :
                    return BaseColumn.VAR_ARRAY_SIZE;
    
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
                    return BaseColumn.NON_ARRAY_SIZE;
                }
            }
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
        // TODO Auto-generated method stub
        }
    
    protected JdbcColumn.Metadata.Jdbc jdbcmeta()
        {
        return new JdbcColumn.Metadata.Jdbc()
            {
            @Override
            public String name()
                {
                return JdbcColumnEntity.this.name();
                }
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
            @Override
            public CreateSql create()
                {
                return new CreateSql()
                    {
                    @Override
                    public String name()
                        {
                        // TODO Validate name with product specific rules ?
                        return JdbcColumnEntity.this.name();
                        }

                    @Override
                    public String type()
                        {
                        final StringBuilder builder = new StringBuilder();
                        final JdbcColumn.Type type = jdbctype();
                        final JdbcProductType prod = JdbcColumnEntity.this.resource().connection().type();

                        //
                        // TODO Make JdbcProductType an interface.
                        // Use Spring to load available product types ?
                        // ProductType methods for creating tables and columns.

                        switch(prod)
                            {
                            case MSSQL :
                                switch(type)
                                    {
                                    case DATE :
                                    case TIME :
                                    case TIMESTAMP :
                                        builder.append(
                                            "DATETIME"
                                            );
                                        break ;

                                    default :
                                        builder.append(
                                            jdbctype().name()
                                            );
                                        break ;
                                    }

                                break ;

                            default :
                                builder.append(
                                    jdbctype().name()
                                    );
                                break ;
                            }

                        if (jdbctype().strsize() == BaseColumn.VAR_ARRAY_SIZE)
                            {
                            if (jdbcsize() == BaseColumn.VAR_ARRAY_SIZE)
                                {
                                builder.append("(*)");
                                }
                            else {
                                builder.append("(");
                                builder.append(
                                    jdbcsize()
                                    );
                                builder.append(")");
                                }
                            }
                        return builder.toString();
                        }
                    };
                }
            };
        }

    @Override
    public JdbcColumn.Metadata meta()
        {
        return new JdbcColumn.Metadata()
            {
            @Override
            public String name()
                {
                return JdbcColumnEntity.this.name();
                }

            @Override
            public AdqlColumn.Metadata.Adql adql()
                {
                return adqlmeta();
                }

            @Override
            public JdbcColumn.Metadata .Jdbc jdbc()
                {
                return jdbcmeta();
                }
            };
        }

    @Override
    public void update(final JdbcColumn.Metadata meta)
        {
        if (meta.adql() != null)
            {
            if (meta.adql().text() != null)
                {
                this.text(meta.adql().text());
                }
            //
            //TODO Check the type and size - warn/fail if they have changed ?
            //
            }
        }
    }
