package uk.ac.roe.wfau.firethorn.webapp.tap;


/**
 * Generate TAP_SCHEMA of a resource
 * 
 * @author stelios
 * 
 */

public interface TapSchemaGenerator{
	
	/**
	 * Create the TAP_SCHEMA for a given resource
	 * @param componentFactories 
	 * 
	 */
	public void createTapSchema();
	
	
 
}
