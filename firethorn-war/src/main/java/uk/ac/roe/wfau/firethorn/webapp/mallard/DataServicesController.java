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

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import uk.ac.roe.wfau.firethorn.webapp.control.ControllerBase;
import uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder;
import uk.ac.roe.wfau.firethorn.webapp.control.SpringPathBuilder;
import uk.ac.roe.wfau.firethorn.webapp.control.LocationHeaders;

import uk.ac.roe.wfau.firethorn.mallard.DataService ;

/**
 * Spring MVC controller for AdqlServices.
 *
 */
@Slf4j
@Controller
@RequestMapping(DataServicesController.CONTROLLER_PATH)
public class DataServicesController
extends ControllerBase
    {
    /**
     * URL path for this Controller.
     *
     */
    public static final String CONTROLLER_PATH = "adql/services" ;

    /**
     * MVC property for a Iterable list of Service(s).
     *
     */
    public static final String SERVICE_ITER_PROPERTY = "adql.services.iterator" ;

    /**
     * MVC property for the Service name.
     *
     */
    public static final String SELECT_NAME_PROPERTY = "adql.services.select.name" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT_PROPERTY = "adql.services.search.text" ;

    /**
     * MVC property for the Service name.
     *
     */
    public static final String CREATE_NAME_PROPERTY = "adql.services.create.name" ;

    /**
     * MVC property for a created Service.
     *
     */
    public static final String CREATE_SERVICE_ENTITY = "adql.services.create.entity" ;

    /**
     * GET request for the index page.
     *
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView index(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("AdqlServicesController.index()");

		model.setViewName(
		    "adql/services/index"
		    );

        return model ;
        }

    /**
     * GET request to select all.
     *
     */
	@RequestMapping(value="select", method=RequestMethod.GET)
	public ModelAndView select(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("AdqlServicesController.select()");

        final Iterable<DataService> services = womble().services().select();

		model.addObject(
		    SERVICE_ITER_PROPERTY,
		    services
		    );

		model.setViewName(
		    "adql/services/select"
		    );

        return model ;

        }

    /**
     * GET request to select by name.
     *
     */
	@RequestMapping(value="select", method=RequestMethod.GET, params=SELECT_NAME_PROPERTY)
	public ModelAndView select(
        @RequestParam(SELECT_NAME_PROPERTY) final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("AdqlServicesController.select(String name)");

        final Iterable<DataService> services = womble().services().select(
            name
            );

		model.addObject(
		    SELECT_NAME_PROPERTY,
            name
		    );

		model.addObject(
		    SERVICE_ITER_PROPERTY,
            services
		    );

		model.setViewName(
		    "adql/services/select"
		    );

        return model ;

        }

    /**
     * GET request to search with no params.
     * (displays the HTML form)
     *
     */
	@RequestMapping(value="search", method=RequestMethod.GET)
	public ModelAndView search(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("AdqlServicesController.search()");

		model.setViewName(
		    "adql/services/search"
		    );

        return model ;

        }

    /**
     * GET request to search by text.
     *
     */
	@RequestMapping(value="search", method=RequestMethod.GET, params=SEARCH_TEXT_PROPERTY)
	public ModelAndView search(
        @RequestParam(SEARCH_TEXT_PROPERTY) final String text,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("AdqlServicesController.search(String text)");

        final Iterable<DataService> services = womble().services().search(
            text
            );

		model.addObject(
		    SEARCH_TEXT_PROPERTY,
            text
		    );

		model.addObject(
		    SERVICE_ITER_PROPERTY,
            services
		    );

		model.setViewName(
		    "adql/services/search"
		    );

        return model ;
        }


    /**
     * GET request to create a new DataService.
     * (displays the HTML form)
     *
     */
	@RequestMapping(value="create", method=RequestMethod.GET)
	public ModelAndView create(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("AdqlServicesController.create()");

		model.setViewName(
		    "adql/services/create"
		    );

        return model ;
        }

    /**
     * POST request to create a new DataService.
     *
     */
	@RequestMapping(value="create", method=RequestMethod.POST)
	public ResponseEntity<String> create(
        @RequestParam(CREATE_NAME_PROPERTY) final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("AdqlServicesController.create(String name)");
        log.debug("  Name [{}]", name);

        //
        // Try creating the service.
        final DataService service = womble().services().create(
            name
            );

        final PathBuilder builder = new SpringPathBuilder(
            request
            );

        return new ResponseEntity<String>(
            new LocationHeaders(
                builder.location(
                    "adql/service",
                    service
                    )
                ),
            HttpStatus.SEE_OTHER
            );
        }
    }

