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
public class ControllerData<T>
    {

    /**
     * Spring model attribute name.
     *
     */
    public static final String MODEL_ATTRIB = "target" ;

    public ControllerData(WebRequest request, T target)
        {
        this.target  = target  ;
        this.request = request ;
        }

    private WebRequest request ;
    public WebRequest request()
        {
        return this.request ;
        }

    /**
     * Our target object.
     *
     */
    private T target;
    public  T target()
        {
        return this.target ;
        }

    }

