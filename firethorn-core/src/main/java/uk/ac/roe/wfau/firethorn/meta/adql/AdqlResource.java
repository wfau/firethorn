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

import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;


/**
 *
 *
 */
public interface AdqlResource
extends BaseResource<AdqlSchema>
    {
    /**
     * {@link BaseResource.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends BaseResource.IdentFactory<AdqlResource>
        {
        }

    /**
     * {@link BaseResource.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends BaseResource.NameFactory<AdqlResource>
        {
        }

    /**
     * {@link BaseResource.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends BaseResource.LinkFactory<AdqlResource>
        {
        }

    /**
     * {@link BaseResource.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends BaseResource.EntityFactory<AdqlResource>
        {
        /**
         * Create a new {@link AdqlResource}.
         *
         */
        public AdqlResource create(final String name);

        /**
         * Our local {@link AdqlSchema.EntityFactory} implementation.
         * @todo - move to services
         *
         */
        public AdqlSchema.EntityFactory schemas();

        //TODO - move to services
        @Override
        public AdqlResource.IdentFactory idents();

        //TODO - move to services
        //@Override
        //public AdqlSchema.NameFactory names();

        //TODO - move to services
        @Override
        public AdqlResource.LinkFactory links();
        
        }

    /**
     * Our resource {@link AdqlSchema schema}.
     *
     */
    public interface Schemas extends BaseResource.Schemas<AdqlSchema>
        {
        /**
         * Create a new {@link AdqlSchema schema}.
         *
         */
        public AdqlSchema create(final String name);

        /**
         * Create a new {@link AdqlSchema schema}, importing a {@link BaseTable}.
         *
         */
        public AdqlSchema create(final String name, final BaseTable<?,?> base);

        /**
         * Create a new {@link AdqlSchema schema}, importing all the tables from a {@link BaseSchema}.
         *
         */
        public AdqlSchema create(final BaseSchema<?,?> base);

        /**
         * Create a new {@link AdqlSchema schema}, importing all the tables from a {@link BaseSchema}.
         *
         */
        public AdqlSchema create(final CopyDepth depth, final BaseSchema<?,?> base);

        /**
         * Create a new {@link AdqlSchema schema}, importing all the tables from a {@link BaseSchema}.
         *
         */
        public AdqlSchema create(final String name, final BaseSchema<?,?> base);

        /**
         * Create a new {@link AdqlSchema schema}, importing all the tables from a {@link BaseSchema}.
         *
         */
        public AdqlSchema create(final CopyDepth depth, final String name, final BaseSchema<?,?> base);

        /**
         * Import a {@link BaseSchema}.
         * @todo How is this different from create ?
         *
         */
        public AdqlSchema inport(final String name, final BaseSchema<?, ?> base);

        }
    @Override
    public Schemas schemas();

    /**
     * The {@link AdqlResource} metadata.
     *
     */
    public interface Metadata
    extends BaseResource.Metadata
        {
        /**
         * The ADQL metadata.
         * 
         */
        public interface Adql
            {
            }
        }

    @Override
    public AdqlResource.Metadata meta();
    
    }
