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
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.job.test.TestJob;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;

/**
 *
 *
 */
public interface AdqlQuery
extends Entity, Job
    {
    /**
     * Our local service implementations.
     * 
     */
    public static interface Services
        {
        /**
         * Our Name factory.
         * 
         */
        public NameFactory names();

        /**
         * Our Link factory.
         * 
         */
        public LinkFactory links();

        /**
         * Our Ident factory.
         * 
         */
        public IdentFactory idents();

        /**
         * Our Query resolver.
         * 
         */
        public Resolver resolver();

        /**
         * Our Query Factory.
         * 
         */
        public Factory factory();

        /**
         * Our Query executor.
         * 
         */
        public Executor executor();

        }

    /**
     * Our local service implementations.
     *
    public Services services();
     */

    /**
     * Name factory interface.
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
     * Resolver interface.
     *
     */
    public static interface Resolver
    extends Entity.Factory<AdqlQuery>
        {
        }

    /**
     * Factory interface.
     *
     */
    public static interface Factory
    extends Job.Factory<AdqlQuery>
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

        }

    /**
     * Get the input text.
     *
     */
    public String input();

    /**
     * Set the input text.
     *
     */
    public void input(final String input);

    /**
     * Prepare the Job for execution.
     *
    public Status prepare(String input);
     */

    /**
     * Query syntax validation status.
     *
     */
    public interface Syntax
        {
        /**
         * The validation status.
         * 
         */
        public enum Status
            {
            /**
             * The query has been parsed and is valid ADQL.
             * 
             */
            VALID(),
            
            /**
             * A parser error in the ADQL query.
             * 
             */
            PARSE_ERROR(),

            /**
             * A translation error processing the query.
             * 
             */
            TRANS_ERROR(),

            /**
             * Unknown state - the query hasn't been parsed yet.
             * 
             */
            UNKNOWN();
            }

        /**
         * The validation status.
         * 
         */
        public Status status();

        /**
         * The original parser error message.
         * 
         */
        public String message();

        /**
         * A user friendly message.
         * 
         */
        public String friendly();
        
        }
    
    /**
     * Get the syntax validation status.
     *
     */
    public Syntax syntax();

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
         * Distributed query handled DQP.
         *
         */
        DISTRIBUTED();

        }

    /**
     * The OGSA-DAI query mode.
     *
     */
    public Mode mode();

    /**
     * The ADQL resource this query applies to.
     *
     */
    public AdqlResource resource();
    
    /**
     * The processed ADQL query.
     *
     */
    public String adql();

    /**
     * The processed SQL query.
     *
     */
    public String osql();

    /**
     * A list of AdqlColumns used by the query.
     *
     */
    public Iterable<AdqlColumn> columns();

    /**
     * A list of AdqlTables used by the query.
     *
     */
    public Iterable<AdqlTable> tables();

    /**
     * A list of BaseResources used by the query.
     *
     */
    public Iterable<BaseResource<?>> targets();

    /**
     * The primary BaseResource used by the query.
     *
     */
    public BaseResource<?> target();
    
    }
