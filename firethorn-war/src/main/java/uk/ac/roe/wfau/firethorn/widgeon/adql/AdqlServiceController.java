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

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;

/**
 * Spring MVC controller for <code>AdqlService</code>.
 *
 */
@Slf4j
@Controller
@RequestMapping(AdqlServiceController.CONTROLLER_PATH )
public class AdqlServiceController
extends AbstractController
    {
    /**
     * URL path for this Controller.
     *
     */
    public static final String CONTROLLER_PATH = AdqlServiceIdentFactory.IDENT_PATH ;

    @Override
    public Path path()
        {
        return new PathImpl(
            CONTROLLER_PATH
            );
        }

    /**
     * Public constructor.
     *
     */
    public AdqlServiceController()
        {
        super();
        }

    /**
     * MVC property for the target AdqlService entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:adql.service.entity" ;

    /**
     * HTML GET request for a service.
     * @todo Wrap the entity as a bean (with a URI)
     *
     */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView htmlSelect(
	    @PathVariable("ident")
	    final String ident,
        final ModelAndView model,
        final HttpServletRequest request
	    ){
        try {
		    model.addObject(
		        TARGET_ENTITY,
	            new AdqlServiceBean(
	                womble().adql().services().select(
	                    womble().adql().services().ident(
	                        ident
	                        )
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
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public AdqlServiceBean jsonSelect(
        @PathVariable("ident")
        final String ident,
        final ModelAndView model,
        final HttpServletRequest request
        ){
        try {
            return new AdqlServiceBean(
                womble().adql().services().select(
                    womble().adql().services().ident(
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

