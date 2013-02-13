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
public interface AdqlResource
extends BaseResource<AdqlSchema>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<AdqlResource>
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
    extends BaseResource.Factory<AdqlResource>
        {
        /**
         * The resource schema factory.
         *
         */
        public AdqlSchema.Factory schemas();

        }

    /**
     * Access to the resource schemas.
     *
     */
    public interface Schemas extends BaseResource.Schemas<AdqlSchema>
        {
        /**
         * Create a new schema.
         *
         */
        public AdqlSchema create(final String name);

        /**
         * Import tables from a schema.
         *
         */
        public AdqlSchema inport(final BaseSchema<?,?> base, final String name);

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
        public AdqlQuery create(final String query);

        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final String name, final String query);

        /**
         * Select all the queries for this resource.
         *
         */
        public Iterable<AdqlQuery> select();

        /**
         * Text search for queries (name starts with).
         *
         */
        public Iterable<AdqlQuery> search(final String text);

        }

    /**
     * Access to the resource queries.
     *
     */
    public Queries queries();
    
    }
