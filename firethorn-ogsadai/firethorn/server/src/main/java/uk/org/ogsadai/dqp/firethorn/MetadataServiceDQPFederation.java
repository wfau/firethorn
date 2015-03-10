package uk.org.ogsadai.dqp.firethorn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.rest.SimpleTableMappingServiceImpl;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.common.DataDictionary;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.udf.FunctionRepository;
import uk.org.ogsadai.dqp.presentation.common.DQPResourceConfigurationException;
import uk.org.ogsadai.resource.dataresource.dqp.DQPFederation;

public class MetadataServiceDQPFederation implements DQPFederation
{

    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(MetadataServiceDQPFederation.class);

    private Set<EvaluationNode> mEvaluationNodes = new HashSet<EvaluationNode>();
    private Map<String, DataNode> mDataNodes = new HashMap<String, DataNode>();
    private EvaluationNode mLocalNode;
    private FunctionRepository mFunctionRepository;
    private MetadataServiceFactory mMetadataServiceFactory;

    public MetadataServiceDQPFederation()
        {
        // ZRQ
        log.debug("MetadataServiceDQPFederation()");
        }
    
    @Override
    public Set<EvaluationNode> getEvaluationNodes()
    {
        log.debug("getEvaluationNodes()");
        if (mEvaluationNodes == null)
            {
            log.debug("  EvaluationNodes [null]");
            }
        else {
            for (EvaluationNode node : mEvaluationNodes)
                {
                log.debug("  EvaluationNode [" + node.toString() + "]");
                }
            }
        return mEvaluationNodes;
    }

    @Override
    public Set<DataNode> getDataNodes() 
    {
        log.debug("-------- --------");
        log.debug("<getDataNodes>");
        if (mDataNodes == null)
            {
            log.debug("  DataNodes [null]");
            }
        else {
            for (String key : mDataNodes.keySet())
                {
                log.debug("  DataNode [" + key + "][" + mDataNodes.get(key) + "]");
                }
            }
        log.debug("</getDataNodes>");
        log.debug("-------- --------");
        return new HashSet<DataNode>(mDataNodes.values());
    }

    @Override
    public EvaluationNode getLocalNode()
    {
        log.debug("getLocalNode()");
        log.debug("  LocalNode [" + mLocalNode + "]");
        return mLocalNode;
    }

    @Override
    public DataDictionary getDataDictionary(RequestDetails requestDetails) 
    {
        log.debug("-------- --------");
        log.debug("<getDataDictionary>");
        MetadataServiceDataDictionary dataDictionary = 
                new MetadataServiceDataDictionary();
        dataDictionary.setFederation(this);
        dataDictionary.setRequestDetails(requestDetails);
        dataDictionary.setTableMappingService(
                mMetadataServiceFactory.getTableMappingService(requestDetails));
        dataDictionary.setAttributeService(
                mMetadataServiceFactory.getAttributeService(requestDetails));
        dataDictionary.setStatisticsService(
                mMetadataServiceFactory.getStatisticsService(requestDetails));
        log.debug("</getDataDictionary>");
        log.debug("-------- --------");
        return dataDictionary;
    }

    @Override
    public void refreshDataDictionary(RequestDetails requestDetails) 
    {
        // nothing to do - data dictionary is being looked up for each query
    }
    
    protected FunctionRepository getFunctionRepository()
    {
        if (mFunctionRepository == null)
        {
            mFunctionRepository =   
                    (FunctionRepository) OGSADAIContext.getInstance().get(
                        FunctionRepository.FUNCTION_REPOSITORY_KEY);

        }
        return mFunctionRepository;
    }
    
    public Map<String, DataNode> getDataNodesMap()
    {
    log.debug("-------- --------");
    log.debug("<getDataNodesMap/>");
    log.debug("-------- --------");
        return mDataNodes;
    }
    
    public void setMetadataServiceFactory(MetadataServiceFactory factory)
    {
        mMetadataServiceFactory = factory;
    }
    
    /**
     * Sets the function repository for the federation. If this value is not set
     * then the function repository is looked up from the OGSA-DAI context.
     * 
     * @param functionRepository
     *            function repository
     */
    public void setFunctionRepository(FunctionRepository functionRepository)
    {
        mFunctionRepository = functionRepository;
    }
    
    /**
     * Specifies the data nodes in this federation. Data nodes are identified
     * by names that the table mapping service uses.
     * 
     * @param dataNodes
     *            data node mapping
     */
    public void setDataNodesMap(Map<String, DataNode> dataNodes)
    {
    log.debug("-------- --------");
    log.debug("<setDataNodesMap>");
        // ++ ZRQ
        log.debug("setDataNodesMap()");
        for (String key : dataNodes.keySet())
            {
            log.debug("  DataNode [" + key + "][" + dataNodes.get(key) + "]");
            }
        // -- ZRQ
        mDataNodes.clear();
        mDataNodes.putAll(dataNodes);
    log.debug("</setDataNodesMap>");
    log.debug("-------- --------");
    }
    
    /**
     * Specifies the evaluation nodes in this federation.
     * 
     * @param evaluationNodes
     *            evaluation nodes
     */
    public void setEvaluationNodes(Set<EvaluationNode> evaluationNodes)
    {
        mEvaluationNodes.clear();
        mEvaluationNodes.addAll(evaluationNodes);
        for (EvaluationNode node : mEvaluationNodes)
        {
            if (node.isLocal())
            {
                mLocalNode = node;
            }
        }
        if (mLocalNode == null)
        {
            throw new DQPResourceConfigurationException(
                    new IllegalArgumentException(
                            "At least one local evaluation node is needed."));
        }
    }
    
}
