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
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;

/**
 *
 *
 */
public interface BaseResource<SchemaType extends BaseSchema<SchemaType,?>>
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
    public static interface NameFactory<ResourceType extends BaseResource<?>>
    extends Entity.NameFactory<ResourceType>
        {
        }
    
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
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory<ResourceType extends BaseResource<?>>
    extends Entity.EntityFactory<ResourceType>
        {
        /**
         * Select all the available resources.
         * @todo Need a better entry point.
         *
         */
        public Iterable<ResourceType> select();

        }

    /**
     * Our resource {@link BaseSchema schema}.
     *
     */
    public interface Schemas<SchemaType>
        {
        /**
         * Select all the {@link BaseSchema schema} for this resource.
         *
         */
        public Iterable<SchemaType> select();

        /**
         * Select a {@link BaseSchema schema} by name.
         *
         */
        public SchemaType select(final String name)
        throws NameNotFoundException;

        /**
         * Search for a {@link BaseSchema schema} by name.
         *
         */
        public SchemaType search(final String name);

        }

    /**
     * Our resource {@link BaseSchema schema}.
     *
     */
    public Schemas<SchemaType> schemas();

    /**
     * The fully qualified resource name.
     *
     */
    public StringBuilder namebuilder();

    /**
     * Get the OGSA-DAI resource ID.
     * @too Move this to an OGSA-DAI specific resource.
     *
     */
    @Deprecated
    public String ogsaid();

    /**
     * Set the OGSA-DAI resource ID.
     * @too Move this to an OGSA-DAI specific resource.
     *
     */
    @Deprecated
    public void ogsaid(final String ogsaid);

    /**
     * The {@link BaseResource} metadata.
     *
     */
    public interface Metadata
        {
        /**
         * The resource name.
         * 
        public String name();
         */

        /**
         * The resource description.
         * 
        public String text();
         */

        /**
         * The OGSA-DAI metadata.
         * 
         */
        public interface Ogsa
            {
            /**
             * Get the OGSA-DAI resource ID.
             *
             */
            public String id();
            }
        /**
         * The OGSA-DAI metadata.
         * 
         */
        public Ogsa ogsa();
        }

    /**
     * The {@link BaseResource} metadata.
     *
     */
    public BaseResource.Metadata meta();
    
    }
