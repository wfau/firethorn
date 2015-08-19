/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.blue;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Mode;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.SelectField;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;

/**
 * ADQL query job.
 *
 */
public interface BlueQuery
extends BlueTask<BlueQuery>
    {
    /**
     * The type URI for this type.
     * @todo Use PURLs.
     *
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/entity/blue-query-1.0.json"
        );
    
    /**
     * Services interface.
     * 
     */
    public static interface Services
    extends BlueTask.Services<BlueQuery>
        {
        @Override
        public BlueQuery.IdentFactory idents();

        @Override
        public BlueQuery.NameFactory names();

        @Override
        public BlueQuery.LinkFactory links();

        @Override
        public BlueQuery.EntityFactory entities();

        @Override
        public BlueQuery.TaskRunner runner(); 

        }

    /**
     * {@link NamedEntity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<BlueQuery>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<BlueQuery>
        {
        /**
         * The URI path for a {@link BlueQuery} service, [{@value}].
         *
         */
        public static final String SERVICE_PATH = "/blue/query" ;

        /**
         * The URI path for a {@link BlueQuery} entity, [{@value}].
         *
         */
        public static final String ENTITY_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

        }
    
    /**
     * {@link Identifier} factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<BlueQuery>
        {
        }
    
    /**
     * EntityFactory interface.
     * 
     */
    public static interface EntityFactory
    extends BlueTask.EntityFactory<BlueQuery>
        {
        /**
         * TAP request parameters.
         * 
         */
        public interface TapRequest
            {
            /**
             * The ADQL request type.
             * 
             */
            public enum Type
                {
                SYNC,
                ASYNC;
                }

            /**
             * The ADQL request type.
             * 
             */
            public Type type();

            /**
             * The ADQL query.
             * 
             */
            public String input();
            
            /**
             * The next {@link TaskState} to move to, e.g {@value TaskState#RUNNING} to run the query.
             * 
             */
            public TaskState next();
            
            /**
             * The maximum number of rows to return.
             * 
             */
            public Long maxrows();

            /**
             * The maximum execution time.
             * 
             */
            public Long maxtime();

            /**
             * The maximum time to wait for a result.
             * 
             */
            public Long maxwait();
            
            }

        /**
         * Create a new {@link BlueQuery}.
         * http://redmine.roe.ac.uk/issues/311
         *
         */
        public BlueQuery create(final AdqlResource resource);

        /**
         * Create a new {@link BlueQuery}.
         * http://redmine.roe.ac.uk/issues/311
         *
         */
        public BlueQuery create(final AdqlResource resource, final TapRequest request);

        /**
         * Create a new {@link BlueQuery} with an ADQL string.
         *
         */
        public BlueQuery create(final AdqlResource resource, final String input);

        /**
         * Create a new {@link BlueQuery} with an ADQL string and state.
         *
         */
        public BlueQuery create(final AdqlResource resource, final String input, final TaskState next);

        /**
         * Create a new {@link BlueQuery} with an ADQL string, state and wait limit.
         *
         */
        public BlueQuery create(final AdqlResource resource, final String input, final TaskState next, long maxwait);

        /**
         * Select all the {@link BlueQuery}s for an {@link AdqlResource}.
         *
         */
        public Iterable<BlueQuery> select(final AdqlResource resource);

        }

    /**
     * {@link BlueTask.TaskRunner} interface.
     *
     */
    public static interface TaskRunner
    extends BlueTask.TaskRunner<BlueQuery>
        {
        public static interface Creator
        extends BlueTask.TaskRunner.Creator<BlueQuery>
            {}

        public static interface Updator
        extends BlueTask.TaskRunner.Updator
            {}

        }
    
    /**
     * The target {@link AdqlResource} to query.
     *
     */
    public AdqlResource resource();
    
    /**
     * Our original input query.
     *
     */
    public String input();

    /**
     * Our original input query.
     *
     */
    public void input(final String inout);

    /**
     * Our ADQL query.
     *
     */
    public String adql();

    /**
     * The OGSA-DAI SQL query.
     *
     */
    public String osql();

    /**
     * The OGSA-DAI query mode.
     *
     */
    public Mode mode();

    /**
     * Our results.
     *
     */
    public interface Results
        {
        /**
         * The physical JDBC table.
         *
         */
        public JdbcTable jdbc();

        /**
         * The abstract ADQL table.
         *
         */
        public AdqlTable adql();
        }

    /**
     * Our results.
     *
     */
    public Results results();

    /**
     * The {@link SelectField}s used by the query.
     *
     */
    public interface Fields
        {
        public Iterable<SelectField> select();
        }

    /**
     * The {@link SelectField}s used by the query.
     *
     */
    public Fields fields();

    /**
     * The {@link AdqlColumn}s used by the query.
     *
     */
    public interface Columns
        {
        /**
         * List the {@link AdqlColumn}s used by the query.
         *
         */
        public Iterable<AdqlColumn> select();

        }
    /**
     * The {@link AdqlColumn}s used by the query.
     *
     */
    public Columns columns();

    /**
     * The {@link AdqlTable}s used by this query.
     *
     */
    public interface Tables
        {
        /**
         * List the {@link AdqlTable}s used by the query.
         *
         */
        public Iterable<AdqlTable> select();
        }

    /**
     * The {@link AdqlTable}s used by this query.
     *
     */
    public Tables tables();

    /**
     * The {@link BaseResource}s used by this query.
     *
     */
    public interface Resources
        {
        /**
         * List the {@link BaseResource}s used by the query.
         *
         */
        public Iterable<BaseResource<?>> select();

        /**
         * Select the primary {@link BaseResource} used by the query.
         *
         */
        public BaseResource<?> primary();
        
        }
    /**
     * The {@link BaseResource}s used by this query.
     *
     */
    public Resources resources();

    /**
     * Event notification handle.
     *
     */
    public static interface Handle
    extends BlueTask.Handle
        {
        }
    }