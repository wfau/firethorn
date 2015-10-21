/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.server.blue;

import uk.org.ogsadai.authorization.SecurityContext;

/**
 * Public interface for context information that gets passed
 * to each of the OGSA-DAI components in the workflow pipeline. 
 *
 */
public interface RequestContext
extends SecurityContext
	{
	
    /**
     * Public interface for a callback endpoint builder.
     *
     */
    public interface Callback
    	{
        /**
         * Get the protocol name.
         *
         */
        public String protocol();

        /**
         * Set the protocol name.
         *
         */
        public void protocol(final String protocol);

    	/**
         * Get the host name.
         *
         */
        public String host();

        /**
         * Set the host name.
         *
         */
        public void host(final String host);

        /**
         * Get the port number.
         *
         */
        public String port();
        
        /**
         * Set the port number.
         *
         */
        public void port(final String port);
        
        /**
         * Get the base URL path.
         *
         */
        public String base();

        /**
         * Set the base URL path.
         *
         */
        public void base(final String base);

        /**
         * The callback endpoint.
         * 
         */
        public StringBuilder endpoint();
    	}

    /**
     * Our callback endpoint builder.
     * 
     */
    public Callback callback();
    
    /**
     * Get the query identifier.
     *
     */
    public String ident();

    /**
     * Set the query identifier.
     *
     */
    public void ident(final String ident);

	}
