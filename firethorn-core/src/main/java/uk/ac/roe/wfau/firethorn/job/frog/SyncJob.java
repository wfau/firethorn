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

import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.job.Job.Status;

/**
 * Synchronous interface for a Job.
 * The methods in this interface are synchronous and blocking.
 * 
 */
public interface SyncJob
    {
    /**
     * Run the Job.
     * This method is synchronous and will block until the Job completes.
     * 
     */
    public Status run();

    /**
     * End (cancel) a running Job.
     * This method is synchronous and will block until the Job has been stopped.
     * 
     */
    public Status end();

    /**
     * Public interface for a synchronous Job controller.
     * The methods in this interface are synchronous and blocking.
     * 
     */
    public interface Controller
        {
        /**
         * Run a synchronous Job.
         * This method is synchronous and will block until the Job completes.
         *
         */
        public Status run(final SyncJob job);

        /**
         * Run a synchronous Job.
         * This method is synchronous and will block until the Job completes.
         *
         */
        public Status run(final Identifier ident);
        
        /**
         * End (cancel) a running Job.
         * This method is synchronous and will block until the Job has been stopped.
         *
         */
        public Status end(final SyncJob job);

        /**
         * End (cancel) a running Job.
         * This method is synchronous and will block until the Job has been stopped.
         *
         */
        public Status end(final Identifier ident);
        
        }
    }

