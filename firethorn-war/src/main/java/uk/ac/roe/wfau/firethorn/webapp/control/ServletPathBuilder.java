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

import javax.servlet.http.HttpServletRequest;

/**
 * A Servlet path builder.
 *
 */
@Slf4j
public class ServletPathBuilder
extends PathBuilder
    {

    /**
     * Public constructor.
     *
     */
    public ServletPathBuilder(HttpServletRequest request)
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
    private HttpServletRequest request ;

    /**
     * Access to our ServletRequest.
     *
     */
    public HttpServletRequest request()
        {
        return this.request ;
        }

    /**
     * Get the request context path.
     *
     */
    public String getContextPath()
        {
        return request.getContextPath();
        }
    }

