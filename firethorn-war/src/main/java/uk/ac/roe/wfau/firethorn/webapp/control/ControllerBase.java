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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

/**
 * Base class for our MVC controllers.
 *
 */
@Controller
public abstract class ControllerBase
    {

    /**
     * Autowired service access point.
     *
     */
    @Autowired
    private Womble womble ;

    /**
     * Our service access point.
     *
     */
    public Womble womble()
        {
        return this.womble ;
        }

    /**
     * The request path for this controller.
     * 
     */
    public abstract String path();

    /**
     * Our URL builder.
     * 
     */
    public UrlBuilder urls()
        {
        return null; 
        }
    
    /**
     * MVC property for our URL builder.
     *
     */
    public static final String URL_BUILDER = "firethorn.servlet.path.builder" ;
    
    /**
     * MVC property for the our UrlBuilder.
     *
     */
    @ModelAttribute(URL_BUILDER)
    public UrlBuilder urlBuilder(
        final HttpServletRequest request
        ){
        return new UrlBuilderImpl(
            request,
            new ServletPathBuilder(
                request
                ) 
            );
        }
    }

