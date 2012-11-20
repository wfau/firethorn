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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc ;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.BeanFactoryDataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Repository;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResourceEntity;
import uk.ac.roe.wfau.firethorn.widgeon.jdbc.JdbcResource.JdbcConnection;

/**
 * Hibernate based <code>JdbcResource</code> implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@DiscriminatorValue(
    value=JdbcResourceEntity.DB_CLASS_TYPE
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "jdbc.resource-select-all",
            query = "FROM JdbcResourceEntity ORDER BY name asc, ident desc"
            ),
            @NamedQuery(
                name  = "jdbc.resource-select-name",
                query = "FROM JdbcResourceEntity WHERE (name = :name) ORDER BY ident desc"
                ),
        @NamedQuery(
            name  = "jdbc.resource-search-text",
            query = "FROM JdbcResourceEntity WHERE (name LIKE :text) ORDER BY ident desc"
            )
        }
    )
@org.hibernate.annotations.Table(
    appliesTo = JdbcResourceEntity.DB_TABLE_NAME, 
    indexes =
        {
        @Index(
            name= JdbcResourceEntity.DB_NAME_IDX,
            columnNames =
                {
                AbstractEntity.DB_NAME_COL
                }
            )
        }
    )
public class JdbcResourceEntity
extends BaseResourceEntity
implements JdbcResource
    {

    /**
     * Our persistence type value.
     *
     */
    public static final String DB_CLASS_TYPE = "JDBC" ;

    /**
     * The index for our name.
     *
     */
    public static final String DB_NAME_IDX = "jdbc_resource_name_idx" ;

    /**
     * The connection url column name .
     *
     */
    public static final String DB_CONNECT_URL_COL = "connect_url" ;

    /**
     * The connection driver class column name .
     *
     */
    public static final String DB_CONNECT_DRIVER_COL = "connect_driver" ;

    /**
     * The connection username column name .
     *
     */
    public static final String DB_CONNECT_USER_COL = "connect_user" ;

    /**
     * The connection password column name .
     *
     */
    public static final String DB_CONNECT_PASS_COL = "connect_pass" ;
    
    /**
     * Our Entity Factory implementation.
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
                    "jdbc.resource-select-all"
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> select(final String name)
            {
            return super.iterable(
                super.query(
                    "jdbc.resource-select-name"
                    ).setString(
                        "name",
                        name
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<JdbcResource> search(final String text)
            {
            return super.iterable(
                super.query(
                    "jdbc.resource-search-text"
                    ).setString(
                        "text",
                        searchParam(
                            text
                            )
                        )
                );
            }

        @Override
        @CreateEntityMethod
        public JdbcResource create(final String name)
            {
            return super.insert(
                new JdbcResourceEntity(
                    name
                    )
                );
            }

        @Autowired
        protected AdqlResource.Factory views ;

        @Override
        public AdqlResource.Factory views()
            {
            return this.views ;
            }

        @Autowired
        protected JdbcCatalog.Factory catalogs ;

        @Override
        public JdbcCatalog.Factory catalogs()
            {
            return this.catalogs ;
            }

        @Autowired
        protected JdbcResource.IdentFactory identifiers ;

        @Override
        public JdbcResource.IdentFactory identifiers()
            {
            return identifiers ;
            }
        }


    @Override
    public JdbcResource.Catalogs catalogs()
        {
        return new JdbcResource.Catalogs()
            {
            @Override
            public JdbcCatalog create(final String name)
                {
                return womble().jdbc().catalogs().create(
                    JdbcResourceEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcCatalog> select()
                {
                return womble().jdbc().catalogs().select(
                    JdbcResourceEntity.this
                    );
                }

            @Override
            public JdbcCatalog select(final String name)
                {
                return womble().jdbc().catalogs().select(
                    JdbcResourceEntity.this,
                    name
                    );
                }

            @Override
            public Iterable<JdbcCatalog> search(final String text)
                {
                return womble().jdbc().catalogs().search(
                    JdbcResourceEntity.this,
                    text
                    );
                }

            @Override
            public List<JdbcDiference> diff(final boolean push, final boolean pull)
                {
                return diff(
                    jdbc().metadata(),
                    new ArrayList<JdbcDiference>(),
                    push,
                    pull
                    );
                }

            @Override
            public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference>  results, final boolean push, final boolean pull)
                {
                log.debug("Comparing catalogs for resource [{}]", name());
                try {
                    //
                    // Scan the DatabaseMetaData for catalogs.
                    final ResultSet catalogs = metadata.getCatalogs();
                    final Map<String, JdbcCatalog> found = new HashMap<String, JdbcCatalog>();
                    while (catalogs.next())
                        {
                        final String name = catalogs.getString(
                            JdbcResource.JDBC_META_TABLE_CAT
                            );
                        log.debug("Checking database catalog [{}]", name);

                        JdbcCatalog catalog = this.select(
                            name
                            );
                        if (catalog == null)
                            {
                            log.debug("Database catalog [{}] is not registered", name);
                            if (pull)
                                {
                                log.debug("Registering missing catalog [{}]", name);
                                catalog = this.create(
                                    name
                                    );
                                }
                            else if (push)
                                {
                                log.debug("Deleting database catalog [{}]", name);
                                log.error("-- delete catalog -- ");
                                }
                            else {
                                results.add(
                                    new JdbcDiference(
                                        JdbcDiference.Type.CATALOG,
                                        null,
                                        name
                                        )
                                    );
                                }
                            }
                        if (catalog != null)
                            {
                            found.put(
                                name,
                                catalog
                                );
                            }
                        }
                    //
                    // Scan our own list of catalogs.
                    for (final JdbcCatalog catalog : select())
                        {
                        log.debug("Checking registered catalog [{}]", catalog.name());
                        JdbcCatalog match = found.get(
                            catalog.name()
                            );
                        //
                        // If we didn't find a match, create the object or disable our entry.
                        if (match == null)
                            {
                            log.debug("Registered catalog [{}] is not in database", catalog.name());
                            if (push)
                                {
                                log.debug("Creating database catalog [{}]", catalog.name());
                                log.error("-- create catalog -- ");
                                match = catalog ;
                                }
                            else if (pull)
                                {
                                log.debug("Disabling registered catalog [{}]", catalog.name());
                                catalog.status(
                                    Status.MISSING
                                    );
                                }
                            else {
                                results.add(
                                    new JdbcDiference(
                                        JdbcDiference.Type.CATALOG,
                                        catalog.name(),
                                        null
                                        )
                                    );
                                }
                            }
                        //
                        // If we have a match, then scan it.
                        if (match != null)
                            {
                            match.diff(
                                metadata,
                                results,
                                push,
                                pull
                                );
                            }
                        }
                    }
                catch (final SQLException ouch)
                    {
                    log.error("Error processing JDBC catalogs", ouch);
                    }
                return results ;
                }

            @Override
            public void scan()
                {
                this.scan(
                    jdbc().metadata()
                    );
                }

            @Override
            public void scan(final DatabaseMetaData metadata)
                {
                log.debug("Comparing catalogs for resource [{}]", name());
                try {
                    //
                    // Scan the DatabaseMetaData for catalogs.
                    final ResultSet catalogs = metadata.getCatalogs();
                    final Map<String, JdbcCatalog> found = new HashMap<String, JdbcCatalog>();
                    while (catalogs.next())
                        {
                        final String name = catalogs.getString(
                            JdbcResource.JDBC_META_TABLE_CAT
                            );
                        log.debug("Checking database catalog [{}]", name);

                        JdbcCatalog catalog = this.select(
                            name
                            );
                        if (catalog == null)
                            {
                            log.debug("Database catalog [{}] is not registered", name);
                            log.debug("Registering missing catalog [{}]", name);
                            catalog = this.create(
                                name
                                );
                            }
                        if (catalog != null)
                            {
                            found.put(
                                name,
                                catalog
                                );
                            }
                        }
                    //
                    // Scan our own list of catalogs.
                    for (final JdbcCatalog catalog : select())
                        {
                        log.debug("Checking registered catalog [{}]", catalog.name());
                        //
                        // Check for a corresponding entry in the list of components we have already found.
                        JdbcCatalog match = found.get(
                            catalog.name()
                            );
                        //
                        // If we didn't find a match, disable our entry.
                        if (match == null)
                            {
                            log.debug("Registered catalog [{}] is not in database", catalog.name());
                            log.debug("Disabling registered catalog [{}]", catalog.name());
                            catalog.status(
                                Status.MISSING
                                );
                            }
                        }
                    }
                catch (final SQLException ouch)
                    {
                    log.error("Error processing JDBC catalog metadata", ouch);
                    status(
                        Status.DISABLED,
                        "SQLException while processing JDBC catalog metadata"
                        );
                    }
                }
            };
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected JdbcResourceEntity()
        {
        super();
        }

    /**
     * Create a new resource.
     *
     */
    private JdbcResourceEntity(final String name)
        {
        super(name);
        }

    /**
     * Our JDBC connection URL.
     *
     */
    @Column(
        name = DB_CONNECT_URL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String connectUrl;

    /**
     * Our JDBC driver class name.
     *
     */
    @Column(
        name = DB_CONNECT_DRIVER_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String connectDriver;
        
    /**
     * Our JDBC connection user name.
     *
     */
    @Column(
        name = DB_CONNECT_USER_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String connectUser ;
    
    /**
     * Our JDBC connection password.
     *
     */
    @Column(
        name = DB_CONNECT_PASS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String connectPass ;

    /**
     * JDBC connection URL prefix for a DriverManager connection.
     * 
     */
    public static final String JDBC_JDBC_URL_PREFIX = "jdbc:" ;

    /**
     * JDBC connection URL prefix for a JNDI registered DataSource.
     * 
     */
    public static final String JDBC_JNDI_URL_PREFIX = "jndi:" ;

    /**
     * JDBC connection URL prefix for a Spring registered DataSource.
     * 
     */
    public static final String JDBC_SPRING_URL_PREFIX = "spring:" ;

    /**
     * Our JDBC DataSource.
     * 
     */
    @Transient
    private DataSource source ;

    /**
     * Initialize our JDBC DataSource.
     * 
     */
    protected synchronized DataSource source()
        {
        if (source == null)
            {
            try {
                if (connectUrl == null)
                    {
                    throw new IllegalArgumentException(
                        "JDBC connection URL required"
                        );
                    }
                else if (connectUrl.startsWith(JDBC_JDBC_URL_PREFIX))
                    {
                    if (connectDriver != null)
                        {
                        try {
                            Class.forName(
                                connectDriver
                                );
                            }
                        catch (final Exception ouch)
                            {
                            log.error("Unable to load JDBC driver [{}]", connectDriver);
                            throw new RuntimeException(
                                ouch
                                );
                            }
                        }
                    source = new DriverManagerDataSource(
                        connectUrl
                        );
                    }
                else if (connectUrl.startsWith(JDBC_JNDI_URL_PREFIX))
                    {
                    DataSourceLookup resolver = new JndiDataSourceLookup();
                    source = resolver.getDataSource(
                        connectUrl.substring(
                            JDBC_JNDI_URL_PREFIX.length()
                            )
                        );
                    }
                else if (connectUrl.startsWith(JDBC_SPRING_URL_PREFIX))
                    {
                    DataSourceLookup resolver = new BeanFactoryDataSourceLookup(
                        womble().spring().context()
                        );
                    source = resolver.getDataSource(
                        connectUrl.substring(
                            JDBC_SPRING_URL_PREFIX.length()
                            )
                        );
                    }
                else {
                    log.error("Unexpected prefix for JDBC connection URL [{}]", connectUrl);
                    throw new IllegalArgumentException(
                        "Unexpected prefix for JDBC connection URL [" + connectUrl + "]" 
                        );
                    }
                }
            catch (final DataSourceLookupFailureException ouch)
                {
                log.error("Unable to locate DataSource [{}]", connectUrl);
                throw new RuntimeException(
                    "Unable to locate DataSource [" + connectUrl +"]"
                    );
                }
            }
        return source ;
        }

    @Transient
    private ThreadLocal<Connection> connector = new ThreadLocal<Connection>()
        {
        @Override
        public Connection get()
            {
            log.debug("ThreadLocal<Connection>.get()");
            return super.get();
            }

        @Override
        public void set(Connection connection)
            {
            log.debug("ThreadLocal<Connection>.set()");
            super.set(
                connection
                );
            }
        
        @Override
        protected Connection initialValue()
            {
            log.debug("ThreadLocal<Connection>.initialValue()");
            try {
                if (connectUser != null)
                    {
                    return source().getConnection(
                        connectUser,
                        connectPass
                        );
                    }
                else {
                    return source().getConnection() ;
                    }
                }
            catch (final SQLException ouch)
                {
                log.error("Unable to connect to database [{}]", ouch);
                throw new RuntimeException(
                    ouch
                    );
                }
            }
        }; 

    /**
     * Reset our JDBC DataSource.
     * 
     */
    protected synchronized void reset()
        {
        log.debug("reset()");
        source = null ;
        connector.remove();
        }
        
    @Override
    public JdbcConnection jdbc()
        {
        return new JdbcConnection()
            {
            
            @Override
            public String url()
                {
                return connectUrl ;
                }

            @Override
            public void url(String url)
                {
                connectUrl = url ;
                reset();
                }

            @Override
            public String user()
                {
                return connectUser ;
                }

            @Override
            public void user(String user)
                {
                connectUser = user ;
                reset();
                }

            @Override
            public String pass()
                {
                return connectPass;
                }

            @Override
            public void pass(String pass)
                {
                connectPass = pass ;
                reset();
                }

            @Override
            public String driver()
                {
                return connectDriver;
                }

            @Override
            public void driver(String driver)
                {
                connectDriver = driver ;
                reset();
                }

            @Override
            public Connection connection()
                {
                return connector.get();
                }

            @Override
            public DatabaseMetaData metadata()
                {
                try {
                    return connection().getMetaData();
                    }
                catch (final SQLException ouch)
                    {
                    log.error("Error fetching database metadata", ouch);
                    throw new RuntimeException(
                        ouch
                        );
                    }
                }

            @Override
            public void close()
                {
                synchronized (connector)
                    {
                    try {
                        connector.get().close();
                        }
                    catch (final SQLException ouch)
                        {
                        log.error("Error closing database connection", ouch);
                        throw new RuntimeException(
                            ouch
                            );
                        }
                    finally {
                        connector.remove();
                        }
                    }
                }
            };
        }
   

    @Override
    public List<JdbcDiference> diff(final boolean push, final boolean pull)
        {
        return diff(
            jdbc().metadata(),
            new ArrayList<JdbcDiference>(),
            push,
            pull
            );
        }

    @Override
    public List<JdbcDiference> diff(final DatabaseMetaData metadata, final List<JdbcDiference> results, final boolean push, final boolean pull)
        {
        log.debug("Comparing resource [{}]", name());
        //
        // Check this resource.
        // ....
        //
        // Check our catalogs.
        return this.catalogs().diff(
            metadata,
            results,
            push,
            pull
            );
        }

    @Override
    public String link()
        {
        return womble().jdbc().resources().link(
            this
            );
        }

    @Override
    public void scan()
        {
        this.scan(
            jdbc().metadata()
            );
        }

    @Override
    public void scan(final DatabaseMetaData metadata)
        {
        catalogs().scan(
            metadata
            );
        }
    }

