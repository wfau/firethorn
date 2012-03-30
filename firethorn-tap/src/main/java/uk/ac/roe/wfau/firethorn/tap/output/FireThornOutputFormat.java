package uk.ac.roe.wfau.firethorn.tap.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import tap.ADQLExecutor;
import tap.TAPException;

//import tap.formatter.OutputFormat;
import tap.OutputFormat;

import uws.job.Result;

import uk.ac.roe.wfau.firethorn.tap.FireThornTapResult;
import uk.ac.roe.wfau.firethorn.tap.FireThornMetaStuff;

import uk.ac.roe.wfau.firethorn.uws.FireThornUwsResult;

public abstract class FireThornOutputFormat
implements OutputFormat<FireThornTapResult>
    {

	public final Result writeResult(FireThornTapResult result, ADQLExecutor<FireThornTapResult> job)
    throws TAPException
        {
		return writeResult(result, job, this);
	    }

	public final static Result writeResult(FireThornTapResult result, ADQLExecutor<FireThornTapResult> job, FireThornOutputFormat formatter)
	throws TAPException
	    {
		File resultFile = new File(
		    FireThornMetaStuff.RESULT_DIR +
		    File.separator +
		    FireThornMetaStuff.RESULT_FILE_PREFIX +
		    System.currentTimeMillis() +
		    "." +
		    formatter.getFileExtension()
		    );
		try{
			OutputStream output = new FileOutputStream(resultFile);

			// Write the result file:
			formatter.writeResult(result, output, job);
			output.close();

			return new FireThornUwsResult(
			    job
			    );
/*
			return new LocalResult(
			    job,
			    FirethornMetaStuff.RESULT_ID,
			    resultFile
			    );
 */
		    }
	    catch(IOException ioe)
	        {
			throw new TAPException("An IO error occurs while creating the result file ("+resultFile+") of "+job.getJobId()+" !", ioe);
		    }
	    }
    }

