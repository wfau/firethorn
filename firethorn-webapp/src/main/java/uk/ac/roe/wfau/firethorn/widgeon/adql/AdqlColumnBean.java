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

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;

/**
 * Bean wrapper for <code>AdqlColumn</code>.
 *
 */
@Slf4j
public class AdqlColumnBean
extends NamedEntityBeanImpl<AdqlColumn>
implements NamedEntityBean<AdqlColumn>
    {
    public static class Iter
    extends AbstractEntityBeanIter<AdqlColumn>
        {
        public Iter(final Iterable<AdqlColumn> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public NamedEntityBean<AdqlColumn> bean(final AdqlColumn entity)
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

    public String getParent()
        {
        return entity().table().link();
        }

    public String getBase()
        {
        return entity().base().link();
        }

    public String getRoot()
        {
        return entity().root().link();
        }

    public String getAlias()
        {
        return entity().alias();
        }

    public String getFullname()
        {
        return entity().namebuilder().toString();
        }

    /**
     * @todo Move to a common base class.
     *
     */
    public interface Metadata
        {
        public interface Adql
            {
            public AdqlColumn.Type getType();
            public Integer getArraySize();
            public interface UCD
                {
                public String getType();
                public String getValue();
                }
            public UCD getUCD();
            }
        public Adql getAdql();
        }

    /**
     * @todo Move to a common base class.
     *
     */
    public Metadata getMeta()
        {
        return new Metadata()
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
                    public Integer getArraySize()
                        {
                        return entity().meta().adql().arraysize();
                        }
                    @Override
                    public UCD getUCD()
                        {
                        return new UCD()
                            {
                            public String getType()
                                {
                                return entity().meta().adql().ucd().type().toString();
                                }
                            public String getValue()
                                {
                                return entity().meta().adql().ucd().value();
                                }
                            };
                        }
                    };
                }
            };
        }
    }
