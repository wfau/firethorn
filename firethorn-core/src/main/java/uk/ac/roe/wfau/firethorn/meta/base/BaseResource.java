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

/**
 *
 *
 */
public interface BaseResource<SchemaType extends BaseSchema<SchemaType,?>>
extends BaseComponent
    {

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
    public static interface Factory<ResourceType extends BaseResource<?>>
    extends Entity.EntityFactory<ResourceType>
        {
        /**
         * Select all the available resources.
         *
         */
        public Iterable<ResourceType> select();

        /**
         * Select Resources ...
         *
         */
        @Deprecated
        public Iterable<ResourceType> search(final String text);

        }

    /**
     * Access to the schemas for this resource.
     *
     */
    public interface Schemas<SchemaType>
        {
        /**
         * Select all the schemas for this resource.
         *
         */
        public Iterable<SchemaType> select();

        /**
         * Select a specific schema by name.
         *
         */
        public SchemaType select(final String name);

        /**
         * Search for schemas by name.
         *
         */
        public Iterable<SchemaType> search(final String text);

        }

    /**
     * Access to the schemas for this resource.
     *
     */
    public Schemas<SchemaType> schemas();

    /**
     * The fully qualified resource name.
     *
     */
    public StringBuilder fullname();

    /**
     * Get the OGSA-DAI resource ID.
     * @too Move this to an OGSA-DAI specific resource.
     *
     */
    public String ogsaid();

    /**
     * Set the OGSA-DAI resource ID.
     * @too Move this to an OGSA-DAI specific resource.
     *
     */
    public void ogsaid(final String ogsaid);

    }
