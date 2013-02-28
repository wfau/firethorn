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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.firethorn.MetadataServiceFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;


class MetadataServiceFactoryImpl
implements MetadataServiceFactory
    {
    private static Log log = LogFactory.getLog(MetadataServiceFactoryImpl.class);

    /*
     *
     *
     */
    private String endpoint ;

    /*
     *
     *
     */
    public  String endpoint()
        {
        return this.endpoint ;
        }
    
    /**
     * Public constructor.
     *
     */
    public MetadataServiceFactoryImpl(String endpoint)
        {
        log.debug("MetadataServiceFactoryImpl(String)");
        log.debug("  Endpoint [" + endpoint + "]");
        this.endpoint = endpoint ;
        }

    @Override
    public AttributeService getAttributeService(RequestDetails request)
        {
        log.debug("getAttributeService(RequestDetails)");
        log.debug("  Request [" + request + "]");
        return new CachingAttributeServiceImpl(
            this.endpoint,
            request
            );
        }
    
    @Override
    public TableMappingService getTableMappingService(RequestDetails request)
        {
        log.debug("getTableMappingService(RequestDetails)");
        log.debug("  Request [" + request + "]");
        return new CachingTableMappingServiceImpl(
            this.endpoint,
            request
            );
        }
    
    @Override
    public StatisticsService getStatisticsService(RequestDetails request)
        {
        log.debug("getStatisticsService(RequestDetails)");
        log.debug("  Request [" + request + "]");
        return new StatisticsServiceImpl(
            this.endpoint,
            request
            );
        }
    }

