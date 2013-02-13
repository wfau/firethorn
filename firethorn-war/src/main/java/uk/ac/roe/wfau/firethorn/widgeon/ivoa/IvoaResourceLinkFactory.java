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

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.tuesday.IvoaResource;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>IvoaResource</code>.
 *
 */
@Component
public class IvoaResourceLinkFactory
extends WebappLinkFactory<IvoaResource>
implements IvoaResource.LinkFactory
    {
    protected IvoaResourceLinkFactory()
        {
        super(
            SERVICE_PATH
            );
        }

    /**
     * The URI path for the service.
     *
     */
    public static final String SERVICE_PATH = "/ivoa/resource";

    /**
     * The URI path for individual resources.
     *
     */
    public static final String RESOURCE_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    /**
     * The URI path for resource schema.
     *
     */
    public static final String RESOURCE_SCHEMA_PATH = RESOURCE_PATH + "/schemas" ;

    @Override
    public String link(final IvoaResource entity)
        {
        return link(
            RESOURCE_PATH,
            entity
            );
        }
    }
