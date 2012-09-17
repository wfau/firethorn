/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.roe.wfau.firethorn.common.womble.Womble;

/**
 * Base class for our MVC controllers.
 *
 */
@Controller
public class ControllerBase
    {

    /**
     * Autowired service access point.
     *
     */
    @Autowired
    private Womble womble ;

    /**
     * Our service access point.
     *
     */
    public Womble womble()
        {
        return this.womble ;
        }

    /**
     * MVC property for the request URL.
     *
     */
    public static final String REQUEST_URL = "firethorn.servlet.request.url" ;

    /**
     * Extract the request URL.
     *
     */
    @ModelAttribute(REQUEST_URL)
    public String requestUrl(
        final HttpServletRequest request
        ){
        return request.getRequestURL().toString();
        }

    /**
     * MVC property for the Spring WebRequest PathBuilder.
     *
    public static final String SPRING_PATH_BUILDER = "spring.path.builder" ;
     */

    /**
     * Create the PathBuilder for a request.
     *
    @ModelAttribute(SPRING_PATH_BUILDER)
    public PathBuilder paths(
        WebRequest request
        ){
        return new SpringPathBuilder(
            request
            );
        }
     */

    }

