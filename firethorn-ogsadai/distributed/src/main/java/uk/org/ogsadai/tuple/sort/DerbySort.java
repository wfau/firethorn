package uk.org.ogsadai.tuple.sort;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.derby.DerbyTable;
import uk.org.ogsadai.activity.derby.DerbyUtilities;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.client.toolkit.activities.relational.SortOrder;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.dataresource.jdbc.EnhancedJDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCSettings;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

public class DerbySort 
{
    
    private static final DAILogger LOG = DAILogger.getLogger(DerbySort.class);
    
    private EnhancedJDBCConnectionProvider mResource;
    private DerbyTable mTable;
    private Connection mConnection;
    private PreparedStatement mStatement;

    private SortOrder[] mSortOrders;
    private int[] mColumns;
    
    public void setIndex(int[] columns, SortOrder[] sortOrders) 
    {
        if(columns.length != sortOrders.length)
        {
            throw new RuntimeException(
                    "The size of columns array and sortOrders array must be the same.");
        }
        mColumns = columns;
        mSortOrders = sortOrders;
    }

    public void setResource(EnhancedJDBCConnectionProvider resource)
    {
        mResource = resource;
    }
    
    public void store(TupleListIterator tuples)
            throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException
    {
        TupleMetadata metadata =
                (TupleMetadata) tuples.getMetadataWrapper().getMetadata();
        createTable(metadata);
        Tuple tuple;
        while ((tuple = (Tuple)tuples.nextValue()) != null)
        {
            store(tuple, metadata);
        }
        close();
        LOG.debug("Stored input data.");
    }

    private void createTable(TupleMetadata metadata) 
            throws ActivityUserException, ActivityProcessingException 
    {
        LOG.debug("Creating table for " + metadata);
        String tableName = "ogsadai_" + DerbyUtilities.generateTableName();
        mTable = new DerbyTable(tableName, mResource);
        mConnection = mTable.connect();
        mTable.create(metadata, mConnection);
        mTable.createIndex(metadata, mColumns, mSortOrders, mConnection);
        String sql = SQLUtilities.createInsertStatementSQL(
                        mTable.getName(), metadata);
        try
        {
            LOG.debug("Preparing statement: " + sql);
            mStatement = mConnection.prepareStatement(sql);
        }
        catch (SQLException e)
        {
            throw new ActivityProcessingException(e);
        }
    }

    private void store(Tuple tuple, TupleMetadata metadata) 
            throws ActivityProcessingException 
    {
        try
        {
            SQLUtilities.setStatementParameters(mStatement, tuple, metadata);
            mStatement.execute();
        }
        catch (SQLException e)
        {
            throw new ActivityProcessingException(e);
        }
    }

    private void close() 
    {
        try
        {
            mStatement.close();
        } 
        catch (SQLException e) 
        {
            LOG.warn(e);
        }
        mTable.release(mConnection);
        mConnection = null;
    }

    public void writeSorted(BlockWriter output) 
            throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException 
    {
        ResultSet resultset = null;
        Connection connection = mTable.connect();
        try
        {
            Statement statement = connection.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ").append(mTable.getName());
            sql.append(" ORDER BY ");
            for (int i=0; i<mColumns.length; i++)
            {
                if (i>0) sql.append(", ");
                sql.append(mColumns[i]+1);
                sql.append(" ").append(mSortOrders[i]);
            }
            LOG.debug("Executing query to produce ordered result set: " + sql);
            resultset = statement.executeQuery(sql.toString());
            JDBCSettings jdbcSettings = mResource.getJDBCSettings();
            SQLUtilities.createTupleList(
                    resultset, output, jdbcSettings.getJDBCColumnTypeMapper(), 
                    null, null,
                    jdbcSettings.getResourceMetaDataHandler(),
                    true, true);
        }
        catch (SQLException e)
        {
            throw new ActivityProcessingException(e);
        } 
        catch (IOException e) 
        {
            throw new ActivityProcessingException(e);
        } catch (PipeClosedException e) 
        {
            // stop producing data
        } 
        catch (PipeIOException e) 
        {
            throw new ActivityProcessingException(e);
        }
        catch (PipeTerminatedException e) 
        {
            throw new ActivityTerminatedException();
        }
        finally
        {
            if (resultset != null)
            {
                try
                {
                    resultset.close();
                }
                catch (SQLException e)
                {
                    LOG.warn(e);
                }
            }
            // after writing all results drop the table and close connection
            mTable.release(connection);
            connection = mTable.connect();
            mTable.dropTable(connection);
            mTable.release(connection);
        }
    }
    
    

}
