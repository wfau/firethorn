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
import java.sql.Driver;

import uk.ac.roe.wfau.firethorn.adql.parser.AdqlTranslator;

/**
 * JDBC resource connection details.
 *
 */
public interface JdbcConnection
    {
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
     * Get the database name.
     * 
     */
    public String database();

    /**
     * Set the database name.
     * 
     */
    public void database(final String database);

    /**
     * Get the primary database catalog.
     * 
     */
    public String catalog();

    /**
     * Set the primary database catalog.
     * 
     */
    public void catalog(final String catalog);

    /**
     * Get the JDBC connection host name.
     *
     */
    public String host();

    /**
     * Set the JDBC connection host name.
     *
     */
    public void host(final String host);

    /**
     * Get the JDBC connection port.
     *
     */
    public Integer port();

    /**
     * Set the JDBC connection port.
     *
     */
    public void port(final Integer port);
    
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
     * Open a new database {@link Connection}.
     *
     */
    public Connection open();

    /**
     * Close the current database {@link Connection}.
     *
     */
    public void close();

    /**
     * Reset the current database {@link Connection}.
     *
     */
    public void reset();

    /**
     * Get the JDBC connection URL for this database (as a {@link String}).
     *
     */
    public String url();

    /**
     * Get the JDBC {@link Driver} for this database.
     *
     */
    public Driver driver();
    
    /**
     * Get the {@link JdbcMetadataScanner} for this database.
     * 
     */
    public JdbcMetadataScanner scanner();

    /**
     * Get the {@link JdbcOperator} for this database.
     * 
     */
    public JdbcOperator operator();

    /**
     * Get the {@link AdqlTranslator} for this database.
     * 
     */
    public AdqlTranslator translator();

    }
