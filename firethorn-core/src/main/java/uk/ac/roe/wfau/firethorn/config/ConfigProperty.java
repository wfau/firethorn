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

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 * Public interface for a configuration property.
 *
 */
public interface ConfigProperty
extends Entity
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<ConfigProperty>
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
     * A ConfigProperty factory.
     *
     */
    public static interface Factory
    extends Entity.EntityFactory<ConfigProperty>
        {

        /**
         * Create or update a ConfigProperty.
         * @param key
         *      The property key.
         * @param name
         *      A descriptive name.
         * @param value
         *      The property value.
         * @return
         *      The corresponding ConfigProperty.
         *
         */
        public ConfigProperty create(final URI key, final String name, final String value);

        /**
         * Select a specific ConfigProperty by key.
         * @param key
         *      The property key.
         * @return
         *      The corresponding ConfigProperty, or null if none found.
         *
         */
        public ConfigProperty select(final URI key);

        }

    /**
     * The property key.
     *
     */
    public URI key();

    /**
     * The property value as a String.
     *
     */
    @Override
    public String toString();

    /**
     * The property value as a URI.
     *
     */
    public URI toUri();

    }
