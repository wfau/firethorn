package tap;

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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import uws.UWSException;

import uws.job.JobList;
import uws.job.JobOwner;

import uws.service.AbstractUWS;

//ZRQ
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class Uws4Tap extends AbstractUWS<JobList<ADQLExecutor>, ADQLExecutor> {

    //ZRQ
    private static Logger logger = LoggerFactory.getLogger(Uws4Tap.class);

	private static final long serialVersionUID = 1L;

	protected final ServiceConnection service;

	public Uws4Tap(ServiceConnection service) {
		super();
logger.debug("Uws4Tap(ServiceConnection)");

		this.service = service;
	}

	@Override
	public ADQLExecutor<?> createJob(final Map<String, String> params, final JobOwner owner) throws UWSException {
logger.debug("createJob(Map<String, String>, JobOwner)");
		try{
			ADQLExecutor<?> executor = service.getFactory().createExecutor(params, owner);
			return executor;
		}catch(TAPException ex){
			throw new UWSException(UWSException.INTERNAL_SERVER_ERROR, ex, "Can not create an ADQLExecutor !");
		}
	}

	@Override
	public ADQLExecutor createJob(HttpServletRequest request, JobOwner owner) throws UWSException {
logger.debug("createJob(HttpServletRequest, JobOwner)");
		if (!service.isAvailable())
			throw new UWSException(HttpServletResponse.SC_SERVICE_UNAVAILABLE, service.getAvailability());

		try {
			// Extract TAP parameters:
			TAPParameters tapParams = new TAPParameters(request, service);
			Map<String, String> params = tapParams.otherParameters;

			// Control the execution duration:
			getExecutionDurationController().init(params);

			// Control the destruction time:
			getDestructionTimeController().init(params);

			// Create the job:
			ADQLExecutor newJob = createJob(params, owner);

logger.debug("Job created [{}]", newJob);

			// Set its TAP parameters:
			newJob.loadTAPParams(tapParams);

			// Set its execution manager:
			newJob.setExecutionManager(getExecutionManager());

			// Start the job if asked:
			newJob.applyPhaseParam();

			return newJob;
		} catch (TAPException e) {
			throw new UWSException(UWSException.INTERNAL_SERVER_ERROR, e, "["+getName()+"]  Error while parsing TAP parameters.");
		}
	}

	@Override
	public Map<String, String> extractParameters(HttpServletRequest req) throws UWSException {
logger.debug("extractParameters(HttpServletRequest)");
		if (TAPParameters.isMultipartContent(req))
		    {
logger.debug("Multipart request");
			HashMap<String, String> params = new HashMap<String, String>();
			try{
				MultipartRequest multipart = new MultipartRequest(req, service.getUploadDirectory());
				Enumeration<String> names = multipart.getParameterNames();
				String name = null;
				while(names.hasMoreElements()){
logger.debug("param [{}]", name);
					name = names.nextElement();
					params.put(name.toLowerCase(), multipart.getParameter(name));
				}
			}catch(IOException ioe){
				throw new UWSException(UWSException.INTERNAL_SERVER_ERROR, ioe, "Impossible to extract request parameters !");
			}

			return params;
		}else
		    {
logger.debug("Standard request");
            return super.extractParameters(req);
            }
	}

    //ZRQ
	public boolean executeRequest(HttpServletRequest request, HttpServletResponse response)
	throws UWSException, IOException
	    {
        logger.debug("executeRequest(HttpServletRequest, HttpServletResponse)");
        return super.executeRequest(
            request,
            response
            );
        }
    }
