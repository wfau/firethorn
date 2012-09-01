/**
 *
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.control;

import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.roe.wfau.firethorn.common.womble.Womble;

/**
 * Test MVC controller.
 *
 */
@Controller
public class ControllerBase
    {

    /**
     * Get the Session ID from a WebRequest.
     *
    public static String sid(WebRequest request)
        {
        return request.getSessionId();
        }
     */

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

