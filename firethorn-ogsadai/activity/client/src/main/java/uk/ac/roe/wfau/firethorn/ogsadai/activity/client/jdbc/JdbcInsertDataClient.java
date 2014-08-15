/**
 * Copyright (c) 2014, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcInsertDataParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Client for the JdbcInsertData Activity.
 *
 */
public class JdbcInsertDataClient
extends BaseResourceActivity
implements ResourceActivity
    {

    /**
     * Public interface for the Activity parameters.
     *
     */
    public static interface Param
        {
        /**
         * The target resource ID, as a String.
         * @return The target resource ID.
         *
         */
        public String store();
        
        /**
         * The target table name.
         * @return The target table name.
         *
         */
        public String table();

        /**
         * The first block size.
         * @return The first block size.
         *
         */
        public Integer first();

        /**
         * The main block size.
         * @return The main block size.
         *
         */
        public Integer block();
        
        }

    /**
     * Simple {@link Param} implementation.
     * 
     */
    public static class SimpleParam
    implements Param
        {
        private static final Integer DEFAULT_FIRST = new Integer(1000);
        private static final Integer DEFAULT_BLOCK = new Integer(1000);
        
        public SimpleParam(final String store, final String table)
            {
            this(
                store,
                table,
                DEFAULT_FIRST,
                DEFAULT_BLOCK
                );
            }
        
        public SimpleParam(final String store, final String table, final Integer first, final Integer block)
            {
            this.store = store;
            this.table = table;
            this.first = first;
            this.block = block;
            }

        private String store;
        @Override
        public String store()
            {
            return this.store;
            }

        private String table;
        @Override
        public String table()
            {
            return this.table;
            }
        
        private Integer first;
        @Override
        public Integer first()
            {
            return this.first;
            }
        
        private Integer block;
        @Override
        public Integer block()
            {
            return this.block;
            }
        }
    /**
     * The input tuples
     *
     */
    private final ActivityInput input;

    /**
     * The target table name.
     *
     */
    private final ActivityInput table;

    /**
     * The first block size.
     *
     */
    private final ActivityInput first;

    /**
     * The main block size.
     *
     */
    private final ActivityInput block;
    
    /**
     * The output tuples
     *
     */
    private final ActivityOutput results;

    /**
     * Public constructor.
     * @param source The input tuple source.
     * @param target The target resource.
     * @param param The activity parameters.
     * 
     */
    public JdbcInsertDataClient(final SingleActivityOutput source, final Param param)
        {
        super(
            new ActivityName(
                JdbcInsertDataParam.ACTIVITY_NAME
                )
            );
        this.setResourceID(
            new ResourceID(
                param.store()
                )
            );
        
        this.input = new SimpleActivityInput(
            JdbcInsertDataParam.JDBC_INSERT_TUPLE_INPUT
            );
        this.input.connect(
            source
            );

        this.table = new SimpleActivityInput(
            JdbcInsertDataParam.JDBC_INSERT_TABLE_NAME
            );
        this.table.add(
            new StringData(
                param.table()
                )
            );

        this.first = new SimpleActivityInput(
            JdbcInsertDataParam.JDBC_INSERT_FIRST_SIZE,
            true
            );
        if (param.first() != null)
            {
            this.first.add(
                new IntegerData(
                    param.first()
                    )
                );
            }

        this.block = new SimpleActivityInput(
            JdbcInsertDataParam.JDBC_INSERT_BLOCK_SIZE,
            true
            );
        if (param.block() != null)
            {
            this.block.add(
                new IntegerData(
                    param.block()
                    )
                );
            }

        this.results = new SimpleActivityOutput(
            JdbcInsertDataParam.ACTIVITY_RESULTS
            );
        }

    /**
     * Get the tuples output.
     * @return The tuples output
     *
     */
    public SingleActivityOutput results()
        {
        return results.getSingleActivityOutputs()[0];
        }

    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }

    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]
            {
            input,
            table,
            first,
            block
            };
        }

    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]
          {
          results
          };
        }
    }
