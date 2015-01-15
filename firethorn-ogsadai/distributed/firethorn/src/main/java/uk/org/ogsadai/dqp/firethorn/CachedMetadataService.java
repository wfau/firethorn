package uk.org.ogsadai.dqp.firethorn;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMapping;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.PhysicalSchema;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.common.TableSchema;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.cardinality.AttributeStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityStatistics;
import uk.org.ogsadai.dqp.lqp.cardinality.CardinalityUtils;
import uk.org.ogsadai.dqp.lqp.cardinality.StatisticsPhysicalSchema;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.resource.dataresource.dqp.DQPFederation;

public class CachedMetadataService
implements AttributeService, TableMappingService, StatisticsService 
{
    
    private static final DAILogger LOG = 
            DAILogger.getLogger(CachedMetadataService.class);

    private Map<String, TableMapping> mTableMappings = 
            new HashMap<String, TableMapping>();
    private Map<String, CardinalityStatistics> mStatistics = 
            new HashMap<String, CardinalityStatistics>();
    private Map<String, List<Attribute>> mAttributes = 
            new HashMap<String, List<Attribute>>();
    private DataDictionary mDataDictionary;

    public CachedMetadataService(
            RequestDetails details,
            DQPFederation federation, 
            Map<String, DataNode> dataNodes)
    {
        mDataDictionary = federation.getDataDictionary(details);
        Map<DataNode, String> dataNodeIdentifiers = createDataNodeIdentifier(dataNodes);
        for (TableSchema tableSchema : mDataDictionary.getTableSchemas())
        {
            String tableName = tableSchema.getTableName();
            LOG.debug("Retrieving table schema for '" + tableName + "'");
            List<Attribute> attributes = tableSchema.getSchema().getAttributes();
            mAttributes.put(tableName, attributes);
            LOG.debug("Attributes: " + attributes);
            // choose the first data node that can access the table
            if (tableSchema.getDataNodeTables().isEmpty())
            {
                throw new RuntimeException(
                        "Table " + tableName + " has no data node.");
            }
            DataNode dataNode = 
                    tableSchema.getDataNodeTables().get(0).getDataNode();
            String localName = null;
            try
            {
                localName = mDataDictionary.getOriginalTableName(
                        tableName, dataNode);
            }
            catch (TableNotFoundException e)
            {
                // this shouldn't happen
                throw new RuntimeException(e);
            }
            TableMapping tableMapping = 
                    new SimpleTableMapping(
                            tableName, 
                            localName, 
                            dataNodeIdentifiers.get(dataNode));
            LOG.debug("Adding table mapping: " + tableMapping);
            
            mTableMappings.put(tableName, tableMapping);
            initStatistics(tableSchema, attributes);
        }
    }

    @Override
    public AttributeStatistics getStatistics(Attribute attribute) 
    {
        CardinalityStatistics cardStats = mStatistics.get(attribute.getSource());
        if (cardStats == null)
        {
            LOG.debug("Table statistics not found: " + attribute.getSource());
            return null;
        }
        return cardStats.getStatistics(attribute);
    }

    @Override
    public AttributeStatistics getStatistics(String source, String name)
    {
        Attribute attribute = new AttributeImpl(name, source);
        return getStatistics(attribute);
    }

    @Override
    public TableMapping getTableMapping(String source)
    {
        return mTableMappings.get(source);
    }

    @Override
    public Iterable<Attribute> getAttributes(String source) 
    {
        List<Attribute> result = mAttributes.get(source);
        if (result == null)
        {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public Attribute getAttribute(String source, String name)
    {
        List<Attribute> attributes = mAttributes.get(source);
        if (attributes != null)
        {
            for (Attribute attribute : attributes)
            {
                if (attribute.getName().equals(name))
                {
                    return attribute;
                }
            }
        }
        return null;
    }
    
    private void initStatistics(
            TableSchema tableSchema,
            List<Attribute> attributes)
    {
        LOG.debug("Adding statistics for '" + tableSchema.getTableName() + "'.");
        CardinalityStatistics cardStats;
        // Go to physical data dictionary and get table scan statistics
        PhysicalSchema physicalSchema = tableSchema.getPhysicalSchema();
        
        if (physicalSchema == null)
        {
            // make up a schema
            LOG.debug("Adding standard schema.");
            cardStats = CardinalityUtils.makeStatisticsFromHeading(
                    attributes, 100000, 10000);
        }
        else if (!(physicalSchema instanceof StatisticsPhysicalSchema))
        {
            long cardinality = physicalSchema.getCardinality();
            LOG.debug("Adding simple schema with cardinality=" + cardinality);
            // Convert simple physical schema into statistics schema.
            cardStats = CardinalityUtils.makeStatisticsFromHeading(
                    attributes, 
                    cardinality, 
                    cardinality/10.0);
        }
        else
        {
            LOG.debug("Found statistics schema.");
            StatisticsPhysicalSchema statsPhysicalSchema =
                    (StatisticsPhysicalSchema) physicalSchema;
                        
            cardStats = statsPhysicalSchema.getCardinalityStatistics();
        }
        mStatistics.put(tableSchema.getTableName(), cardStats);
    }

    /**
     * Creates an identifier for the data node.
     * 
     * @param dataNode
     *            data node
     * @return unique identifier for the data node
     */
    private Map<DataNode, String> createDataNodeIdentifier(
            Map<String, DataNode> dataNodes) 
    {
        Map<DataNode, String> result = new HashMap<DataNode, String>();
        for (Entry<String, DataNode> entry : dataNodes.entrySet())
        {
            result.put(entry.getValue(), entry.getKey());
        }
        return result;
    }

//    private List<Attribute> createLocalAttributes(
//            String localTableName, List<Attribute> attributes) 
//    {
//        List<Attribute> result = new ArrayList<Attribute>(attributes.size());
//        for (Attribute attr : attributes)
//        {
//            result.add(new AttributeImpl(attr.getName(), localTableName));
//        }
//        return result;
//    }

}
