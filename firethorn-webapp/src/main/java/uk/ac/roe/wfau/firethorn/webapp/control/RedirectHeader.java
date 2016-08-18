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
package uk.ac.roe.wfau.firethorn.webapp.control ;

import java.net.URI;

import org.springframework.http.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

/**
 * Extension of the Spring HttpHeaders to set the location.
 *
 */
@Slf4j
public class RedirectHeader
extends HttpHeaders
    {

    /**
     *
     *
     */
    private static final long serialVersionUID = 7232751705032547280L;

    /**
     * Public constructor.
     *
     */
    public RedirectHeader(final EntityBean<?> bean)
        {
        this(
            bean.getIdent()
            );
        }

    /**
     * Public constructor.
     *
     */
    public RedirectHeader(final URI uri)
        {
        super();
        log.debug("RedirectHeader [{}]", uri);
        this.setLocation(
            uri
            );
        }
    }

