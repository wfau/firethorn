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
 * Common parameter names for the JdbcCreateResource Activity.
 *
 */
public interface JdbcCreateTableParam
    {
    /**
     * The Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.JdbcCreateTable" ;

    /**
     * Parameter name for the table name, {@value}.
     * 
     */
    public static final String JDBC_CREATE_TABLE_NAME = "jdbc.create.table.name"  ;
    
    /**
     * Parameter name for the input tuples, {@value}.
     * 
     */
    public static final String JDBC_CREATE_TUPLE_INPUT  = "jdbc.create.input.tuples" ;

    /**
     * Parameter name for the Activity results, {@value}.
     * 
     */
    public static final String JDBC_CREATE_RESULTS = "jdbc.create.results" ;

    }

