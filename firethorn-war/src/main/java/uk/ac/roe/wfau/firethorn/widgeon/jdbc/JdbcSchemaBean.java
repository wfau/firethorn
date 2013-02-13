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

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.roe.wfau.firethorn.tuesday.JdbcSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>JdbcSchema</code>.
 *
 */
public class JdbcSchemaBean
extends AbstractEntityBeanImpl<JdbcSchema>
implements EntityBean<JdbcSchema>
    {
    public static class Iter
    extends AbstractEntityBeanIter<JdbcSchema>
        {
        /**
         * Public constructor.
         *
         */
        public Iter(final Iterable<JdbcSchema> iterable)
            {
            super(
                iterable
                );
            }

        @Override
        public EntityBean<JdbcSchema> bean(final JdbcSchema entity)
            {
            return new JdbcSchemaBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public JdbcSchemaBean(final JdbcSchema entity)
        {
        super(
            JdbcSchemaIdentFactory.TYPE_URI,
            entity
            );
        }

    public URI getParent()
        {
        try {
            return new URI(
                entity().resource().link()
                );
            }
        catch (final URISyntaxException ouch)
            {
            throw new RuntimeException(
                ouch
                );
            }
        }

    public String getFullname()
        {
        return entity().fullname().toString();
        }
    }
