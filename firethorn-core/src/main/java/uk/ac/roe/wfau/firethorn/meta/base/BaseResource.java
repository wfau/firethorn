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
import uk.ac.roe.wfau.firethorn.adql.parser.AdqlTranslator;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaBaseResource;

/**
 * Public interface for a resource.
 *
 */
public interface BaseResource<SchemaType extends BaseSchema<SchemaType,?>>
extends TreeComponent
    {
    
    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory<ResourceType extends BaseResource<?>>
    extends Entity.LinkFactory<ResourceType>
        {
        }

    /**
     * {@link Entity.EntityResolver} interface.
     *
     */
    public static interface EntityResolver<ResourceType extends BaseResource<?>>
    extends Entity.EntityFactory<ResourceType>
        {
        }

    /**
     * {@link TreeComponent.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<ResourceType extends BaseResource<?>>
    extends TreeComponent.EntityFactory<ResourceType>
        {
        /**
         * Select all the available resources.
         * @throws ProtectionException 
         *
         */
        public Iterable<ResourceType> select()
        throws ProtectionException;

        }

    /**
     * Our resource {@link BaseSchema schema}.
     *
     */
    public interface Schemas<SchemaType>
        {
        /**
         * Select all the {@link BaseSchema schema} for this resource.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Iterable<SchemaType> select()
        throws ProtectionException;

        /**
         * Select a {@link BaseSchema schema} by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public SchemaType select(final String name)
        throws ProtectionException, NameNotFoundException;

        /**
         * Search for a {@link BaseSchema schema} by name.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public SchemaType search(final String name)
        throws ProtectionException;

        }

    /**
     * Our resource {@link BaseSchema schema}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public Schemas<SchemaType> schemas()
    throws ProtectionException;

    /**
     * The {@link BaseResource} metadata.
     *
     */
    public interface Metadata
        {
        }

    /**
     * The {@link BaseResource} metadata.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     *
     */
    public BaseResource.Metadata meta()
    throws ProtectionException;
    
    /**
     * Interface to access the {@link OgsaBaseResource} OGSA-DAI resources for this {@link BaseResource}.
     * @todo Move this to RootResource rather than BaseResource.
     * 
     */
    public interface OgsaBaseResources
        {
        /**
         * Select the primary {@link OgsaBaseResource} OGSA-DAI resource for this {@link BaseResource}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public OgsaBaseResource primary()
        throws ProtectionException;
        }

    /**
     * Access to the {@link OgsaBaseResource} OGSA-DAI resources for this {@link BaseResource}.
     * @todo Move this to {@link RootResource} rather than {@link BaseResource}.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public OgsaBaseResources ogsa()
    throws ProtectionException;

    /**
     * Access to the ADQLTranslator for this resource.
     * 
     */
    public AdqlTranslator translator();
    
    }
