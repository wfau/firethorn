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

import org.springframework.web.context.request.WebRequest;

/**
 * Test MVC controller data.
 *
 */
public class ControllerData
    {

    /**
     * Spring MVC property name.
     *
     */
    public static final String MODEL_PROPERTY = "firethorn.controller.data" ;


    /**
     * Protected constructor.
     *
     */
    protected ControllerData(final WebRequest request)
        {
        this.request = request ;
        }

    /**
     * Our web request.
     *
     */
    private final WebRequest request ;

    /**
     * Access to our web request.
     *
     */
    public WebRequest request()
        {
        return this.request ;
        }

    }

