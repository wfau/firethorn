package uws.job;

/*
 * This file is part of UWSLibrary.
 * 
 * UWSLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UWSLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UWSLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2011 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import uws.UWSException;

import uws.job.serializer.UWSSerializer;
import uws.service.UWSUrl;

/**
 * This class gives a short description (mainly an ID and a URL) of a job result.
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 05/2011
 */
public class Result extends SerializableUWSObject {
	private static final long serialVersionUID = 1L;

	public final static String DEFAULT_RESULT_NAME = "result";

	/** <b>[Required ; Default="result"]</b> Name or ID of this result. */
	protected String id = DEFAULT_RESULT_NAME;

	/** <i>[Optional]</i> The readable URL which points toward the result file. */
	protected String href = null;

	/** <i>[Optional]</i> The XLINK URL type (simple (default), extended, locator, arc, resource, title or none ; see http://www.w3.org/TR/xlink/#linking-elements for more details). */
	protected String type = "simple";

	/** <i>[Optional]</i> The MIME type of the result. */
	protected String mimeType = null;

	/* ************ */
	/* CONSTRUCTORS */
	/* ************ */
	/**
	 * Default constructor. Nothing is done (id={@link #DEFAULT_RESULT_NAME} ; href=null ; type="simple"; mimeType=null).
	 */
	public Result(){ ; }

	/**
	 * Builds a result with its ID/name.
	 * 
	 * @param name	Name or ID of the result.
	 */
	public Result(String name){
		if (name != null)
			id = name;
	}

	/**
	 * Builds a result with the URL toward the file which contains the results.
	 * 
	 * @param resultUrl	Result file URL.
	 */
	public Result(java.net.URL resultUrl){
		if (resultUrl != null){
			id = resultUrl.getFile();
			href = resultUrl.toString();
		}
	}

	/**
	 * Builds a result with an ID/name and the URL toward the file which contains the results.
	 * 
	 * @param name			Name or ID of the result.
	 * @param resultUrl		Result file URL.
	 * 
	 * @see Result#Result(String)
	 */
	public Result(String name, String resultUrl){
		this(name);
		href = resultUrl;
	}

	/**
	 * Builds a result with an ID/name, a result type and the URL toward the file which contains the results.
	 * 
	 * @param name			Name or ID or the result.
	 * @param resultType	Type of result.
	 * @param resultUrl		Result file URL.
	 * 
	 * @see Result#Result(String, String)
	 */
	public Result(String name, String resultType, String resultUrl){
		this(name, resultUrl);
		type = resultType;
	}

	/**
	 * Gets the HREF as {jobList}/{job}/results/ID.
	 * 
	 * @param id	ID of the concerned Result.
	 * @param job	The job which has to contain the Result instance.
	 * 
	 * @return		The HREF field of the Result.
	 */
	protected static final String getDefaultUrl(String id, AbstractJob job){
		UWSUrl url = job.getUrl();
		url.setAttributes(new String[]{AbstractJob.PARAM_RESULTS, id});
		return url.toString();
	}

	/* ******* */
	/* GETTERS */
	/* ******* */
	/**
	 * Gets the id/name of this result.
	 * 
	 * @return	The result id or name.
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Gets the URL of the result file.
	 * 
	 * @return	The result file URL.
	 */
	public final String getHref() {
		return href;
	}

	/**
	 * Gets the type of this result.
	 * 
	 * @return	The result type.
	 */
	public final String getType() {
		return type;
	}

	/**
	 * Gets the MIME type of this result.
	 * 
	 * @return The MIME Type.
	 */
	public final String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets the MIME type of this result.
	 * 
	 * @param mimeType The MIME type to set.
	 */
	public final void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/* ***************** */
	/* INHERITED METHODS */
	/* ***************** */
	@Override
	public String serialize(UWSSerializer serializer, JobOwner owner) throws UWSException {
		return serializer.getResult(this, true);
	}

	@Override
	public String toString(){
		return "RESULT {id: "+id+"; type: \""+((type==null)?"?":type)+"\"; href: "+((href==null)?"none":href)+"; mimeType: "+((mimeType==null)?"none":mimeType)+"}";
	}

	/* ************** */
	/* RESULT CONTENT */
	/* ************** */
	public void writeContent(HttpServletResponse response) throws UWSException, IOException {
		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		response.setHeader("Location", getHref());
		response.flushBuffer();
	}

	/* ****************** */
	/* DEPRECATED METHODS */
	/* ****************** */
	/**
	 * Builds a result with an ID/name and the URL toward the file which contains the results.
	 * 
	 * @param name			Name or ID of the result.
	 * @param resultUrl		Result file URL.
	 * 
	 * @deprecated	Replaced by {@link #Result(String, String)}.
	 * @see 		#Result(String, String)
	 */
	@Deprecated
	public Result(String name, java.net.URL resultUrl){
		this(name, resultUrl.toString());
	}

	/**
	 * Builds a result with an ID/name, a result type and the URL toward the file which contains the results.
	 * 
	 * @param name			Name or ID or the result.
	 * @param resultType	Type of result.
	 * @param resultUrl		Result file URL.
	 * 
	 * @deprecated	Replaced by {@link #Result(String, String, String)}.
	 * @see 		#Result(String, String, String)
	 */
	@Deprecated
	public Result(String name, String resultType, java.net.URL resultUrl){
		this(name, resultType, resultUrl.toString());
	}
}
