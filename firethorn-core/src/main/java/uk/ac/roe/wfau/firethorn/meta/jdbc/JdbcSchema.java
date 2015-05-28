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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
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
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<JdbcSchema, JdbcSchema.Metadata>
        {
        /**
         * Create or update a {@link JdbcSchema}.
         *
         */
        public JdbcSchema build(final JdbcSchema.Metadata meta)
        throws DuplicateEntityException;
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
         *
         */
        public JdbcSchema create(final JdbcResource parent, final JdbcSchema.Metadata meta);

        /**
         * Create a new {@link JdbcSchema}.
         * Used by {@link JdbcResourceEntity#scanimpl()}.
         *
         */
        public JdbcSchema create(final JdbcResource parent, final String catalog, final String schema);

        /**
         * Create a new {@link JdbcSchema} for an identity.
         * This should create a new schema in the user data database.
         * @todo Move this to data space interface.
         *
         */
        public JdbcSchema build(final JdbcResource parent, final Identity identity);

        /**
         * Select a {@link JdbcSchema} based on (owner) Identity.
         * @todo Move this to data space interface.
         *
         */
        public Iterable<JdbcSchema> select(final JdbcResource parent, final Identity identity);

        /**
         * Our local {@link JdbcSchema.OldBuilder} implementation.
         * @todo Move this to data space interface.
         *
         */
        public JdbcSchema.OldBuilder oldbuilder();
        
        /**
         * Select a {@link JdbcSchema} based on catalog and schema name.
         *
         */
        public JdbcSchema select(final JdbcResource parent, final String catalog, final String schema)
        throws NameNotFoundException;

        /**
         * Search for a {@link JdbcSchema} based on catalog and schema name.
         *
         */
        public JdbcSchema search(final JdbcResource parent, final String catalog, final String schema);

        /**
         * Our local {@link JdbcTable.EntityFactory} implementation.
         * @todo Move to services
         *
         */
        public JdbcTable.EntityFactory tables();

        /**
         * Our local {@link JdbcSchema.NameFactory} implementation.
         * @todo Move to services
         *
         */
        public JdbcSchema.NameFactory names();

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
         *  Create a new {@link JdbcTable table}.
         *
         */
        @Deprecated
        public JdbcTable create(final String name);

        /**
         *  Create a new {@link JdbcTable table}.
         *
         */
        @Deprecated
        public JdbcTable create(final String name, final JdbcTable.JdbcType type);

        /**
         *  Create a new {@link JdbcTable table}.
         *
         */
        public JdbcTable create(final JdbcTable.Metadata meta);

        /**
         *  Create a new {@link JdbcTable table}.
         *
         */
        public JdbcTable create(final AdqlQuery query);

        /**
         * Get the next set of tables for garbage collection ..
         * @Move this to the data space interface.
         *
         */
        public Iterable<JdbcTable> pending(final DateTime date, final int page);

        /**
         * Create a {@link JdbcTable.Builder}.
         *
         */
        public JdbcTable.Builder builder();  
        
        }
    @Override
    public Tables tables();

    /**
     * The catalog name.
     *
     */
    public String catalog();

    /**
     * The schema name.
     *
     */
    public String schema();

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
             * 
             */
            public String fullname() ;

            /**
             * The schema name.
             * 
             */
            public String schema() ;

            /**
             * The catalog name.
             * 
             */
            public String catalog() ;

            }

        /**
         * The JDBC metadata.
         * 
         */
        public Jdbc jdbc();
        
        }

    @Override
    public JdbcSchema.Metadata meta();

    /**
     * Update the schema properties.
     * 
     */
    public void update(final JdbcSchema.Metadata meta);
    
    }
