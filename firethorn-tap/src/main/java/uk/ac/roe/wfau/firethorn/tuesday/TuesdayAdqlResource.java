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
public interface TuesdayAdqlResource
extends TuesdayBaseResource<TuesdayAdqlSchema>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TuesdayAdqlResource>
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
     * Resource factory interface.
     *
     */
    public static interface Factory
    extends TuesdayBaseResource.Factory<TuesdayAdqlResource>
        {
        /**
         * The resource schema factory.
         *
         */
        public TuesdayAdqlSchema.Factory schemas();

        }

    /**
     * Access to the resource schemas.
     *
     */
    public interface Schemas extends TuesdayBaseResource.Schemas<TuesdayAdqlSchema>
        {
        /**
         * Create a new schema.
         *
         */
        public TuesdayAdqlSchema create(final String name);

        /**
         * Import tables from a schema.
         *
         */
        public TuesdayAdqlSchema inport(final TuesdayBaseSchema<?,?> base, final String name);

        }
    @Override
    public Schemas schemas();

    /**
     * Access to the resource queries.
     *
     */
    public interface Queries
        {
        /**
         * Create a new query.
         *
         */
        public TuesdayAdqlQuery create(final String query);

        /**
         * Create a new query.
         *
         */
        public TuesdayAdqlQuery create(final String name, final String query);

        /**
         * Select all the queries for this resource.
         *
         */
        public Iterable<TuesdayAdqlQuery> select();

        /**
         * Text search for queries (name starts with).
         *
         */
        public Iterable<TuesdayAdqlQuery> search(final String text);

        }

    /**
     * Access to the resource queries.
     *
     */
    public Queries queries();
    
    }
