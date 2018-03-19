package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.dqp.common.simple.SimplePhysicalSchema;

public class SimpleStatisticsPhysicalSchema extends SimplePhysicalSchema 
    implements StatisticsPhysicalSchema
{
    
    CardinalityStatistics mStatistics;

    public SimpleStatisticsPhysicalSchema(
            String tableName,
            String databaseName,
            long cardinality, 
            long avgRowLength,
            long dataLength) 
    {
        super(tableName, databaseName, cardinality, avgRowLength, dataLength);
    }

    public SimpleStatisticsPhysicalSchema(
            String tableName, String databaseName, long cardinality) 
    {
        this(tableName, databaseName, cardinality, -1, -1);
    }

    public SimpleStatisticsPhysicalSchema(
            String tableName, long cardinality) 
    {
        this(tableName, null, cardinality);
    }

    @Override
    public CardinalityStatistics getCardinalityStatistics() 
    {
        return mStatistics;
    }
    
    public void setCardinalityStatistics(CardinalityStatistics statistics)
    {
        mStatistics = statistics;
    }
    
    @Override
    public String toString() 
    {
        return "PhysicalSchema(" + mStatistics + ")";
    }
}
