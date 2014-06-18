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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlColumnBean;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTableBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseColumnBean;
import uk.ac.roe.wfau.firethorn.widgeon.ivoa.IvoaTableBean.MetadataBean.IvoaMetadataBean;

/**
 * Bean wrapper for <code>IvoaColumn</code>.
 *
 */
public class IvoaColumnBean
extends BaseColumnBean<IvoaColumn>
    {
    public static class Iter
    extends AbstractEntityBeanIter<IvoaColumn, IvoaColumnBean>
        {
        public Iter(final Iterable<IvoaColumn> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public IvoaColumnBean bean(final IvoaColumn entity)
            {
            return new IvoaColumnBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public IvoaColumnBean(final IvoaColumn entity)
        {
        super(
            IvoaColumnIdentFactory.TYPE_URI,
            entity
            );
        }

    public class IvoaMetadataBean
        {
        public String getName()
            {
            return entity().name();
            }
        }

    public class MetadataBean
    extends BaseColumnBean<IvoaColumn>.MetadataBean
        {
        public IvoaMetadataBean getIvoa()
            {
            return new IvoaMetadataBean();
            }
        }

    @Override
    public MetadataBean getMeta()
        {
        return new MetadataBean();
        }
    }
