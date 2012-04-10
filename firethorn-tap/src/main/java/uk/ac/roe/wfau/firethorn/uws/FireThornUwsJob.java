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
package uk.ac.roe.wfau.firethorn.uws;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import adql.db.DBChecker;
import adql.parser.ADQLParser;
import adql.parser.ParseException;

import adql.query.ADQLQuery;

import adql.search.IReplaceHandler;

import adql.translator.ADQLTranslator;
import adql.translator.PgSphereTranslator;
import adql.translator.TranslationException;

//import asov.adql.ReplacePointHandler;

import uws.UWSException;

import uws.job.AbstractJob;
import uws.job.LocalResult;
import uws.job.Result;

import uk.ac.roe.wfau.firethorn.tap.FireThornMetaStuff;

public class FireThornUwsJob
extends AbstractJob
    {

	private static final long serialVersionUID = 1L;

	private String adql = null;

	public FireThornUwsJob(Map<String, String> param)
    throws UWSException
        {
		super(
		    param
		    );
	    }

	@Override
	protected String generateJobId()
	throws UWSException
	    {
		String ident = "firethorn-" + System.currentTimeMillis() ;
// Check for duplicates ...
		return ident ;
	    }

	@Override
	protected boolean loadAdditionalParams()
    throws UWSException
	    {
	    //
	    // Set result to false if a param is missing or is incorrect.
        boolean result = true ;

		// Get the ADQL query (at the initialization but also, each time this parameter is changed):
		if (additionalParameters.containsKey("query"))
		    {
			adql = additionalParameters.get("query");
            result &= true ;
		    }
        else {
            result &= false ;
            }

        // Get our additional params (beyond TAP spec.).

		return result ;
	    }

	@Override
	protected void jobWork()
    throws UWSException, InterruptedException
	    {
		try {
			// 1. Build the ADQL parser:
            ADQLParser parser = new ADQLParser();
			parser.setQueryChecker(
			    new DBChecker(
			        FireThornMetaStuff.getDBTables()
			        )
		        );
			parser.setCoordinateSystems(
			    FireThornMetaStuff.getCoordinateSystems()
			    );

			// 2. Parse the query:
			ADQLQuery query = parser.parseQuery(
			    adql
			    );
/*
			// 2bis. Manipulate the query: point('...', ra, dec) => coord:
			IReplaceHandler replacer = new ReplacePointHandler();
			replacer.searchAndReplace(
			    query
			    );
*/
			// 3. Translate into SQL:
			ADQLTranslator translator = new PgSphereTranslator();
			String sql = translator.translate(
			    query
			    );

			// 4. Execute on the database:
			Connection conn = FireThornMetaStuff.connectDB();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
			    sql
			    );

			// 5. Build and set the result:
/*
			File resultFile = FireThornMetaStuff.buildResult(
			    this,
			    rs
			    );
 */
			addResult(
			    new FireThornUwsResult(
			        this
			        )
			    );

/*
 *
			addResult(
			    (Result) new LocalResult(
			        this,
			        FirethornMetaStuff.RESULT_ID,
			        resultFile
			        )
		        );
 *
 */

	        }

	    catch(ParseException pe)
	        {
			throw new UWSException(HttpServletResponse.SC_BAD_REQUEST, pe, "Incorrect ADQL syntax between (l."+pe.getBeginLine()+",c."+pe.getBeginColumn()+") and (l."+pe.getEndLine()+",c."+pe.getEndColumn()+")");
		    }
	    catch (TranslationException e)
	        {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "Error when translating ADQL to SQL");
		    }
	    catch (SQLException e)
	        {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "SQL Error");
		    }
	    catch (ClassNotFoundException e)
	        {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "JDBC Driver not found");
		    }
/*
	    catch (IOException e)
	        {
			throw new UWSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "Can not write the result file");
		    }
 */
	    }
    }

