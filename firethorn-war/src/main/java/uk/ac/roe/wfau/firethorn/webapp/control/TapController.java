/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.control;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Test MVC controller.
 *
 */
@Slf4j 
@Controller
@RequestMapping("tap")
public class TapController
    {

    /**
     * Page counter.
     *
     */
    private static long count ;

    /**
     * Get request.
     * 
     */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView get(
	    ModelAndView model
	    ){

        log.debug("page access [{}]", count++);

		model.setViewName(
		    "test"
		    );

        return model ;

        }

    /**
     * Post request.
     * 
     */
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView post(
	    ModelAndView model
	    ){

        log.debug("page access [{}]", count++);

		model.setViewName(
		    "test"
		    );

        return model ;

        }

    }

