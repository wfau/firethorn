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
 * Parameter names for the JdbcInsertData Activity.
 *
 */
public class JdbcInsertDataParam
    {
    /**
     * The Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.JdbcInsertData" ;

    /**
     * Parameter name for the Activity results, {@value}.
     * 
     */
    public static final String ACTIVITY_RESULTS = "jdbc.insert.results" ;

    /**
     * Parameter name for the size of the first block, {@value}.
     * 
     */
    public static final String JDBC_INSERT_FIRST_SIZE = "jdbc.insert.first.size" ;

    /**
     * Default value for the first block size, {@value}.
     * 
     */
    public static final Integer JDBC_INSERT_FIRST_DEFAULT = new Integer(100);
    
    /**
     * Parameter name for the main block size, {@value}.
     * 
     */
    public static final String JDBC_INSERT_BLOCK_SIZE = "jdbc.insert.block.size"  ;

    /**
     * Default value for the main block size, {@value}.
     * 
     */
    public static final Integer JDBC_INSERT_BLOCK_DEFAULT_BLOCK = new Integer(1000);

    /**
     * Parameter name for the table name, {@value}.
     * 
     */
    public static final String JDBC_INSERT_TABLE_NAME = "jdbc.insert.table.name"  ;
    
    /**
     * Parameter name for the input tuples, {@value}.
     * 
     */
    public static final String JDBC_INSERT_TUPLE_INPUT  = "jdbc.insert.input.tuples" ;

    }

