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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcSelectDataParam;
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
 * Client for the JdbcSelectData Activity.
 *
 */
public class JdbcSelectDataClient
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
         * The SQL select query.
         * @return The SQL select query.
         *
         */
        public String query();
        
        }
    
    /**
     * The SQL select query.
     *
     */
    private final ActivityInput query;

    /**
     * The output tuples
     *
     */
    private final ActivityOutput results;

    /**
     * Public constructor.
     * @param param The activity parameters.
     * 
     */
    public JdbcSelectDataClient(final ResourceID source, final Param param)
        {
        super(
            new ActivityName(
                JdbcSelectDataParam.ACTIVITY_NAME
                )
            );
        this.setResourceID(
            source
            );

        this.query = new SimpleActivityInput(
            JdbcSelectDataParam.DATABASE_QUERY,
            false
            );
        if (param.query() != null)
            {
            this.query.add(
                new StringData(
                    param.query()
                    )
                );
            }

        this.results = new SimpleActivityOutput(
            JdbcSelectDataParam.TUPLE_OUTPUT
            );
        }

    /**
     * Get the query results.
     * @return The query results.
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
            query
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
