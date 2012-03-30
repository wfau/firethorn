package tap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import tap.metadata.TAPMetadata;
import tap.metadata.TAPSchema;
import tap.metadata.TAPTable;
import tap.upload.Uploader;
import uws.UWSException;
import uws.job.JobOwner;

import adql.db.DBChecker;
import adql.db.DBTable;
import adql.parser.QueryChecker;

public abstract class AbstractTAPFactory<R> implements TAPFactory<R> {

	protected final ServiceConnection<R> service;

	protected AbstractTAPFactory(ServiceConnection<R> service) throws NullPointerException {
		if (service == null)
			throw new NullPointerException("Can not create a TAPFactory without a ServiceConnection instance !");

		this.service = service;
	}

	@Override
	public ADQLExecutor<R> createExecutor(Map<String, String> params, JobOwner owner) throws TAPException, UWSException {
		return new ADQLExecutor<R>(service, params, owner);
	}

	@Override
	public QueryChecker createQueryChecker(TAPSchema uploadSchema) throws TAPException {
		TAPMetadata meta = service.getTAPMetadata();
		ArrayList<DBTable> tables = new ArrayList<DBTable>(meta.getNbTables());
		Iterator<TAPTable> it = meta.getTables();
		while(it.hasNext())
			tables.add(it.next());
		if (uploadSchema != null){
			for(TAPTable table : uploadSchema)
				tables.add(table);
		}
		return new DBChecker(tables);
	}

	public Uploader createUploader() throws TAPException {
		return new Uploader(service);
	}

}
