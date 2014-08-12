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
 * Parameter names for JdbcResources.
 *
 */
public interface JdbcResourceParam
    {

    /**
     * Parameter name for the database URL, {@value}.
     * 
     */
    public static final String DATABASE_URL = "jdbc.database.url"  ;

    /**
     * Parameter name for the database user name, {@value}.
     * 
     */
    public static final String DATABASE_USERNAME = "jdbc.database.username"  ;

    /**
     * Parameter name for the database password, {@value}.
     * 
     */
    public static final String DATABASE_PASSWORD = "jdbc.database.password"  ;

    /**
     * Parameter name for the database driver name, {@value}.
     * 
     */
    public static final String DATABASE_DRIVER = "jdbc.database.driver"  ;

    /**
     * Parameter name for the writable flag, {@value}.
     * 
     */
    public static final String DATABASE_WRITABLE = "jdbc.database.writable"  ;

    }

