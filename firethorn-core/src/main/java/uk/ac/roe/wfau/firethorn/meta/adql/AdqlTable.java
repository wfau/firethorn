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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 *
 *
 */
public interface AdqlTable
extends BaseTable<AdqlTable, AdqlColumn>
    {
    /**
     * Name factory interface.
     *
     */
    public static interface NameFactory
    extends BaseTable.NameFactory<AdqlTable>
        {
        }

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
     * Entity factory interface.
     *
     */
    public static interface Factory
    extends BaseTable.Factory<AdqlSchema, AdqlTable>
        {

        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base);

        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final BaseTable<?, ?> base);

        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base, final String name);

        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final BaseTable<?, ?> base, final String name);

        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final AdqlSchema schema, final AdqlQuery query);

        /**
         * Create a new table.
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final AdqlQuery query);

        /**
         * The table column factory.
         *
         */
        public AdqlColumn.EntityFactory columns();

        @Override
        public AdqlTable.IdentFactory idents();

        @Override
        public AdqlTable.LinkFactory links();

        }

    @Override
    public AdqlResource resource();
    @Override
    public AdqlSchema schema();

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

        /**
         * Import a named column from our base table.
         *
         */
        public AdqlColumn inport(final String name)
        throws NameNotFoundException;

        }

    @Override
    public Columns columns();

    @Override
    public BaseTable<?,?> base();

    /**
     * ADQL table metadata.
     * @todo make this consistent with BaseColum.Metadata
     *
     */
    public interface Metadata
    extends BaseTable.Metadata
        {
        /**
         * The ADQL table metadata.
         *
         */
        public interface AdqlMetadata
            {
            }

        /**
         * The ADQL table metadata.
         *
         */
        public AdqlMetadata adql();
        }

    @Override
    public AdqlTable.Metadata meta();

    }
