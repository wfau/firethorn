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
package uk.ac.roe.wfau.firethorn.webapp.blue;

import java.util.Iterator;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery.ResultState;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseResource;
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
     * Get the {@link BlueQuery} {@link AdqlResource} workspace.
     *
     */
    public String getWorkspace()
        {
        if (entity().source() != null)
            {
            return entity().source().link();
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
     * The {@link BlueQuery} {@link AdqlQueryBase.Mode} mode.
     *
     */
    public AdqlQueryBase.Mode getMode()
        {
        return entity().mode();
        }

    /**
     * The {@link BlueQuery} {@link BlueTask.TaskState} status.
     *
     */
    public BlueTask.TaskState getStatus()
        {
        return entity().state();
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
     * The {@link BlueQuery} callback URL.
     * 
     */
    public String getCallback()
        {
        return entity().callback();
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
     * The {@link BlueQuery} query parser results.
     *
     */
    public interface Syntax
        {
        /**
         * The parser result {@link AdqlQueryBase.Syntax.State}.
         *
         */
        public AdqlQueryBase.Syntax.State getStatus();

        /**
         * The parser message.
         * TODO Make this into list of results.
         *
         */
        public String getMessage();

        /**
         * The parser message.
         * TODO Make this into list of results.
         *
         */
        public String getFriendly();
        }

    /**
     * Get the {@link BlueQuery} query parser results.
     * 
     *
     */
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
     * The {@link BlueQuery} results.
     *
     */
    public interface Results
        {
        /**
         * A URL to access the {@link AdqlTable} results
         *
         */
        public String getTable();
        
        /**
         * The result row count.
         * 
         */
        public Long getCount();

        /**
         * The {@link ResultState} state
         *
         */
        public ResultState getState();
        
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
            public String getTable()
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
            public Long getCount()
                {
                return entity().results().rowcount();
                }
            @Override
            public ResultState getState()
                {
                return entity().results().state();
                }
            };
        }
    
    /**
     * A {@BlueQuery} {@link AdqlQueryBase.SelectField}.
     *
     */
    public interface FieldBean
        {
        public String  getName();

        public Integer getLength();

        public String  getType();
        }

    /**
     * The list of {@BlueQuery} {@link AdqlQueryBase.SelectField}s.
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
                    final Iterator<AdqlQueryBase.SelectField> iter = entity().fields().select().iterator();
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

    /**
     * A {@BlueQuery} {@link AdqlQueryBase.Limits}.
     *
     */
    public interface LimitsBean
        {
        public Long getRows();
        public Long getCells();
        public Long getTime();
        }

    /**
     * Get the {@BlueQuery} {@link AdqlQueryBase.Limits}.
     *
     */
    public LimitsBean getLimits()
        {
        return new LimitsBean()
            {
            public Long getRows()
                {
                return entity().limits().rows(); 
                }
            public Long getCells()
                {
                return entity().limits().cells(); 
                }
            public Long getTime()
                {
                return entity().limits().time(); 
                }
            };
        }
    
    /**
     * A {@BlueQuery} {@link AdqlQueryBase.Delays}.
     *
     */
    public interface DelaysBean
        {
        /**
         * The delay for the first row.
         * 
         */
        public Integer getFirst();
        /**
         * The delay for every row.
         * 
         */
        public Integer getEvery();
        /**
         * The delay for the last row.
         * 
         */
        public Integer getLast();
        }

    /**
     * Get the {@BlueQuery} {@link AdqlQueryBase.Delays}.
     *
     */
    public DelaysBean getDelays()
        {
        return new DelaysBean()
            {
            public Integer getFirst()
                {
                return entity().delays().first(); 
                }
            public Integer getEvery()
                {
                return entity().delays().every(); 
                }
            public Integer getLast()
                {
                return entity().delays().last(); 
                }
            };
        }
    }
