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
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.DelaysParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.InsertParam;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.sql.ActivitySQLException;
import uk.org.ogsadai.activity.sql.ActivitySQLUserException;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Copy of the OGSA-DAI SQLBulkLoadTupleActivity modified to use batch insert.
 * http://viralpatel.net/blogs/batch-insert-in-java-jdbc/
 *
 */
public class InsertActivity
extends MatchedIterativeActivity
implements ResourceActivity
    {

    /**
     * Debug logger.
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(
        InsertActivity.class
        );

    /**
     * Public constructor.
     *
     */
    public InsertActivity()
        {
        super();
        }

    /**
     * Our JDBC connection provider
     * 
     */
    private JDBCConnectionProvider provider;

    /**
     * Our database Connection.
     *
     */
    private Connection connection;

    /**
     * Our JDBC update statement.
     * 
     */
    private PreparedStatement statement;

    /**
     * Our Activity output.
     * 
     */
    private BlockWriter writer;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends ResourceAccessor> getTargetResourceAccessorClass()
        {
        return JDBCConnectionProvider.class;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTargetResourceAccessor(final ResourceAccessor resource)
        {
        provider = (JDBCConnectionProvider) resource;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActivityInput[] getIterationInputs()
        {
        return new ActivityInput[]
            {
            new TypedActivityInput(
                InsertParam.TABLE_NAME,
                String.class
                ),
            new TypedOptionalActivityInput(
                InsertParam.FIRST_SIZE,
                Integer.class,
                InsertParam.DEFAULT_FIRST
                ),
            new TypedOptionalActivityInput(
                InsertParam.BLOCK_SIZE,
                Integer.class,
                InsertParam.DEFAULT_BLOCK
                ),
            new TupleListActivityInput(
                InsertParam.TUPLE_INPUT
                )
            };
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
        {
        logger.debug("preprocess()");
        try {
            validateOutput(
                InsertParam.TUPLE_OUTPUT
                );
            writer = getOutput(
                DelaysParam.TUPLE_OUTPUT
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
            logger.debug("Preparing connection");
            connection = provider.getConnection();
            connection.setAutoCommit(
                false
                );
            }
        catch (final SQLException e)
            {
            throw new ActivitySQLException(e);
            }
        catch (final JDBCConnectionUseException e)
            {
            throw new ActivitySQLException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processIteration(final Object[] inputs)
    throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException
        {
        logger.debug("processIteration(Object[])");
        final String table = (String)  inputs[0];
        final long   first = (Integer) inputs[1];
        final long   block = (Integer) inputs[2];
        
        final TupleListIterator tuples = (TupleListIterator) inputs[3];

        final MetadataWrapper wrapper  = tuples.getMetadataWrapper();
        final TupleMetadata   metadata = (TupleMetadata) wrapper.getMetadata();
        final String insert = SQLUtilities.createInsertStatementSQL(table, metadata);

        try {
            //log.debug("Processing iter ..");
        	statement = connection.prepareStatement(insert);

            long count = 0 ;
            long total = 0 ;

            for (Tuple tuple = null; ((tuple = (Tuple) tuples.nextValue()) != null) ; )
                {
                SQLUtilities.setStatementParameters(statement, tuple, metadata);
                //
                // Write the first few rows individually.
                if (++total <= first)
                    {
                    statement.executeUpdate();
                    //log.debug("Row [" + total + "]");
                    }
                //
                // Write the rest in batches.
                else {
                    statement.addBatch();
                    if (++count >= block)
                    	{
                    	statement.executeBatch();
                    	logger.debug("XX Block [" + count + "][" + total + "]");
                        count = 0;
                    	}
                    }
                }

            //
            // Write any remaining rows.
            if (count != 0)
                {
                statement.executeBatch();
                //log.debug("Last [" + count + "][" + total + "]");
                }

            writer.write(
                new Long(
                    total
                    )
                );
            connection.commit();
            statement.close();
            }

        catch (final SQLException ouch)
            {
            //rollback();
            throw new ActivitySQLUserException(ouch);
            }
        catch (final PipeClosedException ouch)
            {
            logger.warn("PipeClosedException during processing");
            iterativeStageComplete();
            }
        catch (final PipeIOException ouch)
            {
            logger.warn("PipeIOException during processing", ouch);
            //rollback();
            throw new ActivityPipeProcessingException(ouch);
            }
        catch (final PipeTerminatedException ouch)
            {
            //rollback();
            throw new ActivityTerminatedException();
            }
        catch (final ActivityUserException ouch)
            {
            logger.warn("ActivityUserException during processing", ouch);
            //rollback();
            throw ouch;
            }
        catch (final ActivityProcessingException ouch)
            {
            logger.warn("ActivityProcessingException during processing", ouch);
            //rollback();
            throw ouch;
            }
        catch (final ActivityTerminatedException ouch)
            {
            logger.warn("ActivityTerminatedException during processing", ouch);
            //rollback();
            throw ouch;
            }
        catch (final Throwable ouch)
            {
            //rollback();
            throw new ActivityProcessingException(ouch);
            }
        finally
            {
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postprocess()
        throws ActivityUserException,
            ActivityProcessingException,
            ActivityTerminatedException
        {
        logger.debug("postprocess()");
        if (connection != null)
            {
            try {
                connection.setAutoCommit(
                    true
                    );
                }
            catch (final SQLException e)
                {
                throw new ActivitySQLUserException(e);
                }
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void cleanUp() throws Exception
        {
        logger.debug("cleanUp()");
        super.cleanUp();

        if (statement != null)
            {
            statement.close();
            }

        if ((connection != null) && (connection.getAutoCommit() == false))
            {
            connection.rollback();
            connection.setAutoCommit(
                true
                );
            }

        if (provider != null)
            {
            provider.releaseConnection(
                connection
                );
            }
        }

    private void rollback()
        {
        try {
            connection.rollback();
            connection.setAutoCommit(
                true
                );
            }
        catch (final Exception ouch)
            {
            logger.error(
                "Exception during rollback()",
                ouch
                );
            }
        }
    }
