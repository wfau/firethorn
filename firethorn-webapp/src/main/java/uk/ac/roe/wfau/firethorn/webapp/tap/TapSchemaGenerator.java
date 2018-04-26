package uk.ac.roe.wfau.firethorn.webapp.tap;

import uk.ac.roe.wfau.firethorn.access.ProtectionException;
import uk.ac.roe.wfau.firethorn.entity.exception.NameNotFoundException;
import uk.ac.roe.wfau.firethorn.meta.jdbc.JdbcSchema;

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
	 * @return 
	 * @throws ProtectionException 
	 * @throws NameNotFoundException 
	 * 
	 */
	public JdbcSchema createTapSchemaJdbc()
    throws ProtectionException, NameNotFoundException;
	
	
 
}
