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
package uk.ac.roe.wfau.firethorn.cds.uws;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uws.UWSException;

import uws.job.JobList;
import uws.job.JobOwner;

import uws.service.BasicUWS;
import uws.service.QueuedBasicUWS;
import uws.service.UWSUrl;
import uws.service.UserIdentifier;

/**
 * Probably replace this with a Spring controller ?
 *
 */
public class FireThornUwsServlet
    extends HttpServlet
    {

	private static final long serialVersionUID = 1L;

	private BasicUWS<FireThornUwsJob> uws = null;

	@Override
	public void init()
	throws ServletException
	    {
		super.init();
		try {

			// 1. Create a UWS with a queue of 10 jobs:
			uws = new QueuedBasicUWS<FireThornUwsJob>(FireThornUwsJob.class, 10);

			// 2. Create 1 jobs list:
			uws.addJobList(new JobList<FireThornUwsJob>("jobs"));

			// Change the home page (/asov/uwsHome.html):
			uws.setHomePage("/asov/uwsHome.html");

			// Identify users by their IP address:
			uws.setUserIdentifier(
			    new UserIdentifier() 
			        {
				    private static final long serialVersionUID = 1L;
/*
			        @Override
			        public String extractUserId(UWSUrl urlInterpreter, HttpServletRequest request)
			        throws UWSException
			            {
				        return request.getRemoteAddr();
                        }
 */
/**/
			        @Override
			        public JobOwner extractUserId(UWSUrl urlInterpreter, HttpServletRequest request)
			        throws UWSException
			            {
			            return new JobOwner()
			                {
                        	public String getID()
                        	    {
                        	    return "owner-id";
                        	    }

                        	public String getPseudo()
                        	    {
                        	    return "owner-pseudo";
                        	    }
			                };
			            }
 /**/
			        }
			    );
		    }
	    catch (UWSException e)
	        {
			throw new ServletException(e);
		    }
	    }

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	    {
		try {
			// 3. Forward all requests to the UWS object:
			uws.executeRequest(request, response);
		    }
	    catch (UWSException e)
	        {
			response.sendError(e.getHttpErrorCode(), e.getMessage());
		    }
	    }
    }

