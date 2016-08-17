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

import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * Public interface for a local JDBC table.
 *
 */
public interface JdbcTable
extends BaseTable<JdbcTable, JdbcColumn>
    {
    /**
     * Physical JDBC driver interface.
     *
     */
    public static interface JdbcDriver
    extends JdbcColumn.JdbcDriver
        {
        /**
         * Create (CREATE) a JDBC table.
         *
         */
        public void create(final JdbcTable table);

        /**
         * Delete (DELETE) the contents of JDBC data.
         *
         */
        public void delete(final JdbcTable table);

        /**
         * Delete (DROP) a JDBC table.
         *
         */
        public void drop(final JdbcTable table);

        }

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
    extends Entity.IdentFactory<JdbcTable>
        {
        }

    /**
     * {@link BaseTable.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<JdbcTable>
        {
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
     * Builder interface that manipulates the 'real' JDBC tables.
     * @deprecated
     *
     */
    @Deprecated
    public static interface OldBuilder
        {
        public JdbcTable create(final JdbcTable table);

        public void delete(final JdbcTable table);

        }
    
    /**
     * {@link BaseTable.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseTable.EntityFactory<JdbcSchema, JdbcTable>
        {
        /**
         * Create a new {@link JdbcTable} with a generated name.
         *
         */
        public JdbcTable create(final JdbcSchema parent);

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
         * Used by {@link JdbcSchemaEntity#scanimpl()}.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final String name, final JdbcType type);

        /**
         * Create a new {@link JdbcTable}.
         * Used by AdqlQuery.
         *
         */
        public JdbcTable create(final JdbcSchema parent, final GreenQuery query);

        /**
         * Get the next set of tables for garbage collection ..
         * @Move this to the data space interface.
         *
         */
        public Iterable<JdbcTable> pending(final JdbcSchema parent, final DateTime date, final int page);

        /**
         * Our physical JDBC factory.
         *
        public JdbcTable.JdbcDriver driver();
         */
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<JdbcTable>
        {
        /**
         * Our {@link JdbcTable.EntityFactory} instance.
         *
         */
        public JdbcTable.EntityFactory entities();

        /**
         * Our {@link JdbcTable.AliasFactory} instance.
         *
         */
        public JdbcTable.AliasFactory aliases();

        /**
         * Our {@link JdbcColumn.EntityFactory} instance.
         *
         */
        public JdbcColumn.EntityFactory columns();

        /**
         * The physical JDBC factory implementation.
         * @todo This should depend on the local database dialect.
         *
        public JdbcTable.JdbcDriver driver()
         */
        
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
         * Used by JdbcColumn.Builder
         *
         */
        public JdbcColumn create(final JdbcColumn.Metadata meta);

        /**
         * Create a new {@link JdbcColumn}.
         * Used by JdbcTableEntity.
         *
         */
        public JdbcColumn create(final String name, final JdbcColumn.JdbcType type, final Integer size);

        /**
         * Create a {@link JdbcColumn.Builder}.
         *
         */
        public JdbcColumn.Builder builder();  
        
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
