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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.access.Protector;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQueryEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.exception.IllegalStateTransition;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;

/**
 * {@link Jdbctable} implementation.
 *
 */
@Slf4j
@Entity
@Access(
    AccessType.FIELD
    )
@Table(
    name = JdbcTableEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = JdbcTableEntity.DB_PARENT_COL
            )
        },
    uniqueConstraints={
        @UniqueConstraint(
            columnNames = {
                JdbcTableEntity.DB_NAME_COL,
                JdbcTableEntity.DB_PARENT_COL
                }
            )
        }
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcTable-select-parent",
            query = "FROM JdbcTableEntity WHERE parent = :parent ORDER BY name asc"
            ),
        @NamedQuery(
            name  = "JdbcTable-select-parent-ident",
            query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (ident = :ident)) ORDER BY name asc"
            ),
        @NamedQuery(
            name  = "JdbcTable-select-parent-name",
            query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc"
            ),
        @NamedQuery(
            name  = "JdbcTable-pending-parent-created",
            query = " FROM" +
                    "    JdbcTableEntity" +
                    " WHERE" +
                    "    ((jdbcstatus = 'CREATED') OR (jdbcstatus = 'UPDATED'))" +
                    " AND" +
                    "    (parent = :parent)" +
                    " AND" +
                    "    (created <= :date)" +
                    " ORDER BY" +
                    "    ident asc"
            )
        }
    )
