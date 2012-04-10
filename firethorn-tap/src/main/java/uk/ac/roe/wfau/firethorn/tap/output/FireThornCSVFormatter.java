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

import java.io.OutputStream;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import tap.ADQLExecutor;
import tap.TAPException;

import uk.ac.roe.wfau.firethorn.tap.FireThornTapResult;

public class FireThornCSVFormatter
extends FireThornOutputFormat
    {

	public static final char COMMA_SEPARATOR = ',';
	public static final char SEMI_COLON_SEPARATOR = ';';
	public static final char TAB_SEPARATOR = '\t';

	protected final String separator;
	protected final boolean delimitStr;

	public FireThornCSVFormatter(char colSeparator)
	    {
		this(colSeparator, true);
	    }

	public FireThornCSVFormatter(char colSeparator, boolean delimitStrings)
	    {
		separator = ""+colSeparator;
		delimitStr = delimitStrings;
	    }

	public FireThornCSVFormatter(String colSeparator)
	    {
		this(colSeparator, true);
	    }

	public FireThornCSVFormatter(String colSeparator, boolean delimitStrings)
	    {
		separator = (colSeparator==null)?(""+COMMA_SEPARATOR):colSeparator;
		delimitStr = delimitStrings;
	    }

	public String getMimeType()
	    {
		switch(separator.charAt(0))
		    {
		    case COMMA_SEPARATOR:
		    case SEMI_COLON_SEPARATOR:
			    return "text/csv";

		    case TAB_SEPARATOR:
			    return "text/tsv";

		    default:
			    return "text/plain";
		    }
	    }

	public String getShortMimeType()
	    {
		switch(separator.charAt(0))
		    {
		    case COMMA_SEPARATOR:
		    case SEMI_COLON_SEPARATOR:
			    return "csv";

		    case TAB_SEPARATOR:
			    return "tsv";

		    default:
			    return "text";
            }
        }

	public String getDescription()
	    {
	    return null;
	    }

	public String getFileExtension()
	    {
		switch(separator.charAt(0))
		    {
		    case COMMA_SEPARATOR:
		    case SEMI_COLON_SEPARATOR:
			    return "csv";

		    case TAB_SEPARATOR:
			    return "tsv";

		    default:
			    return "txt";
            }
        }

	public void writeResult(final FireThornTapResult result, final OutputStream output, final ADQLExecutor<FireThornTapResult> job)
	throws TAPException
	    {
		try{
		    ResultSet queryResult = result.results();

			byte[] sep = separator.getBytes();

			final long startTime = System.currentTimeMillis();

			// Write header:
			ResultSetMetaData meta = queryResult.getMetaData();
			int nbColumns = meta.getColumnCount();
			if (nbColumns == 0)
			    {
				return;
				}

			for(int i=1; i<nbColumns; i++)
			    {
				output.write(meta.getColumnName(i).getBytes());
				output.write(sep);
			    }
			output.write(meta.getColumnName(nbColumns).getBytes());
			output.write('\n');

			// Write data:
			int nbRows = 0;
			while(queryResult.next())
			    {
				if (nbRows >= job.getMaxRec())	// that's to say: OVERFLOW !
				    {
					break;
					}

				for(int i=1; i<=nbColumns; i++)
				    {
					Object obj = queryResult.getObject(i);
					if (obj != null)
					    {
						if (obj instanceof String)
						    {
							output.write('"');
							output.write(obj.toString().replaceAll("\"", "'").getBytes());
							output.write('"');
						    }
					    else {
							output.write(obj.toString().getBytes());
							}
    					}
					if (i != nbColumns)
					    {
						output.write(sep);
						}
				    }
				output.write('\n');
				nbRows++;
			    }

			System.out.println("Job "+job.getJobId()+" - Result formatted (in SV["+separator+"] ; "+nbRows+" rows ; "+nbColumns+" columns) in "+(System.currentTimeMillis()-startTime)+" ms !");

		    }
	    catch(Exception ex)
	        {
			System.err.println("While formatting in (T/C)SV !");
			ex.printStackTrace(System.err);
		    }
	    }
    }

