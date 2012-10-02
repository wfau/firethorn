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

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.roe.wfau.firethorn.webapp.control.ControllerBase;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.paths.PathImpl;
import uk.ac.roe.wfau.firethorn.webapp.paths.UriBuilder;

/**
 * Spring MVC controller for JdbcResources.
 *
 */
@Slf4j
@Controller
@RequestMapping(JdbcResourceController.CONTROLLER_PATH)
public class JdbcResourceController
    extends ControllerBase
    {
    /**
     * URI path for this Controller.
     *
     */
    public static final String CONTROLLER_PATH = "jdbc/resource/{ident}" ;

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
    public JdbcResourceController()
        {
        super();
        }

    /**
     * MVC property for the target JdbcResource entity.
     *
     */
    public static final String TARGET_ENTITY = "urn:jdbc.resource.entity" ;

    /**
     * GET request for a resource.
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
                new JdbcResourceBean(
                    this.builder(
                        request
                        ),
                    womble().resources().jdbc().select(
                        womble().resources().jdbc().ident(
                            ident
                            )
                        )
                    )
                );
            model.setViewName(
                "jdbc/resource/display"
                );

            return model ;
            }
        catch (final Exception ouch)
            {
            return null ;
            }
        }

    /**
     * JSON GET request for a resource.
     *
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET, produces=JSON_MAPPING)
    public JdbcResourceBean jsonSelect(
        @PathVariable("ident")
        final String ident,
        final ModelAndView model,
        final HttpServletRequest request
        ){
        try {
            return new JdbcResourceBean(
                this.builder(
                    request
                    ),
                womble().resources().jdbc().select(
                    womble().resources().jdbc().ident(
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
