/**
 *
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
    @ModelAttribute(ControllerData.MODEL_ATTRIB)
    public ControllerData<Long> data(
        WebRequest request,
        @PathVariable("ident") String ident
        ){
        return new ControllerData<Long>(
            request,
            new Long(
                count
                )
            );
        }
     */

    /**
     * Get request.
     * 
     */
	@RequestMapping(value={"", "/", "/frog"}, method=RequestMethod.GET)
	public ModelAndView page(
	    ModelAndView model
	    ){

        log.debug("count [{}]", count++);

		model.setViewName(
		    "test"
		    );

        return model ;

        }

    /**
     * Get request.
     * 
     */
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
		    ControllerData.MODEL_ATTRIB,
		    data
		    );

		model.setViewName(
		    "test"
		    );

        return model ;

        }

    }

