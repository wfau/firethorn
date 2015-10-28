package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.io.PrintWriter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;


/**
 * Generate capabilities of a resource
 * @author stelios
 *
 */

@Component
public class CapabilitiesGenerator{

	 @Value("${firethorn.webapp.baseurl:null}")
	 private String baseurl;

	/**
	 * Get baseurl
	 * 
	 * @return baseurl
	 */
	public String getBaseurl() {
		return baseurl;
	}

	/**
	 * Set baseurl
	 * 
	 * @return
	 */
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
    /**
     * Generate xml of capabilities into writer
     *
     */
    public String generateCapabilities(
        AdqlResource resource
        ){
    	
		StringBuilder writer = new StringBuilder();
		
    	writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		    "<vosi:capabilities xmlns:vosi=\"http://www.ivoa.net/xml/VOSICapabilities/v1.0\" xmlns:vod=\"http://www.ivoa.net/xml/VODataService/v1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
		    " <capability standardID=\"ivo://ivoa.net/std/VOSI#capabilities\">" +
		    "    <interface xsi:type=\"vod:ParamHTTP\" role=\"std\" version=\"1.0\">" +
		    "      <accessURL use=\"full\">" + getBaseurl() + "/tap/" + resource.ident() + "/capabilities</accessURL>" +
		    "    </interface>" +
		    "  </capability>" +
		    "  <capability standardID=\"ivo://ivoa.net/std/VOSI#availability\">" +
		    "    <interface xsi:type=\"vod:ParamHTTP\" role=\"std\" version=\"1.0\">" +
		    "      <accessURL use=\"full\">" + getBaseurl() + "/tap/" + resource.ident() + "/availability</accessURL>" +
		    "    </interface>" +
		    "  </capability>" +
		    "  <capability standardID=\"ivo://ivoa.net/std/VOSI#tables\">" +
		    "    <interface xsi:type=\"vod:ParamHTTP\" role=\"std\" version=\"1.0\">" +
		    "      <accessURL use=\"full\">" + getBaseurl() + "/tap/" + resource.ident() + "/tables</accessURL>" +
		    "    </interface>" +
		    "    <!-- RESTful VOSI-tables-1.1 would be use=\"base\" -->" +
		    "  </capability>" +
		    "  <!-- TAP-1.1 sync -->" +
		    "  <capability standardID=\"ivo://ivoa.net/std/TAP#sync-1.1\">" +
		    "    <interface xsi:type=\"vod:ParamHTTP\" role=\"std\" version=\"1.0\">" +
		    "      <accessURL use=\"base\">" + getBaseurl() + "/tap/" + resource.ident() + "/sync</accessURL>" +
		    "    </interface>" +
		    "   </capability>" +
		    "  <!-- TAP-1.1 async -->" +
		    "    <capability standardID=\"ivo://ivoa.net/std/TAP#async-1.1\">" +
		    "      <interface xsi:type=\"vod:ParamHTTP\" role=\"std\" version=\"1.0\">" +
		    "        <accessURL use=\"base\">" + getBaseurl() + "/tap/" + resource.ident() + "/async</accessURL>" +
		    "      </interface>" +
		    "     </capability>" +
		    "  </vosi:capabilities>" 
		);
        // Based on VOTable-1.3 specification.
        // http://www.ivoa.net/documents/VOTable/20130315/PR-VOTable-1.3-20130315.html

    	return writer.toString();
    	
    	}

}