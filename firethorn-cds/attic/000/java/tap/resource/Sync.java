package tap.resource;

/*
 * This file is part of TAPLibrary.
 * 
 * TAPLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TAPLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with TAPLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2011 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tap.ADQLExecutor;
import tap.ServiceConnection;
import tap.TAPException;
import tap.TAPParameters;
import tap.formatter.OutputFormat;
import uws.UWSException;
import uws.job.JobOwner;
import uws.service.UWSUrl;

public class Sync implements TAPResource {

	public static final String RESOURCE_NAME = "sync";

	protected String accessURL = null;

	protected final ServiceConnection<?> service;

	protected final Capabilities capabilities;

	public Sync(ServiceConnection<?> service, Capabilities capabilities){
		this.service = service;
		this.capabilities = capabilities;
	}

	@Override
	public String getName() { return RESOURCE_NAME; }

	@Override
	public void setTAPBaseURL(String baseURL) {
		accessURL = ((baseURL != null)?(baseURL+"/"):"")+getName();
	}

	@Override
	public void init(ServletConfig config) throws ServletException { ; }

	@Override
	public void destroy() { ; }

	@Override
	public boolean executeResource(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TAPException {
		TAPParameters params = new TAPParameters(request, service);
		if (params.request.equalsIgnoreCase(TAPParameters.REQUEST_GET_CAPABILITIES))
			return capabilities.executeResource(request, response);

		if (!service.isAvailable()){
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, service.getAvailability());
			return false;
		}

		JobOwner owner = null;

		// User Identification:
		if (service.getUserIdentifier() != null){
			try {
				owner = service.getUserIdentifier().extractUserId(new UWSUrl(request), request);
			} catch (UWSException e) {
				System.err.println("User identification failed !");
				e.printStackTrace(System.err);
			}
		}

		ADQLExecutor<?> processor;
		try{
			processor = service.getFactory().createExecutor(new HashMap<String,String>(0), owner);
		}catch(UWSException ex){
			throw new TAPException("Can not create an ADQLExecutor !", ex);
		}

		processor.loadTAPParams(params);

		// TODO Now: getFormat()=null => Job not initialized => no content type is specified => Need to search its corresponding MIME type !
		OutputFormat<?>[] formats = service.getOutputFormats();
		int indFormat = 0;
		while(indFormat<formats.length && !formats[indFormat].getShortMimeType().equalsIgnoreCase(processor.getFormat()) && !formats[indFormat].getMimeType().equalsIgnoreCase(processor.getFormat()))
			indFormat++;
		if (indFormat<formats.length){
			response.setContentType(formats[indFormat].getMimeType());
			System.out.println("@@@ FOUND OUTPUT FORMAT: "+formats[indFormat].getShortMimeType()+" = "+formats[indFormat].getMimeType()+" @@@");
		}else
			response.setContentType("application/xml");

		processor.startSync(response.getOutputStream());

		return true;
	}

}
