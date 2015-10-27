package uk.ac.roe.wfau.firethorn.webapp.tap;

import java.util.List;


public class CommonParams  {


	
	/**
	 * Default query schema
	 */
	static final String DEFAULT_QUERY_SCHEMA = "query_schema";
	
	 /**
     * VOTable MIME type.
     *
     */
    public static final String VOTABLE_MIME = "application/x-votable+xml" ;
    
    /**
     * TextXml MIME type.
     *
     */
    public static final String TEXT_XML_MIME = "text/xml" ;
	
   	
	/**
	 * Capabilities jsp template path
	 */
    public static final String VOSI_XML_VIEW = "adql/vosi-xml" ;

    
    
	/**
	 * Capabilities jsp template path
	 */
    public static final String CAPABILITIES_XML_VIEW = "adql/capabilities-xml" ;

	/**
	 * Blue Query path
	 */
	static final String BLUE_QUERY_PATH = "blue/query";
	
	/**
	 * Default format
	 */
	static final String DEFAULT_FORMAT = "VOTable";

	/**
	 * Default version
	 */
	static final String DEFAULT_VERSION = "1.0";
	
	/**
	 * Default request
	 */
	public static final String DEFAULT_REQUEST = "doQuery";
	
	/**
	 * Default request
	 */
	public static final String DEFAULT_LANG = "ADQL";

}