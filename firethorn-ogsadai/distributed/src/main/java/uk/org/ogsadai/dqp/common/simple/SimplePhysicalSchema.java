// Copyright (c) The University of Edinburgh, 2008.
// See OGSA-DAI-Licence.txt for licencing information.

package uk.org.ogsadai.dqp.common.simple;

import uk.org.ogsadai.dqp.common.PhysicalSchema;

/**
 * TODO: doc
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SimplePhysicalSchema implements PhysicalSchema
{
    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = 
        "Copyright (c) The University of Edinburgh, 2008";

    private long mAvgRowLength = -1;
    
    private long mCardinality = -1;
    
    private long mDataLength = -1;
    
    private String mDatabaseName;
    
    private String mTableName;

    /**
     * Constructor. 
     * 
     * @param tableName
     * @param databaseName
     * @param cardinality
     * @param avgRowLength
     * @param dataLength
     */
    public SimplePhysicalSchema(String tableName, String databaseName,
        long cardinality, long avgRowLength, long dataLength)
    {
        mTableName = tableName;
        mDatabaseName = databaseName;
        mCardinality = cardinality;
        mAvgRowLength = avgRowLength;
        mDataLength = dataLength;
    }
    
    /**
     * {@inheritDoc}
     */
    public long getAverageRowLength()
    {
        return mAvgRowLength;
    }

    /**
     * {@inheritDoc}
     */
    public long getCardinality()
    {
        return mCardinality;
    }

    /**
     * {@inheritDoc}
     */
    public long getDataLength()
    {
        return mDataLength;
    }

    /**
     * {@inheritDoc}
     */
    public String getDatabaseName()
    {
        return mDatabaseName;
    }

    /**
     * {@inheritDoc}
     */
    public String getTableName()
    {
        return mTableName;
    }
}
