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
import java.sql.SQLInvalidAuthorizationSpecException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;

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
    name = JdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcResource-select-all",
            query = "FROM JdbcResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "JdbcResource-search-text",
            query = "FROM JdbcResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
public class JdbcResourceEntity
    extends BaseResourceEntity<JdbcSchema>
    implements JdbcResource
    {
    /**
     * Hibernate table mapping.
     *
     */
    protected static final String DB_TABLE_NAME = "JdbcResourceEntity";

    /**
     * Hibernate column mapping.
     *
     */
    protected static final String DB_JDBC_CATALOG_COL = "jdbccatalog";
    protected static final String DB_JDBC_OGSAID_COL  = "jdbcogsaid";
    /**
     * Resource factory implementation.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<JdbcResource>
    implements JdbcResource.Factory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcResourceEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> select()
            {
            return super.iterable(
                super.query(
                    "JdbcResource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "JdbcResource-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Override
        @Deprecated
        @CreateEntityMethod
        public JdbcResource create(final String ogsaid, final String name, final String url)
            {
            return this.create(
                ogsaid,
                null,
                name,
                url
                );
            }

        @Override
        @CreateEntityMethod
        public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url)
            {
            return super.insert(
                new JdbcResourceEntity(
                    ogsaid,
                    catalog,
                    name,
                    url
                    )
                );
            }

		@Override
        @CreateEntityMethod
		public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url, final String user, final String pass) {
            return super.insert(
                new JdbcResourceEntity(
                    ogsaid,
                    catalog,
                    name,
                    url,
                    user,
                    pass
                    )
                );
			}

        @Autowired
        protected JdbcSchema.Factory schemas;
        @Override
        public JdbcSchema.Factory schemas()
            {
            return this.schemas;
            }

        @Autowired
        protected JdbcResource.IdentFactory idents ;
        @Override
        public JdbcResource.IdentFactory idents()
            {
            return this.idents ;
            }

        @Autowired
        protected JdbcResource.LinkFactory links;
        @Override
        public JdbcResource.LinkFactory links()
            {
            return this.links;
            }
        }

    protected JdbcResourceEntity()
        {
        super();
        }

    @Deprecated
    protected JdbcResourceEntity(final String ogsaid, final String name)
        {
        super(name);
        this.ogsaid = ogsaid ;
        this.connection = new JdbcConnectionEntity(
            this
            );
        }

    protected JdbcResourceEntity(final String ogsaid, final String catalog, final String name, final  String url)
        {
        super(name);
        this.ogsaid  = ogsaid  ;
        this.catalog = catalog ;
        this.connection = new JdbcConnectionEntity(
            this,
            url
            );
        }

    protected JdbcResourceEntity(final String ogsaid, final String catalog, final String name, final String url, final String user, final String pass)
	    {
	    super(name);
        this.ogsaid  = ogsaid  ;
        this.catalog = catalog ;
	    this.connection = new JdbcConnectionEntity(
	        this,
	        url,
	        user,
	        pass
	        );
	    }

    @Override
    public JdbcResource.Schemas schemas()
        {
        this.scan(false);
        return this.schemasimpl();
        }

    protected JdbcResource.Schemas schemasimpl()
        {
        return new JdbcResource.Schemas(){

            @Override
            public Iterable<JdbcSchema> select()
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this
                    );
                }
            
            @Override
            public JdbcSchema create(final String catalog, final String schema)
                {
                return factories().jdbc().schemas().create(
                    JdbcResourceEntity.this,
                    catalog,
                    schema
                    );
                }
            
            @Override
            public JdbcSchema select(final String catalog, final String schema)
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this,
                    catalog,
                    schema
                    );
                }
            
            @Override
            public JdbcSchema select(final String name)
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this,
                    name
                    );
                }
            
            @Override
            public Iterable<JdbcSchema> search(final String text)
                {
                return factories().jdbc().schemas().search(
                    JdbcResourceEntity.this,
                    text
                    );
                }

            @Override
            public JdbcSchema create(final Identity identity)
                {
                return factories().jdbc().schemas().create(
                    JdbcResourceEntity.this
                    );
                }

            @Override
            public Iterable<JdbcSchema> select(final Identity identity)
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this,
                    identity
                    );
                }
            
            @Override
            public void scan()
                {
                JdbcResourceEntity.this.scan(true);
                }
            };
        }

    @Embedded
    private JdbcConnectionEntity connection;

    @Override
    public JdbcConnection connection()
        {
        return this.connection;
        }

    /**
     * The the OGSA-DAI resource ID.
     *
     */
    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_OGSAID_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String ogsaid;
    @Override
    public String ogsaid()
        {
        return this.ogsaid;
        }
    @Override
    public void ogsaid(final String ogsaid)
        {
        this.ogsaid = ogsaid;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_CATALOG_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String catalog ;
    @Override
    public String catalog()
        {
        return this.catalog ;
        }
    @Override
    public void catalog(final String catalog)
        {
        this.catalog = catalog ;
        this.scan(true);
        }

    @Override
    public String link()
        {
        return factories().jdbc().resources().links().link(
            this
            );
        }

    @Override
    protected void scanimpl()
        {
        log.debug("scanimpl()");
        //
        // Default to using the catalog name from the Connection.
        // Explicitly set it to 'ALL_CATALOGS' to get all.
        if (this.catalog == null)
            {
            this.catalog = connection().catalog();
            }
        //
        // Scan all the catalogs.
        if (ALL_CATALOGS.equals(this.catalog))
            {
            for (final String cname : connection().catalogs())
                {
                try {
                    scanimpl(
                        cname
                        );
                    }
                catch (final Exception ouch)
                    {
                    log.debug("Exception in catalog processing loop");
                    log.debug("Exception text   [{}]", ouch.getMessage());
                    log.debug("Exception string [{}]", ouch.toString());
                    log.debug("Exception class  [{}]", ouch.getClass().toString());
                    //
                    // Continue with the rest of the catalogs ...
                    //
                    }
                }
            }
        //
        // Just scan one catalog.
        else {
            scanimpl(
                this.catalog
                );
            }
//
// TODO
// Reprocess the list disable missing ones ...
//
        scandate(new DateTime());
        scanflag(false);

        }

    protected void scanimpl(final String catalog)
        {
        log.debug("scanimpl(String)");
        log.debug("  Catalog [{}]", catalog);
        //
        // Get the database metadata
        try {
            final DatabaseMetaData metadata = connection().metadata();
            final JdbcProductType  product  = JdbcProductType.match(
                metadata
                );
            // TODO - fix connection errors
            if (metadata != null)
                {
                try {
                    final ResultSet tables = metadata.getTables(
                        catalog,
                        null, // sch
                        null, // tab
                        new String[]
                            {
                            JdbcMetadata.JDBC_META_TABLE_TYPE_TABLE,
                            JdbcMetadata.JDBC_META_TABLE_TYPE_VIEW
                            }
                        );

                    String cprev = null ;
                    String sprev = null ;
                    while (tables.next())
                        {
                        String cname = tables.getString(JdbcMetadata.JDBC_META_TABLE_CAT);
                        String sname = tables.getString(JdbcMetadata.JDBC_META_TABLE_SCHEM);
                        log.debug("Found schema [{}][{}]", new Object[]{cname, sname});
                        //
                        // In MySQL the schema name is always null, use the catalog name instead.
                        if (product == JdbcProductType.MYSQL)
                            {
                            sname = cname ;
                            cname = null ;
                            }
                        //
                        // In HSQLDB the schema and catalogs overlap, use the catalog name as the schema.
                        if (product == JdbcProductType.HSQLDB)
                            {
                            //log.debug("JdbcProductType.HSQLDB, swapping names");
                            //sname = cname ;
                            //cname = null ;
                            }

                        //
                        // Skip if the schema is on our ignore list.
                        if (product.ignores().contains(sname))
                            {
                            //log.debug("Schema [{}] is on the ignore list for [{}]", sname, product);
                            continue;
                            }
                        //
                        // Check if we have already done this one.
                        if (
                            ((cname == null) ? cprev == null : cname.equals(cprev))
                            &&
                            ((sname == null) ? sprev == null : sname.equals(sprev))
                            ){
                            log.debug("Already done [{}][{}], skipping", cname, sname);
                            continue;
                            }
                        else {
                            cprev = cname;
                            sprev = sname;
                            }

                        log.debug("Processing schema [{}][{}]", new Object[]{cname, sname});

                        //
                        // Check for an existing schema.
                        JdbcSchema schema = this.schemasimpl().select(
                            cname,
                            sname
                            );
                        log.debug("Found schema [{}]", schema);
                        //
                        // If none found, create a new one.
                        if (schema == null)
                            {
                            schema = this.schemasimpl().create(
                                cname,
                                sname
                                );
                            }
                        }
                    }
                catch (final SQLInvalidAuthorizationSpecException ouch)
                    {
                    log.debug("Authorization exception reading JDBC metadata for [{}][{}][{}]", connection().uri(), catalog, ouch.getMessage());
                    }
                catch (final SQLException ouch)
                    {
                    log.error("Exception reading JDBC metadata for [{}][{}][{}]", connection().uri(), catalog, ouch.getMessage());
                    log.debug("Exception text   [{}]", ouch.getMessage());
                    log.debug("Exception string [{}]", ouch.toString());
                    log.debug("Exception class  [{}]", ouch.getClass().toString());
                    throw connection().translator().translate(
                        "Reading JDBC catalog schemas",
                        null,
                        ouch
                        );
                    }
                }
            }
        finally {
            connection().close();
            }
        }
    }
