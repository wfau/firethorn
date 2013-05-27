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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;

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
    name = JdbcSchemaEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcSchema-select-parent",
            query = "FROM JdbcSchemaEntity WHERE parent = :parent ORDER BY name asc, ident asc"
            ),
        @NamedQuery(
            name  = "JdbcSchema-select-parent.catalog.schema",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (catalog = :catalog) AND (schema = :schema)) ORDER BY name asc, ident asc"
            ),

        @NamedQuery(
            name  = "JdbcSchema-select-parent.null-catalog.null-schema",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (catalog IS NULL) AND (schema IS NULL)) ORDER BY name asc, ident asc"
            ),
        @NamedQuery(
            name  = "JdbcSchema-select-parent.catalog.null-schema",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (catalog = :catalog) AND (schema IS NULL)) ORDER BY name asc, ident asc"
            ),
        @NamedQuery(
            name  = "JdbcSchema-select-parent.null-catalog.schema",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (catalog IS NULL) AND (schema = :schema)) ORDER BY name asc, ident asc"
            ),

        @NamedQuery(
            name  = "JdbcSchema-select-parent.name",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (name = :name)) ORDER BY name asc, ident asc"
            ),
        @NamedQuery(
            name  = "JdbcSchema-search-parent.text",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (name LIKE :text)) ORDER BY name asc, ident asc"
            )
        }
    )
