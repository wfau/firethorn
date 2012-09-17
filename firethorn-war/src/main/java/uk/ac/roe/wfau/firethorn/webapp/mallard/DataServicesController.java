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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;

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
     * MVC property for the request URL.
     *
     */
    public static final String REQUEST_URL = "firethorn.servlet.request.url" ;

    /**
     * Extract the request URL.
     *
     */
    @ModelAttribute(REQUEST_URL)
    public String requestUrl(
        final HttpServletRequest request
        ){
        return request.getRequestURL().toString();
        }

    /**
     * HTML GET request to display the index page.
     *
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView htmlIndex(
	    @ModelAttribute(REQUEST_URL) final String url,
	    final ModelAndView model
	    ){
		model.setViewName(
		    "adql/services/index"
		    );
        return model ;
        }

    /**
     * HTML GET request to select all.
     *
     */
	@RequestMapping(value=SELECT_PATH, method=RequestMethod.GET)
	public ModelAndView htmlSelect(
	    @ModelAttribute(REQUEST_URL) final String url,
	    final ModelAndView model
	    ){
		model.addObject(
		    SELECT_RESULT,
            womble().services().select()
		    );
		model.setViewName(
		    "adql/services/select"
		    );
        return model ;
        }

    /**
     * JSON GET request to select all.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces="application/json")
    public Iterable<DataServiceBean> jsonSelect(
	    @ModelAttribute(REQUEST_URL) final String url,
        final ModelAndView model
        ){
        return new DataServiceBeanIterable(
            womble().services().select()
            );
        }
	
    /**
     * HTML GET or POST request to select by name.
     *
     */
	@RequestMapping(value=SELECT_PATH, params=SELECT_NAME)
	public ModelAndView htmlSelect(
	    @ModelAttribute(REQUEST_URL) final String url,
        @RequestParam(SELECT_NAME)   final String name,
	    final ModelAndView model
	    ){
		model.addObject(
		    SELECT_RESULT,
            womble().services().select(
                name
                )
		    );
		model.setViewName(
		    "adql/services/select"
		    );
        return model ;
        }

    /**
     * JSON GET or POST request to select by name.
     *
     */
    @ResponseBody
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces="application/json")
    public Iterable<DataServiceBean> jsonSelect(
	    @ModelAttribute(REQUEST_URL) final String url,
        @RequestParam(SELECT_NAME)   final String name,
        final ModelAndView model
        ){
        return new DataServiceBeanIterable(
            womble().services().select(
                name
                )
            );
        }
	
    /**
     * HTML GET request to display the search form.
     *
     */
	@RequestMapping(value=SEARCH_PATH, method=RequestMethod.GET)
	public ModelAndView htmlSearch(
	    @ModelAttribute(REQUEST_URL) final String url,
	    final ModelAndView model
	    ){
		model.setViewName(
		    "adql/services/search"
		    );
        return model ;
        }

    /**
     * HTML GET or POST request to search by text.
     *
     */
	@RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT)
	public ModelAndView htmlSearch(
	    @ModelAttribute(REQUEST_URL) final String url,
        @RequestParam(SEARCH_TEXT)   final String text,
	    final ModelAndView model
	    ){
		model.addObject(
		    SEARCH_RESULT,
            womble().services().search(
                text
                )
		    );
		model.setViewName(
		    "adql/services/search"
		    );
        return model ;
        }

    /**
     * JSON GET or POST request to search by text.
     *
     */
    @ResponseBody
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces="application/json")
    public Iterable<DataServiceBean> jsonSearch(
	    @ModelAttribute(REQUEST_URL) final String url,
        @RequestParam(SEARCH_TEXT)   final String text,
        final ModelAndView model
        ){
        return new DataServiceBeanIterable(
            womble().services().search(
                text
                )
            );
        }

    /**
     * HTML GET request to display the create form.
     *
     */
	@RequestMapping(value=CREATE_PATH, method=RequestMethod.GET)
	public ModelAndView htmlCreate(
	    @ModelAttribute(REQUEST_URL) final String url,
	    final ModelAndView model
	    ){
		model.setViewName(
		    "adql/services/create"
		    );
        return model ;
        }

    /**
     * HTML POST request to create a new DataService.
     *
     */
	@RequestMapping(value=CREATE_PATH, method=RequestMethod.POST)
	public ResponseEntity<String> htmlCreate(
	    @ModelAttribute(REQUEST_URL) final String url,
        @RequestParam(CREATE_NAME)   final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("htmlCreate(String name)");
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

    /**
     * JSON POST request to create a new DataService.
     *
     */
	@RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<DataServiceBean> jsonCreate(
	    @ModelAttribute(REQUEST_URL) final String url,
        @RequestParam(CREATE_NAME)   final String name,
        final WebRequest request,
	    final ModelAndView model
	    ){
        log.debug("jsonCreate(String name)");
        log.debug("  Name [{}]", name);
        //
        // Create the service.
        final DataService service = womble().services().create(
            name
            );
        final PathBuilder builder = new SpringPathBuilder(
            request
            );
        return new ResponseEntity<DataServiceBean>(
            new DataServiceBean(
                service
                ),
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

