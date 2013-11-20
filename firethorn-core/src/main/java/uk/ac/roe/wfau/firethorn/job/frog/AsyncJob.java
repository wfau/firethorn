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
package uk.ac.roe.wfau.firethorn.job.frog;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.job.Job.Status;

/**
 * Asynchronous interface for a Job.
 * The methods in this interface are asynchronous and non-blocking.
 * 
 */
public interface AsyncJob
    {
    /**
     * Start a Job running, and return a Future to collect the status.
     * This method is asynchronous and non-blocking.
     *  
     */
    public Future<Status> start();

    /**
     * Start a Job running, and return a Future to collect the status.
     * This method is asynchronous and non-blocking.
     *  
     */
    public Future<Status> start(final long wait, final TimeUnit unit);

    /**
     * Stop (cancel) a running Job, and return a Future to collect the status.
     * This method is asynchronous and non-blocking.
     *  
     */
    public Future<Status> stop();

    /**
     * Stop (cancel) a running Job, and return a Future to collect the status.
     * This method is asynchronous and non-blocking.
     *  
     */
    public Future<Status> stop(final long wait, final TimeUnit unit);

    /**
     * Public interface for an asynchronous Job controller.
     * The methods in this interface are asynchronous and non-blocking.
     * 
     */
    public interface Controller
        {
        /**
         * Start a Job running, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> start(final AsyncJob job);

        /**
         * Start a Job running, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> start(final Identifier ident);

        /**
         * Start a Job running, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> start(final AsyncJob job, final long wait, final TimeUnit unit);

        /**
         * Start a Job running, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> start(final Identifier ident, final long wait, final TimeUnit unit);

        /**
         * Stop (cancel) a running Job, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> stop(final AsyncJob job);

        /**
         * Stop (cancel) a running Job, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> stop(final Identifier ident);

        /**
         * Stop (cancel) a running Job, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> stop(final AsyncJob job, final long wait, final TimeUnit unit);

        /**
         * Stop (cancel) a running Job, and return a Future to collect the status.
         * This method is asynchronous and non-blocking.
         *  
         */
        public Future<Status> stop(final Identifier ident, final long wait, final TimeUnit unit);
        
        }
    }

