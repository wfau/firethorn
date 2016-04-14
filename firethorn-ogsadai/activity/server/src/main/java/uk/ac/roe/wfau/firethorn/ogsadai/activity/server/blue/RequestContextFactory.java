/**
 * 
 */
package uk.ac.roe.wfau.firethorn.ogsadai.activity.server.blue;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ogsadai.service.rest.authorisation.SecurityContextFactory;

/**
 *
 */
public class RequestContextFactory
implements SecurityContextFactory 
	{
    /**
     * Debug logger.
     *
     */
    private static Log log = LogFactory.getLog(RequestContextFactory.class);

    /**
	 * The default protocol, {@value}.
	 * 
	 */
	protected static final String DEFAULT_PROTOCOL = "http" ;

	/**
	 * The default host name, {@value}.
	 * 
	 */
	protected static final String DEFAULT_HOST = "localhost" ;

	/**
	 * The default port number, {@value}.
	 * 
	 */
	protected static final String DEFAULT_PORT = "8080" ;

	/**
	 * The default path, {@value}.
	 * 
	 */
	protected static final String DEFAULT_BASE = "/firethorn" ;
	
	/**
	 * Public constructor. 
	 * 
	 */
	public RequestContextFactory()
		{
		}

	@Override
	public RequestContext createContext(final HttpServletRequest request)
		{
        log.debug("createContext(HttpServletRequest)");
		final String remote = request.getRemoteHost();
        log.debug("remote [" + remote + "]");
        
        return new RequestContext()
			{
			private EndpointBuilder builder = new EndpointBuilder()
                {
                private String protocol = DEFAULT_PROTOCOL;
                @Override
                public String protocol()
                    {
                    return this.protocol;
                    }
                @Override
                public void protocol(final String protocol)
                    {
                    this.protocol = protocol;
                    }

                private String host = (remote != null) ? remote : DEFAULT_HOST ;
                @Override
                public String host()
                    {
                    return this.host;
                    }
                @Override
                public void host(final String host)
                    {
                    this.host = host;
                    }

                private String port = DEFAULT_PORT;
                @Override
                public String port()
                    {
                    return this.port;
                    }
                @Override
                public void port(final String port)
                    {
                    this.port = port;
                    }

                private String base = DEFAULT_BASE;
                @Override
                public String base()
                    {
                    return this.base;
                    }
                @Override
                public void base(final String path)
                    {
                    this.base = path;
                    }

                @Override
                public StringBuilder endpoint()
                    {
                    final StringBuilder builder = new StringBuilder();
                    builder.append(this.protocol);
                    builder.append("://");
                    builder.append(this.host);
                    builder.append(":");
                    builder.append(this.port);
                    builder.append("/");
                    builder.append(this.base);

                    builder.append(
                        "/blue/query/callback/"
                        );
                    builder.append(
                        ident()
                        );

                    log.debug("endpoint [" + builder.toString() + "]");
                    return builder ;
                    }
                };

			@Override
            public EndpointBuilder builder()
			    {
			    return this.builder;
			    }

			private String ident;
			@Override
			public String ident()
				{
				return this.ident;
				}
			@Override
			public void ident(String ident)
				{
				this.ident = ident;
				}
            @Override
            public CallbackHandler handler()
                {
                return new CallbackHandler(
                    this
                    );
                }
            @Override
            public StringBuilder endpoint()
                {
                return this.builder.endpoint();
                }
			};
		}
	}
