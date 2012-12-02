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

/**
 * JDBC resource connection details.
 *
 */
public interface TuesdayJdbcConnection
    {
    /**
     * Get the connection URL (as a String).
     *  
     */
    public String url();

    /**
     * Set the connection URL (as a String).
     *  
     */
    public void url(String url);

    /**
     * Get the JDBC driver class name.
     *  
     */
    public String driver();

    /**
     * Set the JDBC driver class name.
     *  
     */
    public void driver(String driver);
    
    /**
     * Get the JDBC user name.
     *  
     */
    public String user();

    /**
     * Set the JDBC user name.
     *  
     */
    public void user(String user);

    /**
     * Get the JDBC password.
     *  
     */
    public String pass();

    /**
     * Set the JDBC password.
     *  
     */
    public void pass(String pass);

    /**
     * Open a database Connection.
     *
     */
    public Connection open();

    /**
     * Get the database metadata.
     *
     */
    public DatabaseMetaData metadata();

    /**
     * Close the current database Connection.
     *
     */
    public void close();

    /**
     * Get a list of catalog names.
     * 
     */
    public Iterable<String> catalogs();

    /**
     * Get the catalog name of the current database Connection.
     * 
     */
    public String catalog();

    /**
     * Connection status values.
     * 
     */
    public enum Status
        {
        CREATED(),
        ENABLED(),
        DISABLED(),
        FAILED();
        }

    /**
     * Get the connection status.
     * 
     */
    public Status status();

    /**
     * Set the connection status.
     * 
     */
    public void status(Status status);
    
    }
