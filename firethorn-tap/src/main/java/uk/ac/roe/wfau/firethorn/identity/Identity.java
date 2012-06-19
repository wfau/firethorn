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
package uk.ac.roe.wfau.firethorn.identity ;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 * Public Identity interface.
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
         * Get the current Identity.
         *
         */
        public Identity current();
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
         */
        public Identity create(String name);

        /**
         * Select all the Identities.
         *
         */
        public Iterable<Identity> select();

        }



    }
