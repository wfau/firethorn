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
public interface TuesdayIvoaTable
extends TuesdayOgsaTable<TuesdayIvoaTable, TuesdayIvoaColumn>, TuesdayBaseTable<TuesdayIvoaTable, TuesdayIvoaColumn>
    {
    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory
    extends TuesdayBaseTable.AliasFactory<TuesdayIvoaTable>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TuesdayIvoaTable>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<TuesdayIvoaTable>
        {
        }

    /**
     * Table factory interface.
     *
     */
    public static interface Factory
    extends TuesdayBaseTable.Factory<TuesdayIvoaSchema, TuesdayIvoaTable>
        {
        /**
         * Create a new table.
         *
         */
        public TuesdayIvoaTable create(final TuesdayIvoaSchema parent, final String name);

        /**
         * The table column factory.
         *
         */
        public TuesdayIvoaColumn.Factory columns();
        }

    @Override
    public TuesdayIvoaResource resource();
    @Override
    public TuesdayIvoaSchema schema();

    /**
     * The table columns.
     *
     */
    public interface Columns extends TuesdayBaseTable.Columns<TuesdayIvoaColumn>
        {
        }
    @Override
    public Columns columns();

    }
