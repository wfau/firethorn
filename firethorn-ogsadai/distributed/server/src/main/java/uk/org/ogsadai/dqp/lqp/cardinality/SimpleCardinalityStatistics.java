package uk.org.ogsadai.dqp.lqp.cardinality;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeMatchMode;
import uk.org.ogsadai.dqp.lqp.AttributeUtils;

public class SimpleCardinalityStatistics
    implements CardinalityStatistics
{
    private Map<Attribute,AttributeStatistics> mStatistics;
    
    public SimpleCardinalityStatistics()
    {
        mStatistics = new HashMap<Attribute, AttributeStatistics>();
    }

    public void addAttributeStatistics(
        Attribute attr, AttributeStatistics attrStatistics)
    {
        mStatistics.put(attr, attrStatistics);
    }
    
    @Override
    public double getCardinality()
    {
        return mStatistics.values().iterator().next().getNumRows();
    }
    
    @Override
    public boolean contains(Attribute attr)
    {
        return AttributeUtils.containsMatching(
                attr, mStatistics.keySet(), 
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
    }
    
    @Override
    public AttributeStatistics getStatistics(Attribute attr)
    {
        List<Attribute> matchingAttrs =
            AttributeUtils.getMatching(
                attr, mStatistics.keySet(), 
                AttributeMatchMode.NAME_AND_NULL_SOURCE);
        
        if (matchingAttrs.size() == 1)
        {
            return mStatistics.get(matchingAttrs.iterator().next());
        }
        else
        {
            throw new RuntimeException(
                "Unexpected number of matching attributes: " + 
                matchingAttrs.size() + " for attribute: " + 
                attr.getSource() + "." + attr.getName() + ", " + attr.getType());
        }
    }

    /**
     * Gets the attribute to attribute statistics map.  This method should only 
     * be used by test classes to validate the statistics produced.
     * 
     * @return the attribute to statistics map.
     */
    public Map<Attribute,AttributeStatistics> getStatistics()
    {
        return mStatistics;
    }
    
    public String toString()
    {
        return mStatistics.toString();
    }

}
