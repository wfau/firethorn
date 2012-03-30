package tap;

/*
 * This file is part of TAPLibrary.
 * 
 * TAPLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TAPLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with TAPLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2011 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.MultipartRequest;

import tap.ServiceConnection.LimitUnit;
import tap.upload.TableLoader;
import uws.UWSToolBox;
import uws.job.AbstractJob;

/**
 * This class describes all defined parameters of a TAP request.
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 09/2011
 */
public class TAPParameters {
	public static final String PARAM_REQUEST = "request";
	public static final String REQUEST_DO_QUERY = "doQuery";
	public static final String REQUEST_GET_CAPABILITIES = "getCapabilities";

	public static final String PARAM_LANGUAGE = "lang";
	public static final String LANG_ADQL = "ADQL";
	public static final String LANG_PQL = "PQL";

	public static final String PARAM_VERSION = "version";
	public static final String VERSION_1_0 = "1.0";

	public static final String PARAM_FORMAT = "format";
	public static final String FORMAT_VOTABLE = "votable";

	public static final String PARAM_QUERY = "query";
	public static final String PARAM_MAX_REC = "maxrec";
	public static final String PARAM_RUN_ID = "runid";
	public static final String PARAM_UPLOAD = "upload";

	/** Part of HTTP content type header. */
	public static final String MULTIPART = "multipart/";

	public String request = null;
	public String version = VERSION_1_0;
	public String lang = null;
	public String query = null;
	public String format = FORMAT_VOTABLE;
	public int maxrec = -1;
	public String runid = null;
	public String upload = null;
	public TableLoader[] tablesToUpload = null;
	public Map<String, String> otherParameters = null;

	private int defaultOutputLimit = -1;
	private int maxOutputLimit = -1;

	/**
	 * Builds a TAPParameters object only with the three required TAP parameters.
	 * 
	 * @param reqMethod		The REQUEST value.
	 * @param queryLanguage	The LANG value.
	 * @param queryExp		The QUERY value.
	 */
	public TAPParameters(String reqMethod, String queryLanguage, String queryExp, ServiceConnection<?> service) throws TAPException {
		request = reqMethod;
		lang = queryLanguage;
		query = queryExp;
		otherParameters = new HashMap<String, String>();

		setLimits(service);

		checkTAPParameters();
	}

	public TAPParameters(Map<String, String> params, ServiceConnection<?> service) throws TAPException {
		setLimits(service);

		otherParameters = new HashMap<String, String>();

		// Extract and identify each pair (key,value):
		for (String key : params.keySet())
			setTAPParameter(key, params.get(key));

		// Check parameters:
		checkTAPParameters();
	}

	@SuppressWarnings("unchecked")
	public TAPParameters(HttpServletRequest req, ServiceConnection<?> service) throws TAPException {
		setLimits(service);

		otherParameters = new HashMap<String, String>();

		MultipartRequest multipart = null;
		if (isMultipartContent(req)){
			try{
				multipart = new MultipartRequest(req, service.getUploadDirectory());
				Enumeration<String> e = multipart.getParameterNames();
				while(e.hasMoreElements()){
					String param = e.nextElement();
					setTAPParameter(param.toLowerCase(), multipart.getParameter(param));
				}
			}catch(IOException ioe){
				throw new TAPException("An error has occurred while reading the Multipart content !", ioe);
			}
		}else{
			// Extract and identify each pair (key,value):
			Map<String, String> params = UWSToolBox.getParamsMap(req);
			for (String key : params.keySet())
				setTAPParameter(key, params.get(key));
		}

		if (service.uploadEnabled() && this.upload != null)
			tablesToUpload = buildLoaders(this.upload, multipart);

		// Check parameters:
		checkTAPParameters();
	}

	/**
	 * Utility method that determines whether the request contains multipart
	 * content.
	 *
	 * @param request The servlet request to be evaluated. Must be non-null.
	 *
	 * @return <code>true</code> if the request is multipart;
	 *         <code>false</code> otherwise.
	 */
	public static final boolean isMultipartContent(HttpServletRequest request) {
		if (!"post".equals(request.getMethod().toLowerCase())) {
			return false;
		}
		String contentType = request.getContentType();
		if (contentType == null) {
			return false;
		}
		if (contentType.toLowerCase().startsWith(MULTIPART)) {
			return true;
		}
		return false;
	}

	/**
	 * Builds as many TableLoader instances as tables to upload.
	 * 
	 * @param upload	The upload field (syntax: "tableName1,URI1 ; tableName2,URI2 ; ...", where URI may start by "param:" to indicate that the VOTable is inline).
	 * @param multipart	The multipart content of the request if any.
	 * 
	 * @return			All table loaders (one per table to upload).
	 * 
	 * @throws TAPException	If the syntax of the "upload" field is incorrect.
	 */
	private TableLoader[] buildLoaders(final String upload, final MultipartRequest multipart) throws TAPException {
		if (upload == null || upload.trim().isEmpty())
			return new TableLoader[0];

		String[] pairs = upload.split(";");
		TableLoader[] loaders = new TableLoader[pairs.length];

		for(int i=0; i<pairs.length; i++){
			String[] table = pairs[i].split(",");
			if (table.length != 2)
				throw new TAPException("Bad syntax ! An UPLOAD parameter must contain a list of pairs separated by a ';'. Each pair is composed of 2 parts, a table name and a URI separated by a ','.");
			loaders[i] = new TableLoader(table[0], table[1], multipart);
		}

		return loaders;
	}

