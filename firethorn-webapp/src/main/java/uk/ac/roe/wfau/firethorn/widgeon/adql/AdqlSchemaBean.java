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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseSchemaBean;

/**
 * An {@link EntityBean} wrapper for an {@link AdqlSchema}.
 *
 */
public class AdqlSchemaBean
extends BaseSchemaBean<AdqlSchema>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for an {@link AdqlSchema} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<AdqlSchema, AdqlSchemaBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link AdqlSchema} {@link Iterable} to wrap.
         *
         */
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
     * @param entity The {@link AdqlSchema} to wrap.
     *
     */
    public AdqlSchemaBean(final AdqlSchema entity)
        {
        super(
            AdqlSchemaIdentFactory.TYPE_URI,
            entity
            );
        }

    /**
     * Access to the list of {@AdqlQuery} linked to this {@link AdqlSchema}.
     * @return A URL to access the list of {@AdqlQuery}.
     *
     */
    public String getQueries()
    	{
    	return entity().link().concat(
    	    "/queries/select"
    	    );
    	}
    }
