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
package uk.ac.roe.wfau.firethorn.meta.base;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 *
 *
 */
@Access(
    AccessType.FIELD
    )
@Embeddable
@Deprecated
public abstract class BaseValueEntity<ValueType>
    implements BaseValue<ValueType>
    {

    public void BaseValue()
        {
        }

    public void BaseValue(final ValueType value)
        {
        this.base(
            value
            );
        }

    @Override
    public ValueType value()
        {
        if (this.user() != null)
            {
            return this.user();
            }
        else {
            return this.base();
            }
        }

    @Override
    public abstract ValueType user() ;
    @Override
    public abstract void user(final ValueType value) ;

    @Override
    public abstract ValueType base() ;
    @Override
    public abstract void base(final ValueType value) ;

    }
