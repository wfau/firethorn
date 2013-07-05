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
package uk.ac.roe.wfau.firethorn.meta.ivoa;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;

/**
 *
 *
 */
public interface IvoaSchema
extends BaseSchema<IvoaSchema, IvoaTable>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<IvoaSchema>
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
     * Schema factory interface.
     *
     */
    public static interface Factory
    extends BaseSchema.Factory<IvoaResource, IvoaSchema>
        {
        /**
         * Create a new schema ...
         *
         */
        public IvoaSchema create(final IvoaResource parent, final String name);

        /**
         * The schema table factory.
         *
         */
        public IvoaTable.Factory tables();
        }

    @Override
    public IvoaResource resource();

    /**
     * Access to the schema tables.
     *
     */
    public interface Tables extends BaseSchema.Tables<IvoaTable>
        {
        }
    @Override
    public Tables tables();

    }
