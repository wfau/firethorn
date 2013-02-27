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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.Parent;
import org.hibernate.exception.internal.StandardSQLExceptionConverter;
import org.hibernate.exception.spi.SQLExceptionConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.BeanFactoryDataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jdbc.support.SQLExceptionSubclassTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import uk.ac.roe.wfau.firethorn.meta.base.BaseObject;

/**
 * JDBC resource connection details.
 *
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class JdbcConnectionEntity
    extends BaseObject
    implements JdbcConnection
    {
    /**
     * Hibernate column mapping.
     * 
     */
    protected static final String DB_JDBC_URL_COL    = "jdbcurl";
    protected static final String DB_JDBC_USER_COL   = "jdbcuser";
    protected static final String DB_JDBC_PASS_COL   = "jdbcpass";
    protected static final String DB_JDBC_STATUS_COL = "jdbcstatus";
    protected static final String DB_JDBC_DRIVER_COL = "jdbcdriver";

    /**
     * Our Spring SQLException translator.
     *
     */
    protected static final SQLExceptionTranslator translator = new SQLExceptionSubclassTranslator();

    @Override
    public SQLExceptionTranslator translator()
        {
        return translator ;
        }

    /**
     * Our Hibernate SQLException converter.
     *
     */
    protected static final SQLExceptionConverter converter = new StandardSQLExceptionConverter();

    @Override
    public SQLExceptionConverter converter()
        {
        return converter ;
        }
    
    public JdbcConnectionEntity()
        {
        }

    public JdbcConnectionEntity(final String url)
        {
        this(
            null,
            url
            );
        }

    public JdbcConnectionEntity(final JdbcResourceEntity parent)
        {
        this(
            parent,
            null
            );
        }

    public JdbcConnectionEntity(final JdbcResourceEntity parent, final String url)
        {
        this.url    = url ;
        this.parent = parent;
        }

    public JdbcConnectionEntity(final JdbcResourceEntity parent, final String url, final String user, final String pass)
	    {
	    this.url    = url ;
	    this.user   = user;
	    this.pass   = pass;
	    this.parent = parent;
	    }

    @Parent
    protected JdbcResourceEntity parent;
    protected JdbcResourceEntity getParent()
        {
        return this.parent;
        }
    protected void setParent(final JdbcResourceEntity parent)
        {
        this.parent = parent;
        }

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_URL_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String url = "";
    @Override
    public String uri()
        {
        return this.url;
        }
    @Override
    public synchronized void url(final String url)
        {
        this.url = url;
        this.reset();
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

    @Basic(
        fetch = FetchType.EAGER
        )
    @Column(
        name = DB_JDBC_DRIVER_COL,
        unique = false,
        nullable = true,
        updatable = true
        )
    private String driver;
    @Override
    public String driver()
        {
        return this.driver;
        }
    @Override
    public synchronized void driver(final String driver)
        {
        this.driver = driver;
        this.reset();
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
     * Initialise our JDBC DataSource and create a Connection.
     * @todo Messed up and not thread safe at all.
     * 
     */
    private synchronized Connection connect()
        {
        log.debug("connect()");

        if (this.state == State.CLOSED)
            {
            log.debug("State is CLOSED, updating to READY");
            this.state = State.READY;
            }

        if (this.source == null)
            {
            log.debug("Source is null, updating to INIT");
            this.state = State.EMPTY;
            }

        if (this.state == State.EMPTY)
            {
            log.debug("State is INIT, initialising DataSource");
            if (this.url == null)
                {
                this.state = State.INVALID;
                throw new IllegalArgumentException(
                    "JDBC connection URL required"
                    );
                }

            else if (this.url.equals(""))
                {
                this.state = State.INVALID;
                throw new IllegalArgumentException(
                    "JDBC connection URL required"
                    );
                }

            else if (this.url.startsWith(JDBC_URL_PREFIX))
                {
                if (this.driver != null)
                    {
                    try {
                        Class.forName(
                            this.driver
                            );
                        }
                    catch (final Exception ouch)
                        {
                        this.state = State.FAILED;
                        log.error("Unable to load JDBC driver [{}]", this.driver);
                        throw new RuntimeException(
                            ouch
                            );
                        }
                    }
                this.source = new DriverManagerDataSource(
                    this.url
                    );
                this.state = State.READY;
                }

            else if (this.url.startsWith(JNDI_URL_PREFIX))
                {
                try {
                    final DataSourceLookup resolver = new JndiDataSourceLookup();
                    this.source = resolver.getDataSource(
                        this.url.substring(
                            JNDI_URL_PREFIX.length()
                            )
                        );
                    this.state = State.READY;
                    }
                catch (final DataSourceLookupFailureException ouch)
                    {
                    this.state = State.FAILED;
                    log.error("Unable to resolve DataSource [{}]", this.url);
                    throw ouch;
                    }
                catch (final Exception ouch)
                    {
                    this.state = State.FAILED;
                    log.error("Unable to resolve DataSource [{}]", this.url);
                    throw new RuntimeException(
                        ouch
                        );
                    }
                }

            else if (this.url.startsWith(SPRING_URL_PREFIX))
                {
                try {
                    final DataSourceLookup resolver = new BeanFactoryDataSourceLookup(
                        this.factories().spring().context()
                        );
                    this.source = resolver.getDataSource(
                        this.url.substring(
                            SPRING_URL_PREFIX.length()
                            )
                        );
                    this.state = State.READY;
                    }
                catch (final DataSourceLookupFailureException ouch)
                    {
                    this.state = State.FAILED;
                    log.error("Unable to resolve DataSource [{}]", this.url);
                    throw ouch;
                    }
                catch (final Exception ouch)
                    {
                    this.state = State.FAILED;
                    log.error("Unable to resolve DataSource [{}]", this.url);
                    throw new RuntimeException(
                        ouch
                        );
                    }
                }

            else if (this.url.startsWith(TEST_URL_PREFIX))
                {
                this.source = null ;
                this.state  = State.READY;
                }

            else {
                this.state = State.INVALID;
                log.error("Unexpected prefix for JDBC connection URL [{}]", this.url);
                throw new IllegalArgumentException(
                    "Unexpected prefix for JDBC connection URL [" + this.url + "]"
                    );
                }
            }
        
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
                    try {
                        Connection connection = this.source.getConnection(
                            this.user,
                            this.pass
                            );
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
                    try {
                        Connection connection = this.source.getConnection();
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

    /*
     * ThreadLocal database Connection.
     *
     */
    @Transient
    private final ThreadLocal<Connection> local = new ThreadLocal<Connection>()
        {
        @Override
        public Connection get()
            {
            return super.get();
            }

        @Override
        public void set(final Connection connection)
            {
            super.set(
                connection
                );
            }

        @Override
        protected Connection initialValue()
            {
            return connect();
            }
        };

    /**
     * Reset our JDBC DataSource.
     *
     */
    protected synchronized void reset()
        {
        log.debug("reset()");
        this.source = null ;
        this.state = State.EMPTY; 
        this.local.remove();
        }

    @Override
    public Connection open()
        {
        log.debug("open()");
        synchronized (this.local)
            {
            return this.local.get();
            }
        }

    @Override
    public void close()
        {
        log.debug("close()");
        synchronized (this.local)
            {
            try {
                if (this.state == State.CONNECTED)
                    {
                    Connection connection = this.local.get();
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

    @Override
    public DatabaseMetaData metadata()
        {
        Connection connection = open();
        if (connection != null)
            {
            try {
                return connection.getMetaData();
                }
            catch (final SQLException ouch)
                {
                log.warn("Exception fetching JDBC metadata [{}]", ouch.getMessage());
                throw new JdbcMetadataAccessException(
                    ouch
                    );
                }
            }
        return null ;
        }

    @Override
    public String catalog()
        {
        Connection connection = open();
        if (connection != null)
            {
            try {
                return connection.getCatalog();
                }
            catch (final SQLException ouch)
                {
                log.warn("Exception fetching JDBC catalog name [{}]", ouch.getMessage());
                throw new JdbcMetadataAccessException(
                    ouch
                    );
                }
            }
        return null ;
        }

    @Override
    public Iterable<String> catalogs()
        {
        final List<String> catalogs = new ArrayList<String>();
        try {
            final DatabaseMetaData metadata = this.metadata();
            final ResultSet results = metadata.getCatalogs();
            while (results.next())
                {
                catalogs.add(
                    results.getString(
                        JdbcMetadata.JDBC_META_TABLE_CAT
                        )
                    );
                }
            }
        catch(final SQLException ouch)
            {
            log.warn("Exception fetching JDBC catalog list [{}]", ouch.getMessage());
            throw new JdbcMetadataAccessException(
                ouch
                );
            }
        return catalogs;
        }

    /**
     * The component status.
     *
     */
    @Column(
        name = DB_JDBC_STATUS_COL,
        unique = false,
        nullable = false,
        updatable = true
        )
    @Enumerated(
        EnumType.STRING
        )
    private Status status = Status.CREATED;
    @Override
    public Status status()
        {
        return this.status;
        }

    @Override
    public void status(final Status value)
        {
        switch(value)
            {
            case ENABLED:
                this.status = Status.ENABLED;
                if (this.parent != null)
                    {
                    try {
                        this.parent.scan();
                        }
                    catch (final RuntimeException ouch)
                        {
                        this.status = Status.FAILED;
                        throw ouch;
                        }
                    }
                break;
            case DISABLED:
                this.status = Status.DISABLED;
                break ;
            default :
                throw new IllegalArgumentException(
                    "Invalid status update [" + value.name() + "]"
                    );
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

    /**
     * The connection state.
     * 
     */
    public State state()
        {
        return this.state;
        }

    /**
     * The connection state.
     * 
     */
    public void state(State state)
        {
        this.state = state ;
        }
    }
