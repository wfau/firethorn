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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.entity.AbstractEntityFactory;
import uk.ac.roe.wfau.firethorn.entity.annotation.CreateMethod;
import uk.ac.roe.wfau.firethorn.entity.annotation.SelectMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityServiceException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResourceEntity;
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
    name = JdbcResourceEntity.DB_TABLE_NAME
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "JdbcResource-select-all",
            query = "FROM JdbcResourceEntity ORDER BY name asc, ident desc"
            ),
        @NamedQuery(
            name  = "JdbcResource-select-ogsaid",
            query = "FROM JdbcResourceEntity WHERE (ogsaid = :ogsaid) ORDER BY ident desc"
            )
        }
    )
public class JdbcResourceEntity
    extends BaseResourceEntity<JdbcResource, JdbcSchema>
    implements JdbcResource
    {
    /**
     * Hibernate table mapping, {@value}.
     *
     */
    protected static final String DB_TABLE_NAME = DB_TABLE_PREFIX + "JdbcResourceEntity";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_CATALOG_COL = "jdbccatalog";

    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_OGSAID_COL  = "jdbcogsaid";

    /**
     * Resource factory implementation.
     *
     */
    @Repository
    public static class EntityFactory
    extends AbstractEntityFactory<JdbcResource>
    implements JdbcResource.EntityFactory
        {

        @Override
        public Class<?> etype()
            {
            return JdbcResourceEntity.class ;
            }

        @Override
        @SelectMethod
        public Iterable<JdbcResource> select()
            {
            return super.iterable(
                super.query(
                    "JdbcResource-select-all"
                    )
                );
            }

        @Override
        @CreateMethod
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
        @CreateMethod
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
        @CreateMethod
        public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url, final String user, final String pass)
		    {
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

		@Override
	    @CreateMethod
	    public JdbcResource create(final String ogsaid, final String catalog, final String name, final String url, final String user, final String pass, final String driver)
		    {
		    return super.insert(
		        new JdbcResourceEntity(
		            ogsaid,
		            catalog,
		            name,
		            url,
		            user,
		            pass,
		            driver
		            )
                );
            }

        @Autowired
        protected JdbcSchema.EntityFactory schemas;
        @Override
        public JdbcSchema.EntityFactory schemas()
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

        /**
         * The default 'userdate' OGSA-DAI resource id.
         * @todo Make this a configurable property.
         *
         */
        public static final String DEFAULT_USERDATA_OGSA_ID = "userdata" ;

        /**
         * The default 'userdate' JDBC URI.
         * @todo Make this a configurable property.
         *
         */
        public static final String DEFAULT_USERDATA_URI = "spring:FireThornUserData" ;

        /**
         * Select (or create) the default 'userdata' Resource (based on ogsaid).
         * @todo Make the default properties configurable.
         *
         */
        @Override
        @CreateMethod
        public JdbcResource userdata()
            {
            log.debug("userdata()");
            JdbcResource userdata = ogsaid(
                DEFAULT_USERDATA_OGSA_ID
                );
            if (userdata == null)
                {
                log.debug("Userdata resource is null, creating a new one");
                userdata = create(
                    DEFAULT_USERDATA_OGSA_ID,
                    DEFAULT_USERDATA_OGSA_ID,
                    DEFAULT_USERDATA_URI
                    );
                }
            log.debug("Userdata resource [{}][{}]", userdata.ident(), userdata.name());
            return userdata ;
            }

        @SelectMethod
        public JdbcResource ogsaid(final String ogsaid)
            {
            return super.first(
                super.query(
                    "JdbcResource-select-ogsaid"
                    ).setString(
                        "ogsaid",
                        ogsaid
                        )
                );
            }

        }

    /**
     * Protected constructor. 
     *
     */
    protected JdbcResourceEntity()
        {
        super();
        }

    /**
     * Protected constructor. 
     *
     */
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

    /**
     * Protected constructor. 
     *
     */
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

    /**
     * Protected constructor. 
     *
     */
    protected JdbcResourceEntity(final String ogsaid, final String catalog, final String name, final String url, final String user, final String pass, final String driver)
        {
        super(name);
        this.ogsaid  = ogsaid  ;
        this.catalog = catalog ;
        this.connection = new JdbcConnectionEntity(
            this,
            url,
            user,
            pass,
            driver
            );
        }

    @Override
    public JdbcResource.Schemas schemas()
        {
        log.debug("schemas() for [{}][{}]", ident(), namebuilder());
        scantest();
        return new JdbcResource.Schemas()
            {

            @Override
            public Iterable<JdbcSchema> select()
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this
                    );
                }

            @Override
            public JdbcSchema create(final Identity identity)
                {
                return factories().jdbc().schemas().build(
                    JdbcResourceEntity.this,
                    identity
                    );
                }

            @Override
            public JdbcSchema create(final JdbcSchema.Metadata meta)
                {
                return factories().jdbc().schemas().create(
                    JdbcResourceEntity.this,
                    meta
                    );
                }
            
            @Override
            @Deprecated
            public JdbcSchema create(final String catalog, final String schema)
                {
                return factories().jdbc().schemas().create(
                    JdbcResourceEntity.this,
                    catalog,
                    schema
                    );
                }

            @Override
            public JdbcSchema search(final String name)
                {
                return factories().jdbc().schemas().search(
                    JdbcResourceEntity.this,
                    name
                    );
                }

            @Override
            public JdbcSchema select(final String name)
            throws NameNotFoundException
                {
                return factories().jdbc().schemas().select(
                    JdbcResourceEntity.this,
                    name
                    );
                }

            @Override
            public JdbcSchema select(final String catalog, final String schema)
            throws NameNotFoundException
                {
                return select(
                    factories().jdbc().schemas().names().fullname(
                        catalog,
                        schema
                        )
                    );
                }

            @Override
            public JdbcSchema search(final String catalog, final String schema)
                {
                return factories().jdbc().schemas().search(
                    JdbcResourceEntity.this,
                    catalog,
                    schema
                    );
                }

            @Override
            public JdbcSchema simple()
            throws EntityNotFoundException
                {
                try {
                    return factories().jdbc().schemas().select(
                        JdbcResourceEntity.this,
                        connection().catalog(),
                        connection().type().schema()
                        );
                    }
                catch (final MetadataException ouch)
                    {
                    log.warn("Exception trying to access JDBC metadata");
                    throw new EntityServiceException(
                        "Exception trying to access JDBC metadata",
                        ouch
                        );
                    }
                }

            @Override
            public JdbcSchema.Builder builder()
                {
                return new JdbcSchemaEntity.Builder(this.select())
                    {
                    @Override
                    protected JdbcSchema create(final JdbcSchema.Metadata meta)
                        throws DuplicateEntityException
                        {
                        return factories().jdbc().schemas().create(
                            JdbcResourceEntity.this,
                            meta
                            );
                        }
                    };
                }
            };
        }

    @Embedded
    private JdbcConnectionEntity connection;

    @Override
    public JdbcConnector connection()
        {
        return this.connection;
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
        this.scandate(
            null
            );
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
        log.debug("schemas() scan for [{}][{}]", this.ident(), this.namebuilder());
        //
        // Create our metadata scanner.
        JdbcMetadataScanner scanner = new MSSQLMetadataScanner(
            connection()
            );
        //
        // Load our existing schema.
        Map<String, JdbcSchema> existing = new HashMap<String, JdbcSchema>();
        Map<String, JdbcSchema> matching = new HashMap<String, JdbcSchema>();
        for (JdbcSchema schema : factories().jdbc().schemas().select(JdbcResourceEntity.this))
            {
            log.debug("Caching existing schema [{}]", schema.name());
            existing.put(
                schema.name(),
                schema
                );
            }
        //
        // Try/finally to close our connection. 
        try {
            //
            // Default to our Connection catalog name.
            if (this.catalog == null)
                {
                try {
                    this.catalog = connection().catalog();
                    }
                catch (MetadataException ouch)
                    {
                    log.warn("Exception while fetching JDBC catalog [{}][{}]", this.ident(), ouch.getMessage());
                    }
                }
            //
            // Scan all the catalogs.
            if (ALL_CATALOGS.equals(this.catalog))
                {
                try {
                    for (JdbcMetadataScanner.Catalog catalog : scanner.catalogs().select())
                        {
                        scan(
                            existing,
                            matching,
                            catalog
                            );
                        }
                    }
                catch (SQLException ouch)
                    {
                    log.warn("Exception while fetching JDBC catalogs [{}][{}]", this.ident(), ouch.getMessage());
                    scanner.handle(ouch);
                    }
                }
            //
            // Scan a specific catalog.
            else {
                try {
                    scan(
                        existing,
                        matching,
                        scanner.catalogs().select(
                            this.catalog
                            )
                        );
                    }
                catch (SQLException ouch)
                    {
                    log.warn("Exception while fetching JDBC catalog [{}][{}]", this.ident(), ouch.getMessage());
                    scanner.handle(ouch);
                    }
                }
            }
        finally {
            scanner.connector().close();
            }
        log.debug("schemas() scan done for [{}][{}]", this.ident(), this.namebuilder());
        log.debug("Existing contains [{}]", existing.size());
        log.debug("Matching contains [{}]", matching.size());
        }
    
    protected void scan(final Map<String, JdbcSchema> existing, final Map<String, JdbcSchema> matching, final JdbcMetadataScanner.Catalog catalog)
        {
        if (catalog == null)
            {
            log.warn("Null catalog");
            }
        else {
            log.debug("Scanning catalog [{}]", catalog.name());
            try {
                for (JdbcMetadataScanner.Schema schema : catalog.schemas().select())
                    {
                    scan(
                        existing,
                        matching,
                        schema
                        );                
                    }
                }
            catch (SQLException ouch)
                {
                log.warn("Exception while scanning catalog [{}][{}][{}]", this.ident(), catalog.name(), ouch.getMessage());
                catalog.scanner().handle(ouch);
                }
            }
        }

    protected void scan(final Map<String, JdbcSchema> existing, final Map<String, JdbcSchema> matching, final JdbcMetadataScanner.Schema schema)
        {
        String name = schema.catalog().name() + "." + schema.name() ;
        log.debug("Scanning for schema [{}]", name);
        //
        // Check for an existing match.
        if (existing.containsKey(name))
            {
            log.debug("Found existing schema [{}]", name);
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
            log.debug("Creating new schema [{}]", name);
            matching.put(
                name,
                factories().jdbc().schemas().create(
                    JdbcResourceEntity.this,
                    schema.catalog().name(),
                    schema.name()
                    )
                );
            }
        }

    /**
     * Generate the JDBC metadata.
     * 
     */
    protected JdbcResource.Metadata.Jdbc jdbcmeta()
        {
        return new JdbcResource.Metadata.Jdbc()
            {
            };
        }
    
    @Override
    public JdbcResource.Metadata meta()
        {
        return new JdbcResource.Metadata()
            {
            @Override
            public Jdbc jdbc()
                {
                return jdbcmeta();
                }
            @Override
            public Ogsa ogsa()
                {
                return ogsameta();
                }
            };
        }
    }
