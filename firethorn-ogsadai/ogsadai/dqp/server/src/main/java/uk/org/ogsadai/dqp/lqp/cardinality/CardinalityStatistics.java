package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.Map;

import uk.org.ogsadai.dqp.lqp.Attribute;

/**
 * Cardinality statistics for a relation. It contains the cardinality of the
 * relation and also statistics for each attribute of the relation.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface CardinalityStatistics
{
    /**
     * Gets the cardinality of the associated operator.
     * 
     * @return cardinality
     */
    double getCardinality();

    /**
     * Does the statistics contain data for the given attribute.
     * 
     * @param attr  attribute 
     * 
     * @return <tt>true</tt> if data for the attribute is available, 
     *         <tt>false</tt> otherwise.
     */
    boolean contains(Attribute attr);
    
    /**
     * Gets the statistics for the given attribute.
     * 
     * @param attr attribute
     * 
     * @return statistics for the given attribute
     * 
     * @throws RuntimeException 
     *   if the give attribute does not uniquely identify an attribute
     *   for which data is stored.
     */
    AttributeStatistics getStatistics(Attribute attr);
    
    /**
     * Returns the mapping from attribute to attribute statistics. This mapping
     * should be treated as read only.
     * 
     * @return mapping from attribute to attribute statistics.
     */
    Map<Attribute, AttributeStatistics> getStatistics();
}
