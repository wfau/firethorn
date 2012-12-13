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
public interface TuesdayBaseSchema<SchemaType extends TuesdayBaseSchema<SchemaType, TableType>, TableType extends TuesdayBaseTable<TableType, ?>>
extends TuesdayBaseComponent
    {
    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayBaseSchema<?,?>>
        {
        }

    /**
     * Schema factory interface.
     *
     */
    public static interface Factory<ResourceType extends TuesdayBaseResource<SchemaType>, SchemaType extends TuesdayBaseSchema<SchemaType,?>>
    extends Entity.Factory<SchemaType>
        {
        /**
         * Create a new schema.
         *
         */
        public SchemaType create(final ResourceType parent, final String name);

        /**
         * Select all the schemas from a resource.
         *
         */
        public Iterable<SchemaType> select(final ResourceType parent);

        /**
         * Select a named schema from a resource.
         *
         */
        public SchemaType select(final ResourceType parent, final String name);

        /**
         * Text search for schemas (name starts with).
         *
         */
        public Iterable<SchemaType> search(final ResourceType parent, final String text);

        }

    /**
     * Access to our parent resource.
     *
     */
    public TuesdayBaseResource<?> resource();

    /**
     * Access to the schema tables.
     *
     */
    public interface Tables<TableType>
        {
        /**
         * Select all of the tables in this schema.
         *
         */
        public Iterable<TableType> select();
        /**
         * Search for tables (name starts with).
         *
         */
        public Iterable<TableType> search(final String text);
        /**
         * Select a specific column by name.
         *
         */
        public TableType select(final String name);
        }

    /**
     * Access to the schema tables.
     *
     */
    public Tables<TableType> tables();

    public String alias();
    public StringBuilder fullname();

    }
