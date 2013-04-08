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
import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;

/**
 * Bean wrapper for <code>AdqlQuery</code>.
 *
 */
public class AdqlQueryBean
extends AbstractEntityBeanImpl<AdqlQuery>
implements EntityBean<AdqlQuery>
    {
    /**
     * Factory wrapper method.
     *
     */
    public static Iter wrap(final Iterable<AdqlQuery> iterable)
        {
        return new Iter(
            iterable
            );
        }

    /**
     * Iterable wrapper.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<AdqlQuery>
        {
        /**
         * Protected constructor.
         *
         */
        public Iter(final Iterable<AdqlQuery> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public EntityBean<AdqlQuery> bean(final AdqlQuery entity)
            {
            return wrap(
                entity
                );
            }
        }

    /**
     * Factory wrapper method.
     *
     */
    public static AdqlQueryBean wrap(final AdqlQuery entity)
        {
        return new AdqlQueryBean(
            entity
            );
        }

    /**
     * Protected constructor.
     *
     */
    protected AdqlQueryBean(final AdqlQuery entity)
        {
        super(
            AdqlQueryIdentFactory.TYPE_URI,
            entity
            );
        }

    public String getWorkspace()
        {
        return entity().resource().link();
        }

    public String getInput()
        {
        return entity().input();
        }

    public AdqlQuery.Mode getMode()
        {
        return entity().mode();
        }

    public Status getStatus()
        {
        return entity().status();
        }

    public String getAdql()
        {
        return entity().adql();
        }

    public String getOsql()
        {
        return entity().osql();
        }

    public Iterable<String> getResources()
        {
        return new Iterable<String>()
            {
            @Override
            public Iterator<String> iterator()
                {
                return new Iterator<String>()
                    {
                    Iterator<BaseResource<?>> iter = entity().resources().iterator();
                    @Override
                    public boolean hasNext()
                        {
                        return this.iter.hasNext();
                        }
                    @Override
                    public String next()
                        {
                        return this.iter.next().link();
                        }
                    @Override
                    public void remove()
                        {
                        this.iter.remove();
                        }
                    };
                }
            };
        }

    public Iterable<String> getTables()
        {
        return new Iterable<String>()
            {
            @Override
            public Iterator<String> iterator()
                {
                return new Iterator<String>()
                    {
                    Iterator<AdqlTable> iter = entity().tables().iterator();
                    @Override
                    public boolean hasNext()
                        {
                        return this.iter.hasNext();
                        }
                    @Override
                    public String next()
                        {
                        return this.iter.next().link();
                        }
                    @Override
                    public void remove()
                        {
                        this.iter.remove();
                        }
                    };
                }
            };
        }

    public Iterable<String> getColumns()
        {
        return new Iterable<String>()
            {
            @Override
            public Iterator<String> iterator()
                {
                return new Iterator<String>()
                    {
                    Iterator<AdqlColumn> iter = entity().columns().iterator();
                    @Override
                    public boolean hasNext()
                        {
                        return this.iter.hasNext();
                        }
                    @Override
                    public String next()
                        {
                        return this.iter.next().link();
                        }
                    @Override
                    public void remove()
                        {
                        this.iter.remove();
                        }
                    };
                }
            };
        }
    
    public interface Syntax
        {
        public AdqlQuery.Syntax.Status getStatus();
        public String getMessage();
        public String getFriendly();
        }

    public Syntax getSyntax()
        {
        return new Syntax()
            {
            @Override
            public AdqlQuery.Syntax.Status getStatus()
                {
                return entity().syntax().status();
                }
            @Override
            public String getMessage()
                {
                return entity().syntax().message();
                }
            @Override
            public String getFriendly()
                {
                return entity().syntax().friendly();
                }
            };
        }
    }
