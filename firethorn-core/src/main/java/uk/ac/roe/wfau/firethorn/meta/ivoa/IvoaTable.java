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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 * Public interface for an external IVOA table.
 *
 */
public interface IvoaTable
extends BaseTable<IvoaTable, IvoaColumn>
    {
    /**
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<IvoaTable, IvoaTable.Metadata>
        {
        /**
         * Create or update an {@link IvoaTable}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaTable build(final IvoaTable.Metadata meta)
        throws ProtectionException, DuplicateEntityException;
        }

    /**
     * {@link BaseTable.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<IvoaTable>
        {
        }

    /**
     * {@link BaseTable.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<IvoaTable>
        {
        }
    
    /**
     * {@link BaseTable.AliasFactory} interface.
     *
     */
    public static interface AliasFactory
    extends BaseTable.AliasFactory<IvoaTable>
        {
        }

    /**
     * {@link BaseTable.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseTable.LinkFactory<IvoaTable>
        {
        }

    /**
     * {@link BaseTable.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseTable.EntityFactory<IvoaSchema, IvoaTable>
        {
        /**
         * Create a new {@link IvoaTable}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaTable create(final IvoaSchema parent, final IvoaTable.Metadata meta)
        throws ProtectionException;        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<IvoaTable>
        {
        /**
         * Our {@link IvoaTable.EntityFactory} instance.
         *
         */
        public IvoaTable.EntityFactory entities();

        /**
         * Our {@link IvoaTable.AliasFactory} instance.
         *
         */
        public IvoaTable.AliasFactory aliases();

        /**
         * Our {@link IvoaColumn.EntityFactory} instance.
         *
         */
        public IvoaColumn.EntityFactory columns();
        }
    
    @Override
    public IvoaResource resource()
    throws ProtectionException;        

    @Override
    public IvoaSchema schema()
    throws ProtectionException;        

    /**
     * Our table {@link IvoaColumn columns}.
     *
     */
    public interface Columns extends BaseTable.Columns<IvoaColumn>
        {
        /**
         * Create a new {@link IvoaColumn.Builder}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaColumn.Builder builder()  
        throws ProtectionException;        

        }
    @Override
    public Columns columns()
    throws ProtectionException;        

    /**
     * The table metadata.
     * TODO Does this need to extend the AdqlTable.Metadata ?
     * TODO Does AdqlTable.TableStatus make sense for this ?
     *
     */
    public interface Metadata
    extends AdqlTable.Metadata
        {
        /**
         * The IVOA metadata.
         * 
         */
        public interface Ivoa
            {
            /**
             * The table name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String name()
            throws ProtectionException;        

            /**
             * The table title.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String title()
            throws ProtectionException;        

            /**
             * The table description.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String text()
            throws ProtectionException;        

            /**
             * The table uType.
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

    @Override
    public IvoaTable.Metadata meta()
    throws ProtectionException;        

    /**
     * Update the table properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final IvoaTable.Metadata meta)
    throws ProtectionException;        

    /**
     * Update the table properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final IvoaTable.Metadata.Ivoa ivoa)
    throws ProtectionException;        
    
    }
