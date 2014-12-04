// Copyright (c) The University of Edinburgh, 2012.
//
// LICENCE-START
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// LICENCE-END

package uk.org.ogsadai.activity.derby;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.client.toolkit.activities.relational.SortOrder;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionUseException;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

public class DerbyTable
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    private static final DAILogger LOG = DAILogger.getLogger(DerbyTable.class);
    
    private JDBCConnectionProvider mResource;
    private String mTableName;

    /**
     * Represents a table in a database.
     * 
     * @param tableName
     *            name of the table
     * @param resource
     *            connection provider
     */
    public DerbyTable(String tableName, JDBCConnectionProvider resource)
    {
        mResource = resource;
        mTableName = tableName;
    }
    
    /**
     * Returns the table name.
     * 
     * @return table name
     */
    public String getName()
    {
        return mTableName;
    }
    
    /**
     * Connects to the database.
     * 
     * @return the connection
     * @throws ActivityUserException
     */
    public Connection connect() throws ActivityUserException 
    {
        Connection connection = null;
        try
        {
            connection = mResource.getConnection();
        }
        catch (JDBCConnectionUseException e) 
        {
            // something is wrong with the database connection
            throw new ActivityUserException(e);
        }
        return connection;
    }

    /**
     * Releases the connection. Exceptions are logged but not raised. 
     * 
     * @param connection
     *            database connection
     */
    public void release(Connection connection)
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
    
    /**
     * Creates a new table.
     * 
     * @param metadata
     *            metadata of the table to create
     * @param connection
     *            database connection
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     */
    public void create(TupleMetadata metadata, Connection connection)
            throws ActivityUserException, ActivityProcessingException 
    {
        try 
        {
            String createTable = 
                DerbyUtilities.getCreateTableStatement(mTableName, metadata);
            LOG.debug("Creating table with statement : " + createTable);
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTable);
            LOG.debug("Successfully created table " + mTableName);
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
    }
    
    /**
     * Creates an index for the specified columns.
     * 
     * @param metadata
     *            table metadata
     * @param columns
     *            columns to add to the index
     * @param sortOrders
     *            sort order of the column
     * @param connection
     *            database connection
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     */
    public void createIndex(
            TupleMetadata metadata,
            int[] columns,
            SortOrder[] sortOrders, 
            Connection connection)
            throws ActivityUserException, ActivityProcessingException 
    {
        try 
        {
            LOG.debug("Trying to create an index for columns " +
                    Arrays.toString(columns));
            boolean hasIndex = false;
            StringBuilder createIndex = new StringBuilder();
            createIndex.append("CREATE INDEX ogsadai_sort ON ");
            createIndex.append(mTableName);
            createIndex.append("(");
            for (int i=0; i<columns.length; i++)
            {   
                ColumnMetadata column = metadata.getColumnMetadata(columns[i]); 
                if (DerbyUtilities.canBeIndexed(column.getType()))
                {
                    hasIndex = true;
                    if (i>0) createIndex.append(", ");
                    createIndex.append(column.getName());
                    createIndex.append(" ");
                    createIndex.append(sortOrders[i]);
                }
            }
            createIndex.append(")");
            if (hasIndex)
            {
                Statement statement = connection.createStatement();
                statement.executeUpdate(createIndex.toString());
                LOG.debug("Successfully created index.");
            }
            else
            {
                LOG.debug("Cannot create index with unsupported column types.");
            }
        }
        catch (SQLException e) 
        {
            // failed to create an index - we ignore that for now
            LOG.error(e);
        }
        catch (Throwable e)
        {
            // failed to create an index - we ignore that for now
            LOG.error(e);
        }
    }
    
    /**
     * Drops the database table without raising exceptions. Any errors are only
     * logged.
     * 
     * @param connection
     *            database connection
     */
    public void dropTable(Connection connection)
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
