package asov.tap;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import asov.DemoASOV;
import asov.tap.outputs.DemoTAP_CSVFormatter;
import asov.tap.outputs.DemoTAP_VOTableFormatter;

import tap.ADQLExecutor;
import tap.ServiceConnection;
import tap.TAPException;
import tap.TAPFactory;

import tap.OutputFormat;

import tap.metadata.TAPMetadata;
import uws.UWSException;

import uws.job.LocalResult;

import uws.service.UWSUrl;
import uws.service.UserIdentifier;

public class DemoTAP_ServiceConnection implements ServiceConnection<ResultSet> {

	private final ArrayList<String> coordSys;

	private final TAPMetadata meta;
	private final OutputFormat<ResultSet>[] formats;
	private final UserIdentifier identifier;

	@SuppressWarnings("unchecked")
	public DemoTAP_ServiceConnection(){
		// List all schemas, tables and columns available:
		meta = new TAPMetadata();
		meta.addSchema(DemoASOV.getTAPSchema());

		// List all available outputs (VOTable & CSV):
		formats = new OutputFormat[2];
		formats[0] = new DemoTAP_VOTableFormatter(this);
		formats[1] = new DemoTAP_CSVFormatter(",");

		// List all allowed coordinate systems:
		coordSys = new ArrayList<String>(2);
		coordSys.add("ICRS");
		coordSys.add("ICRS BARYCENTER");

		// Create a way to identify users (by IP address):
		identifier = new UserIdentifier() {
			private static final long serialVersionUID = 1L;
			@Override
			public String extractUserId(UWSUrl urlInterpreter, HttpServletRequest request) throws UWSException {
				return request.getRemoteAddr();
			}
		};
	}

	/* *************************** */
	/* GENERAL SERVICE DESCRIPTION */
	/* *************************** */
	@Override
	public String getProviderName() { return "ASOV"; }

	@Override
	public String getProviderDescription() { return "Demo TAP for ASOV."; }
	
	@Override
	public void log(tap.ServiceConnection.LogType type, String msg) {
		switch(type){
		case INFO:
			System.out.println("INFO: "+msg); break;
		default:
			System.err.println(type+": "+msg);
		}
	}

//ZRQ
	@Override
	public void log(Throwable ouch) {
			System.err.println("exception" + " : " + ouch.getMessage());
	}


	/* ******************** */
	/* SERVICE AVAILABILITY */
	/* ******************** */
	@Override
	public boolean isAvailable() { return true; }

	@Override
	public String getAvailability() { return "Demo TAP for ASOV available !"; }

	/* ************************ */
	/* AVAILABLE TABLES/COLUMNS */
	/* ************************ */
	@Override
	public TAPMetadata getTAPMetadata() { return meta; }

	/* ****************** */
	/* COORDINATE SYSTEMS */
	/* ****************** */
	@Override
	public Collection<String> getCoordinateSystems() { return coordSys; }

	/* ******************* */
	/* USER IDENTIFICATION */
	/* ******************* */
	@Override
	public UserIdentifier getUserIdentifier() { return identifier; }

	/* ********************* */
	/* SERVICE CONFIGURATION */
	/* ********************* */
	@Override
	public long[] getExecutionDuration() {
		return new long[]{3600, 7200};		// default = 1 hour , max = 2 hours
	}

	@Override
	public int[] getRetentionPeriod() {
		return new int[]{604800, 604800};	// default = max = 7 days
	}

	@Override
	public TAPFactory<ResultSet> getFactory() {
		return new DemoTAP_TAPFactory(this);
	}
	
	/* ******************** */
	/* OUTPUT CONFIGURATION */
	/* ******************** */
	@Override
	public OutputFormat<ResultSet> getOutputFormat(String format) {
		for(OutputFormat<ResultSet> f : formats){
			if (f.getMimeType().equalsIgnoreCase(format) || f.getShortMimeType().equalsIgnoreCase(format))
				return f;
		}
		return null;
	}

	@Override
	public OutputFormat<ResultSet>[] getOutputFormats() {
		return formats;
	}

	@Override
	public int[] getOutputLimit() {
		return new int[]{2000, 1000000};	// default = 2000 rows, max = 1000000 rows
	}

	@Override
	public LimitUnit[] getOutputLimitType() {
		return new LimitUnit[]{LimitUnit.rows, LimitUnit.rows};
	}

	/* ************************* */
	/* HOW TO DELETE JOB RESULTS */
	/* ************************* */
	@Override
	public void deleteResults(ADQLExecutor<ResultSet> job) throws TAPException {
		if (job.getResult(DemoASOV.RESULT_ID) != null)
			((LocalResult)job.getResult(DemoASOV.RESULT_ID)).getFile().delete();
	}
	
	/* ******************** */
	/* UPLOAD CONFIGURATION */
	/* ******************** */
	@Override
	public boolean uploadEnabled() {
		return false;
	}

	@Override
	public String getUploadDirectory() {
		return null;
	}

	@Override
	public int[] getUploadLimit() {
		return null;
	}

	@Override
	public tap.ServiceConnection.LimitUnit[] getUploadLimitType() {
		return null;
	}

}
