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
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * Public interface for an ADQL schema.
 *
 */
public interface AdqlSchema
extends BaseSchema<AdqlSchema, AdqlTable>
    {
    /**
     * {@link BaseSchema.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends BaseSchema.IdentFactory<AdqlSchema>
        {
        }

    /**
     * {@link BaseSchema.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends BaseSchema.NameFactory<AdqlSchema>
        {
        }

    /**
     * {@link BaseSchema.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseSchema.LinkFactory<AdqlSchema>
        {
        }
    
    /**
     * {@link BaseSchema.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseSchema.EntityFactory<AdqlResource, AdqlSchema>
        {
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

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<AdqlSchema>
        {
        /**
         * Our {@link AdqlSchema.EntityFactory} instance.
         *
         */
        public AdqlSchema.EntityFactory entities();

        /**
         * Our {@link AdqlTable.EntityFactory} instance.
         *
         */
        public AdqlTable.EntityFactory tables();
        }
    
    @Override
    public AdqlResource resource();

    /**
     * Our table {@link AdqlTable tables}.
     *
     */
    public interface Tables extends BaseSchema.Tables<AdqlTable>
        {
        /**
         * Create a new {@link AdqlTable table}, importing the columns from a {@link BaseTable base table}.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base);

        /**
         * Create a new {@link AdqlTable table}, importing the columns from a {@link BaseTable base table}.
         *
         */
        public AdqlTable create(final BaseTable<?,?> base, final String name);

        /**
         * Create a new {@link AdqlTable table}, importing the columns from a {@link BaseTable base table}.
         *
         */
        public AdqlTable create(final CopyDepth depth, final BaseTable<?,?> base);

        /**
         * Create a new {@link AdqlTable table}, importing the columns from a {@link BaseTable base table}.
         *
         */
        public AdqlTable create(final CopyDepth depth, final BaseTable<?,?> base, final String name);

        /**
         * Import a {@link AdqlTable table} from our base schema..
         *
         */
        public AdqlTable inport(final String name)
        throws NameNotFoundException;

        }
    @Override
    public Tables tables();

    /**
     * The {@link AdqlSchema} metadata.
     *
     */
    public interface Metadata
    extends BaseSchema.Metadata
        {
        /**
         * The ADQL metadata.
         * 
         */
        public interface Adql
            {
            /**
             * The schema name.
             *
             */
            public String name();

            /**
             * The schema description.
             * 
             */
            public String text();
            }

        /**
         * The ADQL metadata.
         * 
         */
        public Adql adql();
        }

    @Override
    public AdqlSchema.Metadata meta();

    }
