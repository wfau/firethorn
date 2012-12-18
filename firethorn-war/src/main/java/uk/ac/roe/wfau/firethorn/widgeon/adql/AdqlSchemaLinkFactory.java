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

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.common.entity.Identifier;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlResource;
import uk.ac.roe.wfau.firethorn.tuesday.TuesdayAdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>AdqlSchema</code>.
 * TODO
 */
@Component
public class AdqlSchemaLinkFactory
extends WebappLinkFactory<TuesdayAdqlSchema>
implements TuesdayAdqlSchema.LinkFactory
    {
    /**
     * The URI path for individual schema.
     *
     */
    public static final String SCHEMA_PATH = "/adql/schema/" + IDENT_TOKEN ;

    /**
     * The URI path for schema tables.
     *
     */
    public static final String SCHEMA_TABLE_PATH = SCHEMA_PATH + "/tables" ;

    @Override
    public String link(final TuesdayAdqlSchema entity)
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
