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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseSchemaBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTableBean;

/**
 * Bean wrapper for <code>AdqlSchema</code>.
 *
 */
public class AdqlSchemaBean
extends BaseSchemaBean<AdqlSchema>
    {
    public static class Iter
    extends AbstractEntityBeanIter<AdqlSchema, AdqlSchemaBean>
        {
        public Iter(final Iterable<AdqlSchema> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public AdqlSchemaBean bean(final AdqlSchema entity)
            {
            return new AdqlSchemaBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public AdqlSchemaBean(final AdqlSchema entity)
        {
        super(
            AdqlSchemaIdentFactory.TYPE_URI,
            entity
            );
        }
    }
