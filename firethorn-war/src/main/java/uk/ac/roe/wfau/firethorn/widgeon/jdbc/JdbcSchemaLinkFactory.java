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

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayJdbcSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>JdbcSchema</code>.
 *
 */
@Component
public class JdbcSchemaLinkFactory
extends WebappLinkFactory<TuesdayJdbcSchema>
implements TuesdayJdbcSchema.LinkFactory
    {
    /**
     * The URI path for individual schema.
     *
     */
    public static final String SCHEMA_PATH = "/jdbc/schema/" + IDENT_TOKEN ;

    /**
     * The URI path for schema tables.
     *
     */
    public static final String SCHEMA_TABLE_PATH = SCHEMA_PATH + "/tables" ;

    @Override
    public String link(final TuesdayJdbcSchema entity)
        {
        return link(
            SCHEMA_PATH,
            entity
            );
        }

    @Override
    public Identifier parse(String string)
        {
        // TODO Auto-generated method stub
        return null;
        }
    }
