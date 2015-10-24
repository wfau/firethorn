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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, produces = "text/plain")
	public void initAsyncJob(@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			@RequestParam(value = "LANG", required = false) String LANG,
			@RequestParam(value = "REQUEST", required = false) String REQUEST,
			@RequestParam(value = "QUERY", required = false) String QUERY, final HttpServletResponse response)
					throws Exception {

		UWSJob uwsjob;
		PrintWriter writer = response.getWriter();
		BlueQuery qry;

		if (resource != null) {

			// Check input parameters and return VOTable with appropriate
			// message if any errors found
			boolean check = checkParams(writer, REQUEST, LANG, QUERY);

			if (check) {

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

				uwsfactory.writeUWSJobToXML(uwsjob, writer);

			}

		} else {

			TapError.writeErrorToVotable(TapJobErrors.RESOURCE_MISSING, writer);
		}

	}

	@RequestMapping(value = "/{jobid}/parameters", method = RequestMethod.POST)
	@ResponseBody
	public void setparameters(@PathVariable String jobid,
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			@RequestParam(value = "LANG", required = false) String LANG,
			@RequestParam(value = "REQUEST", required = false) String REQUEST,
			@RequestParam(value = "QUERY", required = false) final String QUERY, final HttpServletResponse response)
					throws IdentifierNotFoundException, Exception {

		PrintWriter writer = response.getWriter();
		UWSJob uwsjob;
		final BlueQuery queryentity = getqueryentity(jobid);

		if (queryentity != null) {
			uwsjob = uwsfactory.create(resource, queryentity, jobType);
		} else {
			uwsjob = uwsfactory.create(resource, jobType);
		}

		if (REQUEST != null)
			uwsjob.setRequest(REQUEST);
		if (LANG != null)
			uwsjob.setLang(LANG);
		if (QUERY != null)
			uwsjob.setQuery(QUERY);

		uwsfactory.writeUWSJobToXML(uwsjob, writer);

	}

	@RequestMapping(value = "/{jobid}/phase", method = RequestMethod.POST)
	@UpdateAtomicMethod
	@ResponseBody
	public void phase(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response, @RequestParam(value = "PHASE", required = true) String PHASE)
					throws IdentifierNotFoundException, Exception {

		PrintWriter writer = response.getWriter();
		UWSJob uwsjob;
		BlueQuery queryentity = getqueryentity(jobid);

		try {

			if (queryentity != null) {
				uwsjob = uwsfactory.create(resource, queryentity, jobType);
			} else {
				uwsjob = uwsfactory.create(resource, jobType);
			}

			if (PHASE == null) {
				TapError.writeErrorToVotable(TapJobErrors.PARAM_PHASE_MISSING, writer);
			} else {
				uwsjob.setPhase(PHASE);
			}

			uwsfactory.writeUWSJobToXML(uwsjob, writer);

		} catch (Exception e) {
			TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR, writer);
		}


	}

	@RequestMapping(value = "/{jobid}/phase", method = RequestMethod.GET)
	@UpdateAtomicMethod
	@ResponseBody
	public void phaseGet(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		PrintWriter writer = response.getWriter();
		UWSJob uwsjob;
		BlueQuery queryentity = getqueryentity(jobid);

		try {

			if (queryentity != null) {
				uwsjob = uwsfactory.create(resource, queryentity, jobType);
			} else {
				uwsjob = uwsfactory.create(resource, jobType);
			}

			writer.append(uwsjob.getPhase());
			
		} catch (Exception e) {
			TapError.writeErrorToVotable(TapJobErrors.INTERNAL_ERROR, writer);
		}

	}

	@RequestMapping(value = "/{jobid}/quote", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String quote(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource)
			throws IdentifierNotFoundException, Exception {
		java.util.Date date = new java.util.Date();
		return new Timestamp(date.getTime()).toString();
	}

	@RequestMapping(value = "/{jobid}/executionduration", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String executionduration(@PathVariable String jobid,
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource)
					throws IdentifierNotFoundException, Exception {
		return Integer.toString(TapJobParams.EXECUTION_DURATION);
	}

	@RequestMapping(value = "/{jobid}/destruction", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String destruction(@PathVariable String jobid,
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource)
					throws IdentifierNotFoundException, Exception {

		return null;
	}
	
	@RequestMapping(value = "/{jobid}/owner", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String owner(@PathVariable String jobid,
			@ModelAttribute("urn:adql.resource.entity") AdqlResource resource)
					throws IdentifierNotFoundException, Exception {
		return null;
	}

	@RequestMapping(value = "/{jobid}/error", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void error(@PathVariable String jobid, 
		@ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
		final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		PrintWriter writer = response.getWriter();
		BlueQuery queryentity = getqueryentity(jobid);
		if (queryentity.state()==TaskState.ERROR || queryentity.state()==TaskState.FAILED ){
			TapError.writeErrorToVotable(queryentity.text(), writer);
		} else {
			writer.append("");
		}
	}

	@RequestMapping(value = "/{jobid}/results", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void results(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		UWSJob uwsjob;
		BlueQuery queryentity = getqueryentity(jobid);
		PrintWriter writer = response.getWriter();

		try {

			uwsjob = uwsfactory.create(resource, queryentity, jobType);
			uwsfactory.writeUWSResultToXML(uwsjob, writer);

		} catch (Exception e) {

		}

	}

	@RequestMapping(value = "/{jobid}/results/result", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void result(@PathVariable String jobid, @ModelAttribute("urn:adql.resource.entity") AdqlResource resource,
			final HttpServletResponse response) throws IdentifierNotFoundException, Exception {

		UWSJob uwsjob;
		BlueQuery queryentity = getqueryentity(jobid);
		PrintWriter writer = response.getWriter();

		try {
			uwsjob = uwsfactory.create(resource, queryentity, jobType);
			writer.append(uwsjob.getResults());
		} catch (Exception e) {

		}

	}

	private boolean checkParams(final PrintWriter writer, final String REQUEST, final String LANG, final String QUERY) {

		String error_message;
		boolean valid = true;

		// Check for errors and return appropriate VOTable error messages
		if (REQUEST == null) {
			TapError.writeErrorToVotable(TapJobErrors.PARAM_REQUEST_MISSING, writer);
			valid = false;
			return valid;
		} else if (!REQUEST.equalsIgnoreCase("doQuery")) {
			error_message = "Invalid REQUEST: " + REQUEST;
			TapError.writeErrorToVotable(error_message, writer);
			valid = false;
			return valid;
		}

		if (LANG == null) {
			TapError.writeErrorToVotable(TapJobErrors.PARAM_LANGUAGE_MISSING, writer);
			valid = false;
			return valid;
		} else if (!LANG.equalsIgnoreCase("ADQL") && !LANG.equalsIgnoreCase("PQL")) {
			error_message = "Invalid LANGUAGE: " + LANG;
			TapError.writeErrorToVotable(error_message, writer);
			valid = false;
		}

		return valid;

	}
}
