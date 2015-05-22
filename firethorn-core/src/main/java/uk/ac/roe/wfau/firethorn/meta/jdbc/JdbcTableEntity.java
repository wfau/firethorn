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

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryEntity;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.exception.IllegalStateTransition;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;

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
    name = JdbcTableEntity.DB_TABLE_NAME,
    indexes={
        @Index(
            columnList = JdbcTableEntity.DB_PARENT_COL
            ),
        @Index(
            columnList = JdbcTableEntity.DB_JDBC_QUERY_COL
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
            name  = "JdbcTable-select-parent.name",
            query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc"
            ),
        @NamedQuery(
            name  = "JdbcTable-search-parent.name",
            query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc"
            ),
        @NamedQuery(
            name  = "JdbcTable-pending-parent.created",
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
    protected static final String DB_JDBC_QUERY_COL  = "adqlquery"  ;

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
            {
            return meta.jdbc().name();
            }

        @Override
        protected void update(final JdbcTable table, final JdbcTable.Metadata meta)
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
        public Class<?> etype()
            {
            return JdbcTableEntity.class ;
            }

        @Override
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema, final JdbcTable.Metadata meta)
            {
            return this.insert(
                new JdbcTableEntity(
                    schema,
                    meta
                    )
                );
            }

        @Override
        @Deprecated
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema, final String name)
            {
            return this.insert(
                new JdbcTableEntity(
                    schema,
                    name
                    )
                );
            }

        @Override
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema, final String name, final JdbcType type)
            {
            return this.insert(
                new JdbcTableEntity(
                    schema,
                    name,
                    type
                    )
                );
            }

        @Override
        @CreateMethod
        public JdbcTable create(final JdbcSchema schema, final AdqlQuery query)
            {
            final JdbcTable table = this.insert(
                new JdbcTableEntity(
                    schema,
                    query,
                    names.name()
                    )
                );
            //
            // Add the select fields.
            for (final AdqlQuery.SelectField field : query.fields())
                {
                final String name = field.name();
                if (name == null)
                    {
                    log.warn("Null field name [{}]", field.name());
                    continue;
                    }
                
                final AdqlColumn.AdqlType type = field.type();
                if (type == null)
                    {
                    log.warn("Null field type [{}]", field.name());
                    continue;
                    }
                if (type == AdqlColumn.AdqlType.UNKNOWN)
                    {
                    log.warn("Unknown field type [{}]", field.name());
                    continue;
                    }
                
                table.columns().create(
                    name,
                    type.jdbctype(),
                    field.arraysize()
                    );
                }

            return builder().create(
                table
                );
            }

        @Override
        @SelectMethod
        public Iterable<JdbcTable> select(final JdbcSchema parent)
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
        public JdbcTable select(final JdbcSchema parent, final String name)
        throws NameNotFoundException
            {
            try {
                return super.single(
                    super.query(
                        "JdbcTable-select-parent.name"
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
            {
            return super.first(
                super.query(
                    "JdbcTable-select-parent.name"
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
        protected JdbcColumn.EntityFactory columns;
        @Override
        public JdbcColumn.EntityFactory columns()
            {
            return this.columns;
            }

        @Autowired
        protected JdbcTable.IdentFactory idents ;
        @Override
        public JdbcTable.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected JdbcTable.NameFactory names;
        @Override
        public JdbcTable.NameFactory names()
            {
            return this.names;
            }
        
        @Autowired
        protected JdbcTable.AliasFactory aliases;
        @Override
        public JdbcTable.AliasFactory aliases()
            {
            return this.aliases;
            }

        @Autowired
        protected JdbcTable.LinkFactory links;
        @Override
        public JdbcTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected JdbcTable.OldBuilder builder;
        public JdbcTable.OldBuilder builder()
            {
            return this.builder;
            }

        /**
         * The physical JDBC factory implementation.
         * @todo This should depend on the local database dialect.
         *
         */
        @Autowired
        private JdbcTable.JdbcDriver jdbc;
        @Override
        public JdbcTable.JdbcDriver driver()
            {
            return this.jdbc;
            }

        @Override
        @SelectMethod
        public Iterable<JdbcTable> pending(final JdbcSchema parent, final DateTime date, final int page)
            {
            log.debug("pending(JdbcSchema, DateTime)");
            return super.iterable(
                page,
                super.query(
                    "JdbcTable-pending-parent.created"
                    ).setEntity(
                        "parent",
                        parent
                        ).setDate(
                            "date",
                            date.toDate()
                            )
                );
            }
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
    protected JdbcTableEntity(final JdbcSchema schema, final JdbcTable.Metadata meta)
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
            JdbcType.TABLE
            );
        }

    /**
     * Protected constructor.
     *
     */
    @Deprecated
    public JdbcTableEntity(final JdbcSchema schema, final String name, final JdbcType type)
        {
        this(
            schema,
            null,
            name,
            type
            );
        }

    /**
     * Protected constructor.
     *
     */
    public JdbcTableEntity(final JdbcSchema schema, final AdqlQuery query, final String name)
        {
        this(
            schema,
            query,
            name,
            JdbcType.TABLE
            );
        }

    /**
     * Protected constructor.
     *
     */
    public JdbcTableEntity(final JdbcSchema schema, final AdqlQuery query, final String name, final JdbcType type)
        {
        super(
            schema,
            name
            );
        this.query  = query;
        this.schema = schema;

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

    @Override
    public JdbcTable.Columns columns()
        {
        log.debug("columns() for [{}][{}]", ident(), namebuilder());
        scantest();
        return new JdbcTable.Columns()
            {
            @Override
            public Iterable<JdbcColumn> select()
                {
                return factories().jdbc().columns().select(
                    JdbcTableEntity.this
                    );
                }

            @Override
            public JdbcColumn search(final String name)
                {
                return factories().jdbc().columns().search(
                    JdbcTableEntity.this,
                    name
                    );
                }

            @Override
            public JdbcColumn select(final String name)
            throws NameNotFoundException
                {
                return factories().jdbc().columns().select(
                    JdbcTableEntity.this,
                    name
                    );
                }
            
            @Override
            public JdbcColumn create(final JdbcColumn.Metadata meta)
                {
                return factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    meta
                    );
                }
            
            @Override
            public JdbcColumn create(final String name, final JdbcColumn.JdbcType type, final Integer size)
                {
                return factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    name,
                    type,
                    size
                    );
                }

            @Override
            public JdbcColumn select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                // TODO Add parent constraint.
                return factories().jdbc().columns().select(
                    ident
                    );
                }

            @Override
            public JdbcColumn.Builder builder()
                {
                return new JdbcColumnEntity.Builder(this.select())
                    {
                    @Override
                    protected JdbcColumn create(final JdbcColumn.Metadata meta)
                        throws DuplicateEntityException
                        {
                        return factories().jdbc().columns().create(
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
        {
        JdbcTable.TableStatus prev = this.jdbcstatus; 
        log.debug("status(JdbcTable.TableStatus)");
        log.debug("  prev [{}]", prev);
        log.debug("  next [{}]", next);
        if (next == JdbcTable.TableStatus.UNKNOWN)
            {
            log.warn("Setting JdbcTable.JdbcStatus to UNKNOWN [{}]", this.ident());
            }
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
                    case UPDATED:
                    case UNKNOWN:
                        this.jdbcstatus = next ;
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
                    case UNKNOWN:
                        this.jdbcstatus = next ;
                        break ;
    
                    case CREATED:
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
                    case UNKNOWN:
                        this.jdbcstatus = next ;
                        break ;
    
                    case CREATED:
                    case UPDATED:
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
                case UNKNOWN:
                    this.jdbcstatus = next ;
                    break ;

                case CREATED:
                case UPDATED:
                case DELETED:
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
                    this.jdbcstatus = next ;
                    break ;

                case CREATED:
                case UPDATED:
                case DELETED:
                case DROPPED:
                default:
                    throw new IllegalStateTransition(
                        JdbcTableEntity.this,
                        jdbcstatus(),
                        next
                        );
                }
            break ;

        default:
            break ;

            }
        }

    @Override
    protected void adqlstatus(final AdqlTable.TableStatus next)
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
//ZRQ
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
        {
        factories().jdbc().tables().driver().delete(
            JdbcTableEntity.this
            );
        this.adqlcount  = EMPTY_COUNT_VALUE;
        this.adqlstatus = AdqlTable.TableStatus.DELETED;
        this.jdbcstatus = JdbcTable.TableStatus.DELETED;
        }

    protected void jdbcdrop()
        {
        factories().jdbc().tables().driver().drop(
            JdbcTableEntity.this
            );
        this.adqlcount  = EMPTY_COUNT_VALUE;
        this.adqlstatus = AdqlTable.TableStatus.DELETED;
        this.jdbcstatus = JdbcTable.TableStatus.DROPPED;
        }

    @Override
    public String link()
        {
        return factories().jdbc().tables().links().link(
            this
            );
        }

    @Override
    public String alias()
        {
        return factories().jdbc().tables().aliases().alias(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        log.debug("columns() scan for [{}][{}]", this.ident(), this.namebuilder());
        //
        // Create our metadata scanner.
        JdbcMetadataScanner scanner = resource().connection().scanner();
        //
        // Load our Map of known columns.
        Map<String, JdbcColumn> known = new HashMap<String, JdbcColumn>();
        Map<String, JdbcColumn> matching = new HashMap<String, JdbcColumn>();
        for (JdbcColumn column : factories().jdbc().columns().select(JdbcTableEntity.this))
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
        {
        log.debug("scanning table [{}]", (table != null) ? table.name() : null);
        if (table == null)
            {
            log.warn("Null table");
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
                table.schema().catalog().scanner().handle(ouch);
                }
            }
        }

    protected void scan(final Map<String, JdbcColumn> existing, final Map<String, JdbcColumn> matching, final JdbcMetadataScanner.Column column)
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
                factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    name,
                    column.type(),
                    column.strlen()
                    )
                );
            }
        }

    // TODO
    // Refactor this as mapped identity ?
    // http://www.codereye.com/2009/04/hibernate-bi-directional-one-to-one.html
    //
    // SQLServer won't allow a unique column to have a null value.
    // http://improvingsoftware.com/2010/03/26/creating-a-unique-constraint-that-ignores-nulls-in-sql-server/
    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlQueryEntity.class
        )
    @JoinColumn(
        name = DB_JDBC_QUERY_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private AdqlQuery query;
    @Override
    public AdqlQuery query()
        {
        return this.query;
        }

    protected JdbcTable.Metadata.Jdbc jdbcmeta()
        {
        return new JdbcTable.Metadata.Jdbc()
            {
            @Override
            public String name()
                {
                return JdbcTableEntity.this.name();
                }

            @Override
            public Long count()
                {
                return adqlcount();
                }
            
            @Override
            public JdbcType type()
                {
                return jdbctype() ;
                }

            @Override
            public void type(final JdbcType type)
                {
                jdbctype(
                    type
                    );
                }

            @Override
            public JdbcTable.TableStatus status()
                {
                return jdbcstatus();
                }

            @Override
            public void status(final JdbcTable.TableStatus next)
                {
                jdbcstatus(
                    next
                    );
                }
            };
        }

    @Override
    public JdbcTable.Metadata meta()
        {
        return new JdbcTable.Metadata()
            {
            @Override
            public String name()
                {
                return JdbcTableEntity.this.name();
                }

            @Override
            public Jdbc jdbc()
                {
                return jdbcmeta();
                }

            @Override
            public Adql adql()
                {
                return adqlmeta();
                }
            };
        }
    
    @Override
    public void update(final JdbcTable.Metadata meta)
        {
        // TODO Auto-generated method stub
        }
    }
