package tap;

import java.util.Map;

import tap.db.DBConnection;
import tap.metadata.TAPSchema;
import tap.upload.Uploader;

import uws.UWSException;
import uws.job.JobOwner;

import adql.parser.QueryChecker;

import adql.translator.ADQLTranslator;

public interface TAPFactory<R> {

	public ADQLExecutor<R> createExecutor(Map<String, String> params, JobOwner owner) throws TAPException, UWSException;

	public QueryChecker createQueryChecker(TAPSchema uploadSchema) throws TAPException;

	public ADQLTranslator createADQLTranslator() throws TAPException;

	public DBConnection<R> createDBConnection() throws TAPException;

	public Uploader createUploader() throws TAPException;

}
