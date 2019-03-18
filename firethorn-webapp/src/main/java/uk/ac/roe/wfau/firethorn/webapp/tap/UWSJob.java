package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryBase;
import uk.ac.roe.wfau.firethorn.adql.query.AdqlQueryLimits;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueQuery;
import uk.ac.roe.wfau.firethorn.adql.query.blue.BlueTask.TaskState;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;


/**
 * @author stelios
 *
 */
@Slf4j
public class UWSJob {

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

	/** Value of the parameter <i>phase</i> when executing  */
	public static final String PHASE_EXECUTING = "EXECUTING";

	/** Value of the parameter <i>phase</i> when completed */
	public static final String PHASE_COMPLETED = "COMPLETED";
	
	/** Value of the parameter <i>phase</i> when aborted */
	public static final String PHASE_ABORTED = "ABORTED";	
	
	/** Value of the parameter <i>phase</i> when queued */
	private static final String PHASE_QUEUED = null;

	/** Value of the parameter <i>phase</i> when query produced an error  */
	public static final String PHASE_ERROR = "ERROR";
		
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
	private final String jobURL;
	private final String ownerId;
	private long quote = QUOTE_NOT_KNOWN;
	private String phase = null;
	private String lang = null;
	private AdqlResource resource = null;
	private BlueQuery query = null;
	private String format = null;
	private String version = null;
	private String maxrec = null;
	private String request = null;
	private String jobstatus = null;

	/** <p>This error summary gives a human-readable error message for the underlying job.</p>
	 * <i><u>Note:</u> This object is intended to be a detailed error message, and consequently,
	 * might be a large piece of text such as a stack trace.</i> */
	protected TapError errorSummary = null;

