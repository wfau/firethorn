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

import java.util.Map;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InternalServerErrorException;
import uk.ac.roe.wfau.firethorn.adql.query.blue.InvalidRequestException;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseSchema;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.base.TreeComponent;


/**
 * Public interface for an abstract ADQL resource.
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
        public AdqlResource create();

        /**
         * Create a new {@link AdqlResource}.
         *
         */
        public AdqlResource create(final String name);

        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<AdqlResource>
        {
        /**
         * Our {@link AdqlResource.EntityFactory} instance.
         *
         */
        public AdqlResource.EntityFactory entities();

        /**
         * {@link BlueQuery.EntityFactory} instance.
         *
         */
        public BlueQuery.EntityFactory blues();

        /**
         * {@link AdqlSchema.EntityFactory} instance.
         *
         */
        public AdqlSchema.EntityFactory schemas();

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
        public AdqlSchema create(final TreeComponent.CopyDepth depth, final BaseSchema<?,?> base);

        /**
         * Create a new {@link AdqlSchema schema}, importing all the tables from a {@link BaseSchema}.
         *
         */
        public AdqlSchema create(final String name, final BaseSchema<?,?> base);

        /**
         * Create a new {@link AdqlSchema schema}, importing all the tables from a {@link BaseSchema}.
         *
         */
        public AdqlSchema create(final TreeComponent.CopyDepth depth, final String name, final BaseSchema<?,?> base);

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

    /**
     * The {@link BlueQuery}s associated with this {@link AdqlResource}.
     * 
     */
    public interface Blues
        {
        /**
         * Select all the {@link BlueQuery}s.
         * 
         */
        public Iterable<BlueQuery> select();

        /**
         * Create a new {@link BlueQuery} for this {@link AdqlResource}.
         * 
         * @param input    The ADQL query.
         * @param mode     The {@link AdqlQueryBase.Mode}.
         * @param syntax   The {@link AdqlQueryBase.Syntax.Level}.
         * @param limits   The {@link AdqlQueryBase.Limits}.
         * @param delays   The {@link AdqlQueryBase.Delays}.
         * @param next     The next {@link BlueTask.TaskState} to wait for. 
         * @param wait     How long to wait for the next {@link BlueTask.TaskState}.
         * @param triggers Map of {@link BlueTask.Trigger}.
         * 
         */
        public BlueQuery create(final String input, final AdqlQueryBase.Mode mode, final AdqlQueryBase.Syntax.Level syntax, final AdqlQueryBase.Limits limits, final AdqlQueryBase.Delays delays, final TaskState next, final Long wait, final Map<String, String> triggers)
        throws InvalidRequestException, InternalServerErrorException;

        /**
         * Create a new {@link BlueQuery} for this {@link AdqlResource}.
         * 
         * @param input  The ADQL query.
         * @return
         */
        public BlueQuery create(String string)
        throws InvalidRequestException, InternalServerErrorException;

        }

    /**
     * The {@link BlueQuery}s associated with this {@link AdqlResource}.
     * 
     */
    public Blues blues();
    
    }
