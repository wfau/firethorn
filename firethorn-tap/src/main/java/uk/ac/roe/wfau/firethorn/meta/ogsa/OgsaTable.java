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
package uk.ac.roe.wfau.firethorn.meta.ogsa;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 *
 *
 */
public interface OgsaTable<TableType extends BaseTable<TableType,  ColumnType>, ColumnType extends BaseColumn<ColumnType>>
extends BaseTable<TableType, ColumnType>
    {
    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<OgsaTable<?,?>>
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
        public OgsaTable<?,?> resolve(final String alias)
        throws NotFoundException;
         */

        }
/*
    @Override
    public String alias();

    @Override
    public StringBuilder fullname();

    @Override
    public OgsaResource<?> resource();
 */
    }
