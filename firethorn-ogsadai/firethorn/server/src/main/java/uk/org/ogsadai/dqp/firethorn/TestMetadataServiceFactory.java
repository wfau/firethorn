package uk.org.ogsadai.dqp.firethorn;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.dqp.common.RequestDetails;

public class TestMetadataServiceFactory implements MetadataServiceFactory
{
    
    private TestMetadataService mTestMetadataService;

    public TestMetadataServiceFactory() 
    {
        mTestMetadataService = new TestMetadataService();
    }

    @Override
    public AttributeService getAttributeService(RequestDetails details)
    {
        return mTestMetadataService;
    }

    @Override
    public TableMappingService getTableMappingService(RequestDetails details) 
    {
        return mTestMetadataService;
    }

    @Override
    public StatisticsService getStatisticsService(RequestDetails details) 
    {
        return mTestMetadataService;
    }

}
