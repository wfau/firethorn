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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;

/**
 *
 *
 */
public interface JdbcSchema
extends BaseSchema<JdbcSchema, JdbcTable>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<JdbcSchema>
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
     * Schema factory interface.
     *
     */
    public static interface Factory
    extends BaseSchema.Factory<JdbcResource, JdbcSchema>
        {
        /**
         * Create a schema name.
         *
         */
        public String name(final String catalog, final String schema);

        /**
         * Create a new schema.
         *
         */
        public JdbcSchema create(final JdbcResource parent, final String catalog, final String schema);

        /**
         * Search for a schema.
         *
         */
        public JdbcSchema select(final JdbcResource parent, final String catalog, final String schema);

        /**
         * The schema table factory.
         *
         */
        public JdbcTable.Factory tables();
        }

    @Override
    public JdbcResource resource();

    /**
     * Access to the schema tables.
     *
     */
    public interface Tables extends BaseSchema.Tables<JdbcTable>
        {
        /**
         *  Create a new table.
         *
         */
        public JdbcTable create(final String name);

        /**
         *  Create a new table.
         *
         */
        public JdbcTable create(final String name, final JdbcTable.TableType type);

        /**
         *  Create a new table.
         *
         */
        public JdbcTable create(final AdqlQuery query, final String name);

        /**
         * Update the tables.
         *
         */
        public void scan();

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
     * Update the schema.
     *
     */
    public void scan();

    }
