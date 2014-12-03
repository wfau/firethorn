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

package uk.org.ogsadai.tuple.join;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.derby.DerbyTable;
import uk.org.ogsadai.activity.derby.DerbyUtilities;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.RenameMap;
import uk.org.ogsadai.dqp.lqp.SimpleRenameMap;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.IncomparableTypesException;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TypeMismatchException;

public class DerbyJoin extends Join
{
    private static final DAILogger LOG =
            DAILogger.getLogger(DerbyJoin.class);
    
    protected JDBCConnectionProvider mResource;
    protected TupleMetadata mLeft, mRight, mStoredMetadata, mStreamedMetadata;
    protected DerbyTable mTable = null;
    protected Connection mConnection;
    protected PreparedStatement mStatement;

    private ArrayList<Integer> mColumns;
    
    
    @Override
    public void configure(TupleMetadata left, TupleMetadata right)
        throws ColumnNotFoundException, TypeMismatchException,
            IncomparableTypesException, ConfigurationException 
    {
        super.configure(left, right);
        mLeft = left;
        mRight = right;
    }
    
    @Override
    public void storeTuples(TupleListIterator iterator)
        throws ActivityUserException, 
               ActivityProcessingException,
               ActivityTerminatedException
    {
        init();
        
        LOG.debug("Inserting data into table '" + mTable.getName() + "'.");
        int count = 0;
        Tuple tuple;
        while ((tuple = (Tuple) iterator.nextValue()) != null)
        {
            storeTuple(tuple);
            count++;
        }
        LOG.debug("Stored " + count + " tuples.");
        
        try
        {
            mStatement.close();
        }
        catch (SQLException e)
        {
            LOG.warn(e);
        }
        mStatement = null;
        mTable.release(mConnection);
        mConnection = null;
    }
    
    @Override
    public void storeTuples(Iterable<Tuple> iterable)
            throws ActivityUserException, ActivityProcessingException,
            ActivityTerminatedException
    {
        init();
        LOG.debug("Inserting data into table '" + mTable.getName() + "'.");
        int count = 0;
        for (Tuple tuple : iterable)
        {
            storeTuple(tuple);
            count++;
        }
        LOG.debug("Stored " + count + " tuples.");
        
        try
        {
            mStatement.close();
        }
        catch (SQLException e)
        {
            LOG.warn(e);
        }
        mStatement = null;
        mTable.release(mConnection);
        mConnection = null;
    }
    
    private void init() throws ActivityUserException, ActivityProcessingException
    {
        mStoredMetadata = mStoreLeft? mLeft : mRight;
        createTable();
        createPreparedStatement();
    }
    
