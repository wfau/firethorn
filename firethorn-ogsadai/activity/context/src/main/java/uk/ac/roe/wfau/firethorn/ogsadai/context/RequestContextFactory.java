/**
 *
 */
package uk.ac.roe.wfau.firethorn.ogsadai.context;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParam;
import uk.ac.roe.wfau.firethorn.ogsadai.activity.common.chaos.MonkeyParamImpl;
import uk.org.ogsadai.service.rest.authorisation.SecurityContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
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
	protected static final String DEFAULT_BASE = "firethorn" ;

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
                
                @Value("${firethorn.host:#{localhost2}}")
                private String host;
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

                    log.debug("endpoint [" + builder.toString() + "]");
                    return builder ;
                    }
                };

			@Override
            public EndpointBuilder builder()
			    {
			    return this.builder;
			    }
            @Override
            public StringBuilder endpoint()
                {
                return this.builder.endpoint();
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

			private MonkeyParam monkey = new MonkeyParamImpl();

            @Override
            public MonkeyParam monkey()
                {
                return this.monkey;
                }
            @Override
            public void monkey(final MonkeyParam monkey)
                {
                this.monkey = monkey;
                }
			};
		}
	}
