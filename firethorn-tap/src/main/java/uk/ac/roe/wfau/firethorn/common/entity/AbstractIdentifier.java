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
package uk.ac.roe.wfau.firethorn.common.entity ;

import java.io.Serializable;

/**
 * Generic implementation of the Identifier interface.
 *
 */
public abstract class AbstractIdentifier<T extends Serializable>
implements Identifier
    {

    public AbstractIdentifier(final T value)
        {
        this.value = value ;
        }

    private final T value ;

    @Override
    public Serializable value()
        {
        return this.value ;
        }

    @Override
    public boolean equals(final Object that)
        {
        if (that != null)
            {
            if (this == that)
                {
                return true ;
                }
            if (that instanceof Identifier)
                {
                return this.value().equals(
                    ((Identifier)that).value()
                    );
                }
            }
        return false ;
        }

    @Override
    public String toString()
        {
        if (null != this.value())
            {
            return this.value().toString();
            }
        else {
            return null ;
            }
        }

    /*
     * Not yet ....
    @Override
    public URI toUri()
        {
        return null;
        }
     */
    }

