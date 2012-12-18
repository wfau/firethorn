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
import uk.ac.roe.wfau.firethorn.common.entity.exception.NotFoundException;

/**
 *
 *
 */
public interface TuesdayOgsaTable<TableType extends TuesdayBaseTable<TableType,  ColumnType>, ColumnType extends TuesdayBaseColumn<ColumnType>>
extends TuesdayBaseTable<TableType, ColumnType>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TuesdayOgsaTable<?,?>>
        {
        }

    /**
     * Alias resolver interface.
     *
     */
    public static interface AliasResolver
        {
        /**
         * Resolve an alias into a table.
         *
         */
        public TuesdayOgsaTable<?,?> resolve(final String alias)
        throws NotFoundException;

        }

    @Override
    public String alias();

    @Override
    public StringBuilder fullname();

    @Override
    public TuesdayOgsaResource<?> resource();

    }
