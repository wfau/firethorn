package tap.db;

import cds.savot.model.SavotTR;
import tap.metadata.TAPTable;

import adql.query.ADQLQuery;

/**
 * TODO
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 10/2011
 * 
 * @param <R>	Result type of the execution of a query (see {@link #executeQuery(String, ADQLQuery)}.
 */
public interface DBConnection<R> {

	public void startTransaction() throws DBException;

	public void cancelTransaction() throws DBException;

	public void endTransaction() throws DBException;

	public R executeQuery(final String sqlQuery, final ADQLQuery adqlQuery) throws DBException;

	public void createSchema(final String schemaName) throws DBException;

	public void dropSchema(final String schemaName) throws DBException;

	public void createTable(final TAPTable table) throws DBException;

	public void insertRow(final SavotTR row, final TAPTable table) throws DBException;

	public void dropTable(final TAPTable table) throws DBException;

	public void close() throws DBException;

}
