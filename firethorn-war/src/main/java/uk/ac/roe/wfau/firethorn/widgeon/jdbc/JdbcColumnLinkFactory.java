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

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcColumn;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>JdbcColumn</code>.
 *
 */
@Component
public class JdbcColumnLinkFactory
extends WebappLinkFactory<TuesdayJdbcColumn>
implements TuesdayJdbcColumn.LinkFactory
    {
    /**
     * The URI path for individual columns.
     *
     */
    public static final String COLUMN_PATH = "/jdbc/column/" + IDENT_TOKEN ;

    @Override
    public String link(final TuesdayJdbcColumn entity)
        {
        return link(
            COLUMN_PATH,
            entity
            );
        }
    }
