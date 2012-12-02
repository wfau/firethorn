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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;


/**
 * Public interface for an IVOA resource.
 *
 */
public interface IvoaResource
    extends BaseResource
    {

    /**
     * Factory interface for creating and selecting resources.
     *
     */
    public static interface Factory
    extends BaseResource.Factory<IvoaResource>
        {
        /**
         * Create a new resource.
         *
         */
        public IvoaResource create(final String name);

        }

    }
