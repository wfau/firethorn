package uk.org.ogsadai.dqp.firethorn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private Set<EvaluationNode> mEvaluationNodes = new HashSet<EvaluationNode>();
    private Map<String, DataNode> mDataNodes = new HashMap<String, DataNode>();
    private EvaluationNode mLocalNode;
    private FunctionRepository mFunctionRepository;
    private MetadataServiceFactory mMetadataServiceFactory;

    @Override
    public Set<EvaluationNode> getEvaluationNodes()
    {
        return mEvaluationNodes;
    }

    @Override
    public Set<DataNode> getDataNodes() 
    {
        return new HashSet<DataNode>(mDataNodes.values());
    }

    @Override
    public EvaluationNode getLocalNode()
    {
        return mLocalNode;
    }

    @Override
    public DataDictionary getDataDictionary(RequestDetails requestDetails) 
    {
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
    
    protected Map<String, DataNode> getDataNodesMap()
    {
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
        mDataNodes.clear();
        mDataNodes.putAll(dataNodes);
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
