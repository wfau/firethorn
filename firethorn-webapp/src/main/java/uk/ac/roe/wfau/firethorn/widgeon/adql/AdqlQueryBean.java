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

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;
import uk.ac.roe.wfau.firethorn.adql.query.green.GreenJob.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlQueryIdentFactory;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlQueryLinkFactory;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;

/**
 * An {@link EntityBean} wrapper for an {@link GreenQuery}.
 *
 */
public class AdqlQueryBean
extends NamedEntityBeanImpl<GreenQuery>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for an {@link GreenQuery} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<GreenQuery, AdqlQueryBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link GreenQuery} {@link Iterable} to wrap.
         *
         */
        public Iter(final Iterable<GreenQuery> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public AdqlQueryBean bean(final GreenQuery entity)
            {
            return new AdqlQueryBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     * @param entity The {@link GreenQuery} to wrap.
     *
     */
    protected AdqlQueryBean(final GreenQuery entity)
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

    public AdqlQueryBase.Mode getMode()
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
        public AdqlQueryBase.Syntax.State getStatus();
        public String getMessage();
        public String getFriendly();
        }

    public Syntax getSyntax()
        {
        return new Syntax()
            {
            @Override
            public AdqlQueryBase.Syntax.State getStatus()
                {
                if (entity().syntax() != null)
                    {
                    return entity().syntax().state();
                    }
                else {
                    return AdqlQueryBase.Syntax.State.UNKNOWN;
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
     * The {@link GreenQuery} results.
     * @todo Simplify this to just the {@link AdqlTable} results.
     *
     */
    public interface Results
        {
        /**
         * A URL to access the {@link AdqlTable} results
         *
         */
        public String getAdql();
        
        /**
         * A URL to access the {@link JdbcTable} results
         * @deprecated Use the {@link AdqlTable#base()}
         *
         */
        public String getJdbc();

        /**
         * A URL to access a VOTable representation of the results.
         * @deprecated Use the VOTable view of the {@link AdqlTable}
         *
         */
        public String getVotable();

        /**
         * A URL to access a DataTable representation of the results.
         * @deprecated Use the DataTable view of the {@link AdqlTable}
         *
         */
        public String getDatatable();

        }

    /**
     * The {@link GreenQuery} results.
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
           
            
            @Override
            public String getDatatable()
                {
            	  if (entity().results().adql() != null)
                  {
            		  return entity().results().adql().link().concat(
                              AdqlQueryLinkFactory.DATATABLE_NAME
                           );
                  }
            	  else {
            		  return null ;
            	  }
            };
            
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
                    final Iterator<AdqlQueryBase.SelectField> iter = entity().fields().iterator();
                    protected Iterator<AdqlQueryBase.SelectField> iter()
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
                            final AdqlQueryBase.SelectField field = iter().next();
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
