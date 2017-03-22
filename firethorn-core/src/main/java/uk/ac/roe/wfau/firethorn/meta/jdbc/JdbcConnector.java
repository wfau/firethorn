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

import org.hibernate.exception.spi.SQLExceptionConverter;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import uk.ac.roe.wfau.firethorn.adql.parser.BaseTranslator;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcConnectionEntity.MetadataException;

/**
 * JDBC resource connection details.
 *
 */
public interface JdbcConnector
    {
    /**
     * Get the connection URL (as a String).
     *
     */
    public String uri();

    /**
     * Set the connection URL (as a String).
     *
     */
    public void url(final String url);

    /**
     * Get the JDBC driver class name.
     *
     */
    public String driver();

    /**
     * Set the JDBC driver class name.
     *
     */
    public void driver(final String driver);

    /**
     * Get the JDBC user name.
     *
     */
    public String user();

    /**
     * Set the JDBC user name.
     *
     */
    public void user(final String user);

    /**
     * Get the JDBC password.
     *
     */
    public String pass();

    /**
     * Set the JDBC password.
     *
     */
    public void pass(final String pass);

    /**
     * Open a database Connection.
     *
     */
    public Connection open();

    /**
     * Get the database metadata.
     * @throws MetadataException
     *
     */
    public DatabaseMetaData metadata()
    throws MetadataException;

    /**
     * Close the current database Connection.
     *
     */
    public void close();

    /**
     * Reset the database Connection.
     *
     */
    public void reset();

    /**
     * Get a list of catalog names.
     * @throws MetadataException
     *
     */
    public Iterable<String> catalogs()
    throws MetadataException;

    /**
     * Get the catalog name of the current database Connection.
     * @throws MetadataException
     *
     */
    public String catalog()
    throws MetadataException;

    /**
     * Our Spring SQLException translator.
     *
     */
    public SQLExceptionTranslator translator();

    /**
     * Our Hibernate SQLException converter.
     *
     */
    public SQLExceptionConverter converter();

    /**
     * Get the database type.
     *
     */
    public JdbcProductType type();

    /**
     * Set the database type.
     *
     */
    public void type(final JdbcProductType type);

    /**
     * Get the {@link JdbcMetadataScanner} for this database.
     * 
     */
    public JdbcMetadataScanner scanner();

    /**
     * Get the {@link JdbcResource.JdbcDriver} for this database.
     * 
     */
    public JdbcResource.JdbcDriver jdbcdriver();

    /**
     * Get the {@link JdbcResource.JdbcDriver} for this database.
     * 
     */
    public BaseTranslator jdbctranslator();
    
    
    
    }
