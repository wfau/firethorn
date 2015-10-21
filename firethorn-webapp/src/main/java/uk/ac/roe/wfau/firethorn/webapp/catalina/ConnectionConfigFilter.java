/**
 * 
 */
package uk.ac.roe.wfau.firethorn.webapp.catalina;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.catalina.Container;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.commons.lang3.reflect.FieldUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * A servlet Filter to detect the Tomcat context and port numbers.
 * http://stackoverflow.com/a/7482746
 *
 */
@Slf4j
public class ConnectionConfigFilter implements Filter
	{

	/**
	 * Public constructor.
	 * 
	 */
	public ConnectionConfigFilter()
		{
		}

	@Override
	public void destroy()
		{
		}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException
		{
		chain.doFilter(
			request,
			response
			);
		}

	@Override
	public void init(final FilterConfig config)
		throws ServletException
		{
		final ServletContext context = config.getServletContext();

        try {
            Object object = FieldUtils.readField(context, "context", true);

            StandardContext std = (StandardContext) FieldUtils.readField(object, "context", true);

    		log.debug("StandardContext");
    		log.debug("  name [{}]", std.getName());
            
            Container container = (Container) std;
            
            Container c = container.getParent();

    		log.debug("Container");
    		log.debug("  name [{}]", c.getName());

            while (c != null && !(c instanceof StandardEngine))
        		{
        		c = c.getParent();
        		}

            if (c != null)
            	{
        		log.debug("Container");
        		log.debug("  name [{}]", c.getName());
            	
            	StandardEngine engine = (StandardEngine) c;
            	for (Connector connector : engine.getService().findConnectors())
            		{
            		// Get port for each connector. Store it in the ServletContext or whatever
            		log.debug("Connector");
            		log.debug("  port     [{}]", connector.getPort());
            		log.debug("  protocol [{}]", connector.getProtocol());
            		}
            	}
        	}
        catch (Exception ouch)
        	{
        	ouch.printStackTrace();
        	}
		}
	}
