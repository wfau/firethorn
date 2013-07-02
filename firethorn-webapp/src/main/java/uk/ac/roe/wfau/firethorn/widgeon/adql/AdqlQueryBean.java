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

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBean;

/**
 * Bean wrapper for <code>AdqlQuery</code>.
 *
 */
public class AdqlQueryBean
extends NamedEntityBeanImpl<AdqlQuery>
implements NamedEntityBean<AdqlQuery>
    {

    public static class Iter
    extends AbstractEntityBeanIter<AdqlQuery>
        {
        public Iter(final Iterable<AdqlQuery> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public NamedEntityBean<AdqlQuery> bean(final AdqlQuery entity)
            {
            return new AdqlQueryBean(
                entity
                );
            }
        }

    protected AdqlQueryBean(final AdqlQuery entity)
        {
        super(
            AdqlQueryIdentFactory.TYPE_URI,
            entity
            );
        }

    public String getSchema()
        {
        if (entity().schema() != null)
            {
            return entity().schema().link();
            }
        return null ;
        }

    public String getWorkspace()
        {
        if (entity().schema() != null)
            {
            if (entity().schema().resource() != null)
                {
                return entity().schema().resource().link();
                }
            }
        return null ;
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
        public AdqlQuery.Syntax.State getStatus();
        public String getMessage();
        public String getFriendly();
        }

    public Syntax getSyntax()
        {
        return new Syntax()
            {
            @Override
            public AdqlQuery.Syntax.State getStatus()
                {
                if (entity().syntax() != null)
                    {
                    return entity().syntax().state();
                    }
                else {
                    return AdqlQuery.Syntax.State.UNKNOWN;
                    }
                }
            @Override
            public String getMessage()
                {
                if (entity().syntax() != null)
                    {
                    return entity().syntax().message();
                    }
                else {
                    return null ;
                    }
                }
            @Override
            public String getFriendly()
                {
                if (entity().syntax() != null)
                    {
                    return entity().syntax().friendly();
                    }
                else {
                    return null ;
                    }
                }
            };
        }

    /**
     * The query results.
     *
     */
    public interface Results
        {
        /**
         * The abstract (ADQL) table
         *
         */
        public String getAdql();
        /**
         * The physical (JDBC) table
         *
         */
        public String getJdbc();

        /**
         * Access to a VOTable representation of the results.
         *
         */
        public String getVotable();

        }

    /**
     * The query results.
     *
     */
    public Results getResults()
        {
        return new Results()
            {
            @Override
            public String getAdql()
                {
                if (entity().results().adql() != null)
                    {
                    return entity().results().adql().link();
                    }
                else {
                    return null ;
                }
                }
            @Override
            public String getJdbc()
                {
                if (entity().results().jdbc() != null)
                    {
                    return entity().results().jdbc().link();
                    }
                else {
                    return null ;
                    }
                }
            @Override
            public String getVotable()
                {
                return entity().link().concat(
                    AdqlQueryLinkFactory.VOTABLE_NAME
                    );
                }
            };
        }

    /**
     * A select field bean.
     *
     */
    public interface FieldBean
        {
        public String  getName();

        public Integer getLength();
        
        public String  getType();
        }

    /**
     * The query select fields.
     *
     */
    public Iterable<FieldBean> getFields()
        {
        return new Iterable<FieldBean>()
            {
            @Override
            public Iterator<FieldBean> iterator()
                {
                return new Iterator<FieldBean>()
                    {
                    final Iterator<AdqlQuery.SelectField> iter = entity().fields().iterator();
                    protected Iterator<AdqlQuery.SelectField> iter()
                        {
                        return this.iter;
                        }
                    @Override
                    public boolean hasNext()
                        {
                        return this.iter.hasNext();
                        }
                    @Override
                    public FieldBean next()
                        {
                        return new FieldBean(){
                            final AdqlQuery.SelectField field = iter().next();
                            @Override
                            public String getName()
                                {
                                return this.field.name();
                                }
                            @Override
                            public Integer getLength()
                                {
                                return this.field.arraysize();
                                }

                            @Override
                            public String getType()
                                {
                                return this.field.type().name();
                                }
                            };
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
    }
