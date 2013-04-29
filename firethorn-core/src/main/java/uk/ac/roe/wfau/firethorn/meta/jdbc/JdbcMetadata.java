/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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

import java.sql.DatabaseMetaData;

/**
 *
 *
 */
public interface JdbcMetadata
    {
    /**
     * JDBC DatabaseMetaData column names.
     * @see DatabaseMetaData
     *
     */
    public static final String JDBC_META_TABLE_CAT     = "TABLE_CAT" ;
    public static final String JDBC_META_TABLE_CATALOG = "TABLE_CATALOG" ;
    public static final String JDBC_META_TABLE_TYPE    = "TABLE_TYPE" ;
    public static final String JDBC_META_TABLE_NAME    = "TABLE_NAME" ;
    public static final String JDBC_META_TABLE_SCHEM   = "TABLE_SCHEM" ;

    public static final String JDBC_META_TABLE_TYPE_VIEW  = "VIEW" ;
    public static final String JDBC_META_TABLE_TYPE_TABLE = "TABLE" ;

    public static final String JDBC_META_COLUMN_NAME      = "COLUMN_NAME" ;
    public static final String JDBC_META_COLUMN_TYPE_TYPE = "DATA_TYPE";
    public static final String JDBC_META_COLUMN_TYPE_NAME = "TYPE_NAME";
    public static final String JDBC_META_COLUMN_SIZE      = "COLUMN_SIZE";

    }
