package tap;

/*
 * This file is part of TAPLibrary.
 * 
 * TAPLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TAPLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with TAPLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2011 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import java.io.IOException;
import java.io.OutputStream;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import adql.db.DBColumn;
import adql.parser.ADQLParser;
import adql.parser.ParseException;

import adql.query.ADQLQuery;

import adql.translator.ADQLTranslator;
import adql.translator.TranslationException;

import tap.ServiceConnection.LogType;
import tap.db.DBConnection;
import tap.db.DBException;
import tap.formatter.OutputFormat;
import tap.metadata.TAPSchema;
import tap.metadata.TAPTable;
import tap.upload.TableLoader;
import uws.UWSException;

import uws.job.AbstractJob;
import uws.job.ErrorSummary;
import uws.job.ExecutionPhase;
import uws.job.JobOwner;
import uws.job.Result;

public class ADQLExecutor<R> extends AbstractJob {

	private static final long serialVersionUID = 1L;

	protected final ServiceConnection<R> service;

	protected ExecutionProgression progression;

	protected String adqlQuery;
	protected String sqlQuery = null;
	protected String format;
	protected int maxRec;
	protected String upload;
	protected TableLoader[] tablesToUpload = null;
	protected TAPSchema uploadSchema = null;

	protected DBColumn[] selectedColumns = null;

	private Object execLock = new Object();
	private DBConnection<R> dbConn = null;


	public ADQLExecutor(final ServiceConnection<R> service, final Map<String, String> params, final JobOwner owner) throws UWSException, TAPException {
		super(owner, params);
		progression = ExecutionProgression.PENDING;
		this.service = service;
	}

	public ADQLExecutor(final ServiceConnection<R> service, String jobId, String jobName, JobOwner owner,
			ExecutionPhase phase, Date startTime, Date endTime,
			long maxDuration, Date destructTime, List<Result> results,
			ErrorSummary errors, Map<String, String> additionalParams) throws UWSException {
		super(jobId, jobName, owner, phase, startTime, endTime, maxDuration,
				destructTime, results, errors, additionalParams);
		this.service = service;
	}

	public void loadTAPParams(TAPParameters params) {
		adqlQuery = params.query;
		additionalParameters.put(TAPParameters.PARAM_QUERY, adqlQuery);

		format = (params.format == null)?"application/x-votable+xml":params.format;
		additionalParameters.put(TAPParameters.PARAM_FORMAT, format);

		maxRec = params.maxrec;
		additionalParameters.put(TAPParameters.PARAM_MAX_REC, maxRec+"");

		upload = params.upload;
		tablesToUpload = params.tablesToUpload;
		additionalParameters.put(TAPParameters.PARAM_UPLOAD, upload);
	}

	public String getUploadParam(){
		return upload;
	}

	public boolean hasUploadedTables(){
		return (uploadSchema != null) && (uploadSchema.getNbTables() > 0);
	}

	/**
	 * @return The service.
	 */
	public final ServiceConnection<R> getService() {
		return service;
	}

	/**
	 * @return The progression.
	 */
	public final ExecutionProgression getProgression() {
		return progression;
	}

	protected final void setProgression(ExecutionProgression prog){
		if (prog != null){
			synchronized(execLock){
				progression = prog;
				additionalParameters.put("progression", progression.toString());
			}
		}
	}

	/**
	 * @return The ADQL query given by the user.
	 */
	public final String getADQLQuery() {
		return adqlQuery;
	}

	/**
	 * @return The SQL query as it has been translated from the given ADQL query.
	 */
	public final String getSQLQuery(){
		return sqlQuery;
	}

	/**
	 * @return The format.
	 */
	public final String getFormat() {
		return format;
	}

	/**
	 * @return The maxRec.
	 */
	public final int getMaxRec() {
		return maxRec;
	}

	/**
	 * @return The columns by the executed ADQL query.
	 */
	public final DBColumn[] getSelectedColumns(){
		return selectedColumns;
	}

	protected final DBConnection<R> getDBConnection() throws TAPException {
		return (dbConn != null) ? dbConn : (dbConn=service.getFactory().createDBConnection());
	}

	protected final void closeDBConnection() throws TAPException {
		if (dbConn != null){
			dbConn.close();
			dbConn = null;
		}
	}

	private final void uploadTables() throws TAPException {
		if (tablesToUpload.length > 0){
			service.log(LogType.INFO, "Job "+getJobId()+" - Loading uploaded tables ("+tablesToUpload.length+")...");
			uploadSchema = service.getFactory().createUploader().upload(tablesToUpload);
			TAPParameters.deleteUploadedTables(tablesToUpload);
		}
	}

	private final R executeADQL() throws ParseException, InterruptedException, TranslationException, SQLException, TAPException {
		setProgression(ExecutionProgression.PARSING);
		ADQLQuery adql = parseADQL();

		selectedColumns = adql.getResultingColumns();

		final int limit = adql.getSelect().getLimit();
		if (maxRec > -1){
			if (limit <= -1 || limit > maxRec)
				adql.getSelect().setLimit(maxRec+1);
		}

		setProgression(ExecutionProgression.TRANSLATING);
		sqlQuery = translateADQL(adql);

		setProgression(ExecutionProgression.EXECUTING_SQL);
		return executeQuery(sqlQuery, adql);
	}

	public final void startSync(OutputStream output) throws TAPException, IOException {
		synchronized(execLock){
			if (tablesToUpload != null){
				try{
					setProgression(ExecutionProgression.UPLOADING);
					uploadTables();
				}catch(TAPException te){
					throw new TAPException(te, adqlQuery, ExecutionProgression.UPLOADING);
				}
			}

			try {
				R queryResult = executeADQL();
				if (queryResult == null){
					service.log(LogType.INFO, "Job "+getJobId()+" - ABORTED");
					return;
				}

				setProgression(ExecutionProgression.WRITING_RESULT);
				writeResult(queryResult, output);

				service.log(LogType.INFO, "Job "+getJobId()+" - COMPLETED");
				setProgression(ExecutionProgression.FINISHED);
			} catch (ParseException e) {
				throw new TAPException(e, adqlQuery, ExecutionProgression.PARSING);
			} catch (InterruptedException e) {
				throw new TAPException(e, adqlQuery, progression);
			} catch (TranslationException e) {
				throw new TAPException(e, adqlQuery, ExecutionProgression.TRANSLATING);
			} catch (SQLException e) {
				throw new TAPException(e, adqlQuery, ExecutionProgression.EXECUTING_SQL);
			} finally {
				try {
					dropUploadedTables();
				} catch (TAPException e) { service.log(LogType.ERROR, "Job "+getJobId()+" - Can not drop uploaded tables: "+e.getMessage()); }
				try {
					closeDBConnection();
				} catch (TAPException e) { service.log(LogType.ERROR, "Job "+getJobId()+" - Can not close the DB connection: "+e.getMessage()); }
			}
		}
	}

	@Override
	protected synchronized final void jobWork() throws UWSException, InterruptedException {
		synchronized(execLock){
			try {
				if (tablesToUpload != null){
					setProgression(ExecutionProgression.UPLOADING);
					uploadTables();
				}

				R queryResult = executeADQL();
				if (queryResult == null)
					throw new InterruptedException();

				setProgression(ExecutionProgression.WRITING_RESULT);
				Result result = createUWSResult(queryResult);
				if (queryResult != null && result != null)
					addResult(result);

				service.log(LogType.INFO, "Job "+getJobId()+" - COMPLETED");
				setProgression(ExecutionProgression.FINISHED);
			} catch (ParseException e) {
				throw new UWSException(e);
			} catch (TranslationException e) {
				throw new UWSException(e);
			} catch (SQLException e) {
				throw new UWSException(e);
			} catch (TAPException e){
				throw new UWSException(e);
			}catch(NullPointerException npe){
				npe.printStackTrace();
				throw npe;
			} finally {
				try {
					dropUploadedTables();
				} catch (TAPException e) { service.log(LogType.ERROR, "Job "+getJobId()+" - Can not drop uploaded tables: "+e.getMessage()); }
				try {
					closeDBConnection();
				} catch (TAPException e) { service.log(LogType.ERROR, "Job "+getJobId()+" - Can not close the DB connection: "+e.getMessage()); }
			}
		}
	}

	protected ADQLQuery parseADQL() throws ParseException, InterruptedException, TAPException {
		ADQLParser parser = new ADQLParser(service.getFactory().createQueryChecker(uploadSchema));
		parser.setCoordinateSystems(service.getCoordinateSystems());
		parser.setDebug(false);
		service.log(LogType.INFO, "Job "+getJobId()+" - 1/5 Parsing ADQL....");
		return parser.parseQuery(adqlQuery);
	}

	protected String translateADQL(ADQLQuery query) throws TranslationException, InterruptedException, TAPException {
		ADQLTranslator translator = service.getFactory().createADQLTranslator();
		service.log(LogType.INFO, "Job "+getJobId()+" - 2/5 Translating ADQL...");
		return translator.translate(query);
	}

	protected R executeQuery(String sql, ADQLQuery adql) throws SQLException, InterruptedException, TAPException {
		service.log(LogType.INFO, "Job "+getJobId()+" - 3/5 Creating DBConnection....");
		DBConnection<R> dbConn = getDBConnection();
		service.log(LogType.INFO, "Job "+getJobId()+" - 4/5 Executing query...\n"+sql);
		final long startTime = System.currentTimeMillis();
		R result = dbConn.executeQuery(sql, adql);
		if (result == null)
			service.log(LogType.INFO, "Job "+getJobId()+" - QUERY ABORTED AFTER "+(System.currentTimeMillis()-startTime)+" MS !");
		else
			service.log(LogType.INFO, "Job "+getJobId()+" - QUERY SUCCESFULLY EXECUTED IN "+(System.currentTimeMillis()-startTime)+" MS !");
		return result;
	}

	protected OutputFormat<R> getFormatter() throws TAPException {
		// Search for the corresponding formatter:
		OutputFormat<R> formatter = service.getOutputFormat((format == null)?"votable":format);
		if (format != null && formatter == null)
			formatter = service.getOutputFormat("votable");

		// Format the result:
		if (formatter == null)
			throw new TAPException("Impossible to format the query result: no formatter has been found for the given MIME type \""+format+"\" and for the default MIME type \"votable\" (short form) !");

		return formatter;
	}

	protected void writeResult(R queryResult, OutputStream output) throws InterruptedException, TAPException {
		service.log(LogType.INFO, "Job "+getJobId()+" - 5/5 Writing result file...");
		getFormatter().writeResult(queryResult, output, this);
	}

	protected Result createUWSResult(R queryResult) throws InterruptedException, TAPException {
		return getFormatter().writeResult(queryResult, this);
	}

	protected void dropUploadedTables() throws TAPException {
		if (uploadSchema != null){
			// Drop all uploaded tables:
			DBConnection<R> dbConn = getDBConnection();
			for(TAPTable t : uploadSchema){
				try{
					dbConn.dropTable(t);
				}catch(DBException dbe){
					service.log(LogType.ERROR, "Job "+getJobId()+" - Can not drop the table \""+t.getDBName()+"\" (in adql \""+t.getADQLName()+"\") from the database: "+dbe.getMessage());
				}
			}
			closeDBConnection();
		}
	}

	@Override
	protected void stop() {
		if (!isStopped()){
			try {
				stopping = true;
				closeDBConnection();
				super.stop();
			} catch (TAPException e) {
				service.log(LogType.ERROR, "Impossible to cancel the query execution !");
				service.log(e);
				return;
			}
		}
	}

	protected boolean deleteResultFiles(){
		try{
			service.deleteResults(this);
			return true;
		}catch(TAPException ex){
			service.log(LogType.ERROR, "Job "+getJobId()+" - Can't delete results files: "+ex.getMessage());
			return false;
		}
	}

	@Override
	public void clearResources() {
		super.clearResources();
		deleteResultFiles();
	}


}
