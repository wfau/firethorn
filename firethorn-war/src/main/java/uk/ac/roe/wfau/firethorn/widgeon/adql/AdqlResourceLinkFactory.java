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
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>AdqlResource</code>.
 *
 */
@Component
public class AdqlResourceLinkFactory
extends WebappLinkFactory<TuesdayAdqlResource>
implements TuesdayAdqlResource.LinkFactory
    {

    /**
     * The URI path for the service.
     *
     */
    public static final String SERVICE_PATH = "/adql/resources";

    /**
     * The URI path for individual objects.
     *
     */
    public static final String RESOURCE_PATH = "/adql/resource/" + IDENT_TOKEN ;

    /**
     * The URI path for resource schema.
     *
     */
    public static final String RESOURCE_SCHEMA_PATH = RESOURCE_PATH + "/schemas" ;

    @Override
    public String link(final TuesdayAdqlResource entity)
        {
        return link(
            RESOURCE_PATH,
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
