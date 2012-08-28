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
public class ControllerBase
    {

    /**
     * Session attribute name for the current user ID.
     * 
     */
    public static final String SESSION_UID = "firethorn.uid" ;

    /**
     * Get the Session ID from a WebRequest.
     * 
     */
    public String sid(WebRequest request)
        {
        return request.getSessionId();
        }


    
    }

