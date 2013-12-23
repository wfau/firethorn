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

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlTable;
import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.webapp.control.NamedEntityBeanImpl;

/**
 *
 *
 */
public class BaseTableBean<TableType extends BaseTable<?,?>>
extends NamedEntityBeanImpl<TableType>
    {

    public BaseTableBean(final URI type, final TableType table)
        {
        super(
            type,
            table
            );
        }

    public String getParent()
        {
        return entity().schema().link();
        }

    public String getSchema()
        {
        return entity().schema().link();
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

    public String getColumns()
        {
        return entity().link().concat("/columns/select");
        }

    public interface MetadataBean
        {
        }
    
    }
