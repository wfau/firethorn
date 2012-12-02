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

import java.net.URI;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.mallard.AdqlService;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;

/**
 * Ident factory for <code>AdqlService</code>.
 *
 */
@Component
public class AdqlServiceIdentFactory
extends WebappIdentFactory<AdqlService>
implements AdqlService.IdentFactory
    {
    /**
     * The type URI for this type.
     *
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/adql-service-1.0.json"
        );

    /**
     * The URI path for identifiers.
     *
    public static final String IDENT_PATH = "/adql/service/" + IDENT_TOKEN ;
     */

    /**
     * The URI path for the service service.
     *
     */
    public static final String SERVICES_PATH = "/adql/services";

    /**
     * The URI path for individual services.
     *
     */
    public static final String SERVICE_PATH = "/adql/service/" + IDENT_TOKEN ;

    /**
     * The URI path for service resources.
     *
     */
    public static final String RESOURCES_PATH = SERVICE_PATH + "/resources" ;

    @Override
    public String link(final AdqlService entity)
        {
        return link(
            SERVICES_PATH,
            entity
            );
        }
    }
