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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.DelayParam;
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
import uk.org.ogsadai.common.msgs.DAILogger;
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
    private static final DAILogger log = DAILogger.getLogger(InsertActivity.class);

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
    public Class getTargetResourceAccessorClass()
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
     * Default value for the first block size.
     * 
     */
    private static final Integer DEFAULT_FIRST = new Integer(100);

    /**
     * Default value for the main block size.
     * 
     */
    private static final Integer DEFAULT_BLOCK = new Integer(1000);

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
                DEFAULT_FIRST
                ),
            new TypedOptionalActivityInput(
                InsertParam.BLOCK_SIZE,
                Integer.class,
                DEFAULT_BLOCK
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
        validateOutput(
            InsertParam.TUPLE_OUTPUT
            );
        writer = getOutput();
        try {
            log.debug("Preparing connection");
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
                    	log.debug("XX Block [" + count + "][" + total + "]");
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

        catch (final SQLException e)
            {
            rollback();
            throw new ActivitySQLUserException(e);
            }
        catch (final PipeClosedException ouch)
            {
            rollback();
            iterativeStageComplete();
            }
        catch (final PipeIOException ouch)
            {
            rollback();
            throw new ActivityPipeProcessingException(ouch);
            }
        catch (final PipeTerminatedException ouch)
            {
            rollback();
            throw new ActivityTerminatedException();
            }
        catch (final ActivityUserException ouch)
            {
            rollback();
            throw ouch;
            }
        catch (final ActivityProcessingException ouch)
            {
            rollback();
            throw ouch;
            }
        catch (final ActivityTerminatedException ouch)
            {
            rollback();
            throw ouch;
            }
        catch (final Throwable ouch)
            {
            rollback();
            throw new ActivityProcessingException(ouch);
            }
        finally
            {
            }
        }

    @Override
    protected void postprocess() throws ActivityUserException,
        ActivityProcessingException, ActivityTerminatedException
        {
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
        catch (final SQLException ouch)
            {
            log.error(ouch);
            }
        }
    }
