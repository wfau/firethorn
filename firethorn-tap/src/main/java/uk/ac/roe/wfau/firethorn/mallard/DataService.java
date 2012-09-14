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
package uk.ac.roe.wfau.firethorn.mallard ;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlResource;

/**
 * A service instance.
 *
 */
public interface DataService
extends Entity
    {

    /**
     * A service factory (TAP factory).
     *
     */
    public static interface Factory
    extends Entity.Factory<DataService>
        {
        /**
         * Create a new DataService.
         *
         */
        public DataService create(final String name);

        /**
         * Select all the Mallards.
         *
         */
        public Iterable<DataService> select();

        /**
         * Select Service(s) by name.
         *
         */
        public Iterable<DataService> select(final String name);

        /**
         * Search for Service(s) by name.
         *
         */
        public Iterable<DataService> search(final String text);

        /**
         * Access to our Job factory.
         *
         */
        public Job.Factory jobs();
        }

    /**
     * The collection of resources used by this service.
     *
     */
    public Resources resources();
    public interface Resources
        {

        /**
         * Add a DataResource to this service.
         *
         */
        public void insert(final AdqlResource resource);

        /**
         * Select all the DataResources used by this service.
         *
         */
        public Iterable<AdqlResource> select();

        }

    /**
     * An ADQL query job.
     *
     */
    public interface Job
    extends Entity
        {

        public interface Factory
        extends Entity.Factory<Job>
            {

            /**
             * Create a new Job.
             *
             */
            public Job create(final DataService service, final String name, final String adql);

            /**
             * Select all the Jobs for a DataService.
             *
             */
            public Iterable<Job> select(final DataService service);

            }

        /**
         * Access to the Job status.
         *
         */
        public Status status();

        /**
         * Job status values.
         *
         */
        public enum Status
            {
            EDITING(),
            PENDING(),
            RUNNING(),
            COMPLETED(),
            FAILED();
            };

        /**
         * The parent DataService.
         *
         */
        public DataService service();

        /**
         * The ADQL query.
         *
         */
        public String adql();

        // Results

        }

    /**
     * Job list for this TAP service.
     *
     */
    public Jobs jobs();
    public interface Jobs
        {
        public Job create(final String name, final String adql);
        public Iterable<Job> select();
        }

    }

