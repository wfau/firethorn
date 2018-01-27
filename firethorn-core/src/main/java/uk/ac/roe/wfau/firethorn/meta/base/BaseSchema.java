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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;

/**
 * Public interface for schema.
 *
 */
public interface BaseSchema<SchemaType extends BaseSchema<SchemaType, TableType>, TableType extends BaseTable<TableType, ?>>
extends TreeComponent
    {
    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory<SchemaType extends BaseSchema<?,?>>
    extends Entity.LinkFactory<SchemaType>
        {
        }

    /**
     * A resolver to resolve links. 
     *
     */
    public static interface EntityResolver
        {
        /**
         * Resolve a link into a {@link BaseSchema}.
         * @throws IdentifierFormatException
         * @throws IdentifierNotFoundException 
         * @throws EntityNotFoundException 
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *  
         */
        public BaseSchema<?,?> resolve(String link)
        throws ProtectionException, IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException;
        }

    /**
     * {@link TreeComponent.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<ResourceType extends BaseResource<SchemaType>, SchemaType extends BaseSchema<SchemaType,?>>
    extends TreeComponent.EntityFactory<SchemaType>
        {
        /**
         * Select all the schemas from a resource.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<SchemaType> select(final ResourceType parent)
        throws ProtectionException;

        /**
         * Select a schema by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public SchemaType select(final ResourceType parent, final String name)
        throws NameNotFoundException, ProtectionException;

        /**
         * Search for a schema by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public SchemaType search(final ResourceType parent, final String name)
        throws ProtectionException;

        }

    /**
     * The {@link BaseSchema} this schema is derived from.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseSchema<?, ?> base()
    throws ProtectionException;

    /**
     * The root of the chain that this schema is derived from.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseSchema<?, ?> root()
    throws ProtectionException;

    /**
     * Our parent {@linkBaseResource resource}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseResource<?> resource()
    throws ProtectionException;

    /**
     * Our schema {@link BaseTable tables}.
     *
     */
    public interface Tables<TableType>
        {

        /**
         * Select all of the {@link BaseTable tables} in this schema.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<TableType> select()
        throws ProtectionException;

        /**
         * Search for a {@link BaseTable table} by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public TableType search(final String name)
        throws ProtectionException;

        /**
         * Select a {@link BaseTable table} by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public TableType select(final String name)
        throws ProtectionException, NameNotFoundException;

        /**
         * Select a {@link BaseTable table} by ident.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public TableType select(final Identifier ident)
        throws ProtectionException, IdentifierNotFoundException;

        }

    /**
     * Our table {@link BaseTable tables}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public Tables<TableType> tables()
    throws ProtectionException;

    /**
     * The {@link BaseSchema} metadata.
     *
     */
    public interface Metadata
        {
        /**
         * The schema name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         */
        @Deprecated
        public String name()
        throws ProtectionException;

        }

    /**
     * The {@link BaseSchema} metadata.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public AdqlSchema.Metadata meta()
    throws ProtectionException;

    }
