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
package uk.ac.roe.wfau.firethorn.widgeon.base;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.meta.base.BaseTable;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>BaseTable</code>.
 *
 */
@Component
public class BaseTableLinkFactory
extends WebappLinkFactory<BaseTable<?,?>>
implements BaseTable.LinkFactory
    {
    protected BaseTableLinkFactory()
        {
        super(
            LINK_PATH
            );
        }

    /**
     * The URI path for link parser.
     *
     */
    public static final String LINK_PATH = "/table";

    /**
     * The URI path for the service.
     *
     */
    public static final String SERVICE_PATH = "/base/table";

    /**
     * The URI path for individual tables.
     *
     */
    public static final String TABLE_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    /**
     * The URI path for table columns.
     *
     */
    public static final String TABLE_COLUMN_PATH = TABLE_PATH + "/columns" ;

    @Override
    public String link(final BaseTable<?,?> entity)
        {
        return this.link(
            TABLE_PATH,
            entity
            );
        }
    }