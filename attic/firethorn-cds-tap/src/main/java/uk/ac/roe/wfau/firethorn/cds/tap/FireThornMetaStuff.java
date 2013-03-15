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
package uk.ac.roe.wfau.firethorn.cds.tap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;

import tap.metadata.TAPColumn;
import tap.metadata.TAPSchema;
import tap.metadata.TAPTable;

import uws.job.AbstractJob;

import adql.db.DBTable;
import adql.db.DefaultDBColumn;
import adql.db.DefaultDBTable;

public class FireThornMetaStuff
    {

	public final static String DB_NAME  = "firethorndata"  ;
	public final static String DB_OWNER = "firethorn-user" ;
	public final static String DB_PASS  = "firethorn-pass" ;

	public final static String SERVER_NAME = "localhost";

	public final static String RESULT_ID  = "result";
	public final static String RESULT_DIR = "results";
	public final static String RESULT_FILE_PREFIX = RESULT_ID;

    public final static String HOME_PAGE = "http://localhost/home.html" ;

	public FireThornMetaStuff()
	    {
	    }

	/** Get the list of all available tables for the ADQL library (DefaultDBTable and DefaultDBColumn). */
	public static final ArrayList<DBTable> getDBTables()
	    {
		// Describe the available table:
		DefaultDBTable table = new DefaultDBTable("data");

		// Describe its columns:
		table.addColumn(new DefaultDBColumn("id", table));
		table.addColumn(new DefaultDBColumn("name", table));
		table.addColumn(new DefaultDBColumn("ra", table));
		table.addColumn(new DefaultDBColumn("dec", table));
		table.addColumn(new DefaultDBColumn("coord", table));
		table.addColumn(new DefaultDBColumn("type", table));

		// List all available tables:
		ArrayList<DBTable> tables = new ArrayList<DBTable>(1);
		tables.add(table);

		return tables;
	    }

	public static final TAPSchema getTAPSchema()
	    {
		// Describe the available table:
		TAPTable table = new TAPTable("data");

		// Describe its columns:
		table.addColumn(new TAPColumn("id", "Object ID"));
		table.addColumn(new TAPColumn("name", "Main object name"));
		table.addColumn(new TAPColumn("ra", "Right ascension", "deg", "pos.eq.ra;meta.main", ""));
		table.addColumn(new TAPColumn("dec", "Declination", "deg", "pos.eq.dec;meta.main", ""));
		table.addColumn(new TAPColumn("coord", "Center position", "spoint"));
		table.addColumn(new TAPColumn("type", "Object type (i.e. galaxy, star, ...)"));

		// List all available tables:
		TAPSchema schema = new TAPSchema("public");
		schema.addTable(table);

		return schema;
	    }

	/** Get the list of all allowed coordinate systems. */
	public final static ArrayList<String> getCoordinateSystems()
	    {
		ArrayList<String> coordSys = new ArrayList<String>(2);
		coordSys.add("ICRS");
		coordSys.add("ICRS BARYCENTER");
		return coordSys;
	    }

	/** Get the connection toward the database DB_NAME. */
	public static final Connection connectDB()
	throws SQLException, ClassNotFoundException
	    {
		String url = "jdbc:postgresql://127.0.0.1/"+DB_NAME;
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(
		    url,
		    DB_OWNER,
		    DB_PASS
		    );
	    }

	/** Format the given ResultSet in CSV, write it in a file and build the corresponding Result object. */
/*
	public static final File buildResult(final AbstractJob job, final ResultSet rs)
	throws IOException, SQLException
	    {
		char sep = ',';
		File resultFile = new File(RESULT_DIR+File.separator+RESULT_FILE_PREFIX+System.currentTimeMillis()+".csv");
		FileWriter output = new FileWriter(resultFile);

		// Write header:
		ResultSetMetaData meta = rs.getMetaData();
		int nbColumns = meta.getColumnCount();
		if (nbColumns == 0)
		    {
			return null;
			}

		for(int i=1; i<nbColumns; i++)
		    {
			output.write(meta.getColumnName(i));
			output.write(sep);
		    }
		output.write(meta.getColumnName(nbColumns));
		output.write('\n');

		// Write data:
		int nbRows = 0;
		while(rs.next())
		    {
			for(int i=1; i<=nbColumns; i++)
			    {
				Object obj = rs.getObject(i);
				if (obj != null)
				    {
					if (obj instanceof String)
					    {
						output.write('"');
						output.write(obj.toString().replaceAll("\"", "'"));
						output.write('"');
						}
					else {
						output.write(obj.toString());
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
		output.close();

		return resultFile;
    	}
 */
    }

