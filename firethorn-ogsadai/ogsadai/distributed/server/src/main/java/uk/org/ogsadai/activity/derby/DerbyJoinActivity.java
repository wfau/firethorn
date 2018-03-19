package uk.org.ogsadai.activity.derby;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListActivityInput;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.expression.arithmetic.ExpressionException;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.jdbc.EnhancedJDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCSettings;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

public class DerbyJoinActivity 
    extends MatchedIterativeActivity 
    implements ResourceActivity
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    private static final DAILogger LOG = 
        DAILogger.getLogger(DerbyJoinActivity.class);

    public static final String INPUT_DATA1 = "data1";
    public static final String INPUT_DATA2 = "data2";
    public static final String INPUT_CONDITION = "condition";
    public static final String OUTPUT_DATA = "result";
    
    private BlockWriter mOutput;
    private EnhancedJDBCConnectionProvider mResource;

    private JDBCSettings mSettings;

    @Override
    protected ActivityInput[] getIterationInputs() 
    {
        return new ActivityInput[] {
                new TupleListActivityInput(INPUT_DATA1),
                new TupleListActivityInput(INPUT_DATA2),
                new TypedActivityInput(INPUT_CONDITION, String.class),
        };
    }

    @Override
    protected void preprocess()
        throws ActivityUserException,
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();
        mSettings = mResource.getJDBCSettings();
    }

    @Override
    protected void processIteration(Object[] iterationData)
        throws ActivityProcessingException, 
               ActivityTerminatedException,
               ActivityUserException 
    {
        TupleListIterator data1 = (TupleListIterator) iterationData[0];
        TupleListIterator data2 = (TupleListIterator) iterationData[1];
        String joinCondition = (String) iterationData[2];
        
        // generate table names 
        String id = UUID.randomUUID().toString().replaceAll("-", "_");
        String tableName1 = "ogsadai1_" + id;
        String tableName2 = "ogsadai2_" + id;
        
        // create tables and bulk load data into Derby resource
        DerbyBulkLoad bulkload1 = 
            new DerbyBulkLoad(mResource, tableName1, data1);
        DerbyBulkLoad bulkload2 = 
            new DerbyBulkLoad(mResource, tableName2, data2);

        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        CompletionService<Void> completionService = 
            new ExecutorCompletionService<Void>(threadPool);
        try 
        {
            // submit both bulkload tasks
            completionService.submit(bulkload1);
            completionService.submit(bulkload2);
            // wait for the tasks to finish (or fail)
            completionService.take().get();
            completionService.take().get();
        } 
        catch (InterruptedException e)
        {
            iterativeStageComplete();
            Thread.currentThread().interrupt();
            return;
        }
        catch (ExecutionException e)
        {
            if (e.getCause() instanceof ActivityUserException)
            {
                throw (ActivityUserException) e.getCause();
            }
            if (e.getCause() instanceof ActivityProcessingException)
            {
                throw (ActivityProcessingException) e.getCause();
            }
            if (e.getCause() instanceof ActivityTerminatedException)
            {
                throw (ActivityTerminatedException) e.getCause();
            }
            // some other uncaught exception
            throw new ActivityProcessingException(e.getCause());
        }
        finally
        {
            // if one task failed this shuts down the other one as well
            threadPool.shutdownNow();
        }
        
        TupleMetadata metadata1 = 
            (TupleMetadata)data1.getMetadataWrapper().getMetadata();
        TupleMetadata metadata2 =
            (TupleMetadata)data2.getMetadataWrapper().getMetadata();
        String query;
        try
        {
            query = DerbyUtilities.generateJoinQuery(
                    tableName1, tableName2, joinCondition, metadata1, metadata2);
        }
        catch (SQLParserException e)
        {
            // could not parse the join condition
            throw new ActivityUserException(e);
        }
        catch (ExpressionException e)
        {
            // failed to create an expression from the join condition AST
            throw new ActivityUserException(e);
        }

        // execute the join query and output result set as tuple list
        Connection connection = null;
        try 
        {
            LOG.debug("Executing query: " + query);
            connection = mResource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            LOG.debug("Received results. Writing tuple list.");
            mOutput.write(ControlBlock.LIST_BEGIN);
            TupleMetadata resultMetadata =
                new SimpleTupleMetadata(metadata1, metadata2);
            mOutput.write(new MetadataWrapper(resultMetadata));
            SQLUtilities.createTupleList(
                    resultSet, 
                    mOutput, 
                    mSettings.getJDBCColumnTypeMapper(),
                    mResource.getResource().getResourceID(),
                    null,
                    mSettings.getResourceMetaDataHandler(), false, false);
            mOutput.write(ControlBlock.LIST_END);
        }
        catch (SQLException e)
        {
            throw new ActivityUserException(e);
        }
        catch (JDBCConnectionUseException e) 
        {
            throw new ActivityUserException(e);
        } 
        catch (PipeClosedException e) 
        {
            // stop processing if the consumer doesn't accept any more data
            iterativeStageComplete();
        } 
        catch (PipeIOException e) 
        {
            throw new ActivityProcessingException(e);
        } 
        catch (PipeTerminatedException e) 
        {
            throw new ActivityTerminatedException();
        } 
        catch (IOException e) 
        {
            throw new ActivityProcessingException(e);
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    // release connection to close the result set
                    // just in case
                    mResource.releaseConnection(connection);
                    connection = mResource.getConnection();
                    dropTable(tableName1, connection);
                    dropTable(tableName2, connection);
                    mResource.releaseConnection(connection);
                }
                catch (JDBCConnectionUseException e)
                {
                    // log the exception but don't raise it
                    LOG.debug("SQLException : Failed to close connection");
                    LOG.error(e, true);
                }
            }
        }
    }

    /**
     * Drop a table without raising exceptions. Any SQL exceptions that occur
     * are being logged.
     * 
     * @param tableName
     *            name of the table to drop
     * @param connection
     *            database connection
     */
    private void dropTable(String tableName, Connection connection)
    {
        try
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE " + tableName);
            LOG.debug("Dropped table " + tableName);
        }
        catch (SQLException e)
        {
            // log the exception but don't raise it
            if (LOG.isDebugEnabled())
            {
                LOG.debug("SQLException : Failed to drop table " + tableName);
                LOG.debug(e.getMessage());
            }
            LOG.error(e, true);
        }
    }


    @Override
    protected void postprocess() throws ActivityUserException,
            ActivityProcessingException, ActivityTerminatedException 
    {
        // no post-processing
    }

    @Override
    public void setTargetResourceAccessor(ResourceAccessor targetResource) 
    {
        mResource = (EnhancedJDBCConnectionProvider) targetResource;
    }

    @Override
    public Class<? extends ResourceAccessor> getTargetResourceAccessorClass() 
    {
        return JDBCConnectionProvider.class;
    }
    


}
