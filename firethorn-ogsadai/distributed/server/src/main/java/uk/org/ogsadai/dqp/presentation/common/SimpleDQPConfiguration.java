package uk.org.ogsadai.dqp.presentation.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;

public class SimpleDQPConfiguration implements DQPResourceConfiguration 
{

    private static final Logger LOG = Logger.getLogger(SimpleDQPConfiguration.class);
    
    private Set<EvaluationNode> mEvaluationNodes = new HashSet<EvaluationNode>();
    private Set<DataNode> mDataNodes = new HashSet<DataNode>();
    private EvaluationNode mLocalNode;
    private TableSchemaFetcher mTableSchemaFetcher;
    /** Indicates whether physical schemas are fetched. Default is true. */
    private boolean mFetchPhysicalSchemas = true;

    @Override
    public Set<EvaluationNode> getEvaluationNodes() 
    {
        return mEvaluationNodes;
    }

    @Override
    public Set<DataNode> getDataNodes()
    {
        return mDataNodes;
    }

    @Override
    public EvaluationNode getLocalNode() 
    {
        return mLocalNode;
    }

    @Override
    public TableSchemaFetcher getTableSchemaFetcher() 
    {
        return mTableSchemaFetcher;
    }
    
    public void setTableSchemaFetcher(TableSchemaFetcher fetcher)
    {
        mTableSchemaFetcher = fetcher;
    }
    
    public void setFetchPhysicalSchemas(boolean fetch)
    {
        mFetchPhysicalSchemas = fetch;
    }
    
    public void setDataNodes(Set<DataNode> dataNodes)
    {
        mDataNodes.clear();
        mDataNodes.addAll(dataNodes);
        for (DataNode dataNode : dataNodes)
        {
            mEvaluationNodes.add(dataNode.getEvaluationNode());
        }
    }
    
    public void setEvaluationNodes(Set<EvaluationNode> evaluationNodes)
    {
        mEvaluationNodes.addAll(evaluationNodes);
    }

    /**
     * Validates configuration. At least one local evaluation node is expected.
     * Resource name clashes are also being detected.
     */
    public void validate()
    {
        LOG.debug("Evaluating configuration.");
        
        // check if there is at least one local evaluation node
        boolean local = false;
        for(EvaluationNode en : mEvaluationNodes)
        {
            if (en.isLocal())
            {
                local = true;
                mLocalNode = en;
            }
        }
        
        if(!local)
        {
            throw new DQPResourceConfigurationException(new Throwable(
                "At least one local evaluation node is needed."));
        }
        
        // check for name clashes
        Set<String> resourceNames = new HashSet<String>();
        for(DataNode dn : mDataNodes)
        {
            if (resourceNames.contains(dn.getTableNamePrefix()))
            {
                throw new DQPResourceConfigurationException(new Throwable(
                    "There is a clash in resource names"));
            }
            else
            {
                resourceNames.add(dn.getTableNamePrefix());
            }
        }
        
        // use the default schema fetcher if none is provided
        if (mTableSchemaFetcher == null)
        {
            mTableSchemaFetcher = new SimpleTableSchemaFetcher();
        }
        
        LOG.debug("DQP federation is valid.");
    }

    @Override
    public String toString() 
    {
        StringBuilder builder = new StringBuilder();
        builder.append("DQPFederationBean\n");
        builder.append("  Data Nodes: ");
        for (DataNode node : mDataNodes)
        {
            builder.append("\n    ");
            builder.append(node);
        }
        builder.append("\n");
        builder.append("  Evaluation Nodes: ");
        for (EvaluationNode node : mEvaluationNodes)
        {
            builder.append("\n    ");
            builder.append(node);
        }
        builder.append("\n");
        builder.append("  Table schema fetcher:   ");
        builder.append(mTableSchemaFetcher.getClass().getName());
        builder.append("\n");
        builder.append("  Fetch physical schemas: ");
        builder.append(mFetchPhysicalSchemas);
        return builder.toString();
    }

}
