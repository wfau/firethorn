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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumnBean;

/**
 * An {@link EntityBean} wrapper for an {@link AdqlColumn}.
 *
 */
public class AdqlColumnBean
extends BaseColumnBean<AdqlColumn>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for an {@link AdqlColumn} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<AdqlColumn, AdqlColumnBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link AdqlColumn} {@link Iterable} to wrap.
         *
         */
        public Iter(final Iterable<AdqlColumn> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public AdqlColumnBean bean(final AdqlColumn entity)
            {
            return new AdqlColumnBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     * @param entity The {@link AdqlColumn} to wrap.
     *
     */
    public AdqlColumnBean(final AdqlColumn entity)
        {
        super(
            AdqlColumnIdentFactory.TYPE_URI,
            entity
            );
        }
    }
