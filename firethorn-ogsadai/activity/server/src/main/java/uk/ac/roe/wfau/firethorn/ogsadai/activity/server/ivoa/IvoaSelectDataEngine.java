/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.ivoa;

/**
 * Our TAP service client.
 * Separated out to make it testable.
 * Write our own to make it fixable.
 *
 */
@Deprecated
public class IvoaSelectDataEngine
    {

    /**
     * Public constructor.
     * 
     */
    public IvoaSelectDataEngine(final String endpoint, final String query)
        {
        //
        // Check we have the async endpoint.
        if (endpoint.endsWith("/async"))
            {
            this.endpoint = endpoint;
            }
        else if (endpoint.endsWith("/"))
            {
            this.endpoint = endpoint + "async" ; 
            }
        else {
            this.endpoint = endpoint + "/async" ; 
            }
        }
    
    /**
     * Our TAP service endpoint URL.
     *  
     */
    private String endpoint ;

    /**
     * Our TAP service endpoint URL.
     *  
     */
    public String endpoint()
        {
        return this.endpoint;
        }

    /**
     * Our ADQL query.
     * 
     */
    private String query;

    /**
     * Our ADQL query.
     * 
     */
    public String query()
        {
        return this.query;
        }

    /**
     * Our TAP/UWS job identifier.
     *  
     */
    private String jobid ;

    /**
     * Our TAP/UWS job identifier.
     *  
     */
    public String jobid()
        {
        return this.jobid;
        }

    /**
     * Our TAP/UWS job URL.
     *  
     */
    public String joburl()
        {
        return this.endpoint + "/" + this.jobid;
        }

    /**
     * Start the UWS job.
     * 
     */
    public void start()
        {
        if (this.jobid == null)
            {
            
            }

        if (this.jobid != null)
            {
            
            }

        if (this.phase.active() == false)
            {
            
            }
            
        }

    /**
     * Enum to represent the UWS job status.
     * 
     */
    public enum Phase {

        PENDING(false),
        HELD(false),

        QUEUED(true),
        EXECUTING(true),
        SUSPENDED(true),

        COMPLETED(false),
        ERROR(false),
        ABORTED(false),
        ARCHIVED(false),
        UNKNOWN(false);

        /**
         * Protected constructor.
         *  
         */
        private Phase(final boolean active)
            {
            this.active = active;
            }

        /**
         * Flag to indicate the job is active.
         *  
         */
        private boolean active;

        /**
         * Flag to indicate the job is active.
         *  
         */
        public boolean active()
            {
            return this.active;
            }
    
        };

    /**
     * The current UWS job status
     * 
     */
    private Phase phase;

    /**
     * Pollthe UWS job status
     * 
     */
    public Phase poll()
        {
        if (this.jobid != null)
            {
            return Phase.UNKNOWN;
            }
        else {
            return Phase.UNKNOWN;
            }
        }
    }
