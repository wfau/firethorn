package uk.org.ogsadai.dqp.firethorn;

import java.util.HashMap;
import java.util.Map;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMapping;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;

public class TestMetadataService 
implements AttributeService, StatisticsService, TableMappingService
{

    private Map<String, TableMapping> mTableMappings;
    private Map<String, Map<String, Attribute>> mTables;
    private Map<String, Map<String, AttributeStatistics>> mStatistics;

    @Override
    public AttributeStatistics getStatistics(Attribute attribute)
    {
        return getStatistics(attribute.getSource(), attribute.getName());
    }

    @Override
    public AttributeStatistics getStatistics(String source, String name) 
    {
        return mStatistics.get(source).get(name);
    }

    @Override
    public Iterable<Attribute> getAttributes(String source) 
    {
        return mTables.get(source).values();
    }

    @Override
    public Attribute getAttribute(String source, String name) 
    {
        return mTables.get(source).get(name);
    }

    @Override
    public TableMapping getTableMapping(String source) 
    {
        return mTableMappings.get(source);
    }
    
    public void add(Attribute attribute)
    {
        Map<String, Attribute> table = mTables.get(attribute.getSource());
        if (table == null)
        {
            table = new HashMap<String, Attribute>();
            mTables.put(attribute.getSource(), table);
        }
        table.put(attribute.getName(), attribute);
    }
    
    public void add(Attribute attribute, AttributeStatistics attrStats)
    {
        Map<String, AttributeStatistics> table = 
                mStatistics.get(attribute.getSource());
        if (table == null)
        {
            table = new HashMap<String, AttributeStatistics>();
            mStatistics.put(attribute.getSource(), table);
        }
        table.put(attribute.getName(), attrStats);
    }
    
    public void add(TableMapping mapping)
    {
        mTableMappings.put(mapping.tableName(), mapping);
    }

}
