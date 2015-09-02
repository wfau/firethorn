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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateTableParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Client for the JdbcCreateTable Activity.
 *
 */
public class JdbcCreateTableClient
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
        public String resource();

        /**
         * The table name.
         * @return The table name.
         *
         */
        public String table();

        }
    
    /**
     * The input tuples
     *
     */
    private final ActivityInput input;

    /**
     * The table name.
     *
     */
    private final ActivityInput table;

    /**
     * The output tuples
     *
     */
    private final ActivityOutput results;

    /**
     * Public constructor.
     * @param source The input tuple source.
     * @param param The activity parameters.
     * 
     */
    public JdbcCreateTableClient(final SingleActivityOutput source, final Param param)
        {
        super(
            new ActivityName(
                JdbcCreateTableParam.ACTIVITY_NAME
                )
            );
        this.setResourceID(
            new ResourceID(
                param.resource()
                )
            );

        this.input = new SimpleActivityInput(
            JdbcCreateTableParam.JDBC_CREATE_TUPLE_INPUT
            );
        this.input.connect(
            source
            );

        this.table = new SimpleActivityInput(
            JdbcCreateTableParam.JDBC_CREATE_TABLE_NAME
            );
        if (param.table() != null)
            {
            this.table.add(
                new StringData(
                    param.table()
                    )
                );
            }

        this.results = new SimpleActivityOutput(
            JdbcCreateTableParam.ACTIVITY_RESULTS
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
            table
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
