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
package uk.ac.roe.wfau.firethorn.webapp.mallard;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.roe.wfau.firethorn.webapp.control.ControllerBase;
import uk.ac.roe.wfau.firethorn.mallard.DataService ;

/**
 * Spring MVC controller for AdqlServices.
 *
 */
@Slf4j
@Controller
@RequestMapping(DataServiceController.CONTROLLER_PATH)
public class DataServiceController
extends ControllerBase
    {
    /**
     * URL path for this Controller.
     *
     */
    public static final String CONTROLLER_PATH = "adql/service/{ident}" ;

    /**
     * MVC property for a Service entity.
     *
     */
    public static final String SERVICE_ENTITY = "adql.service.entity" ;

    /**
     * GET request for a service.
     *
     */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView select(
	    @PathVariable("ident") final String ident,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("select(String ident)");
        log.debug("  Ident [{}]", ident);

        //
        // Try locating the service.
        try {
            final DataService service = womble().services().select(
                womble().services().ident(
                    ident
                    )
                );

		    model.addObject(
		        SERVICE_ENTITY,
		        service
		        );

		    model.setViewName(
		        "adql/services/display"
		        );

            return model ;
            }

        catch (final Exception ouch)
            {
            return null ;
            }
        }
    }

