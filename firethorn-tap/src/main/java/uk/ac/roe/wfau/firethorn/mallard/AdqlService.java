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
 * Public interface for an ADQL service.
 *
 */
public interface AdqlService
extends Entity
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlService>
        {
        }

    /**
     * A service factory.
     *
     */
    public static interface Factory
    extends Entity.Factory<AdqlService>
        {
        /**
         * Create a new AdqlService.
         *
         */
        public AdqlService create(final String name);

        /**
         * Select all the AdqlServices.
         *
         */
        public Iterable<AdqlService> select();

        /**
         * Select AdqlServices(s) by name.
         *
         */
        public Iterable<AdqlService> select(final String name);

        /**
         * Search for AdqlServices(s) by name.
         *
         */
        public Iterable<AdqlService> search(final String text);

        /**
         * Access to our AdqlJob factory.
         *
         */
        public AdqlJob.Factory jobs();
        }

    /**
     * The collection of resources used by this service.
     *
     */
    public Resources resources();
    public interface Resources
        {

        /**
         * Add an AdqlResource to this service.
         *
         */
        public void insert(final AdqlResource resource);

        /**
         * Select all the AdqlResources in this service.
         *
         */
        public Iterable<AdqlResource> select();

        /**
         * Select an AdqlResources by name.
         *
         */
        public Iterable<AdqlResource> select(String name);

        /**
         * Text search for AdqlResources.
         *
         */
        public Iterable<AdqlResource> search(String name);

        }

    /**
     * AdqlJob list for this TAP service.
     *
     */
    public Jobs jobs();
    public interface Jobs
        {
        public AdqlJob create(final String name, final String adql);
        public Iterable<AdqlJob> select();
        }

    }

