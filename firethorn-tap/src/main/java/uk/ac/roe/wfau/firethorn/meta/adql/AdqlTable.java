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
package uk.ac.roe.wfau.firethorn.meta.adql;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable.TableType;

/**
 *
 *
 */
public interface AdqlTable
extends BaseTable<AdqlTable, AdqlColumn>
    {
    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory
    extends BaseTable.AliasFactory<AdqlTable>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<AdqlTable>
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
    extends BaseTable.Factory<AdqlSchema, AdqlTable>
        {
        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?, ?> base);

        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final AdqlSchema parent, final BaseTable<?, ?> base, final String name);

        /**
         * The table column factory.
         *
         */
        public AdqlColumn.Factory columns();

        @Override
        public AdqlTable.IdentFactory idents();

        @Override
        public AdqlTable.LinkFactory links();

        }

    @Override
    public AdqlResource resource();
    @Override
    public AdqlSchema schema();
    public void schema(final AdqlSchema schema);

    /**
     * The table columns.
     *
     */
    public interface Columns extends BaseTable.Columns<AdqlColumn>
        {
        /**
         * Create a new column.
         *
         */
        public AdqlColumn create(final BaseColumn<?> base);

        }
    @Override
    public Columns columns();

    @Override
    public BaseTable<?,?> base();

    /**
     * ADQL table metadata.
     *
     */
    public interface Info
    extends BaseTable.Info
        {
        /**
         * The ADQL table metadata.
         *
         */
        public interface AdqlMeta
            {
            }

        /**
         * The ADQL table metadata.
         *
         */
        public AdqlMeta adql();
        }

    @Override
    public AdqlTable.Info info();

    }
