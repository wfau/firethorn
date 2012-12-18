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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>AdqlColumn</code>.
 *
 */
public class AdqlColumnBean
extends AbstractEntityBeanImpl<TuesdayAdqlColumn>
implements EntityBean<TuesdayAdqlColumn>
    {
    /**
     * Public constructor.
     *
     */
    public AdqlColumnBean(final TuesdayAdqlColumn entity)
        {
        super(
            AdqlColumnIdentFactory.TYPE_URI,
            entity
            );
        }

    public static class Iter
    extends AbstractEntityBeanIter<TuesdayAdqlColumn>
        {
        /**
         * Public constructor.
         *
         */
        public Iter(final Iterable<TuesdayAdqlColumn> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public EntityBean<TuesdayAdqlColumn> bean(final TuesdayAdqlColumn entity)
            {
            return new AdqlColumnBean(
                entity
                );
            }
        }
    }
