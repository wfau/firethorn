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
package uk.ac.roe.wfau.firethorn.meta.jdbc;

import java.util.HashMap;
import java.util.Map;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable.Info;

/**
 *
 *
 */
public interface JdbcTable
extends BaseTable<JdbcTable, JdbcColumn>
    {
    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory
    extends BaseTable.AliasFactory<JdbcTable>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<JdbcTable>
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
     * Table factory interface.
     *
     */
    public static interface Factory
    extends BaseTable.Factory<JdbcSchema, JdbcTable>
        {
        /**
         * Create a new table.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final String name);

        /**
         * Create a new table.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final String name, final TableType type);

        /**
         * Create a new query table.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final AdqlQuery query, final String name);
        
        /**
         * The table column factory.
         *
         */
        public JdbcColumn.Factory columns();

        }

    @Override
    public JdbcResource resource();
    @Override
    public JdbcSchema schema();

    /**
     * The table columns.
     *
     */
    public interface Columns extends BaseTable.Columns<JdbcColumn>
        {
        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final String name);

        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final String name, final int type, final int size);

        /**
         * Scan the JDBC metadata.
         *
         */
        public void scan();

        }
    @Override
    public Columns columns();

    /**
     * Scan the JDBC metadata.
     *
     */
    public void scan();

    /**
     * JDBC table types.
     *
     */
    public static enum TableType
        {
        TABLE("TABLE"),
        VIEW("VIEW"),
        ALIAS("ALIAS"),
        SYNONYM("SYNONYM"),
        SYSTEM("SYSTEM TABLE"),
        GLOBAL_TEMP("GLOBAL TEMPORARY"),
        LOCAL_TEMP("LOCAL TEMPORARY");

        protected String jdbc ;
        public String jdbc()
            {
            return this.jdbc;
            }

        private TableType(final String jdbc)
            {
            this.jdbc = jdbc;
            }

        static protected Map<String, TableType> mapping = new HashMap<String, TableType>();
        static {
            for (final TableType type : TableType.values())
                {
                mapping.put(
                    type.jdbc,
                    type
                    );
                }
            }

        static public TableType match(final String string)
            {
            return mapping.get(
                string
                );
            }
        }

    /**
     * JDBC table metadata.
     *
     */
    public interface Info
    extends AdqlTable.Info
        {
        /**
         * The JDBC table metadata.
         *
         */
        public interface JdbcMeta {

            /**
             * Get the database table type.
             *
             */
            public TableType type();
    
            /**
             * Set the database table type.
             *
             */
            public void type(final TableType type);

            }
        
        /**
         * The JDBC table metadata.
         *
         */
        public JdbcMeta jdbc();
        
        }
    
    @Override
    public JdbcTable.Info info();
    
    /**
     * Get the database table type.
     * @todo Move these to a sub-interface
     *
    @Deprecated
    public TableType jdbctype();
     */

    /**
     * Set the database table type.
     * @todo Move these to a sub-interface
     *
    @Deprecated
    public void jdbctype(final TableType type);
     */
    
    }
