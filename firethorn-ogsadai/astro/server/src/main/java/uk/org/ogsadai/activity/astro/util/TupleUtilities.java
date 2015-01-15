// Copyright (c) The University of Edinburgh, 2002-2010.
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

package uk.org.ogsadai.activity.astro.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.astro.votable.VOTableMetaData;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.sql.SQLUtilities;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.dataresource.jdbc.DefaultMetaDataHandler;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCColumnTypeMapper;
import uk.org.ogsadai.resource.dataresource.jdbc.MetaDataHandler;
import uk.org.ogsadai.tuple.ColumnNotFoundException;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.SimpleBlob;
import uk.org.ogsadai.tuple.SimpleClob;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * Utility methods for use by TAP activities.
 *  
 * @author The OGSA-DAI Project Team.
 */
public class TupleUtilities extends SQLUtilities
{
    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2007-2008";

    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(SQLUtilities.class);

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
     *     or <tt>null</tt> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <tt>null</tt> if unknown.
     *     
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     <code>uk.org.ogsadai.tuple.Clob</code> object
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
            resultset, output, mapper, dataResourceID, dresUri, new DefaultMetaDataHandler(), true, true);
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
     *     or <tt>null</tt> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <tt>null</tt> if unknown.
     * @param includeListMarkers
     *     Should the list markers be output? <tt>true</tt> specifies list
     *     markers should be written, <tt>false</tt> specifies no list markers.
     * @param includeMetaData
     *     Should the metadata block be output?  <tt>true</tt> specifies the
     *     metadata block should be written, <tt>false</tt> specifies no 
     *     metadata block.
     *     
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     <code>uk.org.ogsadai.tuple.Clob</code> object
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
                resultset, output, mapper, dataResourceID, dresUri, new DefaultMetaDataHandler(), includeListMarkers, includeMetaData);  
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
     *     or <tt>null</tt> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <tt>null</tt> if unknown.
     * @param handler
     *     MetaDataHandler for resource.
     *     
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     <code>uk.org.ogsadai.tuple.Clob</code> object
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
     *     or <tt>null</tt> if unknown.
     * @param dresUri
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <tt>null</tt> if unknown.
     * @param handler
     *     MetaDataHandler for resource.
     * @param includeListMarkers
     *     Should the list markers be output? <tt>true</tt> specifies list
     *     markers should be written, <tt>false</tt> specifies no list markers.
     * @param includeMetaData
     *     Should the metadata block be output?  <tt>true</tt> specifies the
     *     metadata block should be written, <tt>false</tt> specifies no 
     *     metadata block.
     *     
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     <code>uk.org.ogsadai.tuple.Clob</code> object
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
                // and the metadata.
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
    
    /**
     * 
     * Creates a list of tuples containing the data obtain from a star table.
     * 
     * @param table
     *     star table
     * @param resource
     *     resource
     * @param dres
     *     URI of the Data Request Execution Service (DRES) used to access the
     *     data resource through which the result set was obtained, or 
     *     <tt>null</tt> if unknown.
     * @param includeListMarkers
     *     Should the list markers be output? <tt>true</tt> specifies list
     *     markers should be written, <tt>false</tt> specifies no list markers.
     * @param includeMetaData
     *     Should the metadata block be output?  <tt>true</tt> specifies the
     *     metadata block should be written, <tt>false</tt> specifies no 
     *     metadata block.
     * @param output
     *     Output where data is going to be written to.
     * @throws PipeClosedException
     *     If the output closes prematurely while writing to it
     * @throws PipeIOException
     *     If a problem occurs while writing to the output.
     * @throws PipeTerminatedException
     *     If the pipe was terminated.
     * @throws SQLException
     *     If a problem occurs interacting with the result set.
     * @throws IOException
     *     If a problem occurs while initialising a
     *     <code>uk.org.ogsadai.tuple.Clob</code> object
     */
    public static void createTupleList(
            final StarTable table,
            final Resource resource,
            final URI dres,
            boolean includeListMarkers,
            boolean includeMetaData,
            final BlockWriter output)
            throws PipeClosedException,
                   PipeIOException, 
                   PipeTerminatedException, 
                   SQLException, 
                   IOException {
        
        if (includeListMarkers) {
            output.write(ControlBlock.LIST_BEGIN);
        }
        VOTableMetaData tupleMetaData = new VOTableMetaData(table, dres, resource);
        int numberOfColumns = tupleMetaData.getColumnCount();
        if (includeMetaData) {
            output.write(new MetadataWrapper(tupleMetaData));
        }

        // Assign converters per column type.
        ObjectConverter[] converters = 
            new ObjectConverter[numberOfColumns];
        for (int i = 0; i < converters.length; i++)
        {
            int colType = tupleMetaData.getColumnMetadata(i).getType();
            converters[i] = mapTypeToObjectConverter(colType);
        }

        RowSequence sequence = table.getRowSequence();
        try {
            while (sequence.next()) {
                List<Object> data = new ArrayList<Object>(numberOfColumns);
                Object[] row = sequence.getRow();
                for (int i=0; i<row.length; i++) {
                    Object columnValue = converters[i].convert(row[i]);
                    if (columnValue == null)
                    {
                        columnValue = Null.getValue();
                    }
                    data.add(columnValue);
                }
                // Create the tuple tuple consisting of the above data
                // and the metadata.
                Tuple tuple = new SimpleTuple(data);
                output.write(tuple);
            }
        }
        finally {
            sequence.close();
        }
        
        if (includeListMarkers)
        {
            output.write(ControlBlock.LIST_END);
        }
    }
    
    /**
     * Maps column types to object converters.
     * 
     * @param columnType column type identifier, as defined in TupleTypes
     * 
     * @return the object converter to convert an object from a result set to
     *         the appropriate java type.
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
         * @param resultSet 
         *    result set positioned at the row to be read
         * @param index 
         *    position in the row to be read
         * @return 
         *     the column value as the object type, or <code>null</code>
         *     if the column value was null.
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
            "Copyright (c) The University of Edinburgh, 2007-2008.";

        /**
         * {@inheritDoc}
         *
         * @return the object as returned from the result set.
         */
        public Object convert(Object object) 
        {
            return object;
        }        
    }
    
    /**
     * Converts an object into an OGSA-DAI Blob.
     *
     * @author The OGSA-DAI Project Team.
     */
    static class BlobConverter implements ObjectConverter
    {
        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE = 
            "Copyright (c) The University of Edinburgh, 2007-2008.";

        /**
         * {@inheritDoc}
         *
         * @return a BLOB created from the input object which is
         * expected to be of type <code>byte[]</code> or
         * <code>java.sql.Blob</code>. 
         * @throws IOException 
         * @throws SQLException 
         */
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
            "Copyright (c) The University of Edinburgh, 2007-2008";

        /**
         * {@inheritDoc}
         *
         * @return a CLOB created from the input object which is
         * expected to be of type<code>byte[]</code> or
         * <code>java.sql.Clob</code>. 
         * @throws IOException 
         * @throws SQLException 
         */
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
            "Copyright (c) The University of Edinburgh, 2007-2008";

        /**
         * {@inheritDoc}
         *
         * @return a BigDecimal object
         */
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
     * given input <code>ListIterator</code>.
     * 
     * @param metadata  
     *     tuple metadata associated with a single tuple stream
     * @param columnIds 
     *     <code>ListIterator</code> giving access to the list of column IDs
     *     that specify which columns should be used in the join.  The iterator
     *     should give access to objects of type <code>Integer</code> or
     *     <code>String</code>.  Integers are treated as (zero-based) indices
     *     that specify the element within the tuple.  Strings are treaded as
     *     column names and the metadata is used to obtain the appropriate
     *     index.  If this parameter is <code>null</code> then the client has
     *     not specified any column indices in which case <code>null</code> is
     *     returned.
     * @param inputName
     *     Name of the input that provided this data.  This is used to generate
     *     any error messages.
     *     
     * @return an array of column indices corresponding to the column 
     *         identifiers.
     * 
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
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
                        LOG.debug("Column index out of range: " + index);
                        throw new ActivityUserException(
                            ErrorID.COLUMN_INDEX_OUT_OF_RANGE,
                            new Object[]{ inputName, id });
                    }
                    
                    if (index < 0)
                    {
                        LOG.debug("Column index out of range: " + index);
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
                    catch(ColumnNotFoundException e)
                    {
                        LOG.debug("Column name not found: " + id);
                        throw new ActivityUserException(
                            ErrorID.UNKNOWN_COLUMN_NAME,
                            new Object[]{ inputName, id });
                    }
                    
                    if (index < 0)
                    {
                        LOG.debug("Column name not found: " + id);
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
     * given input <code>ListIterator</code>.
     * 
     * @param metadata  
     *     tuple metadata associated with a single tuple stream
     * @param columnIds 
     *     <code>ListIterator</code> giving access to the list of column IDs
     *     that specify which columns should be used in the join.  The iterator
     *     should give access to objects of type <code>Integer</code> or
     *     <code>String</code>.  Integers are treated as (zero-based) indices
     *     that specify the element within the tuple.  Strings are treaded as
     *     column names and the metadata is used to obtain the appropriate
     *     index.  If this parameter is <code>null</code> then the client has
     *     not specified any column indices in which case <code>null</code> is
     *     returned.
     * @param inputName
     *     Name of the input that provided this data.  This is used to generate
     *     any error messages.
     *     
     * @return a list of column names corresponding to the column identifiers.
     * 
     * @throws ActivityUserException
     * @throws ActivityProcessingException
     * @throws ActivityTerminatedException
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
                        LOG.debug("Column index out of range: " + index);
                        throw new ActivityUserException(
                            ErrorID.COLUMN_INDEX_OUT_OF_RANGE,
                            new Object[]{ inputName, id });
                    }
                    
                    if (index < 0)
                    {
                        LOG.debug("Column index out of range: " + index);
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
                        LOG.debug("Column name not found: " + id);
                        throw new ActivityUserException(
                            ErrorID.UNKNOWN_COLUMN_NAME,
                            new Object[]{ inputName, id });
                    }
                    
                    if (index < 0)
                    {
                        LOG.debug("Column name not found: " + id);
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
    

}
