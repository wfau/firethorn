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
 * Common interface for the JdbcInsertData Activity.
 *
 */
public interface JdbcInsertDataParam
    {
    /**
     * The default Activity name, {@value}.
     * 
     */
    public static final String ACTIVITY_NAME = "uk.ac.roe.wfau.firethorn.JdbcInsertData" ;

    /**
     * Activity input name for the size of the first block, {@value}.
     * 
     */
    public static final String JDBC_INSERT_FIRST_SIZE = "jdbc.insert.first.size" ;

    /**
     * Default value for the first block size, {@value}.
     * 
     */
    public static final Integer JDBC_INSERT_FIRST_DEFAULT = new Integer(0x01);
    
    /**
     * Activity input name for the main block size, {@value}.
     * 
     */
    public static final String JDBC_INSERT_BLOCK_SIZE = "jdbc.insert.block.size"  ;

    /**
     * Default value for the main block size, {@value}.
     * 
     */
    public static final Integer JDBC_INSERT_BLOCK_DEFAULT = new Integer(0x10000);

    /**
     * Activity input name for the table name, {@value}.
     * 
     */
    public static final String JDBC_INSERT_TABLE_NAME = "jdbc.insert.table.name"  ;
    
    /**
     * Activity output name for the Activity results, {@value}.
     * @todo replace this with a tuples output.
     * 
     */
    public static final String ACTIVITY_RESULTS = "jdbc.insert.results" ;

    /**
     * Activity input name for the input tuples, {@value}.
     * @todo inherit this from TuplesCommon
     * 
     */
    public static final String TUPLE_INPUT = "tuples" ;

    /**
     * Activity output name for the output tuples.
     * @todo inherit this from TuplesCommon
     * 
     */
    public static final String TUPLE_OUTPUT = "tuples" ;

    }

