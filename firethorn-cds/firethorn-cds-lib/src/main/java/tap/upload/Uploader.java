package tap.upload;

import java.io.IOException;
import java.io.InputStream;

import com.oreilly.servlet.multipart.ExceededSizeException;

import cds.savot.model.DataBinaryReader;
import cds.savot.model.FieldSet;
import cds.savot.model.SavotBinary;
import cds.savot.model.SavotField;
import cds.savot.model.SavotResource;
import cds.savot.model.SavotTR;
import cds.savot.model.SavotTableData;
import cds.savot.model.TRSet;

import cds.savot.pull.SavotPullEngine;
import cds.savot.pull.SavotPullParser;

import tap.ServiceConnection;
import tap.TAPException;
import tap.ServiceConnection.LimitUnit;
import tap.ServiceConnection.LogType;
import tap.db.DBConnection;
import tap.db.DBException;

import tap.metadata.TAPSchema;
import tap.metadata.TAPTable;
import tap.metadata.TAPTypes;
import tap.metadata.VotType;

public class Uploader {

	protected final ServiceConnection<?> service;
	protected final DBConnection<?> dbConn;
	protected final int nbRowsLimit;
	protected final int nbBytesLimit;

	protected int nbRows = 0;

	public Uploader(final ServiceConnection<?> service) throws TAPException {
		if (service == null)
			throw new NullPointerException("The given ServiceConnection is NULL !");

		this.service = service;

		dbConn = service.getFactory().createDBConnection();
		if (dbConn == null)
			throw new NullPointerException("The given DBConnection is NULL !");

		if (service.uploadEnabled()){
			if (service.getUploadLimitType()[1] == LimitUnit.rows){
				nbRowsLimit = ((service.getUploadLimit()[1] > 0) ? service.getUploadLimit()[1] : -1);
				nbBytesLimit = -1;
			}else{
				nbBytesLimit = ((service.getUploadLimit()[1] > 0) ? service.getUploadLimit()[1] : -1);
				nbRowsLimit = -1;
			}
		}else{
			nbRowsLimit = -1;
			nbBytesLimit = -1;
		}
	}

	public TAPSchema upload(final TableLoader[] loaders) throws TAPException {
		// Begin a DB transaction:
		dbConn.startTransaction();

		TAPSchema uploadSchema = new TAPSchema("TAP_UPLOAD");
		InputStream votable = null;
		String tableName = null;
		nbRows = 0;
		try{
			for(TableLoader loader : loaders){
				tableName = loader.tableName;
				votable = loader.openStream();

				if (nbBytesLimit > 0)
					votable = new LimitedSizeInputStream(votable, nbBytesLimit);

				// start parsing the VOTable:
				SavotPullParser parser = new SavotPullParser(votable, SavotPullEngine.SEQUENTIAL, null);

				SavotResource resource = parser.getNextResource();
				FieldSet fields = resource.getFieldSet(0);

				// 1st STEP: Convert the VOTable metadata into DBTable:
				TAPTable tapTable = fetchTableMeta(tableName, System.currentTimeMillis()+"", fields);
				uploadSchema.addTable(tapTable);

				// 2nd STEP: Create the corresponding table in the database:
				dbConn.createTable(tapTable);

				// 3rd STEP: Load rows into this table:
				SavotBinary binary = resource.getData(0).getBinary();
				if (binary != null)
					loadTable(tapTable, fields, binary);
				else
					loadTable(tapTable, fields, resource.getData(0).getTableData());

				votable.close();
			}
		}catch(DBException dbe){
			dbConn.cancelTransaction();	// ROLLBACK
			throw dbe;
		}catch(ExceededSizeException ese){
			dbConn.cancelTransaction();	// ROLLBACK
			throw new TAPException("Upload limit exceeded ! You can upload at most "+((nbBytesLimit > 0)?(nbBytesLimit+" bytes."):(nbRowsLimit+" rows.")));
		}catch(IOException ioe){
			dbConn.cancelTransaction(); // ROLLBACK
			throw new TAPException("Error while reading the VOTable of \""+tableName+"\" !", ioe);
		}catch(NullPointerException npe){
			dbConn.cancelTransaction();	// ROLLBACK
			if (votable != null && votable instanceof LimitedSizeInputStream)
				throw new TAPException("Upload limit exceeded ! You can upload at most "+((nbBytesLimit > 0)?(nbBytesLimit+" bytes."):(nbRowsLimit+" rows.")));
			else
				throw new TAPException(npe);
		}finally{
			try{
				if (votable != null)
					votable.close();
			}catch(IOException ioe){;}
		}

		// Commit modifications:
		try{
			dbConn.endTransaction();
		}finally{
			dbConn.close();
		}

		return uploadSchema;
	}

	private TAPTable fetchTableMeta(final String tableName, final String userId, final FieldSet fields){
		TAPTable tapTable = new TAPTable(tableName);
		tapTable.setDBName(tableName+"_"+userId);

		for(int j=0 ; j<fields.getItemCount(); j++){
			SavotField field = (SavotField)fields.getItemAt(j);
			int arraysize = TAPTypes.NO_SIZE;
			if (field.getArraySize() == null || field.getArraySize().trim().isEmpty())
				arraysize = 1;
			else if (field.getArraySize().equalsIgnoreCase("*"))
				arraysize = TAPTypes.STAR_SIZE;
			else{
				try{
					arraysize = Integer.parseInt(field.getArraySize());
				}catch(NumberFormatException nfe){
					service.log(LogType.WARNING, "Invalid array-size in the uploaded table \""+tableName+"\" for the field \""+field.getName()+"\": \""+field.getArraySize()+"\" ! It will be considered as \"*\" !");
				}
			}
			tapTable.addColumn(field.getName(), field.getDescription(), field.getUnit(), field.getUcd(), field.getUtype(), new VotType(field.getDataType(), arraysize, field.getXtype()), false, false, false);
		}

		return tapTable;
	}

	private int loadTable(final TAPTable tapTable, final FieldSet fields, final SavotBinary binary) throws TAPException, ExceededSizeException {
		// Read the raw binary data:
		DataBinaryReader reader = null;
		try{
			reader = new DataBinaryReader(binary.getStream(), fields, false);
			while(reader.next()){
				if (nbRowsLimit > 0 && nbRows >= nbRowsLimit)
					throw new ExceededSizeException();
				dbConn.insertRow(reader.getTR(), tapTable);
				nbRows++;
			}
		}catch(ExceededSizeException ese){
			throw ese;
		}catch(IOException se){
			throw new TAPException("Error while reading the binary data of the VOTable of \""+tapTable.getADQLName()+"\" !", se);
		}finally{
			try{
				if (reader != null)
					reader.close();
			}catch(IOException ioe){;}
		}

		return nbRows;
	}

	private int loadTable(final TAPTable tapTable, final FieldSet fields, final SavotTableData data) throws TAPException, ExceededSizeException {
		TRSet rows = data.getTRs();
		for(int i=0; i<rows.getItemCount(); i++){
			if (nbRowsLimit > 0 && nbRows >= nbRowsLimit)
				throw new ExceededSizeException();
			dbConn.insertRow((SavotTR)rows.getItemAt(i), tapTable);
			nbRows++;
		}

		return nbRows;
	}

}
