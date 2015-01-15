package uk.org.ogsadai.dqp.firethorn;

import java.util.HashMap;
import java.util.Map;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.resource.dataresource.dqp.DQPFederation;

/**
 * Wrapper for the DQP table schema fetcher.
 *  
 * @author The OGSA-DAI Project Team.
 */
public class CachedMetadataServiceFactory implements MetadataServiceFactory
{

    private CachedMetadataService mService;
    private DQPFederation mDQPFederation;
    private Map<String, DataNode> mDataNodes = new HashMap<String, DataNode>();

    @Override
    public AttributeService getAttributeService(RequestDetails details) 
    {
        initialise(details);
        return mService;
    }

    @Override
    public TableMappingService getTableMappingService(RequestDetails details) 
    {
        initialise(details);
        return mService;
    }

    @Override
    public StatisticsService getStatisticsService(RequestDetails details)
    {
        initialise(details);
        return mService;
    }
    
    public synchronized void initialise(RequestDetails requestDetails)
    {
        if (mService == null)
        {
            mService = 
                    new CachedMetadataService(
                            requestDetails, mDQPFederation, mDataNodes);
        } 
    }
    
    public void setFederation(DQPFederation federation)
    {
        mDQPFederation = federation;
    }
    
    public void setDataNodes(Map<String, DataNode> dataNodes)
    {
        mDataNodes.clear();
        mDataNodes.putAll(dataNodes);
    }

    

}
