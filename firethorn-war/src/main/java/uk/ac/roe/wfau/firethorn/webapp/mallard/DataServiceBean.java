/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.webapp.mallard;

import java.net.URI;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.mallard.DataService;

/**
 * Bean wrapper to enable the JSON converter to process a DataService.  
 *
 */
@Slf4j
public class DataServiceBean
    {

    /**
     * Our date time formatter.
     * @todo Move this much further back.
     * 
     */
    private DateTimeFormatter formatter = ISODateTimeFormat.dateHourMinuteSecondFraction().withZoneUTC()  ; 
    
    /**
     * The data type identifier.
     * 
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json"
        );
    
    /**
     * The wrapped DataService.
     * 
     */
    private DataService service ;

    /**
     * The DataService access URL.
     * 
     */
    private URL url ;
    
    /**
     * 
     * Public constructor.
     * @param builder
     *      A UrlBuilder for generating the service URL.
     * @param service
     *      The DataService to wrap.
     *
     */
    public DataServiceBean(URL url, DataService service)
        {
        this.url = url ;
        this.service = service ;
        }

    public String getId()
        {
        return service.ident().toString();
        }

    public URL getIdent()
        {
        return url;
        }

    public URI getType()
        {
        return TYPE_URI ;
        }

    public String getName()
        {
        return service.name();
        }
    
    public String getCreated()
        {
        return formatter.print(
            new DateTime(
                service.created()
                )
            );
        }
    
    public String getModified()
        {
        return formatter.print(
            new DateTime(
                service.modified()
                )
            );
        }
    }
