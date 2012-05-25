package tap.formatter;

import java.io.IOException;

import tap.ADQLExecutor;
import tap.TAPException;

import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import tap.ServiceConnection;
import tap.ServiceConnection.LogType;

import tap.metadata.TAPColumn;
import tap.metadata.TAPTypes;

import adql.db.DBColumn;

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

/**
 * Formats a {@link ResultSet} into a VOTable.
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 11/2011
 */
public abstract class ResultSet2VotableFormatter extends VOTableFormat<ResultSet> {

	public ResultSet2VotableFormatter(final ServiceConnection<ResultSet> service) throws NullPointerException {
		super(service);
	}

	public ResultSet2VotableFormatter(final ServiceConnection<ResultSet> service, final boolean logFormatReport) throws NullPointerException {
		super(service, logFormatReport);
	}

	@Override
	protected DBColumn[] writeMetadata(final ResultSet queryResult, final PrintWriter output, final ADQLExecutor<ResultSet> job) throws IOException, TAPException {
		DBColumn[] selectedColumns = job.getSelectedColumns();
		try {
			ResultSetMetaData meta = queryResult.getMetaData();
			int indField = 1;
			if (selectedColumns != null){
				for(DBColumn field : selectedColumns){
					TAPColumn tapCol = null;
					try {
						tapCol = (TAPColumn)field;
					} catch(ClassCastException ex){
						tapCol = new TAPColumn(field.getADQLName());
						tapCol.setDatatype(meta.getColumnTypeName(indField), TAPTypes.NO_SIZE);
						service.log(LogType.WARNING, "Unknown DB datatype for the field \""+tapCol.getName()+"\" ! It is supposed to be \""+tapCol.getDatatype()+"\" (original value: \""+meta.getColumnTypeName(indField-1)+"\").");
						selectedColumns[indField-1] = tapCol;
					}
					writeFieldMeta(tapCol, output);
					indField++;
				}
			}
		} catch (SQLException e) {
			service.log(LogType.ERROR, "Job N°"+job.getJobId()+" - Impossible to get the metadata of the given ResultSet !");
			service.log(e);
			output.println("<INFO name=\"WARNING\" value=\"MISSING_META\">Error while getting field(s) metadata</INFO>");
		}
		return selectedColumns;
	}

	@Override
	protected int writeData(final ResultSet queryResult, final DBColumn[] selectedColumns, final OutputStream output, final ADQLExecutor<ResultSet> job) throws IOException, TAPException {
		int nbRows = 0;
		try {
			output.write("\t\t\t\t<TABLEDATA>\n".getBytes());
			int nbColumns = queryResult.getMetaData().getColumnCount();
			while(queryResult.next()){
				if (nbRows >= job.getMaxRec())
					break;

				output.write("\t\t\t\t\t<TR>\n".getBytes());
				for(int i=1; i<=nbColumns; i++){
					output.write("\t\t\t\t\t\t<TD>".getBytes());
					writeFieldValue(queryResult.getObject(i), selectedColumns[i-1], output);
					output.write("</TD>\n".getBytes());
				}

				output.write("\t\t\t\t\t</TR>\n".getBytes());
				nbRows++;
			}
			output.write("\t\t\t\t</TABLEDATA>\n".getBytes());
			return nbRows;
		} catch (SQLException e) {
			throw new TAPException("Job N°"+job.getJobId()+" - Impossible to get the "+(nbRows+1)+"-th rows from the given ResultSet !", e);
		}
	}
}
