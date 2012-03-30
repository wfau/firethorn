package uk.ac.roe.wfau.firethorn.tap;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import uk.ac.roe.wfau.firethorn.tap.FireThornMetaStuff;
import uk.ac.roe.wfau.firethorn.tap.output.FireThornOutputFormat;
import uk.ac.roe.wfau.firethorn.tap.output.FireThornCSVFormatter;
import uk.ac.roe.wfau.firethorn.tap.output.FireThornVOTableFormatter;

import tap.ADQLExecutor;
import tap.ServiceConnection;
import tap.TAPException;
import tap.TAPFactory;

import tap.metadata.TAPMetadata;
import uws.UWSException;

//import uws.job.LocalResult;
//import uws.job.JobOwner;

import uws.service.UWSUrl;
import uws.service.UserIdentifier;

public class FireThornServiceConnection
implements ServiceConnection<FireThornTapResult>
    {

	private final ArrayList<String> coordSys;

	private final TAPMetadata meta;
//	private final OutputFormat<FireThornTapResult>[] formats;
	private final FireThornOutputFormat[] formats;
	private final UserIdentifier identifier;

	@SuppressWarnings("unchecked")
	public FireThornServiceConnection()
	    {
		// List all schemas, tables and columns available:
		meta = new TAPMetadata();
		meta.addSchema(
		    FireThornMetaStuff.getTAPSchema()
		    );

		// List all available outputs (VOTable & CSV):
		formats = new FireThornOutputFormat[2];
		formats[0] = new FireThornVOTableFormatter(this);
		formats[1] = new FireThornCSVFormatter(",");

		// List all allowed coordinate systems:
		coordSys = new ArrayList<String>(2);
		coordSys.add("ICRS");
		coordSys.add("ICRS BARYCENTER");

		// Create a way to identify users (by IP address):
		identifier = new UserIdentifier() {
			private static final long serialVersionUID = 1L;

			@Override
			public String extractUserId(UWSUrl urlInterpreter, HttpServletRequest request)
			throws UWSException
			    {
				return request.getRemoteAddr();
			    }

/*
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
 */
		    };
	    }

	/* *************************** */
	/* GENERAL SERVICE DESCRIPTION */
	/* *************************** */
	@Override
	public String getProviderName()
	    {
	    return "WFAU";
	    }

	@Override
	public String getProviderDescription()
	    {
	    return "Experimental TAP service.";
	    }
	
	@Override
	public void log(tap.ServiceConnection.LogType type, String msg)
	    {
		switch(type)
		    {
		    case INFO:
			    System.out.println("INFO: "+msg);
			    break;
		    default:
			    System.err.println(type+": "+msg);
			    break;
    		}
    	}

/*
	@Override
	public void log(Throwable ouch)
	    {
	    System.err.println("exception : " + ouch.getMessage());
        }
 */

	/* ******************** */
	/* SERVICE AVAILABILITY */
	/* ******************** */
	@Override
	public boolean isAvailable()
	    {
	    return true;
	    }

	@Override
	public String getAvailability()
	    {
	    return "Demo TAP for ASOV available !";
	    }

	/* ************************ */
	/* AVAILABLE TABLES/COLUMNS */
	/* ************************ */
	@Override
	public TAPMetadata getTAPMetadata()
	    {
	    return meta;
	    }

	/* ****************** */
	/* COORDINATE SYSTEMS */
	/* ****************** */
	@Override
	public Collection<String> getCoordinateSystems()
	    {
	    return coordSys;
	    }

	/* ******************* */
	/* USER IDENTIFICATION */
	/* ******************* */
	@Override
	public UserIdentifier getUserIdentifier()
	    {
	    return identifier;
	    }

	/* ********************* */
	/* SERVICE CONFIGURATION */
	/* ********************* */
	@Override
	//public int[] getExecutionDuration()
	public long[] getExecutionDuration()
	    {
		return new long[]{3600, 7200};		// default = 1 hour , max = 2 hours
	    }

	@Override
	public int[] getRetentionPeriod()
	    {
		return new int[]{604800, 604800};	// default = max = 7 days
	    }

	@Override
//	public TAPFactory<FireThornTapResult> getFactory()
	public FireThornDBFactory getFactory()
	    {
		return new FireThornDBFactory(
		    this
		    );
	    }
	
	/* ******************** */
	/* OUTPUT CONFIGURATION */
	/* ******************** */
	@Override
//	public OutputFormat<FireThornTapResult> getOutputFormat(String type)
	public FireThornOutputFormat getOutputFormat(String type)
	    {
		for(FireThornOutputFormat format : formats)
		    {
			if (format.getMimeType().equalsIgnoreCase(type) || format.getShortMimeType().equalsIgnoreCase(type))
			    {
				return format;
				}
		    }
		return null;
	    }

	@Override
//	public OutputFormat<FireThornTapResult>[] getOutputFormats()
	public FireThornOutputFormat[] getOutputFormats()
	    {
		return formats;
	    }

	@Override
	public int[] getOutputLimit()
	    {
		return new int[]{2000, 1000000};	// default = 2000 rows, max = 1000000 rows
	    }

	@Override
	public LimitUnit[] getOutputLimitType()
	    {
		return new LimitUnit[]{LimitUnit.rows, LimitUnit.rows};
	    }

	/* ************************* */
	/* HOW TO DELETE JOB RESULTS */
	/* ************************* */
	@Override
	public void deleteResults(ADQLExecutor<FireThornTapResult> job)
	throws TAPException
	    {

		if (job.getResult(FireThornMetaStuff.RESULT_ID) != null)
		    {
/*
 * LocalResult class specific - bad
			    (
			    (LocalResult)
			        job.getResult(
			            FirethornMetaStuff.RESULT_ID
			            )
			        ).getFile().delete();
 *
 */
			}
        }
	
	/* ******************** */
	/* UPLOAD CONFIGURATION */
	/* ******************** */
	@Override
	public boolean uploadEnabled()
	    {
		return false;
	    }

	@Override
	public String getUploadDirectory()
	    {
		return null;
	    }

	@Override
	public int[] getUploadLimit()
	    {
		return null;
	    }

	@Override
	public tap.ServiceConnection.LimitUnit[] getUploadLimitType()
	    {
		return null;
	    }
    }

