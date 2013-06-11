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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>JdbcColumn</code>.
 * @todo Split this into an interface and an implementation.
 *
 */
public class JdbcColumnBean
extends AbstractEntityBeanImpl<JdbcColumn>
implements EntityBean<JdbcColumn>
    {
    public static class Iter
    extends AbstractEntityBeanIter<JdbcColumn>
        {
        /**
         * Public constructor.
         *
         */
        public Iter(final Iterable<JdbcColumn> iterable)
            {
            super(
                iterable
                );
            }

        @Override
        public EntityBean<JdbcColumn> bean(final JdbcColumn entity)
            {
            return new JdbcColumnBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public JdbcColumnBean(final JdbcColumn entity)
        {
        super(
            JdbcColumnIdentFactory.TYPE_URI,
            entity
            );
        }

    public String getParent()
        {
        return entity().table().link();
        }

    public String getFullname()
        {
        return entity().fullname().toString();
        }

    public interface Info
        {
        public interface Adql
            {
            public AdqlColumn.Type getType();
            public Integer getSize();
            }
        public Adql getAdql();
        public interface Jdbc
            {
            public JdbcColumn.Type getType();
            public Integer getSize();
            }
        public Jdbc getJdbc();
        }

    public Info getInfo()
        {
        return new Info()
            {
            @Override
            public Adql getAdql()
                {
                return new Adql()
                    {
                    @Override
                    public AdqlColumn.Type getType()
                        {
                        return entity().meta().adql().type();
                        }
                    @Override
                    public Integer getSize()
                        {
                        return entity().meta().adql().array();
                        }
                    };
                }
            @Override
            public Jdbc getJdbc()
                {
                return new Jdbc()
                    {
                    @Override
                    public JdbcColumn.Type getType()
                        {
                        return entity().meta().jdbc().type();
                        }
                    @Override
                    public Integer getSize()
                        {
                        return entity().meta().jdbc().size();
                        }
                    };
                }
            };
        }
    }
