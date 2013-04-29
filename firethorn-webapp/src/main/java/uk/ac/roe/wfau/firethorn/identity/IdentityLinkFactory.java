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
package uk.ac.roe.wfau.firethorn.identity;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>Identity</code>.
 *
 */
@Component
public class IdentityLinkFactory
extends WebappLinkFactory<Identity>
implements Identity.LinkFactory
    {
    protected IdentityLinkFactory()
        {
        super(
            SERVICE_PATH
            );
        }

    /**
     * The URI path for the service.
     *
     */
    public static final String SERVICE_PATH = "/auth/identity";

    /**
     * The URI path for individual identities.
     *
     */
    public static final String IDENTITY_PATH = SERVICE_PATH + "/" + IDENT_TOKEN ;

    @Override
    public String link(final Identity entity)
        {
        return this.link(
            IDENTITY_PATH,
            entity
            );
        }
    }
