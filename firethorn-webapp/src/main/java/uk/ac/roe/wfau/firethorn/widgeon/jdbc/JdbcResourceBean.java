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

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResourceBean;
import uk.ac.roe.wfau.firethorn.widgeon.ogsa.OgsaJdbcResourceBean;

/**
 * Bean wrapper for <code>JdbcResource</code>.
 *
 */
@Slf4j
public class JdbcResourceBean
extends BaseResourceBean<JdbcResource>
    {
    public static class Iter
    extends AbstractEntityBeanIter<JdbcResource, JdbcResourceBean>
        {
        public Iter(final Iterable<JdbcResource> iterable)
            {
            super(
                iterable
                );
            }

        @Override
        public JdbcResourceBean bean(final JdbcResource entity)
            {
            return new JdbcResourceBean(
                entity
                );
            }
        }
    /**
     * Public constructor.
     *
     */
    public JdbcResourceBean(final JdbcResource entity)
        {
        super(
            JdbcResourceIdentFactory.TYPE_URI,
            entity
            );
        }

    public interface ConnectionBean
        {
        public String getUri();
        public String getUser();
        public String getPass();
        }

    public ConnectionBean getConnection()
        {
        return new ConnectionBean()
            {
            public String getUri()
                {
                return entity().connection().uri();
                }
            public String getUser()
                {
                return entity().connection().user();
                }
            public String getPass()
                {
                return "########";
                }
            };
        }

    public interface OgsaJdbdResources
        {
        public String getPrimary();
        //public OgsaJdbcResourceBean.Iter getSelect();
        }

    public OgsaJdbdResources getOgsaResources()
        {
        return new OgsaJdbdResources()
            {
            public String getPrimary()
                {
                //return entity().ogsa().primary().link();
                return entity().ogsa().primary().ogsaid();
                }
/*            
            public OgsaJdbcResourceBean.Iter getSelect()
                {
                log.debug("getSelect()");
                return new OgsaJdbcResourceBean.Iter(
                    entity().ogsa().select()
                    );
                }
 */                    
            };
        }
    }
