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
package uk.ac.roe.wfau.firethorn.mallard;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 * Public interface for an ADQL job.
 *
 */
public interface AdqlJob
extends Entity
    {
    /**
     * Factory interface for identifiers.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlJob>
        {
        }

    public interface Factory
    extends Entity.Factory<AdqlJob>
        {

        /**
         * Create a new AdqlJob.
         *
         */
        public AdqlJob create(final AdqlService service, final String name, final String adql);

        /**
         * Select all the Jobs for a AdqlService.
         *
         */
        public Iterable<AdqlJob> select(final AdqlService service);

        }

    /**
     * Access to the AdqlJob status.
     *
     */
    public AdqlJob.Status status();

    /**
     * AdqlJob status values.
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
     * The parent AdqlService.
     *
     */
    public AdqlService service();

    /**
     * The ADQL query.
     *
     */
    public String adql();

    // Results

    }