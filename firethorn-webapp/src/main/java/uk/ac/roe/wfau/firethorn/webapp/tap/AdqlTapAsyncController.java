/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import adql.query.ADQLQuery;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.votable.AdqlQueryVOTableController;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/async")
public class AdqlTapAsyncController extends AbstractController {
	
	
   /**
	 * Default query schema
	 */
	static final String DEFAULT_QUERY_SCHEMA = "query_schema";
	
	
   /**
    * VOTable MIME type.
    *
    */
   public static final String VOTABLE_MIME = "application/x-votable+xml" ;
   
   /**
    * TextXml MIME type.
    *
    */
   public static final String TEXT_XML_MIME = "text/xml" ;

	@Override
	public Path path() {
		// TODO Auto-generated method stub
		return path("/tap/{ident}/async") ;
	}
	
	/**
     * Get the target workspace based on the ident in the path. 
     *
     */
    @ModelAttribute("urn:adql.resource.entity")
    public AdqlResource entity(
        @PathVariable("ident")
        final String ident
        ) throws IdentifierNotFoundException  {
        log.debug("entity() [{}]", ident);
        return factories().adql().resources().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    @RequestMapping(value="/{jobid}/phase", method = RequestMethod.GET)
	@ResponseBody
    public String phase(@PathVariable String jobid) {
        return jobid;
    }
    
    @RequestMapping(value="/{jobid}/quote", method = RequestMethod.GET)
	@ResponseBody
    public String quote(@PathVariable String jobid) {
        return "quote";
    }
    
    @RequestMapping(value="/{jobid}/executionduration", method = RequestMethod.GET	)
	@ResponseBody
    public String executionduration(@PathVariable String jobid) {
        return "executionduration";
    }
 
    @RequestMapping(value="/{jobid}/parameters", method = RequestMethod.POST)
	@ResponseBody
    public String parameters(@PathVariable String jobid) {
        return "parameters";
    }
    
    @RequestMapping(value="/{jobid}/destruction", method = RequestMethod.GET)
   	@ResponseBody
       public String destruction(@PathVariable String jobid) {
           return "destruction";
       }
    
    @RequestMapping(value="/{jobid}/error", method = RequestMethod.GET)
    
    public String error(@PathVariable String jobid) {
        return "error";
    }
    
    
    @RequestMapping(value="/{jobid}/results", method = RequestMethod.GET)
	@ResponseBody
    public String results(@PathVariable String jobid) {
        return "results";
    }
  
    
    /**
     * Web service method
     * Create an Async query job
     * 
     */
	@RequestMapping( method = RequestMethod.GET)
	@ResponseBody
	public String createAsyncJob(
        @ModelAttribute("urn:adql.resource.entity")
        AdqlResource resource,
        final HttpServletResponse response,
        @RequestParam(value="QUERY", required = false) String QUERY,
        @RequestParam(value="LANG", required = false) String LANG,
        @RequestParam(value="REQUEST", required = false) String REQUEST 
        ) throws IdentifierNotFoundException, IOException {
		
		
		PrintWriter writer = response.getWriter();
		AdqlSchema schema;
	
		// Check input parameters and return VOTable with appropriate message if any errors found
		boolean check = checkParams(writer, REQUEST, LANG, QUERY);
		
		if (check){
			
				try{
					schema = resource.schemas().select(DEFAULT_QUERY_SCHEMA);
				}  catch (final NameNotFoundException ouch) {
					schema = resource.schemas().create(DEFAULT_QUERY_SCHEMA);
				}
				
				try {
					
					AdqlQuery query = schema.queries().create(
					                QUERY
					                );
				
					if (query!=null){
					
						Status jobstatus = query.prepare();
						if (jobstatus == Status.READY){
							jobstatus = query.execute();
						}
						
						return "query";
	
					}
				

				} catch (final Exception ouch) {
					log.error("Exception caught [{}]", ouch);
					ouch.printStackTrace();

		        }
				

			} else {
				
				log.error("Invalid Params [{}][{}][{}]",REQUEST,LANG, QUERY);

			}
			return "query";
		
        }


	private boolean checkParams(PrintWriter writer, String REQUEST,String LANG,String QUERY){

		String error_message;
		boolean valid = true;
		
		// Check for errors and return appropriate VOTable error messages		
		if (REQUEST==null){
			XMLResponse.writeErrorToVotable(TapJobErrors.PARAM_REQUEST_MISSING, writer);
			valid = false;
			return valid;
		} else if (!REQUEST.equalsIgnoreCase("doQuery")){
			error_message = "Invalid REQUEST: " + REQUEST;
			XMLResponse.writeErrorToVotable(error_message, writer);
			valid = false;
			return valid;
		}
		
		
		if (LANG==null){
			XMLResponse.writeErrorToVotable(TapJobErrors.PARAM_LANGUAGE_MISSING, writer);
			valid = false;
			return valid;
		}  else if (!LANG.equalsIgnoreCase("ADQL") && !LANG.equalsIgnoreCase("PQL")){
			error_message = "Invalid LANGUAGE: " + LANG;
			XMLResponse.writeErrorToVotable(error_message, writer);
			valid = false;
		} 
		
		if (QUERY==null){
			XMLResponse.writeErrorToVotable(TapJobErrors.PARAM_QUERY_MISSING, writer);
			valid = false;
			return valid;
		}

	
		
		return valid;
		
	}
}
