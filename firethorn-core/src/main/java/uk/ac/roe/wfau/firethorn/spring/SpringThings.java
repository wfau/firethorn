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
package uk.ac.roe.wfau.firethorn.spring;

import org.springframework.context.ApplicationContext;


/**
 * A global instance to handle Spring related things.
 *
 */
public interface SpringThings
    {
    /**
     * Public interface for a transaction wrapper.
     *
     */
    public interface TransactionWrapper
        {
        /**
         * Perform an action in a read-only transaction.
         *
         */
        public void select(final Runnable runnable);

        /**
         * Perform an action in a read-write transaction.
         *
         */
        public void update(final Runnable runnable);

        }
    /**
     * Our transaction wrapper.
     *
     */
    public SpringThings.TransactionWrapper transactor();

    /**
     * The current ApplicationContext.
     *
     */
    public ApplicationContext context();

    }
