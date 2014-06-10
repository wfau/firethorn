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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;

/**
 *
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
         * Create or update a column.
         *
         */
        public IvoaColumn select(final String name, final IvoaColumn.Metadata param)
        throws DuplicateEntityException;
        }

    /**
     * {@link BaseColumn.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory
        {
        }

    /**
     * {@link BaseColumn.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends BaseColumn.NameFactory<IvoaColumn>
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
     * {@link BaseColumn.EntityResolver} interface.
     *
     */
    public static interface EntityResolver
    extends BaseColumn.EntityResolver<IvoaTable, IvoaColumn>
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
         *
         */
        public IvoaColumn create(final IvoaTable parent, final String name);

        /**
         * Create a new {@link IvoaColumn}.
         *
         */
        public IvoaColumn create(final IvoaTable parent, final String name, final IvoaColumn.Metadata param);

        //TODO - move to services
        @Override
        public IvoaColumn.IdentFactory idents();

        //TODO - move to services
        //@Override
        //public IvoaColumn.NameFactory names();

        //TODO - move to services
        @Override
        public IvoaColumn.AliasFactory aliases();
        
        //TODO - move to services
        @Override
        public IvoaColumn.LinkFactory links();

        }

    @Override
    public IvoaTable table();
    @Override
    public IvoaSchema schema();
    @Override
    public IvoaResource resource();

    /**
     * The column metadata.
     *
     */
    public interface Metadata
    extends AdqlColumn.Metadata
        {
        }

    @Override
    public IvoaColumn.Metadata meta();

    }
