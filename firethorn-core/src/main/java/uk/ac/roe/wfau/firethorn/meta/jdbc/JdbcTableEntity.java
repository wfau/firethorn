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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryEntity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseNameFactory;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;

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
     * Default count value.
     * 
     */
    public static final Long DEFAULT_COUNT_VALUE = new Long(0);
    
    /**
     * Hibernate column mapping.
     *
     */
    protected static final String JDBC_TYPE_COL     = "jdbctype"   ;
    protected static final String JDBC_COUNT_COL    = "jdbccount"  ;
    protected static final String JDBC_STATUS_COL   = "jdbcstatus" ;
    protected static final String ADQL_STATUS_COL   = "adqlstatus" ;
    protected static final String DB_ADQL_QUERY_COL = "adqlquery"  ;

    @Component
    public static class Worker
    	{
    	@Scheduled(fixedDelay=30000)
    	public void something() {
    	    log.debug("Start");

    	    log.debug("Done");
    		}	
    	}

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
    extends BaseTableEntity.Factory<JdbcSchema, JdbcTable>
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
            if (query.rowid() != null)
                {
                table.columns().create(
                    query.rowid(),
                    JdbcColumn.Type.BIGINT
                    );
                }
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
            return super.list(
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
            catch (final NotFoundException ouch)
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
        private JdbcTable.JdbcFactory jdbc;
        @Override
        public JdbcTable.JdbcFactory jdbc()
            {
            return this.jdbc;
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
        this.jdbccount  = DEFAULT_COUNT_VALUE;
        this.jdbcstatus = JdbcStatus.CREATED;
        this.adqlstatus = AdqlTable.AdqlStatus.CREATED;

        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
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
        return this;
        }
    @Override
    public JdbcTable root()
        {
        return this;
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
        name = JDBC_TYPE_COL,
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
        name = JDBC_STATUS_COL,
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
    protected void jdbcstatus(final JdbcTable.JdbcStatus status)
        {
        this.jdbcstatus = status;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = ADQL_STATUS_COL,
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
    protected void adqlstatus(final AdqlTable.AdqlStatus  status)
        {
        this.adqlstatus = status;
        }

    @Basic(fetch = FetchType.EAGER)
    @Column(
        name = JDBC_COUNT_COL,
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
            return DEFAULT_COUNT_VALUE;
            }
        }
    protected void jdbccount(final Long count)
        {
        this.jdbccount = count;
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
                    public void create()
                        {
                        factories().jdbc().tables().jdbc().create(
                            JdbcTableEntity.this
                            );
                        }

                    @Override
                    public void delete()
                        {
                        factories().jdbc().tables().jdbc().delete(
                            JdbcTableEntity.this
                            );
                        }

                    @Override
                    public void drop()
                        {
                        factories().jdbc().tables().jdbc().drop(
                            JdbcTableEntity.this
                            );
                        }

                    @Override
                    public JdbcTable.JdbcStatus status()
                        {
                        return jdbcstatus();
                        }

                    @Override
                    public void status(final JdbcTable.JdbcStatus status)
                        {
                        jdbcstatus(
                            status
                            );
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
                    public void status(AdqlTable.AdqlStatus status)
                        {
                        adqlstatus(
                            status
                            );
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
                        catch (final NotFoundException ouch)
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
                    log.error("Exception reading JDBC table metadata [{}]", ouch.getMessage());
                    throw resource().connection().translator().translate(
                        "Reading JDBC table metadata",
                        null,
                        ouch
                        );
                    }
                }
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
    @Index(
        name=DB_TABLE_NAME + "IndexByAdqlQuery"
        )
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

    @Override
    public boolean exists()
        {
        return true ;
        }
    }
