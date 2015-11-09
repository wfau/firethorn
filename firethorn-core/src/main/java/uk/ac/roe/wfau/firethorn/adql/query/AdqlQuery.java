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
package uk.ac.roe.wfau.firethorn.adql.query;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.NamedEntity;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;

/**
 *
 *
 */
public interface AdqlQuery
extends NamedEntity, Job
    {
    /**
     * Query timing statistics.
     * 
     */
    public interface Timings
        {
        /**
         * Timestamp (absolute) for the start of query processing.
         * 
         */
        public Long start();  

        /**
         * Timestamp (duration) for the ADQL parsing.
         * 
         */
        public Long adql();  

        /**
         * Timestamp (duration) for creating the JDBC table.
         * 
         */
        public Long jdbc();  

        /**
         * Timestamp (duration) for the OGSA-DAI processing.
         * 
         */
        public Long ogsa();  

        /**
         * Timestamp (dutation) for the whole query processing.
         * 
         */
        public Long total();  

        }

    /**
     * The query timing statistics.
     * 
     */
    public Timings timings();

    /**
     * The Query limits.
     * 
     */
    public interface Limits
    extends LimitsClient.Param
        {
        /**
         * Public factory interface.
         * 
         */
        public interface Factory
            {
            /**
             * The default system limits, used if no other limits are defined.
             * @return A new Limits based on the system defaults.
             * 
             */
            public Limits defaults();

            /**
             * Compare a Limits with the default system limits, using the defaults to fill in any missing values.
             * @return A new Limits containing a combination of the original limits and the system defaults.
             * @see Limits.combine()
             * 
             */
            public Limits defaults(final Limits that);

            /**
             * The absolute system limits. These will override any of the other limits.
             * This enables us to start with the defaults set quite low, allow the user to increase the limits for a particular query, but still have an absolute upper bound.
             * 
             */
            public Limits absolute();

            /**
             * Compare a Limits with the absolute system limits, using the lowest available value for each limit.
             * @return A new Limits containing the lowest value of each limit.
             * @see Limits.lowest()
             * 
             */
            public Limits absolute(final Limits that);

            /**
             * Compare a Limits with the system defaults to fill in any missing values and then apply the system absolutes.
             * @return A new Limits containing a combination of values from the supplied Limits, system defaults and the system absolutes.
             * @see absolute(Limits)
             * @see defaults(Limits) 
             * 
             */
            public Limits runtime(final Limits that);

            }
        
        /**
         * Compare this Limits with another and return a new Limits containing the lowest value of each limit.
         * If the value from both Limits are not null, then the lowest value is chosen.
         * If the value from one of the Limits is null and the other is not null, then the non-null value is chosen.
         * If the values from both of the Limits are null, then the result is null.
         * @param left  The Limits to compare with this Limits.
         * @return A new Limits containing a combination of the lowest values from the two Limits.
         * 
         */
        public Limits lowest(final Limits that);

        /**
         * Compare this Limits with another and return a new Limits containing a combination of values from two Limits.
         * If the value from the this Limits is not null, then this value is chosen.
         * If the value from this Limits is null, then the value from that Limits is chosen.
         * If the values from both of the Limits are null, then the result is null.
         * @param left  The Limits to compare with this Limits.
         * @return A new Limits containing a combination of the lowest values from the two Limits.
         */
        public Limits combine(final Limits that);
       
        /**
         * The row limit.
         * @param value The row limit.
         *
         */
        public void rows(final Long value);

        /**
         * The cells limit.
         * @param value The cells limit.
         *
         */
        public void cells(final Long value);

        /**
         * The time limit.
         * @param value The time limit.
         *
         */
        public void time(final Long value);
            
        }
    
    /**
     * The query limits.
     * 
     */
    public Limits limits();

    /**
     * Set the query limits using a combination of the current values and the values from another Limits object.
     * @param limits The Limits object to combine.
     * @see combine(Limits)
     * 
     */
    public void limits(final Limits limits);

    /**
     * Set the query limits.
     * @param rows  The rows value.
     * @param cells The cells value.
     * @param time  The time value.
     * 
     */
    public void limits(final Long rows, final Long cells, final Long time);
        
    /**
     * Query delay properties.
     * 
     */
    public interface Delays
    extends DelaysClient.Param
        {

        /**
         * The delay before the first row.
         * @param value The delay value.
         *
         */
        public void first(final Integer value);

        /**
         * The delay after the last row.
         * @param value The delay value.
         *
         */
        public void last(final Integer value);

        /**
         * The delay between each row.
         * @param value The delay value.
         *
         */
        public void every(final Integer value);
        
        }

    /**
     * The query delays.
     * 
     */
    public Delays delays();
    
    /**
     * Public interface for OGSA-DAI query params.
     * @todo This should become the basis for an OgsaDaiService entity ?
     *
     */
    public interface QueryParam
        {
        /**
         * The service endpoint URL.
         *
         */
        public String endpoint();

        /**
         * The DQP processor name.
         *
         */
        public String dqp();

        /**
         * The DQP processor mode.
         *
         */
        public Mode mode();

        /**
         * The ADQL parser level.
         *
         */
        public AdqlQuery.Syntax.Level level();
        
        }

    /**
     * OGSA-DAI param factory interface.
     *
     */
    public static interface ParamFactory
        {
        /**
         * Create a new set of params using the environment settings.
         *
         */
        public QueryParam create();

        /**
         * Create a new set of params, with a specific mode.
         * @param level The @{link AdqlQuery.Syntax.Level}.
         * @param mode  The @{link AdqlQuery.Mode}.
         *
         */
        public QueryParam create(final AdqlQuery.Syntax.Level level, final AdqlQuery.Mode mode);

        }

    /**
     * {@link NamedEntity.NameFactory} interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<AdqlQuery>
        {
        }

    /**
     * {@link Entity.LinkFactory} interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<AdqlQuery>
        {
        }

    /**
     * {@link Entity.IdentFactory} interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlQuery>
        {
        }

    /**
     * {@link Entity.EntityFactory} interface.
     *
     */
    public static interface EntityFactory
    extends Job.EntityFactory<AdqlQuery>
        {
        /**
         * Create a new query.
         *
         */
        public AdqlQuery create(final AdqlSchema schema, final QueryParam params, final String input)
        throws QueryProcessingException;

        /**
         * Create a new query.
         * @throws QueryProcessingException 
         *
         */
        public AdqlQuery create(final AdqlSchema schema, final QueryParam params, final String input, final String name)
        throws QueryProcessingException;

        /**
         * Select all the queries from a resource.
         *
         */
        public Iterable<AdqlQuery> select(final AdqlSchema schema);

        /**
         * Text search for queries (name starts with).
         *
         */
        public Iterable<AdqlQuery> search(final AdqlSchema schema, final String text);
        
        }

    /**
     * {@link Entity.EntityServices} interface.
     * 
     */
    public static interface EntityServices
    extends NamedEntity.EntityServices<AdqlQuery>
        {
        /**
         * Our {@link AdqlQuery.EntityFactory} instance.
         *
         */
        public AdqlQuery.EntityFactory entities();

        /**
         * Our {@link Job.Executor} instance.
         *
         */
        public Job.Executor executor();

        /**
         * Our OGSA-DAI param factory instance.
         *
         */
        public ParamFactory params();

        /**
         * Our query limits factory.
         *
         */
        public Limits.Factory limits();
        
        }
    
    /**
     * QueryTable builder interface.
     *
     */
    @Deprecated
    public static interface Builder
        {
        /**
         * Build a physical table.
         *
         */
        public JdbcTable create(final JdbcSchema store, final AdqlQuery query);

        }

    /**
     * Get the original input text.
     *
     */
    public String input();

    /**
     * Set the input text.
     *
     */
    public void input(final String input);
        
    /**
     * Get the processed input text.
     *
     */
    public String cleaned();

    /**
     * ADQL syntax status.
     *
     */
    public interface Syntax
        {
        /**
         * The validation level.
         *
         */
        public enum Level
            {
            /**
             * Enforce the ADQL specification.
             *
             */
            STRICT(),

            /**
             * Compensate for legacy SQLServer syntax.
             *
             */
            LEGACY();

            }

        /**
         * Get the syntax level.
         *
         */
        public Level level();

        /**
         * Set the syntax level.
         *
         */
        public void level(final Level level);

        /**
         * The validation state.
         *
         */
        public enum State
            {
            /**
             * The query has been parsed and is valid ADQL.
             *
             */
            VALID(),

            /**
             * A parser error in the ADQL query.
             *
             */
            PARSE_ERROR(),

            /**
             * A translation error processing the query.
             *
             */
            TRANS_ERROR(),

            /**
             * Unknown state - the query hasn't been parsed yet.
             *
             */
            UNKNOWN();
            }

        /**
         * The validation sate.
         *
         */
        public State state();

        /**
         * The ADQL parser error message.
         *
         */
        public String message();

        /**
         * A user friendly error message.
         *
         */
        public String friendly();

        /**
         * A list of syntax warnings.
         *
         */
        public Iterable<String> warnings();

        }

    /**
     * The ADQL syntax status.
     *
     */
    public Syntax syntax();

    /**
     * The OGSA-DAI query params.
     *
     */
    public QueryParam params();

    /**
     * OGSA-DAI query mode.
     *
     */
    public enum Mode
        {
        /**
         * Automatic selection.
         *
         */
        AUTO(),

        /**
         * Direct query to a single resource.
         *
         */
        DIRECT(),

        /**
         * Distributed query handled DQP.
         *
         */
        DISTRIBUTED();

        }

    /**
     * The OGSA-DAI query mode.
     *
     */
    public Mode mode();

    /**
     * The ADQL schema this query applies to.
     *
     */
    public AdqlSchema schema();

    /**
     * The processed ADQL query.
     *
     */
    public String adql();

    /**
     * The processed SQL query.
     *
     */
    public String osql();

    /**
     * A list of the AdqlColumns used by the query.
     *
     */
    public Iterable<AdqlColumn> columns();

    /**
     * A list of the AdqlTables used by the query.
     * The list is only generated in response to a POST request that updates the ADQL query.
     * The list is generated when an input query is parsed and is not saved in the database.
     * On subsequent GET requests the list will be empty.
     *
     */
    public Iterable<AdqlTable> tables();

    /**
     * A list of the resources used by the query.
     *
     */
    public Iterable<BaseResource<?>> resources();

    /**
     * Select the primary resource used by this query.
     * @todo rename to resource()
     *
     */
    public BaseResource<?> primary();

    /**
     * Metadata for a SELECT field.
     *
     */
    public interface SelectField
        {

        /**
         * The field name.
         *
         */
        public abstract String name();

        /**
         * The field size.
         *
         */
        public abstract Integer arraysize();

        /**
         * The field type.
         *
         */
        public abstract AdqlColumn.AdqlType type();

        
        }

    /**
     * A list of the SELECT fields used in this query.
     * ** The list is only generated when an input query is parsed.
     * ** The list is NOT saved in the database.
     *
     */
    public Iterable<SelectField> fields();

    /**
     * Our result tables.
     *
     */
    public interface Results
        {
        /**
         * The physical JDBC database table.
         *
         */
        public JdbcTable jdbc();

        /**
         * The physical base table.
         *
         */
        public BaseTable<?,?> base();

        /**
         * The abstract ADQL table.
         *
         */
        public AdqlTable adql();
        }

    /**
     * Our result tables.
     *
     */
    public Results results();

    }
