/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Controller
@RequestMapping("test")
public class TestController
    {

    /**
     * Our debug logger.
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(TestController.class);

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
	public ModelAndView page(
	    ModelAndView model
	    ){

        logger.debug("page access {}", count++);

		model.setViewName(
		    "test"
		    );

        return model ;

        }
    }

