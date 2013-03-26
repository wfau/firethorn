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
package uk.ac.roe.wfau.firethorn.ogsadai.metadata.client.mock ;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.metadata.AttributeService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.StatisticsService;
import uk.ac.roe.wfau.firethorn.ogsadai.metadata.TableMappingService;

import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.firethorn.MetadataServiceFactory;


class MetadataServiceFactoryMock
implements MetadataServiceFactory
    {
    private static Log log = LogFactory.getLog(MetadataServiceFactoryMock.class);

    private String name ;
    
    /**
     * Public constructor.
     *
     */
    public MetadataServiceFactoryMock(String name)
        {
        log.debug("MetadataServiceFactoryMock() [" + name + "]");
        this.name = name ;
        this.tables = new TableMappingServiceMock();
        this.tables.put(
            "twomass",
            "table20",
            //"twomass_psc"
            "TWOMASS.dbo.twomass_psc"
            );
        this.tables.put(
            "ukidss",
            "table21",
            "gcsPointSource"
            //"UKIDSSDR5PLUS.dbo.gcsPointSource"
            );
        this.tables.put(
            "ukidss",
            "table22",
            //"gcsSourceXtwomass_psc"
            "UKIDSSDR5PLUS.dbo.gcsSourceXtwomass_psc"
            );

        this.attrib = new AttributeServiceMock();
        this.attrib.add(
            "ra",
            TupleTypes._DOUBLE,
            "table20",
            false
            );        
        this.attrib.add(
            "dec",
            TupleTypes._DOUBLE,
            "table20",
            false
            );        
        this.attrib.add(
            "pts_key",
            TupleTypes._LONG,
            "table20",
            true
            );        

        this.attrib.add(
            "ra",
            TupleTypes._DOUBLE,
            "table21",
            false
            );        
        this.attrib.add(
            "dec",
            TupleTypes._DOUBLE,
            "table21",
            false
            );        
        this.attrib.add(
            "sourceID",
            TupleTypes._LONG,
            "table21",
            true
            );        

        this.attrib.add(
            "masterObjID",
            TupleTypes._LONG,
            "table22",
            true
            );        
        this.attrib.add(
            "slaveObjID",
            TupleTypes._LONG,
            "table22",
            true
            );        
        this.attrib.add(
            "distanceMins",
            TupleTypes._DOUBLE,
            "table22",
            false
            );        

        this.stats = new StatisticsServiceMock();

        }

    private TableMappingServiceMock tables ;
    private AttributeServiceMock    attrib ;
    private StatisticsServiceMock   stats ;

    @Override
    public AttributeService getAttributeService(RequestDetails details)
        {
        log.debug("getAttributeService() [" + details + "]");
        log(details);
        return this.attrib;
        }
    
    @Override
    public TableMappingService getTableMappingService(RequestDetails details)
        {
        log.debug("getTableMappingService() [" + details + "]");
        log(details);
        return this.tables;
        }
    
    @Override
    public StatisticsService getStatisticsService(RequestDetails details)
        {
        log.debug("getStatisticsService() [" + details + "]");
        log(details);
        return this.stats;
        }

   /**
    *  
    * 
    */
    public void log(RequestDetails details)
        {
        log.debug("RequestDetails ----");
        log.debug("  Resource ID [" + details.getResourceID() + "]");
        log.debug("  request resource  [" + details.getRequestResource().toString() + "]");
        log.debug("  Security context  [" + details.getSecurityContext().toString() + "]");
        log.debug("  Activity instance [" + details.getActivityInstanceName().toString() + "]");
        log.debug("----");
        }
    }

