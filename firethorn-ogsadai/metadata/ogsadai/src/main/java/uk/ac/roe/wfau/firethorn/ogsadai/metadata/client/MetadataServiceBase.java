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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.firethorn.MetadataServiceFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;

/**
 *
 *
 */
class MetadataServiceBase
    {
    private static Log log = LogFactory.getLog(MetadataServiceBase.class);

    /**
     *
     *
     */
    protected MetadataServiceBase(String endpoint, RequestDetails request)
        {
        log.debug("MetadataServiceBase(String, RequestDetails)");
        log.debug("  Endpoint [" + endpoint + "]");
        log.debug("  Request  [" + request  + "]");
        this.request  = request  ;
        this.endpoint = endpoint ;
        }

    private RequestDetails request ;
    public  RequestDetails request()
        {
        return this.request ;
        }

    private String endpoint ;
    public  String endpoint()
        {
        return this.endpoint ;
        }

    /**
     *  
     * 
     */
    public void debug(RequestDetails request)
        {
        log.debug("RequestDetails ----");
        log.debug("  Resource ID [" + request.getResourceID() + "]");
        log.debug("  Request resource  [" + request.getRequestResource().toString() + "]");
        log.debug("  Security context  [" + request.getSecurityContext().toString() + "]");
        log.debug("  Activity instance [" + request.getActivityInstanceName().toString() + "]");
        log.debug("----");
        }
    }


