/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.identity;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 * Public interface for a Community of Identities.
 *
 */
public interface Community
extends Entity
    {

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<Community>
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
     * Factory interface for Communities.
     * 
     */
    public interface Factory
    extends Entity.Factory<Community>
        {
        public Community create(final String uri, final String name);
        public Community select(final String uri);
        }
    
    /**
     * Access to the Identities in this Community.
     * 
     */
    public interface Identities
        {
        /**
         * Create a new Identity.
         *
         */
        public Identity create(final String name);

        /**
         * Select an existing Identity.
         *
         */
        public Identity select(final String name);

        }

    /**
     * Access to the Identities in this Community.
     * 
     */
    public Identities identities();

    /**
     * The unique identifier.
     * 
     */
    public String uri();

    }
