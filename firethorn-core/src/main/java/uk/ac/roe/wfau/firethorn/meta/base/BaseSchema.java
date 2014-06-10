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
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;

/**
 *
 *
 */
public interface BaseSchema<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
extends BaseComponent
    {
    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }
    
    /**
     * {@link Entity.NameFactory} interface.
     *
     */
    public static interface NameFactory<SchemaType extends BaseSchema<?,?>>
    extends Entity.NameFactory<SchemaType>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory<SchemaType extends BaseSchema<?,?>>
    extends Entity.LinkFactory<SchemaType>
        {
        }

    /**
     * {@link Entity.EntityResolver} interface.
     *
     */
    public static interface EntityResolver<ResourceType extends BaseResource<SchemaType>, SchemaType extends BaseSchema<SchemaType,?>>
    extends Entity.EntityFactory<SchemaType>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<ResourceType extends BaseResource<SchemaType>, SchemaType extends BaseSchema<SchemaType,?>>
    extends Entity.EntityFactory<SchemaType>
        {
        /**
         * Select all the schemas from a resource.
         *
         */
        public Iterable<SchemaType> select(final ResourceType parent);

        /**
         * Select a schema by name.
         *
         */
        public SchemaType select(final ResourceType parent, final String name)
        throws NameNotFoundException;

        /**
         * Search for a schema by name.
         *
         */
        public SchemaType search(final ResourceType parent, final String name);

        }

    /**
     * The {@link BaseSchema} this schema is derived from.
     *
     */
    public BaseSchema<?, ?> base();

    /**
     * The root of the chain that this schema is derived from.
     *
     */
    public BaseSchema<?, ?> root();

    /**
     * Our parent {@linkBaseResource resource}.
     *
     */
    public BaseResource<?> resource();

    /**
     * Our schema {@link BaseTable tables}.
     *
     */
    public interface Tables<TableType>
        {
        /**
         * Select all of the {@link BaseTable tables} in this schema.
         *
         */
        public Iterable<TableType> select();

        /**
         * Search for a {@link BaseTable table} by name.
         *
         */
        public TableType search(final String name);

        /**
         * Select a {@link BaseTable table} by name.
         *
         */
        public TableType select(final String name)
        throws NameNotFoundException;

        /**
         * Select a {@link BaseTable table} by ident.
         *
         */
        public TableType select(final Identifier ident)
        throws IdentifierNotFoundException;

        }

    /**
     * Our table {@link BaseTable tables}.
     *
     */
    public Tables<TableType> tables();

    /**
     * The fully qualified schema name.
     *
     */
    public StringBuilder namebuilder();

    /**
     * The {@link BaseSchema} metadata.
     *
     */
    public interface Metadata
        {
        }

    /**
     * The {@link BaseSchema} metadata.
     *
     */
    public AdqlSchema.Metadata meta();

    }
