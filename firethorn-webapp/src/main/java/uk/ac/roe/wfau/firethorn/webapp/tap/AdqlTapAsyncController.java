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

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import uk.ac.roe.wfau.firethorn.entity.annotation.UpdateAtomicMethod;
import uk.ac.roe.wfau.firethorn.entity.exception.EntityNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierFormatException;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;
import uk.ac.roe.wfau.firethorn.webapp.paths.Path;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJobFactory;
import uk.ac.roe.wfau.firethorn.blue.*;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.webapp.tap.CommonParams;

@Slf4j
@Controller
@RequestMapping("/tap/{ident}/async")
public class AdqlTapAsyncController extends AbstractController {

	@Override
	public Path path() {
		return path("/tap/{ident}/async");
	}

	@Autowired
	private UWSJobFactory uwsfactory;

	@Autowired
	private BlueQuery.EntityServices services;

	private final String jobType = "ASYNC";

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
	 * Get query entity by Ident
	 * 
	 * @param jobid
	 * @return
	 * @throws IdentifierFormatException
	 * @throws IdentifierNotFoundException
	 * @throws EntityNotFoundException
	 */
	public BlueQuery getqueryentity(final String jobid)
			throws IdentifierFormatException, IdentifierNotFoundException, EntityNotFoundException {
		log.debug("query() [{}]", jobid);
		return services.entities().select(services.idents().ident(jobid));

	}

	@RequestMapping(method = RequestMethod.POST)
	@UpdateAtomicMethod
	@ResponseStatus(value = HttpStatus.SEE_OTHER)
	public void async(@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			@RequestParam(value = "LANG", required = false) String LANG,
			@RequestParam(value = "REQUEST", required = false) String REQUEST,
			@RequestParam(value = "QUERY", required = false) String QUERY, 
			@RequestParam(value = "FORMAT", required = false) final String FORMAT, 
			@RequestParam(value = "VERSION", required = false) final String VERSION, 
			@RequestParam(value = "MAXREC", required = false) final String MAXREC,
			final HttpServletResponse response)
					throws Exception {


		UWSJob uwsjob = null;
		BlueQuery qry;
		PrintWriter writer = response.getWriter();
		
		if (resource != null) {

			// Check input parameters and return VOTable with appropriate
			// message if any errors found
			//boolean check = checkParams(writer, REQUEST, LANG, QUERY, FORMAT, VERSION);

			//if (check) {

			if (QUERY != null) {
				qry = uwsfactory.createNewQuery(resource, QUERY);
				uwsjob = uwsfactory.create(resource, qry, jobType);
				uwsjob.setQuery(QUERY);
			} else {
				qry = uwsfactory.createNewQuery(resource);
				uwsjob = uwsfactory.create(resource, qry, jobType);
			}

			if (REQUEST != null)
				uwsjob.setRequest(REQUEST);
			if (LANG != null)
				uwsjob.setLang(LANG);
			if (VERSION != null)
				uwsjob.setVersion(VERSION);
			if (FORMAT != null)
				uwsjob.setFormat(FORMAT);
			if (MAXREC != null)
				uwsjob.setMaxrec(MAXREC);
			
			log.debug("Location:" + uwsjob.getJobURL());


		} else {
			throw new IdentifierNotFoundException(null, "Resource not found");
		    
		}
		
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		writer.append("Location: " + uwsjob.getJobURL());
	    response.setHeader("Location",  uwsjob.getJobURL());
	    
	    return;
		
	}


	@RequestMapping(value="{jobid}",  method = { RequestMethod.POST, RequestMethod.GET })
	public void getJobInfo(@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			@PathVariable("jobid") String jobid,
			@RequestParam(value = "ACTION", required = false) String ACTION,
			final HttpServletResponse response)
					throws Exception {

		if (ACTION=="DELETE"){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;

		}
		
		response.setContentType(CommonParams.TEXT_XML_MIME);
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();

		UWSJob uwsjob;
		BlueQuery queryentity;
		
		try {
			queryentity = getqueryentity(jobid);
		} catch (Exception e) {
			writer.append(TapError.writeErrorToVotable(TapJobErrors.JOBID_NOTFOUND));
			return;
		}
		
		if (resource != null) {
			
			if (queryentity != null) {
				uwsjob = uwsfactory.create(resource, queryentity, jobType);
				writer.append(uwsfactory.writeUWSJobToXML(uwsjob));
				return;
			} else {
				writer.append(TapError.writeErrorToVotable(TapJobErrors.JOBID_MISSING));
				return;
			}

		} else {
			writer.append(TapError.writeErrorToVotable(TapJobErrors.RESOURCE_MISSING));
			return;
		}
		
	}

	
	
