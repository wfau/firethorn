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
package uk.ac.roe.wfau.firethorn.meta.base;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;

/**
 *
 *
 */
public interface BaseSchema<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
extends BaseComponent
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<BaseSchema<?,?>>
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
     * Schema resolver interface.
     *
     */
    public static interface Resolver
    extends Entity.EntityFactory<BaseSchema<?,?>>
        {

        }

    /**
     * Schema factory interface.
     *
     */
    public static interface Factory<ResourceType extends BaseResource<SchemaType>, SchemaType extends BaseSchema<SchemaType,?>>
    extends Entity.EntityFactory<SchemaType>
        {
        /**
         * Select all the schemas from a resource.
         *
         */
        public Iterable<SchemaType> select(final ResourceType parent);

        /**
         * Select a named schema from a resource.
         *
         */
        public SchemaType select(final ResourceType parent, final String name)
        throws NotFoundException;

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
    public BaseResource<?> resource();

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

    /**
     * The fully qualified name.
     *
     */
    public StringBuilder namebuilder();

    }
