package uk.org.ogsadai.dqp.lqp.cardinality;

import uk.org.ogsadai.dqp.common.PhysicalSchema;

public interface StatisticsPhysicalSchema extends PhysicalSchema
{
    /**
     * Gets the cardinality statistics.
     * 
     * @return cardinality statistics data structure.  This data structure
     *         must not be altered.
     */
    CardinalityStatistics getCardinalityStatistics();

}
