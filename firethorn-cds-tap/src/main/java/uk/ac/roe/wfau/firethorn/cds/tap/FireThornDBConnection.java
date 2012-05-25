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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import adql.query.ADQLQuery;
import cds.savot.model.SavotTR;

import tap.TAPException;
import tap.db.DBConnection;
import tap.db.DBException;
import tap.metadata.TAPTable;

public class FireThornDBConnection
implements DBConnection<FireThornTapResult>
    {

	private final Connection dbConn;

	public FireThornDBConnection()
    throws TAPException
	    {
	    try {
		    dbConn = FireThornMetaStuff.connectDB();
	        }
        catch (SQLException e)
            {
			throw new TAPException("Can not connect to database, because: "+e.getMessage());
		    }
	    catch (ClassNotFoundException e)
	        {
			throw new TAPException("JDBC Driver not found !");
		    }
	    }

	@Override
	public void startTransaction()
    throws DBException
        {
		try {
		    dbConn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	        }
        catch (SQLException e)
            {
			throw new DBException("Impossible to start transaction, because: "+e.getMessage());
		    }
	    }

	@Override
	public void cancelTransaction()
    throws DBException
	    {
		try {
	        dbConn.rollback();
		    }
	    catch (SQLException e)
	        {
			throw new DBException("Impossible to cancel transaction, because: "+e.getMessage());
		    }
	    }

	@Override
	public void endTransaction()
    throws DBException
	    {
		try {
			dbConn.commit();
		    }
	    catch (SQLException e)
	        {
			throw new DBException("Impossible to commit transaction, because: "+e.getMessage());
		    }
	    }

	@Override
	public void close()
    throws DBException
	    {
		try {
			dbConn.close();
		    }
	    catch (SQLException e)
	        {
			throw new DBException("Impossible to close DB connection, because: "+e.getMessage());
		    }
	    }

	@Override
	public FireThornTapResult executeQuery(String sql, ADQLQuery query)
	throws DBException
	    {
		try{
			return new FireThornTapResult(
			    dbConn.createStatement().executeQuery(
			        sql
			        )
			    );
		    }
	    catch(SQLException se)
	        {
			throw new DBException("Can not execute the following SQL: \n"+sql+"\n. Because: "+se.getMessage());
		    }
	    }


	/* ************************************** */
	/* METHODS USED ONLY IF UPLOAD IS ENABLED */
	/* ************************************** */

	@Override
	public void createSchema(String arg0)
	throws DBException
	    {
		// TODO ONLY IF upload is enabled !
	    }

	@Override
	public void dropSchema(String arg0)
	throws DBException
	    {
		// TODO ONLY IF upload is enabled !
	    }

	@Override
	public void createTable(TAPTable table)
    throws DBException
	    {
		// TODO ONLY IF upload is enabled !
    	}

	@Override
	public void insertRow(SavotTR row, TAPTable table)
	throws DBException
	    {
		// TODO ONLY IF upload is enabled !
	    }

	@Override
	public void dropTable(TAPTable table)
	throws DBException
	    {
		// TODO ONLY IF upload is enabled !
	    }
    }

