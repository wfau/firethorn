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
package uk.ac.roe.wfau.firethorn.widgeon.jdbc;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean iterator for <code>JdbcTable</code>.
 *
 */
public class JdbcTableBeanIter
extends AbstractEntityBeanIter<TuesdayJdbcTable>
    {
    /**
     * Public constructor.
     *
     */
    public JdbcTableBeanIter(final Iterable<TuesdayJdbcTable> iterable)
        {
        super(
            iterable
            );
        }

    @Override
    public EntityBean<TuesdayJdbcTable> bean(final TuesdayJdbcTable entity)
        {
        return new JdbcTableBean(
            entity
            );
        }
    }