package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.util.List;


public class TapJobParams  {

	private static final long serialVersionUID = 1L;

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

	public static final String PARAM_MAX_REC = "maxRec";
	public static final int UNLIMITED_MAX_REC = -1;

	public static final String PARAM_QUERY = "query";
	public static final String PARAM_UPLOAD = "upload";

	public static final String PARAM_PROGRESSION = "progression";

	/**
	 * Default query schema
	 */
	public static final String DEFAULT_QUERY_SCHEMA = "query_schema";

	public static final int EXECUTION_DURATION = 180000;





}