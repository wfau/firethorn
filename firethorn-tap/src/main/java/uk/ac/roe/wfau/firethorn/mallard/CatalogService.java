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
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonBase;
import uk.ac.roe.wfau.firethorn.widgeon.WidgeonView;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 * A TAP service instance.
 *
 */
public interface CatalogService
extends Entity
    {

    /**
     * A service factory (TAP factory).
     *
     */
    public static interface Factory
    extends Entity.Factory<CatalogService>
        {
        /**
         * Create a new CatalogService.
         *
         */
        public CatalogService create(String name);

        /**
         * Select all the Mallards.
         *
         */
        public Iterable<CatalogService> select();

        /**
         * Access to our Job factory.
         * 
         */
        public Job.Factory jobs();
        }

    /**
     * The collection of Widgeons (resources) used by this service.
     *
     */
    public Widgeons widgeons();
    public interface Widgeons
        {

        /**
         * Add a Widgeon to this service.
         *
         */
        public void insert(WidgeonView widgeon);

        /**
         * Select all the Widgeons used by this service.
         *
         */
        public Iterable<WidgeonView> select();

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

            /**
             * Create a new Job.
             *
             */
            public Job create(CatalogService service, String name, String adql);

            /**
             * Select all the Jobs for a CatalogService.
             *
             */
            public Iterable<Job> select(CatalogService service);

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
         * The parent CatalogService.
         *
         */
        public CatalogService service();

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

