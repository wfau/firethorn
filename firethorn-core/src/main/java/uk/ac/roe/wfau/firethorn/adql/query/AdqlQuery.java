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
             * Create a new Limits using the default settings. 
             * @return A new Limits object.
             *
            public Limits create();
             */

            /**
             * Create a new Limits using the values from another Limits. 
             * @return A new Limits object.
             *
            public Limits create(final Limits origin);
             */
            
            /**
             * Create a new Limits using specific settings. 
             * @return A new Limits object.
             *
            public Limits create(final Long rows, final Long cell, final Long time);
             */
            
            /**
             * The default system limits, used if no other limits are defined.
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
       
        }

    /**
     * Modifiable Query Limits.
     * @todo Fold this back into Limits
     * 
     */
    public interface ModifiableLimits
    extends Limits
        {

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
    public ModifiableLimits limits();

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
         * The user data store name.
         *
         */
        public String store();

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
         * The current environment params.
         *
         */
        public QueryParam param();

        /**
         * The current environment params, with a specific level.
         *
         */
        public QueryParam param(final AdqlQuery.Syntax.Level level);

        }

    /**
     * Our local service implementations.
     *
     */
    public static interface Services
        {
        /**
         * Our Name factory.
         *
         */
        public NameFactory names();

        /**
         * Our Link factory.
         *
         */
        public LinkFactory links();

        /**
         * Our Ident factory.
         *
         */
        public IdentFactory idents();

        /**
         * Our Query Factory.
         *
         */
        public EntityFactory factory();

        /**
         * Our Query executor.
         *
         */
        public Job.Executor executor();

        /**
         * OGSA-DAI param factory.
         *
         */
        public ParamFactory params();

        }

    /**
     * Our local service implementations.
     *
     */
    public Services services();

    /**
     * Name factory interface.
     *
     */
    public static interface NameFactory
    extends NamedEntity.NameFactory<AdqlQuery>
        {
        }

    /**
     * Link factory interface.
     *
     */
    public static interface LinkFactory
    extends Entity.LinkFactory<AdqlQuery>
        {
        }

    /**
     * Identifier factory interface.
     *
     */
    public static interface IdentFactory
    extends Entity.IdentFactory<AdqlQuery>
        {
        }

    /**
     * Factory interface.
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

        /**
         * Our OGSA-DAI param factory.
         * @todo Move this to Services interface
         *
         */
        public ParamFactory params();

        /**
         * Our query limits factory.
         * @todo Move this to Services interface
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
     * Query syntax validation status.
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
         * Get the syntax validation status.
         *
         */
        public Level level();

        /**
         * Set the syntax validation status.
         *
         */
        public void level(final Level level);

        /**
         * The validation status.
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
         * The validation status.
         *
         */
        public State state();

        /**
         * The original parser error message.
         *
         */
        public String message();

        /**
         * A user friendly version of the erro message.
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
     * Get the syntax validation status.
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
     * The primary resource used by the query.
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
         * The original SelectItem.
         *
        public abstract SelectItem item();
         */

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
        public abstract AdqlColumn.Type type();

        /**
         * The ADQL column.
         *
        public abstract AdqlColumn adql();
         */

        /**
         * The JDBC column.
         *
        public abstract JdbcColumn jdbc();
         */

        /**
         * The root column.
         *
        public abstract BaseColumn<?> root();
         */

        }

    /**
     * A list of the SELECT fields.
     * The list is only generated when an input query is parsed.
     * The list is not saved in the database.
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
