package uk.ac.roe.wfau.firethorn.webapp.tap;

import uk.ac.roe.wfau.firethorn.meta.adql.AdqlResource;



/**
 * Generate TAP_SCHEMA of a resource
 * 
 * @author stelios
 * 
 */
public interface TapSchemaGenerator{
	
	/**
	 * Create the TAP_SCHEMA for a given resource
	 * 
	 */
	 void createTapSchema();
	
	
 
}
