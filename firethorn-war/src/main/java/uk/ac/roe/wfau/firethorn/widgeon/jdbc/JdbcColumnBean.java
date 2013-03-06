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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnType;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlColumnBean.Info;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlColumnBean.Info.Adql;

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

    public URI getParent()
        {
        try {
            return new URI(
                entity().table().link()
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

    public interface Info
        {
        public interface Adql
            {
            public AdqlColumnType getType();
            public Integer getSize();
            }
        public Adql getAdql();
        public interface Jdbc
            {
            public int getType();
            public int getSize();
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
                    public AdqlColumnType getType()
                        {
                        return entity().info().adql().type();
                        }
                    @Override
                    public Integer getSize()
                        {
                        return entity().info().adql().size();
                        }
                    };
                }
            @Override
            public Jdbc getJdbc()
                {
                return new Jdbc()
                    {
                    @Override
                    public int getType()
                        {
                        return entity().info().jdbc().type();
                        }
                    @Override
                    public int getSize()
                        {
                        return entity().info().jdbc().size();
                        }
                    }; 
                }
            };
        }
    }
