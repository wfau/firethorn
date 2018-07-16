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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base)
        throws ProtectionException;

        /**
         * Create a new {@link AdqlTable}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final BaseTable<?, ?> base)
        throws ProtectionException;

        /**
         * Create a new {@link AdqlTable}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlTable create(final AdqlSchema schema, final BaseTable<?, ?> base, final String name)
        throws ProtectionException;

        /**
         * Create a new {@link AdqlTable}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlTable create(final CopyDepth type, final AdqlSchema schema, final BaseTable<?, ?> base, final String name)
        throws ProtectionException;

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
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlColumn create(final BaseColumn<?> base)
        throws ProtectionException;

        /**
         * Create a {@link AdqlColumn}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlColumn create(final BaseColumn<?> base, final String name)
        throws ProtectionException;

        /**
         * Create a {@link AdqlColumn}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlColumn create(final BaseColumn<?> base, final AdqlColumn.Metadata meta)
        throws ProtectionException;

        /**
         * Import a {@link AdqlColumn} from our base table.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public AdqlColumn inport(final String name)
        throws ProtectionException, NameNotFoundException;

        }

    @Override
    public Columns columns()
    throws ProtectionException;

    @Override
    public BaseTable<?,?> base()
    throws ProtectionException;

    /**
     * Enum for the table status.
     *
     */
    public static enum TableStatus
        {
        CREATED(),
        PARTIAL(),
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
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String name()
            throws ProtectionException;

            /**
             * The table description.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String text()
            throws ProtectionException;
            
            /**
             * Get the table row count.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public Long rowcount()
            throws ProtectionException;

            /**
             * Get the table status.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public TableStatus status()
            throws ProtectionException;

            /**
             * Set the table status.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void status(final TableStatus value)
            throws ProtectionException;

            /**
             * Get the table uType.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String utype()
            throws ProtectionException;

            /**
             * Set the table uType.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public void utype(String utype)
            throws ProtectionException;

            }

        /**
         * The ADQL metadata.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Adql adql()
        throws ProtectionException;

        }

    @Override
    public AdqlTable.Metadata meta()
    throws ProtectionException;

    }
