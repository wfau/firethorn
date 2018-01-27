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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.identity.Identity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;

/**
 * Public interface for an external IVOA column.
 *
 */
public interface IvoaColumn
extends BaseColumn<IvoaColumn>
    {

    /**
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<IvoaColumn, IvoaColumn.Metadata>
        {
        /**
         * Create or update an {@link IvoaColumn}.
         *
         */
        public IvoaColumn build(final IvoaColumn.Metadata meta)
        throws ProtectionException, DuplicateEntityException;
        }

    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<IvoaColumn>
        {
        }

    /**
     * {@link NamedEntity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<IvoaColumn>
        {
        }
    
    /**
     * {@link BaseColumn.AliasFactory} interface.
     *
     */
    public static interface AliasFactory
    extends BaseColumn.AliasFactory<IvoaColumn>
        {
        }

    /**
     * {@link BaseColumn.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseColumn.LinkFactory<IvoaColumn>
        {
        }

    /**
     * {@link BaseColumn.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseColumn.EntityFactory<IvoaTable, IvoaColumn>
        {
        /**
         * Create a new {@link IvoaColumn}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaColumn create(final IvoaTable parent, final IvoaColumn.Metadata meta)
        throws ProtectionException;
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<IvoaColumn>
        {
        /**
         * Our {@link IvoaColumn.EntityFactory} instance.
         *
         */
        public IvoaColumn.EntityFactory entities();

        /**
         * Our {@link IvoaColumn.AliasFactory} instance.
         *
         */
        public IvoaColumn.AliasFactory aliases();
        }

    @Override
    public IvoaTable table()
    throws ProtectionException;

    @Override
    public IvoaSchema schema()
    throws ProtectionException;

    @Override
    public IvoaResource resource()
    throws ProtectionException;

    /**
     * The {@link IvoaColumn} metadata interface.
     *
     */
    public interface Metadata
    extends AdqlColumn.Metadata
        {
        /**
         * The IVOA metadata interface.
         * 
         */
        public interface Ivoa
            {
            /**
             * The column name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String name()
            throws ProtectionException;

            /**
             * The column {@link AdqlColumn.AdqlType}.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public AdqlColumn.AdqlType type()
            throws ProtectionException;

            /**
             * The column title.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String title()
            throws ProtectionException;

            /**
             * The column description.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String text()
            throws ProtectionException;

            /**
             * The column units.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String unit()
            throws ProtectionException;

            /**
             * The column arraysize.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public Integer arraysize()
            throws ProtectionException;

            /**
             * The column UCD.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String ucd()
            throws ProtectionException;

            /**
             * The column uType.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String utype()
            throws ProtectionException;

            }

        /**
         * The IVOA metadata.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         * 
         */
        public Ivoa ivoa()
        throws ProtectionException;

        }

    /**
     * The {@link IvoaColumn} modifier interface.
     *
     */
    public interface Modifier
    extends AdqlColumn.Modifier
        {
        /**
         * The IVOA modifier interface.
         *
         */
        public interface Ivoa
        extends IvoaColumn.Metadata.Ivoa
            {
            }

        /**
         * The IVOA modifier.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public Ivoa ivoa()
        throws ProtectionException;
        
        }
    
    @Override
    public IvoaColumn.Modifier meta()
    throws ProtectionException;

    /**
     * Update the {@link IvoaColumn} properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final IvoaColumn.Metadata.Ivoa meta)
    throws ProtectionException;

    }
