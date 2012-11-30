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
package uk.ac.roe.wfau.firethorn.widgeon.adql;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.control.RedirectHeader;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlService</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlServiceIdentFactory.SERVICES_PATH)
public class AdqlServicesController
extends AbstractController
    {

    @Override
    public Path path()
        {
        return new PathImpl(
            AdqlServiceIdentFactory.SERVICES_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlServicesController()
        {
        super();
        }

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
     * HTML GET request to display the index page.
     *
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView htmlIndex(
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
        final ModelAndView model
	    ){
	    model.addObject(
		    SELECT_RESULT,
	        new AdqlServiceBeanIter(
	            womble().adql().services().select()
	            )
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
    @RequestMapping(value=SELECT_PATH, method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlServiceBeanIter jsonSelect(
        final ModelAndView model
        ){
        return new AdqlServiceBeanIter(
            womble().adql().services().select()
            );
        }

    /**
     * HTML GET or POST request to select by name.
     *
     */
	@RequestMapping(value=SELECT_PATH, params=SELECT_NAME)
	public ModelAndView htmlSelect(
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
	    ){
        model.addObject(
            SELECT_NAME,
            name
            );
		model.addObject(
		    SELECT_RESULT,
	        new AdqlServiceBeanIter(
	            womble().adql().services().select(
	                name
	                )
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
    @RequestMapping(value=SELECT_PATH, params=SELECT_NAME, produces=JSON_MAPPING)
    public AdqlServiceBeanIter jsonSelect(
        @RequestParam(SELECT_NAME)
        final String name,
        final ModelAndView model
        ){
        return new AdqlServiceBeanIter(
            womble().adql().services().select(
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
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
	    ){
        model.addObject(
            SEARCH_TEXT,
            text
            );
		model.addObject(
		    SEARCH_RESULT,
	        new AdqlServiceBeanIter(
	            womble().adql().services().search(
	                text
	                )
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
    @RequestMapping(value=SEARCH_PATH, params=SEARCH_TEXT, produces=JSON_MAPPING)
    public AdqlServiceBeanIter jsonSearch(
        @RequestParam(SEARCH_TEXT)
        final String text,
        final ModelAndView model
        ){
        return new AdqlServiceBeanIter(
            womble().adql().services().search(
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
        final ModelAndView model
	    ){
		model.setViewName(
		    "adql/services/create"
		    );
        return model ;
        }

    /**
     * HTML POST request to create a new AdqlService.
     *
     */
	@RequestMapping(value=CREATE_PATH, method=RequestMethod.POST)
	public ResponseEntity<String>  htmlCreate(
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
	    ){
	    log.debug("htmlCreate() [{}]", name);
        try {
            final AdqlServiceBean bean = null ;
            /*
            final AdqlServiceBean bean = new AdqlServiceBean(
                womble().adql().services().create(
                    name
                    )
                );
            */
            return new ResponseEntity<String>(
                new RedirectHeader(
                    bean
                    ),
                HttpStatus.SEE_OTHER
                );
            }
        catch (final Exception ouch)
            {
            return null ;
            }
        }

    /**
     * JSON POST request to create a new AdqlService.
     *
     */
	@RequestMapping(value=CREATE_PATH, method=RequestMethod.POST, produces=JSON_MAPPING)
	public ResponseEntity<AdqlServiceBean> jsonCreate(
        @RequestParam(CREATE_NAME)
        final String name,
        final ModelAndView model
	    ){
        log.debug("jsonCreate() [{}]", name);
        try {
            final AdqlServiceBean bean = null ;
            /*
            final AdqlServiceBean bean = new AdqlServiceBean(
                womble().adql().services().create(
                    name
                    )
                );
            */
            return new ResponseEntity<AdqlServiceBean>(
                bean,
                new RedirectHeader(
                    bean
                    ),
                HttpStatus.CREATED
                );
            }
        catch (final Exception ouch)
            {
            log.error("failed to create AdqlService [{}]", ouch);
            return null ;
            }
        }
    }

