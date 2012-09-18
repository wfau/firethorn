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

import java.net.URL;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import uk.ac.roe.wfau.firethorn.mallard.DataService;
import uk.ac.roe.wfau.firethorn.webapp.control.UrlBuilder;

/**
 * Bean wrapper to enable the JSON converter to process a DataService.  
 *
 */
public class DataServiceBean
    {

    private DateTimeFormatter formatter = ISODateTimeFormat.dateHourMinuteSecondFraction().withZoneUTC()  ; 
    
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
    
    public URL getIdent()
        {
        return url;
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