	@RequestMapping(value = "/{jobid}/parameters", method = RequestMethod.POST)
	@UpdateAtomicMethod
	@ResponseStatus(value = HttpStatus.SEE_OTHER)
	public void parameters(@PathVariable String jobid,
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			@RequestParam(value = "LANG", required = false) String LANG,
			@RequestParam(value = "REQUEST", required = false) String REQUEST,
			@RequestParam(value = "QUERY", required = false) final String QUERY, 
			@RequestParam(value = "FORMAT", required = false) final String FORMAT, 
			@RequestParam(value = "VERSION", required = false) final String VERSION, 
			@RequestParam(value = "MAXREC", required = false) final String MAXREC,
			final HttpServletResponse response)
					throws IdentifierNotFoundException, Exception {

	
		UWSJob uwsjob;
		BlueQuery queryentity = null;
		PrintWriter writer = response.getWriter();

		try {
			queryentity = getqueryentity(jobid);
		} catch (Exception e) {
			writer.append(TapError.writeErrorToVotable(TapJobErrors.JOBID_NOTFOUND));
		}

		if (queryentity != null) {
			uwsjob = uwsfactory.create(resource, queryentity, jobType);
		} else {
			uwsjob = uwsfactory.create(resource, jobType);
		}
		
		try {

			if (QUERY != null)
				uwsjob.setQuery(QUERY);
			if (REQUEST != null)
				uwsjob.setRequest(REQUEST);
			if (LANG != null)
				uwsjob.setLang(LANG);
			if (VERSION != null)
				uwsjob.setVersion(VERSION);
			if (FORMAT != null)
				uwsjob.setFormat(FORMAT);
			if (MAXREC != null)
				uwsjob.setMaxrec(MAXREC);
			
			response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		    response.setHeader("Location", uwsjob.getJobURL());
		    
		} catch (Exception e) {
			
			log.debug(e.getMessage());
			writer.append(TapError.writeErrorToVotable(e.getMessage()));
			return;
		}
		
		return;
		
	}

