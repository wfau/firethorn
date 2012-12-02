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

import java.net.URI;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.tuesday.TuesdayIvoaResource;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappIdentFactory;

/**
 * Ident factory for <code>IvoaResource</code>.
 * TODO
 * 
 */
@Component
public class IvoaResourceIdentFactory
extends WebappIdentFactory<TuesdayIvoaResource>
implements TuesdayIvoaResource.IdentFactory
    {
    /**
     * The type URI for this type.
     *
     */
    public static final URI TYPE_URI = URI.create(
        "http://data.metagrid.co.uk/wfau/firethorn/types/ivoa-resource-1.0.json"
        );

    /**
     * The URI path for the resource service.
     *
     */
    public static final String RESOURCES_PATH = "/ivoa/resources";

    /**
     * The URI path for individual resources.
     *
     */
    public static final String RESOURCE_PATH = "/ivoa/resource/" + IDENT_TOKEN ;

    /**
     * The URI path for resource catalogs.
     *
     */
    public static final String CATALOGS_PATH = RESOURCE_PATH + "/catalogs" ;


    @Override
    public String link(final TuesdayIvoaResource entity)
        {
        return link(
            RESOURCE_PATH,
            entity
            );
        }
    }
