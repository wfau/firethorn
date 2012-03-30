package uk.ac.roe.wfau.firethorn.uws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import uws.job.Result;
import uws.job.AbstractJob;
import uws.UWSException;

public class FireThornUwsResult
extends Result
    {

    public FireThornUwsResult(AbstractJob job)
        {
        super("FireThornUwsResult");
		href = getDefaultUrl(id, job);
		mimeType = "firethorn-mime";
        }

	@Override
	public void writeContent(HttpServletResponse response)
    throws UWSException, IOException
        {

		//
		// Set the HTTP content type:
		if (mimeType != null)
		    {
			response.setContentType(mimeType);
			}

//
// Get data from user's database table.
// Write to HTTP response.

		ServletOutputStream output = response.getOutputStream();

        //
        // ....
        //

        }

    }
