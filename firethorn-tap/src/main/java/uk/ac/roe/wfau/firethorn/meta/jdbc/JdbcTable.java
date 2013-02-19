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

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaTable;

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
        public JdbcTable create(final JdbcSchema parent, final String name, final JdbcTableType type);

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

        }
    @Override
    public Columns columns();

    /**
     * JDBC table types.
     *
     */
    public static enum JdbcTableType
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

        private JdbcTableType(final String jdbc)
            {
            this.jdbc = jdbc;
            }

        static protected Map<String, JdbcTableType> mapping = new HashMap<String, JdbcTableType>();
        static {
            for (final JdbcTableType type : JdbcTableType.values())
                {
                mapping.put(
                    type.jdbc,
                    type
                    );
                }
            }

        static public JdbcTableType match(final String string)
            {
            return mapping.get(
                string
                );
            }
        }

    /**
     * Get the database table type.
     * @todo Move these to a sub-interface
     * 
     */
    public JdbcTableType jdbctype();

    /**
     * Set the database table type.
     * @todo Move these to a sub-interface
     *
     */
    public void jdbctype(final JdbcTableType type);

    }
