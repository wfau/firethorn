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
 * Public interface for an {@link Authentication} {@link Entity}.
 *
 */
public interface AuthenticationImpl
extends Entity, Authentication
    {

    /**
     * {@link AuthenticationImpl} {@link Link} factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<AuthenticationImpl>
        {
        }

    /**
     * {@link AuthenticationImpl} {@link Identifier} factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AuthenticationImpl>
        {
        }

    /**
     * {@link AuthenticationImpl} factory interface.
     *
     */
    public interface EntityFactory
    extends Entity.EntityFactory<AuthenticationImpl>
        {
        /**
         * Get the current active {@link AuthenticationImpl}.
         *
         */
        public AuthenticationImpl current();

        /**
         * Create a new {@link AuthenticationImpl}.
         *
         */
        public AuthenticationImpl create(final Operation oper, final Identity identity, final String method);

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends Entity.EntityServices<AuthenticationImpl>
        {
        /**
         * Our {@link AuthenticationImpl.EntityFactory} instance.
         *
         */
        public AuthenticationImpl.EntityFactory entities();
        }

    /**
     * The Operation this {@link Authentication} applies to.
     * TODO - do we need this in the interface ?
    public Operation operation();
     */

    }
