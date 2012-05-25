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
package uk.ac.roe.wfau.firethorn.tap.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import tap.ADQLExecutor;
import tap.TAPException;

import tap.formatter.OutputFormat;
//import tap.OutputFormat;

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

