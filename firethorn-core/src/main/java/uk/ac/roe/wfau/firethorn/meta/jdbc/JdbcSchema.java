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

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;

/**
 * Public interface for a local JDBC schema.
 *
 */
public interface JdbcSchema
extends BaseSchema<JdbcSchema, JdbcTable>
    {
    /**
     * Physical JDBC driver interface.
     *
     */
    public static interface JdbcDriver
    extends JdbcTable.JdbcDriver
        {
        /**
         * Create (CREATE) a JDBC schema.
         * @todo Should this be part of JdbcResource.JdbcDriver ?
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public void create(final JdbcSchema schema)
        throws ProtectionException;

        /**
         * Delete (DROP) a JDBC schema.
         * @todo Should this be part of JdbcResource.JdbcDriver ?
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public void drop(final JdbcSchema schema)
        throws ProtectionException;

        }

    /**
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<JdbcSchema, JdbcSchema.Metadata>
        {
        /**
         * Create or update a {@link JdbcSchema}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcSchema build(final JdbcSchema.Metadata meta)
        throws ProtectionException, DuplicateEntityException;
        }

    /**
     * {@link BaseSchema.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<JdbcSchema>
        {
        }
    
    /**
     * {@link BaseSchema.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<JdbcSchema>
        {
        /**
         * Create a fully qualified schema name from separate catalog and schema names.
         *
         */
        public String fullname(final String catalog, final String schema);

        }

    /**
     * {@link BaseSchema.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseSchema.LinkFactory<JdbcSchema>
        {
        }

    /**
     * {@link BaseSchema.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseSchema.EntityFactory<JdbcResource, JdbcSchema>
        {
        /**
         * Create a new {@link JdbcSchema}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcSchema create(final JdbcResource parent, final JdbcSchema.Metadata meta)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcSchema}.
         * Used by {@link JdbcResourceEntity#scanimpl()}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcSchema create(final JdbcResource parent, final String catalog, final String schema)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcSchema} for an identity.
         * This should create a new schema in the user data database.
         * @todo Move this to data space interface.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcSchema build(final JdbcResource parent, final Identity identity)
        throws ProtectionException;

        /**
         * Select a {@link JdbcSchema} based on (owner) Identity.
         * @todo Move this to data space interface.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<JdbcSchema> select(final JdbcResource parent, final Identity identity)
        throws ProtectionException;

        /**
         * Select a {@link JdbcSchema} based on catalog and schema name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcSchema select(final JdbcResource parent, final String catalog, final String schema)
        throws ProtectionException, NameNotFoundException;

        /**
         * Search for a {@link JdbcSchema} based on catalog and schema name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcSchema search(final JdbcResource parent, final String catalog, final String schema)
        throws ProtectionException;

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<JdbcSchema>
        {
        /**
         * Our {@link JdbcSchema.EntityFactory} instance.
         *
         */
        public JdbcSchema.EntityFactory entities();

        /**
         * Our {@link JdbcTable.EntityFactory} instance.
         *
         */
        public JdbcTable.EntityFactory tables();
        }

    /**
     * Builder interface that manages physical {@link JdbcSchema}.
     * @todo Move this to data space interface.
     *
     */
    public static interface OldBuilder
        {
        /**
         * Create a {@link JdbcSchema}.
         *
         */
        public JdbcSchema create(final JdbcSchema schema);

        /**
         * Delete a {@link JdbcSchema}.
         *
         */
        public void delete(final JdbcSchema schema);

        }

    @Override
    public JdbcResource resource();

    /**
     * The schema {@link JdbcTable tables}.
     *
     */
    public interface Tables extends BaseSchema.Tables<JdbcTable>
        {
        /**
         * Create a new {@link JdbcTable} with a generated name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcTable create()
        throws ProtectionException;

        /**
         * Create a new {@link JdbcTable table}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        @Deprecated
        public JdbcTable create(final String name, final JdbcTable.JdbcType type)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcTable table}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcTable create(final JdbcTable.Metadata meta)
        throws ProtectionException;

        /**
         * Create a new {@link JdbcTable} for a {@link BlueQuery} results.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcTable create(final BlueQuery query)
        throws ProtectionException;

        /**
         * Get the next set of tables for garbage collection ..
         * @Move this to the data space interface.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<JdbcTable> pending(final DateTime date, final int page)
        throws ProtectionException;

        /**
         * Create a {@link JdbcTable.Builder}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public JdbcTable.Builder builder()  
        throws ProtectionException;
        
        }

    @Override
    public Tables tables()
    throws ProtectionException;

    /**
     * The catalog name.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public String catalog()
    throws ProtectionException;

    /**
     * The schema name.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public String schema()
    throws ProtectionException;

    /**
     * The schema metadata.
     *
     */
    public interface Metadata
    extends AdqlSchema.Metadata
        {
        /**
         * The JDBC metadata.
         * 
         */
        public interface Jdbc
            {
            /**
             * The fully qualified name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String fullname()
            throws ProtectionException;

            /**
             * The schema name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String schema()
            throws ProtectionException;

            /**
             * The catalog name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String catalog()
            throws ProtectionException;

            }

        /**
         * The JDBC metadata.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Jdbc jdbc()
        throws ProtectionException;
        
        }

    @Override
    public JdbcSchema.Metadata meta()
    throws ProtectionException;

    /**
     * Update the schema properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final JdbcSchema.Metadata meta)
    throws ProtectionException;

    }
