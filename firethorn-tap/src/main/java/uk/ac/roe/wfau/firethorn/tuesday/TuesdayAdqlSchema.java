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
public interface TuesdayAdqlSchema
extends TuesdayBaseSchema<TuesdayAdqlSchema, TuesdayAdqlTable>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TuesdayAdqlSchema>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayAdqlSchema>
        {
        }

    /**
     * Schema factory interface.
     *
     */
    public static interface Factory
    extends TuesdayBaseSchema.Factory<TuesdayAdqlResource, TuesdayAdqlSchema>
        {
        /**
         * The schema table factory.
         *
         */
        public TuesdayAdqlTable.Factory tables();

        /**
         * Create a new schema.
         *
         */
		public TuesdayAdqlSchema create(final TuesdayAdqlResourceEntity parent, final TuesdayBaseSchema<?,?> base, final String name);
        }

    @Override
    public TuesdayAdqlResource resource();

    /**
     * Access to the schema tables.
     *
     */
    public interface Tables extends TuesdayBaseSchema.Tables<TuesdayAdqlTable>
        {
        /**
         * The create a new table.
         *
         */
        public TuesdayAdqlTable create(final TuesdayBaseTable<?,?> base);

        /**
         * The create a new table.
         *
         */
        public TuesdayAdqlTable create(final TuesdayBaseTable<?,?> base, final String name);

        }
    @Override
    public Tables tables();

    }
