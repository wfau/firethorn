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
package uk.ac.roe.wfau.firethorn.webapp.control;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Test MVC controller.
 *
 */
@Slf4j
@Controller
@RequestMapping("test")
public class TestController
extends ControllerBase
    {

    /**
     * Request counter.
     *
     */
    private static long count ;

    /**
     * Create our ControllerData for this request.
     * Always tries to create this, even if not required.
     *
     */
    @ModelAttribute(ControllerData.MODEL_PROPERTY)
    public ControllerData data(
        final WebRequest request,
        @PathVariable("ident") final String ident
        ){
        log.debug("TestController.data() [{}]", ident);

        return new ControllerData(
            request
            );

        }

    /**
     * Get request.
     *
     */
	@RequestMapping(value={"", "/", "/frog"}, method=RequestMethod.GET)
	public ModelAndView page(
	    final ModelAndView model
	    ){
        log.debug("TestController.page( [{}]");
        log.debug("count [{}]", count++);

		model.setViewName(
		    "test"
		    );

        return model ;

        }

	@RequestMapping(value="/{ident}", method=RequestMethod.GET)
	public ModelAndView page(
	    @ModelAttribute(ControllerData.MODEL_PROPERTY) final
	    ControllerData data,
	    final ModelAndView model
	    ){
        log.debug("TestController.page( [{}]");
        log.debug("count [{}]", count++);
        //log.debug("ident [{}]", data.target());

		model.setViewName(
		    "test"
		    );

        return model ;

        }

    /**
     * Get request.
     *
	@RequestMapping(value="/{ident}", method=RequestMethod.GET)
	public ModelAndView page(
        @PathVariable("ident") String ident,
        WebRequest request,
	    ModelAndView model
	    ){
        return page(
            new ControllerData<Long>(
                request,
                new Long(
                    count
                    )
                ),
            model
            );
        }

	public ModelAndView page(
        ControllerData<Long> data,
	    ModelAndView model
	    ){
        log.debug("count [{}]", count++);
        log.debug("ident [{}]", data.target());

		model.addObject(
		    ControllerData.MODEL_PROPERTY,
		    data
		    );

		model.setViewName(
		    "test"
		    );

        return model ;

        }
     */

    }

