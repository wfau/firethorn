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

import uk.ac.roe.wfau.firethorn.entity.EntityBuilder;
import uk.ac.roe.wfau.firethorn.entity.exception.DuplicateEntityException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;

/**
 *
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
         * Create or update a table.
         *
         */
        public IvoaTable select(final String name, final IvoaTable.Metadata meta)
        throws DuplicateEntityException;
        }

    /**
     * {@link BaseTable.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends BaseTable.IdentFactory
        {
        }

    /**
     * {@link BaseTable.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends BaseTable.NameFactory<IvoaTable>
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
         *
         */
        public IvoaTable create(final IvoaSchema parent, final String name);

        /**
         * Create a new {@link IvoaTable}.
         *
         */
        public IvoaTable create(final IvoaSchema parent, final String name, final IvoaTable.Metadata param);
        
        /**
         * Our local {@link IvoaColumn} implementation.
         * @todo - move to services
         *
         */
        public IvoaColumn.EntityFactory columns();

        //TODO - move to services
        @Override
        public IvoaTable.IdentFactory idents();

        //TODO - move to services
        @Override
        public IvoaTable.NameFactory names();

        //TODO - move to services
        @Override
        public IvoaTable.AliasFactory aliases();

        //TODO - move to services
        @Override
        public IvoaTable.LinkFactory links();
        
        }

    @Override
    public IvoaResource resource();

    @Override
    public IvoaSchema schema();

    /**
     * Our table {@link IvoaColumn columns}.
     *
     */
    public interface Columns extends BaseTable.Columns<IvoaColumn>
        {
        /**
         * Create a {@link IvoaColumn}.
         * 
        public IvoaColumn create(final String name)
        throws DuplicateEntityException;
         */
        
        /**
         * Create a new {@link IvoaColumn.Builder}.
         *
         */
        public IvoaColumn.Builder builder();  

        }
    @Override
    public Columns columns();

    /**
     * The table metadata.
     * TODO Does this really extend the AdqlTable.Metadata ?
     * TODO Does the AdqlTable.TableStatus make sense for this ?
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
            }
        
        /**
         * The IVOA metadata.
         * 
         */
        public Ivoa ivoa();
        }

    @Override
    public IvoaTable.Metadata meta();

    }
