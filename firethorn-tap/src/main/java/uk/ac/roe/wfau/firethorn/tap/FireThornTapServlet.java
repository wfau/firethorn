package uk.ac.roe.wfau.firethorn.tap;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.roe.wfau.firethorn.tap.FireThornMetaStuff;

import tap.resource.TAP;

import uws.UWSException;

/*
 * Replace with a Spring controller.
 *
 */
public class FireThornTapServlet
extends HttpServlet
    {

	private static final long serialVersionUID = 1L;

	private TAP tap = null;

	@Override
	public void init() throws ServletException
	    {
		super.init();
		try {
			// 1. Create a TAP instance with the written ServiceConnection:
			tap = new TAP(new FireThornServiceConnection());

			// Customize the TAP home page:
			tap.setHomePageURI(FireThornMetaStuff.HOME_PAGE);
		    }
	    catch (UWSException e)
	        {
		    throw new ServletException(e);
		    }
	    }

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
		try {
			// 2. Forward all requests to the TAP instance:
			tap.executeRequest(request, response);

		    }
	    catch(Throwable t)
	        {
			System.err.println("Request aborted !");
			t.printStackTrace(System.err);
			response.sendError(
			    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			    t.getMessage()
			    );
			}
		}
	}

