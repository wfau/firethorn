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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Index;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityServiceException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.exception.IllegalStateTransition;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable.AdqlStatus;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseNameFactory;
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
            columnList = JdbcTableEntity.DB_ADQL_QUERY_COL
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
            name  = "JdbcTable-select-parent",
            query = "FROM JdbcTableEntity WHERE parent = :parent ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcTable-select-parent.name",
            query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY ident asc"
            ),
        @NamedQuery(
            name  = "JdbcTable-search-parent.text",
            query = "FROM JdbcTableEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY ident asc"
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
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcTableEntity";

    /**
     * Empty count value.
     *
     */
    protected static final Long EMPTY_COUNT_VALUE = new Long(0L);

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_TYPE_COL   = "jdbctype"   ;
    protected static final String DB_JDBC_COUNT_COL  = "jdbccount"  ;
    protected static final String DB_JDBC_STATUS_COL = "jdbcstatus" ;
    protected static final String DB_ADQL_STATUS_COL = "adqlstatus" ;
    protected static final String DB_ADQL_QUERY_COL  = "adqlquery"  ;

    /**
     * Alias factory implementation.
     * @todo Move to a separate package.
     *
     */
    @Component
    public static class AliasFactory
    implements JdbcTable.AliasFactory
        {
        @Override
        public String alias(final JdbcTable table)
            {
            return "JDBC_".concat(
                table.ident().toString()
                );
            }
        }

    /**
     * Name factory implementation.
     * @todo Move to a separate package.
     * @todo Count of tables per query, or per owner
     *
     */
    @Component
    public static class NameFactory
    extends BaseNameFactory<JdbcTable>
    implements JdbcTable.NameFactory
        {
        @Override
        public String name(final AdqlQuery query)
            {
            return name(
                "QUERY_".concat(
                    query.ident().toString()
                    )
                );
            }
        }

    /**
     * Table factory implementation.
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
                    names.name(
                        query
                        )
                    )
                );

            // Add our rowid column
/*
 *
            if (query.rowid() != null)
                {
                table.columns().create(
                    query.rowid(),
                    JdbcColumn.Type.BIGINT
                    );
                }
 * 
 */
            //
            // Add the select fields.
            for (final AdqlQuery.SelectField field : query.fields())
                {
    // Size is confused .... ?
    // Include alias for unsafe names ?
                log.debug("create(SelectField)");
                log.debug("  Name [{}]", field.name());
                log.debug("  Type [{}]", field.type());
                log.debug("  Size [{}]", field.arraysize());
                table.columns().create(
                    field
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
        protected JdbcTable.LinkFactory links;
        @Override
        public JdbcTable.LinkFactory links()
            {
            return this.links;
            }

        @Autowired
        protected JdbcTable.AliasFactory aliases;
        @Override
        public JdbcTable.AliasFactory aliases()
            {
            return this.aliases;
            }

        @Autowired
        protected JdbcTable.NameFactory names;
        @Override
        public JdbcTable.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        protected JdbcTable.Builder builder;
        @Override
        public JdbcTable.Builder builder()
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

    protected JdbcTableEntity()
        {
        super();
        }

    protected JdbcTableEntity(final JdbcSchema schema, final String name)
        {
        this(
            schema,
            null,
            name,
            JdbcType.TABLE
            );
        }

    public JdbcTableEntity(final JdbcSchema schema, final String name, final JdbcType type)
        {
        this(
            schema,
            null,
            name,
            type
            );
        }

    public JdbcTableEntity(final JdbcSchema schema, final AdqlQuery query, final String name)
        {
        this(
            schema,
            query,
            name,
            JdbcType.TABLE
            );
        }

    public JdbcTableEntity(final JdbcSchema schema, final AdqlQuery query, final String name, final JdbcType type)
        {
        super(schema, name);
        this.query  = query;
        this.schema = schema;

        this.jdbctype   = type;
        this.jdbccount  = EMPTY_COUNT_VALUE;
        this.jdbcstatus = JdbcStatus.CREATED;
        this.adqlstatus = AdqlTable.AdqlStatus.CREATED;

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

    /*
     * HibernateCollections
    @OrderBy(
        "name ASC"
        )
    @MapKey(
        name="name"
        )
    @OneToMany(
        fetch   = FetchType.LAZY,
        mappedBy = "table",
        targetEntity = JdbcColumnEntity.class
        )
    private Map<String, JdbcColumn> children = new LinkedHashMap<String, JdbcColumn>();
     *
     */

    @Override
    public JdbcTable.Columns columns()
        {
        log.debug("columns() for [{}]", ident());
        scantest();
        return columnsimpl();
        }

    public JdbcTable.Columns columnsimpl()
        {
        return new JdbcTable.Columns()
            {
            @Override
            public Iterable<JdbcColumn> select()
                {
                return factories().jdbc().columns().select(
                    JdbcTableEntity.this
                    );
                /*
                 * HibernateCollections
                return children.values();
                 *
                 */
                }

            @Override
            public JdbcColumn search(final String name)
                {
                /*
                 * HibernateCollections
                return children.get(name);
                 *
                 */
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
                /*
                 * HibernateCollections
                JdbcColumn result = children.get(name);
                if (result != null)
                    {
                    return result ;
                    }
                else {
                    throw new NotFoundException(
                        name
                        );
                    }
                 *
                 */
                }
            @Override
            public JdbcColumn create(final AdqlQuery.SelectField field)
                {
                final JdbcColumn result = factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    field
                    );
                /*
                 * HibernateCollections
                children.put(
                    result .name(),
                    result
                    );
                 *
                 */
                return result ;
                }
            @Override
            public JdbcColumn create(final String name, final int type, final int size)
                {
                final JdbcColumn result = factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    name,
                    type,
                    size
                    );
                /*
                 * HibernateCollections
                children.put(
                    result .name(),
                    result
                    );
                 *
                 */
                return result ;
                }

            @Override
            public JdbcColumn create(final String name, final JdbcColumn.Type type)
                {
                final JdbcColumn result = factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    name,
                    type
                    );
                /*
                 * HibernateCollections
                children.put(
                    result .name(),
                    result
                    );
                 *
                 */
                return result ;
                }

            @Override
            public void scan()
                {
                log.debug("tables.scan() [{}]", ident());
                JdbcTableEntity.this.scansync();
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
    private JdbcTable.JdbcStatus jdbcstatus ;
    protected JdbcTable.JdbcStatus jdbcstatus()
        {
        if (this.jdbcstatus != null)
            {
            return this.jdbcstatus;
            }
        else {
            return JdbcTable.JdbcStatus.UNKNOWN;
            }
        }

    protected void jdbcstatus(final JdbcTable.JdbcStatus next)
        {
        if (next == JdbcTable.JdbcStatus.UNKNOWN)
            {
            log.warn("Setting JdbcTable.JdbcStatus to UNKNOWN [{}]", this.ident());
            }
        this.jdbcstatus = next ;
        }


    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_ADQL_STATUS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private AdqlTable.AdqlStatus adqlstatus ;
    protected AdqlTable.AdqlStatus adqlstatus()
        {
        if (this.adqlstatus != null)
            {
            return this.adqlstatus ;
            }
        else {
            return AdqlTable.AdqlStatus.UNKNOWN;
            }
        }
    protected void adqlstatus(final AdqlTable.AdqlStatus next)
        {
        if (next == AdqlTable.AdqlStatus.UNKNOWN)
            {
            log.warn("Setting AdqlTable.AdqlStatus to UNKNOWN [{}]", this.ident());
            }
        this.adqlstatus = next;
        }


    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = DB_JDBC_COUNT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Long jdbccount ;
    protected Long jdbccount()
        {
        if (this.jdbccount != null)
            {
            return this.jdbccount;
            }
        else {
            return EMPTY_COUNT_VALUE;
            }
        }
    protected void jdbccount(final Long count)
        {
        this.jdbccount = count;
        }

    protected void jdbcdelete()
        {
        factories().jdbc().tables().driver().delete(
            JdbcTableEntity.this
            );
        adqlstatus(
            AdqlStatus.DELETED
            );
        jdbcstatus(
            JdbcStatus.DELETED
            );
        jdbccount(
            EMPTY_COUNT_VALUE
            );
        }

    protected void jdbcdrop()
        {
        factories().jdbc().tables().driver().drop(
            JdbcTableEntity.this
            );
        adqlstatus(
            AdqlStatus.DELETED
            );
        jdbcstatus(
            JdbcStatus.DROPPED
            );
        jdbccount(
            EMPTY_COUNT_VALUE
            );
        }

    @Override
    public JdbcTable.Metadata meta()
        {
        return new JdbcTable.Metadata()
            {
            @Override
            public JdbcMetadata jdbc()
                {
                return new JdbcMetadata()
                    {
                    @Override
                    public Long count()
                        {
                        return jdbccount();
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
                    public JdbcTable.JdbcStatus status()
                        {
                        return jdbcstatus();
                        }

                    @Override
                    public void status(final JdbcTable.JdbcStatus next)
                        {
                        log.debug("status(JdbcTable.JdbcStatus)");
                        log.debug("  prev [{}]", jdbcstatus());
                        log.debug("  next [{}]", next);

                        switch(jdbcstatus())
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
                                        jdbcstatus(
                                            next
                                            );
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
                                        jdbcstatus(
                                            next
                                            );
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
                                        jdbcstatus(
                                            next
                                            );
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
                                        jdbcstatus(
                                            next
                                            );
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
                                        jdbcstatus(
                                            next
                                            );
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
                    };
                }

            @Override
            public AdqlMetadata adql()
                {
                return new AdqlMetadata()
                    {
                    @Override
                    public Long count()
                        {
                        return jdbccount();
                        }

                    @Override
                    public AdqlTable.AdqlStatus status()
                        {
                        return adqlstatus();
                        }

                    @Override
                    public void status(final AdqlTable.AdqlStatus next)
                        {
                        log.debug("status(AdqlTable.AdqlStatus)");
                        log.debug("  prev [{}]", adqlstatus());
                        log.debug("  next [{}]", next);

                        switch(adqlstatus())
                            {
                            case CREATED:
                                switch(next)
                                    {
                                    case CREATED:
                                    case COMPLETED:
                                    case TRUNCATED:
                                    case UNKNOWN:
                                        adqlstatus(
                                            next
                                            );
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
                                        adqlstatus(
                                            next
                                            );
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
                                        adqlstatus(
                                            next
                                            );
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
                                        adqlstatus(
                                            next
                                            );
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
                                        adqlstatus(
                                            next
                                            );
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
                    };
                }
            };
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
    public void scanimpl()
        {
        log.debug("scanimpl()");
        try {
            final DatabaseMetaData metadata = resource().connection().metadata();
            final JdbcProductType  product  = JdbcProductType.match(
                metadata
                );
            log.debug("JdbcProductType [{}]", product);
            // TODO - fix connection errors
            if (metadata != null)
                {
                //
                // TODO - Check the table actually exists !!

                try {
                    //
                    // Check the table columns.

/*
 * JDBC metadata ...
 * http://docs.oracle.com/javase/7/docs/api/java/sql/DatabaseMetaData.html
 *
 */

                    final ResultSet columns = metadata.getColumns(
                        this.schema().catalog(),
                        this.schema().schema(),
                        this.name(),
                        null
                        );
                    while (columns.next())
                        {
                        final String ccname  = columns.getString(JdbcTypes.JDBC_META_TABLE_CAT);
                        final String csname  = columns.getString(JdbcTypes.JDBC_META_TABLE_SCHEM);
                        final String ctname  = columns.getString(JdbcTypes.JDBC_META_TABLE_NAME);
                        final String colname = columns.getString(JdbcTypes.JDBC_META_COLUMN_NAME);
                        final JdbcColumn.Type coltype = JdbcColumn.Type.jdbc(
                            columns.getInt(
                                JdbcTypes.JDBC_META_COLUMN_TYPE_TYPE
                                )
                            );
                        final Integer colsize = new Integer(
                            columns.getInt(
                                JdbcTypes.JDBC_META_COLUMN_SIZE
                                )
                            );

                        log.debug(
                            "Found column [{}][{}][{}][{}][{}][{}]",
                            new Object[]{
                                ccname,
                                csname,
                                ctname,
                                colname,
                                coltype,
                                colsize
                                }
                            );

                        // TODO Remove the try/catch
                        // Use search() and (column != null)

                        // Might be faster to load all the existing columns into a HashMap indexed by name ?
                        // One JDBC call rather than many.
                        try {
                            final JdbcColumn column = columnsimpl().select(
                                colname
                                );
                            //
                            // Update the column ..
                            // TODO How do we handle changes to column types ?
                            column.meta().jdbc().size(
                                colsize
                                );
                            column.meta().jdbc().type(
                                coltype
                                );
                            }
                        catch (final EntityNotFoundException ouch)
                            {
                            columnsimpl().create(
                                colname,
                                coltype.sqltype(),
                                colsize.intValue()
                                );
                            }
                        }
        //
        // TODO
        // Reprocess the list disable missing ones ...
        //
                    }
                catch (final SQLException ouch)
                    {
                    log.warn("Exception reading JDBC table metadata [{}]", ouch.getMessage());
                    throw resource().connection().translator().translate(
                        "Exception reading JDBC table metadata",
                        null,
                        ouch
                        );
                    }
                }
            }
        catch (final MetadataException ouch)
            {
            log.warn("Exception while reading JdbcSchema metadata [{}]", ouch.getMessage());
            throw new EntityServiceException(
                "Exception while reading JdbcSchema metadata",
                ouch
                );
            }
        finally {
            resource().connection().close();
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
        name = DB_ADQL_QUERY_COL,
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
    }
