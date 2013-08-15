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
 * Public interface for an operation.
 *
 */
public interface Operation
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
    extends Entity.LinkFactory<Operation>
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
    extends Entity.EntityFactory<Operation>
        {
        /**
         * Access to the current operation.
         *
         */
        public Operation current();

        /**
         * Create a new Operation.
         *
         */
        public Operation create(final String target, final String method, final String source);

        }

    public String target();
    public String method();
    public String source();

    /*
    @Deprecated
    public Authentication auth();
     */

    interface Authentications
        {
        public void resolve();

        public Authentication primary();

        public Authentication create(final Identity identity, final String method);

        public Iterable<Authentication> select();
        }

    public Authentications authentications();

    }
