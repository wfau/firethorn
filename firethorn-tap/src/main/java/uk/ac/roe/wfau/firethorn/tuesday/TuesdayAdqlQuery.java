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
package uk.ac.roe.wfau.firethorn.tuesday;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 *
 *
 */
public interface TuesdayAdqlQuery
extends Entity
    {
    /**
     * Nname factory interface.
     *
     */
    public static interface NameFactory
    extends Entity.NameFactory
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TuesdayAdqlQuery>
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
    public static interface Factory
    extends Entity.Factory<TuesdayAdqlQuery>
        {
        /**
         * Create a new query.
         *
         */
        public TuesdayAdqlQuery create(final TuesdayAdqlResource resource, final String input);

        /**
         * Create a new query.
         *
         */
        public TuesdayAdqlQuery create(final TuesdayAdqlResource resource, final String name, final String input);

        /**
         * Select all the queries from a resource.
         *
         */
        public Iterable<TuesdayAdqlQuery> select(final TuesdayAdqlResource resource);

        /**
         * Text search for queries (name starts with).
         *
         */
        public Iterable<TuesdayAdqlQuery> search(final TuesdayAdqlResource resource, final String text);
        
        /**
         * Access to our name factory.
         *
         */
        public TuesdayAdqlQuery.NameFactory names();

        }

    /**
     * Query status indicator.
     *
     */
    public enum Status
        {
        /**
         * The query is being edited.
         *
         */
        EDITING(),

        /**
         * The query has an (ADQL) error.
         *
         */
        ERROR(),

        /**
         * The query is waiting in the queue.
         *
         */
        PENDING(),

        /**
         * The query is running.
         *
         */
        RUNNING(),

        /**
         * The query completed.
         *
         */
        COMPLETED(),

        /**
         * The query was cancelled.
         *
         */
        CANCELLED(),

        /**
         * The query failed to execute.
         *
         */
        FAILED();

        }

    /**
     * OGSA-DAI query mode.
     *
     */
    public enum Mode
        {
        /**
         * Direct query to a single resource.
         *
         */
        DIRECT(),

        /**
         * Distributed query handled by the OGSA-DAI DQP.
         *
         */
        DISTRIBUTED();

        }

    /**
     * The ADQL workspace this query applies to.
     *
     */
    public TuesdayAdqlResource resource();

    /**
     * The query mode.
     *
     */
    public Mode mode();

    /**
     * The query status.
     *
     */
    public Status status();

    /**
     * Set the query status.
     *
     */
    public void status(Status status);

    /**
     * The input query.
     *
     */
    public String query();

    /**
     * Set the input query.
     *
     */
    public void query(final String input);

    /**
     * The processed ADQL query.
     *
     */
    public String adql();

    /**
     * The processed SQL query we pass to OGSA-DAI.
     *
     */
    public String ogsa();

    /**
     * The set of the AdqlColumns used by the query.
     *
     */
    public Iterable<TuesdayAdqlColumn> columns();

    /**
     * The set of the AdqlTables used by the query.
     *
     */
    public Iterable<TuesdayAdqlTable> tables();

    /**
     * The set of the OGSA-DAI resources used by the query.
     *
     */
    public Iterable<TuesdayOgsaResource<?>> resources();

    /**
     * Parse the query and update our properties.
     *
    public void parse();
     */

    }
