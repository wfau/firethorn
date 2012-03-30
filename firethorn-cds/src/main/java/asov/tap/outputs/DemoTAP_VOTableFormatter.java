package asov.tap.outputs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import cds.savot.writer.SavotWriter;

import adql.db.DBColumn;

import tap.ADQLExecutor;
import tap.ServiceConnection;
import tap.TAPException;

import tap.ServiceConnection.LogType;

import tap.metadata.TAPColumn;

public class DemoTAP_VOTableFormatter extends DemoTAP_Formatter {

	protected String votTableVersion = "1.2";
	protected String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";
	protected String xsiSchemaLocation = "http://www.ivoa.net/xml/VOTable/v1.2";
	protected String xsiNoNamespaceSchemaLocation = null;
	protected String xmlns = "http://www.ivoa.net/xml/VOTable/v1.2";

	private final ServiceConnection<ResultSet> service;

	public DemoTAP_VOTableFormatter(final ServiceConnection<ResultSet> service){
		this.service = service;
	}

	@Override
	public String getDescription() { return "VOTable format."; }
	@Override
	public String getFileExtension() { return ".vot"; }
	@Override
	public String getMimeType() { return "text/xml"; }
	@Override
	public String getShortMimeType() { return "votable"; }

	@Override
	public void writeResult(ResultSet queryResult, OutputStream output, ADQLExecutor<ResultSet> job) throws TAPException {
		try{
			long start = System.currentTimeMillis();

			PrintWriter out = new PrintWriter(output);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writeHeader(out, job);
			out.println("\t\t<TABLE>");
			DBColumn[] columns = writeMetadata(queryResult, out, job);
			out.println("\t\t\t<DATA>");
			out.flush();
			int nbRows = writeData(queryResult, columns, output, job);
			output.flush();
			out.println("\t\t\t</DATA>");
			out.println("\t\t</TABLE>");
			// OVERFLOW ?
			if (nbRows >= job.getMaxRec())
				out.println("\t\t<INFO name=\"QUERY_STATUS\" value=\"OVERFLOW\" />");
			out.println("\t</RESOURCE>");
			out.println("</VOTABLE>");
			out.flush();

			System.out.println("INFO: Job "+job.getJobId()+" - Result formatted (in VOTable ; "+nbRows+" rows ; "+columns.length+" columns) in "+(System.currentTimeMillis()-start)+" ms !");
		}catch(IOException ioe){
			ioe.printStackTrace();
			throw new TAPException("Error while writing a query result in VOTable !", ioe);
		}
	}

	protected void writeHeader(final PrintWriter output, final ADQLExecutor<ResultSet> job) throws IOException, TAPException {
		StringBuffer strBuf = new StringBuffer("<VOTABLE");
		if (votTableVersion != null)
			strBuf.append(" version=\"").append(SavotWriter.encodeAttribute(votTableVersion)).append('\"');
		if (xmlnsXsi != null)
			strBuf.append(" xmlns:xsi=\"").append(SavotWriter.encodeAttribute(xmlnsXsi)).append('\"');
		if (xsiSchemaLocation != null)
			strBuf.append(" xsi:schemaLocation=\"").append(SavotWriter.encodeAttribute(xsiSchemaLocation)).append('\"');
		if (xsiNoNamespaceSchemaLocation != null)
			strBuf.append(" xsi:noNamespaceSchemaLocation=\"").append(SavotWriter.encodeAttribute(xsiNoNamespaceSchemaLocation)).append('\"');
		if (xmlns != null)
			strBuf.append(" xmlns=\"").append(SavotWriter.encodeAttribute(xmlns)).append('\"');
		strBuf.append('>');
		output.println(strBuf);

		output.println("\t<RESOURCE type=\"results\">");

		// INFO items:
		output.println("\t\t<INFO name=\"QUERY_STATUS\" value=\"OK\" />");
		output.println("\t\t<INFO name=\"PROVIDER\" value=\""+SavotWriter.encodeAttribute(service.getProviderName())+"\">"+SavotWriter.encodeElement(service.getProviderDescription())+"</INFO>");
		output.println("\t\t<INFO name=\"QUERY\"><![CDATA["+job.getAdditionalParameterValue("query")+"]]></INFO>");
	}

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
						tapCol.setDatatype(meta.getColumnTypeName(indField), -1);
						service.log(LogType.WARNING, "Unknown DB datatype for the field \""+tapCol.getName()+"\" ! It is supposed to be \""+tapCol.getDatatype()+"\" (original value: \""+meta.getColumnTypeName(indField-1)+"\").");
						selectedColumns[indField-1] = tapCol;
					}
					writeFieldMeta(tapCol, output);
					indField++;
				}
			}
		} catch (SQLException e) {
			service.log(LogType.ERROR, "Job "+job.getJobId()+" - Impossible to get the metadata of the given ResultSet !");
			e.printStackTrace();
			output.println("<INFO name=\"WARNING\" value=\"MISSING_META\">Error while getting field(s) metadata</INFO>");
		}
		return selectedColumns;
	}

	protected void writeFieldMeta(TAPColumn col, PrintWriter out) throws IOException, TAPException {
		StringBuffer fieldline = new StringBuffer("\t\t\t");

		fieldline.append("<FIELD ID=").append('"').append(SavotWriter.encodeAttribute(col.getADQLName())).append('"');
		fieldline.append(" name=").append('"').append(SavotWriter.encodeAttribute(col.getADQLName())).append('"');

		if (col.getUcd() != null && col.getUcd().length() > 0)
			fieldline.append(" ucd=").append('"').append(SavotWriter.encodeAttribute(col.getUcd())).append('"');

		if (col.getUtype() != null && col.getUtype().length() > 0)
			fieldline.append(" utype=").append('"').append(SavotWriter.encodeAttribute(col.getUtype())).append('"');

		if (col.getUnit() != null && col.getUnit().length() > 0)
			fieldline.append(" unit=").append('"').append(SavotWriter.encodeAttribute(col.getUnit())).append('"');

		String description = null;
		if (col.getDescription() != null && !col.getDescription().trim().isEmpty())
			description = col.getDescription().trim();
		else
			description = null;

		if (description != null) {
			fieldline.append(">\n");
			if (description != null)
				fieldline.append("<DESCRIPTION>").append(SavotWriter.encodeElement(description)).append("</DESCRIPTION>\n");
			fieldline.append("</FIELD>");
			out.println(fieldline);
		} else {
			fieldline.append("/>");
			out.println(fieldline);
		}
	}

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
			e.printStackTrace();
			throw new TAPException("Job "+job.getJobId()+" - Impossible to get the "+(nbRows+1)+"-th rows from the given ResultSet !", e);
		}
	}

	protected void writeFieldValue(final Object value, final DBColumn column, final OutputStream output) throws IOException, TAPException {
		String fieldValue = (value==null)?null:value.toString();
		if (fieldValue == null && column instanceof TAPColumn)
			fieldValue = "";
		if (fieldValue != null)
			output.write(SavotWriter.encodeElement(fieldValue).getBytes());
	}



}
