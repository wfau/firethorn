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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.ac.roe.wfau.firethorn.webapp.votable.*;
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
		return path("/tap/{ident}/");
	}

	/**
	 * Get the target workspace based on the ident in the path.
	 *
	 */
	@ModelAttribute("urn:adql.resource.entity")
	public AdqlResource entity(@PathVariable("ident") final String ident) throws IdentifierNotFoundException {
		log.debug("entity() [{}]", ident);
		return factories().adql().resources().entities().select(factories().adql().resources().idents().ident(ident));
	}

	/**
	 * Web service method Create a Synchronous query job
	 * 
	 */
	@RequestMapping(value = "sync", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = CommonParams.TEXT_XML_MIME)
	@ResponseStatus(value = HttpStatus.SEE_OTHER)
	public void sync(@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response, @RequestParam(value = "QUERY", required = false) String QUERY,
			@RequestParam(value = "LANG", required = false) String LANG,
			@RequestParam(value = "REQUEST", required = false) String REQUEST, 
			@RequestParam(value = "FORMAT", required = false) String FORMAT, 
			@RequestParam(value = "VERSION", required = false) String VERSION, 
			@RequestParam(value = "MAXREC", required = false) String MAXREC, 

			BindingResult result)
					throws IdentifierNotFoundException, IOException {

		String results = "";
		PrintWriter writer = response.getWriter();
		// Check input parameters and return VOTable with appropriate message if
		// any errors found
		boolean valid = checkParams(REQUEST, LANG, QUERY, FORMAT, VERSION);

		if (valid) {

			if (REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
				writer.append(capgenerator.generateCapabilities(resource));
				return;
			} else if (REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_DO_QUERY)) {

				try {

					BlueQuery query = resource.blues().create(QUERY, TaskState.COMPLETED,
							Long.valueOf(TapJobParams.EXECUTION_DURATION));

					// Write results to VOTable using AdqlQueryVOTableController
					if (query != null) {
						
						if (query.state() == TaskState.EDITING || 
								query.state() == TaskState.QUEUED || 
										query.state() == TaskState.READY || 
												query.state() == TaskState.RUNNING || 
														query.state() == TaskState.CANCELLED
								) {
							
							writer.append(TapError.writeErrorToVotable(TapJobErrors.FILE_NOTFOUND));
							return;
							
						} else if (query.state() == TaskState.FAILED || query.state() == TaskState.ERROR) {
							
							if (query.syntax().friendly()!=null){
								writer.append(TapError.writeErrorToVotable(query.syntax().friendly()));
							} else {
								writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
							}
							return;
							
						} else {
							results = query.results().adql().link() + "/votable";
							response.setStatus(HttpServletResponse.SC_SEE_OTHER);
						    response.setHeader("Location", results);
						    return;
						    
						}
					

					} 

				} catch (final Exception ouch) {
					log.error("Exception caught [{}]", ouch);
					TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR);
					return;
				}

			}
		} else {
			writer.append(getErrorVOTable(REQUEST, LANG, QUERY, FORMAT, VERSION));
		}
		
		writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
	}

	private boolean checkParams(String REQUEST, String LANG, String QUERY, String FORMAT, String VERSION) {

		String error_message;
		boolean valid = true;

		// Check for errors and return appropriate VOTable error messages
		if (REQUEST == null) {
			TapError.writeErrorToVotable(TapJobErrors.PARAM_REQUEST_MISSING);
			valid = false;
			return valid;
		} else if (!REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_DO_QUERY)
				&& !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			error_message = "Invalid REQUEST: " + REQUEST;
			TapError.writeErrorToVotable(error_message);
			valid = false;
			return valid;
		}

		if (LANG == null && !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			TapError.writeErrorToVotable(TapJobErrors.PARAM_LANGUAGE_MISSING);
			valid = false;
			return valid;
		} else if (LANG != null) {
			if (!LANG.equalsIgnoreCase("ADQL") && !LANG.equalsIgnoreCase("PQL")) {
				error_message = "Invalid LANGUAGE: " + LANG;
				TapError.writeErrorToVotable(error_message);
				valid = false;
			}
		}

		if (QUERY == null && !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			TapError.writeErrorToVotable(TapJobErrors.PARAM_QUERY_MISSING);
			valid = false;
			return valid;
		}

		if (FORMAT != null) {
			if (!FORMAT.equalsIgnoreCase("votable")) {
				error_message = "FORMAT '" + FORMAT + "'not supported" ;
				TapError.writeErrorToVotable(error_message);
				valid = false;
			}
		}
		
		if (VERSION != null) {
			if (!VERSION.equalsIgnoreCase("1.0") || !VERSION.equalsIgnoreCase("1")) {
				error_message = "VERSION '" + VERSION + "'not supported" ;
				TapError.writeErrorToVotable(error_message);
				valid = false;
			}
		}
		
		return valid;

	}
	
	private String getErrorVOTable(String REQUEST, String LANG, String QUERY, String FORMAT, String VERSION) {

		String error_message;

		// Check for errors and return appropriate VOTable error messages
		if (REQUEST == null) {
			return TapError.writeErrorToVotable(TapJobErrors.PARAM_REQUEST_MISSING);
		} else if (!REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_DO_QUERY)
				&& !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			error_message = "Invalid REQUEST: " + REQUEST;
			return TapError.writeErrorToVotable(error_message);
		}

		if (LANG == null && !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			return TapError.writeErrorToVotable(TapJobErrors.PARAM_LANGUAGE_MISSING);
		} else if (LANG != null) {
			if (!LANG.equalsIgnoreCase("ADQL") && !LANG.equalsIgnoreCase("PQL")) {
				error_message = "Invalid LANGUAGE: " + LANG;
				return TapError.writeErrorToVotable(error_message);
			}
		}

		if (QUERY == null && !REQUEST.equalsIgnoreCase(TapJobParams.REQUEST_GET_CAPABILITIES)) {
			return TapError.writeErrorToVotable(TapJobErrors.PARAM_QUERY_MISSING);
		}

		if (FORMAT != null) {
			if (!FORMAT.equalsIgnoreCase("votable")) {
				error_message = "FORMAT '" + FORMAT + "'not supported" ;
				return TapError.writeErrorToVotable(error_message);
			}
		}
		
		if (VERSION != null) {
			if (!VERSION.equalsIgnoreCase("1.0") || !VERSION.equalsIgnoreCase("1")) {
				error_message = "VERSION '" + VERSION + "'not supported" ;
				return TapError.writeErrorToVotable(error_message);
			}
		}
		
		return "";

	}

}
