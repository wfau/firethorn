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
package uk.ac.roe.wfau.firethorn.tuesday;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;

/**
 *
 *
 */
public interface TuesdayJdbcResource
extends TuesdayOgsaResource<TuesdayJdbcSchema>, TuesdayBaseResource<TuesdayJdbcSchema>
    {
    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayJdbcResource>
        {
        }

    /**
     * Resource factory interface.
     *
     */
    public static interface Factory
    extends TuesdayBaseResource.Factory<TuesdayJdbcResource>
        {
        /**
         * The resource schema factory.
         *
         */
        public TuesdayJdbcSchema.Factory schemas();

        }

    /**
     * Access to the resource schemas.
     * 
     */
    public interface Schemas extends TuesdayBaseResource.Schemas<TuesdayJdbcSchema>
        {
        } 
    @Override
    public Schemas schemas();

    /**
     * Access to our JDBC resource connection.
     * 
     */
    public TuesdayJdbcConnection connection();

    }