    @Override
    protected void storeTuple(Tuple tuple) 
    {
        try
        {
            SQLUtilities.setStatementParameters(mStatement, tuple, mStoredMetadata);
            mStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected ResultSetIterator getCandidateMatches(Tuple tuple) 
    {
        try 
        {
            if (mConnection == null)
            {
                mConnection = mTable.connect();
                mStreamedMetadata = mStoreLeft? mRight : mLeft;
                mStatement = createCandidateStatement(mStreamedMetadata);
            }
            for (int i=0; i<mColumns.size(); i++)
            {
                int index = mColumns.get(i);
                SQLUtilities.setStatementParameter(
                        mStatement, 
                        tuple, 
                        index,
                        i+1, 
                        mStreamedMetadata.getColumnMetadata(index).getType());
            }
            final ResultSet resultSet = mStatement.executeQuery();
            return new ResultSetIterator(resultSet);
        } 
        catch (ActivityUserException e) 
        {
            close();
            throw new RuntimeException(e);
        } 
        catch (SQLException e) 
        {
            close();
            throw new RuntimeException(e);
        }
        catch (Throwable e)
        {
            close();
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void close() 
    {
        try
        {
            // releasing the connection closes any open result sets
            mTable.release(mConnection);
            mConnection = mTable.connect();
            mTable.dropTable(mConnection);
            mConnection = null;
        }
        catch (ActivityUserException e)
        {
            LOG.error(e.getCause());
        }
    }

    public void setResource(JDBCConnectionProvider connectionProvider)
    {
        mResource = connectionProvider;
    }
    
    private void createTable() 
        throws ActivityUserException, ActivityProcessingException
    {
        // generate table names 
        String tableName = "ogsadai_" + DerbyUtilities.generateTableName();
        mTable = new DerbyTable(tableName, mResource);
        mConnection = mTable.connect();
        mTable.create(mStoredMetadata, mConnection);
    }
    
    private void createPreparedStatement() throws ActivityProcessingException

    {        
        String sql = SQLUtilities.createInsertStatementSQL(
                mTable.getName(), mStoredMetadata);
        try 
        {
            LOG.debug("Prepared statement for bulkload: " + sql);
            mStatement = mConnection.prepareStatement(sql);
        }
        catch (SQLException e)
        {
            throw new ActivityProcessingException(e);
        }
    }
    
    private PreparedStatement createCandidateStatement(TupleMetadata metadata) 
            throws SQLException
    {
        List<Attribute> original = new ArrayList<Attribute>(metadata.getColumnCount());
        List<Attribute> renamed = new ArrayList<Attribute>(metadata.getColumnCount());
        for (int i=0; i<metadata.getColumnCount(); i++)
        {
            ColumnMetadata column = metadata.getColumnMetadata(i);
            original.add(new AttributeImpl(column.getName(), column.getTableName()));
            renamed.add(new AttributeImpl("?{" + i + "}"));
        }
        for (int i=0; i<mStoredMetadata.getColumnCount(); i++)
        {
            ColumnMetadata column = mStoredMetadata.getColumnMetadata(i);
            if (column.getTableName() != null && !column.getTableName().isEmpty())
            {
                original.add(new AttributeImpl(column.getName(), column.getTableName()));
                renamed.add(new AttributeImpl(column.getTableName() + "_" + column.getName()));
            }
        }
        RenameMap renameMap = new SimpleRenameMap(original, renamed);
        ExpressionUtils.renameUsedAttributes(mCondition, renameMap);
        String joinCondition = ExpressionUtils.generateSQL(mCondition);
        StringBuilder preparedCondition = new StringBuilder();
        mColumns = new ArrayList<Integer>();
        int beginIndex = joinCondition.indexOf("?{", 0) + "?{".length();
        int endIndex = -1;
        while (beginIndex >= 2)
        {
            preparedCondition.append(joinCondition.substring(endIndex+1, beginIndex-1));
            endIndex = joinCondition.indexOf("}", beginIndex);
            int columnIndex = 
                    Integer.parseInt(
                            joinCondition.substring(beginIndex, endIndex));
            mColumns.add(columnIndex);
            beginIndex = joinCondition.indexOf("?{", endIndex) + "?{".length();
        }
        preparedCondition.append(joinCondition.substring(endIndex+1, joinCondition.length()));
        String sql = "SELECT * FROM " + mTable.getName() + 
                " WHERE " + preparedCondition;
        LOG.debug("Created candidate select statement: " + sql);
        return mConnection.prepareStatement(sql);
    }
    
    static class ResultSetIterator implements Iterator<Tuple>
    {
        private boolean mReadNext = false;
        private boolean mIsClosed = false;
        private ResultSet mResultSet;
        private int mNumColumns;
        
        public ResultSetIterator(ResultSet resultSet) 
        {
            mResultSet = resultSet;
            try
            {
                mNumColumns = resultSet.getMetaData().getColumnCount();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext() 
        {
            try
            {
                if (!mReadNext && !mIsClosed)
                {
                    if (!mResultSet.next())
                    {
                        mResultSet.close();
                        mIsClosed = true;
                        mReadNext = false;
                    }
                    else
                    {
                        mReadNext = true;
                    }
                }
                return mReadNext;
            }
            catch (SQLException e) 
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Tuple next()
        {
            try
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                // Iterate through data and create tuples.
                List<Object> data = new ArrayList<Object>(mNumColumns);
                for (int i = 1; i <= mNumColumns; i++)
                {
                    Object value = mResultSet.getObject(i);
                    if (value == null)
                    {
                        value = Null.getValue();
                    }
                    data.add(value);
                }
                mReadNext = false;
                return new SimpleTuple(data);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
        public void close()
        {
            try 
            {
                mResultSet.close();
            }
            catch (SQLException e) 
            {
                LOG.debug("Error when closing result set");
                LOG.warn(e);
            }
        }
    };


}
