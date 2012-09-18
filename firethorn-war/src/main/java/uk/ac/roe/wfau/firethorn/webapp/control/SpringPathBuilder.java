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

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.request.WebRequest;

/**
 * A Spring controller path builder.
 *
 */
@Slf4j
public class SpringPathBuilder
extends PathBuilderBase
implements PathBuilder
    {

    /**
     * Public constructor.
     *
     */
    public SpringPathBuilder(final WebRequest request)
        {
        log.debug("SpringPathBuilder(WebRequest)");
        log.debug(" Context [{}]", request.getContextPath());
        this.request = request ;
        }

    /**
     * Our WebRequest.
     *
     */
    private final WebRequest request ;

    /**
     * Access to our WebRequest.
     *
     */
    public WebRequest request()
        {
        return this.request ;
        }

    /**
     * Get the request context path.
     *
     */
    @Override
    public String getContextPath()
        {
        return request.getContextPath();
        }
    }

