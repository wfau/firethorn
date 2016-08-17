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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable.TableStatus;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.webapp.control.EntityBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTableBean;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlTableIdentFactory;
import uk.ac.roe.wfau.firethorn.widgeon.name.AdqlTableLinkFactory;

/**
 * An {@link EntityBean} wrapper for an {@link AdqlTable}.
 *
 */
public class AdqlTableBean
extends BaseTableBean<AdqlTable>
    {
    /**
     * An {@link EntityBean.Iter} wrapper for an {@link AdqlTable} {@link Iterable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<AdqlTable, AdqlTableBean>
        {
        /**
         * Public constructor.
         * @param iterable The {@link AdqlTable} {@link Iterable} to wrap.
         *
         */
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
     * @param entity The {@link AdqlTable} to wrap.
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
    extends BaseTableBean.MetadataBean
        {
        public interface AdqlMetadataBean
            {
            public Long getCount();
            public AdqlTable.TableStatus getStatus();
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
                    public TableStatus getStatus()
                        {
                        return entity().meta().adql().status();
                        }
                    };
                }
            };
        }

    /**
     * Access to the {@AdqlTable} data in different formats.   
     *
     */
    public interface FormatsBean
    extends BaseTableBean.FormatsBean
        {
        /**
         * Access to the {@AdqlTable} data as a VOTable.   
         * @return A URL to access the {@AdqlTable} data as a VOTable.
         * @see AdqlTableVOTableController
         * 
         */
        public String getVotable();

        /**
         * Access to the {@AdqlTable} data as a DataTable.
         * @return A URL to access the {@AdqlTable} data as a DataTable.
         * @see AdqlTableDataTableController
         * 
         */
        public String getDatatable();
        }

    /**
     * Access to the {@AdqlTable} data in different formats.   
     *
     */
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
