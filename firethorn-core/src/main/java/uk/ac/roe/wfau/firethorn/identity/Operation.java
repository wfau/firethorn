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
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<Operation>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<Operation>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public interface EntityFactory
    extends Entity.EntityFactory<Operation>
        {
        /**
         * Get the current active operation.
         *
         */
        public Operation current();

        /**
         * Create a new Operation.
         *
         */
        public Operation create(final String target, final String method, final String source);

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends Entity.EntityServices<Operation>
        {
        }
    
    /**
     * The target URL for the operation.
     *
     */
    public String target();

    /**
     * The HTTP method.
     *
     */
    public String method();

    /**
     * The remote address.
     *
     */
    public String source();

    /**
     * The list of Authentication(s) for this operation.
     *
     */
    public static interface Authentications
        {
        /**
         * The primary Authentication for this operation.
         *
         */
        public Authentication primary();

        /**
         * Create a new Authentication for this operation.
         *
         */
        public Authentication create(final Identity identity, final String method);

        /**
         * Get the list of Authentication(s) for this operation.
         *
         */
        public Iterable<Authentication> select();
        }

    /**
     * Access to the list of Authentication(s) for this operation.
     *
     */
    public Authentications auth();

    }
