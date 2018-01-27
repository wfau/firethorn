package uk.ac.roe.wfau.firethorn.webapp.tap;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;

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
	 * @throws ProtectionException 
	 * 
	 */
	public void createTapSchema()
    throws ProtectionException;
	
	
 
}
