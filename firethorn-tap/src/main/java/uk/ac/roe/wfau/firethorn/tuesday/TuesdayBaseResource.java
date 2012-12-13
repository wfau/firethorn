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
public interface TuesdayBaseResource<SchemaType extends TuesdayBaseSchema<SchemaType,?>>
extends TuesdayBaseComponent
    {

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayBaseResource<?>>
        {
        }

    /**
     * Resource factory interface.
     *
     */
    public static interface Factory<ResourceType extends TuesdayBaseResource<?>>
    extends Entity.Factory<ResourceType>
        {
        /**
         * Create a new resource.
         *
         */
        public ResourceType create(final String name);

        /**
         * Select all the available resources.
         *
         */
        public Iterable<ResourceType> select();

        /**
         * Text search for resources (name starts with).
         *
         */
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
        public SchemaType select(String name);

        /**
         * Search for schemas by name.
         *
         */
        public Iterable<SchemaType> search(String text);
        } 

    /**
     * Access to the schemas for this resource.
     * 
     */
    public Schemas<SchemaType> schemas();

    //public String alias();
    public StringBuilder fullname();
    
    }
