package asov.tap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import adql.query.ADQLQuery;
import asov.DemoASOV;
import cds.savot.model.SavotTR;

import tap.TAPException;
import tap.db.DBConnection;
import tap.db.DBException;
import tap.metadata.TAPTable;

public class DemoTAP_DBConnection implements DBConnection<ResultSet> {

	private final Connection dbConn;

	public DemoTAP_DBConnection() throws TAPException {
		try {
			dbConn = DemoASOV.connectDB();
		} catch (SQLException e) {
			throw new TAPException("Can not connect to database, because: "+e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new TAPException("JDBC Driver not found !");
		}
	}

	@Override
	public void startTransaction() throws DBException {
		try {
			dbConn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			throw new DBException("Impossible to start transaction, because: "+e.getMessage());
		}
	}

	@Override
	public void cancelTransaction() throws DBException {
		try{
			dbConn.rollback();
		} catch (SQLException e) {
			throw new DBException("Impossible to cancel transaction, because: "+e.getMessage());
		}
	}

	@Override
	public void endTransaction() throws DBException {
		try{
			dbConn.commit();
		} catch (SQLException e) {
			throw new DBException("Impossible to commit transaction, because: "+e.getMessage());
		}
	}

	@Override
	public void close() throws DBException {
		try{
			dbConn.close();
		} catch (SQLException e) {
			throw new DBException("Impossible to close DB connection, because: "+e.getMessage());
		}
	}

	@Override
	public ResultSet executeQuery(String sql, ADQLQuery query) throws DBException {
		try{
			Statement stmt = dbConn.createStatement();
			return stmt.executeQuery(sql);
		}catch(SQLException se){
			throw new DBException("Can not execute the following SQL: \n"+sql+"\n. Because: "+se.getMessage());
		}
	}


	/* ************************************** */
	/* METHODS USED ONLY IF UPLOAD IS ENABLED */
	/* ************************************** */

	@Override
	public void createSchema(String arg0) throws DBException {
		// TODO ONLY IF upload is enabled !
	}

	@Override
	public void dropSchema(String arg0) throws DBException {
		// TODO ONLY IF upload is enabled !
	}

	@Override
	public void createTable(TAPTable table) throws DBException {
		// TODO ONLY IF upload is enabled !
	}

	@Override
	public void insertRow(SavotTR row, TAPTable table) throws DBException {
		// TODO ONLY IF upload is enabled !
	}

	@Override
	public void dropTable(TAPTable table) throws DBException {
		// TODO ONLY IF upload is enabled !
	}

}
