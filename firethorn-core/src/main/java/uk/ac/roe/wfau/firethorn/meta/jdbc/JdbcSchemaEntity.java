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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.DateNameFactory;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityServiceException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseComponentEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchemaEntity;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.sqlserver.MSSQLMetadataScanner;

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
    indexes={
        @Index(
            columnList = JdbcSchemaEntity.DB_PARENT_COL
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
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcSchemaEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_SCHEMA_COL  = "jdbcschema";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_CATALOG_COL = "jdbccatalog";

    /**
     * {@link EntityBuilder} implementation.
     *
     */
    public static abstract class Builder
    extends AbstractEntityBuilder<JdbcSchema, JdbcSchema.Metadata>
    implements JdbcSchema.Builder
        {
        public Builder(final Iterable<JdbcSchema> source)
            {
            this.init(
                source
                );
            }

        @Override
        protected String name(JdbcSchema.Metadata meta)
            {
            return meta.jdbc().fullname();
            }

        @Override
        protected void update(final JdbcSchema schema, final JdbcSchema.Metadata meta)
            {
            schema.update(
                meta
                );
            }
        }
    
    /**
     * {@link JdbcSchema.NameFactory} implementation.
     * @todo Does this depend on the database type ?
     * @todo Is this part of the parent resource ? 
     *
     */
    @Component
    public static class NameFactory
    extends DateNameFactory<JdbcSchema>
    implements JdbcSchema.NameFactory
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
        }

    /**
     * {@link JdbcSchema.EntityFactory} implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<JdbcSchema>
    implements JdbcSchema.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcSchemaEntity.class ;
            }

        //TODO Resolve the full name problem
        @Override
        public JdbcSchema create(final JdbcResource parent, final JdbcSchema.Metadata meta)
            {
            return this.create(
                parent,
                meta.jdbc().catalog(),
                meta.jdbc().schema()
                );
            }

        
        @Override
        @CreateMethod
        public JdbcSchema build(final JdbcResource parent, final Identity identity)
            {
// TODO Need the resource catalog name ?
// NameFactory - Generate a unique name from JdbcResource and Identity.
// TODO Liquibase SchemaBuilder ?
            log.debug("JdbcSchema build(JdbcResource ,Identity)");
            log.debug(" Identity [{}][{}]", identity.ident(), identity.name());
           
            return oldbuilder().create(
                this.create(
                    parent,
                    parent.catalog(),
                    names.name()
                    )
                );
            }

        @Override
        @CreateMethod
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

        @CreateMethod
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
        @SelectMethod
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
        @SelectMethod
        public JdbcSchema select(final JdbcResource parent, final String catalog, final String schema)
        throws EntityNotFoundException
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
                throw new EntityNotFoundException(
                    "Unable to find matching schema [" + catalog + "][" + schema  + "]"
                    );
                }
            }

        @Override
        @SelectMethod
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
        @SelectMethod
        public JdbcSchema select(final JdbcResource parent, final String name)
        throws NameNotFoundException
            {
            log.debug("JdbcSchema select(JdbcResource, String)");
            log.debug("  Resource [{}][{}]", parent.ident(), parent.name());
            log.debug("  Schema   [{}]", name);
            try {
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
            catch (final EntityNotFoundException ouch)
                {
                log.debug("Unable to locate schema [{}][{}]", parent.namebuilder().toString(), name);
                throw new NameNotFoundException(
                    name,
                    ouch
                    );
                }
            }

        @Override
        @SelectMethod
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
        @SelectMethod
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
        protected JdbcTable.EntityFactory tables;
        @Override
        public JdbcTable.EntityFactory tables()
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
        protected JdbcSchema.OldBuilder builder;
        @Override
        public JdbcSchema.OldBuilder oldbuilder()
            {
            return this.builder;
            }
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcSchemaEntity()
        {
        super();
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcSchemaEntity(final JdbcResource resource, final String catalog, final String schema, final String name)
        {
        super(resource, name);
        this.resource = resource;
        this.catalog  = catalog ;
        this.schema   = schema  ;
        }

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
        return self();
        }
    @Override
    public BaseSchema<?, ?> root()
        {
        return self();
        }

    @Override
    public JdbcSchema.Tables tables()
        {
        log.debug("tables() for [{}][{}]", ident(), namebuilder());
        scantest();
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
            public JdbcTable search(final String name)
                {
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
                }

            @Override
            @Deprecated
            public JdbcTable create(final String name)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    name
                    );
                }

            @Override
            @Deprecated
            public JdbcTable create(final String name, final JdbcTable.JdbcType type)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    name,
                    type
                    );
                }

            @Override
            public JdbcTable create(final JdbcTable.Metadata meta)
                {
                return factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    meta
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
            public JdbcTable select(final Identifier ident)
            throws IdentifierNotFoundException
                {
                // TODO Add parent constraint.
                return factories().jdbc().tables().select(
                    ident
                    );
                }

            @Override
            public Iterable<JdbcTable> pending(final DateTime date, final int page)
                {
                return factories().jdbc().tables().pending(
                    JdbcSchemaEntity.this,
                    date,
                    page
                    );
                }

            @Override
            public JdbcTable.Builder builder()
                {
                return new JdbcTableEntity.Builder(this.select())
                    {
                    @Override
                    protected JdbcTable create(final JdbcTable.Metadata meta)
                        throws DuplicateEntityException
                        {
                        return factories().jdbc().tables().create(
                            JdbcSchemaEntity.this,
                            meta
                            );
                        }
                    };
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
        log.debug("tables() scan for [{}][{}]", this.ident(), this.namebuilder());
        //
        // Create our metadata scanner.
        JdbcMetadataScanner scanner = resource().connection().scanner();

        //
        // Load our existing tables.
        Map<String, JdbcTable> existing = new HashMap<String, JdbcTable>();
        Map<String, JdbcTable> matching = new HashMap<String, JdbcTable>();
        for (JdbcTable table : factories().jdbc().tables().select(JdbcSchemaEntity.this))
            {
            log.debug("Caching existing table [{}]", table.name());
            existing.put(
                table.name(),
                table
                );
            }
        //
        // Process our tables.
        try {
            scan(
                existing,
                matching,
                scanner.catalogs().select(
                    this.catalog()
                    ).schemas().select(
                        this.schema()
                        )
                );
            }
        catch (SQLException ouch)
            {
            log.warn("Exception while fetching schema [{}][{}]", this.ident(), ouch.getMessage());
            scanner.handle(ouch);
            }
        catch (MetadataException ouch)
            {
            log.warn("Exception while fetching schema [{}][{}]", this.ident(), ouch.getMessage());
            }
        finally {
            scanner.connector().close();
            }
        log.debug("tables() scan done for [{}][{}]", this.ident(), this.namebuilder());
        log.debug("Existing contains [{}]", existing.size());
        log.debug("Matching contains [{}]", matching.size());
        }

    protected void scan(final Map<String, JdbcTable> existing, final Map<String, JdbcTable> matching, final JdbcMetadataScanner.Schema schema)
        {
        log.debug("scanning schema [{}]", (schema != null) ? schema.name() : null);
        if (schema == null)
            {
            log.warn("Null schema");
            }
        else {
            try {
                for (JdbcMetadataScanner.Table table : schema.tables().select())
                    {
                    scan(
                        existing,
                        matching,
                        table
                        );
                    }
                }
            catch (SQLException ouch)
                {
                log.warn("Exception while fetching schema tables [{}][{}][{}]", this.ident(), schema.name(), ouch.getMessage());
                schema.catalog().scanner().handle(ouch);
                }
            }
        }

    protected void scan(final Map<String, JdbcTable> existing, final Map<String, JdbcTable> matching, final JdbcMetadataScanner.Table table)
        {
        String name = table.name();
        log.debug("Scanning for table [{}]", name);
        //
        // Check for an existing match.
        if (existing.containsKey(name))
            {
            log.debug("Found existing table [{}]", name);
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
            log.debug("Creating new table [{}]", name);
            matching.put(
                name,
                factories().jdbc().tables().create(
                    JdbcSchemaEntity.this,
                    table.name(),
                    JdbcTable.JdbcType.TABLE
                    )
                );
            }
        }

    /**
     * Generate the JDBC metadata.
     * 
     */
    public JdbcSchema.Metadata.Jdbc jdbcmeta()
        {
        return new JdbcSchema.Metadata.Jdbc()
            {
            @Override
            public String fullname()
                {
                return JdbcSchemaEntity.this.name();
                }

            @Override
            public String schema()
                {
                return JdbcSchemaEntity.this.schema();
                }

            @Override
            public String catalog()
                {
                return JdbcSchemaEntity.this.catalog();
                }
            };
        }

    @Override
    public JdbcSchema.Metadata meta()
        {
        return new JdbcSchema.Metadata()
            {
            @Override
            public String name()
                {
                return JdbcSchemaEntity.this.name();
                }

            @Override
            public Adql adql()
                {
                return adqlmeta();
                }

            @Override
            public Jdbc jdbc()
                {
                return jdbcmeta();
                }
            };
        }

    @Override
    public void update(final JdbcSchema.Metadata meta)
        {
        if (meta.adql() != null)
            {
            if (meta.adql().text() != null)
                {
                this.text(
                    meta.adql().text()
                    );
                }
            }
        }
    }
