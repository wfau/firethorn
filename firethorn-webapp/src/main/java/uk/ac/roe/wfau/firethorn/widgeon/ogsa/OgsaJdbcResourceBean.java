/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.widgeon.ogsa;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.ogsa.OgsaJdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;

/**
 *
 *
 */
@Slf4j
public class OgsaJdbcResourceBean
extends AbstractEntityBeanImpl<OgsaJdbcResource>
    {

    public OgsaJdbcResourceBean(final OgsaJdbcResource entity)
        {
        super(
            OgsaJdbcResourceIdentFactory.TYPE_URI,
            entity
            );
        log.debug("OgsaJdbcResourceBean() [{}]", entity);
        }
    
    public static class Iter
    extends AbstractEntityBeanIter<OgsaJdbcResource, OgsaJdbcResourceBean>
        {
        public Iter(final Iterable<OgsaJdbcResource> iterable)
            {
            super(
                iterable
                );
            log.debug("OgsaJdbcResourceBean.Iter() [{}]", iterable);
            }

        @Override
        public OgsaJdbcResourceBean bean(final OgsaJdbcResource entity)
            {
            return new OgsaJdbcResourceBean(
                entity
                );
            }
        }

    public String getOgsaid()
        {
        return entity().ogsaid();
        }
    }
