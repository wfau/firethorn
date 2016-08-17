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

import uk.ac.roe.wfau.firethorn.exception.FirethornCheckedException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.DelaysClient;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.data.LimitsClient;

/**
 *
 *
 */
public interface AdqlQueryBase
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
             * Create a new Limits object.
             * 
             */
            public Limits create(final Long rows, final Long cells, final Long time);
            
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
             * The absolute limits. These will override any of the other limits.
             * This enables us to start with the defaults set quite low, allow the user to increase the limits for a particular query, but still have an absolute upper bound.
             * 
             */
            public Limits absolute();

            /**
             * Compare a Limits with the absolute limits, using the lowest available value for each limit.
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

            /**
             * Compare a Limits with the system defaults to fill in any missing values and then apply the system absolutes.
             * @return A new Limits containing the lowest value of each limit.
             * @throws 
             * @see absolute(Limits)
             * @see defaults(Limits) 
             * 
             */
            public Limits validate(final Limits that)
            throws ValidationException;

            /**
             * Exception to indicate a validation error.  
             * 
             */
            public static class ValidationException
            extends FirethornCheckedException
                {
                /**
                 * Default serial version UID.
                 *
                 */
                private static final long serialVersionUID = 1L;

                /**
                 * Public constructor.
                 *
                 */
                public ValidationException(final Limits limits, final String message)
                    {
                    super(
                        message
                        );
                    this.limits = limits;
                    }

                private Limits limits;
                public Limits limits()
                    {
                    return this.limits;
                    }
                }
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
         * Public factory interface.
         * 
         */
        public interface Factory
            {
            /**
             * Create a new Delays object.
             * 
             */
            public Delays create(final Integer first, final Integer every, final Integer last);
            }

        /**
         * The delay before the first row.
         * @param value The delay value.
         *
         */
        public void first(final Integer value);

        /**
         * The delay between every row.
         * @param value The delay value.
         *
         */
        public void every(final Integer value);
        
        /**
         * The delay after the last row.
         * @param value The delay value.
         *
         */
        public void last(final Integer value);

        }

    /**
     * The query delays.
     * 
     */
    public Delays delays();
   

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
    }
