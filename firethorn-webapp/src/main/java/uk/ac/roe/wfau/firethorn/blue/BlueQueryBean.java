/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.blue;

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;

/**
 * An {@link EntityBean} wrapper for an {@link BlueQuery}.
 *
 */
public class BlueQueryBean
    extends NamedEntityBeanImpl<BlueQuery>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for an {@link BlueQuery} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<BlueQuery, BlueQueryBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link BlueQuery} {@link Iterable} to wrap.
         *
         */
        public Iter(final Iterable<BlueQuery> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public BlueQueryBean bean(final BlueQuery entity)
            {
            return new BlueQueryBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     * @param entity The {@link BlueQuery} to wrap.
     *
     */
    public BlueQueryBean(final BlueQuery entity)
        {
        super(
            BlueQuery.TYPE_URI,
            entity
            );
        }

    /**
     * The {@link BlueQuery} workspace.
     *
     */
    public String getWorkspace()
        {
        if (entity().resource() != null)
            {
            return entity().resource().link();
            }
        return null ;
        }

    /**
     * The {@link BlueQuery} input.
     *
     */
    public String getInput()
        {
        return entity().input();
        }

    /**
     * The {@link BlueQuery} {@link AdqlQuery.Mode} mode.
     *
     */
    public AdqlQuery.Mode getMode()
        {
        return entity().mode();
        }

    /**
     * The {@link BlueQuery} {@link BlueTask.StatusOne} status.
     *
     */
    public BlueTask.StatusOne getStatus()
        {
        return entity().one();
        }

    /**
     * The {@link BlueQuery} ADQL query.
     *
     */
    public String getAdql()
        {
        return entity().adql();
        }

    /**
     * The {@link BlueQuery} OGSA-DA SQL query.
     *
     */
    public String getOsql()
        {
        return entity().osql();
        }

    /**
     * The {@link BlueQuery} resources.
     *
     */
    public Iterable<String> getResources()
        {
        return new Iterable<String>()
            {
            @Override
            public Iterator<String> iterator()
                {
                return new Iterator<String>()
                    {
                    Iterator<BaseResource<?>> iter = entity().resources().select().iterator();
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

    /**
     * The {@link BlueQuery} tables.
     *
     */
    public Iterable<String> getTables()
        {
        return new Iterable<String>()
            {
            @Override
            public Iterator<String> iterator()
                {
                return new Iterator<String>()
                    {
                    Iterator<AdqlTable> iter = entity().tables().select().iterator();
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

    /**
     * The {@link BlueQuery} columns.
     *
     */
    public Iterable<String> getColumns()
        {
        return new Iterable<String>()
            {
            @Override
            public Iterator<String> iterator()
                {
                return new Iterator<String>()
                    {
                    Iterator<AdqlColumn> iter = entity().columns().select().iterator();
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

    /**
     * The {@link BlueQuery} results.
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

        }

    /**
     * The {@link BlueQuery} results.
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
            };
        }
    
    /**
     * An {@link AdqlQuery.SelectField} bean.
     *
     */
    public interface FieldBean
        {
        public String  getName();

        public Integer getLength();

        public String  getType();
        }

    /**
     * The {@BlueQuery} select {@link AdqlQuery.SelectField}s.
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
                    final Iterator<AdqlQuery.SelectField> iter = entity().fields().select().iterator();
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
