/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.client.jdbc;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateResourceParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Client for the JdbcCreateResource Activity.
 *
 */
public class JdbcCreateResourceClient
extends BaseActivity implements Activity
    {
    /**
     * Public interface for the Activity params.
     *
     */
    public interface Param
        {
        /**
         * The database URL, as a String.
         *
         */
        public String jdbcurl();

        /**
         * The database username, as a String.
         *
         */
        public String username();

        /**
         * The database password, as a String.
         *
         */
        public String password();

        /**
         * The database driver class name, as a String.
         *
         */
        public String driver();

        /**
         * A flag to indicate if the resource should be writable.
         * (adds the InsertActivity)
         *
         */
        public boolean writable();

        }

    /**
     * The database URL, as a String.
     *
     */
    private final ActivityInput jdbcurl;

    /**
     * The database username, as a String.
     *
     */
    private final ActivityInput username;

    /**
     * The database password, as a String.
     *
     */
    private final ActivityInput password;

    /**
     * The database driver class name, as a String.
     *
     */
    private final ActivityInput driver;

    /**
     * Our result output.
     *
     */
    private final ActivityOutput results;

    /**
     * Get our results output.
     * @return Our results output.
     *
     */
    public SingleActivityOutput result()
        {
        return results.getSingleActivityOutputs()[0];
        }

    /**
     * Public constructor.
     * @param param The Activity parameters.
     * 
     */
    public JdbcCreateResourceClient(final Param param)
        {
        super(
            new ActivityName(
                JdbcCreateResourceParam.ACTIVITY_NAME
                )
            );
        this.jdbcurl = new SimpleActivityInput(
            JdbcCreateResourceParam.DATABASE_URL,
            true
            );
        if (param.jdbcurl() != null)
            {
            this.jdbcurl.add(
                new StringData(
                    param.jdbcurl()
                    )
                );
            }

        this.username = new SimpleActivityInput(
            JdbcCreateResourceParam.DATABASE_USERNAME,
            true
            );
        if (param.username() != null)
            {
            this.username.add(
                new StringData(
                    param.username()
                    )
                );
            }

        this.password = new SimpleActivityInput(
            JdbcCreateResourceParam.DATABASE_PASSWORD,
            true
            );
        if (param.password() != null)
            {
            this.password.add(
                new StringData(
                    param.password()
                    )
                );
            }
        
        this.driver = new SimpleActivityInput(
            JdbcCreateResourceParam.DATABASE_DRIVER,
            true
            );
        if (param.driver() != null)
            {
            this.driver.add(
                new StringData(
                    param.driver()
                    )
                );
            }

        this.results = new SimpleActivityOutput(
            JdbcCreateResourceParam.ACTIVITY_RESULTS
            );
        }
    
    @Override
    protected ActivityInput[] getInputs()
        {
        return new ActivityInput[]{
            jdbcurl,
            username,
            password,
            driver
            };
        }

    @Override
    protected ActivityOutput[] getOutputs()
        {
        return new ActivityOutput[]{
            results
            };
        }

    @Override
    protected void validateIOState()
    throws ActivityIOIllegalStateException
        {
        }

    public ResourceID resource()
    throws Exception
        {
        return new ResourceID(
            this.results.getDataValueIterator().nextAsString()
            );
        }
    }
