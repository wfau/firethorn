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
package uk.ac.roe.wfau.firethorn.webapp.control;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.entity.AbstractLinkFactory;

/**
 * Base class for IdentFactory implementations within the webapp.
 *
 */
@Slf4j
public abstract class WebappLinkFactory<EntityType extends Entity>
extends AbstractLinkFactory<EntityType>
implements Entity.LinkFactory<EntityType>
    {

    public static final String IDENT_FIELD = "ident" ;
    public static final String IDENT_TOKEN = "{ident}" ;
    public static final String IDENT_REGEX = "\\{ident\\}" ;

    @Value("${firethorn.webapp.baseurl:null}")
    private String baseurl ;
    protected String baseurl()
        {
        if ("null".equals(this.baseurl))
            {
            return null;
            }
        else {
            return this.baseurl;
            }
        }

    protected WebappLinkFactory(final String path)
        {
        super(path);
        }

    protected String link(final String path, final Entity entity)
        {
        return link(
            path,
            entity.ident().value().toString()
            );
        }

    protected String link(final String path, final String ident)
        {
        UriComponentsBuilder builder ;
        if (baseurl() != null)
            {
            builder = UriComponentsBuilder.fromHttpUrl(
                baseurl()
                );
            }
        else {
            builder = ServletUriComponentsBuilder.fromCurrentContextPath();
            }
        return builder.path(
            path
            ).build().expand(
                ident
                ).toUriString();
        }
    }
