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

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema.Metadata.Adql;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 *
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
         *
         */
        public IvoaSchema build(final IvoaSchema.Metadata param)
        throws DuplicateEntityException;
        }

    /**
     * {@link BaseSchema.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends BaseSchema.IdentFactory
        {
        }

    /**
     * {@link BaseSchema.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends BaseSchema.NameFactory<AdqlSchema>
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
     * {@link BaseSchema.EntityResolver} interface.
     *
     */
    public static interface EntityResolver
    extends BaseSchema.EntityResolver<IvoaResource, IvoaSchema>
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
         *
         */
        public IvoaSchema create(final IvoaResource parent, final IvoaSchema.Metadata param);
        
        /**
         * Our local {@link IvoaTable.EntityFactory} implementation.
         * @todo Move to services
         *
         */
        public IvoaTable.EntityFactory tables();
        }

    @Override
    public IvoaResource resource();

    /**
     * The schema {@link IvoaTable tables}.
     *
     */
    public interface Tables extends BaseSchema.Tables<IvoaTable>
        {
        /**
         * Create a {@link IvoaTable.Builder}.
         *
         */
        public IvoaTable.Builder builder();  
        }
    @Override
    public Tables tables();

    /**
     * The schema metadata.
     * TODO Does this really extend the AdqlSchema.Metadata?
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
            }

        /**
         * The IVOA metadata.
         * 
         */
        public Ivoa ivoa();
        }

    @Override
    public IvoaSchema.Metadata meta();

    /**
     * Update the schema properties.
     * 
     */
    public void update(final IvoaSchema.Metadata meta);

    }
