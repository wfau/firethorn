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
 * Internal Job implementation interface.
 * This interface is not intended for use outside the job control system.
 * 
 */
public interface JobImpl
    {
    /**
     * Prepare the Job for execution.
     * This method is synchronous and will block until the state transition has been completed.
     * EDITING -> READY
     * 
     */
    public Status prepare();

    /**
     * Add the Job to the pending queue.
     * This method is synchronous and will block until the state transition has been completed.
     * READY -> PENDING
     * 
     */
    public Status pending();
    
    /**
     * Activate the Job.
     * This method is synchronous and will block until the state transition has been completed.
     * PENDING -> RUNNING
     * 
     */
    public Status running();
    
    /**
     * Execute the Job.
     * This method is synchronous and will block until the state transition has been completed.
     * RUNNING -> COMPLETED
     * 
     */
    public Status execute();

    /**
     * Cancel the Job.
     * This method is synchronous and will block until the state transition has been completed.
     * EDITING|READY|PENDING|RUNNING -> CANCELLED
     * 
     */
    public Status cancel();

    }

