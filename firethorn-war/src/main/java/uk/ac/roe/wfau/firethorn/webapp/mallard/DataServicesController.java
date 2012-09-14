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

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import uk.ac.roe.wfau.firethorn.webapp.control.ControllerBase;
import uk.ac.roe.wfau.firethorn.webapp.control.PathBuilder;
import uk.ac.roe.wfau.firethorn.webapp.control.SpringPathBuilder;
import uk.ac.roe.wfau.firethorn.webapp.control.LocationHeaders;
import uk.ac.roe.wfau.firethorn.webapp.mallard.DataServiceController.DataServiceBean;

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
     * URL path for the select method.
     * 
     */
    public static final String SELECT_PATH = "select" ;

    /**
     * URL path for the search method.
     * 
     */
    public static final String SEARCH_PATH = "search" ;

    /**
     * URL path for the create method.
     * 
     */
    public static final String CREATE_PATH = "create" ;

    /**
     * MVC property for the Service name.
     *
     */
    public static final String SELECT_NAME = "adql.services.select.name" ;

    /**
     * MVC property for the selected Service(s).
     *
     */
    public static final String SELECT_RESULT = "adql.services.select.result" ;

    /**
     * MVC property for the search text.
     *
     */
    public static final String SEARCH_TEXT = "adql.services.search.text" ;

    /**
     * MVC property for the selected Service(s).
     *
     */
    public static final String SEARCH_RESULT = "adql.services.search.result" ;

    /**
     * MVC property for the Service name.
     *
     */
    public static final String CREATE_NAME = "adql.services.create.name" ;

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
		    "adql/services/index"
		    );

        return model ;
        }

    /**
     * GET request to select all.
     *
     */
	@RequestMapping(value=SELECT_PATH, method=RequestMethod.GET)
	public ModelAndView select(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("select()");

        final Iterable<DataService> services = womble().services().select();

		model.addObject(
		    SELECT_RESULT,
		    services
		    );

		model.setViewName(
		    "adql/services/select"
		    );

        return model ;

        }

    /**
     * GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces="application/json")
    public Iterable<DataServiceController.DataServiceBean> jsonSelect(
        final WebRequest request,
        final ModelAndView model
        ){
        log.debug("select()");

        final Iterable<DataService> services = womble().services().select();

        return new Iterable<DataServiceController.DataServiceBean>()
            {
            public Iterator<DataServiceBean> iterator()
                {
                return new Iterator<DataServiceBean>()
                    {
                    Iterator<DataService> inner = services.iterator();
                    public boolean hasNext()
                        {
                        return inner.hasNext();
                        }

                    public DataServiceBean next()
                        {
                        return new DataServiceBean(
                            null,
                            inner.next()
                            );
                        }
                    public void remove()
                        {
                        inner.remove();
                        }
                    };
                }
            };
        }
	
	
    /**
     * GET request to select by name.
     *
     */
	@RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, params=SELECT_NAME)
	public ModelAndView select(
        @RequestParam(SELECT_NAME) final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("select(String name)");

        final Iterable<DataService> services = womble().services().select(
            name
            );

		model.addObject(
		    SELECT_NAME,
            name
		    );

		model.addObject(
		    SELECT_RESULT,
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
	@RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET)
	public ModelAndView search(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("search()");

		model.setViewName(
		    "adql/services/search"
		    );

        return model ;

        }

    /**
     * GET request to search by text.
     *
     */
	@RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET, params=SEARCH_TEXT)
	public ModelAndView search(
        @RequestParam(SEARCH_TEXT) final String text,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("search(String text)");

        final Iterable<DataService> services = womble().services().search(
            text
            );

		model.addObject(
		    SEARCH_TEXT,
            text
		    );

		model.addObject(
		    SEARCH_RESULT,
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
	@RequestMapping(value=CREATE_PATH, method=RequestMethod.GET)
	public ModelAndView create(
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("create()");

		model.setViewName(
		    "adql/services/create"
		    );

        return model ;
        }

    /**
     * POST request to create a new DataService.
     *
     */
	@RequestMapping(value=CREATE_PATH, method=RequestMethod.POST)
	public ResponseEntity<String> create(
        @RequestParam(CREATE_NAME) final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("create(String name)");
        log.debug("  Name [{}]", name);

        //
        // Create the service.
        final DataService service = womble().services().create(
            name
            );

        final PathBuilder builder = new SpringPathBuilder(
            request
            );

        return new ResponseEntity<String>(
            new LocationHeaders(
                builder.location(
                    //DataServiceController.CONTROLLER_PATH,
                    "adql/service",
                    service
                    )
                ),
            HttpStatus.SEE_OTHER
            );
        }
    }