	public TAPParameters(AbstractJob job, ServiceConnection<?> service) throws TAPException {
		setLimits(service);

		otherParameters = new HashMap<String, String>();

		// Extract and identify each pair (key,value):
		for (String key : job.getAdditionalParameters()) {
			String value = job.getAdditionalParameterValue(key);
			setTAPParameter(key, value);
		}

		// Check parameters:
		checkTAPParameters();
	}

	protected void setLimits(ServiceConnection<?> service){
		final int[] limits = service.getOutputLimit();
		final LimitUnit[] limitsType = service.getOutputLimitType();
		if (limits != null && limits.length >= 2 && limitsType != null && limitsType.length >= 2){
			if (limitsType[0] != null && limitsType[0] == LimitUnit.rows){
				defaultOutputLimit = limits[0];
				if (defaultOutputLimit < 0)
					defaultOutputLimit = -1;
			}
			if (limitsType[1] != null && limitsType[1] == LimitUnit.rows){
				maxOutputLimit = limits[1];
				if (maxOutputLimit < 0)
					maxOutputLimit = -1;
			}
		}
	}

	protected void setTAPParameter(String key, String value) throws TAPException {
		// REQUEST case:
		if (key.equalsIgnoreCase(PARAM_REQUEST)) {
			if (!value.equals(REQUEST_DO_QUERY) && !value.equals(REQUEST_GET_CAPABILITIES))
				throw new TAPException("The parameter \""+PARAM_REQUEST+"\" must be equal to \""+REQUEST_DO_QUERY+"\" or \""+REQUEST_GET_CAPABILITIES+"\" (now: "+PARAM_REQUEST+"=\""+request+"\" ! (note: the parameter name is not case sensitive instead of the parameter value which must have exactly the same case)");
			else
				request = value;
			// LANG case:
		} else if (key.equalsIgnoreCase(PARAM_LANGUAGE))
			lang = value;
		// QUERY case:
		else if (key.equalsIgnoreCase(PARAM_QUERY))
			query = value;
		// VERSION case:
		else if (key.equalsIgnoreCase(PARAM_VERSION))
			version = value;
		// FORMAT case:
		else if (key.equalsIgnoreCase(PARAM_FORMAT))
			format = value;
		// MAXREC case:
		else if (key.equalsIgnoreCase(PARAM_MAX_REC)) {
			try {
				maxrec = Integer.parseInt(value);
			} catch (NumberFormatException nfe) {
				if (value == null || value.trim().length() == 0)
					maxrec = -1;
				else
					throw new TAPException("The \""+PARAM_MAX_REC+"\" parameter must be a numeric value (now: "+PARAM_MAX_REC+"="+value+") !");
			}
			// RUNID case:
		} else if (key.equalsIgnoreCase(PARAM_RUN_ID)){
			runid = value;
			otherParameters.put(key, value);
		}
		// UPLOAD case:
		else if (key.equalsIgnoreCase(PARAM_UPLOAD))
			upload = value;
		// OTHER case:
		else
			otherParameters.put(key, value);
	}

	protected void checkTAPParameters() throws TAPException {
		// Check that required parameters are not NON-NULL:
		if (request == null)
			throw new TAPException("The parameter \""+PARAM_REQUEST+"\" must be provided and its value must be equal to \""+REQUEST_DO_QUERY+"\" or \""+REQUEST_GET_CAPABILITIES+"\" !");

		if (request.equals(REQUEST_DO_QUERY)){
			if (lang == null)
				throw new TAPException("The parameter \""+PARAM_LANGUAGE+"\" must be provided if "+PARAM_REQUEST+"="+REQUEST_DO_QUERY+" !");
			else if (query == null)
				throw new TAPException("The parameter \""+PARAM_QUERY+"\" must be provided if "+PARAM_REQUEST+"="+REQUEST_DO_QUERY+" !");
		}

		// Check the version if needed:
		if (version != null && !version.equals("1") && !version.equals("1.0"))
			throw new TAPException("Version \""+version+"\" of TAP not implemented !");

		// Check format if needed:
		if (format == null)
			format = FORMAT_VOTABLE;

		// Check maxrec:
		if (maxrec <= -1)
			maxrec = defaultOutputLimit;

		if (maxOutputLimit > -1){
			if (maxrec > maxOutputLimit)
				maxrec = maxOutputLimit;
			else if (maxrec <= -1)
				maxrec = maxOutputLimit;
		}
	}

	public static final void deleteUploadedTables(final TableLoader[] loaders) {
		if (loaders != null){
			for(TableLoader loader : loaders)
				loader.deleteFile();
		}
	}
}
