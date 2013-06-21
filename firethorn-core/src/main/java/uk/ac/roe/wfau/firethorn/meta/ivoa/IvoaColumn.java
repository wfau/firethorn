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
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;

/**
 *
 *
 */
public interface IvoaColumn
extends BaseColumn<IvoaColumn>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<IvoaColumn>
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
     * Alias factory interface.
     *
     */
    public static interface AliasFactory
    extends BaseColumn.AliasFactory<IvoaColumn>
        {
        }

    /**
     * Column factory interface.
     *
     */
    public static interface Factory
    extends BaseColumn.EntityFactory<IvoaTable, IvoaColumn>
        {
        /**
         * Create a new column.
         *
         */
        public IvoaColumn create(final IvoaTable parent, final String name);

        }

    @Override
    public IvoaTable table();
    @Override
    public IvoaSchema schema();
    @Override
    public IvoaResource resource();
    }
