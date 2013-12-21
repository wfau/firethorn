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

import lombok.extern.slf4j.Slf4j;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

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
     * Name factory interface.
     *
     */
    public static interface NameFactory
    extends BaseTable.NameFactory<JdbcTable>
        {
        /**
         * Create a name for a query results table.
         *
         */
        public String name(final AdqlQuery query);
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
     * Builder interface that manipulates the 'real' JDBC tables.
     * @deprecated
     *
     */
    public static interface Builder
        {
        /**
         * Create a JDBC table.
         *
         */
        public JdbcTable create(final JdbcTable table);

        /**
         * Delete a JDBC table.
         *
         */
        public void delete(final JdbcTable table);

        }

    /**
     * JDBC table cleaner interface.
     * 
     */
    public static interface Cleaner
        {

        /**
         * Get the next set of tables to delete. 
         *
         */
        public Iterable<JdbcTable> pending(final JdbcSchema parent);

        }    

    /**
     * Physical JDBC factory interface.
     * @todo Move this up to resource ?
     * 
     */
    public static interface JdbcDriver
        {
        /**
         * Create a 'physical' JDBC table.
         * *This should only be reachable via a transactional method on our parent resource. 
         * 
         */
        public void create(final JdbcTable table);

        /**
         * Delete (DELETE) a JDBC data.
         * *This should only be reachable via a transactional method on our parent resource. 
         * 
         */
        public void delete(final JdbcTable table);

        /**
         * Delete (DROP) a JDBC table.
         * *This should only be reachable via a transactional method on our parent resource. 
         * 
         */
        public void drop(final JdbcTable table);

        }
    
    /**
     * Table factory interface.
     *
     */
    public static interface EntityFactory
    extends BaseTable.EntityFactory<JdbcSchema, JdbcTable>
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
        public JdbcTable create(final JdbcSchema parent, final String name, final JdbcType type);

        /**
         * Create a new query table.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final AdqlQuery query);

        /**
         * Column factory implementation.
         *
         */
        public JdbcColumn.EntityFactory columns();

        /**
         * Builder implementation.
         *
         */
        public JdbcTable.Builder builder();

        /**
         * Our physical JDBC factory.
         * 
         */
        public JdbcTable.JdbcDriver driver();

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
        public JdbcColumn create(final AdqlQuery.SelectField field);

        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final String name, final int type, final int size);

        /**
         * Create a new column.
         *
         */
        public JdbcColumn create(final String name, final JdbcColumn.Type type);

        /**
         * Scan the JDBC metadata.
         *
         */
        public void scan();

        }
    @Override
    public Columns columns();

    /**
     * Flag to indicate that the physical table exists.
     *
     */
    public boolean exists();

    /**
     * Enum for the JDBC table types.
     *
     */
    public static enum JdbcType
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

        private JdbcType(final String jdbc)
            {
            this.jdbc = jdbc;
            }

        static protected Map<String, JdbcType> mapping = new HashMap<String, JdbcType>();
        static {
            for (final JdbcType type : JdbcType.values())
                {
                mapping.put(
                    type.jdbc,
                    type
                    );
                }
            }

        static public JdbcType match(final String string)
            {
            return mapping.get(
                string
                );
            }
        }

    /**
     * Enum for the physical table status.
     * @todo Move up to resource ?
     * 
     */
    @Slf4j
    public static enum JdbcStatus
        {
        CREATED(),
        UPDATED(),
        DELETED(),
        DROPPED(),
        UNKNOWN();

        }
    
    /**
     * JDBC table metadata.
     *
     */
    public interface Metadata
    extends AdqlTable.Metadata
        {
        /**
         * The JDBC table metadata.
         *
         */
        public interface JdbcMetadata {

            /**
             * Get the database table type.
             *
             */
            public JdbcType type();

            /**
             * Set the database table type.
             * 
             */
            public void type(final JdbcType type);

            // -- JdbcFactory stuff --
            
            /**
             * Create (CREATE) the JDBC table.
             * @todo Implement as a status change.
             * 
            public void create();
             */

            /**
             * Delete (DELETE) the JDBC data.
             * @todo Implement as a status change.
             * 
            public void delete();
             */

            /**
             * Delete (DROP) the JDBC table.
             * @todo Implement as a status change.
             * 
            public void drop();
             */

            /**
             * The JDBC table status.
             * 
             */
            public JdbcTable.JdbcStatus status() ;

            /**
             * Set the JDBC table status.
             * 
             */
            public void status(final JdbcTable.JdbcStatus status) ;

            }

        /**
         * The JDBC table metadata.
         *
         */
        public JdbcMetadata jdbc();

        }

    @Override
    public JdbcTable.Metadata meta();

    }
