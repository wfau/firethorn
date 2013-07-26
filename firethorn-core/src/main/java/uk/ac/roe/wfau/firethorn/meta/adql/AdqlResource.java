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
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;


/**
 *
 *
 */
public interface AdqlResource
extends BaseResource<AdqlSchema>
    {
    /**
     * Name factory interface.
     *
     */
    public static interface NameFactory
    extends Entity.NameFactory<AdqlResource>
        {
        }

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
         * Create a new Resource.
         *
         */
        public AdqlResource create(final String name);

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
         * Create a new schema, importing a base table.
         *
         */
        public AdqlSchema create(final String name, final BaseTable<?,?> base);

        /**
         * Create a new schema, importing all the tables from a base schema.
         *
         */
        public AdqlSchema create(final BaseSchema<?,?> base);

        /**
         * Create a new schema, importing the tables from a base schema.
         *
         */
        public AdqlSchema create(final CopyDepth depth, final BaseSchema<?,?> base);

        /**
         * Create a new schema, importing all the tables from a base schema.
         *
         */
        public AdqlSchema create(final String name, final BaseSchema<?,?> base);

        /**
         * Create a new schema, importing the tables from a base schema.
         *
         */
        public AdqlSchema create(final CopyDepth depth, final String name, final BaseSchema<?,?> base);

        /**
         * Select or create a named schema.  
         *
         */
        public AdqlSchema inport(String read, BaseSchema<?, ?> base);

        }
    @Override
    public Schemas schemas();

    }
