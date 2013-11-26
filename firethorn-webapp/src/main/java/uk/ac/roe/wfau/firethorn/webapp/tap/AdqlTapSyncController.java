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

import javax.persistence.EnumType;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.roe.wfau.firethorn.webapp.votable.*;

import adql.query.ADQLQuery;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery.Syntax.Level;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
@Slf4j
@Controller
@RequestMapping("/tap/{ident}/")
public class AdqlTapSyncController extends AbstractController {
	
	
	/**
	 * Default query schema
	 */
	static final String DEFAULT_QUERY_SCHEMA = "query_schema";
	
	/**
	 * Timeout for query job in miliseconds
	 */
	static final Integer TIMEOUT = 600000;
	
	/**
	 * Param to start a job
	 */
	static final Status STARTJOB = Status.RUNNING;
	
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
        return factories().adql().resources().select(
            factories().adql().resources().idents().ident(
                ident
                )
            );
        }

    
    
    /**
     * Web service method
     * Create an Async query job
     * 
     */
	@RequestMapping(value="sync", method = RequestMethod.GET, produces=TEXT_XML_MIME)
	public void createSyncJob(
        @ModelAttribute("urn:adql.resource.entity")
        AdqlResource resource,
        final HttpServletResponse response,
        @RequestParam("QUERY") String QUERY,
        @RequestParam("LANG") String LANG,
        @RequestParam("REQUEST") String REQUEST    
        ) throws NotFoundException, IOException {
		
		

			AdqlSchema schema;
		
		
			if (REQUEST.equalsIgnoreCase("doQuery") && LANG.equalsIgnoreCase("ADQL")){
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
						
						PrintWriter writer = response.getWriter();
						AdqlQueryVOTableController adqvotable = new AdqlQueryVOTableController();
						adqvotable.generateVotable(writer,query);
						
	
					}
				

				} catch (final Exception ouch) {
					log.error("Exception caught [{}]", ouch);
					ouch.printStackTrace();

		        }
				

			} else {
				
				log.error("Invalid Params [{}][{}][{}]",REQUEST,LANG, QUERY);

			}
		
		
			
			
        }



	 
}
