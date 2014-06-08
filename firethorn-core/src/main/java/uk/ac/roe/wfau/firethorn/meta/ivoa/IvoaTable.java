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
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 *
 *
 */
public interface IvoaTable
extends BaseTable<IvoaTable, IvoaColumn>
    {
    /**
     * EntityBuilder interface.
     * 
     */
    public static interface Builder
    extends EntityBuilder<IvoaTable>
        {
        /**
         * Worker interface.
         * 
         */
        public static interface Worker
        extends EntityBuilder.Worker<IvoaTable>
            {
            }
        }
    
    /**
     * Alias factory interface.
     *
     */
    public static interface AliasFactory
    extends BaseTable.AliasFactory<IvoaTable>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<IvoaTable>
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
    public static interface EntityFactory
    extends BaseTable.EntityFactory<IvoaSchema, IvoaTable>
        {
        /**
         * Create a new IvoaTable.
         *
         */
        public IvoaTable create(final IvoaSchema parent, final String name);

        /**
         * The table column factory.
         *
         */
        public IvoaColumn.EntityFactory columns();
        }

    @Override
    public IvoaResource resource();
    @Override
    public IvoaSchema schema();

    /**
     * The table columns.
     *
     */
    public interface Columns extends BaseTable.Columns<IvoaColumn>
        {
        /**
         * Create a new column.
         * 
         */
        public IvoaColumn create(final String name)
        throws DuplicateEntityException;
        
        /**
         * Create a builder.
         *
         */
        public IvoaColumn.Builder builder();  
        }
    @Override
    public Columns columns();

    }
