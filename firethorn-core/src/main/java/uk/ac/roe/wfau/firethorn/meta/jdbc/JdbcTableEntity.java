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
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryEntity;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTableEntity;

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
    name = JdbcTableEntity.DB_TABLE_NAME,
    uniqueConstraints={
        }
    )
@Inheritance(
    strategy = InheritanceType.JOINED
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
    protected static final String DB_TABLE_NAME = "JdbcTableEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String JDBC_TYPE_COL = "jdbctype" ;
    protected static final String DB_ADQL_QUERY_COL = "adqlquery" ;

    /**
     * Alias factory implementation.
     *
     */
    @Repository
    public static class AliasFactory
    implements JdbcTable.AliasFactory
        {
        /**
         * The alias prefix for this type.
         *
         */
        protected static final String PREFIX = "JDBC_" ;

        @Override
        public String alias(final JdbcTable table)
            {
            return PREFIX + table.ident();
            }
        }

    /**
     * Table factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends BaseTableEntity.Factory<JdbcSchema, JdbcTable>
    implements JdbcTable.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcTableEntity.class ;
            }

        @Override
        @CreateEntityMethod
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
        @CreateEntityMethod
        public JdbcTable create(final JdbcSchema schema, final String name, final TableType type)
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
        @CreateEntityMethod
        public JdbcTable create(final JdbcSchema schema, final AdqlQuery query, final String name)
            {
            return this.insert(
                new JdbcTableEntity(
                    schema,
                    query,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
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
        @SelectEntityMethod
        public JdbcTable select(final JdbcSchema parent, final String name)
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

        @Override
        @SelectEntityMethod
        public Iterable<JdbcTable> search(final JdbcSchema parent, final String text)
            {
            return super.iterable(
                super.query(
                    "JdbcTable-search-parent.text"
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
        protected JdbcColumn.Factory columns;
        @Override
        public JdbcColumn.Factory columns()
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
            TableType.TABLE
            );
        }

    public JdbcTableEntity(final JdbcSchema schema, final String name, final TableType type)
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
            TableType.TABLE
            );
        }

    public JdbcTableEntity(final JdbcSchema schema, final AdqlQuery query, final String name, final TableType type)
        {
        super(schema, name);
        this.query  = query;
        this.schema = schema;
        this.jdbctype = type;
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

    @Override
    public JdbcTable.Columns columns()
        {
        this.scan(false);
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
                }
            @Override
            public JdbcColumn select(final String name)
            throws NotFoundException
                {
                return factories().jdbc().columns().select(
                    JdbcTableEntity.this,
                    name
                    );
                }
/*
            @Override
            public JdbcColumn create(final String name)
                {
                return factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    name
                    );
                }
*/
            @Override
            public JdbcColumn create(final String name, final int type, final int size)
                {
                return factories().jdbc().columns().create(
                    JdbcTableEntity.this,
                    name,
                    type,
                    size
                    );
                }
            @Override
            public Iterable<JdbcColumn> search(final String text)
                {
                return factories().jdbc().columns().search(
                    JdbcTableEntity.this,
                    text
                    );
                }
            @Override
            public void scan()
                {
                JdbcTableEntity.this.scan();
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
    private TableType jdbctype ;
    protected TableType jdbctype()
        {
        return this.jdbctype;
        }
    protected void jdbctype(final TableType type)
        {
        this.jdbctype = type;
        }

    @Override
    public JdbcTable.Info info()
        {
        return new JdbcTable.Info()
            {
            @Override
            public JdbcMeta jdbc()
                {
                return new JdbcMeta()
                    {
                    @Override
                    public TableType type()
                        {
                        return jdbctype() ;
                        }

                    @Override
                    public void type(final TableType type)
                        {
                        jdbctype(
                            type
                            );
                        }
                    };
                }

            @Override
            public AdqlMeta adql()
                {
                return new AdqlMeta()
                    {

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
                        final String ccname  = columns.getString(JdbcMetadata.JDBC_META_TABLE_CAT);
                        final String csname  = columns.getString(JdbcMetadata.JDBC_META_TABLE_SCHEM);
                        final String ctname  = columns.getString(JdbcMetadata.JDBC_META_TABLE_NAME);
                        final String colname = columns.getString(JdbcMetadata.JDBC_META_COLUMN_NAME);
                        final JdbcColumn.Type coltype = JdbcColumn.Type.jdbc(
                            columns.getInt(
                                JdbcMetadata.JDBC_META_COLUMN_TYPE_TYPE
                                )
                            );
                        final Integer colsize = new Integer(
                            columns.getInt(
                                JdbcMetadata.JDBC_META_COLUMN_SIZE
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
                        try {
                            final JdbcColumn column = columnsimpl().select(
                                colname
                                );
                            //
                            // Update the column ..
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
                                coltype.code(),
                                colsize.intValue()
                                );
                            }
                        }
        //
        // TODO
        // Reprocess the list disable missing ones ...
        //
                    scandate(
                        new DateTime()
                        );
                    scanflag(
                        false
                        );
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
    @Index(
        name=DB_TABLE_NAME + "IndexByAdqlQuery"
        )
    @OneToOne(
        fetch = FetchType.LAZY,
        targetEntity = AdqlQueryEntity.class
        )
    @JoinColumn(
        name = DB_ADQL_QUERY_COL,
        unique = true,
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
