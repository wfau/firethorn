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
package uk.ac.roe.wfau.firethorn.tuesday;

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

/**
 * JDBC resource connection details.
 *
 */
@Slf4j
@Embeddable
@Access(
    AccessType.FIELD
    )
public class TuesdayJdbcConnectionEntity
    extends TuesdayBaseObject
    implements TuesdayJdbcConnection
    {
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

    /**
     * Our Hibernate SQLException converter.
     *
     */
    protected static final SQLExceptionConverter converter = new StandardSQLExceptionConverter();

    public TuesdayJdbcConnectionEntity()
        {
        }

    public TuesdayJdbcConnectionEntity(final String url)
        {
        this(
            null,
            url
            );
        }

    public TuesdayJdbcConnectionEntity(final TuesdayJdbcResourceEntity parent)
        {
        this(
            parent,
            null
            );
        }

    public TuesdayJdbcConnectionEntity(final TuesdayJdbcResourceEntity parent, final String url)
        {
        this.url    = url ;
        this.parent = parent;
        }

    public TuesdayJdbcConnectionEntity(final TuesdayJdbcResourceEntity parent, final String url, final String user, final String pass)
	    {
	    this.url    = url ;
	    this.user   = user;
	    this.pass   = pass;
	    this.parent = parent;
	    }

    @Parent
    protected TuesdayJdbcResourceEntity parent;
    protected TuesdayJdbcResourceEntity getParent()
        {
        return this.parent;
        }
    protected void setParent(final TuesdayJdbcResourceEntity parent)
        {
        this.parent = parent;
        }

    @Basic(fetch = FetchType.EAGER)
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

    @Basic(fetch = FetchType.EAGER)
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

    @Basic(fetch = FetchType.EAGER)
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

    @Basic(fetch = FetchType.EAGER)
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
     * Our JDBC DataSource.
     *
     */
    @Transient
    private DataSource source ;

    /**
     * Initialise our JDBC DataSource.
     *
     */
    protected synchronized DataSource source()
        {
        if (this.source == null)
            {
            try {
                if (this.url == null)
                    {
                    throw new IllegalArgumentException(
                        "JDBC connection URL required"
                        );
                    }
                else if (this.url.equals(""))
                    {
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
                            log.error("Unable to load JDBC driver [{}]", this.driver);
                            throw new RuntimeException(
                                ouch
                                );
                            }
                        }
                    this.source = new DriverManagerDataSource(
                        this.url
                        );
                    }
                else if (this.url.startsWith(JNDI_URL_PREFIX))
                    {
                    final DataSourceLookup resolver = new JndiDataSourceLookup();
                    this.source = resolver.getDataSource(
                        this.url.substring(
                            JNDI_URL_PREFIX.length()
                            )
                        );
                    }
                else if (this.url.startsWith(SPRING_URL_PREFIX))
                    {
                    final DataSourceLookup resolver = new BeanFactoryDataSourceLookup(
                        this.factories().spring().context()
                        );
                    this.source = resolver.getDataSource(
                        this.url.substring(
                            SPRING_URL_PREFIX.length()
                            )
                        );
                    }
                else {
                    log.error("Unexpected prefix for JDBC connection URL [{}]", this.url);
                    throw new IllegalArgumentException(
                        "Unexpected prefix for JDBC connection URL [" + this.url + "]"
                        );
                    }
                }
            catch (final DataSourceLookupFailureException ouch)
                {
                log.error("Unable to locate DataSource [{}]", this.url);
                throw ouch;
                }
            }
        return this.source ;
        }

    @Transient
    private final ThreadLocal<Connection> local = new ThreadLocal<Connection>()
        {
        @Override
        public Connection get()
            {
            //log.debug("ThreadLocal<Connection>.get()");
            return super.get();
            }

        @Override
        public void set(final Connection connection)
            {
            //log.debug("ThreadLocal<Connection>.set()");
            super.set(
                connection
                );
            }

        @Override
        protected Connection initialValue()
            {
            //log.debug("ThreadLocal<Connection>.initialValue()");
            try {
                if (user() != null)
                    {
                    return source().getConnection(
                        user(),
                        pass()
                        );
                    }
                else {
                    return source().getConnection() ;
                    }
                }
            catch (final SQLException ouch)
                {
                throw new TuesdayJdbcConnectionFailedException(
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
        //log.debug("reset()");
        this.source = null ;
        this.local.remove();
        }

    @Override
    public Connection open()
        {
        return this.local.get();
        }

    @Override
    public DatabaseMetaData metadata()
        {
        try {
            return this.local.get().getMetaData();
            }
        catch (final SQLException ouch)
            {
            throw new TuesdayJdbcMetadatAccessException(
                ouch
                );
            }
        }

    @Override
    public void close()
        {
        synchronized (this.local)
            {
            try {
                // TODO Need to be able to 'peek' at the connection.
                // Otherwise, if the connection has failed, this just tries to open it again.
                this.local.get().close();
                }
            catch (final Throwable ouch)
                {
                log.error("Error closing database connection [{}]", ouch.getMessage());
                }
            finally {
                this.local.remove();
                }
            }
        }

    @Override
    public String catalog()
        {
        try {
            return this.local.get().getCatalog();
            }
        catch(final Exception ouch)
            {
            //log.error("Exception reading database metadata", ouch);
            return null ;
            }
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
                        TuesdayJdbcResource.JDBC_META_TABLE_CAT
                        )
                    );
                }
            }
        catch(final SQLException ouch)
            {
            log.error("Exception reading database metadata [{}]", ouch.getMessage());
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
    public void status(final Status update)
        {
        switch(update)
            {
            case ENABLED:
                this.status = Status.ENABLED;
                if (this.parent != null)
                    {
                    try {
                        this.parent.inport();
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
                    "Invalid status update [" + update.name() + "]"
                    );
            }
        }
    }
