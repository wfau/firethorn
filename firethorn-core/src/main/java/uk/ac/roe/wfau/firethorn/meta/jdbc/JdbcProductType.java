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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Known database types, indexed by the product name in DatabaseMetaData.getDatabaseProductName()
 *
 */
@Slf4j
public enum JdbcProductType
    {
    UNKNOWN(
        "unknown",
        "unknown"
        ),
    PGSQL(
        "PostgreSQL",
        "public",
        new String[]{}
        ),
    MYSQL(
        "MySQL",
        "public",
        new String[]{}
        ),
    MSSQL(
        "Microsoft SQL Server",
        "dbo",
        new String[]{
            "sys",
            "INFORMATION_SCHEMA"
            }
        ),
    HSQLDB(
        "HSQL Database Engine",
        "PUBLIC.PUBLIC",
        new String[]{}
        );

    private JdbcProductType(final String mname, final String schema)
        {
        this(
            mname,
            schema,
            null
            );
        }
    private JdbcProductType(final String mname, final String schema, final String[] ignores)
        {
        this.mname  = mname;
        this.schema = schema ;
        if (ignores != null)
            {
            for (final String ignore : ignores)
                {
                this.ignore.add(
                    ignore
                    );
                }
            }
        }

    /**
     * The name reported in DatabaseMetaData.
     * 
     */
    private final String mname ;

    /**
     * The name reported in DatabaseMetaData.
     * 
     */
    public String mname()
        {
        return this.mname;
        }

    /**
     * The default schema name.
     * 
     */
    private final String schema ;

    /**
     * The default schema name.
     * 
     */
    public String defschema ()
        {
        return this.schema ;
        }


    /**
     * A list of 'system' schema to ignore.
     * 
     */
    private final Collection<String> ignore = new ArrayList<String>();

    /**
     * A list of 'system' schema to ignore.
     * 
     */
    public Collection<String> ignore()
        {
        return this.ignore;
        }

    static protected Map<String, JdbcProductType> mapping = new HashMap<String, JdbcProductType>();
    static {
        for (final JdbcProductType type : JdbcProductType.values())
            {
            mapping.put(
                type.mname(),
                type
                );
            }
        }

    static public JdbcProductType match(final String alias)
        {
        if (mapping.containsKey(alias))
            {
            return mapping.get(
                alias
                );
            }
        else {
            return UNKNOWN;
            }
        }
    static public JdbcProductType match(final DatabaseMetaData metadata)
        {
        if (metadata != null)
            {
            try {
                return match(
                    metadata.getDatabaseProductName()
                    );
                }
            catch (final SQLException ouch)
                {
                log.error("SQLException reading database metadata [{}]", ouch.getMessage());
                return UNKNOWN;
                }
            }
        else {
            return UNKNOWN;
            }
        }
    }
