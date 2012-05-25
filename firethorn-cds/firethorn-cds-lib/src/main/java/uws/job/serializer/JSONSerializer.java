package uws.job.serializer;

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

import java.util.Iterator;

import uws.UWSException;
import uws.job.AbstractJob;
import uws.job.ErrorSummary;
import uws.job.JobList;
import uws.job.JobOwner;
import uws.job.Result;
import uws.service.AbstractUWS;
import uws.service.UWSUrl;

/**
 * Lets serializing any UWS resource in JSON.
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 01/2012
 */
public class JSONSerializer extends UWSSerializer {
	private static final long serialVersionUID = 1L;

	/** Tab to use just before a JSON item. */
	protected String tabPrefix = "";


	@Override
	public final String getMimeType() {
		return MIME_TYPE_JSON;
	}

	@Override
	public String getUWS(final AbstractUWS<? extends JobList<? extends AbstractJob>, ? extends AbstractJob> uws, final JobOwner user) throws UWSException {
		StringBuffer json = new StringBuffer("{\n\t\"name\": \"");
		json.append(escape(uws.getName())).append("\",");
		if (uws.getDescription() != null)
			json.append("\n\t\"description\": \"").append(escape(uws.getDescription())).append("\",");

		json.append("\n\t\"jobLists\": [");
		Iterator<? extends JobList<? extends AbstractJob>> it = uws.iterator();
		while(it.hasNext()){
			JobList<? extends AbstractJob> jobList = it.next();
			UWSUrl jlUrl = jobList.getUrl();
			json.append("\n\t\t{ \"name\": \"").append(escape(jobList.getName())).append("\", \"href\": ");
			if (jlUrl != null) json.append(escape(jlUrl.getRequestURI()));
			json.append("\" }");
			if (it.hasNext()) json.append(",");
		}

		return json.append("\n\t]\n}").toString();
	}

	@Override
	public String getJobList(final JobList<? extends AbstractJob> jobsList, final JobOwner owner, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer("{\n\t\"name\": \"");
		json.append(escape(jobsList.getName())).append("\",\n\t\"jobs\": [");

		UWSUrl jobsListUrl = jobsList.getUrl();
		Iterator<? extends AbstractJob> it = jobsList.getJobs((owner==null)?null:owner.getID());
		while(it.hasNext()){
			json.append("\n\t\t").append(getJobRef(it.next(), jobsListUrl));
			if (it.hasNext()) json.append(",");
		}

		return json.append("\n\t]\n}").toString();
	}

	@Override
	public String getJob(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer("{\n\t");
		String newLine = ",\n\t";
		json.append(getJobID(job, false));
		json.append(newLine).append(getRunID(job, false));
		json.append(newLine).append(getOwnerID(job, false));
		json.append(newLine).append(getPhase(job, false));
		json.append(newLine).append(getQuote(job, false));
		json.append(newLine).append(getStartTime(job, false));
		json.append(newLine).append(getEndTime(job, false));
		json.append(newLine).append(getExecutionDuration(job, false));
		json.append(newLine).append(getDestructionTime(job, false));

		tabPrefix += "\t";
		newLine = ",\n";

		// parameters:
		json.append(newLine).append(getAdditionalParameters(job, false));

		// results:
		json.append(newLine).append(getResults(job, false));

		// errorSummary:
		if (job.getErrorSummary() != null)
			json.append(newLine).append(getErrorSummary(job.getErrorSummary(), false));

		tabPrefix = "";
		return json.append("\n}").toString();
	}

	@Override
	public String getJobRef(final AbstractJob job, final UWSUrl jobsListUrl) throws UWSException {
		StringBuffer json = new StringBuffer("{ ");
		json.append(getJobID(job, false));
		json.append("\", ").append(getRunID(job, false));
		json.append("\", \"href\": \"");
		if (jobsListUrl != null){
			jobsListUrl.setJobId(job.getJobId());
			json.append(escape(jobsListUrl.getRequestURL()));
		}
		json.append("\", ").append(getPhase(job, false)).append(" }");
		return json.toString();
	}

