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
 * Public interface for an Identity authentication.
 *
 */
public interface Authentication
extends Entity
    {
    /**
     * Name factory interface.
     *
    public static interface NameFactory
    extends Entity.NameFactory
        {
        }
     */

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<Authentication>
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
     * Factory interface.
     *
     */
    public interface EntityFactory
    extends Entity.EntityFactory<Authentication>
        {
        /**
         * Get the current active Authentication.
         *
         */
        public Authentication current();

        /**
         * Create a new Authentication.
         *
         */
        public Authentication create(final Operation oper, final Identity identity, final String method);

        }

    /**
     * The URN for the authentication method.
     *
     */
    public String method();

    /**
     * The Identity.
     *
     */
    public Identity identity();

    /**
     * The Operation.
     *
     */
    public Operation operation();

    }
