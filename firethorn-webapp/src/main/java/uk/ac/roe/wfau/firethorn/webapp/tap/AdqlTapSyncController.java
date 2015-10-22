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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.jdbc.Connection;

import uk.ac.roe.wfau.firethorn.webapp.votable.*;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapError;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapJobParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.CommonParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.CapabilitiesGenerator;
import uk.ac.roe.wfau.firethorn.blue.*;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;

   
@Slf4j
@Controller
@RequestMapping("/tap/{ident}/")
public class AdqlTapSyncController extends AbstractController {
	
	/**
	 * Timeout for query job in miliseconds
	 */
	static final Integer TIMEOUT = 600000;
	
	/**
	 * Param to start a job
	 */
	static final Status STARTJOB = Status.RUNNING;

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
    @ModelAttribute("urn:adql.resource.entity")
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
     * Create a Synchronous query job
     * 
     */
	@RequestMapping(value="sync",  method = { RequestMethod.POST, RequestMethod.GET }, produces=CommonParams.TEXT_XML_MIME)
	public void createSyncJob(
        @ModelAttribute("urn:adql.resource.entity")
        AdqlResource resource,
        final HttpServletResponse response,
        @RequestParam(value="QUERY", required = false) String QUERY,
        @RequestParam(value="LANG", required = false) String LANG,
        @RequestParam(value="REQUEST", required = false) String REQUEST,    
        BindingResult result
        ) throws  IdentifierNotFoundException, IOException {
			

	        response.setContentType(
	        		CommonParams.TEXT_XML_MIME
	            );
	        response.setCharacterEncoding(
	            "UTF-8"
	            );
        
			PrintWriter writer = response.getWriter();
			AdqlSchema schema;
			// Check input parameters and return VOTable with appropriate message if any errors found
			boolean check = checkParams(writer, REQUEST, LANG, QUERY);
			
			if (check){

				if (REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)){
					capgenerator.generateCapabilities(writer,resource);
				} else if (REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_DO_QUERY)){ 
				
					// Look for default query schema, if none found create one
					try {
						schema = resource.schemas().select(TapJobParams.DEFAULT_QUERY_SCHEMA);
					} catch (final NameNotFoundException ouch) {
						schema = resource.schemas().create(TapJobParams.DEFAULT_QUERY_SCHEMA);
					}
					
					try {
						
					
						 BlueQuery query = resource.blues().create(
				                		QUERY,
				                		TaskState.COMPLETED,
				                		Long.valueOf(600000)
				                    );
				           
				            /*
							//Create initial query element
							AdqlQuery query = schema.queries().create(
						                factories().adql().queries().params().create(
						                    ),
						                    QUERY
						                );
						
							
							
								// Prepare and execute query 
								Status jobstatus = query.prepare();
								if (jobstatus == Status.READY){
									jobstatus = query.execute();
								}*/
							
							// Write results t VOTable using AdqlQueryVOTableController	
						 if (query!=null){
							AdqlQueryVOTableController adqvotable = new AdqlQueryVOTableController();
							adqvotable.generateTAPVotable(writer,query);

						}
					
		
					} catch (final Exception ouch) {
						log.error("Exception caught [{}]", ouch);
						ouch.printStackTrace();
			        }
					
				} 
			}
			
        }
	
		private boolean checkParams(PrintWriter writer, String REQUEST,String LANG,String QUERY){

			String error_message;
			boolean valid = true;
			
			// Check for errors and return appropriate VOTable error messages		
			if (REQUEST==null){
				TapError.writeErrorToVotable(TapJobErrors.PARAM_REQUEST_MISSING, writer);
				valid = false;
				return valid;
			} else if (!REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_DO_QUERY) && !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)){
				error_message = "Invalid REQUEST: " + REQUEST;
				TapError.writeErrorToVotable(error_message, writer);
				valid = false;
				return valid;
			}
			
			
			if (LANG==null && !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)){
				TapError.writeErrorToVotable(TapJobErrors.PARAM_LANGUAGE_MISSING, writer);
				valid = false;
				return valid;
			}  else if (LANG!=null){
				if (!LANG.equalsIgnoreCase("ADQL") && !LANG.equalsIgnoreCase("PQL")){
					error_message = "Invalid LANGUAGE: " + LANG;
					TapError.writeErrorToVotable(error_message, writer);
					valid = false;
				}
			} 
			
			if (QUERY==null && !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)){
				TapError.writeErrorToVotable(TapJobErrors.PARAM_QUERY_MISSING, writer);
				valid = false;
				return valid;
			}

		
			
			return valid;
			
		}
	 
}
