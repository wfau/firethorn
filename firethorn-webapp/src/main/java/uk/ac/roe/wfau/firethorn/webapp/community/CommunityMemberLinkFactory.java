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
package uk.ac.roe.wfau.firethorn.webapp.community;

import org.springframework.stereotype.Component;

import uk.ac.roe.wfau.firethorn.community.CommunityMember;
import uk.ac.roe.wfau.firethorn.webapp.control.WebappLinkFactory;

/**
 * Link factory for <code>AdqlColumn</code>.
 * TODO
 *
 */
@Component
public class CommunityMemberLinkFactory
extends WebappLinkFactory<CommunityMember>
implements CommunityMember.LinkFactory
    {
    protected CommunityMemberLinkFactory()
        {
        super(
            BASE_PATH
            );
        }

    /**
     * The URI path for the service.
     *
     */
    protected static final String BASE_PATH = "/community-member" ;

    /**
     * The URI path for individual columns.
     *
     */
    public static final String ENTITY_PATH = BASE_PATH + "/" + IDENT_TOKEN ;

    @Override
    public String link(final CommunityMember entity)
        {
        return link(
            ENTITY_PATH,
            entity
            );
        }
    }
