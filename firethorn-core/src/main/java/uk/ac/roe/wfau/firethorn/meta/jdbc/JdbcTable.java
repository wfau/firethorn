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

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;

/**
 *
 *
 */
public interface JdbcTable
extends BaseTable<JdbcTable, JdbcColumn>
    {
    /**
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<JdbcTable, JdbcTable.Metadata>
        {
        /**
         * Create or update a table.
         *
         */
        public JdbcTable build(final JdbcTable.Metadata meta)
        throws DuplicateEntityException;
        }
    
    /**
     * {@link BaseTable.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends BaseTable.IdentFactory
        {
        }

    /**
     * {@link BaseTable.NameFactory} interface.
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
     * {@link BaseTable.AliasFactory} interface.
     *
     */
    public static interface AliasFactory
    extends BaseTable.AliasFactory<JdbcTable>
        {
        }

    /**
     * {@link BaseTable.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseTable.LinkFactory<JdbcTable>
        {
        }

    /**
     * {@link BaseTable.EntityResolver} interface.
     *
     */
    public static interface EntityResolver
    extends BaseTable.EntityResolver<JdbcSchema, JdbcTable>
        {
        }
    
    /**
     * Builder interface that manipulates the 'real' JDBC tables.
     * @deprecated
     *
     */
    @Deprecated
    public static interface OldBuilder
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
     * {@link BaseTable.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseTable.EntityFactory<JdbcSchema, JdbcTable>
        {
        /**
         * Create a new {@link JdbcTable}.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final JdbcTable.Metadata meta);

        /**
         * Create a new {@link JdbcTable}.
         *
         */
        @Deprecated
        public JdbcTable create(final JdbcSchema parent, final String name);

        /**
         * Create a new {@link JdbcTable}.
         *
         */
        @Deprecated
        public JdbcTable create(final JdbcSchema parent, final String name, final JdbcType type);

        /**
         * Create a new {@link JdbcTable}.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final AdqlQuery query);

        /**
         * Our {@link JdbcColumn.EntityFactory} factory.
         *
         */
        public JdbcColumn.EntityFactory columns();

        /**
         * OldBuilder implementation.
         *
         */
        @Deprecated
        public JdbcTable.OldBuilder builder();

        /**
         * Our physical JDBC factory.
         *
         */
        public JdbcTable.JdbcDriver driver();

        /**
         * Get the next set of tables for garbage collection ..
         * @Move this to the data space interface.
         *
         */
        public Iterable<JdbcTable> pending(final JdbcSchema parent, final DateTime date, final int page);
        
        //TODO - move to services
        @Override
        public JdbcTable.IdentFactory idents();

        //TODO - move to services
        @Override
        public JdbcTable.NameFactory names();

        //TODO - move to services
        @Override
        public JdbcTable.AliasFactory aliases();

        //TODO - move to services
        @Override
        public JdbcTable.LinkFactory links();
        
        }

    @Override
    public JdbcResource resource();

    @Override
    public JdbcSchema schema();

    /**
     * Our table {@link JdbcColumn columns}.
     *
     */
    public interface Columns extends BaseTable.Columns<JdbcColumn>
        {
        /**
         * Create a new {@link JdbcColumn}.
         *
         */
        public JdbcColumn create(final JdbcColumn.Metadata meta);

        /**
         * Create a new {@link JdbcColumn}.
         *
         */
        public JdbcColumn create(final AdqlQuery.SelectField field);

        /**
         * Create a new {@link JdbcColumn}.
         *
         */
        @Deprecated
        public JdbcColumn create(final String name, final int type, final int size);

        /**
         * Create a new {@link JdbcColumn}.
         *
         */
        @Deprecated
        public JdbcColumn create(final String name, final JdbcColumn.Type type);

        /**
         * Create a {@link JdbcColumn.Builder}.
         *
         */
        public JdbcColumn.Builder builder();  
        
        /**
         * Scan the JDBC metadata.
         * @todo Move this inside ?
         *
         */
        public void scan();

        }
    @Override
    public Columns columns();

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
     *
     */
    public static enum TableStatus
        {
        CREATED(),
        UPDATED(),
        DELETED(),
        DROPPED(),
        UNKNOWN();
        }

    /**
     * The table metadata.
     *
     */
    public interface Metadata
    extends AdqlTable.Metadata
        {
        /**
         * The JDBC table metadata.
         *
         */
        public interface Jdbc {

            /**
             * The table name.
             * 
             */
            public String name();
            
            /**
             * The table row count.
             *
             */
            public Long count();

            /**
             * Get the JDBC table type.
             *
             */
            public JdbcType type();

            /**
             * Set the JDBC table type.
             *
             */
            public void type(final JdbcType type);

            /**
             * Get the JDBC table status.
             *
             */
            public JdbcTable.TableStatus status() ;

            /**
             * Set the JDBC table status.
             *
             */
            public void status(final JdbcTable.TableStatus status) ;

            }

        /**
         * The JDBC table metadata.
         *
         */
        public Jdbc jdbc();

        }

    @Override
    public JdbcTable.Metadata meta();

    /**
     * Update the table properties.
     * 
     */
    public void update(final JdbcTable.Metadata meta);
    
    }
