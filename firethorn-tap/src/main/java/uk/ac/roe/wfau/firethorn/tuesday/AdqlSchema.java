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

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

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
         * Import a table.
         *
         */
		public AdqlSchema inport(final AdqlResourceEntity parent, final BaseSchema<?,?> base, final String name);
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
         * The create a new table.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base);

        /**
         * The create a new table.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base, final String name);

        }
    @Override
    public Tables tables();

    }
