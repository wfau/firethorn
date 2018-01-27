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

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResourceBean;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcResourceIdentFactory;

/**
 * Bean wrapper for <code>JdbcResource</code>.
 *
 */
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
        public String getUri()
        throws ProtectionException;
        
        public String getUser()
        throws ProtectionException;
        
        public String getPass()
        throws ProtectionException;
        }

    public ConnectionBean getConnection()
        {
        return new ConnectionBean()
            {
            public String getUri()
            throws ProtectionException
                {
                return entity().connection().uri();
                }
            public String getUser()
            throws ProtectionException
                {
                return entity().connection().user();
                }
            public String getPass()
            throws ProtectionException
                {
                return "########";
                }
            };
        }
/*
 * 
    public interface OgsaJdbdResources
        {
        //public String getPrimary();
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
 *            
            public OgsaJdbcResourceBean.Iter getSelect()
                {
                log.debug("getSelect()");
                return new OgsaJdbcResourceBean.Iter(
                    entity().ogsa().select()
                    );
                }
 *                    
            };
        }
 * 
 */
    }
