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

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.webapp.control.UriBuilder;
import uk.ac.roe.wfau.firethorn.webapp.control.UrlBuilder;

/**
 * Bean wrapper to enable the JSON converter to process list of a DataServices.  
 *
 */
public class AdqlServiceBeanIter
implements Iterable<AdqlServiceBean>
    {
    
    /**
     * The Iterable list of AdqlServices.
     * 
     */
    private Iterable<AdqlService> iterable ;

    /**
     * UrlBuilder for generating the service URL.
     * 
     */
    private UriBuilder.AdqlUriBuilder builder ;
    
    /**
     * Public constructor.
     * @param builder
     *      An ADQL UrlBuilder for generating the target URLs.
     * @param iterable
     *      The Iterable list to wrap.
     *
     */
    public AdqlServiceBeanIter(UriBuilder.AdqlUriBuilder builder, Iterable<AdqlService> iterable)
        {
        this.builder  = builder  ;
        this.iterable = iterable ;
        }
    
    @Override
    public Iterator<AdqlServiceBean> iterator()
        {
        return new Iterator<AdqlServiceBean>()
            {
            private Iterator<AdqlService> iterator = iterable.iterator();
            @Override
            public boolean hasNext()
                {
                return iterator.hasNext();
                }
            @Override
            public AdqlServiceBean next()
                {
                AdqlService service = iterator.next(); 
                return new AdqlServiceBean(
                    builder.uri(
                        service
                        ),
                    service
                    );
                }
            @Override
            public void remove()
                {
                iterator.remove();
                }
            };
        }
    }