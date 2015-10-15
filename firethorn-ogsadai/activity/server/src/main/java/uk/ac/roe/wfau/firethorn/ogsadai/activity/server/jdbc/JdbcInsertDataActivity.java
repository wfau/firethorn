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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.jdbc.JdbcInsertDataParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.server.blue.CallbackTool;
import uk.ac.roe.wfau.firethorn.ogsadai.server.blue.RequestContext;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.DataError;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.activity.sql.ActivitySQLUserException;
import uk.org.ogsadai.activity.sql.SQLBulkLoadTupleActivity;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Activity to insert data into a table.
 * Based on the original {@link SQLBulkLoadTupleActivity} in the OGSA-DAI source code, modified to use batch insert.
 * http://viralpatel.net/blogs/batch-insert-in-java-jdbc/
 *
 */
public class JdbcInsertDataActivity
extends MatchedIterativeActivity
implements ResourceActivity, SecureActivity
    {

    /**
     * Debug logger.
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(
        JdbcInsertDataActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public JdbcInsertDataActivity()
        {
        super();
        }

    /**
     * Our SQL statement.
     * 
     */
    private PreparedStatement statement;

    /**
     * Our request context.
     * 
     */
	private RequestContext context ;

	@Override
	public void setSecurityContext(SecurityContext context)
		{
        if ((context != null) && (context instanceof RequestContext))
            {
            this.context = (RequestContext) context; 
            }
		}
    
    /**
     * Our results writer.
     * 
     */
    private BlockWriter writer;

    /**
     * Our database Connection.
     *
     */
    private Connection connection;
    
    @Override
    public Class<? extends ResourceAccessor> getTargetResourceAccessorClass()
        {
        return JDBCConnectionProvider.class;
        }

    /**
     * Flag to indicate if we changed the autocommit state.
     *
     */
    private boolean autochanged = false;
    
    /**
     * Our JDBC connection provider
     * 
     */
    private JDBCConnectionProvider provider;

    @Override
    public void setTargetResourceAccessor(final ResourceAccessor accessor)
        {
        this.provider = (JDBCConnectionProvider) accessor;
        }

    @Override
    protected ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[]
            {
            new TypedActivityInput(
                JdbcInsertDataParam.JDBC_INSERT_TABLE_NAME,
                String.class
                ),
            new TypedOptionalActivityInput(
                JdbcInsertDataParam.JDBC_INSERT_FIRST_SIZE,
                Integer.class,
                JdbcInsertDataParam.JDBC_INSERT_FIRST_DEFAULT
                ),
            new TypedOptionalActivityInput(
                JdbcInsertDataParam.JDBC_INSERT_BLOCK_SIZE,
                Integer.class,
                JdbcInsertDataParam.JDBC_INSERT_BLOCK_DEFAULT
                ),
            new TupleListActivityInput(
                JdbcInsertDataParam.TUPLE_INPUT
                )
            };
        }

    @Override
    protected void preprocess()
    throws ActivityUserException,
        ActivityProcessingException,
        ActivityTerminatedException
        {
        try {
            validateOutput(
                JdbcInsertDataParam.ACTIVITY_RESULTS
                );
            this.writer = this.getOutput(
                JdbcInsertDataParam.ACTIVITY_RESULTS
                );
            }
        catch (final Exception ouch)
            {
            logger.warn("Exception validating outputs", ouch);
            throw new ActivityProcessingException(
                ouch
                );
            }
        try {
            connection = provider.getConnection();
            if (connection.getAutoCommit() == true)
                {
                autochanged = true ;
                connection.setAutoCommit(
                    false
                    );
                }
            }
        catch (final SQLException ouch)
            {
            throw new ActivitySQLException(
                ouch
                );
            }
        catch (final JDBCConnectionUseException ouch)
            {
            throw new ActivitySQLException(
                ouch
                );
            }
        }

    @Override
    protected void processIteration(final Object[] inputs)
    throws ActivityProcessingException,
        ActivityTerminatedException,
        ActivityUserException
        {
    	//
    	// Initialise our callback handler.
		final CallbackTool callback = new CallbackTool(
			context
			); 	        
    	//
    	// Initialise our table name.
        final String table = (String) inputs[0];
    	//
    	// Initialise our tuple counters.
        long block = (Integer) inputs[1];
        long large = (Integer) inputs[2];
        long slot = 100 ;
        long prev = System.currentTimeMillis();
        long next = prev;

        long count = 0 ;
        long total = 0 ;

        boolean done = false ;
        
    	//
    	// Initialise our tuple iterator.
        final TupleListIterator tuples = (TupleListIterator) inputs[3];

    	//
    	// Initialise our tuple metadata.
        final MetadataWrapper wrapper  = tuples.getMetadataWrapper();
        final TupleMetadata   metadata = (TupleMetadata) wrapper.getMetadata();
        final String insert = SQLUtilities.createInsertStatementSQL(
    		table,
    		metadata
    		);

        try {
        	statement = connection.prepareStatement(
    			insert
    			);

            try {
                for (Tuple tuple = null; ((tuple = (Tuple) tuples.nextValue()) != null) ; )
                    {

                    SQLUtilities.setStatementParameters(
                		statement,
                		tuple,
                		metadata
                		);

                    count++;
                    total++;
                    
                    /*
                     * 
                    logger.debug("Loop ----");
                    logger.debug("  total [" + total +"]");
                    logger.debug("  count [" + count +"]");
                    logger.debug("  block [" + block +"]");
                    logger.debug("  large [" + large +"]");
                     * 
                     */

                    //
                    // Add the statement to our batch.
                    statement.addBatch();

                    //
                    // If we have reached the block size.
                    if (count >= block)
                    	{
                    	//
                    	// Execute the current batch.
                    	statement.executeBatch();
                    	count = 0 ;

                    	//
                    	// Double the block size
                    	if (block < large)
                    		{
                    		block <<= 1 ;
                    		}

                    	//
                    	// Check to see when the last callback was. 
                    	next = System.currentTimeMillis();
                    	long diff = next - prev ;
                        //logger.debug("  slot  [" + slot +"]");
                        //logger.debug("  diff  [" + diff +"]");
                    	//
                    	// Callback if not within the same time slot.
                    	if (diff > slot)
    						{
    	        			callback.running(
	        					total
	        					);
    	        			prev = next ;
    						}
                    	}
                    }
                }
            finally {

            	/*
            	 * 
            	logger.debug("finally");
	            logger.debug("total [" + total +"]");
	            logger.debug("count [" + count +"]");
	            logger.debug("block [" + block +"]");
	            logger.debug("large [" + large +"]");
            	 * 
            	 */
            
	            //
                // Write the final batch.
                if (count != 0)
                    {
                    statement.executeBatch();
                    }
                //
                // Close our connection.
                connection.commit();
                statement.close();
                //
                // Pass on our total. 
                writer.write(
                    new Long(
                        total
                        )
                    );
                }
            //
            // Set the flag to say we are done.
        	done = true ;
            }
        catch (final DataError ouch)
            {
            logger.debug("DataError reading tuples [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
        catch (final SQLException ouch)
            {
            logger.warn("SQLException during processing", ouch.getMessage());
            throw new ActivitySQLUserException(
                ouch
                );
            }
        catch (final PipeClosedException ouch)
            {
            logger.warn("PipeClosedException during processing");
            }
        catch (final PipeIOException ouch)
            {
            logger.warn("PipeIOException during processing", ouch.getMessage());
            throw new ActivityPipeProcessingException(
                ouch
                );
            }
        catch (final PipeTerminatedException ouch)
            {
            logger.warn("PipeTerminatedException during processing");
            throw new ActivityTerminatedException();
            }
        catch (final ActivityUserException ouch)
            {
            logger.warn("ActivityUserException during processing", ouch.getMessage());
            throw ouch;
            }
        catch (final ActivityProcessingException ouch)
            {
            logger.warn("ActivityProcessingException during processing", ouch.getMessage());
            throw ouch;
            }
        catch (final ActivityTerminatedException ouch)
            {
            logger.warn("ActivityTerminatedException during processing", ouch.getMessage());
            throw ouch;
            }
        catch (final Throwable ouch)
            {
            logger.warn("Throwable during processing", ouch.getMessage());
            throw new ActivityProcessingException(
                ouch
                );
            }
		//
		// Callback to say we are done.
        finally
            {
            if (done)
            	{
    			callback.completed(
					total
					);
            	}
            else {
				callback.failed(
					total
					);
            	}
            iterativeStageComplete();
            }
        }

    @Override
    protected void postprocess()
    throws ActivityProcessingException
        {
        if (connection != null)
            {
            try {
                if (autochanged)
                    {
                    connection.setAutoCommit(
                        true
                        );
                    }
                }
            catch (final Throwable ouch)
                {
                logger.warn("Exception resetting autocommit [{}][{}]", ouch.getClass().getName(), ouch.getMessage());
                throw new ActivityProcessingException(
                    ouch
                    );
                }
            }
        }

    @Override
    protected void cleanUp() throws Exception
        {
        super.cleanUp();

        if (statement != null)
            {
            statement.close();
            }

        if ((connection != null) && (connection.getAutoCommit() == false))
            {
            connection.rollback();
            if (autochanged)
                {
                connection.setAutoCommit(
                    true
                    );
                }
            }

        if (provider != null)
            {
            provider.releaseConnection(
                connection
                );
            }
        }
    }