public class JdbcTableEntity
extends BaseTableEntity<JdbcTable, JdbcColumn>
implements JdbcTable
    {

    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcTableEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_TYPE_COL   = "jdbctype"   ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_STATUS_COL = "jdbcstatus" ;

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_BLUE_QUERY_COL  = "bluequery" ;

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_COUNT_COL = "jdbcrowcount" ;

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_GUESS_COL = "jdbcrowguess" ;

    /**
     * {@link JdbcTable.Builder} implementation.
     *
     */
    public static abstract class Builder
    extends AbstractEntityBuilder<JdbcTable, JdbcTable.Metadata>
    implements JdbcTable.Builder
        {
        public Builder(final Iterable<JdbcTable> source)
            {
            this.init(
                source
                );
            }

        @Override
        protected String name(JdbcTable.Metadata meta)
        throws ProtectionException
            {
            return meta.jdbc().name();
            }

        @Override
        protected void update(final JdbcTable table, final JdbcTable.Metadata meta)
        throws ProtectionException
            {
            table.update(
                meta
                );
            }
        }
   
    /**
     * {@link JdbcTable.NameFactory} implementation.
     * @todo base32 hash of the ident ?
     *
     */
    @Component
    public static class NameFactory
    extends DateNameFactory<JdbcTable>
    implements JdbcTable.NameFactory
        {
        /**
         * The default name prefix, {@value}.
         * 
         */
        protected static final String PREFIX = "JDBC_TABLE";

        @Override
        public String name()
            {
            return datename(
                PREFIX
                );
            }
        }
    
    /**
     * {@link JdbcTable.AliasFactory} implementation.
     *
     */
    @Component
    public static class AliasFactory
    implements JdbcTable.AliasFactory
        {
        /**
         * The default alias prefix, {@value}.
         * 
         */
        protected static final String PREFIX = "JDBC_TABLE_";

        @Override
        public String alias(final JdbcTable entity)
            {
            return PREFIX.concat(
                entity.ident().toString()
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
        public JdbcTable resolve(String alias)
        throws ProtectionException, EntityNotFoundException
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
         * Our {@link JdbcTable.IdentFactory}.
         * 
         */
        @Autowired
        private JdbcTable.IdentFactory idents ;

        /**
         * Our {@link JdbcTable.EntityFactory}.
         * 
         */
        @Autowired
        private JdbcTable.EntityFactory entities;
        
        }

    /**
     * {@link JdbcTable.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends BaseTableEntity.EntityFactory<JdbcSchema, JdbcTable>
    implements JdbcTable.EntityFactory
        {
        @Override
        public Protector protector()
            {
            return new FactoryAdminCreateProtector();
            }

        @Override
        public Class<?> etype()
            {
            return JdbcTableEntity.class ;
            }

        @Override
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema)
        throws ProtectionException
            {
            return this.insert(
                new JdbcTableEntity(
                    schema
                    )
                );
            }
        
        @Override
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema, final JdbcTable.Metadata meta)
        throws ProtectionException
            {
            return this.insert(
                new JdbcTableEntity(
                    schema,
                    meta
                    )
                );
            }

        @Override
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema, final String name, final JdbcType type)
        throws ProtectionException
            {
            return this.insert(
                new JdbcTableEntity(
                    schema,
                    null,
                    name,
                    type
                    )
                );
            }

        @Override
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema, final BlueQuery query)
        throws ProtectionException
            {
            return this.insert(
                new JdbcTableEntity(
                    schema,
                    query,
                    null
                    )
                );
            }
        
        @Override
        @SelectMethod
        public Iterable<JdbcTable> select(final JdbcSchema parent)
        throws ProtectionException
            {
            return super.iterable(
                super.query(
                    "JdbcTable-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectMethod
        public JdbcTable select(final JdbcSchema parent, final Identifier ident)
        throws ProtectionException, IdentifierNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "JdbcTable-select-parent-ident"
                        ).setEntity(
                            "parent",
                            parent
                        ).setSerializable(
                            "ident",
                            ident.value()
                        )
                    );
                }
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate table [{}][{}]", parent.namebuilder().toString(), ident);
                throw new IdentifierNotFoundException(
                    ident,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public JdbcTable select(final JdbcSchema parent, final String name)
        throws ProtectionException, NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "JdbcTable-select-parent-name"
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
                log.debug("Unable to locate table [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
        public JdbcTable search(final JdbcSchema parent, final String name)
        throws ProtectionException
            {
            return super.first(
                super.query(
                    "JdbcTable-select-parent-name"
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
        @SelectMethod
        public Iterable<JdbcTable> pending(final JdbcSchema parent, final DateTime date, final int page)
        throws ProtectionException
            {
            log.debug("pending(JdbcSchema, DateTime) [{}][{}] [{}]", parent.ident(), parent.name(), date);
            return super.iterable(
                page,
                super.query(
                    "JdbcTable-pending-parent-created"
                    ).setEntity(
                        "parent",
                        parent
                        ).setTimestamp(
                            "date",
                            date.toDate()
                            )
                );
            }

        /**
         * Our {@link JdbcTable.OldBuilder} instance.
         * 
         */
        @Autowired
        protected JdbcTable.OldBuilder builder;
        
        /**
         * Our {@link JdbcTable.NameFactory} instance.
         * 
         */
        @Autowired
        protected JdbcTable.NameFactory names;

        }

    /**
     * {@link Entity.EntityServices} implementation.
     * 
     */
    @Slf4j
    @Component
    public static class EntityServices
    implements JdbcTable.EntityServices
        {
        /**
         * Our singleton instance.
         * 
         */
        private static JdbcTableEntity.EntityServices instance ; 

        /**
         * Our singleton instance.
         * 
         */
        public static EntityServices instance()
            {
            return JdbcTableEntity.EntityServices.instance ;
            }

        /**
         * Protected constructor.
         * 
         */
        protected EntityServices()
            {
            }
        
        /**
         * Protected initialiser.
         * 
         */
        @PostConstruct
        protected void init()
            {
            log.debug("init()");
            if (JdbcTableEntity.EntityServices.instance == null)
                {
                JdbcTableEntity.EntityServices.instance = this ;
                }
            else {
                log.error("Setting instance more than once");
                throw new IllegalStateException(
                    "Setting instance more than once"
                    );
                }
            }
        
        @Autowired
        private JdbcTable.IdentFactory idents;
        @Override
        public JdbcTable.IdentFactory idents()
            {
            return this.idents;
            }

        @Autowired
        private JdbcTable.LinkFactory links;
        @Override
        public JdbcTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        private JdbcTable.NameFactory names;
        @Override
        public JdbcTable.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        private JdbcTable.EntityFactory entities;
        @Override
        public JdbcTable.EntityFactory entities()
            {
            return this.entities;
            }

        @Autowired
		private JdbcTable.AliasFactory aliases;
		@Override
		public JdbcTable.AliasFactory aliases()
			{
			return this.aliases;
			}

        @Autowired
        protected JdbcColumn.EntityFactory columns;
        @Override
        public JdbcColumn.EntityFactory columns()
            {
            return this.columns;
            }
        }

    @Override
    protected JdbcTable.EntityFactory factory()
        {
        log.debug("factory()");
        return JdbcTableEntity.EntityServices.instance().entities() ; 
        }

    @Override
    protected JdbcTable.EntityServices services()
        {
        log.debug("services()");
        return JdbcTableEntity.EntityServices.instance() ; 
        }

    @Override
    public String link()
        {
        return services().links().link(
            this
            );
        }

    @Override
    public String alias()
    throws ProtectionException
        {
        return services().aliases().alias(
            this
            );
        }
    
    /**
     * Protected constructor.
     *
     */
    protected JdbcTableEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcTableEntity(final JdbcSchema schema)
        {
        this(
            schema,
            null,
            null,
            JdbcType.TABLE
            );
        }
         
    /**
     * Protected constructor.
     *
     */
    protected JdbcTableEntity(final JdbcSchema schema, final JdbcTable.Metadata meta)
    throws ProtectionException
        {
        this(
            schema,
            null,
            meta.jdbc().name(),
            meta.jdbc().type()
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
    protected JdbcTableEntity(final JdbcSchema schema, final String name)
        {
        this(
            schema,
            null,
            name,
            JdbcType.TABLE,
            null,
            null
            );
        }

    /**
     * Protected constructor.
     *
     */
    public JdbcTableEntity(final JdbcSchema schema, final BlueQuery query, final String name)
        {
        this(
            schema,
            query,
            name,
            JdbcType.TABLE,
            null,
            null
            );
        }

    /**
     * Protected constructor.
     *
     */
    public JdbcTableEntity(final JdbcSchema schema, final BlueQuery query, final String name, final JdbcType type)
        {
        this(
            schema,
            query,
            name,
            type,
            null,
            null
            );
        }

    /**
     * Protected constructor.
     *
     */
    public JdbcTableEntity(final JdbcSchema schema, final BlueQuery query, final String name, final JdbcType type, final Long rowcount, final Long rowguess)
        {
        super(
            schema,
            name
            );
        log.debug("JdbcTableEntity [{}][{}][{}]", schema.name(), this.name(), this.created());
        
        this.bluequery = query;
        this.schema = schema;

        this.jdbcrowcount = rowcount;
        this.jdbcrowguess = rowguess;

        this.jdbctype   = type ;
        this.jdbcstatus = JdbcTable.TableStatus.CREATED;
        this.adqlstatus = AdqlTable.TableStatus.CREATED;

        }

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcSchemaEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcSchema schema;
    @Override
    public JdbcSchema schema()
        {
        return this.schema;
        }
    @Override
    public JdbcResource resource()
        {
        return this.schema().resource();
        }

    @Override
    public JdbcTable base()
        {
        return self();
        }
    @Override
    public JdbcTable root()
        {
        return self();
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_JDBC_COUNT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected Long jdbcrowcount = null ;
    protected Long jdbcrowcount()
    throws ProtectionException
        {
        if (this.jdbcrowcount != null)
            {
            return this.jdbcrowcount;
            }
        else {
            return this.jdbcrowguess();
            }
        }
    protected void jdbcrowcount(final Long count)
    throws ProtectionException
        {
        this.jdbcrowcount = count;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_JDBC_GUESS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    protected Long jdbcrowguess = null ;
    protected Long jdbcrowguess()
    throws ProtectionException
        {
        if (this.jdbcrowguess != null)
            {
            return this.jdbcrowguess;
            }
        else {
            return EMPTY_COUNT_VALUE;
            }
        }
    protected void jdbcrowguess(final Long count)
    throws ProtectionException
        {
        this.jdbcrowguess = count;
        }

    @Override
    public Long rowcount()
    throws ProtectionException
        {
        if (this.adqlrowcount != null)
            {
            return this.adqlrowcount();
            }
        else {
            return this.jdbcrowcount();
            }
        }
    
    @Override
    public JdbcTable.Columns columns()
    throws ProtectionException
        {
        log.debug("columns() for [{}][{}]", ident(), namebuilder());
        scan();
        return new JdbcTable.Columns()
            {
            @Override
            public Iterable<JdbcColumn> select()
            throws ProtectionException
                {
                return factories().jdbc().columns().entities().select(
                    JdbcTableEntity.this
                    );
                }

            @Override
            public JdbcColumn search(final String name)
            throws ProtectionException
                {
                return factories().jdbc().columns().entities().search(
                    JdbcTableEntity.this,
                    name
                    );
                }

            @Override
            public JdbcColumn select(final String name)
            throws ProtectionException, NameNotFoundException
                {
                return factories().jdbc().columns().entities().select(
                    JdbcTableEntity.this,
                    name
                    );
                }

            @Override
            public JdbcColumn create(final JdbcColumn.Metadata meta)
            throws ProtectionException
                {
                return factories().jdbc().columns().entities().create(
                    JdbcTableEntity.this,
                    meta
                    );
                }

            @Override
            public JdbcColumn create(final String name, final JdbcColumn.JdbcType type, final Integer size)
            throws ProtectionException
                {
                return factories().jdbc().columns().entities().create(
                    JdbcTableEntity.this,
                    name,
                    type,
                    size
                    );
                }

            @Override
            public JdbcColumn select(final Identifier ident)
            throws ProtectionException, IdentifierNotFoundException
                {
                return factories().jdbc().columns().entities().select(
                    JdbcTableEntity.this,
                    ident
                    );
                }

            @Override
            public JdbcColumn.Builder builder()
            throws ProtectionException
                {
                return new JdbcColumnEntity.Builder(this.select())
                    {
                    @Override
                    protected JdbcColumn create(final JdbcColumn.Metadata meta)
                    throws ProtectionException, DuplicateEntityException
                        {
                        return factories().jdbc().columns().entities().create(
                            JdbcTableEntity.this,
                            meta
                            );
                        }
                    };
                }
            };
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_JDBC_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private JdbcTable.JdbcType jdbctype ;
    protected JdbcTable.JdbcType jdbctype()
        {
        return this.jdbctype;
        }
    protected void jdbctype(final JdbcTable.JdbcType type)
        {
        this.jdbctype = type;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_JDBC_STATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private JdbcTable.TableStatus jdbcstatus ;
    protected JdbcTable.TableStatus jdbcstatus()
        {
        if (this.jdbcstatus != null)
            {
            return this.jdbcstatus;
            }
        else {
            return JdbcTable.TableStatus.UNKNOWN;
            }
        }
    protected void jdbcstatus(final JdbcTable.TableStatus next)
    throws ProtectionException
        {
        final JdbcTable.TableStatus prev = this.jdbcstatus; 
        log.debug("jdbcstatus() [{}]->[{}]",
             prev,
             next
             );
        switch(prev)
            {
            case CREATED:
                switch(next)
                    {
                    case DELETED:
                        jdbcdelete();
                        break ;

                    case DROPPED:
                        jdbcdrop();
                        break ;

                    case CREATED:
                        break ;
                    case UPDATED:
                        this.jdbcstatus = TableStatus.UPDATED ;
                        break ;

                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            jdbcstatus(),
                            next
                            );
                    }
                break ;

            case UPDATED:
                switch(next)
                    {
                    case DELETED:
                        jdbcdelete();
                        break ;
    
                    case DROPPED:
                        jdbcdrop();
                        break ;
    
                    case UPDATED:
                        break ;
    
                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            jdbcstatus(),
                            next
                            );
                    }
                break ;

            case DELETED:
                switch(next)
                    {
                    case DROPPED:
                        jdbcdrop();
                        break ;
    
                    case DELETED:
                        break ;
    
                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            jdbcstatus(),
                            next
                            );
                    }
                break ;

            case DROPPED:
                switch(next)
                    {
                    case DROPPED:
                        break ;
    
                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            jdbcstatus(),
                            next
                            );
                    }
                break ;

            case UNKNOWN:
                switch(next)
                    {
                    case UNKNOWN:
                        break ;
    
                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            jdbcstatus(),
                            next
                            );
                    }
                break ;
    
            default:
                log.error("Unknown TableStatus [{}]", prev);
                break ;

            }
        }

    @Override
    protected void adqlstatus(final AdqlTable.TableStatus next)
    throws ProtectionException
        {
        AdqlTable.TableStatus prev = this.adqlstatus; 
        log.debug("status(AdqlTable.TableStatus)");
        log.debug("  prev [{}]", prev);
        log.debug("  next [{}]", next);
        if (next == AdqlTable.TableStatus.UNKNOWN)
            {
            log.warn("Setting AdqlTable.AdqlStatus to UNKNOWN [{}]", this.ident());
            }

        switch(prev)
            {
            case CREATED:
                switch(next)
                    {
                    case CREATED:
                    case COMPLETED:
                    case TRUNCATED:
                    case UNKNOWN:
                        this.adqlstatus = next ;
                        break ;

                    case DELETED:
                        jdbcdelete();
                        break ;

                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            adqlstatus(),
                            next
                            );
                    }
                break ;

            case COMPLETED:
                switch(next)
                    {
                    case COMPLETED:
                    case UNKNOWN:
                        this.adqlstatus = next ;
                        break ;

                    case DELETED:
                        jdbcdelete();
                        break ;

                    case CREATED:
                    case TRUNCATED:
                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            adqlstatus(),
                            next
                            );
                    }
                break ;

            case TRUNCATED:
                switch(next)
                    {
                    case TRUNCATED:
                    case UNKNOWN:
                        this.adqlstatus = next ;
                        break ;

                    case DELETED:
                        jdbcdelete();
                        break ;

                    case CREATED:
                    case COMPLETED:
                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            adqlstatus(),
                            next
                            );
                    }
                break ;

            case DELETED:
                switch(next)
                    {
                    case DELETED:
                    case UNKNOWN:
                        this.adqlstatus = next ;
                        break ;

                    case CREATED:
                    case COMPLETED:
                    case TRUNCATED:
                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            adqlstatus(),
                            next
                            );
                    }
                break ;

            case UNKNOWN:
                switch(next)
                    {
                    case CREATED:
                    case COMPLETED:
                    case TRUNCATED:
                    case DELETED:
                    case UNKNOWN:
                        this.adqlstatus = next ;
                        break ;

                    default:
                        throw new IllegalStateTransition(
                            JdbcTableEntity.this,
                            adqlstatus(),
                            next
                            );
                    }
                break ;

            default:
                break ;
            }
        }
    
    protected void jdbcdelete()
    throws ProtectionException
        {
        log.debug("jdbcdelete [{}][{}]", this.ident(), this.name());
        this.resource().connection().operator().delete(
            JdbcTableEntity.this
            );
        this.adqlstatus = AdqlTable.TableStatus.DELETED;
        this.jdbcstatus = JdbcTable.TableStatus.DELETED;
        this.jdbcrowguess = EMPTY_COUNT_VALUE;
        this.jdbcrowcount = EMPTY_COUNT_VALUE;
        }

    protected void jdbcdrop()
    throws ProtectionException
        {
        log.debug("jdbcdrop[{}][{}]", this.ident(), this.name());
        this.resource().connection().operator().drop(
            JdbcTableEntity.this
            );
        this.adqlstatus = AdqlTable.TableStatus.DELETED;
        this.jdbcstatus = JdbcTable.TableStatus.DROPPED;
        this.jdbcrowguess = EMPTY_COUNT_VALUE;
        this.jdbcrowcount = EMPTY_COUNT_VALUE;
        }

    @Override
    protected void scanimpl()
    throws ProtectionException
        {
        log.debug("scanimpl() for [{}][{}]", this.ident(), this.namebuilder());
        //
        // Create our metadata scanner.
        JdbcMetadataScanner scanner = resource().connection().scanner();
        //
        // Load our Map of known columns.
        Map<String, JdbcColumn> known = new HashMap<String, JdbcColumn>();
        Map<String, JdbcColumn> matching = new HashMap<String, JdbcColumn>();
        for (JdbcColumn column : factories().jdbc().columns().entities().select(JdbcTableEntity.this))
            {
            log.debug("Caching known column [{}]", column.name());
            known.put(
                column.name(),
                column
                );
            }
        //
        // Process our columns.
        try {
            scan(
                known,
                matching,
                scanner.catalogs().select(
                    schema().catalog()
                    ).schemas().select(
                        schema().schema()
                        ).tables().select(
                            this.name()
                            )
                );
            }
        catch (SQLException ouch)
            {
            log.warn("Exception while fetching table [{}][{}]", this.ident(), ouch.getMessage());
            scanner.handle(ouch);
            }
        catch (MetadataException ouch)
            {
            log.warn("Exception while fetching table [{}][{}]", this.ident(), ouch.getMessage());
            }
        finally {
            scanner.connector().close();
            }
        log.debug("columns() scan done for [{}][{}]", this.ident(), this.namebuilder());
        log.debug("Matching columns [{}]", matching.size());
        log.debug("Listed but not matched [{}]", known.size());
        }

    protected void scan(final Map<String, JdbcColumn> known, final Map<String, JdbcColumn> matching, final JdbcMetadataScanner.Table table)
    throws ProtectionException
        {
        log.debug("scanning table [{}]", (table != null) ? table.name() : null);
        if (table == null)
            {
            log.debug("Null table, skipping scan");
            }
        else {
            try {
                for (JdbcMetadataScanner.Column column : table.columns().select())
                    {
                    scan(
                        known,
                        matching,
                        column
                        );
                    }
                }
            catch (SQLException ouch)
                {
                log.warn("Exception while fetching table columns [{}][{}][{}]", this.ident(), table.name(), ouch.getMessage());
                table.handle(ouch);
                }
            }
        }

    protected void scan(final Map<String, JdbcColumn> existing, final Map<String, JdbcColumn> matching, final JdbcMetadataScanner.Column column)
    throws ProtectionException
        {
        String name = column.name();
        log.debug("Scanning for column [{}]", name);
        //
        // Check for an existing match.
        if (existing.containsKey(name))
            {
            log.debug("Found existing column [{}]", name);
            matching.put(
                name,
                existing.remove(
                    name
                    )
                );            
            }
        //
        // No match, so create a new one.
        else {
            log.debug("Creating new column [{}]", name);
            matching.put(
                name,
                factories().jdbc().columns().entities().create(
                    JdbcTableEntity.this,
                    name,
                    column.type(),
                    column.strlen()
                    )
                );
            }
        }

    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = BlueQueryEntity.class
        )
    @JoinColumn(
        name = DB_BLUE_QUERY_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private BlueQuery bluequery;
    @Override
    public BlueQuery bluequery()
        {
        return this.bluequery;
        }
    
    protected JdbcTable.Metadata.Jdbc jdbcmeta()
        {
        return new JdbcTable.Metadata.Jdbc()
            {
            @Override
            public String name()
            throws ProtectionException
                {
                return JdbcTableEntity.this.name();
                }

            @Override
            public Long rowcount()
            throws ProtectionException
                {
                return JdbcTableEntity.this.jdbcrowcount();
                }

            @Override
            public void rowcount(final Long rowcount)
            throws ProtectionException
                {
                JdbcTableEntity.this.jdbcrowcount(
                    rowcount
                    );
                }

            @Override
            public Long rowguess()
            throws ProtectionException
                {
                return JdbcTableEntity.this.jdbcrowguess();
                }
            
            @Override
            public JdbcType type()
            throws ProtectionException
                {
                return JdbcTableEntity.this.jdbctype() ;
                }

            @Override
            public void type(final JdbcType type)
            throws ProtectionException
                {
                JdbcTableEntity.this.jdbctype(
                    type
                    );
                }

            @Override
            public JdbcTable.TableStatus status()
            throws ProtectionException
                {
                return JdbcTableEntity.this.jdbcstatus();
                }

            @Override
            public void status(final JdbcTable.TableStatus next)
            throws ProtectionException
                {
                JdbcTableEntity.this.jdbcstatus(
                    next
                    );
                }
            };
        }

    @Override
    public JdbcTable.Metadata meta()
    throws ProtectionException
        {
        return new JdbcTable.Metadata()
            {
            @Override
            public String name()
            throws ProtectionException
                {
                return JdbcTableEntity.this.name();
                }

            @Override
            public Jdbc jdbc()
            throws ProtectionException
                {
                return jdbcmeta();
                }

            @Override
            public Adql adql()
            throws ProtectionException
                {
                return adqlmeta();
                }
            };
        }
    
    @Override
    public void update(final JdbcTable.Metadata meta)
    throws ProtectionException
        {
        // TODO Auto-generated method stub
        }
    }
