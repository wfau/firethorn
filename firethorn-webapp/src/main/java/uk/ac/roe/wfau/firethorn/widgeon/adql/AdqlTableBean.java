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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable.AdqlStatus;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTableBean;

/**
 * Bean wrapper for <code>AdqlTable</code>.
 *
 */
public class AdqlTableBean
extends BaseTableBean<AdqlTable>
    {
    public static class Iter
    extends AbstractEntityBeanIter<AdqlTable, AdqlTableBean>
        {
        public Iter(final Iterable<AdqlTable> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public AdqlTableBean bean(final AdqlTable entity)
            {
            return new AdqlTableBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public AdqlTableBean(final AdqlTable entity)
        {
        super(
            AdqlTableIdentFactory.TYPE_URI,
            entity
            );
        }

    public String getQuery()
        {
        if (entity().query() != null)
            {
            return entity().query().link();
            }
        else {
            return null ;
            }
        }

    public interface MetadataBean
    extends BaseTableBean.MetadataBean
        {
        public interface AdqlMetadataBean
            {
            public Long getCount();
            public AdqlTable.AdqlStatus getStatus();
            }
        public AdqlMetadataBean getAdql();
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
                    public AdqlStatus getStatus()
                        {
                        return entity().meta().adql().status();
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
                    AdqlTableLinkFactory.VOTABLE_NAME
                    );
                }
            @Override
            public String getDatatable()
                {
                return entity().link().concat(
                    AdqlTableLinkFactory.DATATABLE_NAME
                    );
                }
            };
        }
    }
