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
	   
	public static final String FIRETHORN_BASE = "http://localhost:8080/firethorn/";
	
	public static final String FIRETHORN_TAP_BASE = FIRETHORN_BASE + "tap/";

	public static final String FIRETHORN_QUERY_BASE = FIRETHORN_BASE + "adql/query/";



}