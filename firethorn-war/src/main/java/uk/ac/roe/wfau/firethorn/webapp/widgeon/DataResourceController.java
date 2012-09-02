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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.webapp.control.ControllerBase;
import uk.ac.roe.wfau.firethorn.widgeon.DataResource;

/**
 *
 *
 */
@Slf4j
@Controller
@RequestMapping(DataResourceController.CONTROLLER_PATH)
public class DataResourceController
    extends ControllerBase
    {
    /**
     * URL path for this Controller.
     *
     */
    public static final String CONTROLLER_PATH = "adql/resource/{ident}" ;

    /**
     * MVC property for a DataResource entity.
     *
     */
    public static final String RESOURCE_ENTITY = "adql.resource.entity" ;
    
    /**
     * GET request for a Resource.
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
        // Try locating the resource.
        try {
            final DataResource resource = womble().resources().select(
                womble().resources().ident(
                    ident
                    )
                );

            model.addObject(
                RESOURCE_ENTITY,
                resource
                );

            model.setViewName(
                "adql/resources/display"
                );

            return model ;
            }

        catch (Exception ouch)
            {
            return null ;
            }
        }
    
    }
