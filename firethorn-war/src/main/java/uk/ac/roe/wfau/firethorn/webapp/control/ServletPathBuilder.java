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

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import uk.ac.roe.wfau.firethorn.common.entity.Entity;
import uk.ac.roe.wfau.firethorn.common.entity.Identifier;

/**
 * A Servlet path builder.
 *
 */
@Slf4j
public class ServletPathBuilder
extends PathBuilderBase
    {

    /**
     * Public constructor.
     *
     */
    public ServletPathBuilder(final HttpServletRequest request)
        {
        log.debug("ServletPathBuilder(HttpServletRequest)");
        log.debug(" Request [{}]", request.getRequestURI());
        log.debug(" Context [{}]", request.getContextPath());
        log.debug(" Servlet [{}]", request.getServletPath());
        this.request = request ;
        }

    /**
     * Our ServletRequest.
     *
     */
    private final HttpServletRequest request ;

    /**
     * Access to our ServletRequest.
     *
     */
    public HttpServletRequest request()
        {
        return this.request ;
        }

    @Override
    public String getContextPath()
        {
        return request.getContextPath();
        }
    }

