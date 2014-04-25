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
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResourceBean;

/**
 * An {@link EntityBean} wrapper for an {@link AdqlResource}.
 *
 */
public class AdqlResourceBean
extends BaseResourceBean<AdqlResource>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for an {@link AdqlResource} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<AdqlResource, AdqlResourceBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link AdqlResource} {@link Iterable} to wrap.
         *
         */
        public Iter(final Iterable<AdqlResource> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public AdqlResourceBean bean(final AdqlResource entity)
            {
            return new AdqlResourceBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     * @param entity The {@link AdqlResource} to wrap.
     *
     */
    public AdqlResourceBean(final AdqlResource entity)
        {
        super(
            AdqlResourceIdentFactory.TYPE_URI,
            entity
            );
        }

    /**
     * Access to the VOSI view of this {@link AdqlResource}.
     * @return A URL for the VOSI view.
     * @see AdqlResourceController#VOSI_PATH
     * @see AdqlResourceController#vosi(AdqlResource)
     *
     */
    public String getVosi()
        {
        return entity().link().concat(
            "/" + AdqlResourceController.VOSI_PATH
            );
        }
    }

