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

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>JdbcResource</code>.
 *
 */
public class JdbcResourceBean
extends AbstractEntityBeanImpl<TuesdayJdbcResource>
implements EntityBean<TuesdayJdbcResource>
    {
    /**
     * Public constructor.
     *
     */
    public JdbcResourceBean(final TuesdayJdbcResource entity)
        {
        super(
            JdbcResourceIdentFactory.TYPE_URI,
            entity
            );
        }

    public class ConnectionBean
        {
        public String getUrl()
            {
            return entity().connection().url();
            }
        public String getUser()
            {
            return entity().connection().user();
            }
        public String getStatus()
            {
            return entity().connection().status().name();
            }
        }

    public ConnectionBean getConnection()
        {
        return new ConnectionBean();
        }

    public String getOgsaResourceId()
    	{
    	return entity().ogsaid();
    	}
    }
