package uk.org.ogsadai.dqp.firethorn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.DataNodeTable;
import uk.org.ogsadai.dqp.common.LogicalSchema;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.TableSchema;
import uk.org.ogsadai.dqp.common.simple.SimpleDataNodeTable;
import uk.org.ogsadai.dqp.common.simple.SimpleLogicalSchema;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.SimpleStatisticsPhysicalSchema;

/**
 * A table schema that retrieves logical and physical table metadata on demand.
 * As it is assumed that the table schema is requested once per query the
 * physical and logical schema are cached for the lifetime of this object (ie
 * the duration of the query) to avoid querying the metadata service every time,
 * for example, when an optimiser requests cardinality estimation, or a table
 * appears twice in a query.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class MetadataServiceTableSchema implements TableSchema 
{
    /** Name of the table in the federation. */
    private String mTableName;
    /** List of data node tables that can access the table. */
    private List<DataNodeTable> mDataNodeTables;
    /** The data dictionary which provides access to the metadata services. */
    private MetadataServiceDataDictionary mDataDictionary;
    
    /**
     * Cached copies of the logical and physical schema. If <code>null</code>
     * the schemas have not been retrieved from the metadata services yet.
     */
    private LogicalSchema mLogicalSchema;
    private PhysicalSchema mPhysicalSchema;

    public MetadataServiceTableSchema(
            String tableName, 
            String localName, 
            DataNode dataNode) 
    {
        mTableName = tableName;
        mDataNodeTables = Arrays.<DataNodeTable>asList(
                new SimpleDataNodeTable(dataNode, localName));
    }
    
    public void setDataDictionary(MetadataServiceDataDictionary dataDictionary)
    {
        mDataDictionary = dataDictionary;
    }
    
    @Override
    public List<DataNodeTable> getDataNodeTables() 
    {
        return Collections.unmodifiableList(mDataNodeTables);
    }

    @Override
    public DataNodeTable getDataNodeTable(DataNode dataNode)
    {
        for (DataNodeTable dnTable : mDataNodeTables)
        {
            if (dnTable.getDataNode().equals(dataNode))
            {
                return dnTable;
            }
        }
        return null;
    }

    @Override
    public String getTableName() 
    {
        return mTableName;
    }

    @Override
    public synchronized LogicalSchema getSchema() 
    {
        if (mLogicalSchema == null)
        {
            List<Attribute> attributes = 
                    mDataDictionary.getAttributes(mTableName);
            mLogicalSchema = new SimpleLogicalSchema(mTableName, attributes);
        }
        return mLogicalSchema;
    }

    @Override
    public synchronized PhysicalSchema getPhysicalSchema() 
    {
        if (mPhysicalSchema == null)
        {
            List<Attribute> attributes = 
                    mDataDictionary.getAttributes(mTableName);
            CardinalityStatistics cardStats = 
                new MetadataServiceCardinalityStatistics(
                        attributes, mDataDictionary.getStatisticsService());
            SimpleStatisticsPhysicalSchema physicalSchema = 
                new SimpleStatisticsPhysicalSchema(mTableName, 0);
            physicalSchema.setCardinalityStatistics(cardStats);
            mPhysicalSchema = physicalSchema;
        }
        return mPhysicalSchema;
    }    

}
