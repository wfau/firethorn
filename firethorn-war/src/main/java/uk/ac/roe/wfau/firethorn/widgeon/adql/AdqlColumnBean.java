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

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumnType;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>AdqlColumn</code>.
 *
 */
@Slf4j
public class AdqlColumnBean
extends AbstractEntityBeanImpl<AdqlColumn>
implements EntityBean<AdqlColumn>
    {
    public static class Iter
    extends AbstractEntityBeanIter<AdqlColumn>
        {
        /**
         * Public constructor.
         *
         */
        public Iter(final Iterable<AdqlColumn> iterable)
            {
            super(
                iterable
                );
            log.debug("Iter(Iterable)");
            }
        @Override
        public EntityBean<AdqlColumn> bean(final AdqlColumn entity)
            {
            return new AdqlColumnBean(
                entity
                );
            }
        }
    /**
     * Public constructor.
     *
     */
    public AdqlColumnBean(final AdqlColumn entity)
        {
        super(
            AdqlColumnIdentFactory.TYPE_URI,
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

    public URI getBase()
        {
        try {
            return new URI(
                entity().base().link()
                );
            }
        catch (final URISyntaxException ouch)
            {
            throw new RuntimeException(
                ouch
                );
            }
        }

    public URI getRoot()
        {
        try {
            return new URI(
                entity().root().link()
                );
            }
        catch (final URISyntaxException ouch)
            {
            throw new RuntimeException(
                ouch
                );
            }
        }

    public String getAlias()
        {
        return entity().alias();
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
            };
        }
    }
