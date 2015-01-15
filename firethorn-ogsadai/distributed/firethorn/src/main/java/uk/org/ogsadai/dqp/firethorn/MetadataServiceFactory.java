package uk.org.ogsadai.dqp.firethorn;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.TableMappingService;
import uk.org.ogsadai.dqp.common.RequestDetails;

/**
 * A factory class that creates metadata services for a particular request.
 * 
 * @author The OGSA-DAI Project Team.
 */
public interface MetadataServiceFactory 
{
    /**
     * Returns an attribute service.
     * 
     * @param details
     *            request details
     * @return attribute service for the request
     */
    AttributeService getAttributeService(RequestDetails details);
    
    /**
     * Returns a table mapping service.
     * 
     * @param details
     *            request details
     * @return table mapping service for the request
     */
    TableMappingService getTableMappingService(RequestDetails details);
    
    /**
     * Returns a statistics service.
     * 
     * @param details
     *            request details
     * @return statistics service for the request
     */
    StatisticsService getStatisticsService(RequestDetails details);

}