	@RequestMapping(value = "/{jobid}/phase", method = RequestMethod.POST)
	@UpdateAtomicMethod
	@ResponseStatus(value = HttpStatus.SEE_OTHER)
	public void phase(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response, @RequestParam(value = "PHASE", required = true) String PHASE)
					throws IdentifierNotFoundException, Exception {

		UWSJob uwsjob;
		BlueQuery queryentity = null;
		PrintWriter writer = response.getWriter();

		try {
			queryentity = getqueryentity(jobid);
		} catch (Exception e) {
			writer.append(TapError.writeErrorToVotable(TapJobErrors.JOBID_NOTFOUND));
			return;
		}

		try {

			if (queryentity != null) {
				uwsjob = uwsfactory.create(resource, queryentity, jobType);
			} else {
				uwsjob = uwsfactory.create(resource, jobType);
			}

			if (PHASE == null) {
				writer.append(TapError.writeErrorToVotable(TapJobErrors.PARAM_PHASE_MISSING));
			} else {
				uwsjob.setPhase(PHASE);
			}

		} catch (Exception e) {
			log.debug(e.getMessage());
			writer.append(TapError.writeErrorToVotable(e.getMessage()));
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
	    response.setHeader("Location", uwsjob.getJobURL());
	    writer.append("Location: " + uwsjob.getJobURL());
	    
	    return;


	}

	@RequestMapping(value = "/{jobid}/phase", method = RequestMethod.GET)
	@UpdateAtomicMethod
	public void phaseGet(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		UWSJob uwsjob;
		BlueQuery queryentity;
		PrintWriter writer = response.getWriter();

		try {
			queryentity = getqueryentity(jobid);
		} catch (Exception e) {
			writer.append(TapError.writeErrorToVotable(TapJobErrors.JOBID_NOTFOUND));
			return;
			
		}
		
		String phase = "";

		try {

			if (queryentity != null) {
				uwsjob = uwsfactory.create(resource, queryentity, jobType);
			} else {
				uwsjob = uwsfactory.create(resource, jobType);
			}

			phase = uwsjob.getPhase();

		} catch (Exception e) {
			writer.append(TapError.writeErrorToVotable(e.getMessage()));
			return;
			
		}
		response.setContentType("text/plain");
		writer.append(phase);
		return;
	}

	@RequestMapping(value = "/{jobid}/quote", method = { RequestMethod.POST, RequestMethod.GET })
	public void quote(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response)
			throws IdentifierNotFoundException, Exception {

		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		java.util.Date date = new java.util.Date();
		writer.append(new Timestamp(date.getTime()).toString());
		return;

	}

	@RequestMapping(value = "/{jobid}/executionduration", method = { RequestMethod.POST, RequestMethod.GET })
	public void executionduration(@PathVariable String jobid,
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response)
					throws IdentifierNotFoundException, Exception {
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.append(Integer.toString(TapJobParams.EXECUTION_DURATION));
		return;
	}

	@RequestMapping(value = "/{jobid}/destruction", method = { RequestMethod.POST, RequestMethod.GET })
	public void destruction(@PathVariable String jobid,
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response)
					throws IdentifierNotFoundException, Exception {
		
		UWSJob uwsjob;
		BlueQuery queryentity;
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		
		try {
			
			queryentity = getqueryentity(jobid);

			if (queryentity != null) {
				uwsjob = uwsfactory.create(resource, queryentity, jobType);
			} else {
				uwsjob = uwsfactory.create(resource, jobType);
			}
			
			writer.append(uwsjob.getDestructionTime());
			return;
			
		} catch (Exception e) {
			writer.append("0");
			return;
		}
	}

	@RequestMapping(value = "/{jobid}/owner", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String owner(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource)
			throws IdentifierNotFoundException, Exception {
		return null;
	}

	@RequestMapping(value = "/{jobid}/error", method = { RequestMethod.POST, RequestMethod.GET })
	public void error(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		BlueQuery queryentity = getqueryentity(jobid);
		PrintWriter writer = response.getWriter();
		response.setContentType(CommonParams.TEXT_XML_MIME);
		response.setCharacterEncoding("UTF-8");
		
		if (queryentity.state() == TaskState.ERROR || queryentity.state() == TaskState.FAILED) {
		
			if (queryentity.syntax()!=null){
				writer.append(TapError.writeErrorToVotable(StringEscapeUtils.escapeXml(queryentity.syntax().friendly())));
			} else {
				writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
			}
			return;
			
		} else {
			
			writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
			return;
		}
	}

	@RequestMapping(value = "/{jobid}/results", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String results(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		response.setContentType(CommonParams.TEXT_XML_MIME);
		response.setCharacterEncoding("UTF-8");

		UWSJob uwsjob;
		BlueQuery queryentity;
		
		try {
			queryentity = getqueryentity(jobid);
		} catch (Exception e) {
			return TapError.writeErrorToVotable(TapJobErrors.JOBID_NOTFOUND);
		}
		
		try {
			uwsjob = uwsfactory.create(resource, queryentity, jobType);
			return uwsfactory.writeUWSResultToXML(uwsjob);
		} catch (Exception e) {
			return TapError.writeErrorToVotable(e.getMessage());
		}

	}

	@RequestMapping(value = "/{jobid}/results/result", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseStatus(value = HttpStatus.SEE_OTHER)
	public void result(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		String results = "";
		UWSJob uwsjob;
		BlueQuery queryentity = null;
		PrintWriter writer = response.getWriter();
		
		try {
			queryentity = getqueryentity(jobid);
		} catch (Exception e) {
			writer.append(TapError.writeErrorToVotable(TapJobErrors.JOBID_NOTFOUND));
		}
		
		try {
			uwsjob = uwsfactory.create(resource, queryentity, jobType);

			if (queryentity.state() == TaskState.ERROR || queryentity.state() == TaskState.FAILED) {
				throw new IdentifierNotFoundException(queryentity.ident(), "No results available for this job");
			}
			
			if (queryentity.state() == TaskState.EDITING || 
					queryentity.state() == TaskState.QUEUED || 
					queryentity.state() == TaskState.READY || 
					queryentity.state() == TaskState.RUNNING || 
					queryentity.state() == TaskState.CANCELLED
					) {

				writer.append(TapError.writeErrorToVotable(TapJobErrors.FILE_NOTFOUND));

			}

			results = uwsjob.getResults();
			response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		    response.setHeader("Location", results);
		    writer.append("Location: " + results);
		    
		} catch (Exception e) {
			writer.append(TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR));
		}
		
		
	}

	

}
