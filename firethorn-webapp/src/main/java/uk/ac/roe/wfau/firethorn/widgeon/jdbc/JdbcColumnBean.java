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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumnBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumnBean.AdqlMetadataBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumnBean.MetadataBean;

/**
 * Bean wrapper for <code>JdbcColumn</code>.
 *
 */
public class JdbcColumnBean
extends BaseColumnBean<JdbcColumn>
    {
    public static class Iter
    extends AbstractEntityBeanIter<JdbcColumn, JdbcColumnBean>
        {
        public Iter(final Iterable<JdbcColumn> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public JdbcColumnBean bean(final JdbcColumn entity)
            {
            return new JdbcColumnBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public JdbcColumnBean(final JdbcColumn entity)
        {
        super(
            JdbcColumnIdentFactory.TYPE_URI,
            entity
            );
        }

    public class JdbcMetadataBean
        {
        public JdbcColumn.Type getType()
            {
            return entity().meta().jdbc().type();
            }
        public Integer getSize()
            {
            return entity().meta().jdbc().size();
            }
        }

    public class MetadataBean
    extends BaseColumnBean<JdbcColumn>.MetadataBean
        {
        public JdbcMetadataBean getJdbc()
            {
            return new JdbcMetadataBean();
            }
        }

    public MetadataBean getMeta()
        {
        return new MetadataBean();
        }
    }
