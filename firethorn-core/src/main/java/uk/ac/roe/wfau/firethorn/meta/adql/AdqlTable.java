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

import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * Public interface for an abstract ADQL table.
 *
 */
public interface AdqlTable
extends BaseTable<AdqlTable, AdqlColumn>
    {
    /**
     * {@link BaseTable.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends BaseTable.IdentFactory<AdqlTable>
        {
        }

    /**
     * {@link BaseTable.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends BaseTable.NameFactory<AdqlTable>
        {
        }

    /**
     * {@link BaseTable.AliasFactory} interface.
     *
     */
    public static interface AliasFactory
    extends BaseTable.AliasFactory<AdqlTable>
        {
        }

    /**
     * {@link BaseTable.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseTable.LinkFactory<AdqlTable>
        {
        }

    /**
     * {@link BaseTable.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseTable.EntityFactory<AdqlSchema, AdqlTable>
        {

        /**
         * Create a new {@link AdqlTable}.
         *
         */
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base);

        /**
         * Create a new {@link AdqlTable}.
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final BaseTable<?, ?> base);

        /**
         * Create a new {@link AdqlTable}.
         *
         */
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base, final String name);

        /**
         * Create a new {@link AdqlTable}.
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final BaseTable<?, ?> base, final String name);

        /**
         * Create a new {@link AdqlTable}.
         *
         */
        @Deprecated
        public AdqlTable create(final AdqlSchema schema, final GreenQuery query);

        /**
         * Create a new {@link AdqlTable}.
         *
         */
        @Deprecated
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final GreenQuery query);
        
        /**
         * Create a new {@link AdqlTable}.
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final BaseTable<?, ?> base, final BlueQuery bluequery);

        }

    /**
     * {@link BaseTable.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends BaseTable.EntityServices<AdqlTable>
        {
        /**
         * Our {@link AdqlTable.EntityFactory} instance.
         *
         */
        public AdqlTable.EntityFactory entities();

        /**
         * {@link AdqlColumn.EntityFactory} instance.
         *
         */
        public AdqlColumn.EntityFactory columns();
        }
    
    @Override
    public AdqlResource resource();

    @Override
    public AdqlSchema schema();

    /**
     * Our table {@link AdqlColumn columns}.
     *
     */
    public interface Columns extends BaseTable.Columns<AdqlColumn>
        {
        /**
         * Create a {@link AdqlColumn}.
         * @todo Add name change
         *
         */
        public AdqlColumn create(final BaseColumn<?> base);

        /**
         * Create a {@link AdqlColumn}.
         *
         */
        public AdqlColumn create(final BaseColumn<?> base, final String name);

        /**
         * Import a {@link AdqlColumn} from our base table.
         * @todo Add name change
         *
         */
        public AdqlColumn inport(final String name)
        throws NameNotFoundException;

        /**
         * Create a new {@link IvoaColumn.Builder}.
         *
        public IvoaColumn.Builder builder();  
         */
        
        }

    @Override
    public Columns columns();

    @Override
    public BaseTable<?,?> base();

    /**
     * Enum for the table status.
     *
     */
    public static enum TableStatus
        {
        CREATED(),
        COMPLETED(),
        TRUNCATED(),
        DELETED(),
        UNKNOWN();
        }

    /**
     * The {@link AdqlTable} metadata.
     *
     */
    public interface Metadata
    extends BaseTable.Metadata
        {
        /**
         * The ADQL metadata.
         *
         */
        public interface Adql
            {
            /**
             * The table name.
             *
             */
            public String name();

            /**
             * The table description.
             * 
             */
            public String text();
            
            /**
             * Get the table row count.
             *
             */
            public Long count();

            /**
             * Get the table status.
             *
             */
            public TableStatus status();

            /**
             * Set the table status.
             *
             */
            public void status(final TableStatus value);

            /**
             * Get the table uType.
             *
             */
            public String utype();

            /**
             * Set the table uType.
             *
             */
            public void utype(String utype);

            }

        /**
         * The ADQL metadata.
         *
         */
        public Adql adql();
        }

    @Override
    public AdqlTable.Metadata meta();

    }
