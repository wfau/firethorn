//Copyright (c) The University of Edinburgh 2009-2012.

package uk.org.ogsadai.dqp.common.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.DataNodeTable;
import uk.org.ogsadai.dqp.common.LogicalSchema;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.TableSchema;

/**
 * Simple implementation of a remote table schema.
 *
 * TODO: doc
 *
 * @author The OGSA-DAI Project Team.
 */
public class SimpleTableSchema implements TableSchema
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2009-2012";

    /** 
     * Details of the data node tables associated with this global view table.
     * This is an array to support table replication.
     */
    private final List<DataNodeTable> mDataNodeTables;
    
    private final LogicalSchema mSchema;
    private final String mName;
    private PhysicalSchema mPhysicalSchema;

    /**
     * Constructs a new table schema.
     * 
     * @param schema
     *            table schema
     * @param localName
     *            table name
     * @param dataNode
     *            data node that accesses the table
     */
    public SimpleTableSchema(
            String localName,
            DataNode dataNode,
            LogicalSchema schema)
    {
        this(localName, dataNode, schema, null);
    }

    /**
     * Constructs a new table schema.
     * 
     * @param localName
     *            table name
     * @param dataNode
     *            data node that accesses the table
     * @param schema
     *            table schema
     * @param physicalSchema
     *            physical schema or <code>null</code> if no physical schema
     */
    public SimpleTableSchema(
            String localName,
            DataNode dataNode,
            LogicalSchema schema,
            PhysicalSchema physicalSchema)
    {
        mDataNodeTables = new ArrayList<DataNodeTable>();
        mDataNodeTables.add(new SimpleDataNodeTable(dataNode, localName));
        mName = schema.getName();
        mSchema = schema;
        mPhysicalSchema = physicalSchema;
    }

    public LogicalSchema getSchema()
    {
        return mSchema;
    }

    public String getTableName()
    {
        return mName;
    }
    
    public String toString()
    {
        return "TableSchema [name=" + mName + " " + mSchema + "]";
    }

    public PhysicalSchema getPhysicalSchema()
    {
        return mPhysicalSchema;
    }

    public List<DataNodeTable> getDataNodeTables()
    {
        return Collections.unmodifiableList(mDataNodeTables);
    }

    public DataNodeTable getDataNodeTable(DataNode dataNode)
    {
        for( DataNodeTable dataNodeTable : mDataNodeTables)
        {
            if (dataNode.equals(dataNodeTable.getDataNode()))
            {
                return dataNodeTable;
            }
        }
        
        return null;
    }
    
}
