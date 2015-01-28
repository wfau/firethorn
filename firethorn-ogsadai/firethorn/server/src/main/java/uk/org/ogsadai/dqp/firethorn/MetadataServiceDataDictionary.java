package uk.org.ogsadai.dqp.firethorn;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMapping;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.common.TableSchema;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.exceptions.AttributeNotFoundException;
import uk.org.ogsadai.dqp.lqp.exceptions.TableNotFoundException;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;

public class MetadataServiceDataDictionary implements DataDictionary
{
    /** Logger. */
    private static final DAILogger LOG = 
        DAILogger.getLogger(MetadataServiceDataDictionary.class);

    /** The DQP federation. */
    private MetadataServiceDQPFederation mFederation;
    /** Request details of the client. Not used at the moment. */
    private RequestDetails mRequestDetails;
    private TableMappingService mTableMappingService;
    private AttributeService mAttributeService;
    private StatisticsService mStatisticsService;

    public MetadataServiceDataDictionary()
        {
        // ZRQ
        LOG.debug("MetadataServiceDataDictionary()");
        }
    
    @Override
    public Heading getHeading(String tableName) throws TableNotFoundException 
    {
        return new HeadingImpl(getAttributes(tableName));
    }

    @Override
    public TableSchema getTableSchema(String tableName)
            throws TableNotFoundException 
    {
        TableMapping tableMapping = 
                mTableMappingService.getTableMapping(tableName);
        if (tableMapping == null)
        {
            throw new TableNotFoundException(tableName);
        }
        // ++ ZRQ
        LOG.debug("getTableSchema()");
        LOG.debug("  TableName [" + tableName + "]");
        LOG.debug("  TableMapping name  [" + tableMapping.tableName() + "]");
        LOG.debug("  TableMapping alias [" + tableMapping.tableAlias() + "]");
        LOG.debug("  TableMapping ident [" + tableMapping.resourceIdent() + "]");

        LOG.debug("  Federation [" + mFederation + "]");
        LOG.debug("  ++ Nodes ");
        Map<String, DataNode> nodes = mFederation.getDataNodesMap();
        for (String key : nodes.keySet())
            {
            DataNode node = nodes.get(
                key
                ); 
            LOG.debug("  DataNode [" + node + "]");
            LOG.debug("  DataNode ident [" + node.getResourceID() + "]");
            LOG.debug("  DataNode EvalNode [" + node.getEvaluationNode() + "]");
            }
        LOG.debug("  -- Nodes ");
        //-- ZRQ

        MetadataServiceTableSchema tableSchema = 
                new MetadataServiceTableSchema(
                        tableName, 
                        tableMapping.tableName(), 
                        mFederation.getDataNodesMap().get(
                                tableMapping.resourceIdent()));
        tableSchema.setDataDictionary(this);
        return tableSchema;
//        List<Attribute> attributes = getAttributes(tableName);
//        CardinalityStatistics cardStats = 
//            new MetadataServiceCardinalityStatistics(
//                    attributes, mStatisticsService);
//        SimpleStatisticsPhysicalSchema physicalSchema = 
//            new SimpleStatisticsPhysicalSchema(tableName, 0);
//        physicalSchema.setCardinalityStatistics(cardStats);
//        return new SimpleTableSchema(
//                    tableMapping.tableName(), 
//                    mDataNodes.get(tableMapping.resourceIdent()), 
//                    new SimpleLogicalSchema(tableName, attributes),
//                    physicalSchema);
    }

    public List<Attribute> getAttributes(String tableName) 
    {
        List<Attribute> attributes = new LinkedList<Attribute>();
        Iterable<Attribute> iter = mAttributeService.getAttributes(tableName);
        for (Attribute attribute : iter)
        {
            attributes.add(attribute);
        }
        return attributes;
    }

    @Override
    public String getOriginalTableName(String table, DataNode dataNode)
            throws TableNotFoundException 
    {
        TableMapping mapping = mTableMappingService.getTableMapping(table);
        if (mapping == null)
        {
            throw new TableNotFoundException(table);
        }
        return mapping.tableName();
    }

    @Override
    public Attribute getAttribute(Attribute attribute)
        throws AttributeNotFoundException 
    {
        Attribute result = mAttributeService.getAttribute(
                attribute.getSource(), attribute.getName());
        if (result == null)
        {
            throw new AttributeNotFoundException(attribute);
        }
        return result;
    }

    @Override
    public Set<TableSchema> getTableSchemas() 
    {
        // cannot retrieve the names of all available tables?
        throw new UnsupportedOperationException();
    }

    @Override
    public FunctionRepository getFunctionRepository() 
    {
        return mFederation.getFunctionRepository();
    }
    
    /**
     * Returns the statistics service.
     * 
     * @return statistics service
     */
    public StatisticsService getStatisticsService() 
    {
        return mStatisticsService;
    }

    public void setRequestDetails(RequestDetails requestDetails)
    {
        mRequestDetails = requestDetails;
    }


    public void setFederation(MetadataServiceDQPFederation federation)
    {
        mFederation = federation;
    }
    
    /**
     * Sets the table mapping service which maps a table name to its data node
     * identifier and a local table name.
     * 
     * @param service
     *            table mapping service
     */
    public void setTableMappingService(TableMappingService service)
    {
        mTableMappingService = service;
    }
    
    /**
     * Set the attribute service that provides attribute metadata to the data
     * dictionary.
     * 
     * @param service
     *            attribute service
     */
    public void setAttributeService(AttributeService service)
    {
        mAttributeService = service;
    }
    
    /**
     * Set the attribute statistics service that provides attribute statistics
     * to the data dictionary.
     * 
     * @param service
     *            statistics service
     */
    public void setStatisticsService(StatisticsService service)
    {
        mStatisticsService = service;
    }
    


}
