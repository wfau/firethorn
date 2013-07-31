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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTableEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseNameFactory;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;

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
    name = JdbcSchemaEntity.DB_TABLE_NAME,
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
            ),

        @NamedQuery(
            name  = "JdbcSchema-search-parent.owner",
            query = "FROM JdbcSchemaEntity WHERE ((parent = :parent) AND (owner = :owner)) ORDER BY name asc, ident asc"
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
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcSchemaEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_SCHEMA_COL  = "jdbcschema";
    protected static final String DB_JDBC_CATALOG_COL = "jdbccatalog";

    /**
     * Name factory implementation.
     * @todo Move to a separate package.
     *
     */
    @Component
    public static class NameFactory
    extends BaseNameFactory<JdbcSchema>
    implements JdbcSchema.NameFactory
        {
        @Override
        public String fullname(final String catalog, final String schema)
            {
            if ((catalog == null) && (schema == null))
                {
                throw new IllegalArgumentException(
                    "At least one of [catalog][schema] is required"
                    );
                }
// SQLServer specific '*' catalog.
            else if ((catalog == null) || (JdbcResource.ALL_CATALOGS.equals(catalog)))
                {
                return safe(
                    schema
                    );
                }
            else if (schema == null)
                {
                return safe(
                    catalog
                    );
                }
// SQLServer specific dot notation.
// Explicitly avoiding super class 'safe' method for the '.' dot.
            else {
                return safe(catalog) + "." + safe(schema) ;
                }
            }

        @Override
        public String datename()
            {
            return datename(
                "SCHEMA_",
                factories().authentications().current().identity()
                );
            }

        @Override
        public String datename(final Identity identity)
            {
            return datename(
                "IDENTITY_",
                identity
                );
            }

        @Override
        public String datename(final String prefix, final Identity identity)
            {
            return datename(
                prefix,
                identity.ident()
                );
            }
        }

    /**
     * Entity factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractEntityFactory<JdbcSchema>
    implements JdbcSchema.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcSchemaEntity.class ;
            }

        @Override
        @CreateEntityMethod
        public JdbcSchema build(final JdbcResource parent, final Identity identity)
            {
// TODO Need the resource catalog name ?
// NameFactory - Generate a unique name from JdbcResource and Identity.
// TODO Liquibase SchemaBuilder ?
            log.debug("JdbcSchema build(JdbcResource ,Identity)");
            log.debug(" Identity [{}][{}]", identity.ident(), identity.name());

            return builder().create(
                this.create(
                    parent,
                    parent.catalog(),
                    names.datename(
                        identity
                        )
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public JdbcSchema create(final JdbcResource parent, final String catalog, final String schema)
            {
            return this.create(
                parent,
                catalog,
                schema,
                names.fullname(
                    catalog,
                    schema
                    )
                );
            }

        @CreateEntityMethod
        public JdbcSchema create(final JdbcResource parent, final String catalog, final String schema, final String name)
            {
            return this.insert(
                new JdbcSchemaEntity(
                    parent,
                    catalog,
                    schema,
                    name
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
        throws NotFoundException
            {
            log.debug("JdbcSchema select(JdbcResource, String, String)");
            log.debug("  Parent  [{}]", parent.ident());
            log.debug("  Catalog [{}]", catalog);
            log.debug("  Schema  [{}]", schema);
            final JdbcSchema found = search(
                parent,
                catalog,
                schema
                );
            if (found != null)
                {
                return found ;
                }
            else {
                throw new NotFoundException(
                    "Unable to find matching schema [" + catalog + "][" + found  + "]"
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public JdbcSchema search(final JdbcResource parent, final String catalog, final String schema)
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
        throws NameNotFoundException
            {
            log.debug("JdbcSchema select(JdbcResource, String)");
            log.debug("  Resource [{}][{}]", parent.ident(), parent.name());
            log.debug("  Schema   [{}]", name);
            try
                {
                return super.single(
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
            catch (NotFoundException ouch)
                {
                log.debug("Unable to locate schema [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectEntityMethod
        public JdbcSchema search(final JdbcResource parent, final String name)
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
        public Iterable<JdbcSchema> select(final JdbcResource parent, final Identity owner)
            {
            return super.iterable(
                super.query(
                    "JdbcSchema-search-parent.owner"
                    ).setEntity(
                        "parent",
                        parent
                    ).setEntity(
                        "owner",
                        owner
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

        @Autowired
        protected JdbcSchema.NameFactory names;
        @Override
        public JdbcSchema.NameFactory names()
            {
            return this.names;
            }

        @Autowired
        protected JdbcSchema.Builder builder;
        @Override
        public JdbcSchema.Builder builder()
            {
            return this.builder;
            }
        }

    protected JdbcSchemaEntity()
        {
        super();
        }

    protected JdbcSchemaEntity(final JdbcResource resource, final String catalog, final String schema, final String name)
        {
        super(resource, name);
        this.resource = resource;
        this.catalog  = catalog ;
        this.schema   = schema  ;
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
    public BaseSchema<?, ?> base()
        {
        return this ;
        }
    @Override
    public BaseSchema<?, ?> root()
        {
        return this ;
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
        mappedBy = "schema",
        targetEntity = JdbcTableEntity.class
        )
    private Map<String, JdbcTable> children = new LinkedHashMap<String, JdbcTable>();
     *
     */
    
    @Override
    public JdbcSchema.Tables tables()
        {
        log.debug("tables() for [{}]", ident());
        scantest();
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
                /*
                 * HibernateCollections 
                return children.values();
                 *
                 */
                }

            @Override
            public JdbcTable search(final String name)
                {
                /*
                 * HibernateCollections 
                return children.get(name);
                 *
                 */
                return factories().jdbc().tables().search(
                    JdbcSchemaEntity.this,
                    name
                    );
                }

            @Override
            public JdbcTable select(final String name)
            throws NameNotFoundException
                {
                return factories().jdbc().tables().select(
                    JdbcSchemaEntity.this,
                    name
                    );
                /*
                 * HibernateCollections 
                JdbcTable result = children.get(name);
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
            public JdbcTable create(final String name)
                {
                JdbcTable result = factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    name
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
            public JdbcTable create(final String name, final JdbcTable.TableType type)
                {
                JdbcTable result = factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
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
            public JdbcTable create(final AdqlQuery query)
                {
                JdbcTable result = factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    query
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
                JdbcSchemaEntity.this.scansync();
                }

            @Override
            public JdbcTable select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                // TODO Add parent constraint.
                return factories().jdbc().tables().select(
                    ident
                    );
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
                            JdbcTypes.JDBC_META_TABLE_TYPE_TABLE,
                            JdbcTypes.JDBC_META_TABLE_TYPE_VIEW
                            }
                        );

                    while (tables.next())
                        {
                        final String tcname = tables.getString(JdbcTypes.JDBC_META_TABLE_CAT);
                        final String tsname = tables.getString(JdbcTypes.JDBC_META_TABLE_SCHEM);
                        final String ttname = tables.getString(JdbcTypes.JDBC_META_TABLE_NAME);
                        final String tttype = tables.getString(JdbcTypes.JDBC_META_TABLE_TYPE);
                        log.debug("Found table [{}.{}.{}]", new Object[]{tcname, tsname, ttname});

                        // TODO Remove the try/catch
                        try {
                            final JdbcTable table = tablesimpl().select(
                                ttname
                                );
                            table.meta().jdbc().type(
                                JdbcTable.TableType.match(
                                    tttype
                                    )
                                );
                            }
                        catch (final NotFoundException ouch)
                            {
                            tablesimpl().create(
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
