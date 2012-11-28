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

import java.util.HashMap;
import java.util.Map;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcTable.JdbcTableType;

/**
 *
 *
 */
public interface TuesdayJdbcTable
extends TuesdayOgsaTable<TuesdayJdbcTable, TuesdayJdbcColumn>, TuesdayBaseTable<TuesdayJdbcTable, TuesdayJdbcColumn>
    {
    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayJdbcTable>
        {
        }

    /**
     * Table factory interface.
     *
     */
    public static interface Factory
    extends TuesdayBaseTable.Factory<TuesdayJdbcSchema, TuesdayJdbcTable>
        {
        /**
         * Create a new table.
         *
         */
        public TuesdayJdbcTable create(final TuesdayJdbcSchema parent, final String name);

        /**
         * Create a new table.
         *
         */
        public TuesdayJdbcTable create(final TuesdayJdbcSchema parent, final String name, final JdbcTableType type);

        /**
         * The table column factory.
         *
         */
        public TuesdayJdbcColumn.Factory columns();

        }

    @Override
    public TuesdayJdbcResource resource();
    @Override
    public TuesdayJdbcSchema schema();

    /**
     * The table columns.
     * 
     */
    public interface Columns extends TuesdayBaseTable.Columns<TuesdayJdbcColumn> 
        {
        /**
         * Create a new column.
         *
         */
        public TuesdayJdbcColumn create(final String name);

        /**
         * Create a new column.
         *
         */
        public TuesdayJdbcColumn create(final String name, final int type, final int size);
        
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

        private JdbcTableType(String jdbc)
            {
            this.jdbc = jdbc;
            }

        static protected Map<String, JdbcTableType> mapping = new HashMap<String, JdbcTableType>();
        static {
            for (JdbcTableType type : JdbcTableType.values())
                {
                mapping.put(
                    type.jdbc,
                    type
                    );
                }
            }

        static public JdbcTableType match(String string)
            {
            return mapping.get(
                string
                );
            }
        }

    /**
     * Get the database table type.
     * 
     */
    public JdbcTableType jdbctype();

    /**
     * Set the database table type.
     * 
     */
    public void jdbctype(JdbcTableType type);

    }
