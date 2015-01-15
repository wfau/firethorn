package uk.org.ogsadai.activity.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

public class DerbyBulkLoad implements Callable<Void> 
{
    
    private static final DAILogger LOG = 
        DAILogger.getLogger(DerbyBulkLoad.class);

    private final TupleListIterator mData;
    private final String mTableName;
    private JDBCConnectionProvider mResource;

    public DerbyBulkLoad(
            JDBCConnectionProvider connectionProvider,
            String tableName, 
            TupleListIterator data) 
    {
        mTableName = tableName;
        mData = data;
        mResource = connectionProvider;
    }

    @Override
    public Void call() 
        throws ActivityProcessingException, 
               ActivityTerminatedException, 
               ActivityUserException
    {
        TupleMetadata metadata = 
            (TupleMetadata) mData.getMetadataWrapper().getMetadata();
        Connection connection = null;
        try 
        {
            connection = mResource.getConnection();
            String createTable = 
                DerbyUtilities.getCreateTableStatement(mTableName, metadata);
            LOG.debug("Creating table with statement : " + createTable);
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTable);
            LOG.debug("Successfully created table " + mTableName);
        }
        catch (JDBCConnectionUseException e) 
        {
            // something is wrong with the database connection
            throw new ActivityUserException(e);
        } 
        catch (SQLException e) 
        {
            // something went wrong when creating a statement or 
            // the update to create the table failed
            LOG.debug("Failed to create table " + mTableName);
            dropTable(connection);
            release(connection);
            throw new ActivityUserException(e);
        }
        catch (Throwable e)
        {
            // some other uncaught error
            LOG.debug("Failed to create table " + mTableName);
            dropTable(connection);
            release(connection);
            throw new ActivityProcessingException(e);
        }
        
        // bulk load data into table
        try
        {
            final String sql = 
                SQLUtilities.createInsertStatementSQL(mTableName, metadata);
            LOG.debug("Prepared statement for bulkload: " + sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            int totalCount = 0;
            Tuple tuple = null;
            LOG.debug("Inserting data into table '" + mTableName + "'.");
            while (!Thread.interrupted() && 
                    (tuple = (Tuple) mData.nextValue()) != null)
            {
                SQLUtilities.setStatementParameters(statement, tuple, metadata);
                totalCount += statement.executeUpdate();
            }
            LOG.debug("Completed bulkload into table '" + mTableName + "'. " +
            		"Total count: " + totalCount);
        }
        catch (SQLException e)
        {
            dropTable(connection);
            throw new ActivityUserException(e);
        }
        catch (Throwable e)
        {
            dropTable(connection);
            throw new ActivityProcessingException(e);
        }
        finally
        {
            release(connection);
        }
        
        return null;
    }
    
    private void release(Connection connection)
    {
        if (connection == null)
        {
            return;
        }

        try
        {
            mResource.releaseConnection(connection);
            LOG.debug("Released connection.");
        }
        catch (JDBCConnectionUseException e) 
        {
            LOG.debug("Could not release connection.");
            LOG.error(e, true);
        }
    }   
    
    private void dropTable(Connection connection)
    {
        if (connection == null)
        {
            return;
        }
        
        try
        {
            LOG.debug("Dropping table " + mTableName);
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE " + mTableName);
            LOG.debug("Dropped table " + mTableName);
        }
        catch (SQLException e)
        {
            // log the exception but don't raise it
            if (LOG.isDebugEnabled())
            {
                LOG.debug("SQLException : Failed to drop table " + mTableName);
                LOG.debug(e.getMessage());
            }
            LOG.error(e, true);
        }
    }

}
