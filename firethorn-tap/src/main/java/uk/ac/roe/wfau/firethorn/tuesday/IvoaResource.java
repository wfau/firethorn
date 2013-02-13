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
public interface IvoaResource
extends OgsaResource<IvoaSchema>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<IvoaResource>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * Resource factory interface.
     *
     */
    public static interface Factory
    extends BaseResource.Factory<IvoaResource>
        {
        /**
         * The resource schema factory.
         *
         */
        public IvoaSchema.Factory schemas();

        }

    /**
     * Access to the resource schemas.
     *
     */
    public interface Schemas extends BaseResource.Schemas<IvoaSchema>
        {
        }
    @Override
    public Schemas schemas();

    public String uri();
    public void uri(final String uri);

    public String url();
    public void url(final String url);

    }
