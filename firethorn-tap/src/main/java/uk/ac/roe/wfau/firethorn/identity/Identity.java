/*
 * Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.roe.wfau.firethorn.identity;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.mallard.AdqlService;

/**
 * Public interface fo an Identity.
 * 
 */
public interface Identity
extends Entity
    {

    /**
     * An Identity context.
     * 
     */
    public static interface Context
        {

        /**
         * An Identity Context factory.
         * 
         */
        public static interface Factory
            {

            /**
             * Get the current Identity Context.
             * 
             * @returns The current Identity Context.
             * 
             */
            public Context context();

            }

        /**
         * Get the current Identity.
         * 
         * @returns The current Identity in this context.
         * 
         */
        public Identity identity();

        }

    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<Identity>
        {
        }

    /**
     * An Identity factory.
     * 
     */
    public static interface Factory
    extends Entity.Factory<Identity>
        {

        /**
         * Create a new Identity.
         * 
         * @param name
         *            - The new Identity name.
         * @returns A new Identity.
         * 
         */
        public Identity create(final String name);

        /**
         * Select all the Identities managed by this Factory.
         * 
         * @returns An Iterable iterator of all the Identities managed by this
         *          Factory.
         * 
         */
        public Iterable<Identity> select();

        }
    }

