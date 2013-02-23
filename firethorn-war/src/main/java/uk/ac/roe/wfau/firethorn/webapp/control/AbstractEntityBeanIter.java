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
package uk.ac.roe.wfau.firethorn.webapp.control;

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.entity.Entity;

/**
 * Abstract base class for <code>EntityBeanIter</code> implementations.
 *
 */
public abstract class AbstractEntityBeanIter<EntityType extends Entity>
implements EntityBean.Iter<EntityType>
    {

    /**
     * Public constructor.
     * @param iterable
     *
     */
    public AbstractEntityBeanIter(final Iterable<EntityType> iterable)
        {
        this.iterable = iterable ;
        }

    protected Iterable<EntityType> iterable ;
    protected Iterable<EntityType> iterable()
        {
        return this.iterable;
        }

    /**
     * Wrap an Entity as an EntityBean.
     *
     */
    public abstract EntityBean<EntityType> bean(final EntityType entity);

    @Override
    public Iterator<EntityBean<EntityType>> iterator()
        {
        return new Iterator<EntityBean<EntityType>>()
            {
            private final Iterator<EntityType> iterator = iterable().iterator();

            @Override
            public boolean hasNext()
                {
                return this.iterator.hasNext();
                }
            @Override
            public EntityBean<EntityType> next()
                {
                return bean(
                    this.iterator.next()
                    );
                }
            @Override
            public void remove()
                {
                this.iterator.remove();
                }
            };
        }
    }