	/**
	 * UWSJobFactory
	 */
	private UWSJobFactory myFactory;

	
	/* ************ */
	/* CONSTRUCTORS */
	/* ************ */
	
	
	/**
	 * UWSJob Constructor 
	 * @param f
	 * @param resource
	 * @param jobType
	 * @throws Exception
	 */
	protected UWSJob(UWSJobFactory f, AdqlResource resource, String jobType) throws Exception {
		    this.myFactory = f;
		    this.jobId = "";
			this.phase = PHASE_INITIAL;
			this.ownerId = null;
			this.maxrec = null;
			this.quote = QUOTE_NOT_KNOWN;
			this.startTime = new Date();
			this.errorSummary = new TapError();
			this.resource = resource;
			this.query = f.createNewQuery(resource);
			this.format = CommonParams.DEFAULT_FORMAT;
			this.version = CommonParams.DEFAULT_VERSION;
			this.jobURL = generateJobURL(jobType);
			this.jobstatus = CommonParams.JOB_PENDING;

			/*this.destructionTime = destruction;
			this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/
	}
	
	/**
	 * UWSJob Constructor 
	 * @param f
	 * @param resource
	 * @param query
	 * @param jobType
	 * @throws Exception
	 */
	public UWSJob(UWSJobFactory f, AdqlResource resource,  BlueQuery query, String jobType) throws Exception {
		this.myFactory = f ;
		this.jobId = query.ident().toString();
		this.phase = getPhasefromState(query.state());
		this.ownerId = null;
		this.maxrec = query.param().map().containsKey("maxrec") ? query.param().map().get("maxrec") : null;
		this.quote = QUOTE_NOT_KNOWN;
		this.format = query.param().map().containsKey("format") ? query.param().map().get("format") : null;
		this.version = query.param().map().containsKey("version") ? query.param().map().get("version") :null;
		this.lang = query.param().map().containsKey("lang") ? query.param().map().get("lang") : null;
		this.request = query.param().map().containsKey("request") ? query.param().map().get("request") : null;
		this.startTime = new Date();
		this.errorSummary = new TapError();
		this.resource = resource;
		this.query = query;
		this.jobURL = generateJobURL(jobType);
		this.jobstatus = query.param().map().containsKey("jobstatus") ? query.param().map().get("jobstatus") : null;
		/*this.destructionTime = destruction;
		this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/
		
	}

	/**
	 * Get the async job Phase from a TaskState
	 * @param state
	 * @return
	 */
	private String getPhasefromState(TaskState state) {

		String phase = "";
		switch (state) {
        case CANCELLED :  
        	phase = PHASE_ABORTED;
        	break;
		case COMPLETED:
			phase = PHASE_COMPLETED;
        	break;
		case EDITING:
			phase = PHASE_INITIAL;
        	break;
		case ERROR:
			phase = PHASE_ERROR;
        	break;
		case FAILED:
			phase = PHASE_ERROR;
        	break;
		case QUEUED:
			phase = PHASE_QUEUED;
        	break;
		case READY:
			phase = PHASE_INITIAL;
        	break;
		case RUNNING:
			phase = PHASE_EXECUTING;
        	break;
		default:
			phase = PHASE_INITIAL;
			break;
		}
		return phase;
	}

	public long getQuote() {
		return quote;
	}

	public void setQuote(long quote) {
		this.quote = quote;
	}

	public String getStartTime() {
		return this.query.created().toString();
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.query.modified().toString();
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
	
	public String getJobURL() {
		return jobURL;
	}
	
	public String getExecutionDuration(){
		return Long.toString(0);
	}
	
	public String getDestructionTime(){
		return "";
	}
	
	public void setRequest(String request) throws ProtectionException{
		this.getQuery().param().map().put("request",request);
		this.request = request;
	}
	
	public String getRequest(){
		return request;
	}
	

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) throws ProtectionException {
		this.getQuery().param().map().put("format",format);
		this.format = format;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) throws ProtectionException {
		this.getQuery().param().map().put("version",version);
		this.version = version;
	}

	public String getMaxrec() {
		return maxrec;
	}

	public void setMaxrec(String maxrec) throws ProtectionException {
		this.getQuery().param().map().put("maxrec",maxrec);	
		
		if (maxrec!=null) {
			Long maxrec_long = Long.parseLong(maxrec.trim());
			AdqlQueryBase.Limits limit = new AdqlQueryLimits(maxrec_long, null ,null);
			this.getQuery().limits(limit);
		}
		
		this.maxrec = maxrec;
	}
	

	public void setQuery(String querystring) {
	
		//set query
		try {
			myFactory.prepareQueryJob(this.resource, this.query, querystring);
		}  catch (Exception ouch) {
			ouch.printStackTrace();
		}
	}

	public String getLang() {
		if (lang==null){
			return "ADQL";
		} else {
			return lang;
		}
	}

	public void setLang(String lang) throws ProtectionException {
		this.getQuery().param().map().put("lang",lang);
		this.lang = lang;
	}
	
	/**
	 * Set the query uwsjob phase
	 * @param phase
	 */
	public void setPhase(String phase) {
		this.phase = phase;
		if (this.phase.equalsIgnoreCase(PHASE_RUN)){
			//run query
			try {
				myFactory.runQueryJob(this.query);
			}  catch (Exception ouch) {
				ouch.printStackTrace();
			}
		} else if (this.phase.equalsIgnoreCase(PHASE_ABORT)){
			try {
				myFactory.setQueryJob(this.query, TaskState.CANCELLED);
			}  catch (Exception ouch) {
				ouch.printStackTrace();
			}
		}
	}

	
	public void setJobStatus(String status) throws ProtectionException {
		this.getQuery().param().map().put("jobstatus", status);	
		this.jobstatus=status;
	}
	
	public String getJobStatus() {
		return jobstatus;
	}
	
	/**
	 * Get the BlueQuery
	 * @return BlueQuery
	 */
	public BlueQuery getQuery() {
		return query;
	}
	
	/**
	 * Set the Query 
	 * @return
	 */
	public void setQuery(BlueQuery query) {
		this.query = query;
	}


	/**
	 * Return the full query URL
	 * @return query URL
	 */
	public String getFullQueryURL() {
		String url = myFactory.getBaseurl() + "/" + CommonParams.BLUE_QUERY_PATH + "/" + getQueryId();  
		return url;
	}

	/**
	 * Get UWS results url
	 * @return
	 */
	public String getJobURLResults() {
		return this.getJobURL() + "/results/result";
	}
	
	/**
	 * 
	 * @param jobType
	 * @return
	 */
	private String generateJobURL(String jobType) {
		
		if (jobType=="ASYNC"){
			return myFactory.getBaseurl() + "/tap/" + resource.ident() + "/async/" + this.getJobId() ;
		} else {
			return myFactory.getBaseurl() + "/tap/" + resource.ident() + "/async/" + this.getJobId() ;
		
		}
		
	}
	/**
	 * Get the Query results URL (votable as string)
	 * @return url
	 * @throws ProtectionException 
	 */
	public String getResults() throws ProtectionException {
		String url = "";
		if ( this.query.results().adql()!=null){
			 url = this.query.results().adql().link() + "/votable";
		} 
		log.debug("UWSJob Results:" + url);
		return url;
	}
	
	/**
	 * Get the ID of the query
	 * @return
	 */
	public String getQueryId() {
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
	 * Write UWS Job results
	 * @param uwsjob
	 * @param writer
	 */
	public String writeUWSResultToXML (){
		StringBuilder writer = new StringBuilder();
		String resultsUrl = this.getJobURLResults();
		writer.append("<uws:results xsi:schemaLocation='http://www.ivoa.net/xml/UWS/v1.0 http://vo.ari.uni-heidelberg.de/docs/schemata/uws-1.0.xsd http://www.w3.org/1999/xlink http://vo.ari.uni-heidelberg.de/docs/schemata/xlink.xsd'>");
		writer.append("<uws:result id='result' xlink:href='" + resultsUrl +"'/></uws:results>");
		return writer.toString();
	}
	
	/**
	 * Write UWSJob in XML format
	 * @param uwsjob
	 * @param writer
	 * @throws ProtectionException 
	 */
	public String writeUWSJobToXML () throws ProtectionException{

			StringBuilder writer = new StringBuilder();
			String ownerId = ((this.getOwnerId() == null) ? "" : this.getOwnerId());
	        writer.append("<?xml version='1.0' encoding='UTF-8'?>");
	        writer.append("	<uws:job xmlns:uws='http://www.ivoa.net/xml/UWS/v1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xlink='http://www.w3.org/1999/xlink' xsi:schemaLocation='http://www.ivoa.net/xml/UWS/v1.0 http://www.ivoa.net/xml/UWS/v1.0'>");
	     
	            writer.append("<uws:jobId>" + this.getJobId() + "</uws:jobId>");
	            writer.append("<uws:ownerId xsi:nil='true'>" + ownerId + "</uws:ownerId>");
	            writer.append("<uws:phase>" + this.getPhase() + "</uws:phase>");
	            writer.append("<uws:startTime>" + this.getStartTime() + "</uws:startTime>");
	            writer.append("<uws:endTime>" + this.getEndTime() + "</uws:endTime>");
	            writer.append("<uws:executionDuration>" + this.getExecutionDuration() + "</uws:executionDuration>");
	            writer.append("<uws:destruction xsi:nil='true'>" + this.getDestructionTime() + "</uws:destruction>");
	            writer.append("<uws:parameters>");
			        if (this.getRequest()!=null){
		            	writer.append("<uws:parameter id='request'>" + this.getRequest() + "</uws:parameter>");
			        } else {
		            	writer.append("<uws:parameter id='request'>None</uws:parameter>");
			        }
			        if (this.getLang()!=null){
		            	writer.append("<uws:parameter id='lang'>" + this.getLang() + "</uws:parameter>");
			        } else {
		            	writer.append("<uws:parameter id='lang'>None</uws:parameter>");
			        }
			        if (this.getQuery()!=null){
		            	writer.append("<uws:parameter id='query'>" + this.getQuery().input() + "</uws:parameter>");
			        }
			        if (this.getFormat()!=null){
		            	writer.append("<uws:parameter id='format'>" + this.getFormat() + "</uws:parameter>");
			        } else {
		            	writer.append("<uws:parameter id='format'>None</uws:parameter>");
			        }
			        if (this.getVersion()!=null){
		            	writer.append("<uws:parameter id='version'>" + this.getVersion() + "</uws:parameter>");
			        } 
			        if (this.getMaxrec()!=null){
		            	writer.append("<uws:parameter id='maxrec'>" + this.getMaxrec() + "</uws:parameter>");
			        } else {
		            	writer.append("<uws:parameter id='maxrec'>None</uws:parameter>");
			        }
			        if (this.getJobStatus()!=null){
		            	writer.append("<uws:parameter id='jobstatus'>" + this.getJobStatus() + "</uws:parameter>");
			        } else {
		            	writer.append("<uws:parameter id='jobstatus'>None</uws:parameter>");
			        }
		        writer.append("</uws:parameters>");
		        
		        writer.append("<uws:results>");
			        if (this.getQuery() !=null){
		            	writer.append("<uws:result id='result'  xlink:href='" + this.getResults() + "'></uws:result>");
			        }
			    
		        writer.append("</uws:results>");
	       
	        writer.append("</uws:job>");
		
	        return writer.toString();
	}


 
}
