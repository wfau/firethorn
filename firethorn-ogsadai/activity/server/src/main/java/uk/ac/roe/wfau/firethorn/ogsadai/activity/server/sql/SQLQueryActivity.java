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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.sql;

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

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.server.blue.CallbackHandler;
import uk.ac.roe.wfau.firethorn.ogsadai.context.RequestContext;
import uk.org.ogsadai.activity.ActivityContractName;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
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
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.EnhancedJDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCColumnTypeMapper;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCSettings;

/**
 * A replacement for the OGDA-DAI SQLQueryActivity classs.
 * Minimal changes to the processIteration() method to be able to handle a PipeClosedException correctly.
 * Unable to do this with by extending SQLQueryActivity because the class members are private.  
 *  
 */
public class SQLQueryActivity 
    extends MatchedIterativeActivity 
    implements ResourceActivity, SecureActivity, ServiceAddressesActivity
    {
    /**
     * Debug logger.
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(
        SQLQueryActivity.class
        );
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh 2002 - 2007.";
    
    /** Logger object for logging in this class. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SQLQueryActivity.class);
    
    /** Activity input name - SQL expression. */
    public static final String INPUT_SQL_EXPRESSION = "expression";
    
    /** Activity output name - produces lists of tuples. */
    public static final String OUTPUT_SQL_RESULTS = "data";
    
    /** The resource this activity is targeted at. */
    private ResourceAccessor mResource;

    /** The JDBC connection provider. */
    private EnhancedJDBCConnectionProvider mConnectionProvider;
    
    /** Service addresses. */
    private ServiceAddresses mServiceAddresses;
    
    /** The database connection. */
    private Connection mConnection;
    
    /** The statement to be used for the execution of queries. */
    private Statement mStatement;
    
    /** Mapper to convert JDBC column types to OGSA-DAI column types. */
    private JDBCColumnTypeMapper mColumnTypeMapper;
    
    /** Advanced Settings Provider Interface. */
    private JDBCSettings mSettings;
    
    /** Executor service. */
    private ExecutorService mExecutorService =
        Executors.newSingleThreadExecutor();
    
    /**
     * Our request context.
     * 
     */
    private RequestContext mContext ;

    @Override
    public void setSecurityContext(SecurityContext context)
        {
        if ((context != null) && (context instanceof RequestContext))
            {
            this.mContext = (RequestContext) context; 
            }
        }

    /**
     * Our callback handler.
     * 
     */
    private CallbackHandler callback;
    
    /**
     * Constructor.
     */
    public SQLQueryActivity()
    {
        super();
        mContracts.add(new ActivityContractName(
                "uk.org.ogsadai.activity.contract.SQLQuery"));
    }

    /**
     * {@inheritDoc}
     */
    public Class getTargetResourceAccessorClass()
    {
        return EnhancedJDBCConnectionProvider.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(final ResourceAccessor targetResource)
    {
        mResource = targetResource;
        mConnectionProvider = (EnhancedJDBCConnectionProvider) targetResource;
    }

    /**
     * {@inheritDoc}
     */
    public void setServiceAddresses(ServiceAddresses serviceAddresses)
    {
        mServiceAddresses = serviceAddresses;
    }

    
    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs()
    {
        return new ActivityInput[] 
        { 
            new TypedActivityInput(INPUT_SQL_EXPRESSION, String.class),

        };
    }

    //  Interface implementation
    /**
     * Validates that the output exists and opens a database connection.
     * 
     * @throws ActivitySQLException
     *             if there was an access error when opening the connection
     */
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        this.callback = new CallbackHandler(
            this.mContext
            );          
        validateOutput(OUTPUT_SQL_RESULTS);
        try
        {
            mConnection = mConnectionProvider.getConnection();
        }
        catch (JDBCConnectionUseException e)
        {
            throw new ActivitySQLException(e);
        }
        
        mSettings = mConnectionProvider.getJDBCSettings();
        mColumnTypeMapper = mSettings.getJDBCColumnTypeMapper();
    }
    
    /**
     * Processes an input SQL expression and executes the query. A list
     * containing the results of the query as OGSA-DAI tuples is written to the
     * output.
     * 
     * @param iterationData 
     *             data values for this iteration
     * 
     * @throws ActivitySQLUserException
     *             if a database access error occurred
     * @throws ActivityIOException
     *             if a problem occurred whilst initialising a Clob or Blob
     *             object
     */
    protected void processIteration(final Object[] iterationData)
        throws ActivityProcessingException, ActivityTerminatedException,
        ActivityUserException
        {
        final String expression = (String) iterationData[0];
        
        logger.debug("SQL QUERY: " + expression);

        try {
            logger.debug("Initialising query");
            mConnection.setAutoCommit(
                mSettings.getAutoCommit()
                );
            mStatement = mConnection.createStatement(
                mSettings.getResultSetType(),
                mSettings.getResultSetConcurrency()
                );
            mStatement.setFetchSize(
                mSettings.getFetchSize()
                );
            
            logger.debug("Executing query");
            this.mContext.monkey().sqlException(this, "dIgjKw32");

            ResultSet resultSet = executeQuery(expression);
            
            logger.debug("Processing tuples");
            try {
                SQLUtilities.createTupleList(
                    resultSet, 
                    getOutput(), 
                    mColumnTypeMapper,
                    mResource.getResource().getResourceID(),
                    mServiceAddresses.getDataRequestExecutionService(),
                    mSettings.getResourceMetaDataHandler(),
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
                mStatement.cancel();
                }
            catch (Exception ouch)
                {
                logger.warn("Exception trying to cancel JDBC statement [{}]", ouch.getMessage());
                }
            try {
                mStatement.close();
                }
            catch (Exception ouch)
                {
                logger.debug("Exception trying to close JDBC statement [{}]", ouch.getMessage());
                }
            long after = System.currentTimeMillis();
            logger.debug("Time taken [{}]", (after - before));
            }

        catch (SQLException e)
            {
	    logger.debug("Caught a SQLException, sending a callback");
	    callback.failed();
	    throw new ActivitySQLUserException(e);
            }
        catch (PipeIOException e)
            {
            logger.debug("Caught a PipeIOException, wrapping it in an ActivityPipeProcessingException");
            throw new ActivityPipeProcessingException(e);
            }
        catch (PipeTerminatedException e)
            {
            logger.debug("Caught a PipeTerminatedException, wrapping it in an ActivityTerminatedException");
            throw new ActivityTerminatedException();
            }
        catch (IOException e)
            {
            logger.debug("Caught an IOException, wrapping it in an ActivityIOException");
            throw new ActivityIOException(e);
            }
        catch (Throwable e)
            {
            logger.debug("Caught a Throwable, wrapping it in an ActivityProcessingException");
            throw new ActivityProcessingException(e);
            }
        }

    /**
     * Execute statement using background thread to it can
     * be cancelled if the request is cancelled.
     *
     * @param expression
     *     Query to run.
     * @return ResultSet
     *     Result set.
     * @throws Throwable
     *     If there is a problem during execution.
     */
    private ResultSet executeQuery(String expression)
        throws Throwable
    {
        // Create an object to use the Statement to execute the
        // query in the background - class definition is below.
        logger.debug("Creating CallableStatement for query");
        Callable<ResultSet> statementCall = 
            new ChaoticCallableStatement(mStatement, expression);
        this.mContext.monkey().sqlException(this, "rey9Po4i");

        logger.debug("Submitting CallableStatement to ExecutorService");
        Future<ResultSet> future = mExecutorService.submit(statementCall);
        this.mContext.monkey().sqlException(this, "uche2aNa");
        ResultSet resultSet = null;
        
        try
        {
        logger.debug("Initiating CallableStatement and starting background execution");
	        // This will initiate the Callable object and so
	        // basically execute Statement.executeQuery in the
	        // background. If execution is interrupted e.g. by
	        // interruption of the current thread as happens if an
	        // OGSA-DAI request is terminated, then an exception
	        // will be thrown.
	        this.mContext.monkey().sqlException(this, "jG2hdk00", "Warning: Fatal error 823 occurred at Mar 19 2019 12:57PM. Note the error and time, and contact your system administrator.");
	        resultSet = future.get();
	        logger.debug("CallableStatement returned ResultSet");
	        this.mContext.monkey().sqlException(this, "chahw2Ao");
        }
        catch (ExecutionException e)
        {
            logger.debug("CallableStatement encountered problem in query execution");
            logger.debug("Original cause [" + e.getCause().getClass().getName() + "]");
            throw e.getCause();
        }
        catch (InterruptedException e)
        {
            logger.debug("CallableStatement interrupted");
            cancelSQLStatement();
        }
        catch (CancellationException e)
        {
            logger.debug("CallableStatement cancelled");
            cancelSQLStatement();
        }
        return resultSet;
    }

    public class ChaoticCallableStatement
    extends CallableStatement
    {
        public ChaoticCallableStatement(final Statement statement, final String query)
        {
            super(statement, query);
        }
        @Override
        public ResultSet call() throws Exception
        {
            if (mContext.monkey().test(this, "Eoph9xie"))
	    {
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException ouch)
                {
                    logger.debug("Sleep interrupted");
                }
                
                 throw new SQLException(
                     "Delayed SQLException"
                 );
            }

            final ResultSet results = super.call();
            mContext.monkey().sqlException(this, "oz4Kie0M");
            return results;
        }
    }

    /**
     * Cancel any running SQL statement. The statement is only
     * cancelled if the JDBC driver currently in use supports this.
     *
     * @throws SQLException
     *     If any problem arises.
     */
    private void cancelSQLStatement() throws SQLException
    {
        logger.debug("Cancelling Statement");
        try
        {
            if (mStatement != null)
            {
                mStatement.cancel();
            }
        }
        catch (SQLException e)
        {
            // Don't care.
            logger.debug("Exception during Statement.cancel(): " + e);
        }
    }

    //  Interface implementation
    /**
     * No post-processing.
     */
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
    {
        //no post-processing
    }
    
    //  Interface implementation
    /**
     * Closes the current statement and releases the connection.
     */
    protected void cleanUp() throws Exception
    {
        super.cleanUp();
        
        logger.debug("Shutting down ExecutorService");
        mExecutorService.shutdown();
        
        if (mStatement != null)
        {
            logger.debug("Closing Statement");
            mContext.monkey().sqlException(this, "baivahP0");
            mStatement.close();
        }

        if (mResource != null)
        {
            logger.debug("Releasing Connection");
            mConnectionProvider.releaseConnection(mConnection);
        }
    }
}
