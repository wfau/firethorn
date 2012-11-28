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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 */
@Slf4j
public class TuesdayJdbcMetadata
    {
    /**
     * JDBC DatabaseMetaData column names.
     * @see DatabaseMetaData
     * @todo Move this to a local sub-interface.
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

    /**
     * Our JDBC metadata.
     * 
     */
    protected DatabaseMetaData metadata;

    /**
     * Our JDBC metadata.
     * 
     */
    public DatabaseMetaData metadata()
        {
        return this.metadata;
        }

    /**
     * Public constructor.
     * 
     */
    public TuesdayJdbcMetadata(DatabaseMetaData metadata)
        {
        this.metadata = metadata;
        }

    /**
     * The connection URL (as a String).
     *  
     */
    public String url()
        {
        try {
            return metadata().getURL();
            }
        catch (SQLException ouch)
            {
            log.error("SQLException reading database URL[{}]", ouch);
            return "unknown";
            }
        }

    /**
     * Process the available database tables.
     * 
     */
    public void process()
        {
        try {
            final ResultSet results = metadata().getTables(
                null,
                null,
                null,
                new String[]
                    {
                    JDBC_META_TABLE_TYPE_TABLE,
                    JDBC_META_TABLE_TYPE_VIEW
                    }
                );
            while (results.next())
                {
                final String cname = results.getString(JDBC_META_TABLE_CAT);
                final String sname = results.getString(JDBC_META_TABLE_SCHEM);
                final String tname = results.getString(JDBC_META_TABLE_NAME);
                final String ttype = results.getString(JDBC_META_TABLE_TYPE);
                log.debug("Found table [{}.{}.{}]", new Object[]{cname, sname, tname});
                //
                // If the catalog name is null.
                if (cname == null)
                    {
                    log.debug("Catalog is null, whatever ..");
                    continue ;
                    }
                //
                // If the catalog name is not null.
                else {

                    
                    //
                    // If the schema name is null.
                    if (sname == null)
                        {
                        log.debug("Schema is null, whatever ..");
                        continue ;
                        }
                    //
                    // If the schema name is not null.
                    else {
                        //
                        // If the table name is null.
                        if (tname == null)
                            {
                            log.debug("Table is null, whatever ..");
                            }
                        //
                        // If the table name is not null.
                        else {
                            log.debug("Table is [{}], processing", tname);
                            }
                        }
                    }
                }
            }
        catch (SQLException ouch)
            {
            log.error("SQLException reading database metadata [{}]", url());
            log.error("SQLException : ", ouch);
            }
        }
    }
