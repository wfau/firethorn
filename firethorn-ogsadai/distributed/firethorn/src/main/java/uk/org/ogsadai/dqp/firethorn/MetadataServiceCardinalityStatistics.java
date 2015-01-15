package uk.org.ogsadai.dqp.firethorn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityStatistics;

/**
 * An implementation of cardinality statistics for a set of attributes which
 * retrieves the attribute statistics on request from a statistics service.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class MetadataServiceCardinalityStatistics implements CardinalityStatistics
{
    private StatisticsService mStatisticsService;
    private List<Attribute> mAttributes;

    public MetadataServiceCardinalityStatistics(
            List<Attribute> attributes,
            StatisticsService statisticsService)
    {
        mStatisticsService = statisticsService;
        mAttributes = attributes;
    }

    @Override
    public double getCardinality()
    {
        if (mAttributes.isEmpty())
        {
            return 0;
        }
        else
        {
            Attribute attr = mAttributes.get(0);
            AttributeStatistics stats = mStatisticsService.getStatistics(attr);
            if (stats == null)
            {
                throw new RuntimeException("No statistics found for " + attr);
            }
            return stats.getNumRows();
        }
    }

    @Override
    public boolean contains(Attribute attr) 
    {
        return AttributeUtils.containsMatching(
                attr, mAttributes, AttributeMatchMode.NAME_AND_NULL_SOURCE);
    }

    @Override
    public AttributeStatistics getStatistics(Attribute attr) 
    {
        return mStatisticsService.getStatistics(attr);
    }

    @Override
    public Map<Attribute, AttributeStatistics> getStatistics() 
    {
        Map<Attribute, AttributeStatistics> result = 
            new HashMap<Attribute, AttributeStatistics>();
        for (Attribute attr : mAttributes)
        {
            result.put(attr, getStatistics(attr));
        }
        return result;
    }
    
    @Override
    public String toString() 
    {
        return "MetadataServiceCardinalityStatistics[attributes=" + 
                mAttributes +
                ", service=" + mStatisticsService + "]";
    }
    
}
