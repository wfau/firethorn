/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc;

/**
 * Shared parameter names for our JdbcCreateActivity.
 *
 */
public interface JdbcCreateParam
    {
    /**
     * Default Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.JdbcCreate" ;

    /**
     * Parameter name for the database URL, {@value}.
     * 
     */
    public static final String JDBC_DATABASE_URL = "jdbc.create.jdbcurl"  ;

    /**
     * Parameter name for the database user name, {@value}.
     * 
     */
    public static final String JDBC_DATABASE_USERNAME = "jdbc.create.username"  ;

    /**
     * Parameter name for the database password, {@value}.
     * 
     */
    public static final String JDBC_DATABASE_PASSWORD = "jdbc.create.password"  ;

    /**
     * Parameter name for the database driver name, {@value}.
     * 
     */
    public static final String JDBC_DATABASE_DRIVER = "jdbc.create.driver"  ;

    /**
     * Parameter name for the results, {@value}.
     * 
     */
    public static final String JDBC_CREATE_RESULT = "jdbc.create.result"  ;

    /**
     * The database URL, as a String.
     *
     */
    public String jdbcurl();

    /**
     * The database username, as a String.
     *
     */
    public String username();

    /**
     * The database password, as a String.
     *
     */
    public String password();

    /**
     * The database driver class name, as a String.
     *
     */
    public String driver();

    }

