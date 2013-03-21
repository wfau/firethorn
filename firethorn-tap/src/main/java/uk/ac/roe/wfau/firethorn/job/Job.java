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
package uk.ac.roe.wfau.firethorn.job;

import java.util.concurrent.Future;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 * Abstract representation of a Job.
 *
 */
public interface Job
extends Entity
    {
    /**
     * Our local service implementations.
     *
     */
    public interface Services
        {
        /**
         * Our LinkFactory.
         * 
         */
        public LinkFactory links();

        /**
         * Our IdentFactory.
         * 
         */
        public IdentFactory idents();

        /**
         * Our Job resolver.
         * 
         */
        public Resolver resolver();

        /**
         * Our Job executor.
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
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<Job>
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
     * Job resolver interface.
     *
     */
    public static interface Resolver
    extends Entity.Factory<Job>
        {
        }

    /**
     * Job factory interface.
     *
     */
    public static interface Factory<JobType extends Job>
    extends Entity.Factory<JobType>
        {
        /**
         * Select all the available jobs.
         *
         */
        public Iterable<JobType> select();
        
        }

    /**
     * Job executor interface.
     * This wraps the Job.status(), Job.prepare() and Job.execute() methods with Spring managed Transaction handling.
     *
     */
    public static interface Executor
        {
        /**
         * Functor interface for an update.
         * 
         */
        public static interface Update
            {
            public Status update();
            }

        /**
         * Perform an Update inside a transaction.
         * 
         */
        public Status update(Update update);
        
        /**
         * Functor interface used by prepare() and execute().
         * 
         */
        public static interface Executable
            {
            /**
             * Execute the function.
             *
             */
            public Status execute();
            }
        
        /**
         * Prepare a Job for execution.
         *
         */
        public Status prepare(Executable executable);

        /**
         * Execute a Job asynchronously.
         *
         */
        public Future<Status> execute(Executable executable);
        
        }
    
    /**
     * Job status indicator.
     *
     */
    public enum Status
        {
        /**
         * The Job is being edited.
         *
         */
        EDITING(),

        /**
         * The Job is ready to be run.
         *
         */
        READY(),

        /**
         * The Job is waiting to run.
         *
         */
        PENDING(),

        /**
         * The Job is running.
         *
         */
        RUNNING(),

        /**
         * The Job has been completed.
         *
         */
        COMPLETED(),

        /**
         * The Job was cancelled.
         *
         */
        CANCELLED(),

        /**
         * The Job failed to execute.
         *
         */
        FAILED(),

        /**
         * An error occurred updating the Job status.
         * The actual Job status is undefined. 
         *
         */
        ERROR();

        }

    /**
     * Get the Job status.
     *
     */
    public Status status();

    /**
     * Set the Job status.
     * @todo This should be protected by a transaction handler.
     *
    public Status status(Status status);
     */

    /**
     * Set the Job status.
     * This uses a call to the Job Executor to add transaction handling.
     *
    public Status update(Status status);
     */

    /**
     * Prepare the Job for execution.
     *
     */
    public Status prepare();

    /**
     * Execute the Job asynchronously.
     *
     */
    public Future<Status> execute();
    
    /**
     * Cancel the Job.
     *
     */
    public Status cancel();
    
    }
