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

import java.net.URL;
import java.net.URI;

import uk.ac.roe.wfau.firethorn.widgeon.Widgeon;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 * A TAP service instance.
 *
 */
public interface Mallard
extends Entity
    {

    /**
     * A service factory (TAP factory).
     *
     */
    public static interface Factory
    extends Entity.Factory<Mallard>
        {
        public Mallard create(String name);

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
    public Widgeons widgeons();
    public interface Widgeons
        {
        public void insert(Widgeon widgeon);
        public Iterable<Widgeon> select();
        }

    /**
     * An ADQL query.
     *
     */
    public interface Job
    extends Entity
        {

        public interface Factory
        extends Entity.Factory<Job>
            {

            public Job create(Mallard mallard, String name, String adql);

            public Iterable<Job> select(Mallard mallard);

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
         * The parent Mallard.
         *
         */
        public Mallard mallard();

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
        public Job create(String name, String adql);
        public Iterable<Job> select();
        }

    }

