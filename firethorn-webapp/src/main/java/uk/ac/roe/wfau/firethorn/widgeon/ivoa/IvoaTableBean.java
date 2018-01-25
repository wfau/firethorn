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
package uk.ac.roe.wfau.firethorn.widgeon.ivoa;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.ivoa.IvoaTable;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractEntityBeanIter;
import uk.ac.roe.wfau.firethorn.widgeon.adql.AdqlTableBean;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseTableBean;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaTableIdentFactory;
import uk.ac.roe.wfau.firethorn.widgeon.name.IvoaTableLinkFactory;

/**
 * Bean wrapper for <code>IvoaTable</code>.
 *
 */
public class IvoaTableBean
extends BaseTableBean<IvoaTable>
    {
    /**
     * {@link Iterable} of {@link IvoaTable}.
     *
     */
    public static class Iter
    extends AbstractEntityBeanIter<IvoaTable, IvoaTableBean>
        {
        public Iter(final Iterable<IvoaTable> iterable)
            {
            super(
                iterable
                );
            }
        @Override
        public IvoaTableBean bean(final IvoaTable entity)
            {
            return new IvoaTableBean(
                entity
                );
            }
        }

    /**
     * Public constructor.
     *
     */
    public IvoaTableBean(final IvoaTable entity)
        {
        super(
            IvoaTableIdentFactory.TYPE_URI,
            entity
            );
        }

    public String getQuery()
    throws ProtectionException
        {
        if (entity().bluequery() != null)
            {
            return entity().bluequery().link();
            }
        else {
            return null ;
            }
        }
    
    public String getBlueQuery()
    throws ProtectionException
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
        public interface IvoaMetadataBean
            {
            public String getName(); 
            }
        public IvoaMetadataBean getIvoa();
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
                    throws ProtectionException
                        {
                        return entity().meta().adql().count();
                        }

                    @Override
                    public AdqlTable.TableStatus getStatus()
                    throws ProtectionException
                        {
                        return entity().meta().adql().status();
                        }
                    };
                }

            @Override
            public IvoaMetadataBean getIvoa()
                {
                return new IvoaMetadataBean()
                    {
                    public String getName()
                        {
                        return entity().name();
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
                    IvoaTableLinkFactory.VOTABLE_NAME
                    );
                }
            @Override
            public String getDatatable()
                {
                return entity().link().concat(
                    IvoaTableLinkFactory.DATATABLE_NAME
                    );
                }
            };
        }
    }
