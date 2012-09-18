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
public abstract class ControllerBase
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
     * The base URL path for this controller.
     * 
     */
    public abstract String path();
    
    /**
     * MVC property for our URL builder.
     *
     */
    public static final String URL_BUILDER = "firethorn.servlet.path.builder" ;
    
    /**
     * MVC property for the our UrlBuilder.
     *
     */
    @ModelAttribute(URL_BUILDER)
    public UrlBuilder urlBuilder(
        final HttpServletRequest request
        ){
        return new UrlBuilderImpl(
            request,
            new ServletPathBuilder(
                request
                ) 
            );
        }

    /**
     * MVC property for the Spring WebRequest PathBuilderBase.
     *
    public static final String SPRING_PATH_BUILDER = "spring.path.builder" ;
     */

    /**
     * Create the PathBuilderBase for a request.
     *
    @ModelAttribute(SPRING_PATH_BUILDER)
    public PathBuilderBase paths(
        WebRequest request
        ){
        return new SpringPathBuilder(
            request
            );
        }
     */

    }

