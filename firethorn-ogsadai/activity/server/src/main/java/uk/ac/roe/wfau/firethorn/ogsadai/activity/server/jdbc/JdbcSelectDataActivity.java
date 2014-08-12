/*
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
 * ----------------------------------------------------------------------
 * Original-licence
 *
 * Copyright (c) The University of Edinburgh,  2007-2008.
 *
 * LICENCE-START
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * LICENCE-END
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcSelectDataParam;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.ServiceAddresses;
import uk.org.ogsadai.activity.extension.ServiceAddressesActivity;
import uk.org.ogsadai.activity.io.ActivityIOException;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.activity.sql.ActivitySQLUserException;
import uk.org.ogsadai.activity.sql.CallableStatement;
import uk.org.ogsadai.activity.sql.SQLQueryActivity;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.EnhancedJDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCColumnTypeMapper;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCSettings;

/**
 * Activity to execute an SQL query.
 * Based on the original {@link SQLQueryActivity} in the OGSA-DAI source code, modified to handle PipeClosedException correctly gracefully.
 *  
 */
public class JdbcSelectDataActivity 
extends MatchedIterativeActivity 
implements ResourceActivity, ServiceAddressesActivity
    {

    /**
     * Debug logger.
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(
        JdbcSelectDataActivity.class
        );

    /**
     * The resource this activity is targeted at.
     * 
     */
    private ResourceAccessor accessor;

    /**
     * Our JDBC connection provider.
     * 
     */
    private EnhancedJDBCConnectionProvider provider;
    
    /**
     * Our service addresses.
     * 
     */
    private ServiceAddresses addresses;
    
    /**
     * Our database connection.
     * 
     */
    private Connection connection;
    
    /**
     * Our SQL statement.
     * 
     */
    private Statement statement;
    
    /**
     * Type mapper to convert JDBC column types to OGSA-DAI column types.
     * 
     */
    private JDBCColumnTypeMapper mapper;
    
    /**
     *  Advanced Settings Provider Interface.
     *
     */
    private JDBCSettings settings;
    
    /**
     * Our concurrent {@link ExecutorService} used to execute the query as a background process.
     * 
     */
    private ExecutorService executor = Executors.newSingleThreadExecutor();
        
    /**
     * Public constructor.
     */
    public JdbcSelectDataActivity()
        {
        super();
        /*
         * Not sure if changes mean we no longer implement this contract.
        mContracts.add(
            new ActivityContractName(
                "uk.org.ogsadai.activity.contract.SQLQuery"
                )
            );
         */
        }

    @Override
    public Class<? extends ResourceAccessor> getTargetResourceAccessorClass()
        {
        return EnhancedJDBCConnectionProvider.class;
        }

    @Override
    public void setTargetResourceAccessor(final ResourceAccessor accessor)
        {
        this.accessor = accessor;
        provider = (EnhancedJDBCConnectionProvider) accessor;
        }

    @Override
    public void setServiceAddresses(final ServiceAddresses addresses)
        {
        this.addresses = addresses;
        }
    
    @Override
    protected ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[]
            { 
            new TypedActivityInput(
                JdbcSelectDataParam.DATABASE_QUERY,
                String.class
                )
            };
        }

    @Override
    protected void preprocess()
    throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException
        {
        validateOutput(
            JdbcSelectDataParam.ACTIVITY_RESULTS
            );
        try {
            this.connection = this.provider.getConnection();
            }
        catch (JDBCConnectionUseException ouch)
            {
            throw new ActivitySQLException(
                ouch
                );
            }
        this.settings = this.provider.getJDBCSettings();
        this.mapper   = this.settings.getJDBCColumnTypeMapper();
        }
    
    @Override
    protected void processIteration(final Object[] iterationData)
    throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException
        {
        final String expression = (String) iterationData[0];

        logger.debug("SQL query [" + expression + "]");

        try {
            logger.debug("Initialising query");
            connection.setAutoCommit(
                settings.getAutoCommit()
                );
            statement = connection.createStatement(
                settings.getResultSetType(),
                settings.getResultSetConcurrency()
                );
            statement.setFetchSize(
                settings.getFetchSize()
                );

            logger.debug("Executing query");
            final ResultSet results = execute(
                expression
                );

            logger.debug("Processing tuples");
            try {
                SQLUtilities.createTupleList(
                    results, 
                    this.getOutput(), 
                    this.mapper,
                    this.accessor.getResource().getResourceID(),
                    this.addresses.getDataRequestExecutionService(),
                    this.settings.getResourceMetaDataHandler(),
                    true,
                    true
                    );
                }
            catch (PipeClosedException ouch)
                {
                logger.debug("PipeClosedException in createTupleList()");
                iterativeStageComplete();
                }
            
            logger.debug("Processing done");

            logger.debug("Closing result set and statement");
            long before = System.currentTimeMillis();

            try {
                statement.cancel();
                }
            catch (Exception ouch)
                {
                logger.warn("Exception trying to cancel JDBC statement [{}]", ouch.getMessage());
                }
            try {
                statement.close();
                }
            catch (Exception ouch)
                {
                logger.debug("Exception trying to close JDBC statement [{}]", ouch.getMessage());
                }
            long after = System.currentTimeMillis();
            logger.debug("Time taken [{}]", (after - before));
            }

        catch (SQLException ouch)
            {
            throw new ActivitySQLUserException(
                ouch
                );
            }
        catch (PipeIOException ouch)
            {
            throw new ActivityPipeProcessingException(
                ouch
                );
            }
        catch (PipeTerminatedException ouch)
            {
            throw new ActivityTerminatedException();
            }
        catch (IOException ouch)
            {
            throw new ActivityIOException(
                ouch
                );
            }
        catch (Throwable ouch)
            {
            throw new ActivityProcessingException(
                ouch
                );
            }
        }

    /**
     * Execute statement using background thread to it can be cancelled if the request is cancelled.
     * @param expression The SQL query to run.
     * @return A Resultset, or null if the query was cancelled.
     * @throws Throwable If there is a problem during execution.
     * 
     */
    private ResultSet execute(String expression)
    throws Throwable
        {
        // Create a callable statement to execute the query in the background.
        logger.debug("Creating CallableStatement");
        final Callable<ResultSet> callable = new CallableStatement(statement, expression);

        logger.debug("Submitting CallableStatement to ExecutorService");
        Future<ResultSet> future = executor.submit(
            callable
            );
        try {
            logger.debug("Initiating CallableStatement and starting background execution");
            // This will initiate the Callable object and so
            // basically execute Statement.executeQuery in the
            // background. If execution is interrupted e.g. by
            // interruption of the current thread as happens if an
            // OGSA-DAI request is terminated, then an exception
            // will be thrown.
            ResultSet results = future.get();
            logger.debug("CallableStatement returned ResultSet");
            return results ;
            }
        catch (ExecutionException ouch)
            {
            logger.debug("ExecutionException while executing CallableStatement");
            throw ouch.getCause();
            }
        catch (InterruptedException e)
            {
            logger.debug("InterruptedException while executing CallableStatement");
            cancel();
            return null ;
            }
        catch (CancellationException e)
            {
            logger.debug("CancellationException while executing CallableStatement");
            logger.debug("CallableStatement cancelled");
            cancel();
            return null ;
            }
        }

    /**
     * Cancel any running SQL statement. The statement is only cancelled if the JDBC driver supports it.
     * @throws SQLException If any problem arises.
     * 
     */
    private void cancel() throws SQLException
        {
        logger.debug("Cancelling statement...");
        try {
            logger.debug("Cancelling SQL statement execution");
            if (statement != null)
                {
                statement.cancel();
                }
            }
        catch (SQLException ouch)
            {
            // Don't care.
            logger.debug("Exception during Statement.cancel() ["+ ouch.getClass().getName() +"]["+ ouch.getMessage() +"]");
            }
        }

    @Override
    protected void postprocess()
    throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException
        {
        }
    
    @Override
    protected void cleanUp() throws Exception
        {
        super.cleanUp();
        
        executor.shutdown();
        
        if (this.statement != null)
            {
            this.statement.close();
            }

        if (this.provider != null)
            {
            this.provider.releaseConnection(
                connection
                );
            }
        }
    }
