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
package uk.ac.roe.wfau.firethorn.job.frog.inner;

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.job.Job.Status;

/**
 * Internal interface for the Job Status state machine.
 * These methods execute in a new database transaction.
 * 
 */
public interface JobStatus
    {

    /**
     * Check the Job status.
     * This method executes in a new database transaction.
     * 
     */
    public Status status();

    /**
     * Update the Job status to a new state.
     * This method executes in a new database transaction.
     * 
     */
    public Status status(final Status status);

    /**
     * Internal interface for a Job state machine controller.
     * 
     */
    public interface Controller
        {
        /**
         * Check a Job status.
         * This method executes in a new database transaction.
         * 
         */
        public Status status(final JobStatus job);

        /**
         * Check a Job status.
         * This method executes in a new database transaction.
         * 
         */
        public Status status(final Identifier ident);

        /**
         * Update a Job status to a new state.
         * This method executes in a new database transaction.
         *  
         */
        public Status status(final JobStatus job, final Status status);

        /**
         * Update a Job status to a new state.
         * This method executes in a new database transaction.
         *
         */
        public Status status(final Identifier ident, final Status status);

        }
    }

