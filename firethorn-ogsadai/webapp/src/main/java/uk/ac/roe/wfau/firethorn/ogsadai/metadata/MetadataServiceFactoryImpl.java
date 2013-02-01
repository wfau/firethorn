/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.metadata ;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;

import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.firethorn.MetadataServiceFactory;

class MetadataServiceFactoryImpl
implements MetadataServiceFactory
    {
    /**
     * Our debug logger.
     * 
     */
    private static Log log = LogFactory.getLog(MetadataServiceFactoryImpl.class);

    private String name ;
    
    /**
     * Public constructor.
     *
     */
    public MetadataServiceFactoryImpl(String name)
        {
        log.debug("MetadataServiceFactory() [" + name + "]");
        this.name = name ;
        }


    /**
     * Returns an attribute service.
     * 
     * @param details
     *            request details
     * @return attribute service for the request
     */
    public AttributeService getAttributeService(RequestDetails details)
        {
        log.debug("getAttributeService() [" + details + "]");
        return null ;
        }
    
    /**
     * Returns a table mapping service.
     * 
     * @param details
     *            request details
     * @return table mapping service for the request
     */
    public TableMappingService getTableMappingService(RequestDetails details)
        {
        log.debug("getTableMappingService() [" + details + "]");
        return null ;
        }
    
    /**
     * Returns a statistics service.
     * 
     * @param details
     *            request details
     * @return statistics service for the request
     */
    public StatisticsService getStatisticsService(RequestDetails details)
        {
        log.debug("getStatisticsService() [" + details + "]");
        return null ;
        }
    
    }

