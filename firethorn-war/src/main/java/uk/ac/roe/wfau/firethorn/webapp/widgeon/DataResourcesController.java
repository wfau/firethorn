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
package uk.ac.roe.wfau.firethorn.webapp.widgeon;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.webapp.control.ControllerBase;
import uk.ac.roe.wfau.firethorn.webapp.control.LocationHeaders;
import uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder;
import uk.ac.roe.wfau.firethorn.webapp.control.SpringPathBuilder;
import uk.ac.roe.wfau.firethorn.widgeon.base.BaseResource;

/**
 * Spring MVC controller for AdqlServices.
 *
 */
@Slf4j
@Controller
@RequestMapping(DataResourcesController.CONTROLLER_PATH)
public class DataResourcesController
extends ControllerBase
    {
    /**
     * URL path for this Controller.
     *
     */
    public static final String CONTROLLER_PATH = "adql/resources" ;

    /**
     * MVC property for the Resource name.
     *
     */
    public static final String SELECT_NAME = "adql.resources.select.name" ;

    /**
     * MVC property for the selected Resource(s).
     *
     */
    public static final String SELECT_RESULT = "adql.resources.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.resources.search.text" ;

    /**
     * MVC property for the selected Resource(s).
     *
     */
    public static final String SEARCH_RESULT = "adql.resources.search.result" ;

    /**
     * MVC property for the Resource name.
     *
     */
    public static final String CREATE_NAME = "adql.resources.create.name" ;

    /**
     * GET request for the index page.
     *
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView index(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("index()");

		model.setViewName(
		    "adql/resources/index"
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
        log.debug("select()");

        final Iterable<BaseResource> resources = womble().resources().base().select();

		model.addObject(
		    SELECT_RESULT,
		    resources
		    );

		model.setViewName(
		    "adql/resources/select"
		    );

        return model ;

        }

    /**
     * GET request to select by name.
     *
     */
	@RequestMapping(value="select", method=RequestMethod.GET, params=SELECT_NAME)
	public ModelAndView select(
        @RequestParam(SELECT_NAME) final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("select(String name)");

        final Iterable<BaseResource> resources = womble().resources().base().select(
            name
            );

		model.addObject(
		    SELECT_NAME,
            name
		    );

		model.addObject(
		    SELECT_RESULT,
		    resources 
		    );

		model.setViewName(
		    "adql/resources/select"
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
        log.debug("search()");

		model.setViewName(
		    "adql/resources/search"
		    );

        return model ;

        }

    /**
     * GET request to search by text.
     *
     */
	@RequestMapping(value="search", method=RequestMethod.GET, params=SEARCH_TEXT)
	public ModelAndView search(
        @RequestParam(SEARCH_TEXT) final String text,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("search(String text)");

        final Iterable<BaseResource> resources = womble().resources().base().search(
            text
            );

		model.addObject(
		    SEARCH_TEXT,
            text
		    );

		model.addObject(
		    SEARCH_RESULT,
		    resources
		    );

		model.setViewName(
		    "adql/resources/search"
		    );

        return model ;
        }


    /**
     * GET request to create a new DataResource.
     * (displays the HTML form)
     *
     */
	@RequestMapping(value="create", method=RequestMethod.GET)
	public ModelAndView create(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("create()");

		model.setViewName(
		    "adql/resources/create"
		    );

        return model ;
        }

    /**
     * POST request to create a new DataResource.
     *
     */
	@RequestMapping(value="create", method=RequestMethod.POST)
	public ResponseEntity<String> create(
        @RequestParam(CREATE_NAME) final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("create(String name)");
        log.debug("  Name [{}]", name);

        //
        // Try creating the resource.
        final BaseResource resource = womble().resources().jdbc().create(
            name
            );

        final PathBuilder builder = new SpringPathBuilder(
            request
            );

        return new ResponseEntity<String>(
            new LocationHeaders(
                builder.location(
                    "adql/resource",
                    resource
                    )
                ),
            HttpStatus.SEE_OTHER
            );
        }
    }

