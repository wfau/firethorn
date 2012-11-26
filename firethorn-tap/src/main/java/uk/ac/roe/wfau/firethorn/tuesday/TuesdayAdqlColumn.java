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
public interface TuesdayAdqlColumn
extends TuesdayBaseColumn<TuesdayAdqlColumn>
    {
    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayAdqlColumn>
        {
        }

    /**
     * Column factory interface.
     *
     */
    public static interface Factory
    extends TuesdayBaseColumn.Factory<TuesdayAdqlTable, TuesdayAdqlColumn>
        {
        /**
         * Create a new column.
         *
         */
        public TuesdayAdqlColumn create(final TuesdayAdqlTable parent, final TuesdayBaseColumn<?> base, final String name);
       
        }

    @Override
    public TuesdayAdqlTable table();
    @Override
    public TuesdayAdqlSchema schema();
    @Override
    public TuesdayAdqlResource resource();

    public TuesdayBaseColumn<?> base();

    }
