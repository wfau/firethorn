package uk.ac.roe.wfau.firethorn.webapp.tap;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import java.lang.IllegalStateException;
import javax.servlet.ServletOutputStream;

import uk.ac.roe.wfau.firethorn.adql.query.AdqlQuery;
import uk.ac.roe.wfau.firethorn.entity.exception.IdentifierNotFoundException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.job.Job.Status;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.tap.TapJobParams;
import uk.ac.roe.wfau.firethorn.webapp.control.AbstractController;

public class UWSJob {
	private static final long serialVersionUID = 1L;

	/* ********* */
	/* CONSTANTS */
	/* ********* */
	/** Name of the parameter <i>ACTION</i>. */
	public static final String PARAM_ACTION = "ACTION";

	/** Name of the DELETE action. */
	public static final String ACTION_DELETE = "DELETE";

	/** Name of the parameter <i>jobId</i>. */
	public static final String PARAM_JOB_ID = "jobId";

	/** Name of the parameter <i>runId</i>. */
	public static final String PARAM_RUN_ID = "runId";

	/** Name of the parameter <i>owner</i>. */
	public static final String PARAM_OWNER = "owner";

	/** Name of the parameter <i>phase</i>. */
	public static final String PARAM_PHASE = "phase";

	/** Value of the parameter <i>phase</i> which starts the job. */
	public static final String PHASE_RUN = "RUN";
	
	/** Initial Value of the parameter <i>phase</i> */
	public static final String PHASE_INITIAL = "PENDING";
	
	/** Value of the parameter <i>phase</i> which aborts the job. */
	public static final String PHASE_ABORT = "ABORT";

	/** Name of the parameter <i>quote</i>. */
	public static final String PARAM_QUOTE = "quote";

	/** Name of the parameter <i>startTime</i>. */
	public static final String PARAM_START_TIME = "startTime";

	/** Name of the parameter <i>endTime</i>. */
	public static final String PARAM_END_TIME = "endTime";

	/** Name of the parameter <i>executionDuration</i>. */
	public static final String PARAM_EXECUTION_DURATION = "executionDuration";

	/** Name of the parameter <i>destructionTime</i>. */
	public static final String PARAM_DESTRUCTION_TIME = "destruction";

	/** Name of the parameter <i>errorSummary</i>. */
	public static final String PARAM_ERROR_SUMMARY = "error";

	/** Name of the parameter <i>otherParameters</i>. */
	public static final String PARAM_PARAMETERS = "parameters";

	/** Name of the parameter <i>results</i>. */
	public static final String PARAM_RESULTS = "results";

	/** Default value of {@link #owner} if no ID are given at the job creation. */
	public final static String ANONYMOUS_OWNER = "anonymous";

	/** Default date format pattern.  */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/** The quote value that indicates the quote of this job is not known. */
	public static final long QUOTE_NOT_KNOWN = -1;
	
	/** The duration that implies an unlimited execution duration. */
	public final static long UNLIMITED_DURATION = 0;
	
	/** The used date formatter. */
	public static final DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	
	/** The time at which the job execution started. */
	private Date startTime = null;
	
	/** The time at which the job execution ended. */
	private Date endTime = null;
	
	private final String jobId;
	private final String ownerId;
	private long quote = QUOTE_NOT_KNOWN;
	private String phase = null;
	private String request = null;
	private String lang = null;
	private String results = null;
	private AdqlResource resource = null;
	private AdqlSchema schema = null;
	private AdqlQuery query = null;
	
	/** <p>This error summary gives a human-readable error message for the underlying job.</p>
	 * <i><u>Note:</u> This object is intended to be a detailed error message, and consequently,
	 * might be a large piece of text such as a stack trace.</i> */
	protected TapError errorSummary = null;


