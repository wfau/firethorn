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

import uk.ac.roe.wfau.firethorn.mallard.DataService;

/**
 * Bean wrapper to enable the JSON converter to process list of a DataServices.  
 *
 */
public class DataServiceBeanIterable
implements Iterable<DataServiceBean>
    {
    /**
     * Our Iterable list of DataServices.
     * 
     */
    private Iterable<DataService> iterable ;

    /**
     * Public constructor.
     * @param iterable
     *      The Iterable list of DataServices to wrap.
     *
     */
    public DataServiceBeanIterable(Iterable<DataService> iterable)
        {
        this.iterable = iterable ;
        }
    
    @Override
    public Iterator<DataServiceBean> iterator()
        {
        return new Iterator<DataServiceBean>()
            {
            private Iterator<DataService> iterator = iterable.iterator();
            @Override
            public boolean hasNext()
                {
                return iterator.hasNext();
                }
            @Override
            public DataServiceBean next()
                {
                return new DataServiceBean(
                    iterator.next()
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