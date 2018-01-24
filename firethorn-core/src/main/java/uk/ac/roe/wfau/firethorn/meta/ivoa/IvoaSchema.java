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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;

/**
 * Public interface for an external IVOA schema.
 *
 */
public interface IvoaSchema
extends BaseSchema<IvoaSchema, IvoaTable>
    {
    /**
     * {@link EntityBuilder} interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<IvoaSchema, IvoaSchema.Metadata>
        {
        /**
         * Create or update an {@link IvoaSchema}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         */
        public IvoaSchema build(final IvoaSchema.Metadata param)
        throws ProtectionException, DuplicateEntityException;
        }

    /**
     * {@link BaseSchema.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<IvoaSchema>
        {
        }

    /**
     * {@link BaseSchema.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseSchema.LinkFactory<IvoaSchema>
        {
        }

    /**
     * {@link BaseSchema.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<IvoaSchema>
        {
        }
    
    /**
     * {@link BaseSchema.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseSchema.EntityFactory<IvoaResource, IvoaSchema>
        {
        /**
         * Create a new {@link IvoaSchema}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaSchema create(final IvoaResource parent, final IvoaSchema.Metadata param)
        throws ProtectionException;
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<IvoaSchema>
        {
        /**
         * Our {@link IvoaSchema.EntityFactory} instance.
         *
         */
        public IvoaSchema.EntityFactory entities();

        /**
         * Our {@link IvoaTable.EntityFactory} instance.
         *
         */
        public IvoaTable.EntityFactory tables();

        }
    
    @Override
    public IvoaResource resource()
    throws ProtectionException;

    /**
     * The schema {@link IvoaTable tables}.
     *
     */
    public interface Tables extends BaseSchema.Tables<IvoaTable>
        {
        /**
         * Create a {@link IvoaTable.Builder}.
         * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
         *
         */
        public IvoaTable.Builder builder()  
        throws ProtectionException;

        }

    @Override
    public Tables tables()
    throws ProtectionException;

    /**
     * The schema metadata.
     * TODO Does this need to extend the AdqlSchema.Metadata?
     *
     */
    public interface Metadata
    extends AdqlSchema.Metadata
        {
        /**
         * The IVOA metadata.
         * 
         */
        public interface Ivoa
            {
            /**
             * The schema name.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             *
             */
            public String name()
            throws ProtectionException;

            /**
             * The schema title.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String title()
            throws ProtectionException;

            /**
             * The schema description.
             * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
             * 
             */
            public String text()
            throws ProtectionException;

            /**
             * The schema uType.
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
    public IvoaSchema.Metadata meta()
    throws ProtectionException;

    /**
     * Update the schema properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final IvoaSchema.Metadata meta)
    throws ProtectionException;

    /**
     * Update the schema properties.
     * @throws ProtectionException If the current {@link Identity} is not allowed to perform this action. 
     * 
     */
    public void update(final IvoaSchema.Metadata.Ivoa ivoa)
    throws ProtectionException;

    }
