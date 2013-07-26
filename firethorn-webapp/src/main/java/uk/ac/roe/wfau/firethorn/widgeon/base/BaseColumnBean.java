/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.widgeon.base;

import java.net.URI;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn;
import uk.ac.roe.wfau.firethorn.meta.base.BaseColumn.UCD;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;

/**
 *
 *
 */
public class BaseColumnBean<ColumnType extends BaseColumn<?>>
extends NamedEntityBeanImpl<ColumnType>
    {

    public BaseColumnBean(final URI type, final ColumnType column)
        {
        super(
            type,
            column
            );
        }
    
    @Deprecated
    public String getParent()
        {
        return entity().table().link();
        }

    public String getTable()
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

    public String getDepth()
        {
        return entity().depth().toString();
        }
    
    public class AdqlMetadataBean
        {
        public AdqlColumn.Type getType()
            {
            return entity().meta().adql().type();
            }
        public Integer getArraySize()
            {
            return entity().meta().adql().arraysize();
            }
        public String getUnits()
            {
            return entity().meta().adql().units();
            }
        public String getUtype()
            {
            return entity().meta().adql().utype();
            }

        public class UCDMetadataBean
            {
            public String getType()
                {
                UCD ucd = entity().meta().adql().ucd();
                if (ucd != null)
                    {
                    return ucd.type().toString();
                    }
                else {
                    return null ;
                    }
                }
            public String getValue()
                {
                UCD ucd = entity().meta().adql().ucd();
                if (ucd != null)
                    {
                    return ucd.value();
                    }
                else {
                    return null ;
                    }
                }
            }
        public UCDMetadataBean getUCD()
            {
            return new UCDMetadataBean();
            }
        }

    public class MetadataBean
        {
        public AdqlMetadataBean getAdql()
            {
            return new AdqlMetadataBean();
            }
        }

    public MetadataBean getMeta()
        {
        return new MetadataBean();
        }
    }
