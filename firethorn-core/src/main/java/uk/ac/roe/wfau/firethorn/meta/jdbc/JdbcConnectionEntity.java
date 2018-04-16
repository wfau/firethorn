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

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import javax.sql.DataSource;

import org.hibernate.annotations.Parent;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlTranslator;
import uk.ac.roe.wfau.firethorn.exception.FirethornCheckedException;
import uk.ac.roe.wfau.firethorn.exception.JdbcConnectionException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.hsqldb.HsqldbOperator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.hsqldb.HsqldbScanner;
import uk.ac.roe.wfau.firethorn.meta.jdbc.hsqldb.HsqldbTranslator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.postgresql.PostgresOperator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.postgresql.PostgresScanner;
import uk.ac.roe.wfau.firethorn.meta.jdbc.postgresql.PostgresTranslator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.sqlserver.SQLServerOperator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.sqlserver.SQLServerScanner;
import uk.ac.roe.wfau.firethorn.meta.jdbc.sqlserver.SQLServerTranslator;
import uk.ac.roe.wfau.firethorn.spring.ComponentFactories;

/**
 * {@link JdbcConnector} implementation.
 * @todo This is not thread safe at all.
 * Miss-match, JDBC connections are held in a ThreadLocal, but the count of open/close is per entity, not per thread.
 *
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class JdbcConnectionEntity
implements JdbcConnector
    {
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_DATABASE_COL = "jdbcdatabase";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_CATALOG_COL = "jdbccatalog";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_TYPE_COL = "jdbctype";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_PROTO_COL = "jdbcproto";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_HOST_COL = "jdbchost";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_PORT_COL = "jdbcport";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_USER_COL = "jdbcuser";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_PASS_COL = "jdbcpass";
    /**
     * Hibernate column mapping, {@value}.
     *
     */
    protected static final String DB_JDBC_STATUS_COL = "jdbcstatus";

    /**
     * An Exception to indicate a problem accessing the JDBC metadata.
     *
     */
    public static class MetadataException
    extends FirethornCheckedException
        {
        /**
         * Serialzable version UID.
         *
         */
        private static final long serialVersionUID = -4567384444408505866L;

        /**
         * Public constructor.
         * @param cause
         *
         */
        public MetadataException(final SQLException cause)
            {
            super(
                cause
                );
            }

        /**
         * Public constructor.
         * @param message
         * @param cause
         *
         */
        public MetadataException(final String message, final SQLException cause)
            {
            super(
                message,
                cause
                );
            }
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcConnectionEntity()
        {
        }

    /**
     * Protected constructor.
     *
     */
    protected JdbcConnectionEntity(final JdbcResourceEntity resource, final JdbcProductType type, final String database, final String catalog, final String host, final Integer port, final String user, final String pass)
        {
        this.resource = resource;
        this.database = database;
        this.catalog = catalog;
        this.type = type;
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        }
    
    /**
     * Access to our ComponentFactories singleton instance.
     *
     */
    protected ComponentFactories factories()
        {
        return resource.factories();
        }
    
    @Parent
    protected JdbcResourceEntity resource;
    protected JdbcResourceEntity resource()
        {
        return this.resource;
        }
    protected void resource(final JdbcResourceEntity resource)
        {
        this.resource = resource;
        }

    /**
     * Get/Set methods for Hibernate.
     * 
     * @return
     */
    protected JdbcResourceEntity getResource()
        {
        return this.resource;
        }
    protected void setResource(final JdbcResourceEntity resource)
        {
        this.resource = resource;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_DATABASE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String database ;
    @Override
    public String database()
        {
        return this.database;
        }
    @Override
    public void database(final String database)
        {
        this.database = database;
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
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_HOST_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String host;
    @Override
    public String host()
        {
        return this.host;
        }
    @Override
    public void host(final String host)
        {
        this.host = host;
        this.reset();
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_PORT_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private Integer port;
    @Override
    public Integer port()
        {
        return this.port;
        }
    @Override
    public void port(Integer port)
        {
        this.port = port ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_USER_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String user;
    @Override
    public String user()
        {
        return this.user;
        }
    @Override
    public synchronized void user(final String user)
        {
        this.user = user;
        this.reset();
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_PASS_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String pass;
    @Override
    public String pass()
        {
        return this.pass;
        }
    @Override
    public synchronized void pass(final String pass)
        {
        this.pass = pass;
        this.reset();
        }

    /**
     * Our JDBC DataSource.
     *
     */
    @Transient
    private DataSource source ;

    /**
     * Our JDBC DataSource.
     *
     */
    public DataSource source()
        {
        return this.source;
        }

    /**
     * JDBC connection URL prefix for a DriverManager connection.
     *
     */
    public static final String JDBC_URL_PREFIX = "jdbc:" ;

    /**
     * JDBC connection URL prefix for a JNDI registered DataSource.
     *
     */
    public static final String JNDI_URL_PREFIX = "jndi:" ;

    /**
     * JDBC connection URL prefix for a Spring registered DataSource.
     *
     */
    public static final String SPRING_URL_PREFIX = "spring:" ;

    /**
     * JDBC connection URL prefix for a test resource.
     *
     */
    public static final String TEST_URL_PREFIX = "test:" ;

    @Override
    public String url()
        {
        return this.operator().url();
        }

    /**
     * Our JDBC {@link Driver}.
     * 
    public Driver driver()
        {
        return null ;
        }
     */

    
    
    /**
     * Initialise our JDBC DataSource and create a Connection.
     *
     */
    private synchronized Connection connect()
        {
        log.debug("connect [{}]", this.url());

        if (this.state == State.CLOSED)
            {
            log.debug("State is CLOSED, updating to READY");
            this.state = State.READY;
            }

        if (this.source == null)
            {
            log.debug("Source is null, updating to EMPTY");
            this.state = State.EMPTY;
            }

        if (this.state == State.EMPTY)
            {
            // Use a pooled connection
            log.debug("State is EMPTY, initialising DataSource");
            this.source = new SimpleDriverDataSource(
                this.operator().driver(),
                this.operator().url(),
                this.user,
                this.pass
                );            
            this.state = State.READY;
            }

       /*
        * http://www.mchange.com/projects/c3p0/#using_datasources_factory
        * Need to have a Map<uuid,ComboPooledDataSource> for this to make sense. 
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "org.postgresql.Driver" );
        cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
        cpds.setUser("swaldman");
        cpds.setPassword("test-password");
        
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);        
        * 
        */
        
        if (this.state == State.READY)
            {
            log.debug("State is READY, initialising Connection");
            if (this.source == null)
                {
                log.error("Null DataSource when state is READY");
                this.state = State.INVALID;
                }
            else {
                if (this.user != null)
                    {
                    log.debug("With user/pass");
                    try {
                        log.debug("connecting ....");
                        final Connection connection = this.source.getConnection(
                            this.user,
                            this.pass
                            );
                        log.debug(".... connected");
                        state(State.CONNECTED);
                        return connection;
                        }
                    catch (final SQLException ouch)
                        {
                        state(State.FAILED);
                        throw new JdbcConnectionFailedException(
                            ouch
                            );
                        }
                    }
                else {
                    log.debug("No auth");
                    try {
                        log.debug("connecting ....");
                        final Connection connection = this.source.getConnection();
                        log.debug(".... connected");
                        state(State.CONNECTED);
                        return connection ;
                        }
                    catch (final SQLException ouch)
                        {
                        state(State.FAILED);
                        throw new JdbcConnectionFailedException(
                            ouch
                            );
                        }
                    }
                }
            }
        return null ;
        }

    /**
     * ThreadLocal database Connection.
     * This is generally a bad idea.
     *
     */
    @Transient
    private final ThreadLocal<Connection> local = new ThreadLocal<Connection>()
        {
        @Override
        public Connection get()
            {
            log.debug("get() [{}]", url());
            return super.get();
            }

        @Override
        public void set(final Connection connection)
            {
            log.debug("set() [{}]", url());
            super.set(
                connection
                );
            }

        @Override
        protected Connection initialValue()
            {
            log.debug("initialValue() [{}]", url());
            return connect();
            }
        };

    /**
     * Reset our connection.
     *
     */
    @Override
    public synchronized void reset()
        {
        log.debug("reset [{}]", url());
        try {
            final Connection connection = this.local.get();
            if (connection != null)
                {
                connection.close();
                }
            }
        catch (final Throwable ouch)
            {
            this.state = State.FAILED;
            log.error("Error closing database connection [{}]", ouch.getMessage());
            }
        finally {
            this.source = null ;
            this.opens = 0;
            this.state = State.EMPTY;
            this.local.remove();
            }
        }

    @Transient
    private int opens;
    private static final boolean DEBUG_NESTED_CONNECTS = false ;

    @Override
    public Connection open()
        {
        synchronized (this.local)
            {
            opens++;
            log.debug("open [{}][{}]", url(), opens);
            if (opens > 1)
                {
                if (DEBUG_NESTED_CONNECTS)
                    {
                    log.error("Duplicate call to open");
                    throw new JdbcConnectionException(
                        "Database connection already open"
                        );
                    }
                }
            return this.local.get();
            }
        }

    @Override
    public void close()
        {
        synchronized (this.local)
            {
            log.debug("close [{}][{}]", url(), opens);
            opens--;
            if (opens < 0)
                {
                if (DEBUG_NESTED_CONNECTS)
                    {
                    log.error("Out of sequence call to close");
                    throw new JdbcConnectionException(
                        "Database connection already closed"
                        );
                    }
                }
            if (opens == 0)
                {
                try {
                    if (this.state == State.CONNECTED)
                        {
                        final Connection connection = this.local.get();
                        if (connection != null)
                            {
                            connection.close();
                            }
                        this.state = State.CLOSED;
                        }
                    }
                catch (final Throwable ouch)
                    {
                    this.state = State.FAILED;
                    log.error("Error closing database connection [{}]", ouch.getMessage());
                    }
                finally {
                    this.local.remove();
                    }
                }
            }
        }

    /**
     * This connection state.
     *
     */
    public enum State
        {
        EMPTY(),
        RESET(),
        READY(),
        CONNECTED(),
        CLOSED(),
        FAILED(),
        INVALID(),
        UNKNOWN();
        }

    /**
     * The connection state.
     *
     */
    @Transient
    private State state = State.EMPTY ;
    @Override
    public State state()
        {
        return this.state;
        }

    /**
     * The connection state.
     *
     */
    public void state(final State state)
        {
        this.state = state ;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Enumerated(
        EnumType.STRING
        )
    @Column(
        name = DB_JDBC_TYPE_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private JdbcProductType type;
    @Override
    public JdbcProductType type()
        {
        return this.type;
        }
    @Override
    public void type(final JdbcProductType type)
        {
        this.type = type ;
        }

    /**
     * Get the corresponding {@link JdbcMetadataScanner} for this type.
     * 
     */
    public JdbcMetadataScanner scanner()
        {
        log.debug("scanner() for [{}]", this.type().name());
        switch (this.type())
            {
            case hsqldb :
                return new HsqldbScanner(
                    this
                    );

            case mssql :
                return new SQLServerScanner(
                    this
                    );

            case pgsql :
                return new PostgresScanner(
                    this
                    );
            
            default : throw new RuntimeException(
                "Unable to load scanner for type [" + this.type + "]"
                );
            }
        }

    @Override
    public JdbcOperator operator()
        {
        log.debug("operator() for [{}]", this.type().name());
        switch (this.type())
            {
            case hsqldb:
                return new HsqldbOperator(
                    this
                    );
            
            case pgsql:
                return new PostgresOperator(
                    this
                    );

            case mssql:
                return new SQLServerOperator(
                    this
                    );

            default : throw new RuntimeException(
                "Unable to load operator for type [" + this.type + "]"
                );
            }
        }

    @Override
    public AdqlTranslator translator()
        {
        log.debug("translator() for [{}]", this.type().name());
        switch (this.type())
            {
            case hsqldb:
                return new HsqldbTranslator();
            
            case pgsql:
                return new PostgresTranslator();
            
            case mssql:
                return new SQLServerTranslator();

            default : throw new RuntimeException(
                "Unable to load translator for type [" + this.type + "]"
                );
            }
        }
    }