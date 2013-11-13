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
package uk.ac.roe.wfau.firethorn.meta.adql;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.QueryParam;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

/**
 *
 *
 */
public interface AdqlSchema
extends BaseSchema<AdqlSchema, AdqlTable>
    {
    /**
     * Name factory interface.
     *
     */
    public static interface NameFactory
    extends Entity.NameFactory<AdqlSchema>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<AdqlSchema>
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
    extends BaseSchema.EntityFactory<AdqlResource, AdqlSchema>
        {
        /**
         * The schema table factory.
         *
         */
        public AdqlTable.Factory tables();

        /**
         * Create an empty schema.
         *
         */
        public AdqlSchema create(final AdqlResource parent, final String name);

        /**
         * Create a new schema, importing a base table.
         *
         */
        public AdqlSchema create(final AdqlResource parent, final String name, final BaseTable<?,?> base);

        /**
         * Create a new schema, importing a base table.
         *
         */
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final String name, final BaseTable<?,?> base);

        /**
         * Create a new schema, importing the tables from a base schema.
         *
         */
        public AdqlSchema create(final AdqlResource parent, final BaseSchema<?,?> base);
        /**
         * Create a new schema, importing the tables from a base schema.
         *
         */
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final BaseSchema<?,?> base);

        /**
         * Create a new schema, importing the tables from a base schema.
         *
         */
        public AdqlSchema create(final AdqlResource parent, final String name, final BaseSchema<?,?> base);

        /**
         * Create a new schema, importing the tables from a base schema.
         *
         */
        public AdqlSchema create(final CopyDepth depth, final AdqlResource parent, final String name, final BaseSchema<?,?> base);

        }

    @Override
    public AdqlResource resource();

    /**
     * Access to the schema tables.
     *
     */
    public interface Tables extends BaseSchema.Tables<AdqlTable>
        {

        /**
         * Create a new table, importing the columns from a base table.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base);

        /**
         * Create a new table, importing the columns from a base table.
         *
         */
        public AdqlTable create(final CopyDepth depth, final BaseTable<?,?> base);

        /**
         * Create a new table, importing the columns from a base table.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base, final String name);

        /**
         * Create a new table, importing the columns from a base table.
         *
         */
        public AdqlTable create(final CopyDepth depth, final BaseTable<?,?> base, final String name);

        /**
         * Create a new results table, importing the details from the query.
         *
         */
        public AdqlTable create(final AdqlQuery query);

        /**
         * Import a named table from our base schema.
         *
         */
        public AdqlTable inport(final String name)
        throws NameNotFoundException;

        }
    @Override
    public Tables tables();

    /**
     * Access to the schema queries.
     *
     */
    public interface Queries
        {
        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final JdbcSchema space, final String query);

        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final JdbcSchema space, final String query, final String rowid);

        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final JdbcSchema space, final QueryParam params, final String query);

        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final JdbcSchema space, final String query, final String rowid, final String name);

        /**
         * Select all the queries for this schema.
         *
         */
        public Iterable<AdqlQuery> select();

        }

    /**
     * Access to the schema queries.
     *
     */
    public Queries queries();

    }
