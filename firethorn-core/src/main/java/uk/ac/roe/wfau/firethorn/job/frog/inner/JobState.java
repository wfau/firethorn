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

import uk.ac.roe.wfau.firethorn.job.Job.Status;

/**
 * Internal interface for the Job Status state machine.
 * These methods are not transactional.
 * 
 */
public interface JobState
    {

    /**
     * Check the Job status.
     * 
     */
    public Status state();

    /**
     * Update the Job status to a new state.
     * This method is not transactional.
     * 
     */
    public Status state(final Status status);

    /**
     * Internal interface for a Job state machine controller.
     * 
     */
    public interface Controller
        {
        /**
         * Check a Job status.
         * 
         */
        public Status state(final JobState job);

        /**
         * Update a Job status to a new state.
         * This method is not transactional.
         *  
         */
        public Status state(final JobState job, final Status status);

        }
    }

