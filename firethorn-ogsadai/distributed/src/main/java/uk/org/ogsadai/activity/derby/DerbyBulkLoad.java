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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

public class DerbyBulkLoad implements Callable<Void> 
{
    
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2012";

    private static final DAILogger LOG = 
        DAILogger.getLogger(DerbyBulkLoad.class);

    /** Data to load into the table. */
    private final TupleListIterator mData;
    private DerbyTable mTable;

    /**
     * Creates a new bulk load for the given table name in the database.
     * 
     * @param connectionProvider
     *            provides the database connection
     * @param tableName
     *            name of the table that stores the data
     * @param data
     *            input data iterator
     */
    public DerbyBulkLoad(
            JDBCConnectionProvider connectionProvider,
            String tableName, 
            TupleListIterator data) 
    {
        mTable = new DerbyTable(tableName, connectionProvider);
        mData = data;
    }

    @Override
    public Void call() 
        throws ActivityProcessingException, 
               ActivityTerminatedException, 
               ActivityUserException
    {
        TupleMetadata metadata = 
            (TupleMetadata) mData.getMetadataWrapper().getMetadata();
        Connection connection = mTable.connect(); 
        mTable.create(metadata, connection);
        
        String tableName = mTable.getName();
        // bulk load data into table
        try
        {
            final String sql = 
                SQLUtilities.createInsertStatementSQL(tableName, metadata);
            LOG.debug("Prepared statement for bulkload: " + sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            int totalCount = 0;
            Tuple tuple = null;
            LOG.debug("Inserting data into table '" + tableName + "'.");
            while (!Thread.interrupted() && 
                    (tuple = (Tuple) mData.nextValue()) != null)
            {
                SQLUtilities.setStatementParameters(statement, tuple, metadata);
                totalCount += statement.executeUpdate();
            }
            LOG.debug("Completed bulkload into table '" + tableName + "'. " +
            		"Total count: " + totalCount);
        }
        catch (SQLException e)
        {
            mTable.dropTable(connection);
            throw new ActivityUserException(e);
        }
        catch (Throwable e)
        {
            mTable.dropTable(connection);
            throw new ActivityProcessingException(e);
        }
        finally
        {
            mTable.release(connection);
        }
        
        return null;
    }

}
