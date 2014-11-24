/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Wrapper for {@link ResultSetIterator} that can filter the results.
 *
 */
public abstract class ResultSetFilterator<EntityType>
extends ResultSetIterator<EntityType>
implements Iterator<EntityType>
    {
    /**
     * An abstract factory class to wrap a {@link ResultSet} as an {@link Iterable}.
     * 
     */
    public abstract static class Factory<EntityType>
    extends ResultSetIterator.Factory<EntityType>
    implements Iterable<EntityType>
        {
        /**
         * Public constructor.
         * 
         */
        public Factory(final ResultSet results)
            {
            super(results);
            }
        
        @Override
        public Iterator<EntityType> iterator()
            {
            return new ResultSetFilterator<EntityType>(results())
                {
                @Override
                protected EntityType getNext()
                    throws SQLException
                    {
                    return Factory.this.getNext();
                    }
                };
            }
        }
    
    /**
     * Abstract method to get the next EntityType.
     *
     */
    protected abstract EntityType getNext()
    throws SQLException; 

    /**
     * Public constructor.
     *
     */
    public ResultSetFilterator(final ResultSet results)
        {
        super(results);
        }

    /**
     * Our next result.
     * 
     */
    private EntityType next ;

    /**
     * Our previous result.
     * 
     */
    private EntityType prev ;

    @Override
    public boolean hasNext()
        {
        while ((super.hasNext()) && (this.next == null)) 
            {
            this.next = super.next();
            }
        return (this.next != null) ;
        }

    @Override
    public EntityType next()
        {
        this.hasNext();
        
        this.prev = this.next ;
        this.next = null ;

        if (this.prev != null)
            {
            return this.prev ;
            }
        else {
            throw new NoSuchElementException();
            }
        }
    }
