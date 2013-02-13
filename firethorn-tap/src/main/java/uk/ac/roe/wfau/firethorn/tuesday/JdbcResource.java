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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 *
 *
 */
public interface JdbcResource
extends OgsaResource<JdbcSchema>, BaseResource<JdbcSchema>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<JdbcResource>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * Resource factory interface.
     *
     */
    public static interface Factory
    extends BaseResource.Factory<JdbcResource>
        {
        /**
         * The resource schema factory.
         *
         */
        public JdbcSchema.Factory schemas();

        /**
         * Create a new resource.
         *
         */
        public JdbcResource create(final String name, final String url);

        /**
         * Create a new resource.
         *
         */
        public JdbcResource create(final String name, final String url, final String user, final String pass);

        }

    /**
     * Access to the resource schemas.
     *
     */
    public interface Schemas extends BaseResource.Schemas<JdbcSchema>
        {
        /**
         * Create a new schema.
         *
         */
        public JdbcSchema create(final String name);

        }

    @Override
    public Schemas schemas();

    /**
     * Access to our JDBC resource connection.
     *
     */
    public JdbcConnection connection();

    /**
     * Import the metadata from our database.
     *
     */
    public void inport();

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

    /**
     * Known database types, indexed by the product name in DatabaseMetaData.getDatabaseProductName()
     *
     */
    @Slf4j
    public static enum JdbcProductType
        {
        UNKNOWN(
            "unknown"
            ),
        PGSQL(
            "PostgreSQL",
            new String[]{}
            ),
        MYSQL(
            "MySQL",
            new String[]{}
            ),
        MSSQL(
            "Microsoft SQL Server",
            new String[]{
                "sys",
                "INFORMATION_SCHEMA"
                }
            ),
        HSQLDB(
            "unknown",
            new String[]{}
            );

        private JdbcProductType(final String alias)
            {
            this(
                alias,
                null
                );
            }
        private JdbcProductType(final String alias, final String[] ignores)
            {
            this.alias = alias;
            if (ignores != null)
                {
                for (final String ignore : ignores)
                    {
                    this.ignores.add(
                        ignore
                        );
                    }
                }
            }

        private final String alias ;
        public String alias()
            {
            return this.alias;
            }

        private final Collection<String> ignores = new ArrayList<String>();
        public Collection<String>  ignores()
            {
            return this.ignores;
            }

        static protected Map<String, JdbcProductType> mapping = new HashMap<String, JdbcProductType>();
        static {
            for (final JdbcProductType type : JdbcProductType.values())
                {
                mapping.put(
                    type.alias(),
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
        }
    }
