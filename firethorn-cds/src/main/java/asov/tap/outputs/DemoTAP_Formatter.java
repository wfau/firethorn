package asov.tap.outputs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.sql.ResultSet;

import asov.DemoASOV;
import tap.ADQLExecutor;
import tap.TAPException;
import tap.OutputFormat;

import uws.job.LocalResult;
import uws.job.Result;

public abstract class DemoTAP_Formatter implements OutputFormat<ResultSet> {

	public final Result writeResult(ResultSet queryResult, ADQLExecutor<ResultSet> job) throws TAPException {
		return writeResult(queryResult, job, this);
	}

	public final static Result writeResult(ResultSet queryResult, ADQLExecutor<ResultSet> job, OutputFormat<ResultSet> formatter) throws TAPException {
		File resultFile = new File(DemoASOV.RESULT_DIR+File.separator+DemoASOV.RESULT_FILE_PREFIX+System.currentTimeMillis()+"."+formatter.getFileExtension());
		try{
			OutputStream output = new FileOutputStream(resultFile);

			// Write the result file:
			formatter.writeResult(queryResult, output, job);
			output.close();

			return new LocalResult(job, DemoASOV.RESULT_ID, resultFile);
		}catch(IOException ioe){
			throw new TAPException("An IO error occurs while creating the result file ("+resultFile+") of "+job.getJobId()+" !", ioe);
		}
	}

}
