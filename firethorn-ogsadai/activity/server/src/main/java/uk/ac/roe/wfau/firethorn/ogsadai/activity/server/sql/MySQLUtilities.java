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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.server.InsertActivity;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TupleListIterator;
import uk.org.ogsadai.activity.util.RelationalUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.dataresource.jdbc.DefaultMetaDataHandler;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCColumnTypeMapper;
import uk.org.ogsadai.resource.dataresource.jdbc.MetaDataHandler;
import uk.org.ogsadai.sql.SQLParameterMode;
import uk.org.ogsadai.sql.SQLParameterTupleConverter;
import uk.org.ogsadai.sql.UnknownSQLParameterModeException;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleBlob;
import uk.org.ogsadai.tuple.SimpleClob;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TupleUtilities;

/**
 * Utility methods for use by the SQL activities.
 *  
 * @author The OGSA-DAI Project Team.
 */
public class MySQLUtilities
    {
    /**
     * Debug logger.
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(
        MySQLUtilities.class
        );

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2008";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(MySQLUtilities.class);
    
    /**
     * Sets the parameters of an SQL prepared statement using the
     * values contained in a tuple.
     * 
     * @param statement
     *     Prepared statement
     * @param tuple
     *     Tuple containing the values to insert into the statement.
     * @param metadata
     *     Meta data describing the tuple column types.
     * @throws SQLException
     *     If a problem occurs interacting with the prepared statement.
     */
    public static void setStatementParameters(
        final PreparedStatement statement, 
        final Tuple tuple,
        final TupleMetadata metadata) 
        throws SQLException 
    {
        for (int i = 0; i < metadata.getColumnCount(); i++) 
        {
            final int jdbcColumn = i + 1;
            final ColumnMetadata coldata = metadata.getColumnMetadata(i);
            setStatementParameter(statement, tuple, i, jdbcColumn, coldata.getType());
        }
    }
    
    /**
     * Set a parameter of a prepared statement with a value from a tuple.
     * 
     * @param statement
     *     Prepared statement.
     * @param tuple
     *     Tuple.
     * @param tupleIndex
     *     Index of column in tuple which provides the parameter value.
     * @param paramIndex
     *     Index of the parameter in the prepared statement to be set.
     * @param type
     *     Type of the tuple column. One of {@link TupleTypes}.
     * @throws SQLException
     *     If any problems arise.
     */
    public static void setStatementParameter(PreparedStatement statement, 
            Tuple tuple, 
            int tupleIndex, 
            int paramIndex, 
            int type)
    throws SQLException
    { 
        if (tuple.getObject(tupleIndex) == Null.getValue())
        {
            statement.setString(paramIndex, null);
            return;
        }
        switch (type) 
        {
            case TupleTypes._BOOLEAN:
                statement.setBoolean(paramIndex, tuple.getBoolean(tupleIndex));
                break;
            case TupleTypes._BIGDECIMAL:
                statement.setBigDecimal(paramIndex, tuple.getBigDecimal(tupleIndex));
                break;
            case TupleTypes._INT: 
                statement.setInt(paramIndex, tuple.getInt(tupleIndex));
                break;
            case TupleTypes._LONG:
                statement.setLong(paramIndex, tuple.getLong(tupleIndex));
                break;
            case TupleTypes._SHORT:
                statement.setShort(paramIndex, tuple.getShort(tupleIndex));
                break;
            case TupleTypes._FLOAT:
                statement.setFloat(paramIndex, tuple.getFloat(tupleIndex));
                break;
            case TupleTypes._DOUBLE:
                statement.setDouble(paramIndex, tuple.getDouble(tupleIndex));
                break;
            case TupleTypes._TIME:
                statement.setTime(paramIndex, tuple.getTime(tupleIndex));
                break;
            case TupleTypes._TIMESTAMP:
                statement.setTimestamp(paramIndex, tuple.getTimestamp(tupleIndex));
                break;
            case TupleTypes._DATE: 
                statement.setDate(paramIndex, tuple.getDate(tupleIndex));
                break;
            case TupleTypes._STRING:
                statement.setString(paramIndex, tuple.getString(tupleIndex));
                break;
            case TupleTypes._ODBLOB:
                final Blob blob = tuple.getBlob(tupleIndex);
                // Note that setBlob(blob) does not work for all
                // databases. 
                // The BLOB length must not be greater than
                // Integer.MAX_VALUE. 
                // For SimpleBlob this is not a problem since the data
                // is held in a byte array which has an integer
                // length. 
                // setBlob(blob) does not work for all databases.
                statement.setBinaryStream(paramIndex, 
                                          blob.getBinaryStream(), 
                                          (int)blob.length());
                break;
            case TupleTypes._ODCLOB:
                final Clob clob = tuple.getClob(tupleIndex);
                // Note that setClob(clob) does not work for all
                // databases. 
                // The CLOB length must not be greater than
                // Integer.MAX_VALUE. 
                // For SimpleClob this is not a problem since the data
                // is held in a byte array which has an integer
                // length.
                statement.setCharacterStream(paramIndex, 
                                             clob.getCharacterStream(),
                                             (int)clob.length());
                break;
            default:
                statement.setObject(paramIndex, tuple.getObject(tupleIndex));
        }
    }
    
    /**
     * Creates the SQL for a prepared insert statement.
     * 
     * @param tableName
     *     Name of table to insert into.
     * @param metadata
     *     Tuple meta data containing column details.
     * @return SQL string.
     */
    public static String createInsertStatementSQL(
        final String tableName, 
        final TupleMetadata metadata)
    {
        final StringBuffer sql = new StringBuffer(
            "INSERT INTO " + tableName + " VALUES(");
        final int columnCount = metadata.getColumnCount();
        for (int i = 0; i < columnCount; i++)
        {
            sql.append("?");
            sql.append(i < columnCount - 1 ? "," : ")");
        }
        return new String(sql);
    }

    /**
     * Creates a list of tuples containing the data retrieved for a
     * relational resource after the execution of a query.
     * 
     * @param resultset
     *     Result set of the statement.
     * @param output
     *     Output where data is going to be written to.
     * @param mapper
     *     Column type mapper used to convert SQL types to OGSA-DAI types.
     * @param dataResourceID
     *     ID of the data resource through which the result set was obtained,
     *     or <code>null</code> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <code>null</code> if unknown.
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it.
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     {@link uk.org.ogsadai.tuple.SimpleClob}.
     */
    public static void createTupleList(
        final ResultSet resultset,
        final BlockWriter output,
        final JDBCColumnTypeMapper mapper,
        final ResourceID dataResourceID,
        final URI dresUri)
        throws PipeClosedException,
               PipeIOException, 
               PipeTerminatedException, 
               SQLException, 
               IOException
    {
        createTupleList(
            resultset, output, mapper, dataResourceID, dresUri, 
            new DefaultMetaDataHandler(), true, true);
    }
    
    /**
     * Creates a list of tuples containing the data retrieved for a
     * relational resource after the execution of a query.
     * 
     * @param resultset
     *     Result set of the statement.
     * @param output
     *     Output where data is going to be written to.
     * @param mapper
     *     column type mapper used to convert SQL types to OGSA-DAI types.
     * @param dataResourceID
     *     ID of the data resource through which the result set was obtained,
     *     or <code>null</code> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <code>null</code> if unknown.
     * @param includeListMarkers
     *     Should the list markers be output? <code>true</code> specifies list
     *     markers should be written, <code>false</code> specifies no list markers.
     * @param includeMetaData
     *     Should the meta data block be output?  <code>true</code> specifies the
     *     meta data block should be written, <code>false</code> specifies no 
     *     meta data block.
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it.
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     {@link uk.org.ogsadai.tuple.SimpleClob}.
     */
    public static void createTupleList(
        final ResultSet resultset,
        final BlockWriter output,
        final JDBCColumnTypeMapper mapper,
        final ResourceID dataResourceID,
        final URI dresUri,
        boolean includeListMarkers,
        boolean includeMetaData)
        throws PipeClosedException,
               PipeIOException, 
               PipeTerminatedException, 
               SQLException, 
               IOException
    {
        createTupleList(
                resultset, output, mapper, dataResourceID, dresUri, 
                new DefaultMetaDataHandler(), includeListMarkers, includeMetaData);  
    }
    
    /**
     * Creates a list of tuples containing the data retrieved for a
     * relational resource after the execution of a query.
     * 
     * @param resultset
     *     Result set of the statement.
     * @param output
     *     Output where data is going to be written to.
     * @param mapper
     *     column type mapper used to convert SQL types to OGSA-DAI types.
     * @param dataResourceID
     *     ID of the data resource through which the result set was obtained,
     *     or <code>null</code> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <code>null</code> if unknown.
     * @param handler
     *     Meta data handler for resource.
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it.
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     {@link uk.org.ogsadai.tuple.SimpleClob}.
     */
    public static void createTupleList(
        final ResultSet resultset,
        final BlockWriter output,
        final JDBCColumnTypeMapper mapper,
        final ResourceID dataResourceID,
        final URI dresUri,
        final MetaDataHandler handler)
        throws PipeClosedException,
               PipeIOException, 
               PipeTerminatedException, 
               SQLException, 
               IOException
    {
        createTupleList(
            resultset, output, mapper, dataResourceID, dresUri, handler, true, true);
    }

    /**
     * Creates a list of tuples containing the data retrieved for a
     * relational resource after the execution of a query.
     * 
     * @param resultset
     *     Result set of the statement.
     * @param output
     *     Output where data is going to be written to.
     * @param mapper
     *     column type mapper used to convert SQL types to OGSA-DAI types.
     * @param dataResourceID
     *     ID of the data resource through which the result set was obtained,
     *     or <code>null</code> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <code>null</code> if unknown.
     * @param handler
     *     Meta data handler for resource.
     * @param includeListMarkers
     *     Should the list markers be output? <code>true</code> specifies list
     *     markers should be written, <code>false</code> specifies no list markers.
     * @param includeMetaData
     *     Should the meta data block be output?  <code>true</code> specifies the
     *     meta data block should be written, <code>false</code> specifies no 
     *     meta data block.
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it.
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     {@link uk.org.ogsadai.tuple.SimpleClob}.
     */
    public static void createTupleList(
        final ResultSet resultset,
        final BlockWriter output,
        final JDBCColumnTypeMapper mapper,
        final ResourceID dataResourceID,
        final URI dresUri,
        final MetaDataHandler handler,
        boolean includeListMarkers,
        boolean includeMetaData)
        throws PipeClosedException,
               PipeIOException, 
               PipeTerminatedException, 
               SQLException, 
               IOException
        {
        logger.debug("createTupleList() begin");

        try {
        
            if (includeListMarkers)
                {
                output.write(ControlBlock.LIST_BEGIN);
                }
            
            if (resultset != null)
                {
                // Get metadata from DB.
                ResultSetMetaData rsmd = resultset.getMetaData();
                TupleMetadata metadata = handler.produceResultSetMetaData(rsmd, 
                        mapper, dataResourceID, dresUri);
                int numberOfColumns = rsmd.getColumnCount();
                if (includeMetaData)
                    {
                    output.write(new MetadataWrapper(metadata));
                    }
                // Assign converters per column type.
                ObjectConverter[] converters = 
                    new ObjectConverter[numberOfColumns];
                for (int i = 0; i < converters.length; i++)
                    {
                    int colType = metadata.getColumnMetadata(i).getType();
                    converters[i] = mapTypeToObjectConverter(colType);
                    }
                // Iterate through data and create tuples.
                while (resultset.next())
                    {
                    List<Object> data = new ArrayList<Object>(numberOfColumns);
                    for (int i = 1; i <= numberOfColumns; i++)
                        {
                        Object value = resultset.getObject(i);
                        Object columnValue = converters[i-1].convert(value);
                        if (columnValue == null)
                            {
                            columnValue = Null.getValue();
                            }
                        data.add(columnValue);
                        }
                    // Create the tuple tuple consisting of the above data
                    // and the meta data.
                    Tuple tuple = new SimpleTuple(data);
                    output.write(tuple);
                    }
                if (includeListMarkers)
                    {
                    output.write(ControlBlock.LIST_END);
                    }
                }
            else
                {
                if (includeListMarkers)
                    {
                    output.write(ControlBlock.LIST_END);
                    }
                }
            }
        catch (PipeClosedException ouch)
            {
            // Do nothing, we are done.
            // Main code will close the connection.
            logger.debug("PipeClosedException in createTupleList()");
            }

        logger.debug("createTupleList() done");
        }
    
    /**
     * Maps column types to object converters.
     * 
     * @param columnType 
     *     Column type identifier, as defined in {@link TupleTypes}.
     * @return the object converter to convert an object from a result set to
     *         the appropriate Java type.
     */
    private static ObjectConverter mapTypeToObjectConverter(int columnType)
    {
        ObjectConverter result;

        switch(columnType)
        {
            case TupleTypes._ODBLOB:
                result = new BlobConverter();
                break;
            case TupleTypes._ODCLOB:
                result = new ClobConverter();
                break;
            case TupleTypes._BIGDECIMAL:
                result = new BigDecimalConverter();
                break;
            default:
                result = new DefaultConverter();
                break;
        }
        return result;
    }
    
    /**
     * Implemented by classes that convert objects from a result set
     * into objects that can be added to a tuple.
     *
     * @author The OGSA-DAI Project Team.
     */
    static interface ObjectConverter
    {
        /**
         * Extracts a column value from a result set and converts it into an
         * appropriate object type.
         * 
         * @param object
         *     Object to convert. 
         * @return converted object.
         * @throws SQLException
         *     If there was a problem creating the output.
         * @throws IOException
         *     If there was a problem creating the output.
         */
        public Object convert(Object object) throws SQLException, IOException;
    }
    
    /**
     * Does not convert objects.
     *
     * @author The OGSA-DAI Project Team.
     */
    static class DefaultConverter implements ObjectConverter
    {
        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2007-2010.";

        /**
         * @return the object as returned from the result set.
         * @see ObjectConverter#convert
         */
        @Override
        public Object convert(Object object) 
        {
            return object;
        }        
    }
    
    /**
     * Converts an object into an OGSA-DAI blob.
     *
     * @author The OGSA-DAI Project Team.
     */
    static class BlobConverter implements ObjectConverter
    {
        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2007-2010.";

        /**
         * @return a BLOB created from the input object which is
         * expected to be of type <code>byte[]</code> or
         * {@link java.sql.Blob}. 
         * @see ObjectConverter#convert
         */
        @Override
        public Object convert(Object obj) throws SQLException, IOException 
        {
            if (obj == null)
            {
                return null;
            }
            else
            {
                return new SimpleBlob(obj);
            }
        }
    }

    /**
     * Converts an object into an OGSA-DAI Clob.
     *
     * @author The OGSA-DAI Project Team.
     */
    static class ClobConverter implements ObjectConverter
    {
        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2007-2010";

        /**
         * @return a CLOB created from the input object which is
         * expected to be of type<code>byte[]</code> or
         * {@link java.sql.Clob}. 
         * @see ObjectConverter#convert
         */
        @Override
        public Object convert(Object obj) throws SQLException, IOException
        {
            if (obj == null)
            {
                return null;
            }
            else
            {
                return new SimpleClob(obj);
            }
        }
    }

    /**
     * Converts an object into an BigDecimal.
     *
     * @author The OGSA-DAI Project Team.
     */
    static class BigDecimalConverter implements ObjectConverter
    {
        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2007-2010";

        /**
         * @return a {@link BigDecimal} object
         * @see ObjectConverter#convert
         */
        @Override
        public Object convert(Object obj)
        {
            if (obj == null)
            {
                return null;
            }
            else if (obj instanceof BigDecimal)
            {
                return obj;
            }
            else if (obj instanceof BigInteger)
            {
                return new BigDecimal((BigInteger)obj);
            }
            else
            {
                return null;
            }
        }
    }
    
    /**
     * Gets the column indices corresponding to the columns identified in the
     * given input {@link ListIterator}.
     * 
     * @param metadata  
     *     Tuple meta data associated with a single tuple stream.
     * @param columnIds 
     *     {@link ListIterator} giving access to the list of column IDs
     *     that specify which columns to get.  The iterator
     *     should give access to objects of type {@link Integer} or
     *     {@link String}. Integers are treated as (zero-based) indices
     *     that specify the element within the tuple.  Strings are treated as
     *     column names and the meta data is used to obtain the appropriate
     *     index. If this parameter is <code>null</code> then the client has
     *     not specified any column indices in which case <code>null</code> is
     *     returned.
     * @param inputName
     *     Name of the input that provided this data.  This is used to generate
     *     any error messages.
     * @return an array of column indices corresponding to the column 
     * identifiers.
     * @throws ActivityUserException
     *     If the column IDs are not consistent with the meta data.
     * @throws ActivityProcessingException
     *     If there is a problem in getting data from the iterator.
     * @throws ActivityTerminatedException
     *     If the activity providing the data has terminated.
     */
    public static int[] getColumnIndices(
        final TupleMetadata metadata,
        final ListIterator columnIds,
        final String inputName) 
    throws ActivityUserException, 
        ActivityProcessingException, 
        ActivityTerminatedException
    {
        ArrayList<Object> array = new ArrayList<Object>();
        if (columnIds == null)
        {
            return null;
        }
        else
        {
            Object id;
            while( (id = columnIds.nextValue()) != null)
            {
                int index = 0;
                if (id instanceof Number)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Column index is a Number: " + id);
                    }
                    index = ((Integer)id).intValue();
                    if (index >= metadata.getColumnCount())
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column index out of range: " + index);
                        }
                        throw new ActivityUserException(
                            ErrorID.COLUMN_INDEX_OUT_OF_RANGE,
                            new Object[]{ inputName, id });
                    }
                    if (index < 0)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column index out of range: " + index);
                        }
                        throw new ActivityUserException(
                            ErrorID.COLUMN_INDEX_OUT_OF_RANGE,
                            new Object[]{ inputName, id });
                    }
                }
                else if (id instanceof String)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Column index is a String: " + id);
                    }
                    try
                    {
                        index = metadata.getColumnMetadataPosition((String) id);
                    }
                    catch (ColumnNotFoundException e)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column name not found: " + id);
                        }
                        throw new ActivityUserException(
                            ErrorID.UNKNOWN_COLUMN_NAME,
                            new Object[]{ inputName, id });
                    }
                    
                    if (index < 0)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column name not found: " + id);
                        }
                        throw new ActivityUserException(
                            ErrorID.UNKNOWN_COLUMN_NAME,
                            new Object[]{ inputName, id });
                    }
                }
                else
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug(
                            "Column index is unexpected type: " + 
                            id.getClass().getName());
                    }
                    throw new ActivityUserException(
                        ErrorID.COLUMN_IDENTIFIER_IS_NOT_INTEGER_OR_STRING,
                        new Object[]{ inputName, id.getClass().getName()});
                }
                
                array.add(new Integer(index));
            }
        }
        int[] result = new int[array.size()];
        for(int i=0; i<result.length; ++i)
        {
            result[i] = ((Integer)array.get(i)).intValue();
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Column index " + i + ": " + result[i]);
            }
        }
        return result;
    }
    
    /**
     * Gets the column names corresponding to the columns identified in the
     * given input {@link ListIterator}.
     * 
     * @param metadata  
     *     Tuple meta data associated with a single tuple stream.
     * @param columnIds 
     *     {@link ListIterator} giving access to the list of column IDs
     *     that specify which columns to get.  The iterator
     *     should give access to objects of type {@link Integer} or
     *     {@link String}. Integers are treated as (zero-based) indices
     *     that specify the element within the tuple.  Strings are treated as
     *     column names and the meta data is used to obtain the appropriate
     *     index. If this parameter is <code>null</code> then the client has
     *     not specified any column indices in which case <code>null</code> is
     *     returned.
     * @param inputName
     *     Name of the input that provided this data.  This is used to generate
     *     any error messages.
     * @return an list of column names corresponding to the column 
     * identifiers.
     * @throws ActivityUserException
     *     If the column IDs are not consistent with the meta data.
     * @throws ActivityProcessingException
     *     If there is a problem in getting data from the iterator.
     * @throws ActivityTerminatedException
     *     If the activity providing the data has terminated.
     */
    public static List<String> getColumnNames(
        final TupleMetadata metadata,
        final ListIterator columnIds,
        final String inputName) 
        throws ActivityUserException, 
               ActivityProcessingException, 
               ActivityTerminatedException
    {
        List<String> result = new LinkedList<String>();
        if (columnIds == null)
        {
            return null;
        }
        else
        {
            Object id;
            while( (id = columnIds.nextValue()) != null)
            {
                String columnName = null;
                if (id instanceof Number)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Column index is an Integer: " + id);
                    }
                    int index = ((Integer)id).intValue();
                    if (index >= metadata.getColumnCount())
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column index out of range: " + index);
                        }
                        throw new ActivityUserException(
                            ErrorID.COLUMN_INDEX_OUT_OF_RANGE,
                            new Object[]{ inputName, id });
                    }
                    if (index < 0)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column index out of range: " + index);
                        }
                        throw new ActivityUserException(
                            ErrorID.COLUMN_INDEX_OUT_OF_RANGE,
                            new Object[]{ inputName, id });
                    }                    
                    columnName = metadata.getColumnMetadata(index).getName();
                }
                else if (id instanceof String)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Column index is a String: " + id);
                    }
                    int index = 0;
                    try
                    {
                        // Test to see the column exists
                        index = metadata.getColumnMetadataPosition((String) id);
                    }
                    catch(ColumnNotFoundException e)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column name not found: " + id);
                        }
                        throw new ActivityUserException(
                            ErrorID.UNKNOWN_COLUMN_NAME,
                            new Object[]{ inputName, id });
                    }       
                    if (index < 0)
                    {
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug("Column name not found: " + id);
                        }
                        throw new ActivityUserException(
                            ErrorID.UNKNOWN_COLUMN_NAME,
                            new Object[]{ inputName, id });
                    }
                    columnName = (String) id;
                }
                else
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug(
                            "Column index is unexpected type: " + 
                            id.getClass().getName());
                    }
                    throw new ActivityUserException(
                        ErrorID.COLUMN_IDENTIFIER_IS_NOT_INTEGER_OR_STRING,
                        new Object[]{ inputName, id.getClass().getName()});
                }
                result.add(columnName);
            }
        }
        return result;
    }
    
    /**
     * Set up the <code>IN,INOUT,OUT</code> parameters for 
     * {@link CallableStatement}.
     * 
     * @param statement
     *     Statement.
     * @param parameters
     *     Tuple list of SQL parameters compliant with the schema defined in
     *     {@link SQLParameterTupleConverter}.
     * @param keys
     *     List populated with positions of each <code>INOUT,OUT</code>
     *     parameter.
     * @throws ActivityUserException
     *     If there is a problem getting a parameter from the activity input due 
     *     to the client e.g. the tuple list is not a list of valid SQL 
     *     parameters.
     * @throws ActivityProcessingException
     *     If there is a problem getting a parameter from the activity input due 
     *     to a server error.
     * @throws ActivityTerminatedException
     *     If there is a problem getting a parameter because the upstream 
     *     activity has terminated.
     * @throws SQLException
     *     If there is a problem setting the parameter on the JDBC 
     *     {@link PreparedStatement}.
     */
    public static void setCallableParameters(
            CallableStatement statement,
            TupleListIterator parameters,
            List<Integer> keys)
    throws ActivityUserException, 
        ActivityProcessingException,
        ActivityTerminatedException,
        SQLException
    {
            TupleMetadata metadata = RelationalUtilities.getMetadata(parameters);
            // Index of SQL parameter type column.
            int typeIndex = 
                metadata.getColumnMetadataPosition(SQLParameterTupleConverter.TYPE);
            // Index of SQL parameter value column.
            int valueIndex = 
                metadata.getColumnMetadataPosition(SQLParameterTupleConverter.VALUE);
            // Index of SQL parameter mode column.
            int modeIndex = 
                metadata.getColumnMetadataPosition(SQLParameterTupleConverter.MODE);
            Tuple tuple = null;
            int pos = 1;
            while ((tuple = (Tuple)parameters.nextValue()) != null)
            {
                // Get parameter mode.
                SQLParameterMode mode = null;
                try
                {
                    mode = SQLParameterTupleConverter.getMode(tuple.getString(modeIndex));
                }
                catch (UnknownSQLParameterModeException e)
                {
                    throw new ActivityUserException(e);
                }
                // Get parameter type.
                int type = tuple.getInt(typeIndex);
                // Map to JDBC type for INOUT and OUT parameters.
                int sqlType = TupleUtilities.mapTupleTypeToSQLType(type);
                if (mode.equals(SQLParameterMode.OUT))
                {
                    // Set OUT parameter.
                    statement.registerOutParameter(pos, sqlType);
                    keys.add(new Integer(pos));
                }
                else if (mode.equals(SQLParameterMode.INOUT))
                {
                    // Set OUT parameter.
                    statement.registerOutParameter(pos, sqlType);
                    // Set IN parameter.
                    setStatementParameter(statement, tuple, valueIndex, pos, type);
                    keys.add(new Integer(pos));
                }
                else if (mode.equals(SQLParameterMode.IN))
                {
                    // Set IN parameter.
                    setStatementParameter(statement, tuple, valueIndex, pos, type);
                }
                pos++;
            }
    }

    /**
     * Set up the <code>IN</code> parameters for {@link PreparedStatement}.
     * 
     * @param statement
     *     Statement.
     * @param parameters
     *     Tuple list of SQL parameters compliant with the schema defined in
     *     {@link SQLParameterTupleConverter}.
     * @throws ActivityUserException
     *     If there is a problem getting a parameter from the activity input due 
     *     to the client e.g. the tuple list is not a list of valid SQL 
     *     parameters or a parameter is not a <code>IN</code> parameter.
     * @throws ActivityProcessingException
     *     If there is a problem getting a parameter from the activity input due
     *     to a server error.
     * @throws ActivityTerminatedException
     *     If there is a problem getting a parameter because the upstream 
     *     activity has terminated.
     * @throws SQLException
     *     If there is a problem setting the parameter on the JDBC 
     *     {@link PreparedStatement}.
     */
    public static void setPreparedParameters(
            PreparedStatement statement,
            TupleListIterator parameters)
    throws ActivityUserException, 
        ActivityProcessingException,
        ActivityTerminatedException,
        SQLException
    {
        TupleMetadata metadata = RelationalUtilities.getMetadata(parameters);
        // Index of SQL parameter type column.
        int typeIndex = 
            metadata.getColumnMetadataPosition(SQLParameterTupleConverter.TYPE);
        // Index of SQL parameter value column.
        int valueIndex = 
            metadata.getColumnMetadataPosition(SQLParameterTupleConverter.VALUE);
        // Index of SQL parameter mode column.
        int modeIndex = 
            metadata.getColumnMetadataPosition(SQLParameterTupleConverter.MODE);
        Tuple tuple = null;
        int pos = 1;
        while ((tuple = (Tuple)parameters.nextValue()) != null)
        {
            // Get parameter mode.
            SQLParameterMode mode = null;
            try
            {
                mode = SQLParameterTupleConverter.getMode(tuple.getString(modeIndex));
            }
            catch (UnknownSQLParameterModeException e)
            {
                throw new ActivityUserException(e);
            }
            // Get parameter type.
            int type = tuple.getInt(typeIndex);
            if (mode.equals(SQLParameterMode.IN))
            {
                // Set IN parameter.
                setStatementParameter(statement, tuple, valueIndex, pos, type);
            }
            else
            {
                throw new ActivityUserException(ErrorID.SQL_IN_PARAMETERS_ONLY);
            }
            pos++;
        }
    }
}
