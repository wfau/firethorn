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
	 * Create the initial TAP_SCHEMA structure & data 
	 * 
	 */
	public void createStructure(String fromScript);
	
	/**
	 * Insert resource metadata into the TAP_SCHEMA schema
	 * @param resource
	 */
	public void insertMetadata(AdqlResource resource); 
 
}