public class JdbcSchemaEntity
    extends BaseSchemaEntity<JdbcSchema, JdbcTable>
    implements JdbcSchema
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = "JdbcSchemaEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_SCHEMA_COL  = "jdbcschema";
    protected static final String DB_JDBC_CATALOG_COL = "jdbccatalog";

    /**
     * Schema factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcSchema>
    implements JdbcSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcSchemaEntity.class ;
            }

        @Override
        public String name(final String catalog, final String schema)
            {
            if (catalog == null)
                {
                return schema.trim() ;
                }
            else if (schema == null)
                {
                return catalog.trim() ;
                }
            else {
                return catalog.trim() + "." + schema.trim() ;
                }
            }

        @Override
        @CreateEntityMethod
        public JdbcSchema create(final JdbcResource parent, final String schema)
            {
            return this.create(
                parent,
                null,
                schema
                );
            }

        @Override
        @CreateEntityMethod
        public JdbcSchema create(final JdbcResource parent, final String catalog, final String schema)
            {
            return this.insert(
                new JdbcSchemaEntity(
                    parent,
                    catalog,
                    schema,
                    name(
                        catalog,
                        schema
                        )
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcSchema> select(final JdbcResource parent)
            {
            return super.list(
                super.query(
                    "JdbcSchema-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public JdbcSchema select(final JdbcResource parent, final String catalog, final String schema)
            {
            log.debug("JdbcSchema select(JdbcResource, String, String)");
            log.debug("  Parent  [{}]", parent.ident());
            log.debug("  Catalog [{}]", catalog);
            log.debug("  Schema  [{}]", schema);

            // Select where could be null ...
            // http://youtrack.jetbrains.com/issue/IDEA-86389
            // http://www.icesoft.org/JForum/posts/list/15439.page
            // http://stackoverflow.com/a/2123461
            // http://stackoverflow.com/questions/2123438/hibernate-how-to-set-null-query-parameter-value-with-hql
            // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4312435

            if ((catalog == null) && (schema == null))
                {
                return super.first(
                    super.query(
                        "JdbcSchema-select-parent.null-catalog.null-schema"
                        ).setEntity(
                            "parent",
                            parent
                        )
                    );
                }
            else if (catalog == null)
                {
                return super.first(
                    super.query(
                        "JdbcSchema-select-parent.null-catalog.schema"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "schema",
                            schema
                        )
                    );
                }
            else if (schema == null)
                {
                return super.first(
                    super.query(
                        "JdbcSchema-select-parent.catalog.null-schema"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "catalog",
                            catalog
                        )
                    );
                }
            else {
                return super.first(
                    super.query(
                        "JdbcSchema-select-parent.catalog.schema"
                        ).setEntity(
                            "parent",
                            parent
                        ).setString(
                            "catalog",
                            catalog
                        ).setString(
                            "schema",
                            schema
                        )
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public JdbcSchema select(final JdbcResource parent, final String name)
            {
            return super.first(
                super.query(
                    "JdbcSchema-select-parent.name"
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
        public Iterable<JdbcSchema> search(final JdbcResource parent, final String text)
            {
            return super.iterable(
                super.query(
                    "JdbcSchema-search-parent.text"
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
        protected JdbcTable.Factory tables;
        @Override
        public JdbcTable.Factory tables()
            {
            return this.tables;
            }

        @Autowired
        protected JdbcSchema.IdentFactory idents ;
        @Override
        public JdbcSchema.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected JdbcSchema.LinkFactory links;
        @Override
        public JdbcSchema.LinkFactory links()
            {
            return this.links;
            }
        }

    protected JdbcSchemaEntity()
        {
        super();
        }

    protected JdbcSchemaEntity(final JdbcResource resource, final String catalog, final String schema, final String name)
        {
        super(resource, name);
        this.catalog  = catalog ;
        this.schema   = schema  ;
        this.resource = resource;
        }

    @Index(
        name=DB_TABLE_NAME + "IndexByParent"
        )
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = JdbcResourceEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private JdbcResource resource;
    @Override
    public JdbcResource resource()
        {
        return this.resource;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_CATALOG_COL,
        unique = false,
        nullable = true,
        updatable = false
        )
    private String catalog;
    @Override
    public  String catalog()
        {
        return this.catalog;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_SCHEMA_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private String schema;
    @Override
    public  String schema()
        {
        return this.schema;
        }

    @Override
    public JdbcSchema.Tables tables()
        {
        this.scan(false);
        return tablesimpl();
        }

    protected JdbcSchema.Tables tablesimpl()
        {
        return new JdbcSchema.Tables()
            {
            @Override
            public Iterable<JdbcTable> select()
                {
                return factories().jdbc().tables().select(
                    JdbcSchemaEntity.this
                    );
                }
            @Override
            public JdbcTable select(final String name)
                {
                return factories().jdbc().tables().select(
                    JdbcSchemaEntity.this,
                    name
                    );
                }
            @Override
            public JdbcTable create(final String name)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    name
                    );
                }
            @Override
            public JdbcTable create(final String name, final JdbcTable.TableType type)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    name,
                    type
                    );
                }
            @Override
            public JdbcTable create(final AdqlQuery query)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    query
                    );
                }
            @Override
            public Iterable<JdbcTable> search(final String text)
                {
                return factories().jdbc().tables().search(
                    JdbcSchemaEntity.this,
                    text
                    );
                }
            @Override
            public void scan()
                {
                JdbcSchemaEntity.this.scan();
                }
            };
        }

    @Override
    public String link()
        {
        return factories().jdbc().schemas().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
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
                try {
                    final ResultSet tables = metadata.getTables(
                        this.catalog(),
                        this.schema(),
                        null, // tab
                        new String[]
                            {
                            JdbcMetadata.JDBC_META_TABLE_TYPE_TABLE,
                            JdbcMetadata.JDBC_META_TABLE_TYPE_VIEW
                            }
                        );

                    while (tables.next())
                        {
                        final String tcname = tables.getString(JdbcMetadata.JDBC_META_TABLE_CAT);
                        final String tsname = tables.getString(JdbcMetadata.JDBC_META_TABLE_SCHEM);
                        final String ttname = tables.getString(JdbcMetadata.JDBC_META_TABLE_NAME);
                        final String tttype = tables.getString(JdbcMetadata.JDBC_META_TABLE_TYPE);
                        log.debug("Found table [{}.{}.{}]", new Object[]{tcname, tsname, ttname});

                        JdbcTable table = tablesimpl().select(
                            ttname
                            );
                        if (table != null)
                            {
                            table.info().jdbc().type(
                                JdbcTable.TableType.match(
                                    tttype
                                    )
                                );
                            }
                        else {
                            table = tablesimpl().create(
                                ttname,
                                JdbcTable.TableType.match(
                                    tttype
                                    )
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
                    log.error("Exception reading JDBC schema metadata [{}]", ouch.getMessage());
                    throw resource().connection().translator().translate(
                        "Reading JDBC resource metadata",
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
    }
