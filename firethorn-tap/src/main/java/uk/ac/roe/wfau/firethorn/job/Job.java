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

import org.joda.time.DateTime;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;

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
     * Wraps the Job.status(), Job.prepare() and Job.execute() methods with Spring managed Transaction handling.
     *
     */
    public static interface Executor
        {
        public static final int MINIMUM_TIMEOUT =  5 ;
        public static final int DEFAULT_TIMEOUT = 10 ;

        /**
         * Check a job status.
         * 
         */
        public Status status(final Identifier ident);

        /**
         * Update a job status.
         * 
         */
        public Status status(final Identifier ident, final Status status);

        /**
         * Update the status, with a timeout in milliseconds.
         *
         */
        public Status update(final Identifier ident, final Job.Status next, final Integer timeout);

        /**
         * Prepare a job.
         * 
         */
        public Status prepare(final Identifier iadent);

        /**
         * Execute a job.
         * 
         */
        public Future<Status> execute(final Identifier ident);
        
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
     * Get the Job status (with refresh).
     *
     */
    public Status status(boolean refresh);

    /**
     * Set the Job status.
     *
     */
    public Status status(final Status status);

    /**
     * The date/time the Job was queued.
     *
     */
    public DateTime queued();

    /**
     * The date/time the Job was started.
     *
     */
    public DateTime started();

    /**
     * The date/time the Job was finished.
     *
     */
    public DateTime finished();

    /**
     * Prepare the Job.
     *
     */
    public Status prepare();

    /**
     * Execute the Job.
     *
     */
    public Status execute();
    
    }
