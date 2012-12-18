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
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.common.entity.Entity.LinkFactory;

/**
 *
 *
 */
public interface TuesdayAdqlTable
extends TuesdayBaseTable<TuesdayAdqlTable, TuesdayAdqlColumn>
    {
    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory
    extends TuesdayBaseTable.AliasFactory<TuesdayAdqlTable>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<TuesdayAdqlTable>
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
     * Table factory interface.
     *
     */
    public static interface Factory
    extends TuesdayBaseTable.Factory<TuesdayAdqlSchema, TuesdayAdqlTable>
        {
        /**
         * Create a new table.
         *
         */
        public TuesdayAdqlTable create(final TuesdayAdqlSchema parent, final TuesdayBaseTable<?, ?> base);

        /**
         * Create a new table.
         *
         */
        public TuesdayAdqlTable create(final TuesdayAdqlSchema parent, final TuesdayBaseTable<?, ?> base, final String name);

        /**
         * The table column factory.
         *
         */
        public TuesdayAdqlColumn.Factory columns();

        @Override
        public TuesdayAdqlTable.IdentFactory idents();

        @Override
        public TuesdayAdqlTable.LinkFactory links();
        
        }

    @Override
    public TuesdayAdqlResource resource();
    @Override
    public TuesdayAdqlSchema schema();
    public void schema(final TuesdayAdqlSchema schema);

    /**
     * The table columns.
     *
     */
    public interface Columns extends TuesdayBaseTable.Columns<TuesdayAdqlColumn>
        {
        /**
         * Create a new column.
         *
         */
        public TuesdayAdqlColumn create(final TuesdayBaseColumn<?> base);

        }
    @Override
    public Columns columns();

    /**
     * The table this table is based on.
     *
     */
    public TuesdayBaseTable<?,?> base();


    }
