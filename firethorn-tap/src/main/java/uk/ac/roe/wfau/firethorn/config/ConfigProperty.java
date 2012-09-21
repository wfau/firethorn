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
package uk.ac.roe.wfau.firethorn.config;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.exception.EntityNotFoundException;

/**
 * Public interface for a configuration property.
 *
 */
public interface ConfigProperty
extends Entity
    {

    /**
     * A ConfigProperty factory.
     *
     */
    public static interface Factory
    extends Entity.Factory<ConfigProperty>
        {

        /**
         * Create a new ConfigProperty.
         * @param key
         *      The property key.
         * @param name
         *      A descriptive name.
         * @param value
         *      The property value.
         * @return
         *      A new ConfigProperty.    
         *
         */
        public ConfigProperty create(final URI key, final String name, final String value);

        /**
         * Select a specific ConfigProperty by key.
         * @param key
         *      The property key.
         * @return
         *      The corresponding ConfigProperty    
         * @throws EntityNotFoundException 
         *      If no matching property was found.  
         *
         */
        public ConfigProperty select(final URI key)
        throws EntityNotFoundException;

        /**
         * Search for a ConfigProperty by key.
         * @param key
         *      The property key.
         * @return
         *      The corresponding ConfigProperty, or null if none found.    
         *
         */
        public ConfigProperty search(final URI key);

        /**
         * Select/create a ConfigProperty.
         * If the corresponding ConfigProperty does not exist yet,
         * then a new one is created using the key, name and value.
         * 
         * @param key
         *      The property key.
         * @param name
         *      A descriptive name.
         * @param value
         *      The default value.
         * @return
         *      The corresponding ConfigProperty.    
         *
         */
        public ConfigProperty select(final URI key, final String name, final String value);

        }

    /**
     * The property key.
     * 
     */
    public URI key();
    
    /**
     * The property value.
     * 
     */
    public String value();
    
    }
