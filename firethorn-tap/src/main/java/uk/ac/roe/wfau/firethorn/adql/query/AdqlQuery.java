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
package uk.ac.roe.wfau.firethorn.adql.query;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaResource;

/**
 *
 *
 */
public interface AdqlQuery
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
    extends Entity.LinkFactory<AdqlQuery>
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
    extends Entity.Factory<AdqlQuery>
        {
        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final AdqlResource resource, final String input);

        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final AdqlResource resource, final String name, final String input);

        /**
         * Select all the queries from a resource.
         *
         */
        public Iterable<AdqlQuery> select(final AdqlResource resource);

        /**
         * Text search for queries (name starts with).
         *
         */
        public Iterable<AdqlQuery> search(final AdqlResource resource, final String text);
        
        /**
         * Access to our name factory.
         *
         */
        public AdqlQuery.NameFactory names();

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
    public AdqlResource resource();

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
     * The original input query.
     *
     */
    public String input();

    /**
     * Update the input query.
     *
     */
    public void input(final String input);

    /**
     * The processed ADQL query.
     *
     */
    public String adql();

    /**
     * The processed SQL query we pass to OGSA-DAI.
     *
     */
    public String osql();

    /**
     * The set of the AdqlColumns used by the query.
     *
     */
    public Iterable<AdqlColumn> columns();

    /**
     * The set of the AdqlTables used by the query.
     *
     */
    public Iterable<AdqlTable> tables();

    /**
     * The set of the OGSA-DAI resources used by the query.
     *
     */
    public Iterable<OgsaResource<?>> resources();

    }
