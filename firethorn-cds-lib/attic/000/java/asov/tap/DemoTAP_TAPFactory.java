package asov.tap;

import java.sql.ResultSet;

import tap.AbstractTAPFactory;
import tap.ServiceConnection;
import tap.TAPException;
import tap.db.DBConnection;
import adql.translator.ADQLTranslator;
import adql.translator.PgSphereTranslator;

public class DemoTAP_TAPFactory extends AbstractTAPFactory<ResultSet> {

	protected DemoTAP_TAPFactory(ServiceConnection<ResultSet> service) throws NullPointerException {
		super(service);
	}

	@Override
	public ADQLTranslator createADQLTranslator() throws TAPException {
		return new PgSphereTranslator();
	}

	@Override
	public DBConnection<ResultSet> createDBConnection() throws TAPException {
		return new DemoTAP_DBConnection();
	}

}
