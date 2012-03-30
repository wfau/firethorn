package uk.ac.roe.wfau.firethorn.tap;

import tap.AbstractTAPFactory;
import tap.ServiceConnection;
import tap.TAPException;
import tap.db.DBConnection;

import adql.translator.ADQLTranslator;
import adql.translator.PgSphereTranslator;

public class FireThornDBFactory
extends AbstractTAPFactory<FireThornTapResult>
    {

	protected FireThornDBFactory(ServiceConnection<FireThornTapResult> service)
	    {
		super(service);
	    }

	@Override
	public ADQLTranslator createADQLTranslator()
	throws TAPException
	    {
		return new PgSphereTranslator();
	    }

	@Override
	public DBConnection<FireThornTapResult> createDBConnection()
    throws TAPException
	    {
		return new FireThornDBConnection();
        }
    }

