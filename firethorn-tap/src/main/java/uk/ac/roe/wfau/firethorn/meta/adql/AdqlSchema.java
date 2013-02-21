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

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 *
 *
 */
public interface AdqlSchema
extends BaseSchema<AdqlSchema, AdqlTable>
    {
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
    extends BaseSchema.Factory<AdqlResource, AdqlSchema>
        {
        /**
         * The schema table factory.
         *
         */
        public AdqlTable.Factory tables();

        /**
         * Create a schema, importing a base table.
         *
         */
        public AdqlSchema create(final AdqlResourceEntity parent, final String name, final BaseTable<?,?> base);

        /**
         * Create a schema, importing the tables from a base schema.
         *
         */
		public AdqlSchema create(final AdqlResourceEntity parent, final String name, final BaseSchema<?,?> base);
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
         * The create a new table, importing the columns from a base table.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base);

        /**
         * The create a new table, importing the columns from a base table.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base, final String name);

        }
    @Override
    public Tables tables();

    }
