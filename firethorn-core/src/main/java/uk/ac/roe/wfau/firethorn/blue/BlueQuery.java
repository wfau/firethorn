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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.Identifier;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
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
     * EntityServices interface.
     * 
     */
    public static interface EntityServices
    extends BlueTask.EntityServices<BlueQuery>
        {
        /**
         * Our {@link BlueQuery.EntityFactory} instance.
         *
         */
        public BlueQuery.EntityFactory entities();
        
        /**
         * Our {@link AdqlQuery.Limits.Factory } instance.
         * 
         */
        public AdqlQuery.Limits.Factory limits();
        
        @Override
        public BlueQuery.LinkFactory links();

        }
    
    /**
     * {@link NamedEntity.NameFactory} interface.
     *
     */
    @Deprecated
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

        /**
         * The URI path for a {@link BlueQuery} callback, [{@value}].
         *
         */
        public static final String CALLBACK_PATH = "/callback/" + IDENT_TOKEN ;

        /**
         * Create a callback link (as a string).
         *
         */
        public String callback(final BlueQuery query);

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
         * Create a new {@link BlueQuery}.
         * http://redmine.roe.ac.uk/issues/311
         *
        public BlueQuery create(final AdqlResource resource)
        throws InvalidRequestException, InternalServerErrorException;
         */

        /**
         * Create a new {@link BlueQuery} with an ADQL string.
         *
        public BlueQuery create(final AdqlResource resource, final String input)
        throws InvalidRequestException, InternalServerErrorException;
         */

        /**
         * Create a new {@link BlueQuery} with an ADQL string and state.
         *
        public BlueQuery create(final AdqlResource resource, final String input, final TaskState next)
        throws InvalidRequestException, InternalServerErrorException;
         */

        /**
         * Create a new {@link BlueQuery} with an ADQL string, {@link BlueQuery.TaskState} and a wait timeout.
         *
         */
        public BlueQuery create(final AdqlResource source, final String input, final BlueTask.TaskState next, final Long wait)
        throws InvalidRequestException, InternalServerErrorException;

        /**
         * Create a new {@link BlueQuery} with an ADQL string, {@link AdqlQuery.Limits}, {@link BlueQuery.TaskState}, and a wait timeout.
         *
         */
        public BlueQuery create(final AdqlResource source, final String input, final AdqlQuery.Limits limits, final BlueTask.TaskState next, final Long wait)
        throws InvalidRequestException, InternalServerErrorException;

        /**
         * Update a new {@link BlueQuery} with an ADQL string and state.
         *
        public BlueQuery update(final Identifier ident, final String input, final TaskState next)
        throws InvalidStateTransitionException;
         */

        /**
         * Update a new {@link BlueQuery} with an ADQL string, prev and next {@link BlueQuery.TaskState}, and a wait timeout.
         *
         */
        public BlueQuery update(final Identifier ident, final String input, final BlueTask.TaskState prev, final BlueTask.TaskState next, Long wait)
        throws IdentifierNotFoundException, InvalidStateRequestException;

        /**
         * Update a new {@link BlueQuery} with an ADQL string, {@link AdqlQuery.Limits}, prev and next {@link BlueQuery.TaskState}, and a wait timeout.
         *
         */
        public BlueQuery update(final Identifier ident, final String input, final AdqlQuery.Limits limits, final BlueTask.TaskState prev, final BlueTask.TaskState next, Long wait)
        throws IdentifierNotFoundException, InvalidStateRequestException;

        /**
         * Select a {@link BlueQuery} with a state and wait limit.
         *
         */
        public BlueQuery select(final Identifier ident, final TaskState prev, final TaskState next, Long wait)
        throws IdentifierNotFoundException;
        
        /**
         * Select all the {@link BlueQuery}s for an {@link AdqlResource}.
         *
         */
        public Iterable<BlueQuery> select(final AdqlResource resource);

        /**
         * Handle a {@link Callback} message. 
         * 
         */
        public BlueQuery callback(final Identifier ident, final Callback message)
        throws IdentifierNotFoundException, InvalidStateRequestException;

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
        extends BlueTask.TaskRunner.Updator<BlueQuery>
            {}
        }

    /**
     * Public interface for a callback event,
     * generated by an external worker service
     * executing a task. 
     *  
     */
    public static interface Callback
        {
        /**
         * The next {@link TaskState}.
         *
         */
        public TaskState taskState();
        
        /**
         * The row count processed so far.
         *
         */
        public Long rowcount();

        /**
         * The results state.
         * @todo Need to know more information - e.g. why the results were truncated.
         * 
         */
        public ResultState resultState();
        
        }
    
    /**
     * Handle a {@link Callback} message, 
     * called by an external worker service
     * executing the {@link BlueQuery}. 
     * 
     */
    public void callback(final BlueQuery.Callback message)
    throws InvalidStateRequestException;

    /**
     * The {@link Callback} URL (as a string).
     *
     */
    public String callback();
    
    /**
     * The source {@link AdqlResource} to query.
     *
     */
    public AdqlResource source();
    
    /**
     * Get our input query.
     *
     */
    public String input();

    /**
     * Update our input query.
     * 
     */
    public void update(final String input)
    throws InvalidStateRequestException;

    /**
     * Update our input query and {@link AdqlQuery.Limits}.
     * 
     */
    public void update(final String input, final AdqlQuery.Limits limits)
    throws InvalidStateRequestException;
    
    /**
     * Our ADQL syntax status.
     *
     */
    public interface Syntax
    extends AdqlQuery.Syntax
    	{
    	}

    /**
     * Our ADQL syntax status.
     *
     */
    public Syntax syntax();
    
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
    public AdqlQuery.Mode mode();

    /**
     * The query results status.
     * 
     */
    public enum ResultState
        {
        EMPTY(),
        PARTIAL(),
        COMPLETED(),
        TRUNCATED();
        }
    
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
        
        /**
         * The number of rows returned.
         * 
         */
        public Long rowcount();
        
        /**
         * The results status.
         * 
         */
        public ResultState state();

        }

    /**
     * Our results.
     *
     */
    public Results results();

    /**
     * The {@link AdqlQuery.SelectField}s used by the query.
     *
     */
    public interface Fields
        {
        public Iterable<AdqlQuery.SelectField> select();
        }

    /**
     * The {@link AdqlQuery.SelectField}s used by the query.
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
     * The query limits.
     * 
     */
    public AdqlQuery.Limits limits();

    /**
     * Set the query limits.
     * 
     */
    public void limits(final AdqlQuery.Limits limits);

    /**
     * Set query limits to specific values.
     * @param rows  The rows value.
     * @param cells The cells value.
     * @param time  The time value.
     * 
     */
    public void limits(final Long rows, final Long cells, final Long time);
    
    /**
     * The query delays.
     * 
     */
    public AdqlQuery.Delays delays();

    /**
     * The query timing statistics.
     * 
     */
    public AdqlQuery.Timings timings();

    /**
     * Event notification handle.
     *
     */
    public static interface Handle
    extends BlueTask.Handle
        {
        }

    /**
     * Our {@link BlueQuery.Handle}.
     *
    public Handle handle();
     */

    }
