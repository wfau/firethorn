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

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;

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
     * HTML GET request for a service.
     *
     */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView htmlSelect(
	    @ModelAttribute(REQUEST_URL) final String url,
	    @PathVariable("ident") final String ident,
	    final ModelAndView model
	    ){
        try {
		    model.addObject(
		        SERVICE_ENTITY,
                womble().services().select(
                    womble().services().ident(
                        ident
                        )
                    )
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

	/**
     * JSON GET request for a service.
     *
     */
	@ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces="application/json")
    public DataServiceBean jsonGet(
	    @ModelAttribute(REQUEST_URL) final String url,
        @PathVariable("ident") final String ident,
        final ModelAndView model
        ){
        log.debug("select(String ident)");
        log.debug("  Ident [{}]", ident);
        try {
            return new DataServiceBean(
                url,
                womble().services().select(
                    womble().services().ident(
                        ident
                        )
                    )
                );
            }
        catch (final Exception ouch)
            {
            return null ;
            }
        }
    }