	@Override
	public String getJobID(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"jobId\": \"").append(escape(job.getJobId())).append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getRunID(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"runId\": \"");
		if (job.getRunId() != null) json.append(escape(job.getRunId()));
		json.append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getOwnerID(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"owner\": \"");
		if (job.getOwner() != null) json.append(escape(job.getOwner().getPseudo()));
		json.append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getPhase(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"phase\": \"").append(escape(job.getPhase().toString())).append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getQuote(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"quote\": \"").append(job.getQuote()).append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getExecutionDuration(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer("\"executionDuration\": \"");
		json.append(job.getExecutionDuration()).append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getDestructionTime(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"destruction\": \"");
		if (job.getDestructionTime() != null)
			json.append(escape(job.getDateFormat().format(job.getDestructionTime())));
		json.append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getStartTime(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"startTime\": \"");
		if (job.getStartTime() != null)
			json.append(escape(job.getDateFormat().format(job.getStartTime())));
		json.append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getEndTime(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"endTime\": \"");
		if (job.getEndTime() != null)
			json.append(escape(job.getDateFormat().format(job.getEndTime())));
		json.append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getErrorSummary(final ErrorSummary error, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(tabPrefix);
		if (root) json.append("{");
		json.append("\"error\": {");

		if (error == null)
			json.append("}");
		else{
			String newLine = "\n\t"+tabPrefix;
			json.append(newLine).append("\"type\": \"").append(escape(error.getType().toString())).append("\",");
			json.append(newLine).append("\"hasDetail\": \"").append(error.hasDetail()).append("\",");
			json.append(newLine).append("\"message\": \"").append(escape(error.getMessage())).append("\"\n").append(tabPrefix).append("}");
		}

		if (root) json.append("}");

		return json.toString();
	}

	@Override
	public String getAdditionalParameters(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(tabPrefix);
		String oldTabPrefix = tabPrefix;
		if (root){
			tabPrefix = tabPrefix+"\t";
			json.append("{\n").append(tabPrefix);
		}
		json.append("\"parameters\": {");

		StringBuffer params = null;
		String newLine = "\n\t"+tabPrefix;
		for(String paramName : job.getAdditionalParameters()){
			if (params == null) params = new StringBuffer(newLine);
			else				params.append(',').append(newLine);
			params.append(getAdditionalParameter(paramName, job.getAdditionalParameterValue(paramName), false));
		}

		if (params != null) json.append(params).append("\n").append(tabPrefix);
		json.append("}");
		if (root){
			tabPrefix = oldTabPrefix;
			json.append("\n").append(tabPrefix).append("}");
		}
		return json.toString();
	}

	@Override
	public String getAdditionalParameter(final String paramName, final String paramValue, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(root?"{":"");
		json.append("\"").append(escape(paramName)).append("\": \"").append(escape(paramValue)).append("\"");
		if (root) json.append("}");
		return json.toString();
	}

	@Override
	public String getResults(final AbstractJob job, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer(tabPrefix);
		String oldTabPrefix = tabPrefix;
		if (root){
			tabPrefix = tabPrefix+"\t";
			json.append("{\n").append(tabPrefix);
		}
		json.append("\"results\": [");

		StringBuffer results = null;
		String newLine = "\n\t"+tabPrefix;
		Iterator<Result> it = job.getResults();
		while(it.hasNext()){
			if (results == null) results = new StringBuffer(newLine);
			else			  	 results.append(',').append(newLine);
			results.append(getResult(it.next(), false));
		}
		if (results != null) json.append(results).append("\n").append(tabPrefix);
		json.append("]");
		if (root){
			tabPrefix = oldTabPrefix;
			json.append("\n").append(tabPrefix).append("}");
		}
		return json.toString();
	}

	@Override
	public String getResult(final Result result, final boolean root) throws UWSException {
		StringBuffer json = new StringBuffer("{");
		json.append(" \"id\": \"").append(escape(result.getId()));
		json.append("\", \"type\": \"").append(escape(result.getType()));
		json.append("\", \"href\": \"").append(escape(result.getHref()));
		if (result.getMimeType() != null)
			json.append("\", \"mime\": \"").append(escape(result.getMimeType()));
		json.append("\" }");
		return json.toString();
	}

	/**
	 * <p><i><b><u>Note:</u>Function copied from the class org.json.simple.JSONValue of the JSON-Simple toolkit (http://code.google.com/p/json-simple/).</b></i></p>
	 * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
	 * @param s
	 * @return
	 */
	public static StringBuffer escape(String s){
		StringBuffer sb = new StringBuffer();
		if (s != null)
			escape(s, sb);
		return sb;
	}

	/**
	 * <p><i><b><u>Note:</u>Function copied from the class org.json.simple.JSONValue of the JSON-Simple toolkit (http://code.google.com/p/json-simple/).</b></i></p>
	 * @param s - Must not be null.
	 * @param sb
	 */
	static void escape(String s, StringBuffer sb) {
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			switch(ch){
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
				//			case '/':
				//				sb.append("\\/");
				//				break;
			default:
				//Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else{
					sb.append(ch);
				}
			}
		}//for
	}



}
