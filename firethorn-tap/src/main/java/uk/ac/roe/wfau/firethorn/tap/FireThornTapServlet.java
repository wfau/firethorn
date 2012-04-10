/*
 *
 * Copyright (c) 2012, ROE (http://www.roe.ac.uk/)
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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

