package uk.ac.roe.wfau.firethorn.webapp.tap;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/")
public class AdqlTapExamplesController extends AbstractController {

	
    public static final String TARGET_ENTITY = "urn:adql.resource.entity" ;

	@Autowired
	private CapabilitiesGenerator capgenerator;
	
	@Override
	public Path path() {
		// TODO Auto-generated method stub
		return path("/tap/{ident}/") ;
	}
	 /**
     * Get the target workspace based on the ident in the path. 
     *
     */
    @ModelAttribute(TARGET_ENTITY)
    public AdqlResource entity(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException  {
        log.debug("entity() [{}]", ident);
        return factories().adql().resources().entities().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }


    /**
     * Web service method
     * Get the capabilities of a resource
     * 
     */
	@RequestMapping(value="examples", method = RequestMethod.GET, produces=CommonParams.TEXT_XML_MIME)
        public ModelAndView examples(
        @ModelAttribute(TARGET_ENTITY)
        AdqlResource resource,
        final HttpServletResponse response,
        HttpServletRequest request
        ) throws  IdentifierNotFoundException, IOException {
		
                ModelAndView modelAndview;

	        response.setContentType(
	        		CommonParams.TEXT_XML_MIME
	            );
	        response.setCharacterEncoding(
	            "UTF-8"
	            );
	        modelAndview = new ModelAndView("examples");
                     return modelAndview;                       
	}


	 
}
