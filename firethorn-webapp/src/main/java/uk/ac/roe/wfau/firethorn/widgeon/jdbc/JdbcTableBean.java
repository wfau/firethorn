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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTableBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTableBean;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcTableIdentFactory;
import uk.ac.roe.wfau.firethorn.widgeon.name.JdbcTableLinkFactory;

/**
 * Bean wrapper for <code>JdbcTable</code>.
 *
 */
public class JdbcTableBean
extends BaseTableBean<JdbcTable>
    {
    /**
     * Bean wrapper for <code>Iterable&lt;JdbcTable&gt;</code>.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<JdbcTable, JdbcTableBean>
        {
        public Iter(final Iterable<JdbcTable> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public JdbcTableBean bean(final JdbcTable entity)
            {
            return new JdbcTableBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public JdbcTableBean(final JdbcTable entity)
        {
        super(
            JdbcTableIdentFactory.TYPE_URI,
            entity
            );
        }

    public String getQuery()
        {
        if (entity().greenquery() != null)
            {
            return entity().greenquery().link();
            }
        else {
            return null ;
            }
        }
    
    public String getBlueQuery()
    {
    if (entity().bluequery() != null)
        {
        return entity().bluequery().link();
        }
    else {
        return null ;
        }
    }

    public interface MetadataBean
    extends AdqlTableBean.MetadataBean
        {
        public interface JdbcMetadataBean
            {
            public Long getCount();
            public JdbcTable.JdbcType getType();
            public JdbcTable.TableStatus getStatus();
            }
        public JdbcMetadataBean getJdbc();
        }

    public MetadataBean getMetadata()
        {
        return new MetadataBean()
            {
            @Override
            public AdqlMetadataBean getAdql()
                {
                return new AdqlMetadataBean()
                    {
                    @Override
                    public Long getCount()
                        {
                        return entity().meta().adql().count();
                        }

                    @Override
                    public AdqlTable.TableStatus getStatus()
                        {
                        return entity().meta().adql().status();
                        }
                    };
                }

            @Override
            public JdbcMetadataBean getJdbc()
                {
                return new JdbcMetadataBean()
                    {
                    @Override
                    public Long getCount()
                        {
                        return entity().meta().jdbc().count();
                        }

                    @Override
                    public JdbcTable.JdbcType getType()
                        {
                        return entity().meta().jdbc().type();
                        }

                    @Override
                    public JdbcTable.TableStatus getStatus()
                        {
                        return entity().meta().jdbc().status();
                        }
                    };
                }
            };
        }

    public interface FormatsBean
    extends BaseTableBean.FormatsBean
        {
        public String getVotable();
        public String getDatatable();
        }

    public FormatsBean getFormats()
        {
        return new FormatsBean()
            {
            @Override
            public String getVotable()
                {
                return entity().link().concat(
                    JdbcTableLinkFactory.VOTABLE_NAME
                    );
                }
            @Override
            public String getDatatable()
                {
                return entity().link().concat(
                    JdbcTableLinkFactory.DATATABLE_NAME
                    );
                }
            };
        }
    }
