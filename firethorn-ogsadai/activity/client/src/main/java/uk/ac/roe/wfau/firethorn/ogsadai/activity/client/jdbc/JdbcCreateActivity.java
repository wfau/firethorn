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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.client.StringActivityInput;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcCreateParam;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.ActivityOutputUnreadableException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.resource.ResourceID;

/**
 * OGSA-DAI client to manage JDBC Resources 
 *
 */
public class JdbcCreateActivity
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
    private final ActivityOutput result;

    /**
     * Get our results output.
     * @return Our results output.
     *
     */
    public SingleActivityOutput result()
        {
        return result.getSingleActivityOutputs()[0];
        }

    /**
     * Public constructor.
     * @param param The Activity parameters.
     * 
     */
    public JdbcCreateActivity(final Param param)
        {
        super(
            new ActivityName(
                JdbcCreateParam.ACTIVITY_NAME
                )
            );
        this.jdbcurl = new StringActivityInput(
            JdbcCreateParam.JDBC_DATABASE_URL,
            param.jdbcurl(),
            true
            );
        this.username = new StringActivityInput(
            JdbcCreateParam.JDBC_DATABASE_USERNAME,
            param.username(),
            true
            );
        this.password = new StringActivityInput(
            JdbcCreateParam.JDBC_DATABASE_PASSWORD,
            param.password(),
            true
            );
        this.driver = new StringActivityInput(
            JdbcCreateParam.JDBC_DATABASE_DRIVER,
            param.driver(),
            true
            );
        this.result = new SimpleActivityOutput(
            JdbcCreateParam.JDBC_CREATE_RESULT,
            false
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
            result
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
            this.result.getDataValueIterator().nextAsString()
            );
        }
    }
