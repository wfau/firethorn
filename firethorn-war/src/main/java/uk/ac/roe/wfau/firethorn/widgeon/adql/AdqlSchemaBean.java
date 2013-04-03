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

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>AdqlSchema</code>.
 *
 */
public class AdqlSchemaBean
extends AbstractEntityBeanImpl<AdqlSchema>
implements EntityBean<AdqlSchema>
    {
    public static class Iter
    extends AbstractEntityBeanIter<AdqlSchema>
        {
        /**
         * Public constructor.
         *
         */
        public Iter(final Iterable<AdqlSchema> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public EntityBean<AdqlSchema> bean(final AdqlSchema entity)
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

    public String getParent()
        {
        return entity().resource().link();
        }

    public String getTables()
        {
        return entity().link().concat("/tables/select");
        }

    public String getFullname()
        {
        return entity().fullname().toString();
        }
    }