	/* ************ */
	/* CONSTRUCTORS */
	/* ************ */
	public UWSJob(AdqlResource resource) throws Exception {
		this.jobId = "";
		this.phase = PHASE_INITIAL;
		this.ownerId = null;
		this.quote = QUOTE_NOT_KNOWN;
		/*this.destructionTime = destruction;
		this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/
		this.startTime = new Date();
		errorSummary = new TapError();
		this.resource = resource;
		createNewQuery();
		
	}
	
	public UWSJob(AdqlQuery query, AdqlResource resource) throws Exception {
		this.jobId = query.ident().toString();
		this.phase = PHASE_INITIAL;
		this.ownerId = null;
		this.quote = QUOTE_NOT_KNOWN;
		/*this.destructionTime = destruction;
		this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/
		this.startTime = new Date();
		errorSummary = new TapError();
		this.resource = resource;
		this.query = query;
		
	}
	
	public UWSJob() throws Exception {
		this.jobId = generateJobId();
		this.phase = PHASE_INITIAL;
		this.ownerId = null;
		this.quote = QUOTE_NOT_KNOWN;
		/*this.destructionTime = destruction;
		this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/
		this.startTime = new Date();
		errorSummary = new TapError();
		
	}
	
	public UWSJob(String jobID, final long quote, final long startTime, final long endTime, final String phase, final String ownerId, final TapError error) throws Exception {
		if (jobID == null)
			jobID = generateJobId();

		this.jobId = jobID;
		this.phase = phase;
		this.ownerId = ownerId;
		this.quote = quote;
		/*this.destructionTime = destruction;
		this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/

		if (startTime > 0)
			this.startTime = new Date(startTime);
		if (endTime > 0)
			this.endTime = new Date(endTime);

		errorSummary = error;

	}

	public long getQuote() {
		return quote;
	}

	public void setQuote(long quote) {
		this.quote = quote;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public TapError getErrorSummary() {
		return errorSummary;
	}

	public void setErrorSummary(TapError errorSummary) {
		this.errorSummary = errorSummary;
	}

	public String getPhase() {
		return phase;
	}

	public void settPhase(String phase) {
		this.phase = phase;
	}
	
	public String getOwnerId() {
		return ownerId;
	}
	
	public String getJobId() {
		return jobId;
	}

	public String getExecutionDuration(){
		return null;
	}
	
	public String getDestructionTime(){
		return null;
	}
	
	public void setRequest(String request){
		this.request = request;
	}
	
	public String getRequest(){
		return request;
	}
	
	public String getResults() {
		return query.results().toString();
	}
	
	public AdqlQuery getQuery() {
		return query;
	}

	public void setQuery(String querystring) {
	
		//set query
		try {
			prepareQueryJob(querystring);
		}  catch (Exception ouch) {
			ouch.printStackTrace();
		}
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setPhase(String phase) {
		this.phase = phase;
		if (this.phase.equalsIgnoreCase(PHASE_RUN)){
			//run query
			try {
				runQueryJob();
			}  catch (Exception ouch) {
				ouch.printStackTrace();
			}
		} 
	}
	
	
	
	public String getQueryId() {
		// TODO Auto-generated method stub
		return this.query.ident().toString();
	}

	
	/**
	 * This function lets generating a unique ID.
	 * @return	A unique job identifier.
	 */
	protected String generateJobId() throws Exception {
		String generatedId = System.currentTimeMillis()+"A";
		generatedId = generatedId.substring(0, generatedId.length()-1)+(char)(generatedId.charAt(generatedId.length()-1)+1);
		return generatedId;
	}

	/**
	 * <p>Gets the value of the specified parameter.</p>
	 * 
	 * <p><i><u>note:</u> No case sensitivity for the UWS parameters ON THE CONTRARY TO the names of the additional parameters (which are case sensitive).</i></p>
	 * 
	 * @param name	Name of the parameter to get.
	 * 
	 * @return		Its value or <i>null</i> if there is no parameter with the given name or if the value is <i>null</i>.
	 * 
	 * @see UWSParameters#get(String)
	 */
	public Object getParameter(String name){
		if (name == null || name.trim().isEmpty())
			return null;

		name = name.trim();
		if (name.equalsIgnoreCase(PARAM_JOB_ID))
			return jobId;
		
		else if (name.equalsIgnoreCase(PARAM_QUOTE))
			return quote;
		else if (name.equalsIgnoreCase(PARAM_START_TIME))
			return startTime;
		else if (name.equalsIgnoreCase(PARAM_END_TIME))
			return endTime;
		else {
			return null;
		}
	}

	 /**
     * 
     * Run an query job
     * 
     */
	public void runQueryJob() throws IdentifierNotFoundException, IOException {
			
			/*For Testing purposes
			try {
				this.schema = resource.schemas().select(TapJobParams.DEFAULT_QUERY_SCHEMA);
			} catch (final NameNotFoundException ouch) {
				this.schema = resource.schemas().create(TapJobParams.DEFAULT_QUERY_SCHEMA);
			}
		
			String querystring = "Select top 10 * from Filter";
				this.query = this.schema.queries().create(querystring);
		*/
			try {
			
			
				if (this.query!=null){
				
					Status jobstatus = this.query.prepare();
					if (jobstatus == Status.READY){
						jobstatus = this.query.execute();
					}
				}
			
			} catch (final Exception ouch) {
				ouch.printStackTrace();

	        }
				
        }
	
	
	 /**
     * 
     * Create a query job
     * 
     */
	public void createNewQuery() throws IdentifierNotFoundException, IOException {
			

			try {
				this.schema = resource.schemas().select(TapJobParams.DEFAULT_QUERY_SCHEMA);
			} catch (final NameNotFoundException ouch) {
				this.schema = resource.schemas().create(TapJobParams.DEFAULT_QUERY_SCHEMA);
			}
			
			try {
				this.query = this.schema.queries().create("");
			
				if (this.query!=null){
					/*
					Status jobstatus = this.query.prepare();
					if (jobstatus == Status.READY){
						jobstatus = query.execute();
					}*/
				}
			
			} catch (final Exception ouch) {
				ouch.printStackTrace();

	        }
				
        }

    /**
     * 
     * Prepare a query job
     * 
     */
	public void prepareQueryJob(String querystring) throws IdentifierNotFoundException, IOException {
			
			if (resource!=null){
				
				
				try {
					this.schema = resource.schemas().select(TapJobParams.DEFAULT_QUERY_SCHEMA);
				} catch (final NameNotFoundException ouch) {
					this.schema = resource.schemas().create(TapJobParams.DEFAULT_QUERY_SCHEMA);
					ouch.printStackTrace();
				}
				
				try {
					 this.query.input(
							 querystring
                             );				
					
					
					if (this.query!=null){
					
						Status jobstatus = this.query.prepare();
						/*if (jobstatus == Status.READY){
							jobstatus = query.execute();
						}*/
					}
				
				} catch (final Exception ouch) {
					ouch.printStackTrace();
	
		        }
			}
				
        }


	/**
	 * Write UWSJob in XML format
	 * @param errorMessage
	 * @param writer
	 */
	public static void writeUWSJobToXML (UWSJob uwsjob, PrintWriter writer){
		
	        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
	        writer.append("	<uws:job xmlns:uws='http://www.ivoa.net/xml/UWS/v1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.ivoa.net/xml/UWS/v1.0 http://vo.ari.uni-heidelberg.de/docs/schemata/uws-1.0.xsd'>");
	     
	            writer.append("<uws:jobId>" + uwsjob.getJobId() + "</uws:jobId>");
	            writer.append("<uws:ownerId xsi:nil='true'>" + uwsjob.getOwnerId() + "</uws:ownerId>");
	            writer.append("<uws:phase>" + uwsjob.getPhase() + "</uws:phase>");
	            writer.append("<uws:startTime xsi:nil='true'>" + uwsjob.getStartTime() + "</uws:startTime>");
	            writer.append("<uws:endTime xsi:nil='true'>" + uwsjob.getEndTime() + "</uws:endTime>");
	            writer.append("<uws:executionDuration>" + uwsjob.getExecutionDuration() + "<uws:executionDuration>");
	            writer.append("<uws:destruction>" + uwsjob.getDestructionTime() + "</uws:destruction>");
	            writer.append("<uws:parameters>");
			        if (uwsjob.getRequest()!=null){
		            	writer.append("<uws:parameter id='request'>" + uwsjob.getRequest() + "</uws:parameter>");
			        }
			        if (uwsjob.getLang()!=null){
		            	writer.append("<uws:parameter id='lang'>" + uwsjob.getLang() + "</uws:parameter>");
			        }
			        if (uwsjob.getQuery()!=null){
		            	writer.append("<uws:parameter id='query'>" + uwsjob.getQuery() + "</uws:parameter>");
			        }
		        writer.append("</uws:parameters>");
		        
		        writer.append("<uws:results>");
			        if (uwsjob.getQuery() !=null){
		            	writer.append("<uws:result id='result'  xlink:href='" + uwsjob.getResults() + "'></uws:result>");
			        }
			    
		        writer.append("</uws:results>");
	       
	        writer.append("</uws:job>");
		
	}


 
}
