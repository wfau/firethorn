package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;
import uk.ac.roe.wfau.firethorn.meta.adql.AdqlSchema;
import uk.ac.roe.wfau.firethorn.webapp.tap.CommonParams;
import uk.ac.roe.wfau.firethorn.webapp.tap.UWSJobFactory;
import uk.ac.roe.wfau.firethorn.blue.*;
import uk.ac.roe.wfau.firethorn.blue.BlueTask.TaskState;


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
	private String request = null;
	private String lang = null;
	private String results = null;
	private AdqlResource resource = null;
	private AdqlSchema schema = null;
	private BlueQuery query = null;
	
	/** <p>This error summary gives a human-readable error message for the underlying job.</p>
	 * <i><u>Note:</u> This object is intended to be a detailed error message, and consequently,
	 * might be a large piece of text such as a stack trace.</i> */
	protected TapError errorSummary = null;

	/**
	 * UWSJobFactory
	 */
	private UWSJobFactory myFactory;

	/**
	 * UWSJob Constructor 
	 * @param f
	 * @param resource
	 * @param jobType
	 * @throws Exception
	 */
	protected UWSJob(UWSJobFactory f, AdqlResource resource, String jobType) throws Exception {
		    myFactory = f ;
		    this.jobId = "";
			this.phase = PHASE_INITIAL;
			this.ownerId = null;
			this.quote = QUOTE_NOT_KNOWN;
			/*this.destructionTime = destruction;
			this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/
			this.startTime = new Date();
			errorSummary = new TapError();
			this.resource = resource;
			this.query = f.createNewQuery(resource);
			this.jobURL = generateJobURL(jobType);
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

	/* ************ */
	/* CONSTRUCTORS */
	/* ************ */

	
	public UWSJob(UWSJobFactory f, AdqlResource resource,  BlueQuery query, String jobType) throws Exception {
		myFactory = f ;
		this.jobId = query.ident().toString();
		this.phase = getPhasefromState(query.state());
		this.ownerId = null;
		this.quote = QUOTE_NOT_KNOWN;
		/*this.destructionTime = destruction;
		this.executionDuration = (maxDuration<0)?UNLIMITED_DURATION:maxDuration;*/
		this.startTime = new Date();
		errorSummary = new TapError();
		this.resource = resource;
		this.query = query;
		this.jobURL = generateJobURL(jobType);
		
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
			phase = PHASE_INITIAL;
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
		return null;
	}
	
	public String getDestructionTime(){
		return null;
	}
	
	public void setRequest(String request){
		this.request = request;
	}
	
	public String getRequest(){
		if (request==null){
			return "doQuery";
		} else {
			return request;
		}
	}
	
	public String getResults() {
		String url = this.query.results().jdbc().link() + "/votable";
		return url;
	}
	
	public BlueQuery getQuery() {
		return query;
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

	public void setLang(String lang) {
		this.lang = lang;
	}

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
	
	public String getFullQueryURL() {
		String url = myFactory.getBaseurl() + "/" + CommonParams.BLUE_QUERY_PATH + "/" + getQueryId();  
		return url;
	}
	
	
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
	 * Get UWS results url
	 * @return
	 */
	public String getJobURLResults() {
		return this.getJobURL() + "/results/result";
	}



	
 
}
